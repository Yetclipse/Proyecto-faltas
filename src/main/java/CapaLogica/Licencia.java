/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CapaLogica;

import java.time.LocalDate;

/** Como se que vas a preguntar Fran aca esta la explicación de esta clase 
 * Cada licencia (o inasistencia) tiene su propia información: fecha de inicio, fecha de fin, motivo, y grupos afectados.

Por ejemplo:

Licencia l1 = new Licencia("Reposo médico", LocalDate.of(2025, 9, 1), LocalDate.of(2025, 9, 5), grupo);

* /
 *
 * @author sebas
 */
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Licencia {
    private int id;                     // es autoincrement
    private String motivo;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private java.util.List<String> grupos = new java.util.ArrayList<>();

    public Licencia() {} //por si acaso uno vacio

    public Licencia(String motivo, LocalDate fechaInicio, LocalDate fechaFin, List<String> grupos) {
        this.motivo = motivo;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        setGrupos(grupos);
        validarFechas(); //es un metodo que esta creado por alla abajo
    }

    // Constructor completo (para leer desde la BD)
    public Licencia(int id, String motivo, LocalDate fechaInicio, LocalDate fechaFin, List<String> grupos) {
        this.id = id;
        this.motivo = motivo;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        setGrupos(grupos);
        validarFechas(); //es un metodo que esta creado por alla abajo
    }
    public Licencia(String motivo, LocalDate fechaInicio, LocalDate fechaFin, String gruposConcatenados) {
        this.motivo = motivo;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        setGruposDesdeCadena(gruposConcatenados);
        validarFechas();
    }

    // --- Getters/Setters ---

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public List<String> getGrupos() { return grupos; }
    public void setGrupos(List<String> grupos) {
        this.grupos = (grupos == null) ? new ArrayList<>() : new ArrayList<>(grupos);
    }
    public void addGrupo(String g) {
        if (g != null) {
            String t = g.trim();
            if (!t.isEmpty()) grupos.add(t);
        }
    }

    // Helpers de compatibilidad/UI
    public String getGruposConcatenados() {
        return String.join("; ", grupos);
    }
    public void setGruposDesdeCadena(String gruposConcatenados) {
        this.grupos = new ArrayList<>();
        if (gruposConcatenados == null || gruposConcatenados.trim().isEmpty()) return;
        Arrays.stream(gruposConcatenados.split(";"))
              .map(String::trim)
              .filter(s -> !s.isEmpty())
              .forEach(grupos::add);
    }
    

    // para asegurr de que el usuario no sea tonto y ponta que termina antes de la fecha de inicio
    private void validarFechas() throws IllegalArgumentException {
        if (fechaInicio != null && fechaFin != null && fechaFin.isBefore(fechaInicio)) {
            throw new IllegalArgumentException("La fecha de fin no puede ser anterior a la fecha de inicio");
        }
    }


    @Override
    public String toString() {
        return "Licencia{id=" + id + ", motivo='" + motivo + '\'' +
               ", desde=" + fechaInicio + ", hasta=" + fechaFin +
               ", grupos='" + grupos + "'}";
    }
}
