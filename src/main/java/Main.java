/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import CapaGrafica.Inicio;
import javax.swing.*;
import com.formdev.flatlaf.FlatDarkLaf;
/**
 *
 * @author sebas
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // 1) Look & Feel global (toda la app)
                UIManager.setLookAndFeel(new FlatDarkLaf()); // 

                // 2) (Opcional) Ajustes globales de estilo
                UIManager.put("Button.focusedBackground", null);
                UIManager.put("Table.showHorizontalLines", true);
                UIManager.put("Table.showVerticalLines", false);
                UIManager.put("Component.focusWidth", 1);
                UIManager.put("Table.rowHeight", 26);

            } catch (Exception ex) {
                System.err.println("No se pudo aplicar FlatLaf: " + ex.getMessage());
            }

            // 3) Crear tu primera ventana (y a partir de ahí, todas)
            new Inicio().setVisible(true);
        });
    for (java.awt.Window w : java.awt.Window.getWindows()) {
        SwingUtilities.updateComponentTreeUI(w);
        w.pack(); // opcional, para recalcular tamaños
    }
    }
}
