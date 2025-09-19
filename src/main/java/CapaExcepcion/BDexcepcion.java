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
public class BDexcepcion extends Exception {
     public BDexcepcion(String string){
     super(string);
 }
}
