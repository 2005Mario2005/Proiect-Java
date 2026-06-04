package com.pao.proiect.elearning;

import com.pao.proiect.elearning.model.*;
import com.pao.proiect.elearning.service.CursService;
import com.pao.proiect.elearning.service.UtilizatorService;
import com.pao.proiect.elearning.util.DatabaseConnection;
import com.pao.proiect.elearning.exception.EmailDuplicatException;
import com.pao.proiect.elearning.exception.NotaInvalidaException;
import com.pao.proiect.elearning.exception.StudentDejaInscrisException;
import com.pao.proiect.elearning.exception.UtilizatorInexistentException;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.Statement;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        //initializarea bazei de date (crearea tabelelor) la fiecare rulare
        initializareBazaDeDate();

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
            System.out.println("11. Afiseaza detalii despre un utilizator (dupa ID)");
            System.out.println("12. Sterge un utilizator (si datele asociate)");
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
                        System.out.print("Nume Profesor: ");
                        String numeProf = scanner.nextLine();
                        System.out.print("Email: ");
                        String emailProf = scanner.nextLine();
                        System.out.print("Departament: ");
                        String depProf = scanner.nextLine();

                        Profesor p = new Profesor(null, numeProf, emailProf, depProf);
                        utilizatorService.adaugaUtilizator(p);
                        break;

                    case 2:
                        System.out.print("Nume Student: ");
                        String numeStud = scanner.nextLine();
                        System.out.print("Email: ");
                        String emailStud = scanner.nextLine();
                        System.out.print("An studiu (numar): ");
                        int anStud = Integer.parseInt(scanner.nextLine());

                        Student s = new Student(null, numeStud, emailStud, anStud);
                        utilizatorService.adaugaUtilizator(s);
                        break;

                    case 3:
                        System.out.print("Titlu Curs: ");
                        String titluCurs = scanner.nextLine();
                        System.out.print("ID Profesor titular: ");
                        Long idProfTitular = Long.parseLong(scanner.nextLine());

                        Utilizator uProfesor = utilizatorService.cautaDupaId(idProfTitular);
                        if (!(uProfesor instanceof Profesor)) {
                            throw new IllegalArgumentException("Utilizatorul cu ID-ul " + idProfTitular + " nu este profesor!");
                        }

                        Curs cursNou = new Curs(null, titluCurs, (Profesor) uProfesor);
                        cursService.adaugaCurs(cursNou);
                        break;

                    case 4:
                        System.out.print("Titlu Lectie: ");
                        String titluLectie = scanner.nextLine();
                        System.out.print("ID Curs de care apartine: ");
                        Long idCursLectie = Long.parseLong(scanner.nextLine());

                        Curs cLectie = cursService.cautaCursDupaId(idCursLectie);
                        Lectie lectieNoua = new Lectie(null, titluLectie, cLectie);
                        cursService.adaugaLectieLaCurs(lectieNoua, cLectie);
                        break;

                    case 5:
                        System.out.print("ID Curs: ");
                        Long idCursInscriere = Long.parseLong(scanner.nextLine());
                        System.out.print("ID Student: ");
                        Long idStudInscriere = Long.parseLong(scanner.nextLine());

                        Curs cursCautat = cursService.cautaCursDupaId(idCursInscriere);
                        Utilizator studCautat = utilizatorService.cautaDupaId(idStudInscriere);

                        if (!(studCautat instanceof Student)) {
                            throw new IllegalArgumentException("ID-ul introdus nu apartine unui student!");
                        }

                        cursService.inscrieStudent(cursCautat, (Student) studCautat);
                        break;

                    case 6:
                        System.out.print("Titlu Quiz: ");
                        String titluQuiz = scanner.nextLine();
                        System.out.print("ID Curs asociat: ");
                        Long idCursQuiz = Long.parseLong(scanner.nextLine());

                        Curs cursQ = cursService.cautaCursDupaId(idCursQuiz);
                        Quiz quizNou = new Quiz(null, titluQuiz, cursQ);
                        cursService.adaugaQuizLaCurs(quizNou, cursQ);
                        break;

                    case 7:
                        System.out.print("ID Student: ");
                        Long idStudNota = Long.parseLong(scanner.nextLine());
                        System.out.print("ID Quiz: ");
                        Long idQuizRezultat = Long.parseLong(scanner.nextLine());
                        System.out.print("Nota obtinuta: ");
                        double nota = Double.parseDouble(scanner.nextLine());

                        Utilizator stNota = utilizatorService.cautaDupaId(idStudNota);
                        if (!(stNota instanceof Student)) {
                            throw new IllegalArgumentException("ID-ul introdus nu apartine unui student!");
                        }

                        Quiz quizNota = cursService.cautaQuizDupaId(idQuizRezultat);
                        cursService.salveazaRezultat((Student) stNota, quizNota, nota);
                        break;

                    case 8:
                        List<Curs> cursuriSortate = cursService.getCursuriSortateAlfabetic();
                        if (cursuriSortate.isEmpty()) {
                            System.out.println("Nu exista cursuri in platforma.");
                        } else {
                            System.out.println("--- Cursuri Platforma ---");
                            for (Curs cursPlatforma : cursuriSortate) {
                                System.out.println("- " + cursPlatforma.toString());
                            }
                        }
                        break;

                    case 9:
                        System.out.print("ID Student: ");
                        Long idStudIstoric = Long.parseLong(scanner.nextLine());
                        Utilizator uIstoric = utilizatorService.cautaDupaId(idStudIstoric);

                        if (!(uIstoric instanceof Student)) {
                            throw new IllegalArgumentException("ID-ul introdus nu apartine unui student!");
                        }

                        List<RezultatQuiz> istoric = cursService.getIstoricStudent((Student) uIstoric);
                        if (istoric.isEmpty()) {
                            System.out.println("Studentul nu are nicio nota inregistrata.");
                        } else {
                            System.out.println("--- Istoric note " + uIstoric.getNume() + " ---");
                            for (RezultatQuiz r : istoric) {
                                System.out.println("- " + r.toString());
                            }
                        }
                        break;

                    case 10:
                        System.out.print("ID Profesor: ");
                        Long idProfCauta = Long.parseLong(scanner.nextLine());
                        Utilizator uProf = utilizatorService.cautaDupaId(idProfCauta);

                        if (!(uProf instanceof Profesor)) {
                            throw new IllegalArgumentException("ID-ul introdus nu apartine unui profesor!");
                        }

                        List<Curs> cursurileLui = cursService.getCursuriProfesor((Profesor) uProf);
                        if (cursurileLui.isEmpty()) {
                            System.out.println("Profesorul nu preda niciun curs inca.");
                        } else {
                            System.out.println("--- Cursurile profesorului " + uProf.getNume() + " ---");
                            for (Curs cPredat : cursurileLui) {
                                System.out.println("- " + cPredat.toString());
                            }
                        }
                        break;

                    case 11:
                        System.out.print("ID Utilizator: ");
                        Long idCautat = Long.parseLong(scanner.nextLine());
                        Utilizator gasit = utilizatorService.cautaDupaId(idCautat);
                        System.out.println("Detalii: " + gasit.toString());
                        break;

                    case 12:
                        System.out.print("ID Utilizator de sters: ");
                        Long idDeSters = Long.parseLong(scanner.nextLine());
                        utilizatorService.stergeUtilizator(idDeSters);
                        break;

                    case 0:
                        System.out.println("Iesire din platforma... La revedere!");
                        break;

                    default:
                        System.out.println("Optiunea nu exista! Te rog alege un numar intre 0 si 12.");
                        break;
                }

            } catch (NumberFormatException e) {
                System.out.println("Eroare de input: Te rog sa introduci doar numere valide pentru ID-uri, note sau ani.");
            } catch (UtilizatorInexistentException | EmailDuplicatException e) {
                System.out.println("Eroare utilizator: " + e.getMessage());
            } catch (StudentDejaInscrisException e) {
                System.out.println("Actiune respinsa: " + e.getMessage());
            } catch (NotaInvalidaException e) {
                System.out.println("Date invalide: " + e.getMessage());
            } catch (RuntimeException e) {
                System.out.println("Eroare sistem: " + e.getMessage());
            }
        }

        scanner.close();
    }

    private static void initializareBazaDeDate() {
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             InputStream is = Main.class.getClassLoader().getResourceAsStream("schema.sql")) {

            if (is != null) {
                Scanner scannerDb = new Scanner(is).useDelimiter("\\A");
                String sqlScript = scannerDb.hasNext() ? scannerDb.next() : "";
                String[] queries = sqlScript.split(";");
                for (String query : queries) {
                    if (!query.trim().isEmpty()) {
                        stmt.execute(query);
                    }
                }
                System.out.println("=> Baza de date a fost initializata cu succes (tabelele au fost resetate).");
            } else {
                System.out.println("=> Fisierul schema.sql nu a fost gasit in resources!");
            }
        } catch (Exception e) {
            System.out.println("=> Eroare la rularea scriptului SQL: " + e.getMessage());
        }
    }
}