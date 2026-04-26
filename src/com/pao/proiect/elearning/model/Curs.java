package com.pao.proiect.elearning.model;

import java.util.Objects;

public class Curs {
    // Cerinta 2.1: Atribute private pentru incapsulare
    private Long id;
    private String titlu;
    private Profesor profesor; // relatie de compozitie (un curs are un profesor)

    // constructor pentru a initializa obiectul complet la creare
    public Curs(Long id, String titlu, Profesor profesor) {
        this.id = id;
        this.titlu = titlu;
        this.profesor = profesor;
    }

    // Cerinta 2.1: Getteri si setteri
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitlu() { return titlu; }
    public void setTitlu(String titlu) { this.titlu = titlu; }

    public Profesor getProfesor() { return profesor; }
    public void setProfesor(Profesor profesor) { this.profesor = profesor; }

    // Cerinta 2.1: A doua clasa cu equals si hashCode suprascrise (comparam dupa ID)
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

    // Cerinta 2.1: Metoda toString suprascrisa
    @Override
    public String toString() {
        return "Curs [ID: " + id + ", Titlu: " + titlu + ", Predat de: " + profesor.getNume() + "]";
    }
}