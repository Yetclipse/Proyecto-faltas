/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CapaLogica;

/**Lee lo que dice licencia y veras pq está esta clase
 *
 * @author sebas
 */
public class Grupo {
    private String nombre;    // 2MA, 2MB, etc.
    private String turno;    // Matutino, Vespertino, Nocturno

    public Grupo(String nombre, String turno) {
        this.nombre = nombre;
        this.turno = turno;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }
    
}
