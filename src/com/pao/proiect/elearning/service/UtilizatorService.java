package com.pao.proiect.elearning.service;

import com.pao.proiect.elearning.exception.EmailDuplicatException;
import com.pao.proiect.elearning.exception.UtilizatorInexistentException;
import com.pao.proiect.elearning.model.Utilizator;
import com.pao.proiect.elearning.repository.UtilizatorRepository;

import java.util.List;
import java.util.Optional;

public class UtilizatorService {

    private static UtilizatorService instance;
    private final UtilizatorRepository utilizatorRepository;
    private final AuditService auditService;

    private UtilizatorService() {
        this.utilizatorRepository = new UtilizatorRepository();
        this.auditService = AuditService.getInstance();
    }

    public static UtilizatorService getInstance() {
        if (instance == null) {
            instance = new UtilizatorService();
        }
        return instance;
    }

    public void adaugaUtilizator(Utilizator u) {
        if (u == null) {
            System.out.println("Eroare: Utilizatorul este null!");
            return;
        }

        //se verifica existenta email-ului aducand lista curenta din baza de date
        List<Utilizator> existenti = utilizatorRepository.findAll();
        for (Utilizator existent : existenti) {
            if (existent.getEmail().equalsIgnoreCase(u.getEmail())) {
                throw new EmailDuplicatException("Email-ul " + u.getEmail() + " este deja folosit de alt utilizator!");
            }
        }

        //se salveaza utilizatorul in baza de date (ID-ul va fi generat si setat de repository)
        utilizatorRepository.save(u);

        //este inregistrata actiunea in fisierul de audit
        auditService.logheazaActiune("adauga_utilizator");
        System.out.println("Utilizator adaugat cu succes in DB: " + u.toString());
    }

    public Utilizator cautaDupaId(Long id) {
        Optional<Utilizator> utilizatorOpt = utilizatorRepository.findById(id);

        if (utilizatorOpt.isEmpty()) {
            throw new UtilizatorInexistentException("Nu exista niciun utilizator in DB cu ID-ul: " + id);
        }

        auditService.logheazaActiune("cauta_utilizator_id");
        return utilizatorOpt.get();
    }

    public void stergeUtilizator(Long id) {
        Utilizator u = cautaDupaId(id);
        utilizatorRepository.delete(id);

        auditService.logheazaActiune("sterge_utilizator");
        System.out.println("Utilizatorul " + u.getNume() + " (ID: " + id + ") a fost sters definitiv.");
    }

    public List<Utilizator> getTotiUtilizatorii() {
        auditService.logheazaActiune("listeaza_utilizatori");
        return utilizatorRepository.findAll();
    }
}