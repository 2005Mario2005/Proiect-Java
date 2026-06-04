package com.pao.proiect.elearning.repository;

import com.pao.proiect.elearning.model.Curs;
import com.pao.proiect.elearning.model.Lectie;
import com.pao.proiect.elearning.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LectieRepository implements Repository<Lectie, Long> {

    private final CursRepository cursRepository;

    public LectieRepository() {
        this.cursRepository = new CursRepository();
    }

    @Override
    public void save(Lectie entity) {
        String sql = "INSERT INTO lectii (titlu, id_curs) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, entity.getTitlu());
            pstmt.setLong(2, entity.getCurs().getId());
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    entity.setId(generatedKeys.getLong(1));
                }
            }
        } catch (SQLException e) {
            System.out.println("Eroare la salvarea lectiei in baza de date: " + e.getMessage());
        }
    }

    @Override
    public Optional<Lectie> findById(Long id) {
        String sql = "SELECT * FROM lectii WHERE id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToLectie(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println("Eroare la cautarea lectiei in baza de date: " + e.getMessage());
        }
        return Optional.empty();
    }

    //(III) JOIN 3/3
    @Override
    public List<Lectie> findAll() {
        List<Lectie> lectii = new ArrayList<>();

        String sql = "SELECT l.id, l.titlu, c.id AS curs_id, c.titlu AS curs_titlu " +
                "FROM lectii l JOIN cursuri c ON l.id_curs = c.id";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Curs curs = new Curs(rs.getLong("curs_id"), rs.getString("curs_titlu"), null);
                lectii.add(new Lectie(rs.getLong("id"), rs.getString("titlu"), curs));
            }
        } catch (SQLException e) {
            System.out.println("Eroare JOIN preluare lectii: " + e.getMessage());
        }
        return lectii;
    }

    @Override
    public void update(Lectie entity) {
        String sql = "UPDATE lectii SET titlu = ?, id_curs = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, entity.getTitlu());
            pstmt.setLong(2, entity.getCurs().getId());
            pstmt.setLong(3, entity.getId());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Eroare la actualizarea lectiei in baza de date: " + e.getMessage());
        }
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM lectii WHERE id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Eroare la stergerea lectiei din baza de date: " + e.getMessage());
        }
    }

    private Lectie mapResultSetToLectie(ResultSet rs) throws SQLException {
        Long id = rs.getLong("id");
        String titlu = rs.getString("titlu");
        Long idCurs = rs.getLong("id_curs");

        Optional<Curs> cursOpt = cursRepository.findById(idCurs);
        if (cursOpt.isPresent()) {
            return new Lectie(id, titlu, cursOpt.get());
        } else {
            throw new SQLException("Cursul cu ID-ul " + idCurs + " asociat acestei lectii nu a fost gasit in DB!");
        }
    }
}