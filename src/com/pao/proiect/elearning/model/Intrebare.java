package com.pao.proiect.elearning.model;

public class Intrebare {
    private Long id;
    private String text;
    private Quiz quiz; // din ce quiz face parte

    public Intrebare(Long id, String text, Quiz quiz) {
        this.id = id;
        this.text = text;
        this.quiz = quiz;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public Quiz getQuiz() { return quiz; }
    public void setQuiz(Quiz quiz) { this.quiz = quiz; }

    @Override
    public String toString() {
        return "Intrebare: " + text;
    }
}