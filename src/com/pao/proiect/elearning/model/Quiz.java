package com.pao.proiect.elearning.model;

import java.util.ArrayList;
import java.util.List;

//clasa care modeleaza un test asociat unui curs
public class Quiz {

    private Long id;
    private String titlu;
    private Curs curs;

    //lista pentru a pastra intrebarile care apartin de acest quiz
    private List<Intrebare> intrebari;

    public Quiz(Long id, String titlu, Curs curs) {
        this.id = id;
        this.titlu = titlu;
        this.curs = curs;
        //se initializeaza lista goala la crearea obiectului
        this.intrebari = new ArrayList<>();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitlu() { return titlu; }
    public void setTitlu(String titlu) { this.titlu = titlu; }

    public Curs getCurs() { return curs; }
    public void setCurs(Curs curs) { this.curs = curs; }

    public List<Intrebare> getIntrebari() { return intrebari; }

    public void adaugaIntrebare(Intrebare intrebare) {
        this.intrebari.add(intrebare);
    }

    @Override
    public String toString() {
        return "[Quiz] ID: " + id + " | Titlu: " + titlu + " | Curs: " + curs.getTitlu();
    }
}