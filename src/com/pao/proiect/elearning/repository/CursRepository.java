package com.pao.proiect.elearning.repository;

import com.pao.proiect.elearning.model.Curs;
import com.pao.proiect.elearning.model.Profesor;
import com.pao.proiect.elearning.model.Utilizator;
import com.pao.proiect.elearning.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CursRepository implements Repository<Curs, Long> {

    private final UtilizatorRepository utilizatorRepository;

    public CursRepository() {
        this.utilizatorRepository = new UtilizatorRepository();
    }

    @Override
    public void save(Curs entity) {
        String sql = "INSERT INTO cursuri (titlu, id_profesor) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, entity.getTitlu());
            pstmt.setLong(2, entity.getProfesor().getId());
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    entity.setId(generatedKeys.getLong(1));
                }
            }
        } catch (SQLException e) {
            System.out.println("Eroare la salvarea cursului in baza de date: " + e.getMessage());
        }
    }

    @Override
    public Optional<Curs> findById(Long id) {
        String sql = "SELECT * FROM cursuri WHERE id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToCurs(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println("Eroare la cautarea cursului in baza de date: " + e.getMessage());
        }
        return Optional.empty();
    }

    //(III) JOIN 2/3
    @Override
    public List<Curs> findAll() {
        List<Curs> cursuri = new ArrayList<>();

        String sql = "SELECT c.id, c.titlu, u.id AS prof_id, u.nume, u.email, u.departament " +
                "FROM cursuri c JOIN utilizatori u ON c.id_profesor = u.id";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Profesor prof = new Profesor(rs.getLong("prof_id"), rs.getString("nume"),
                        rs.getString("email"), rs.getString("departament"));
                cursuri.add(new Curs(rs.getLong("id"), rs.getString("titlu"), prof));
            }
        } catch (SQLException e) {
            System.out.println("Eroare JOIN preluare cursuri: " + e.getMessage());
        }
        return cursuri;
    }

    @Override
    public void update(Curs entity) {
        String sql = "UPDATE cursuri SET titlu = ?, id_profesor = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, entity.getTitlu());
            pstmt.setLong(2, entity.getProfesor().getId());
            pstmt.setLong(3, entity.getId());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Eroare la actualizarea cursului in baza de date: " + e.getMessage());
        }
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM cursuri WHERE id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Eroare la stergerea cursului din baza de date: " + e.getMessage());
        }
    }

    private Curs mapResultSetToCurs(ResultSet rs) throws SQLException {
        Long id = rs.getLong("id");
        String titlu = rs.getString("titlu");
        Long idProfesor = rs.getLong("id_profesor");

        Optional<Utilizator> profesorOpt = utilizatorRepository.findById(idProfesor);
        Profesor profesor = null;

        if (profesorOpt.isPresent() && profesorOpt.get() instanceof Profesor) {
            profesor = (Profesor) profesorOpt.get();
        } else {
            throw new SQLException("Profesorul titular cu ID-ul " + idProfesor + " nu a putut fi gasit!");
        }

        return new Curs(id, titlu, profesor);
    }
}