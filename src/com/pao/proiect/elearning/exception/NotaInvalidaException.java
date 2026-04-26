package com.pao.proiect.elearning.exception;

// Cerinta 2.1: Cel putin 2 exceptii custom
public class NotaInvalidaException extends RuntimeException {

    public NotaInvalidaException(String mesaj) {
        super(mesaj);
    }
}