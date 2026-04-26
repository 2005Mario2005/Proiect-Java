package com.pao.proiect.elearning.model;

// Cerinta 2.1: Ierarhie de mostenire (nivelul 2)
public class Student extends Utilizator {

    // Cerinta 2.1: Atribute private specifice
    private int anStudiu;

    public Student(Long id, String nume, String email, int anStudiu) {
        // super(...) apeleaza constructorul clasei parinte (Utilizator)
        super(id, nume, email);
        this.anStudiu = anStudiu;
    }

    // Cerinta 2.1: Getteri si setteri
    public int getAnStudiu() { return anStudiu; }
    public void setAnStudiu(int anStudiu) { this.anStudiu = anStudiu; }

    // Cerinta 2.1: Metoda toString suprascrisa
    @Override
    public String toString() {
        return "Student [" + super.toString() + ", An: " + anStudiu + "]";
    }

    @Override
    public String getRol() {
        return "STUDENT";
    }
}