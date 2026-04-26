package com.pao.proiect.elearning.service;

import com.pao.proiect.elearning.exception.UtilizatorInexistentException;
import com.pao.proiect.elearning.model.Utilizator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Cerinta 2.3: Cel putin 2 clase de serviciu
public class UtilizatorService {

    // Cerinta 2.3: Singleton - instanta unica, statica si privata
    private static UtilizatorService instance;

    // Cerinta 2.2: Cel putin un Map folosit pentru indexare
    // retin toti utilizatorii (cheia e ID-ul, valoarea e obiectul Utilizator)
    private final Map<Long, Utilizator> utilizatoriMap;

    // Cerinta 2.3: Singleton - constructor privat
    private UtilizatorService() {
        this.utilizatoriMap = new HashMap<>();
    }

    // Cerinta 2.3: Singleton - metoda statica getInstance()
    public static UtilizatorService getInstance() {
        if (instance == null) {
            instance = new UtilizatorService();
        }
        return instance;
    }

    // Cerinta 2.3: Operatie CRUD - Adaugare
    public void adaugaUtilizator(Utilizator u) {
        // Cerinta 2.4: Fara NullPointerException
        if (u == null || u.getId() == null) {
            System.out.println("Eroare: Utilizatorul sau ID-ul este null!");
            return;
        }
        utilizatoriMap.put(u.getId(), u);
        System.out.println("Utilizator adaugat cu succes: " + u.getNume());
    }

    // Cerinta 2.3: Operatie CRUD - Cautare dupa ID
    public Utilizator cautaDupaId(Long id) {
        Utilizator u = utilizatoriMap.get(id);
        if (u == null) {
            // Cerinta 2.1: exceptia custom daca nu exista persoana
            throw new UtilizatorInexistentException("Nu exista niciun utilizator cu ID-ul: " + id);
        }
        return u;
    }

    // Cerinta 2.3: Operatie CRUD - Stergere
    public void stergeUtilizator(Long id) {
        // Verificam intai daca exista in Map
        if (!utilizatoriMap.containsKey(id)) {
            throw new UtilizatorInexistentException("Nu se poate sterge. ID inexistent: " + id);
        }
        utilizatoriMap.remove(id);
        System.out.println("Utilizatorul cu ID-ul " + id + " a fost sters.");
    }

    // Cerinta 2.3: Operatie CRUD - Listare toate
    // valorile din Map sub forma de lista
    public List<Utilizator> getTotiUtilizatorii() {
        return new ArrayList<>(utilizatoriMap.values());
    }
}