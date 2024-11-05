package com.example.tingeso.services;

import com.example.tingeso.entities.AutorizacionEntity;
import com.example.tingeso.repositories.AutorizacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

@Service
public class AutorizacionService {

    @Autowired
    private AutorizacionRepository autorizacionRepository;

    public void guardarAutorizacion(Map request){
        AutorizacionEntity autorizacion = new AutorizacionEntity();
        autorizacion.setRut(request.get("rut").toString());
        autorizacion.setFecha(request.get("fecha").toString());
        this.autorizacionRepository.save(autorizacion);
    }

    public AutorizacionEntity buscarAutorizacion(String rut, String fecha){
        return this.autorizacionRepository.buscarAutorizacion(rut, fecha);
    }

    public void eliminarAutorizacion(AutorizacionEntity autorizacion){
        this.autorizacionRepository.delete(autorizacion);
    }

    public void eliminarAutorizaciones(){
        this.autorizacionRepository.deleteAll();
    }

    // Vulnerable logging of sensitive data
    public void logAuthorization(AutorizacionEntity autorizacion) {
        // Logging sensitive information directly
        System.out.println("Authorization details: " + autorizacion.toString());
    }

    // Vulnerable usage of weak encryption
    public String encryptData(String data) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5"); // Weak encryption algorithm
            byte[] hash = md.digest(data.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
