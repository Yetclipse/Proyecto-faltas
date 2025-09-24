/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CapaLogica;

import CapaPersistencia.ConsultasFaltas;

/**
 *
 * @author sebas
 */
public class TestDAO {
    public static void main(String[] args) {
        ConsultasFaltas dao = new ConsultasFaltas();

        try {
            // 1. Guardar un docente
            Docente d = new Docente();
            d.setCedula("789456123");
            d.setNombre("Laura");
            d.setApellido("Pérez");
            d.setMateria("Matemática");
            d.setTurno("Matutino");
            dao.GuardarDocente(d);
            System.out.println("✅ Docente guardado");

            // 2. Guardar una licencia para ese docente
            Licencia l = new Licencia("Congreso",
                    java.time.LocalDate.of(2025, 10, 1),
                    java.time.LocalDate.of(2025, 10, 5),
                    "2°B");
            int idLic = dao.guardarLicencia("789456123", l);
            System.out.println("✅ Licencia guardada con id " + idLic);

            // 3. Consultar el docente con sus licencias
            Docente consultado = dao.ConsultarDocente("789456123");
            System.out.println("🔎 Docente: " + consultado.getNombre() + " " + consultado.getApellido());
            for (Licencia lic : consultado.getLicencias()) {
                System.out.println("   - Licencia " + lic.getId() + ": " +
                        lic.getMotivo() + " (" + lic.getFechaInicio() + " → " + lic.getFechaFin() + ")");
            }

            // 4. Eliminar al docente (opcional)
            String msg = dao.eliminarDocente("789456123");
            System.out.println("🗑️ " + msg);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
