package com.pao.proiect.elearning;

import com.pao.proiect.elearning.model.*;
import com.pao.proiect.elearning.service.CursService;
import com.pao.proiect.elearning.service.UtilizatorService;
import com.pao.proiect.elearning.exception.NotaInvalidaException;
import com.pao.proiect.elearning.exception.StudentDejaInscrisException;
import com.pao.proiect.elearning.exception.UtilizatorInexistentException;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        UtilizatorService utilizatorService = UtilizatorService.getInstance();
        CursService cursService = CursService.getInstance();

        Scanner scanner = new Scanner(System.in);
        int optiune = -1;

        while (optiune != 0) {
            System.out.println("\n=========================================");
            System.out.println("      PLATFORMA E-LEARNING - MENIU       ");
            System.out.println("=========================================");
            System.out.println("1. Adauga un nou profesor");
            System.out.println("2. Adauga un nou student");
            System.out.println("3. Creeaza un curs nou");
            System.out.println("4. Adauga o lectie la un curs");
            System.out.println("5. Inscrie un student la un curs");
            System.out.println("6. Adauga un quiz pentru un curs");
            System.out.println("7. Salveaza nota studentului la quiz");
            System.out.println("8. Listeaza toate cursurile (alfabetic)");
            System.out.println("9. Afiseaza istoricul notelor unui student");
            System.out.println("10. Listeaza cursurile unui profesor");
            System.out.println("0. Iesire din aplicatie");
            System.out.println("=========================================");
            System.out.print("Alege o optiune: ");

            try {
                optiune = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Te rog sa introduci un numar valid pentru meniu!");
                continue;
            }

            System.out.println();

            try {
                switch (optiune) {
                    case 1:
                        System.out.print("ID Profesor: ");
                        Long idProf = Long.parseLong(scanner.nextLine());
                        System.out.print("Nume: ");
                        String numeProf = scanner.nextLine();
                        System.out.print("Email: ");
                        String emailProf = scanner.nextLine();
                        System.out.print("Departament: ");
                        String depProf = scanner.nextLine();

                        Profesor p = new Profesor(idProf, numeProf, emailProf, depProf);
                        utilizatorService.adaugaUtilizator(p);
                        break;

                    case 2:
                        System.out.print("ID Student: ");
                        Long idStud = Long.parseLong(scanner.nextLine());
                        System.out.print("Nume: ");
                        String numeStud = scanner.nextLine();
                        System.out.print("Email: ");
                        String emailStud = scanner.nextLine();
                        System.out.print("An studiu (numar): ");
                        int anStud = Integer.parseInt(scanner.nextLine());

                        Student s = new Student(idStud, numeStud, emailStud, anStud);
                        utilizatorService.adaugaUtilizator(s);
                        break;

                    case 3:
                        System.out.print("ID Curs nou: ");
                        Long idCurs = Long.parseLong(scanner.nextLine());
                        System.out.print("Titlu Curs: ");
                        String titluCurs = scanner.nextLine();
                        System.out.print("ID Profesor titular: ");
                        Long idProfTitular = Long.parseLong(scanner.nextLine());

                        Utilizator u = utilizatorService.cautaDupaId(idProfTitular);

                        // verific daca id-ul introdus este al unui profesor
                        if (!(u instanceof Profesor)) {
                            throw new IllegalArgumentException("Utilizatorul cu ID-ul " + idProfTitular + " este student, nu profesor!");
                        }

                        Curs cursNou = new Curs(idCurs, titluCurs, (Profesor) u);
                        cursService.adaugaCurs(cursNou);
                        break;

                    case 4:
                        System.out.print("ID Lectie noua: ");
                        Long idLectie = Long.parseLong(scanner.nextLine());
                        System.out.print("Titlu Lectie: ");
                        String titluLectie = scanner.nextLine();
                        System.out.print("ID Curs de care apartine: ");
                        Long idCursLectie = Long.parseLong(scanner.nextLine());

                        Curs c = cursService.cautaCursDupaId(idCursLectie);
                        Lectie lectieNoua = new Lectie(idLectie, titluLectie, c);
                        System.out.println("Lectie adaugata cu succes: " + lectieNoua.getTitlu());
                        break;

                    case 5:
                        System.out.print("ID Curs: ");
                        Long idCursInscriere = Long.parseLong(scanner.nextLine());
                        System.out.print("ID Student: ");
                        Long idStudInscriere = Long.parseLong(scanner.nextLine());

                        Curs cursCautat = cursService.cautaCursDupaId(idCursInscriere);
                        Utilizator studCautat = utilizatorService.cautaDupaId(idStudInscriere);

                        // verific daca este student
                        if (!(studCautat instanceof Student)) {
                            throw new IllegalArgumentException("ID-ul introdus apartine unui profesor, nu unui student!");
                        }

                        cursService.inscrieStudent(cursCautat, (Student) studCautat);
                        break;

                    case 6:
                        System.out.print("ID Quiz: ");
                        Long idQuiz = Long.parseLong(scanner.nextLine());
                        System.out.print("Titlu Quiz: ");
                        String titluQuiz = scanner.nextLine();
                        System.out.print("ID Curs asociat: ");
                        Long idCursQuiz = Long.parseLong(scanner.nextLine());

                        Curs cursQ = cursService.cautaCursDupaId(idCursQuiz);
                        Quiz q = new Quiz(idQuiz, titluQuiz, cursQ);
                        System.out.println("Quiz adaugat: " + q.getTitlu());
                        break;

                    case 7:
                        System.out.print("ID Rezultat: " );
                        Long idRezultat = Long.parseLong(scanner.nextLine());
                        System.out.print("ID Student: ");
                        Long idStudNota = Long.parseLong(scanner.nextLine());
                        System.out.print("Numele Quiz-ului: ");
                        String numeQuizNota = scanner.nextLine();
                        System.out.print("Nota obtinuta: ");
                        double nota = Double.parseDouble(scanner.nextLine());

                        Utilizator stNota = utilizatorService.cautaDupaId(idStudNota);
                        if (stNota instanceof Student) {
                            RezultatQuiz rez = new RezultatQuiz(idRezultat, (Student) stNota, numeQuizNota, nota);
                            cursService.salveazaRezultat(rez);
                        }
                        break;

                    case 8:
                        List<Curs> cursuriSortate = cursService.getCursuriSortateAlfabetic();
                        if (cursuriSortate.isEmpty()) {
                            System.out.println("Nu exista cursuri in platforma.");
                        } else {
                            System.out.println("--- Cursuri Platforma ---");
                            for (Curs cursPlatforma : cursuriSortate) {
                                System.out.println("- [" + cursPlatforma.getId() + "] " + cursPlatforma.getTitlu());
                            }
                        }
                        break;

                    case 9:
                        System.out.print("ID Student: ");
                        Long idStudIstoric = Long.parseLong(scanner.nextLine());
                        Utilizator uIstoric = utilizatorService.cautaDupaId(idStudIstoric);

                        if (uIstoric instanceof Student) {
                            List<RezultatQuiz> istoric = cursService.getIstoricStudent((Student) uIstoric);
                            if (istoric.isEmpty()) {
                                System.out.println("Studentul nu are nicio nota inregistrata.");
                            } else {
                                System.out.println("--- Istoric note " + uIstoric.getNume() + " ---");
                                for (RezultatQuiz r : istoric) {
                                    System.out.println("- " + r.getNumeQuiz() + ": " + r.getNota());
                                }
                            }
                        }
                        break;

                    case 10:
                        System.out.print("ID Profesor: ");
                        Long idProfCauta = Long.parseLong(scanner.nextLine());
                        Utilizator uProf = utilizatorService.cautaDupaId(idProfCauta);

                        if (uProf instanceof Profesor) {
                            List<Curs> cursurileLui = cursService.getCursuriProfesor((Profesor) uProf);
                            if (cursurileLui.isEmpty()) {
                                System.out.println("Profesorul nu preda niciun curs inca.");
                            } else {
                                System.out.println("--- Cursurile domnului/doamnei " + uProf.getNume() + " ---");
                                for (Curs cPredat : cursurileLui) {
                                    System.out.println("- " + cPredat.getTitlu());
                                }
                            }
                        }
                        break;

                    case 0:
                        System.out.println("Iesire din platforma... La revedere!");
                        break;

                    default:
                        System.out.println("Optiunea nu exista! Te rog alege un numar intre 0 si 10.");
                        break;
                }

            } catch (NumberFormatException e) {
                System.out.println("Eroare de input: Te rog sa introduci doar numere valide pentru ID-uri, note sau ani.");

            } catch (UtilizatorInexistentException e) {
                System.out.println("Eroare de cautare: " + e.getMessage());
                System.out.println("Sfat: Poti folosi optiunea 8 sau 10 pentru a verifica ID-urile existente.");

            } catch (StudentDejaInscrisException e) {
                System.out.println("Actiune respinsa: " + e.getMessage());

            } catch (NotaInvalidaException e) {
                System.out.println("Date invalide: " + e.getMessage());

            } catch (Exception e) {
                // pentru orice alta eroare de sistem
                System.out.println("Eroare de sistem: " + e.getMessage());
            }
        }

        scanner.close();
    }
}