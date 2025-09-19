/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CapaLogica;
import CapaPersistencia.ConsultasFaltas;
import CapaExcepcion.BDexcepcion;
import CapaExcepcion.FaltasExcepcion;
/**
 *
 * @author sebas
 */
public class fachada {
    public void guardarDocente (Docente docente) throws Exception{
        ConsultasFaltas per = new ConsultasFaltas();
        per.GuardarDocente(docente);
    }
    public Docente ConsultarDocente(String ci) throws FaltasExcepcion,BDexcepcion, Exception {
        Docente per = new Docente();
        ConsultasFaltas docente = new ConsultasFaltas();
        per=docente.ConsultarDocente(ci);
    return per;
    }
    public void eliminarPersona (String ci) throws FaltasExcepcion, Exception{
        ConsultasFaltas docente = new ConsultasFaltas();
        docente.eliminarDocente(ci);
    }
}