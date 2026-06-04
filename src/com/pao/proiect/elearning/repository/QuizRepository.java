package com.pao.proiect.elearning.repository;

import com.pao.proiect.elearning.model.Curs;
import com.pao.proiect.elearning.model.Quiz;
import com.pao.proiect.elearning.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//repository-ul concret pentru tabela quizuri
public class QuizRepository implements Repository<Quiz, Long> {

    //este necesar acest repository pentru a reconstrui obiectul Curs asociat testului
    private final CursRepository cursRepository;

    public QuizRepository() {
        this.cursRepository = new CursRepository();
    }

    @Override
    public void save(Quiz entity) {
        String sql = "INSERT INTO quizuri (titlu, id_curs) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, entity.getTitlu());
            //se extrage ID-ul cursului de care apartine quiz-ul
            pstmt.setLong(2, entity.getCurs().getId());

            pstmt.executeUpdate();

            //se preia ID-ul auto-generat pentru quiz si se seteaza in obiect
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    entity.setId(generatedKeys.getLong(1));
                }
            }
        } catch (SQLException e) {
            System.out.println("Eroare la salvarea quiz-ului in baza de date: " + e.getMessage());
        }
    }

    @Override
    public Optional<Quiz> findById(Long id) {
        String sql = "SELECT * FROM quizuri WHERE id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToQuiz(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println("Eroare la cautarea quiz-ului in baza de date: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<Quiz> findAll() {
        List<Quiz> quizuri = new ArrayList<>();
        String sql = "SELECT * FROM quizuri";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                quizuri.add(mapResultSetToQuiz(rs));
            }
        } catch (SQLException e) {
            System.out.println("Eroare la preluarea quiz-urilor din baza de date: " + e.getMessage());
        }
        return quizuri;
    }

    @Override
    public void update(Quiz entity) {
        String sql = "UPDATE quizuri SET titlu = ?, id_curs = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, entity.getTitlu());
            pstmt.setLong(2, entity.getCurs().getId());
            pstmt.setLong(3, entity.getId());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Eroare la actualizarea quiz-ului in baza de date: " + e.getMessage());
        }
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM quizuri WHERE id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Eroare la stergerea quiz-ului din baza de date: " + e.getMessage());
        }
    }

    //metoda auxiliara pentru refacerea obiectului Quiz din inregistrarea de baza de date
    private Quiz mapResultSetToQuiz(ResultSet rs) throws SQLException {
        Long id = rs.getLong("id");
        String titlu = rs.getString("titlu");
        Long idCurs = rs.getLong("id_curs");

        //pentru a reconstrui obiectul Quiz, trebuie cautat si mapat intai Cursul
        Optional<Curs> cursOpt = cursRepository.findById(idCurs);
        if (cursOpt.isPresent()) {
            return new Quiz(id, titlu, cursOpt.get());
        } else {
            throw new SQLException("Cursul cu ID-ul " + idCurs + " asociat acestui quiz nu a fost gasit in baza de date!");
        }
    }
}