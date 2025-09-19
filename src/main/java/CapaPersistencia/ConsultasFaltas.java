/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CapaPersistencia;

import CapaLogica.Docente;
import CapaExcepcion.BDexcepcion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import CapaExcepcion.FaltasExcepcion;
import CapaLogica.Licencia;
import java.sql.Array;
import java.util.List;
/**
 *
 * @author sebas
 */
public class ConsultasFaltas {
    private static final String SQLGuardar = ("INSERT INTO persona.personas(Ci,Nombre,Apellido, Materia, Turno, licencia)VALUES (?,?,?,?,?,?)");
    private static final String SQL_CONSULTAR_DOCENTE= ("SELECT * FROM persona.personas WHERE (CI=?)");
    private static final String SQL_ELIMINAR_DOCENTE= ("DELETE FROM personas WHERE CI=?");
    public BaseDeDatos cone = new BaseDeDatos();
    public PreparedStatement ps;
    public ResultSet rs;
    
    public void GuardarDocente (Docente docente) throws Exception, BDexcepcion {
    try{
        int resultado = 0; //Si da un numero negativo esque salio mal, y si es positivo es porque ingreso correctamente
        Connection con = cone.getConnection();//se conecto
        ps =(PreparedStatement) con.prepareStatement(SQLGuardar);
                
        ps.setString (1, docente.getCedula());
        ps.setString(2, docente.getNombre());
        ps.setString(3, docente.getApellido());
        ps.setString(4, docente.getMateria());
        ps.setString(5, docente.getTurno());
        ps.setArray(6, (Array) docente.getLicencias());
        
        resultado=ps.executeUpdate();
        con.close();
    }catch(SQLException SQLE){
        throw new Exception("Santiago Rosas dice que no te conectaste a la base de datos");
    }
    
    
}
    
    public Docente ConsultarDocente (String ci) throws Exception, BDexcepcion{
        Docente docente =new Docente();
        try{
            Connection con;
            con=cone.getConnection();
            ps=(PreparedStatement)con.prepareStatement(SQL_CONSULTAR_DOCENTE);
            ps.setString(1, ci);
            rs=ps.executeQuery();
            if(rs.next()){
                String Ci=rs.getString("CI");
                String nombre=rs.getString("Nombre");
                String apellido=rs.getString("Apellido");
                String materia=rs.getString("Materia");
                String turno=rs.getString("Turno");
                List<Array> licencia= (List<Array>)rs.getArray("Licencia");
                docente.setCedula(Ci);
                docente.setNombre(nombre);
                docente.setApellido(apellido);
                docente.setMateria(materia);
                docente.setTurno(turno);
                docente.agregarLicencia((Licencia) licencia);
                
            }else{
                throw new FaltasExcepcion("No encontrado");  
            }con.close();
        }catch (Exception e){
            throw new FaltasExcepcion("No encontrado");
        }
        return docente;
    }
    
    public void eliminarDocente (String ci) throws FaltasExcepcion, Exception{
        
        String Eliminacion = null;
        try{
            Connection conexion;
            conexion = cone.getConnection();
            ps = conexion.prepareStatement(SQL_ELIMINAR_DOCENTE);
            ps.setString(1, ci);
            int resultado = ps.executeUpdate();
            if(rs.next()){
                Eliminacion = "Mision cumplida. El docente ya no lo molestara";                
            }else{
                Eliminacion = "El docente que desea eliminar no se ha encontrado, alguien ya se ha encargado.";
            }
            conexion.close();
        } catch (Exception e){
            System.out.println(e);
        }
    }
}
