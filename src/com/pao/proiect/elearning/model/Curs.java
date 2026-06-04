package com.pao.proiect.elearning.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

//clasa care modeleaza un curs si contine liste pentru a nu pierde modulele adaugate
public class Curs {

    private Long id;
    private String titlu;
    private Profesor profesor;

    //sunt adaugate liste pentru a pastra referinte catre lectiile si quiz-urile cursului
    private List<Lectie> lectii;
    private List<Quiz> quizuri;

    public Curs(Long id, String titlu, Profesor profesor) {
        this.id = id;
        this.titlu = titlu;
        this.profesor = profesor;
        //se initializeaza listele goale la crearea obiectului
        this.lectii = new ArrayList<>();
        this.quizuri = new ArrayList<>();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitlu() { return titlu; }
    public void setTitlu(String titlu) { this.titlu = titlu; }

    public Profesor getProfesor() { return profesor; }
    public void setProfesor(Profesor profesor) { this.profesor = profesor; }

    public List<Lectie> getLectii() { return lectii; }
    public List<Quiz> getQuizuri() { return quizuri; }

    //sunt expuse metode pentru adaugarea elementelor in liste
    public void adaugaLectie(Lectie lectie) {
        this.lectii.add(lectie);
    }

    public void adaugaQuiz(Quiz quiz) {
        this.quizuri.add(quiz);
    }

    //sunt utilizate metodele equals si hashCode pentru compararea dupa ID
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Curs curs = (Curs) o;
        return Objects.equals(id, curs.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "[Curs] ID: " + id + " | Titlu: " + titlu + " | Predat de: " + profesor.getNume();
    }
}