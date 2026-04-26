package com.pao.proiect.elearning.model;

public class Lectie {
    private Long id;
    private String titlu;
    private Curs curs; // de ce curs apartine lectia

    public Lectie(Long id, String titlu, Curs curs) {
        this.id = id;
        this.titlu = titlu;
        this.curs = curs;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitlu() { return titlu; }
    public void setTitlu(String titlu) { this.titlu = titlu; }

    public Curs getCurs() { return curs; }
    public void setCurs(Curs curs) { this.curs = curs; }

    @Override
    public String toString() {
        return "Lectie: " + titlu;
    }
}