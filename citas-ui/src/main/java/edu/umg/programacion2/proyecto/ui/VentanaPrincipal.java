package edu.umg.programacion2.proyecto.ui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import edu.umg.programacion2.proyecto.dao.CitaDAO;
import edu.umg.programacion2.proyecto.modelo.Cita;

public class VentanaPrincipal extends JFrame {

    private static final long serialVersionUID = 1L;

    private final JTextField txtCliente = new JTextField();
    private final JTextField txtFecha = new JTextField();
    private final JTextField txtHora = new JTextField();
    private final JTextField txtServicio = new JTextField();
    private final JTextField txtDuracion = new JTextField();
    private final JTextField txtFechaUltimaCita = new JTextField();
    
    private final JComboBox<String> cmbEstado =
            new JComboBox<>(new String[] {
                    "pendiente",
                    "confirmada",
                    "cancelada"
            });

    private final DefaultTableModel modeloTabla =
            new DefaultTableModel(
                    new Object[] {
                            "ID",
                            "Cliente",
                            "Fecha y hora",
                            "Servicio",
                            "Duración",
                            "Estado",
                            "Cliente frecuente"
                    }, 0) {

                private static final long serialVersionUID = 1L;

                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

    private final JTable tablaCitas = new JTable(modeloTabla);

    private final CitaDAO citaDAO = new CitaDAO();

    private Integer idSeleccionado = null;

    private final DateTimeFormatter formatoFecha =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final DateTimeFormatter formatoHora =
            DateTimeFormatter.ofPattern("HH:mm");

    public VentanaPrincipal() {

        setTitle("Agenda de citas");
        setSize(950, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        crearInterfaz();
        cargarCitas();
    }

    private void crearInterfaz() {

        setLayout(new BorderLayout(10, 10));

        JPanel formulario = new JPanel(new GridLayout(7, 2, 8, 8));
        formulario.setBorder(
                BorderFactory.createTitledBorder("Agendar cita"));

        formulario.add(new JLabel("Cliente:"));
        formulario.add(txtCliente);

        formulario.add(new JLabel("Fecha (AAAA-MM-DD):"));
        formulario.add(txtFecha);

        formulario.add(new JLabel("Hora (HH:MM):"));
        formulario.add(txtHora);

        formulario.add(new JLabel("Servicio:"));
        formulario.add(txtServicio);

        formulario.add(new JLabel("Duración (min):"));
        formulario.add(txtDuracion);

        formulario.add(new JLabel("Fecha última cita (opcional):"));
        formulario.add(txtFechaUltimaCita);
        
        formulario.add(new JLabel("Estado:"));
        formulario.add(cmbEstado);

        JPanel botones = new JPanel();

        JButton btnGuardar = new JButton("Guardar");
        JButton btnActualizar = new JButton("Actualizar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnLimpiar = new JButton("Limpiar");

        botones.add(btnGuardar);
        botones.add(btnActualizar);
        botones.add(btnEliminar);
        botones.add(btnLimpiar);

        JPanel superior = new JPanel(new BorderLayout());
        superior.add(formulario, BorderLayout.CENTER);
        superior.add(botones, BorderLayout.SOUTH);

        tablaCitas.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION);

        add(superior, BorderLayout.NORTH);
        add(new JScrollPane(tablaCitas), BorderLayout.CENTER);

        btnGuardar.addActionListener(e -> guardarCita());
        btnActualizar.addActionListener(e -> actualizarCita());
        btnEliminar.addActionListener(e -> eliminarCita());
        btnLimpiar.addActionListener(e -> limpiarFormulario());

        tablaCitas.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarCitaSeleccionada();
            }
        });
    }

    private Cita leerFormulario(boolean esNueva) {

        String cliente = txtCliente.getText().trim();
        String fechaTexto = txtFecha.getText().trim();
        String horaTexto = txtHora.getText().trim();
        String servicio = txtServicio.getText().trim();
        String duracionTexto = txtDuracion.getText().trim();
        String fechaUltimaTexto = txtFechaUltimaCita.getText().trim();
        String estado = (String) cmbEstado.getSelectedItem();

        // Validar campos obligatorios
        if (cliente.isEmpty()
                || fechaTexto.isEmpty()
                || horaTexto.isEmpty()
                || servicio.isEmpty()
                || duracionTexto.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Todos los campos obligatorios deben llenarse.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE);

            return null;
        }

        try {

            // Convertir fecha y hora de la nueva cita
            LocalDate fecha = LocalDate.parse(
                    fechaTexto, formatoFecha);

            LocalTime hora = LocalTime.parse(
                    horaTexto, formatoHora);

            LocalDateTime fechaHora =
                    LocalDateTime.of(fecha, hora);

            // Validar duración
            int duracion = Integer.parseInt(duracionTexto);

            if (duracion <= 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "La duración debe ser mayor a cero.",
                        "Validación",
                        JOptionPane.WARNING_MESSAGE);

                return null;
            }

            // Una cita nueva no puede estar en el pasado
            if (esNueva && !fechaHora.isAfter(LocalDateTime.now())) {

                JOptionPane.showMessageDialog(
                        this,
                        "La fecha y hora de la cita debe ser futura.",
                        "Validación",
                        JOptionPane.WARNING_MESSAGE);

                return null;
            }

            // Fecha de última cita es opcional
            LocalDate fechaUltimaCita = null;

            if (!fechaUltimaTexto.isEmpty()) {

                fechaUltimaCita = LocalDate.parse(
                        fechaUltimaTexto, formatoFecha);
            }

            return new Cita(
                    cliente,
                    fechaHora,
                    servicio,
                    duracion,
                    estado,
                    fechaUltimaCita);

        } catch (DateTimeParseException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Formato de fecha u hora incorrecto.\n\n"
                    + "Fecha: AAAA-MM-DD\n"
                    + "Hora: HH:MM\n"
                    + "Ejemplo: 2026-10-10 y 14:30",
                    "Formato incorrecto",
                    JOptionPane.WARNING_MESSAGE);

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "La duración debe ser un número entero.",
                    "Formato incorrecto",
                    JOptionPane.WARNING_MESSAGE);
        }

        return null;
    }

    private void guardarCita() {

        Cita cita = leerFormulario(true);

        if (cita == null) {
            return;
        }

        // Una cita nueva queda pendiente por defecto.
        cita.setEstado("pendiente");

        try {

            citaDAO.crear(cita);

            JOptionPane.showMessageDialog(
                    this,
                    "Cita agendada correctamente.");

            cargarCitas();
            limpiarFormulario();

        } catch (SQLException ex) {

            mostrarErrorBD(ex);
        }
    }

    private void actualizarCita() {

        if (idSeleccionado == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Selecciona una cita de la tabla.");

            return;
        }

        Cita cita = leerFormulario(false);

        if (cita == null) {
            return;
        }

        cita.setId(idSeleccionado);

        try {

            if (citaDAO.actualizar(cita)) {

                JOptionPane.showMessageDialog(
                        this,
                        "Cita actualizada correctamente.");

                cargarCitas();
                limpiarFormulario();
            }

        } catch (SQLException ex) {

            mostrarErrorBD(ex);
        }
    }

    private void eliminarCita() {

        if (idSeleccionado == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Selecciona una cita de la tabla.");

            return;
        }

        int respuesta = JOptionPane.showConfirmDialog(
                this,
                "¿Deseas eliminar esta cita definitivamente?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION);

        if (respuesta != JOptionPane.YES_OPTION) {
            return;
        }

        try {

            if (citaDAO.eliminar(idSeleccionado)) {

                JOptionPane.showMessageDialog(
                        this,
                        "Cita eliminada correctamente.");

                cargarCitas();
                limpiarFormulario();
            }

        } catch (SQLException ex) {

            mostrarErrorBD(ex);
        }
    }

    private void cargarCitas() {

        try {

            List<Cita> citas = citaDAO.listarTodos();

            modeloTabla.setRowCount(0);

            for (Cita cita : citas) {

            	modeloTabla.addRow(new Object[] {
            	        cita.getId(),
            	        cita.getCliente(),
            	        cita.getFechaHora().format(formatoFecha),
            	        cita.getServicio(),
            	        cita.getDuracionMinutos() + " min",
            	        cita.getEstado(),
            	        cita.getFechaUltimaCita() != null ? "✓" : ""
            	});
            }

        } catch (SQLException ex) {

            mostrarErrorBD(ex);
        }
    }

    private void cargarCitaSeleccionada() {

        int fila = tablaCitas.getSelectedRow();

        if (fila == -1) {
            return;
        }

        idSeleccionado =
                Integer.parseInt(
                        modeloTabla.getValueAt(fila, 0).toString());

        txtCliente.setText(
                modeloTabla.getValueAt(fila, 1).toString());

        String fechaHora =
                modeloTabla.getValueAt(fila, 2).toString();

        String[] partes = fechaHora.split(" ");

        txtFecha.setText(partes[0]);
        txtHora.setText(partes[1]);

        txtServicio.setText(
                modeloTabla.getValueAt(fila, 3).toString());

        txtDuracion.setText(
                modeloTabla.getValueAt(fila, 4)
                        .toString()
                        .replace(" min", ""));

        cmbEstado.setSelectedItem(
                modeloTabla.getValueAt(fila, 5).toString());

        // Buscar la cita para obtener la fecha de la última cita
        try {

            citaDAO.buscarPorId(idSeleccionado).ifPresent(cita -> {

                if (cita.getFechaUltimaCita() != null) {

                    txtFechaUltimaCita.setText(
                            cita.getFechaUltimaCita().toString());

                } else {

                    txtFechaUltimaCita.setText("");
                }

                // La fecha de última cita no se puede modificar
                txtFechaUltimaCita.setEnabled(false);
            });

        } catch (SQLException ex) {

            mostrarErrorBD(ex);
        }
    }
    private void limpiarFormulario() {

        idSeleccionado = null;

        txtCliente.setText("");
        txtFecha.setText("");
        txtHora.setText("");
        txtServicio.setText("");
        txtDuracion.setText("");
        txtFechaUltimaCita.setText("");
        txtFechaUltimaCita.setEnabled(true);

        cmbEstado.setSelectedItem("pendiente");

        tablaCitas.clearSelection();
    }

    private void mostrarErrorBD(SQLException ex) {

        JOptionPane.showMessageDialog(
                this,
                "Ocurrió un error al acceder a la base de datos:\n"
                        + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }
}