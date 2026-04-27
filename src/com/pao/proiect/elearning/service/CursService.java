package com.pao.proiect.elearning.service;

import com.pao.proiect.elearning.exception.NotaInvalidaException;
import com.pao.proiect.elearning.exception.StudentDejaInscrisException;
import com.pao.proiect.elearning.model.*;

import java.util.*;

// Cerinta 2.3: Al doilea serviciu
public class CursService {

    // Cerinta 2.3: Singleton
    private static CursService instance;

    // toate cursurile platformei
    private final List<Curs> listaCursuri;

    // map pentru evidenta inscrierilor: cheia este ID-ul cursului -> Set-ul de studenti inscrisi
    // utilizez Set pentru a nu permite duplicate (Cerinta 2.2: Colectii multiple)
    private final Map<Long, Set<Student>> inscrieriMap;

    // istoricul notelor
    private final List<RezultatQuiz> rezultateSistem;

    private CursService() {
        this.listaCursuri = new ArrayList<>();
        this.inscrieriMap = new HashMap<>();
        this.rezultateSistem = new ArrayList<>();
    }

    public static CursService getInstance() {
        if (instance == null) {
            instance = new CursService();
        }
        return instance;
    }

    // Cerinta 2.3: Operatie CRUD - Adaugare curs
    public void adaugaCurs(Curs curs) {
        if (curs == null || curs.getId() == null) return;
        listaCursuri.add(curs);

        // i se atribuie automat o lista goala de studenti
        inscrieriMap.put(curs.getId(), new HashSet<>());
        System.out.println("Curs creat: " + curs.getTitlu());
    }

    // Cerinta 2.3: Operatie CRUD - Cautare curs dupa ID
    public Curs cautaCursDupaId(Long id) {
        for (Curs c : listaCursuri) {
            if (c.getId().equals(id)) {
                return c;
            }
        }
        // daca nu exista cursul
        throw new RuntimeException("Nu exista niciun curs cu ID-ul: " + id);
    }

    // Cerinta 2.3: Operatie CRUD - Stergere curs
    public void stergeCurs(Long id) {
        // utilizez metoda de cautare ca sa verific daca exista
        Curs cursDeSters = cautaCursDupaId(id);

        listaCursuri.remove(cursDeSters);
        inscrieriMap.remove(id); // sterg si evidenta studentilor la acest curs

        System.out.println("Cursul a fost sters cu succes: " + cursDeSters.getTitlu());
    }

    // Cerinta 2.3: Operatie CRUD - Listarea tuturor cursurilor
    public List<Curs> getToateCursurile() {
        return listaCursuri;
    }

    // Operatii specifice platformei

    // inscriere Student la Curs
    public void inscrieStudent(Curs curs, Student student) {
        Set<Student> inscrisi = inscrieriMap.get(curs.getId());

        // Cerinta 2.1: verific daca studentul e deja inscris la curs
        if (inscrisi.contains(student)) {
            throw new StudentDejaInscrisException("Studentul " + student.getNume() + " este deja la cursul " + curs.getTitlu());
        }

        inscrisi.add(student);
        System.out.println(student.getNume() + " s-a inscris cu succes la " + curs.getTitlu());
    }

    // salvare nota
    public void salveazaRezultat(RezultatQuiz rezultat) {
        // Cerinta 2.1: verific ca nota pusa e valida
        if (rezultat.getNota() < 1 || rezultat.getNota() > 10) {
            throw new NotaInvalidaException("Nota " + rezultat.getNota() + " nu este permisa. Trebuie sa fie intre 1 si 10.");
        }
        rezultateSistem.add(rezultat);
        System.out.println("Nota " + rezultat.getNota() + " a fost salvata pentru " + rezultat.getStudent().getNume());
    }

    // Cerinta 2.2: Colectie sortata (cursurile sortate alfabetic)
    public List<Curs> getCursuriSortateAlfabetic() {
        // copie a listei pentru a nu strica ordinea originala din sistem
        List<Curs> cursuriSortate = new ArrayList<>(listaCursuri);

        // metoda de sortare apelata folosind titlul cursului ca referinta
        cursuriSortate.sort(Comparator.comparing(Curs::getTitlu, String.CASE_INSENSITIVE_ORDER));

        return cursuriSortate;
    }

    // afisarea cursurilor predate de un profesor
    public List<Curs> getCursuriProfesor(Profesor profesor) {
        List<Curs> cursurileLui = new ArrayList<>();

        for (Curs c : listaCursuri) {
            if (c.getProfesor().equals(profesor)) { // compara dupa ID
                cursurileLui.add(c);
            }
        }
        return cursurileLui;
    }

    // istoric note student
    public List<RezultatQuiz> getIstoricStudent(Student student) {
        List<RezultatQuiz> istoric = new ArrayList<>();
        for (RezultatQuiz r : rezultateSistem) {
            if (r.getStudent().equals(student)) {
                istoric.add(r);
            }
        }
        return istoric;
    }
}