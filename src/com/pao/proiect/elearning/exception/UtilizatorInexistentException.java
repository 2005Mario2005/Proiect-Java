package com.pao.proiect.elearning.exception;

// Cerinta 2.1: Cel putin 2 exceptii custom
public class UtilizatorInexistentException extends RuntimeException {

    public UtilizatorInexistentException(String mesaj) {
        super(mesaj);
    }
}