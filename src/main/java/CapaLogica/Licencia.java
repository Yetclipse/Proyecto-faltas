/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CapaLogica;

import java.time.LocalDate;

/** Como se que vas a preguntar Fran aca esta la explicación de esta clase 
 * Cada licencia (o inasistencia) tiene su propia información: fecha de inicio, fecha de fin, motivo, y grupos afectados.

Por ejemplo:

Licencia l1 = new Licencia("Reposo médico", LocalDate.of(2025, 9, 1), LocalDate.of(2025, 9, 5));
lo mismo pasa con grupo tonto

* /
 *
 * @author sebas
 */
public class Licencia {
    private int id;
    private String motivo;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String gruposAfectados;

    public Licencia(){}
    public Licencia(int id, LocalDate fechaInicio, LocalDate fechaFin, String grupos, String motivo) {
        this.id = id;
        this.motivo = motivo;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.gruposAfectados = grupos; 
    }

    // Getters y Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGruposAfectados() {
        return gruposAfectados;
    }

    public void setGruposAfectados(String gruposAfectados) {
        this.gruposAfectados = gruposAfectados;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }
    
}
