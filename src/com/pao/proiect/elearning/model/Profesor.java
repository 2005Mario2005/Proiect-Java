package com.pao.proiect.elearning.model;

// Cerinta 2.1: Ierarhie de mostenire (nivelul 2)
public class Profesor extends Utilizator {

    private String departament;

    public Profesor(Long id, String nume, String email, String departament) {
        super(id, nume, email);
        this.departament = departament;
    }

    // Cerinta 2.1: Getteri si setteri
    public String getDepartament() { return departament; }
    public void setDepartament(String departament) { this.departament = departament; }

    // Cerinta 2.1: Metoda toString suprascrisa
    @Override
    public String toString() {
        return "Profesor [" + super.toString() + ", Departament: " + departament + "]";
    }

    @Override
    public String getRol() {
        return "PROFESOR";
    }
}