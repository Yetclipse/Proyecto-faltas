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
public class Gestor {
    private List<Docente> docentes;
    private Admin adminActivo;

    public Gestor() {
        this.docentes = new ArrayList<>();
        
        adminActivo = new Admin("admin", "1234"); // tremenda contra xd
    }

    // Validación del administrador
    public boolean iniciarSesionAdmin(String user, String pass) {
        return adminActivo.validarSesion(user, pass);
    }

    // Agregar docente
    public boolean agregarDocente(Docente nuevo) {
        if (buscarPorCedula(nuevo.getCedula()) == null) {
            docentes.add(nuevo);
            return true;
        }
        return false; // ya existe
    }

    // Eliminar docente
    public boolean eliminarDocente(String cedula) {
        Docente d = buscarPorCedula(cedula);
        if (d != null) {
            docentes.remove(d);
            return true;
        }
        return false; //no existe
    }

    // Buscar docente
    public Docente buscarPorCedula(String cedula) {
        for (Docente d : docentes) { //Busca los docentes
            if (d.getCedula().equals(cedula)) {
                return d;
            } // y aca lo corrobora su ci
        }
        return null;
    }

    // Registrar licencia a un docente
    public boolean registrarLicencia(String cedula, Licencia licencia) {
        Docente d = buscarPorCedula(cedula); //Llamo al metodo para corroborar la cedula, si es retorna d que es docente
        if (d != null) {
            d.agregarLicencia(licencia);
            return true;
        }
        return false;
    }

    public List<Docente> getTodosLosDocentes() {
        return docentes;
    }
}

