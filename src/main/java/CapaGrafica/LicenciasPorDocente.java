/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package CapaGrafica;

import CapaPersistencia.ConsultasFaltas;
import CapaPersistencia.ConsultasFaltas;
import CapaExcepcion.BDexcepcion;
import CapaExcepcion.FaltasExcepcion;
import CapaPersistencia.LicenciaFilas;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.time.format.DateTimeFormatter;
import java.util.List;
/**
 *
 * @author sebas
 */
public class LicenciasPorDocente extends javax.swing.JFrame {
    private final String ciDocente;
    private final String nombreCompleto;
    private final ConsultasFaltas Consultar = new ConsultasFaltas();
    private final DefaultTableModel modelo = new DefaultTableModel(
        new String[]{"ID", "Motivo", "Desde", "Hasta", "Grupos"}, 0 // el 0 indica cuantas filas tendra al principio
            //o sea comienza vacia
    ) {
        @Override public boolean isCellEditable(int r, int c) { return false; } // solo lectura
    };

    private final DateTimeFormatter DF = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    /**
     * Creates new form LicenciasPorDocente
     */
    public LicenciasPorDocente(String ciDocente, String nombreCompleto) {
        initComponents();
        setLocationRelativeTo(null);
        this.ciDocente = ciDocente;
        this.nombreCompleto = nombreCompleto;
        postInit();
    }
    public void postInit(){
        labelNombre.setText(nombreCompleto);
        labelCedula.setText("CI: " + ciDocente);
        // Tabla
        tabla.setModel(modelo);
        // Ocultar la columna ID (queda, pero no se ve)
        tabla.getColumnModel().getColumn(0).setMinWidth(0);
        tabla.getColumnModel().getColumn(0).setMaxWidth(0);
        tabla.getColumnModel().getColumn(0).setPreferredWidth(0);
        // Acciones
        if (btnRefrescar != null) btnRefrescar.addActionListener(e -> cargarLicencias());
        if (btnEliminar != null)  btnEliminar.addActionListener(e -> eliminarSeleccionada());

        // Carga inicial
        cargarLicencias();
        configurarSpinnersFecha();
    }
    //Spinners
    
    private void configurarSpinnersFecha() {
    var m1 = new javax.swing.SpinnerDateModel(new java.util.Date(), null, null, java.util.Calendar.DAY_OF_MONTH);
    var m2 = new javax.swing.SpinnerDateModel(new java.util.Date(), null, null, java.util.Calendar.DAY_OF_MONTH);
    spDesde.setModel(m1);
    spHasta.setModel(m2);

    var ed1 = new javax.swing.JSpinner.DateEditor(spDesde, "dd/MM/yyyy");
    var ed2 = new javax.swing.JSpinner.DateEditor(spHasta, "dd/MM/yyyy");
    spDesde.setEditor(ed1);
    spHasta.setEditor(ed2);
}
    
    
    private void cargarLicencias() {
        modelo.setRowCount(0);
        try {
            List<LicenciaFilas> filas = Consultar.listarLicenciasPorDocente(ciDocente);
            for (LicenciaFilas l : filas) {
                String desde = l.getDesde() != null ? l.getDesde().format(DF) : "";
                String hasta = l.getHasta() != null ? l.getHasta().format(DF) : "";
                modelo.addRow(new Object[]{
                    l.getId(),            // esto esta oculto no se ve 
                    l.getMotivo(),        // ↑ porque no es necesario que el usuario vea el id de las licencias
                    desde,
                    hasta,
                    l.getGrupos()
                });
            }
        } catch (FaltasExcepcion | BDexcepcion ex) {
            JOptionPane.showMessageDialog(this,
                "Error cargando licencias:\n" + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private java.time.LocalDate toLocalDate(java.util.Date d) {
    return d == null ? null : d.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
}
    
    private void eliminarSeleccionada() {
        int row = tabla.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione una licencia en la tabla.",
                    "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }
        // el ID está en la 1ª columna (oculta)
        int id = (int) modelo.getValueAt(row, 0);

        int conf = JOptionPane.showConfirmDialog(this,
                "¿Eliminar la licencia seleccionada?", "Confirmar",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (conf != JOptionPane.YES_OPTION) return;

        try {
            String msg = Consultar.eliminarLicenciaDeDocente(ciDocente, id);
            JOptionPane.showMessageDialog(this, msg);
            cargarLicencias(); // refrescar
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "No se pudo eliminar: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    

    private LicenciasPorDocente() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tabla = new javax.swing.JTable();
        labelNombre = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        labelCedula = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        btnRefrescar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        ElimDoc = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        spDesde = new javax.swing.JSpinner();
        spHasta = new javax.swing.JSpinner();
        btnGuardar = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        tabla.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tabla);

        labelNombre.setText("jLabel1");

        jButton1.setText("Atras");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        labelCedula.setText("jLabel1");

        jLabel5.setFont(new java.awt.Font("Comic Sans MS", 1, 18)); // NOI18N
        jLabel5.setText("Seleciona uno");

        jLabel6.setFont(new java.awt.Font("Comic Sans MS", 1, 18)); // NOI18N
        jLabel6.setText("Motivo");

        btnRefrescar.setText("Refrescar");

        btnEliminar.setText("Eliminar");

        ElimDoc.setText("Eliminar Docente");
        ElimDoc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ElimDocActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Comic Sans MS", 1, 18)); // NOI18N
        jLabel7.setText("Grupo Afectados");

        jLabel8.setFont(new java.awt.Font("Comic Sans MS", 1, 18)); // NOI18N
        jLabel8.setText("para eliminar");

        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Comic Sans MS", 1, 18)); // NOI18N
        jLabel9.setText("Agregar una Licencia:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(ElimDoc))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(17, 17, 17)
                                        .addComponent(btnRefrescar))
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                            .addContainerGap()
                                            .addComponent(jLabel5))
                                        .addGroup(layout.createSequentialGroup()
                                            .addGap(24, 24, 24)
                                            .addComponent(btnEliminar)))
                                    .addGroup(layout.createSequentialGroup()
                                        .addContainerGap()
                                        .addComponent(jLabel8)))
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 487, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(154, 154, 154)
                                .addComponent(labelNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(69, 69, 69)
                                .addComponent(labelCedula, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(322, 322, 322)
                .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(102, 102, 102)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel9)
                            .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(94, 94, 94)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(spDesde, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(136, 136, 136)
                        .addComponent(spHasta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(122, 122, 122)
                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 278, Short.MAX_VALUE)
                        .addComponent(jLabel7)
                        .addGap(17, 17, 17))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(ElimDoc)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(labelNombre)
                            .addComponent(labelCedula))
                        .addGap(27, 27, 27)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(78, 78, 78)
                        .addComponent(btnRefrescar)
                        .addGap(35, 35, 35)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(25, 25, 25)
                        .addComponent(btnEliminar)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(19, 19, 19)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7))
                .addGap(16, 16, 16)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(spDesde, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(spHasta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        dispose();               
        Seleccion Ventana = new Seleccion();
        Ventana.setVisible(true);
        setVisible(false);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void ElimDocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ElimDocActionPerformed
        
        String ciDocente1 = this.ciDocente;
         int conf = JOptionPane.showConfirmDialog(this,
                "¿Eliminar la licencia seleccionada?", "Confirmar",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (conf != JOptionPane.YES_OPTION) return;

        try {
            String msg = Consultar.eliminarDocente(ciDocente);
            JOptionPane.showMessageDialog(this, msg);
            dispose(); 
            Seleccion Ventana = new Seleccion();
            Ventana.setVisible(true);
            setVisible(false);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "No se pudo eliminar: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }  
    }//GEN-LAST:event_ElimDocActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        
        try {
            var d1 = (java.util.Date) spDesde.getValue();
            var d2 = (java.util.Date) spHasta.getValue();

            var desde = toLocalDate(d1);
            var hasta = toLocalDate(d2);

            if (desde == null) {
                javax.swing.JOptionPane.showMessageDialog(this, "Debe ingresar la fecha de inicio.");
                return;
            }
            if (hasta != null && hasta.isBefore(desde)) {
                javax.swing.JOptionPane.showMessageDialog(this, "La fecha fin no puede ser anterior a la fecha inicio.");
                return;
            }
            var motivo = jTextField1.getText().trim();
            var grupos = jTextField3.getText().trim();
            if (motivo.isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(this, "Ingrese un motivo.");
                return;
            }

            var lic = new CapaLogica.Licencia();
            lic.setMotivo(motivo);
            lic.setGruposAfectados(grupos);
            lic.setFechaInicio(desde);
            lic.setFechaFin(hasta);
        
            int id = Consultar.guardarLicencia(ciDocente, lic);
            javax.swing.JOptionPane.showMessageDialog(this, (id > 0)
                ? "Licencia guardada (ID " + id + ")"
                : "Licencia guardada");

            cargarLicencias();     // refresca la JTable
            limpiarFormulario();   // opcional
        } catch (Exception ex) {
            javax.swing.JOptionPane.showMessageDialog(this,
             "Error guardando licencia:\n" + ex.getMessage(),
                "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnGuardarActionPerformed
    private void limpiarFormulario() {
    jTextField1.setText("");
    jTextField3.setText("");
    
}   
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(LicenciasPorDocente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(LicenciasPorDocente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(LicenciasPorDocente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LicenciasPorDocente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new LicenciasPorDocente().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton ElimDoc;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnRefrescar;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JLabel labelCedula;
    private javax.swing.JLabel labelNombre;
    private javax.swing.JSpinner spDesde;
    private javax.swing.JSpinner spHasta;
    private javax.swing.JTable tabla;
    // End of variables declaration//GEN-END:variables
}
