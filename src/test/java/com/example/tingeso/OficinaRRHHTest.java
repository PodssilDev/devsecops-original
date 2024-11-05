package com.example.tingeso;

import com.example.tingeso.entities.EmpleadoEntity;
import com.example.tingeso.entities.JustificativoEntity;
import com.example.tingeso.entities.OficinaRRHHEntity;
import com.example.tingeso.repositories.EmpleadoRepository;
import com.example.tingeso.repositories.JustificativoRepository;
import com.example.tingeso.repositories.OficinaRRHHRepository;
import com.example.tingeso.repositories.SubirDataRepository;
import com.example.tingeso.services.OficinaRRHHService;
import com.example.tingeso.services.SubirDataService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class OficinaRRHHTest {

    @Autowired
    OficinaRRHHService oficinaService;

    @Autowired
    OficinaRRHHRepository oficinaRepository;

    @Autowired
    EmpleadoRepository empleadoRepository;

    @Autowired
    SubirDataService data;

    @Autowired
    SubirDataRepository dataRepository;

    @Autowired
    JustificativoRepository justificativoRepository;

    @Test
    void testExtraCategoriaA(){
        double sueldo = oficinaService.extraCategoria("A", 99);
        assertEquals(2475000.0, sueldo, 0.0);
    }

    @Test
    void testExtraCategoriaB(){
        double sueldo = oficinaService.extraCategoria("B", 10);
        assertEquals(200000.0,sueldo,0.0);
    }

    @Test
    void testExtraCategoriaC(){
        double sueldo = oficinaService.extraCategoria("C", 5);
        assertEquals(50000.0, sueldo, 0.0);
    }

    @Test
    void testExtraCategoriaX(){
        double sueldo = oficinaService.extraCategoria("X", 3);
        assertEquals(0.0, sueldo, 0.0);
    }

    @Test
    void testCalcularBonificacionDedicacion1() {
        double bonificacion = oficinaService.calcularBonificacionDedicacion(1700000, 8);
        assertEquals(85000.0, bonificacion, 0.0);
    }

    @Test
    void testCalcularBonificacionDedicacion2(){
        double bonificacion = oficinaService.calcularBonificacionDedicacion(1200000, 12);
        assertEquals(96000.0, bonificacion, 0.0);
    }

    @Test
    void testCalcularBonificacionDedicacion3(){
        double bonificacion = oficinaService.calcularBonificacionDedicacion(800000, 18);
        assertEquals(88000.0, bonificacion, 0.0);
    }

    @Test
    void testCalcularBonificacionDedicacion4(){
        double bonificacion = oficinaService.calcularBonificacionDedicacion(1700000, 21);
        assertEquals(238000.0, bonificacion, 0.1);
    }

    @Test
    void testCalcularBonificacionDedicacion5(){
        double bonificacion = oficinaService.calcularBonificacionDedicacion(1200000, 28);
        assertEquals(204000.0, bonificacion, 0.1);
    }

    @Test
    void testCalcularBonificacionDedicacion6(){
        double bonificacion = oficinaService.calcularBonificacionDedicacion(-10000, 2);
        assertEquals(0.0, bonificacion, 0.0);
    }

    @Test
    void testCalcularBonificacionDedicacion7(){
        double bonificacion = oficinaService.calcularBonificacionDedicacion(800000, 0);
        assertEquals(0.0, bonificacion, 0.0);
    }

    @Test
    void testContarHoras() throws ParseException {
        String hora = "20:00";
        Integer contador = oficinaService.contarHoras(hora);
        assertEquals(2, contador, 0.0);
    }

    @Test
    void testMetodosCalculos() throws ParseException {
        EmpleadoEntity empleado = new EmpleadoEntity();
        empleado.setRut("942-8");
        empleado.setCategoria("A");
        empleado.setNombres("J");
        empleado.setApellidos("S C");
        empleado.setFecha_ingreso("2010/06/12");
        empleadoRepository.save(empleado);
        data.insertarData(empleado.getRut(), "2022/06/01");
        OficinaRRHHEntity empleado_reporte = new OficinaRRHHEntity();
        empleado_reporte.setRut(empleado.getRut());
        empleado_reporte.setNombre_empleado(empleado.getApellidos() + " " + empleado.getNombres());
        empleado_reporte.setCategoria(empleado.getCategoria());
        empleado_reporte.setDedicacion(oficinaService.calcularDedicacion(empleado.getFecha_ingreso(), data.obtenerFechaRut(empleado.getRut())));
        empleado_reporte.setSueldo_mensual(oficinaService.obtenerSueldo(empleado.getCategoria()));
        empleado_reporte.setBonificacion_dedicacion(oficinaService.calcularBonificacionDedicacion(empleado_reporte.getSueldo_mensual(), empleado_reporte.getDedicacion()));
        empleado_reporte.setHoras_extras(oficinaService.calcularMontoExtra(empleado.getRut(), data.obtenerFechaRut(empleado.getRut())));
        empleado_reporte.setDescuentos(oficinaService.comprobarDescuentos(empleado.getRut(), data.obtenerFechaRut(empleado.getRut())));
        empleado_reporte.setSueldo_bruto(oficinaService.calcularSueldoBruto(empleado_reporte));
        empleado_reporte.setPrevisional(empleado_reporte.getSueldo_bruto() * 0.1);
        empleado_reporte.setSalud(empleado_reporte.getSueldo_bruto() * 0.08);
        empleado_reporte.setSueldo_final(empleado_reporte.getSueldo_bruto() - empleado_reporte.getPrevisional() - empleado_reporte.getSalud());
        assertEquals(1463700.0, empleado_reporte.getSueldo_final(), 0.0);
        data.eliminarData(data.obtenerData(empleado.getRut()));
        empleadoRepository.delete(empleado);
    }

    @Test
    void testCalculoFull() throws ParseException {
        EmpleadoEntity empleado = new EmpleadoEntity();
        empleado.setRut("942-8");
        empleado.setCategoria("B");
        empleado.setNombres("J");
        empleado.setApellidos("S C");
        empleado.setFecha_ingreso("2010/06/12");
        empleadoRepository.save(empleado);
        data.insertarData(empleado.getRut(), "2022/06/01");
        oficinaService.calculoPlanilla(empleado.getRut());
        double sueldo = oficinaService.encontrarRut(empleado.getRut()).getSueldo_final();
        assertEquals(1033200.0,  sueldo, 0.0);
        oficinaService.eliminarData(oficinaService.encontrarRut(empleado.getRut()));
        empleadoRepository.delete(empleado);
        data.eliminarData(data.obtenerData(empleado.getRut()));
    }

    @Test
    void testReportePlanilla() throws ParseException{
        EmpleadoEntity empleado = new EmpleadoEntity();
        empleado.setRut("128-5");
        empleado.setCategoria("A");
        empleado.setNombres("KRIS");
        empleado.setApellidos("DREEMUR");
        empleado.setFecha_ingreso("2018/10/31");
        empleadoRepository.save(empleado);
        data.insertarData(empleado.getRut(), "2022/06/01");
        oficinaService.reportePlanilla();
        OficinaRRHHEntity empleadoReporte = oficinaRepository.findByRut("128-5");
        assertEquals(0.0, empleadoReporte.getBonificacion_dedicacion(), 0.0);
        empleadoRepository.delete(empleado);
        oficinaRepository.delete(empleadoReporte);
        data.eliminarData(data.obtenerData(empleado.getRut()));
    }

    @Test
    void testObtenerReporte(){
        OficinaRRHHEntity reporteEmpleado = new OficinaRRHHEntity();
        reporteEmpleado.setRut("128-5");
        oficinaRepository.save(reporteEmpleado);
        assertNotNull(oficinaService.obtenerData());
    }

    @Test
    void testComprobarJustificativo(){
        JustificativoEntity justificativo = new JustificativoEntity();
        justificativo.setFecha("2022/10/04");
        justificativo.setRut("231-1");
        justificativoRepository.save(justificativo);
        EmpleadoEntity empleado = new EmpleadoEntity();
        empleado.setRut("231-1");
        empleado.setCategoria("C");
        empleado.setNombres("ALCIDES");
        empleado.setApellidos("QUISPE");
        empleado.setFecha_nacimiento("1990/01/03");
        empleado.setFecha_ingreso("2019/10/31");
        empleadoRepository.save(empleado);
        double descuento = oficinaService.comprobarJustificativo("231-1", "2022/10/04", 0.0);
        assertEquals(0.0, descuento, 0.0);
        empleadoRepository.delete(empleado);
        justificativoRepository.delete(justificativo);
    }

    @Test
    void testComprobarJustificativo2(){
        EmpleadoEntity empleado = new EmpleadoEntity();
        empleado.setRut("231-1");
        empleado.setCategoria("C");
        empleadoRepository.save(empleado);
        double descuento = oficinaService.comprobarJustificativo("231-1", "2022/10/18", 0.0);
        assertEquals(120000.0, descuento, 0.0);
        empleadoRepository.delete(empleado);
    }
}