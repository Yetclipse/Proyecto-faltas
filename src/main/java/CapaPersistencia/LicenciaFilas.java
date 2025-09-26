/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CapaPersistencia;

/**
 *
 * @author sebas
 */
//Esta clase se encargara de armar la fila para mostrarla en la tabla
//Por eso LicenciaRow (Row = fila pero en ingles :v)
public class LicenciaFilas {
    private int id = 0;
    private final String cedula;
    private final String docente;
    private final String materia;
    private final String turno;
    private final String motivo;
    private final java.time.LocalDate desde;
    private final java.time.LocalDate hasta;
    private final String grupos;
    
    public LicenciaFilas(int id, String cedula, String docente, String materia, String turno,
                       String motivo, java.time.LocalDate desde, java.time.LocalDate hasta,
                       String grupos) {
        this.id = id;
        this.cedula = cedula;
        this.docente = docente;
        this.materia = materia;
        this.turno = turno;
        this.motivo = motivo;
        this.desde = desde;
        this.hasta = hasta;
        this.grupos = grupos;
    }
    public LicenciaFilas(String cedula, String docente, String materia, String turno,
                       String motivo, java.time.LocalDate desde, java.time.LocalDate hasta,
                       String grupos) {
        this.cedula = cedula;
        this.docente = docente;
        this.materia = materia;
        this.turno = turno;
        this.motivo = motivo;
        this.desde = desde;
        this.hasta = hasta;
        this.grupos = grupos;
    }
    public int getId() { return id; }
    public String getCedula() { return cedula; }
    public String getDocente() { return docente; }
    public String getMateria() { return materia; }
    public String getTurno() { return turno; }
    public String getMotivo() { return motivo; }
    public java.time.LocalDate getDesde() { return desde; }
    public java.time.LocalDate getHasta() { return hasta; }
    public String getGrupos() { return grupos; }
}