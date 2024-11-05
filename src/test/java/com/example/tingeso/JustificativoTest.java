package com.example.tingeso;

import com.example.tingeso.entities.JustificativoEntity;
import com.example.tingeso.services.JustificativoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class JustificativoTest {

    @Autowired
    JustificativoService justificativo;

    @Test
    void testInsertarJustificativo() {
        Map<String, String> newMap = new HashMap<>();
        newMap.put("rut", "53-8");
        newMap.put("fecha", "2022/05/18");
        justificativo.guardarJustificativo(newMap);
        JustificativoEntity newJustificativo = justificativo.buscarJustificativo("53-8", "2022/05/18");
        assertEquals("53-8", newJustificativo.getRut());
        justificativo.eliminarJustificativo(newJustificativo);
    }

    @Test
    void testInsertarJustificativo2(){
        Map<String, String> newmap = new HashMap<>();
        newmap.put("rut", "753-1");
        newmap.put("fecha", "2020/10/17");
        justificativo.guardarJustificativo(newmap);
        JustificativoEntity newJustificativo = justificativo.buscarJustificativo("753-1", "2020/10/17");
        assertEquals("2020/10/17", newJustificativo.getFecha());
        justificativo.eliminarJustificativos();
    }
}