package com.pao.proiect.elearning;

import com.pao.proiect.elearning.model.*;
import com.pao.proiect.elearning.service.CursService;
import com.pao.proiect.elearning.service.UtilizatorService;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        // serviciile
        UtilizatorService utilizatorService = UtilizatorService.getInstance();
        CursService cursService = CursService.getInstance();

        // datele "dummy" la inceput pentru a evita erorile daca userul apasa butoanele in ordine gresita
        Profesor profJava = new Profesor(1L, "Ion Popescu", "ion@facultate.ro", "Informatica");
        Student studentAlex = new Student(2L, "Alexandru Vasile", "alex@student.ro", 2);
        Curs cursJava = new Curs(100L, "Programare Java", profJava);
        Quiz quizJava = new Quiz(50L, "Test Sintaxa", cursJava);

        // Scanner-ul pentru a citi de la tastatura
        Scanner scanner = new Scanner(System.in);
        int optiune = -1;

        // meniul
        while (optiune != 0) {
            System.out.println("\n=========================================");
            System.out.println("      PLATFORMA E-LEARNING - MENIU       ");
            System.out.println("=========================================");
            System.out.println("1. Adauga un nou profesor");
            System.out.println("2. Adauga un nou student");
            System.out.println("3. Creeaza un curs nou");
            System.out.println("4. Adauga o lectie la curs");
            System.out.println("5. Inscrie studentul la curs");
            System.out.println("6. Adauga un quiz pentru curs");
            System.out.println("7. Salveaza nota studentului la quiz");
            System.out.println("8. Listeaza toate cursurile (alfabetic)");
            System.out.println("9. Afiseaza istoricul notelor studentului");
            System.out.println("10. Listeaza cursurile profesorului");
            System.out.println("0. Iesire din aplicatie");
            System.out.println("=========================================");
            System.out.print("Alege o optiune: ");

            try {
                // citirea inputului ca text si transformarea in int (mai sigur decat nextInt)
                optiune = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Te rog sa introduci un numar valid!");
                continue; // sare peste restul codului si reia bucla
            }

            System.out.println(); // un rand liber pentru estetica

            switch (optiune) {
                case 1:
                    utilizatorService.adaugaUtilizator(profJava);
                    break;
                case 2:
                    utilizatorService.adaugaUtilizator(studentAlex);
                    break;
                case 3:
                    cursService.adaugaCurs(cursJava);
                    break;
                case 4:
                    Lectie lectie = new Lectie(10L, "Variabile in Java", cursJava);
                    System.out.println("Lectie adaugata: " + lectie.getTitlu());
                    break;
                case 5:
                    try {
                        cursService.inscrieStudent(cursJava, studentAlex);
                    } catch (Exception e) {
                        // exceptia custom in caz ca se inscrie de 2 ori
                        System.out.println("EROARE: " + e.getMessage());
                    }
                    break;
                case 6:
                    Intrebare intrebare = new Intrebare(1L, "Ce este o clasa?", quizJava);
                    System.out.println("Quiz adaugat: " + quizJava.getTitlu() + " (Intrebare: " + intrebare.getText() + ")");
                    break;
                case 7:
                    try {
                        RezultatQuiz rezultat = new RezultatQuiz(1L, studentAlex, quizJava.getTitlu(), 9.50);
                        cursService.salveazaRezultat(rezultat);
                    } catch (Exception e) {
                        System.out.println("EROARE: " + e.getMessage());
                    }
                    break;
                case 8:
                    List<Curs> cursuriSortate = cursService.getCursuriSortateAlfabetic();
                    System.out.println("--- Cursuri Platforma ---");
                    for (Curs c : cursuriSortate) {
                        System.out.println("- " + c.getTitlu());
                    }
                    break;
                case 9:
                    List<RezultatQuiz> istoric = cursService.getIstoricStudent(studentAlex);
                    System.out.println("--- Istoric note " + studentAlex.getNume() + " ---");
                    for (RezultatQuiz r : istoric) {
                        System.out.println("- " + r.getNumeQuiz() + ": " + r.getNota());
                    }
                    break;
                case 10:
                    List<Curs> cursurileLuiIon = cursService.getCursuriProfesor(profJava);
                    System.out.println("--- Cursurile domnului " + profJava.getNume() + " ---");
                    for (Curs c : cursurileLuiIon) {
                        System.out.println("- " + c.getTitlu());
                    }
                    break;
                case 0:
                    System.out.println("Iesire din platforma... La revedere!");
                    break;
                default:
                    System.out.println("Optiunea nu exista! Te rog alege un numar intre 0 si 10.");
                    break;
            }
        }

        // inshiderea scanner-ului pentru a elibera resursele calculatorului
        scanner.close();
    }
}