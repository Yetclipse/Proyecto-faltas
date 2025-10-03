/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CapaExcepcion;

/**
 *
 * @author sebas
 */
public class FaltasExcepcion extends Exception {
    //Esto es un número que identifica la version de la clase
    //Cuando un objeto se serializa, Java guarda una "Firma" de la clase:
    //Nombres de atributos, métodos, tipos de datos, etc.
    //Cuando se intenta leer un objeto de vuelta, Java compara la firma con la clase acutal
    //Si no coinciden salta un invalidClassExcepction
    //Esto lo añadi para identificar los errores de mejor forma
    // El "1L" es el valor de la version si quiero otra version puedo usar 2L y asi sucesivamente
    private static final long serialVersionUID = 1L;

    public FaltasExcepcion(String message) {
        super(message);
    }

    public FaltasExcepcion(String message, Throwable cause) {
        super(message, cause); // encadena la causa real (SQLException)
    }
}

