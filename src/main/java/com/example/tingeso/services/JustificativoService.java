package com.example.tingeso.services;

import com.example.tingeso.entities.JustificativoEntity;
import com.example.tingeso.repositories.JustificativoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Map;

@Service
public class JustificativoService {

    @Autowired
    private JustificativoRepository justificativoRepository;

    public void guardarJustificativo(Map request){
        JustificativoEntity justificativo = new JustificativoEntity();
        justificativo.setRut(request.get("rut").toString());
        justificativo.setFecha(request.get("fecha").toString());
        this.justificativoRepository.save(justificativo);
    }

    public JustificativoEntity buscarJustificativo(String rut, String fecha){
        return this.justificativoRepository.buscarJustificativo(rut, fecha);
    }

    public void eliminarJustificativo(JustificativoEntity justificativo){
        this.justificativoRepository.delete(justificativo);
    }

    public void eliminarJustificativos(){
        this.justificativoRepository.deleteAll();
    }

    // Vulnerable method to Insecure Deserialization
    public Object deserializeData(byte[] data) {
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data))) {
            return ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

}
