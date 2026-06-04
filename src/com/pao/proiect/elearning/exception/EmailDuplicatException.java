package com.pao.proiect.elearning.exception;

public class EmailDuplicatException extends RuntimeException {

    public EmailDuplicatException(String mesaj) {
        super(mesaj);
    }
}