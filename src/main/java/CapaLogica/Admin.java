/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CapaLogica;

/**
 *
 * @author sebas
 */
    public class Admin {
    private String usuario;
    private String contraseña;

    public Admin(String usuario, String contraseña) {
        this.usuario = usuario;
        this.contraseña = contraseña;
    }
    public boolean validarSesion(String inputUsuario, String inputContraseña) {
        return usuario.equals(inputUsuario) && contraseña.equals(inputContraseña);
    }
    }
