package com.pao.proiect.elearning.exception;

// Cerinta 2.1: Cel putin 2 exceptii custom
public class StudentDejaInscrisException extends RuntimeException {

    public StudentDejaInscrisException(String mesaj) {
        super(mesaj);
    }
}