package com.pao.proiect.elearning.service;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AuditService {

    private static AuditService instance;
    private final String fisierAudit = "audit.csv";
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private AuditService() {
        try (FileWriter fw = new FileWriter(fisierAudit, true);
             PrintWriter pw = new PrintWriter(fw)) {
        } catch (IOException e) {
            System.out.println("Eroare la initializarea fisierului de audit: " + e.getMessage());
        }
    }

    public static AuditService getInstance() {
        if (instance == null) {
            instance = new AuditService();
        }
        return instance;
    }

    //'synchronized' pentru a face scrierea thread-safe
    public synchronized void logheazaActiune(String numeActiune) {
        try (FileWriter fw = new FileWriter(fisierAudit, true);
             PrintWriter pw = new PrintWriter(fw)) {

            String timestamp = LocalDateTime.now().format(formatter);
            pw.println(numeActiune + "," + timestamp);

        } catch (IOException e) {
            System.out.println("Eroare la scrierea in fisierul de audit: " + e.getMessage());
        }
    }
}