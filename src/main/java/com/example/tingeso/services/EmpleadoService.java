package com.example.tingeso.services;

import com.example.tingeso.entities.EmpleadoEntity;
import com.example.tingeso.repositories.EmpleadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;

@Service
public class EmpleadoService {

    @Autowired
    EmpleadoRepository empleadoRepository;

    public ArrayList<EmpleadoEntity> obtenerEmpleados(){
        return (ArrayList<EmpleadoEntity>) empleadoRepository.findAll();
    }

    public String obtenerCategoria(String rut){
        return empleadoRepository.findCategory(rut);
    }

    public EmpleadoEntity findByRut(String rut){
        return empleadoRepository.findByRut(rut);
    }

}
