package com.pao.proiect.elearning.repository;

import com.pao.proiect.elearning.model.Profesor;
import com.pao.proiect.elearning.model.Student;
import com.pao.proiect.elearning.model.Utilizator;
import com.pao.proiect.elearning.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//repository-ul concret pentru tabela utilizatori
public class UtilizatorRepository implements Repository<Utilizator, Long> {

    @Override
    public void save(Utilizator entity) {
        String sql = "INSERT INTO utilizatori (nume, email, rol, departament, an_studiu) VALUES (?, ?, ?, ?, ?)";

        //resursele sunt inchise automat folosind try-with-resources
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, entity.getNume());
            pstmt.setString(2, entity.getEmail());
            pstmt.setString(3, entity.getRol());

            // sunt verificate tipurile specifice pentru a popula corect coloanele
            if (entity instanceof Profesor) {
                pstmt.setString(4, ((Profesor) entity).getDepartament());
                pstmt.setNull(5, Types.INTEGER);
            } else if (entity instanceof Student) {
                pstmt.setNull(4, Types.VARCHAR);
                pstmt.setInt(5, ((Student) entity).getAnStudiu());
            }

            pstmt.executeUpdate();

            // este preluat ID-ul generat de baza de date si setat pe obiect
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    entity.setId(generatedKeys.getLong(1));
                }
            }
        } catch (SQLException e) {
            System.out.println("Eroare la salvarea utilizatorului in baza de date: " + e.getMessage());
        }
    }

    @Override
    public Optional<Utilizator> findById(Long id) {
        String sql = "SELECT * FROM utilizatori WHERE id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToUtilizator(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println("Eroare la cautarea utilizatorului in baza de date: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<Utilizator> findAll() {
        List<Utilizator> utilizatori = new ArrayList<>();
        String sql = "SELECT * FROM utilizatori";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                utilizatori.add(mapResultSetToUtilizator(rs));
            }
        } catch (SQLException e) {
            System.out.println("Eroare la preluarea utilizatorilor din baza de date: " + e.getMessage());
        }
        return utilizatori;
    }

    @Override
    public void update(Utilizator entity) {
        String sql = "UPDATE utilizatori SET nume = ?, email = ?, departament = ?, an_studiu = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, entity.getNume());
            pstmt.setString(2, entity.getEmail());

            if (entity instanceof Profesor) {
                pstmt.setString(3, ((Profesor) entity).getDepartament());
                pstmt.setNull(4, Types.INTEGER);
            } else if (entity instanceof Student) {
                pstmt.setNull(3, Types.VARCHAR);
                pstmt.setInt(4, ((Student) entity).getAnStudiu());
            }

            pstmt.setLong(5, entity.getId());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Eroare la actualizarea utilizatorului in baza de date: " + e.getMessage());
        }
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM utilizatori WHERE id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Eroare la stergerea utilizatorului din baza de date: " + e.getMessage());
        }
    }

    //metoda auxiliara pentru transformarea unui rand din ResultSet intr-un obiect Java
    private Utilizator mapResultSetToUtilizator(ResultSet rs) throws SQLException {
        Long id = rs.getLong("id");
        String nume = rs.getString("nume");
        String email = rs.getString("email");
        String rol = rs.getString("rol");

        if ("PROFESOR".equals(rol)) {
            String departament = rs.getString("departament");
            return new Profesor(id, nume, email, departament);
        } else {
            int anStudiu = rs.getInt("an_studiu");
            return new Student(id, nume, email, anStudiu);
        }
    }
}