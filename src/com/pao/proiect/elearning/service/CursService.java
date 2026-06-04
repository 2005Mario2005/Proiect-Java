package com.pao.proiect.elearning.service;

import com.pao.proiect.elearning.exception.NotaInvalidaException;
import com.pao.proiect.elearning.exception.StudentDejaInscrisException;
import com.pao.proiect.elearning.model.*;
import com.pao.proiect.elearning.repository.CursRepository;
import com.pao.proiect.elearning.repository.LectieRepository;
import com.pao.proiect.elearning.repository.QuizRepository;
import com.pao.proiect.elearning.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class CursService {

    private static CursService instance;
    private final CursRepository cursRepository;
    private final LectieRepository lectieRepository;
    private final QuizRepository quizRepository;
    private final AuditService auditService;

    private CursService() {
        this.cursRepository = new CursRepository();
        this.lectieRepository = new LectieRepository();
        this.quizRepository = new QuizRepository();
        this.auditService = AuditService.getInstance();
    }

    public static CursService getInstance() {
        if (instance == null) {
            instance = new CursService();
        }
        return instance;
    }

    public void adaugaCurs(Curs curs) {
        if (curs == null) return;
        cursRepository.save(curs);
        auditService.logheazaActiune("adauga_curs");
        System.out.println("Curs salvat cu succes in DB: " + curs.toString());
    }

    public Curs cautaCursDupaId(Long id) {
        Optional<Curs> cursOpt = cursRepository.findById(id);
        if (cursOpt.isEmpty()) {
            throw new RuntimeException("Nu exista niciun curs cu ID-ul: " + id);
        }
        auditService.logheazaActiune("cauta_curs_id");
        return cursOpt.get();
    }

    public Quiz cautaQuizDupaId(Long idQuiz) {
        Optional<Quiz> quizOpt = quizRepository.findById(idQuiz);
        if (quizOpt.isEmpty()) {
            throw new RuntimeException("Nu exista niciun quiz in DB cu ID-ul: " + idQuiz);
        }
        auditService.logheazaActiune("cauta_quiz_id");
        return quizOpt.get();
    }

    public void adaugaLectieLaCurs(Lectie lectie, Curs curs) {
        lectieRepository.save(lectie);
        auditService.logheazaActiune("adauga_lectie");
        System.out.println("Lectie adaugata in DB: " + lectie.toString());
    }

    public void adaugaQuizLaCurs(Quiz quiz, Curs curs) {
        quizRepository.save(quiz);
        auditService.logheazaActiune("adauga_quiz");
        System.out.println("Quiz adaugat in DB: " + quiz.toString());
    }

    //tranzactie explicita (commit / rollback)
    public void inscrieStudent(Curs curs, Student student) {
        String insertSql = "INSERT INTO inscrieri (id_curs, id_student) VALUES (?, ?)";
        Connection conn = null;

        try {
            conn = DatabaseConnection.getInstance().getConnection();
            conn.setAutoCommit(false); // (II) tranzactie explicita JDBC

            try (PreparedStatement pstmtInsert = conn.prepareStatement(insertSql)) {
                pstmtInsert.setLong(1, curs.getId());
                pstmtInsert.setLong(2, student.getId());
                pstmtInsert.executeUpdate();
            }

            conn.commit(); //(II) in caz de succes
            auditService.logheazaActiune("inscrie_student_curs");
            System.out.println(student.getNume() + " s-a inscris la " + curs.getTitlu());

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); //(II) in caz de eroare
                } catch (SQLException ex) {
                    System.out.println("Eroare la rollback: " + ex.getMessage());
                }
            }
            throw new StudentDejaInscrisException("Studentul este deja inscris sau a aparut o eroare de DB.");
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); //resetare auto-commit la starea initiala
                } catch (SQLException e) {
                    System.out.println("Eroare resetare conexiune.");
                }
            }
        }
    }

    public void salveazaRezultat(Student student, Quiz quiz, double nota) {
        if (nota < 1 || nota > 10) {
            throw new NotaInvalidaException("Nota " + nota + " nu este permisa. Trebuie sa fie intre 1 si 10.");
        }

        String sql = "INSERT INTO rezultate (id_student, id_quiz, nota) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, student.getId());
            pstmt.setLong(2, quiz.getId());
            pstmt.setDouble(3, nota);
            pstmt.executeUpdate();

            auditService.logheazaActiune("salveaza_rezultat_quiz");
            System.out.println("Nota salvata in DB pentru studentul " + student.getNume() + " la quiz-ul " + quiz.getTitlu());

        } catch (SQLException e) {
            System.out.println("Eroare la salvarea notei in baza de date: " + e.getMessage());
        }
    }

    public List<Curs> getToateCursurile() {
        auditService.logheazaActiune("listeaza_cursuri");
        return cursRepository.findAll();
    }

    public List<Curs> getCursuriSortateAlfabetic() {
        auditService.logheazaActiune("listeaza_cursuri_sortate");
        List<Curs> cursuri = cursRepository.findAll();
        cursuri.sort(Comparator.comparing(Curs::getTitlu, String.CASE_INSENSITIVE_ORDER));
        return cursuri;
    }

    public List<Curs> getCursuriProfesor(Profesor profesor) {
        auditService.logheazaActiune("listeaza_cursuri_profesor");
        List<Curs> cursurileLui = new ArrayList<>();
        for (Curs c : cursRepository.findAll()) {
            if (c.getProfesor().getId().equals(profesor.getId())) {
                cursurileLui.add(c);
            }
        }
        return cursurileLui;
    }

    //(III) JOIN 1/3
    public List<RezultatQuiz> getIstoricStudent(Student student) {
        auditService.logheazaActiune("vizualizare_istoric_student");
        List<RezultatQuiz> istoric = new ArrayList<>();

        String sql = "SELECT r.id, r.nota, q.id AS q_id, q.titlu AS q_titlu, q.id_curs " +
                "FROM rezultate r JOIN quizuri q ON r.id_quiz = q.id WHERE r.id_student = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, student.getId());

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Long idRezultat = rs.getLong("id");
                    double nota = rs.getDouble("nota");
                    Quiz quizDinJoin = new Quiz(rs.getLong("q_id"), rs.getString("q_titlu"),
                            new Curs(rs.getLong("id_curs"), "Curs implicit", null));

                    istoric.add(new RezultatQuiz(idRezultat, student, quizDinJoin, nota));
                }
            }
        } catch (SQLException e) {
            System.out.println("Eroare JOIN istoric: " + e.getMessage());
        }
        return istoric;
    }
}