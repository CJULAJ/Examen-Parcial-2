package edu.umg.programacion2.proyecto.ui;

import javax.swing.SwingUtilities;

public class MainUI {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            VentanaPrincipal ventana = new VentanaPrincipal();
            ventana.setVisible(true);
        });
    }
}
