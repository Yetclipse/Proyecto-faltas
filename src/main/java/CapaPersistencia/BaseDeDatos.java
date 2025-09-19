/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CapaPersistencia;

import CapaExcepcion.BDexcepcion;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 *
 * @author sebas
 */
public class BaseDeDatos {
    public static Connection getConnection() throws BDexcepcion{
    Connection con=null;
    try{
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/proyectosfaltas?zeroDateTimeBehavior=CONVERT_TO_NULL","root","");
    }catch (SQLException sqle){
        throw new BDexcepcion("Error de conexion de base de datos");
    }
    return con;
}
}
