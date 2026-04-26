package com.pao.proiect.elearning.model;

import java.util.Objects;

// Cerinta 2.1: Cel putin o clasa abstracta in ierarhie
public abstract class Utilizator {

    // Cerinta 2.1: Atribute protected pentru a fi vazute in clasele copil
    protected Long id;
    protected String nume;
    protected String email;

    public Utilizator(Long id, String nume, String email) {
        this.id = id;
        this.nume = nume;
        this.email = email;
    }

    // Cerinta 2.1: Getteri si setteri
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNume() { return nume; }
    public void setNume(String nume) { this.nume = nume; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    // Cerinta 2.1: Metode equals si hashCode suprascrise
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Utilizator that = (Utilizator) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // Cerinta 2.1: Metoda toString suprascrisa
    @Override
    public String toString() {
        return "ID: " + id + ", Nume: " + nume + ", Email: " + email;
    }

    // metoda abstracta pe care copiii sunt obligati sa o implementeze
    public abstract String getRol();
}