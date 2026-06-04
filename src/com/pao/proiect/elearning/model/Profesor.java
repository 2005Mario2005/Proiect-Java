package com.pao.proiect.elearning.model;

//clasa care mosteneste Utilizator si adauga atribute specifice profesorului
public class Profesor extends Utilizator {

    private String departament;

    public Profesor(Long id, String nume, String email, String departament) {
        super(id, nume, email);
        this.departament = departament;
    }

    public String getDepartament() { return departament; }
    public void setDepartament(String departament) { this.departament = departament; }

    @Override
    public String toString() {
        return "[Profesor] " + super.toString() + " | Departament: " + departament;
    }

    @Override
    public String getRol() {
        return "PROFESOR";
    }
}