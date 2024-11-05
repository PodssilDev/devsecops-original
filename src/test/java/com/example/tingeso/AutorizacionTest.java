package com.example.tingeso;

import com.example.tingeso.entities.AutorizacionEntity;
import com.example.tingeso.services.AutorizacionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class AutorizacionTest{

    @Autowired
    AutorizacionService autorizacion;

    @Test
    void testInsertarAutorizacion() {
        Map<String, String> newmap = new HashMap<>();
        newmap.put("rut", "20-8");
        newmap.put("fecha", "2022/08/02");
        autorizacion.guardarAutorizacion(newmap);
        AutorizacionEntity newAutorizacion = autorizacion.buscarAutorizacion("20-8", "2022/08/02");
        assertEquals("20-8", newAutorizacion.getRut());
        autorizacion.eliminarAutorizacion(newAutorizacion);
    }

    @Test
    void testInsertarAutorizacion2(){
        Map<String, String> newmap = new HashMap<>();
        newmap.put("rut", "27-8");
        newmap.put("fecha", "2020/10/13");
        autorizacion.guardarAutorizacion(newmap);
        AutorizacionEntity newAutorizacion = autorizacion.buscarAutorizacion("27-8", "2020/10/13");
        assertEquals("2020/10/13", newAutorizacion.getFecha());
        autorizacion.eliminarAutorizaciones();
    }
}
