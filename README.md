# Platforma E-Learning (Proiect PAOJ)

Acest proiect reprezinta o platforma de tip e-learning, creata in Java. Platforma permite interactiunea dintre profesori (cei care creeaza si predau lectiile) si studenti (cei care invata si sustin teste).

## 1. Tipuri de obiecte din domeniu 
Sistemul modeleaza urmatoarele 8 entitati principale:
1. **Utilizator** (clasa abstracta de baza pentru persoanele din sistem)
2. **Student** (studentul care participa la cursuri si rezolva teste)
3. **Profesor** (profesorul care creeaza cursurile)
4. **Curs** (materia predata)
5. **Lectie** (un modul de studiu din interiorul unui curs)
6. **Quiz** (un test asociat unui curs)
7. **Intrebare** (o cerinta dintr-un quiz)
8. **RezultatQuiz** (clasa imutabila care stocheaza nota finala a unui student)

## 2. Actiuni posibile in sistem
Platforma suporta urmatoarele 10 operatii principale:
1. Adauga un nou profesor in sistem.
2. Adauga un nou student in sistem.
3. Creeaza un curs nou (asociat unui profesor).
4. Adauga o lectie in programa unui curs existent.
5. Inscrie un student la un anumit curs.
6. Adauga un quiz pentru un curs (cu intrebari aferente).
7. Salveaza rezultatul unui student in urma sustinerii unui quiz.
8. Afiseaza toate cursurile disponibile pe platforma (sortate alfabetic).
9. Afiseaza istoricul notelor (rezultatelor) pentru un anumit student.
10. Cauta si afiseaza toate cursurile la care preda un anumit profesor.