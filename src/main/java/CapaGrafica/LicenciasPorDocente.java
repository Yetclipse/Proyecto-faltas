/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package CapaGrafica;

import CapaPersistencia.ConsultasFaltas;
import CapaExcepcion.BDexcepcion;
import CapaExcepcion.FaltasExcepcion;
import CapaPersistencia.LicenciaFilas;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
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
    
    ){
        @Override public boolean isCellEditable(int r, int c) { return false; } // solo lectura
    };
    private final DefaultListModel<String> modeloGrupos = new DefaultListModel<>();
    private java.util.List<String> gruposBase = new java.util.ArrayList<>();
    // Mantener selección aunque se filtre
    private final java.util.Set<String> seleccionFija = new java.util.LinkedHashSet<>();
    private java.time.LocalDate rangoDesde = null;
    private java.time.LocalDate rangoHasta = null;
    private boolean esperandoHasta = false; //Esto para que sea un click desde y segundo click hasta


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
        listGrupos.setModel(modeloGrupos);
        listGrupos.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        listGrupos.setSelectionModel(new javax.swing.DefaultListSelectionModel() {
        @Override 
        public void setSelectionInterval(int index0, int index1) {
            if (isSelectedIndex(index0)) {
               removeSelectionInterval(index0, index1);
            } else {
                addSelectionInterval(index0, index1);
            }
        }
        });
        // Hacer click para alternar (tipo checkbox) – lo que ya tienes:
        listGrupos.setSelectionModel(new javax.swing.DefaultListSelectionModel() {
        @Override 
        public void setSelectionInterval(int i0, int i1) {
        if (isSelectedIndex(i0)) removeSelectionInterval(i0, i1);
        else                      addSelectionInterval(i0, i1);
    }
});

    // Mantener el set actualizado cuando el usuario cambia la selección visible
    listGrupos.addListSelectionListener(e -> {
        if (e.getValueIsAdjusting()) return;

        // Qué valores están seleccionados (de los visibles)
        java.util.Set<String> visiblesSel =
            new java.util.HashSet<>(listGrupos.getSelectedValuesList());

        // Quita del set los visibles que acaban de des-seleccionarse
        for (int i = 0; i < modeloGrupos.size(); i++) {
            String v = modeloGrupos.getElementAt(i);
            if (seleccionFija.contains(v) && !visiblesSel.contains(v)) {
                seleccionFija.remove(v);
            }
        }
        // Añade al set los visibles recién seleccionados
        seleccionFija.addAll(visiblesSel);
    });

        cargarGruposBase();
        aplicarFiltro("");
        // Buscador en vivo (ignora mayúsculas/minúsculas)
        txtBuscarGrupo.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
        private void filtra() { aplicarFiltro(txtBuscarGrupo.getText()); }
        @Override public void insertUpdate(javax.swing.event.DocumentEvent e){ filtra(); }
        @Override public void removeUpdate(javax.swing.event.DocumentEvent e){ filtra(); }
        @Override public void changedUpdate(javax.swing.event.DocumentEvent e){ filtra(); }
    });

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
        // Selección de rango con 2 clics en el calendario
        jCalendar1.getDayChooser().addPropertyChangeListener("day", evt -> {
        java.util.Date sel = jCalendar1.getDate();
        java.time.LocalDate d = toLocalDate(sel);
        
        if (d == null) return;

        if (rangoDesde == null || !esperandoHasta) {
            // 1er clic: fijamos DESDE y "abrimos" el rango
            rangoDesde = d;
            rangoHasta = null;
            esperandoHasta = true;
        } else {
            // 2º clic: fijamos HASTA y cerramos el rango
            if (d.isBefore(rangoDesde)) { //before o sea antes del dia ya seleccionado
                // si el usuario clicó una fecha anterior, invertimos
                rangoHasta = rangoDesde;
                rangoDesde = d;
                } else {
                rangoHasta = d;
            }
            esperandoHasta = false;
        }
        actualizarEtiquetaRango();
        });

    }
    
    
    private void cargarLicencias() {
        modelo.setRowCount(0);
        try {
            java.util.List<LicenciaFilas> filas = Consultar.listarLicenciasPorDocente(ciDocente);
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
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        btnRefrescar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        ElimDoc = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        btnGuardar = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jCalendar1 = new com.toedter.calendar.JCalendar();
        jScrollPane2 = new javax.swing.JScrollPane();
        listGrupos = new javax.swing.JList<>();
        txtBuscarGrupo = new javax.swing.JTextField();
        lblRango = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();

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
        jLabel5.setText("Selecciona uno");

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
        jLabel7.setText("Grupos Afectados");

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

        jScrollPane2.setViewportView(listGrupos);

        jButton2.setText("Limpiar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel9))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(58, 58, 58)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(8, 8, 8)
                                .addComponent(jLabel6))
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jCalendar1, javax.swing.GroupLayout.PREFERRED_SIZE, 422, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 37, Short.MAX_VALUE)
                        .addComponent(jButton2)
                        .addGap(56, 56, 56)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(43, 43, 43)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtBuscarGrupo, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(89, 89, 89)
                                        .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGap(93, 93, 93))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(82, 82, 82)
                        .addComponent(lblRango, javax.swing.GroupLayout.PREFERRED_SIZE, 285, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1)
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
                        .addGap(28, 28, 28)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 487, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(155, 155, 155)
                        .addComponent(ElimDoc))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(183, 183, 183)
                        .addComponent(labelNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(102, 102, 102)
                        .addComponent(labelCedula, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(104, 104, 104))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jButton1)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(84, 84, 84)
                        .addComponent(btnRefrescar)
                        .addGap(35, 35, 35)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(25, 25, 25)
                        .addComponent(btnEliminar))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(46, 46, 46)
                        .addComponent(ElimDoc))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(labelNombre)
                            .addComponent(labelCedula))
                        .addGap(37, 37, 37)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblRango, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCalendar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(117, 117, 117)
                                .addComponent(jLabel6)
                                .addGap(16, 16, 16)
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(107, 107, 107)
                                        .addComponent(jButton2))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(102, 102, 102)
                                        .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(30, 30, 30)
                                        .addComponent(txtBuscarGrupo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addContainerGap())))
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
                "¿Eliminar el docente seleccionada?", "Confirmar",
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
            // Validar rango del calendario
            if (rangoDesde == null) {
                javax.swing.JOptionPane.showMessageDialog(this, "Seleccione al menos una fecha en el calendario.");
                return;
            }
            java.time.LocalDate desde = rangoDesde;
            java.time.LocalDate hasta  = (rangoHasta != null ? rangoHasta : rangoDesde);

            String motivo = jTextField1.getText().trim();
            if (motivo.isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(this, "Ingrese un motivo.");
                return;
            }

            // Grupos elegidos 
            java.util.List<String> gruposSel = new java.util.ArrayList<>(seleccionFija);
            if (gruposSel.isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(this, "Seleccione al menos un grupo.");
                return;
            }

            // Armar y guardar
            var lic = new CapaLogica.Licencia();
            lic.setMotivo(motivo);
            lic.setFechaInicio(desde);
            lic.setFechaFin(hasta);
            lic.setGrupos(gruposSel);

            int id = Consultar.guardarLicenciaConGrupos(ciDocente, lic, gruposSel);
            javax.swing.JOptionPane.showMessageDialog(this, (id > 0)
                ? "Licencia guardada (ID " + id + ")"
                : "Licencia guardada");

            // Refrescar 
            cargarLicencias();
            // reset de rango para evitar confusiones
            rangoDesde = null;
            rangoHasta = null;
            esperandoHasta = false;
            actualizarEtiquetaRango();
            jTextField1.setText("");
            
        } catch (Exception ex) {
            javax.swing.JOptionPane.showMessageDialog(this,
             "Error guardando licencia:\n" + ex.getMessage(),
                "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        limpiarFormulario();
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        rangoDesde = null;
        rangoHasta = null;
        esperandoHasta = false;
        actualizarEtiquetaRango();
    }//GEN-LAST:event_jButton2ActionPerformed
    private void limpiarFormulario() {
    jTextField1.setText("");
    listGrupos.clearSelection();
    
    }
        
    private void cargarGruposBase() {
    gruposBase.clear();
    // Si los tenés fijos:
    gruposBase.addAll(java.util.Arrays.asList(
        "1MA", "1MB", "1MC", "1MD", "1ME", "1MF", "1MG", "1MH", "1MI", "1MJ", "1MK", "1ML", "1MM", "1MN", "1MO", "1MP", "1MQ", "1MR", "1MS", "1MT", "1MU", "1MV", "1MW", "1MX", "1MY", "1MZ",
        "2MA", "2MB", "2MC", "2MD", "2ME", "2MF", "2MG", "2MH", "2MI", "2MJ", "2MK", "2ML", "2MM", "2MN", "2MO", "2MP", "2MQ", "2MR", "2MS", "2MT", "2MU", "2MV", "2MW", "2MX", "2MY", "2MZ",
        "3MA", "3MB", "3MC", "3MD", "3ME", "3MF", "3MG", "3MH", "3MI", "3MJ", "3MK", "3ML", "3MM", "3MN", "3MO", "3MP", "3MQ", "3MR", "3MS", "3MT", "3MU", "3MV", "3MW", "3MX", "3MY", "3MZ"
    ));
    }
    private void aplicarFiltro(String q) {
        
    String needle = (q == null ? "" : q.trim().toLowerCase());

    // Reconstruir el modelo con el filtro
    modeloGrupos.clear();//limpia
    for (String g : gruposBase) {//recorre los grupos
        if (needle.isEmpty() || g.toLowerCase().contains(needle)) {
            modeloGrupos.addElement(g);//Agrega los elementos buscados
        }
    }

    // Reaplicar selección, esto ayuda a evitar que no se pierda lo seleccionado incluso cuando vaya a buscar otro grupo
    java.util.List<Integer> idx = new java.util.ArrayList<>();
    for (int i = 0; i < modeloGrupos.size(); i++) {
        if (seleccionFija.contains(modeloGrupos.getElementAt(i))) {
            idx.add(i);
        }
    }
    // Pasar la lista en un int[] porque setSelectedIndices lo requiere asi
    int[] indices = new int[idx.size()];
    for (int i = 0; i < idx.size(); i++) indices[i] = idx.get(i);
    listGrupos.setSelectedIndices(indices);
    }
    
    private void actualizarEtiquetaRango() {
    String t;
    if (rangoDesde == null) {
        t = "Rango: (sin seleccionar)";
    } else if (rangoHasta == null) {
        t = "Rango: " + rangoDesde.format(DF) + " — ?";
    } else {
        t = "Rango: " + rangoDesde.format(DF) + " — " + rangoHasta.format(DF);
    }
    lblRango.setText(t);
    }

    
    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton ElimDoc;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnRefrescar;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private com.toedter.calendar.JCalendar jCalendar1;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JLabel labelCedula;
    private javax.swing.JLabel labelNombre;
    private javax.swing.JLabel lblRango;
    private javax.swing.JList<String> listGrupos;
    private javax.swing.JTable tabla;
    private javax.swing.JTextField txtBuscarGrupo;
    // End of variables declaration//GEN-END:variables
}
