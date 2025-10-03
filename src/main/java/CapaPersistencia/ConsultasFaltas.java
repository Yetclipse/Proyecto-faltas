/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CapaPersistencia;

import CapaLogica.Docente;
import CapaExcepcion.BDexcepcion;
import java.sql.*;
import CapaExcepcion.FaltasExcepcion;
import CapaLogica.Licencia;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author sebas
 */
public class ConsultasFaltas {
    //help me no queria poner tantas consultas :(
    // DOCENTES
private static final String SQL_GUARDAR_DOCENTE =
    "INSERT INTO proyectofaltas.docentes(Cedula, Nombre, Apellido, Materia, Turno) VALUES (?,?,?,?,?)";

private static final String SQL_CONSULTAR_DOCENTE =
    "SELECT * FROM proyectofaltas.docentes WHERE Cedula = ?";

private static final String SQL_ELIMINAR_DOCENTE =
    "DELETE FROM proyectofaltas.docentes WHERE Cedula = ?";

private static final String SQL_LISTAR_DOCENTES =
    "SELECT * FROM proyectofaltas.docentes ORDER BY Apellido, Nombre";

// LICENCIAS sin Grupos_afectados
private static final String SQL_GUARDAR_LICENCIA =
    "INSERT INTO proyectofaltas.licencias (docente_ci, Motivo, Fecha_inicio, Fecha_fin) VALUES (?, ?, ?, ?)";

private static final String SQL_LISTAR_LICENCIAS_POR_DOCENTE =
    "SELECT l.id, l.Motivo, l.Fecha_inicio, l.Fecha_fin, " +
    "COALESCE(GROUP_CONCAT(lg.grupo_codigo ORDER BY lg.grupo_codigo SEPARATOR '; '), '') AS Grupos " +
    "FROM proyectofaltas.licencias l " +
    "LEFT JOIN proyectofaltas.licencias_grupos lg ON lg.licencia_id = l.id " +
    "WHERE l.docente_ci = ? " +
    "GROUP BY l.id " +
    "ORDER BY l.Fecha_inicio DESC";

private static final String SQL_ELIMINAR_LICENCIA =
    "DELETE FROM proyectofaltas.licencias WHERE id = ? AND docente_ci = ?";

// Tabla licencia ↔ grupos
private static final String SQL_INSERT_LIC_GRUPO =
    "INSERT INTO proyectofaltas.licencias_grupos (licencia_id, grupo_codigo) VALUES (?, ?)";

private static final String SQL_LISTAR_GRUPOS_DE_LICENCIA =
    "SELECT grupo_codigo FROM proyectofaltas.licencias_grupos WHERE licencia_id = ? ORDER BY grupo_codigo";

// Listado para UI (últimos 30 días) juntando los grupos
// Listar licencias para la Jtable
    // el CURDATE() funciona para que el intervalo sea de 30 dias y no listar TODAS las inasistencias  
private static final String SQL_LISTAR_FALTA =
    "SELECT d.Cedula, d.Nombre, d.Apellido, d.Materia, d.Turno, " +
    "       l.id, l.Motivo, l.Fecha_inicio, l.Fecha_fin, " +
    "       COALESCE(GROUP_CONCAT(lg.grupo_codigo ORDER BY lg.grupo_codigo SEPARATOR '; '), '') AS Grupos " +
    "FROM proyectofaltas.licencias l " +
    "JOIN proyectofaltas.docentes d ON d.Cedula = l.docente_ci " +
    "LEFT JOIN proyectofaltas.licencias_grupos lg ON lg.licencia_id = l.id " +
    "WHERE l.Fecha_inicio >= CURDATE() - INTERVAL 30 DAY " +
    "GROUP BY l.id " +
    "ORDER BY l.Fecha_inicio DESC";

    public BaseDeDatos cone = new BaseDeDatos();
    public PreparedStatement ps;
    public ResultSet rs;
    
    // DOCENTES
    public void GuardarDocente (Docente docente) throws FaltasExcepcion, Exception, BDexcepcion {
    
        try{
        int resultado = 0; //Si da un numero negativo esque salio mal, y si es positivo es porque ingreso correctamente
        Connection con = cone.getConnection();//se conecto
        ps =(PreparedStatement) con.prepareStatement(SQL_GUARDAR_DOCENTE);
                
        ps.setString (1, docente.getCedula());
        ps.setString(2, docente.getNombre());
        ps.setString(3, docente.getApellido());
        ps.setString(4, docente.getMateria());
        ps.setString(5, docente.getTurno());
        
        resultado=ps.executeUpdate();
        con.close();
    } catch (SQLException e) {
    String detalle = "SQLState=" + e.getSQLState() + ", code=" + e.getErrorCode() + ", msg=" + e.getMessage();
    throw new FaltasExcepcion("Santiago te dice tonto hay un error al guardar docente: " + detalle, e);
}
    
    
}
    // Devuelve el docente con TODAS sus licencias cargadas.
public Docente ConsultarDocente(String ci) throws FaltasExcepcion, BDexcepcion {
    Docente docente;
    try (Connection con = cone.getConnection()) {

        // Docente
        try (PreparedStatement ps = con.prepareStatement(SQL_CONSULTAR_DOCENTE)) {
            ps.setString(1, ci);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) throw new FaltasExcepcion("No se encontró docente " + ci);
                docente = new Docente();
                docente.setCedula(rs.getString("Cedula"));
                docente.setNombre(rs.getString("Nombre"));
                docente.setApellido(rs.getString("Apellido"));
                docente.setMateria(rs.getString("Materia"));
                docente.setTurno(rs.getString("Turno"));
            }
        }

        // Licencias
        try (PreparedStatement psL = con.prepareStatement(SQL_LISTAR_LICENCIAS_POR_DOCENTE)) {
            psL.setString(1, ci);
            try (ResultSet rl = psL.executeQuery()) {
                while (rl.next()) {
                    Licencia lic = new Licencia();
                    lic.setId(rl.getInt("id"));
                    lic.setMotivo(rl.getString("Motivo"));

                    java.sql.Date fi = rl.getDate("Fecha_inicio");
                    java.sql.Date ff = rl.getDate("Fecha_fin");
                    lic.setFechaInicio(fi != null ? fi.toLocalDate() : null);
                    lic.setFechaFin(ff != null ? ff.toLocalDate() : null);
                    
                    lic.setGruposDesdeCadena(rl.getString("Grupos"));
                    // Traer grupos de esa licencia
                    try (PreparedStatement psG = con.prepareStatement(SQL_LISTAR_GRUPOS_DE_LICENCIA)) {
                        psG.setInt(1, lic.getId());
                        try (ResultSet rg = psG.executeQuery()) {
                            java.util.List<String> grupos = new java.util.ArrayList<>();
                            while (rg.next()) {
                                grupos.add(rg.getString("grupo_codigo"));
                            }
                            lic.setGrupos(grupos); // << ahora sí, lista de String
                        }
                    }
                    docente.agregarLicencia(lic);
                }
            }
        }

    } catch (SQLException e) {
        throw new FaltasExcepcion("Error consultando docente/licencias: " + e.getMessage(), e);
    }
    return docente;
}

    
    public String eliminarDocente(String ci) throws FaltasExcepcion, BDexcepcion {
    int MYSQL_FK_ERROR = 1451; // Código de error: no se puede borrar porque tiene licencias asociadas

    try (Connection con = cone.getConnection();
         PreparedStatement ps = con.prepareStatement(SQL_ELIMINAR_DOCENTE)) {

        ps.setString(1, ci);
        int filas = ps.executeUpdate(); // cuántos docentes se borraron

        if (filas > 0) {
            return "Misión cumplida. El docente ya no lo molestará";
        } else {
            return "El docente que desea eliminar no se ha encontrado... alguien ya se encargó antes️";
        }

    } catch (SQLException e) {
        if (e.getErrorCode() == MYSQL_FK_ERROR) {
            throw new FaltasExcepcion("No se puede eliminar al docente porque tiene licencias activas. Primero elimine sus licencias");
        }
        throw new FaltasExcepcion("Error inesperado eliminando docente "+ e.getMessage(), e);
    }
}

    
    // LICENCIAS
    
    //Listar docente para una lista de la capa grafica (estoy en la locura chicos)
    public java.util.List<Docente> listarDocentes() throws FaltasExcepcion, BDexcepcion, SQLException{
        java.util.List<Docente> out = new java.util.ArrayList<>();
        //Y dale con el try
        try(Connection con = cone.getConnection();
                PreparedStatement ps = con.prepareStatement(SQL_LISTAR_DOCENTES);
                ResultSet rs = ps.executeQuery()){
            //Esto ya lo habia explicado pero como reconozco que soy olvidadizo
            //como rl.next() devuelve 1(esta en una fila) o 0(no esta en una fila) el while seguira mientras haya filas cuando llegue al final 
            // o no haya filas queda en 0 que eso es false y ahi se rompe
            while(rs.next()){
                Docente d = new Docente();
                d.setCedula(rs.getString("Cedula"));
                d.setNombre(rs.getString("Nombre"));
                d.setApellido(rs.getString("Apellido"));
                d.setMateria(rs.getString("Materia"));
                d.setTurno(rs.getString("Turno"));
                out.add(d);
            }
        }catch(java.sql.SQLException e){
            throw new FaltasExcepcion("Error listando docentes"+ e.getMessage(), e);
        }
        return out;
    }      
    
public java.util.List<LicenciaFilas> listarParaTabla() throws BDexcepcion {
    java.util.List<LicenciaFilas> out = new java.util.ArrayList<>();
    try (Connection con = cone.getConnection();
         PreparedStatement ps = con.prepareStatement(SQL_LISTAR_FALTA);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            String cedula  = rs.getString("Cedula");
            String nombre  = rs.getString("Nombre");
            String apellido= rs.getString("Apellido");
            String docente = (nombre != null ? nombre : "") + (apellido != null ? " " + apellido : "");
            String materia = rs.getString("Materia");
            String turno   = rs.getString("Turno");
            String motivo  = rs.getString("Motivo");
            java.sql.Date fi = rs.getDate("Fecha_inicio");
            java.sql.Date ff = rs.getDate("Fecha_fin");
            java.time.LocalDate desde = (fi != null) ? fi.toLocalDate() : null;
            java.time.LocalDate hasta = (ff != null) ? ff.toLocalDate() : null;
            String grupos  = rs.getString("Grupos"); // viene del GROUP_CONCAT

            out.add(new LicenciaFilas(cedula, docente, materia, turno, motivo, desde, hasta, grupos));
        }
    } catch (SQLException e) {
        throw new RuntimeException("Error listando licencias", e);
    }
    return out;
}

    
// Esto funciona para mostras la licencias de un docente (solo licencias no los datos del docente)
public java.util.List<LicenciaFilas> listarLicenciasPorDocente(String ci) throws FaltasExcepcion, BDexcepcion {
    java.util.List<LicenciaFilas> out = new java.util.ArrayList<>();
    try (Connection con = cone.getConnection();
        PreparedStatement ps = con.prepareStatement(SQL_LISTAR_LICENCIAS_POR_DOCENTE)) {

        ps.setString(1, ci);
        try (java.sql.ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                var id = rs.getInt("id");
                var mot = rs.getString("Motivo");
                var fi  = rs.getDate("Fecha_inicio");
                var ff  = rs.getDate("Fecha_fin");
                var grp = rs.getString("Grupos");

                LicenciaFilas row = new LicenciaFilas(
                    id,
                    ci,               // cedula
                    "",               // docente no se usara para mostrar la licencia
                    "",               // materia no se usara para mostrar la licencia
                    "",               // turno no se usara para mostrar la licencia
                    mot,
                    fi != null ? fi.toLocalDate() : null,
                    ff != null ? ff.toLocalDate() : null,
                    grp
                );
                out.add(row);
            }
        }
    } catch (java.sql.SQLException e) {
        throw new FaltasExcepcion("Error listando licencias por docente: " + e.getMessage(), e);
    }
    return out;
}

//Eliminar 1 licencia (por seguridad valida la cédula + id)
public String eliminarLicenciaDeDocente(String ci, int idLicencia) throws FaltasExcepcion, BDexcepcion, SQLException{
    try (Connection con = cone.getConnection();
        PreparedStatement ps = con.prepareStatement(SQL_ELIMINAR_LICENCIA)){
        ps.setInt(1, idLicencia);
        ps.setString(2, ci);
        int filas = ps.executeUpdate(); //1 si borro
        if (filas > 0) {
            return "Misión cumplida.";
        } else {
            return "Hubo un error y la verdad no se... Cualquier cosa llamar a Sebastien";
        }
    }catch (java.sql.SQLException e){
        throw new FaltasExcepcion("Error eliminando licencia: " + e.getMessage(), e);
    }
    
}
public int guardarLicenciaConGrupos(String docenteCi, Licencia lic, java.util.List<String> grupos)
        throws FaltasExcepcion, BDexcepcion {
    try (Connection con = cone.getConnection()) {
        con.setAutoCommit(false);
        int licenciaId;

        // 1) Insert la licencia (sin grupos)
        try (PreparedStatement psLic = con.prepareStatement(
                SQL_GUARDAR_LICENCIA, Statement.RETURN_GENERATED_KEYS)) {

            psLic.setString(1, docenteCi);
            psLic.setString(2, lic.getMotivo());
            psLic.setDate(3, lic.getFechaInicio() != null ? java.sql.Date.valueOf(lic.getFechaInicio()) : null);
            psLic.setDate(4, lic.getFechaFin()    != null ? java.sql.Date.valueOf(lic.getFechaFin())    : null);

            psLic.executeUpdate();
            try (ResultSet keys = psLic.getGeneratedKeys()) {
                if (!keys.next()) throw new SQLException("No se generó ID de licencia");
                licenciaId = keys.getInt(1);
            }
        }

        // 2) Insert vínculos licencia ↔ grupo
        if (grupos != null && !grupos.isEmpty()) {
            try (PreparedStatement psLG = con.prepareStatement(SQL_INSERT_LIC_GRUPO)) {
                for (String cod : grupos) {
                    if (cod == null || cod.isBlank()) continue;
                    psLG.setInt(1, licenciaId);
                    psLG.setString(2, cod.trim());
                    psLG.addBatch();
                }
                psLG.executeBatch();
            }
        }

        con.commit();
        lic.setId(licenciaId);
        return licenciaId;
        
    } catch (SQLException e) {
        throw new FaltasExcepcion("Error guardando licencia/grupos: " + e.getMessage(), e);
    }
}   private java.util.List<String> cargarGrupos(Connection con, int licenciaId) throws SQLException {
    java.util.List<String> gs = new java.util.ArrayList<>();
    try (PreparedStatement ps = con.prepareStatement(SQL_LISTAR_GRUPOS_DE_LICENCIA)) {
        ps.setInt(1, licenciaId);
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) gs.add(rs.getString("grupo_codigo"));
        }
    }
    return gs;
}


}
