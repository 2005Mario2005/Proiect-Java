DROP TABLE IF EXISTS rezultate;
DROP TABLE IF EXISTS intrebari;
DROP TABLE IF EXISTS quizuri;
DROP TABLE IF EXISTS lectii;
DROP TABLE IF EXISTS inscrieri;
DROP TABLE IF EXISTS cursuri;
DROP TABLE IF EXISTS utilizatori;

CREATE TABLE utilizatori (
                             id INTEGER PRIMARY KEY AUTOINCREMENT,
                             nume VARCHAR(100) NOT NULL,
                             email VARCHAR(100) UNIQUE NOT NULL,
                             rol VARCHAR(20) NOT NULL,
                             departament VARCHAR(100),
                             an_studiu INTEGER
);

CREATE TABLE cursuri (
                         id INTEGER PRIMARY KEY AUTOINCREMENT,
                         titlu VARCHAR(100) NOT NULL,
                         id_profesor INTEGER NOT NULL,
                         FOREIGN KEY (id_profesor) REFERENCES utilizatori(id) ON DELETE CASCADE
);

CREATE TABLE inscrieri (
                           id_curs INTEGER NOT NULL,
                           id_student INTEGER NOT NULL,
                           PRIMARY KEY (id_curs, id_student),
                           FOREIGN KEY (id_curs) REFERENCES cursuri(id) ON DELETE CASCADE,
                           FOREIGN KEY (id_student) REFERENCES utilizatori(id) ON DELETE CASCADE
);

CREATE TABLE lectii (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        titlu VARCHAR(100) NOT NULL,
                        id_curs INTEGER NOT NULL,
                        FOREIGN KEY (id_curs) REFERENCES cursuri(id) ON DELETE CASCADE
);

CREATE TABLE quizuri (
                         id INTEGER PRIMARY KEY AUTOINCREMENT,
                         titlu VARCHAR(100) NOT NULL,
                         id_curs INTEGER NOT NULL,
                         FOREIGN KEY (id_curs) REFERENCES cursuri(id) ON DELETE CASCADE
);

CREATE TABLE intrebari (
                           id INTEGER PRIMARY KEY AUTOINCREMENT,
                           text_intrebare TEXT NOT NULL,
                           id_quiz INTEGER NOT NULL,
                           FOREIGN KEY (id_quiz) REFERENCES quizuri(id) ON DELETE CASCADE
);

CREATE TABLE rezultate (
                           id INTEGER PRIMARY KEY AUTOINCREMENT,
                           id_student INTEGER NOT NULL,
                           id_quiz INTEGER NOT NULL,
                           nota REAL NOT NULL,
                           FOREIGN KEY (id_student) REFERENCES utilizatori(id) ON DELETE CASCADE,
                           FOREIGN KEY (id_quiz) REFERENCES quizuri(id) ON DELETE CASCADE
);