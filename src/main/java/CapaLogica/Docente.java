/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CapaLogica;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author sebas
 */
public class Docente {
    private String cedula;
    private String nombre;
    private String apellido;
    private String materia;
    private String turno;
    private final List<Licencia> licencias = new ArrayList<>();

   
//Se registra un docente no necesariamente tiene que tener una licencia ya asociada en el momento de su creación, por lo tanto 
// los dos siguientes metodos funcionan para settear o retornar las licencias
    public void agregarLicencia(Licencia li) {
        if(li != null){
            licencias.add(li);
        }
    }

    public List<Licencia> getLicencias() {
        return licencias;
    }
 // Getters y Setters
    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getMateria() {
        return materia;
    }

    public void setMateria(String materia) {
        this.materia = materia;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

   
}
