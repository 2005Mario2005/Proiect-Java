package com.pao.proiect.elearning.model;

public class Student extends Utilizator {

    private int anStudiu;

    public Student(Long id, String nume, String email, int anStudiu) {
        super(id, nume, email);
        this.anStudiu = anStudiu;
    }

    public int getAnStudiu() { return anStudiu; }
    public void setAnStudiu(int anStudiu) { this.anStudiu = anStudiu; }

    @Override
    public String toString() {
        return "[Student] " + super.toString() + " | An studiu: " + anStudiu;
    }

    @Override
    public String getRol() {
        return "STUDENT";
    }
}