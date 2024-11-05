package com.example.tingeso;

import com.example.tingeso.entities.SubirDataEntity;
import com.example.tingeso.repositories.SubirDataRepository;
import com.example.tingeso.services.SubirDataService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class SubirDataTest {

    @Autowired
    SubirDataService subirData;

    @Autowired
    SubirDataRepository dataRepository;

    @Test
    void testGuardarDataDB(){
        subirData.guardarDataDB("2022/10/06", "08:00", "567-8");
        SubirDataEntity newData = subirData.obtenerEspecifico("567-8", "2022/10/06");
        assertEquals("567-8", newData.getRut());
        subirData.eliminarData(subirData.obtenerData(newData.getRut()));
    }

    @Test
    void testEncontrarData() throws ParseException {
        subirData.insertarData("567-8", "2022/10/01");
        SubirDataEntity newData = subirData.obtenerEspecifico2("567-8", "2022/10/06");
        assertEquals("18:00", newData.getHora());
        subirData.eliminarData(subirData.obtenerData(newData.getRut()));
    }

    @Test
    void testObtenerData(){
        SubirDataEntity newData = new SubirDataEntity();
        newData.setRut("123-8");
        newData.setFecha("2025/12/13");
        newData.setHora("08:30");
        dataRepository.save(newData);
        assertNotNull(subirData.obtenerData());
        dataRepository.delete(newData);
    }

    @Test
    void testObtenerRuts(){
        SubirDataEntity newData = new SubirDataEntity();
        newData.setRut("158-8");
        newData.setFecha("2020/03/30");
        newData.setHora("18:30");
        dataRepository.save(newData);
        assertNotNull(subirData.obtenerRuts());
        dataRepository.delete(newData);
    }
}
