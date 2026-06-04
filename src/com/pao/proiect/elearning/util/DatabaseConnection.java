package com.pao.proiect.elearning.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

//(I) clasa pentru gestionarea conexiunii la baza de date, implementata ca Singleton
public class DatabaseConnection {

    private static DatabaseConnection instance;
    private Connection connection;

    //este citita configuratia din fisierul db.properties la momentul instantierii
    private DatabaseConnection() {
        try {
            Properties props = new Properties();
            //este incarcat fisierul din classpath
            InputStream input = getClass().getClassLoader().getResourceAsStream("db.properties");

            if (input != null) {
                props.load(input);
                String url = props.getProperty("db.url");

                //se stabileste conexiunea folosind driver-ul JDBC
                this.connection = DriverManager.getConnection(url);
                System.out.println("Conexiunea la baza de date a fost stabilita cu succes.");
            } else {
                throw new RuntimeException("Fisierul db.properties nu a fost gasit!");
            }
        } catch (Exception e) {
            System.out.println("Eroare la initializarea conexiunii: " + e.getMessage());
        }
    }

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    //este returnata conexiunea activa pentru a fi utilizata de repository-uri
    public Connection getConnection() {
        try {
            //verificare daca conexiunea a fost inchisa sau pierduta si se reface la nevoie
            if (connection == null || connection.isClosed()) {
                Properties props = new Properties();
                InputStream input = getClass().getClassLoader().getResourceAsStream("db.properties");
                if (input != null) {
                    props.load(input);
                    connection = DriverManager.getConnection(props.getProperty("db.url"));
                }
            }
        } catch (Exception e) {
            System.out.println("Eroare la recuperarea conexiunii: " + e.getMessage());
        }
        return connection;
    }
}