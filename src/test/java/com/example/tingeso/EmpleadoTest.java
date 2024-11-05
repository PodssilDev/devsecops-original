package com.example.tingeso;

import com.example.tingeso.entities.EmpleadoEntity;
import com.example.tingeso.repositories.EmpleadoRepository;
import com.example.tingeso.services.EmpleadoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EmpleadoTest{

    @Autowired
    EmpleadoService empleado;

    @Autowired
    EmpleadoRepository empleadoRepository;

    @Test
    void testObtenerEmpleados(){
        EmpleadoEntity newEmpleado = new EmpleadoEntity();
        newEmpleado.setRut("99-8");
        newEmpleado.setApellidos("HINAWARI NARA");
        newEmpleado.setNombres("TENSHI SUMIRE");
        empleadoRepository.save(newEmpleado);
        assertNotNull(empleado.obtenerEmpleados());
        empleadoRepository.delete(newEmpleado);
    }

    @Test
    void testObtenerCategoria(){
        EmpleadoEntity newEmpleado = new EmpleadoEntity();
        newEmpleado.setRut("99-1");
        newEmpleado.setCategoria("A");
        empleadoRepository.save(newEmpleado);
        assertEquals("A", empleado.findByRut(newEmpleado.getRut()).getCategoria());
        empleadoRepository.delete(newEmpleado);
    }
}
