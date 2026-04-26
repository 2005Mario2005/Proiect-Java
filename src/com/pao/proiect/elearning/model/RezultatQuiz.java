package com.pao.proiect.elearning.model;

// Cerinta 2.1: Clasa imutabila (final)
public final class RezultatQuiz {
    // variabile final - primesc valoare doar o data
    private final Long id;
    private final Student student;
    private final String numeQuiz;
    private final double nota;

    public RezultatQuiz(Long id, Student student, String numeQuiz, double nota) {
        this.id = id;
        this.student = student;
        this.numeQuiz = numeQuiz;
        this.nota = nota;
    }

    // doar getteri, FARA setteri - datele nu pot fi modificate
    public Long getId() { return id; }
    public Student getStudent() { return student; }
    public String getNumeQuiz() { return numeQuiz; }
    public double getNota() { return nota; }

    @Override
    public String toString() {
        return "Rezultat: " + student.getNume() + " a luat " + nota + " la " + numeQuiz;
    }
}