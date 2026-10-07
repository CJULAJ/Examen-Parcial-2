package edu.umg.programacion2.proyecto.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import edu.umg.programacion2.proyecto.modelo.Cita;

public class CitaDAO {

    public Cita crear(Cita cita) throws SQLException {

    	String sql = """
    	        INSERT INTO citas
    	        (cliente, fecha_hora, servicio, duracion_minutos, estado,
    	         fecha_ultima_cita)
    	        VALUES (?, ?, ?, ?, ?, ?)
    	        """;

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, cita.getCliente());
            ps.setTimestamp(2, Timestamp.valueOf(cita.getFechaHora()));
            ps.setString(3, cita.getServicio());
            ps.setInt(4, cita.getDuracionMinutos());
            ps.setString(5, cita.getEstado());
            if (cita.getFechaUltimaCita() != null) 
            {
                ps.setDate(6, java.sql.Date.valueOf(cita.getFechaUltimaCita()));
            } else
            {
                ps.setNull(6, java.sql.Types.DATE);
            }
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    cita.setId(rs.getInt(1));
                }
            }

            return cita;
        }
    }

    public List<Cita> listarTodos() throws SQLException {

        List<Cita> citas = new ArrayList<>();

        String sql = """
                SELECT id, cliente, fecha_hora, servicio,
                       duracion_minutos, estado, fecha_ultima_cita
                FROM citas
                ORDER BY fecha_hora
                """;

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                java.sql.Date fechaBD =
                        rs.getDate("fecha_ultima_cita");

                Cita cita = new Cita(
                        rs.getInt("id"),
                        rs.getString("cliente"),
                        rs.getTimestamp("fecha_hora").toLocalDateTime(),
                        rs.getString("servicio"),
                        rs.getInt("duracion_minutos"),
                        rs.getString("estado"),
                        fechaBD == null ? null : fechaBD.toLocalDate()
                );

                citas.add(cita);
            }
        }

        return citas;
    }

    public Optional<Cita> buscarPorId(int id) throws SQLException {

        String sql = """
                SELECT id, cliente, fecha_hora, servicio,
                       duracion_minutos, estado, fecha_ultima_cita
                FROM citas
                WHERE id = ?
                """;

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    java.sql.Date fechaBD =
                            rs.getDate("fecha_ultima_cita");

                    Cita cita = new Cita(
                            rs.getInt("id"),
                            rs.getString("cliente"),
                            rs.getTimestamp("fecha_hora").toLocalDateTime(),
                            rs.getString("servicio"),
                            rs.getInt("duracion_minutos"),
                            rs.getString("estado"),
                            fechaBD == null ? null : fechaBD.toLocalDate()
                    );

                    return Optional.of(cita);
                }
            }
        }

        return Optional.empty();
    }

    public boolean actualizar(Cita cita) throws SQLException {

        String sql = """
                UPDATE citas
                SET cliente = ?,
                    fecha_hora = ?,
                    servicio = ?,
                    duracion_minutos = ?,
                    estado = ?
                WHERE id = ?
                """;

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, cita.getCliente());
            ps.setTimestamp(2, Timestamp.valueOf(cita.getFechaHora()));
            ps.setString(3, cita.getServicio());
            ps.setInt(4, cita.getDuracionMinutos());
            ps.setString(5, cita.getEstado());
            ps.setInt(6, cita.getId());

            return ps.executeUpdate() > 0;
        }
    }

    public boolean eliminar(int id) throws SQLException {

        String sql = "DELETE FROM citas WHERE id = ?";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, id);

            return ps.executeUpdate() > 0;
        }
    }
}