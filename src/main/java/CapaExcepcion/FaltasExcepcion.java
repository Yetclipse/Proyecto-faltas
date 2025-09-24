/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CapaExcepcion;

import java.sql.SQLException;

/**
 *
 * @author sebas
 */
public class FaltasExcepcion extends Exception {
    private static final long serialVersionUID = 1L;

    public FaltasExcepcion(String message) {
        super(message);
    }

    public FaltasExcepcion(String message, Throwable cause) {
        super(message, cause); // encadena la causa real (SQLException)
    }
}

