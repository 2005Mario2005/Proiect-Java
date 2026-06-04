package com.pao.proiect.elearning.model;

//clasa imutabila care stocheaza nota obtinuta de un student la un quiz
public final class RezultatQuiz {

    //variabilele sunt declarate final pentru a asigura imutabilitatea obiectului
    private final Long id;
    private final Student student;
    private final Quiz quiz;
    private final double nota;

    public RezultatQuiz(Long id, Student student, Quiz quiz, double nota) {
        this.id = id;
        this.student = student;
        this.quiz = quiz;
        this.nota = nota;
    }

    public Long getId() { return id; }
    public Student getStudent() { return student; }
    public Quiz getQuiz() { return quiz; }
    public double getNota() { return nota; }

    @Override
    public String toString() {
        return "[Rezultat] ID: " + id + " | Student: " + student.getNume() + " | Quiz: " + quiz.getTitlu() + " | Nota: " + nota;
    }
}