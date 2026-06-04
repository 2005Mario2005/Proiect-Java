package com.pao.proiect.elearning.model;

import java.util.Objects;

//clasa abstracta de baza pentru toti utilizatorii din sistem
public abstract class Utilizator {

    protected Long id;
    protected String nume;
    protected String email;

    public Utilizator(Long id, String nume, String email) {
        this.id = id;
        this.nume = nume;
        this.email = email;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNume() { return nume; }
    public void setNume(String nume) { this.nume = nume; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    //metodele equals si hashCode pentru a compara utilizatorii dupa ID
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

    @Override
    public String toString() {
        return "ID: " + id + " | Nume: " + nume + " | Email: " + email;
    }

    public abstract String getRol();
}