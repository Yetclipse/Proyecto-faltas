/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CapaLogica;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
    private String motivo;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private List<Grupo> gruposAfectados;

    public Licencia(String motivo, LocalDate fechaInicio, LocalDate fechaFin) {
        this.motivo = motivo;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.gruposAfectados = new ArrayList<>();
    }

    // Getters y Setters

    public void agregarGrupo(Grupo grupo) {
        gruposAfectados.add(grupo);
    }

    public List<Grupo> getGruposAfectados() {
        return gruposAfectados;
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
