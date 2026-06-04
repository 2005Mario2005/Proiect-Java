# Platforma E-Learning

Proiect dezvoltat in Java, reprezentand o platforma de gestiune a cursurilor si evaluarilor. Implementarea este realizata treptat, fiind impartita in mai multe etape de dezvoltare.

## Etapa 1
In prima faza am implementat arhitectura de baza a aplicatiei, pastrand datele in memorie si folosind concepte OOP:
* Am definit ierarhia de modele (Utilizator, Student, Profesor, Curs, Lectie, Quiz, Intrebare, RezultatQuiz).
* Am folosit colectii din Java (List, Set, Map) pentru stocarea si gestionarea datelor in servicii.
* Am implementat clasa RezultatQuiz ca obiect imutabil.
* Am creat si tratat exceptii custom pentru logica de business (ex: UtilizatorInexistentException, EmailDuplicatException, StudentDejaInscrisException).
* Am construit un meniu interactiv in consola in clasa Main, delegand logica aplicatiei in clase de tip Service (implementate ca Singleton).

## Etapa 2
In aceasta etapa am inlocuit stocarea in memorie cu o baza de date relationala si am adaugat un sistem de logging:
* Am integrat o baza de date SQLite folosind JDBC (fara framework-uri ORM).
* Am implementat pattern-ul Repository (printr-o interfata generica si clase concrete) pentru a separa logica SQL de cea de business.
* Am mutat responsabilitatea stergerii in cascada direct pe baza de date, folosind constrangeri de tip ON DELETE CASCADE in scriptul de creare a tabelelor.
* Am creat un serviciu de audit (AuditService) care inregistreaza intr-un fisier CSV (audit.csv) fiecare actiune efectuata de utilizator in aplicatie, insotita de timestamp.
* Am automatizat initializarea bazei de date: la fiecare pornire a aplicatiei se executa scriptul schema.sql pentru recrearea structurii.

## Rulare si configurare
Pentru a rula proiectul, este necesara adaugarea driver-ului JDBC pentru SQLite (fisierul sqlite-jdbc.jar) in librariile proiectului. 
Aplicatia se porneste ruland clasa Main din pachetul principal. La executie, atat fisierul bazei de date (elearning.db), cat si cel de audit (audit.csv) vor fi generate automat in radacina proiectului.