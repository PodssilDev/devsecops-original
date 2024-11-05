package com.example.tingeso.controllers;

import com.example.tingeso.services.AutorizacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
@RequestMapping
public class AutorizacionController {

    @Autowired
    private AutorizacionService autorizacionService;

    @GetMapping("/subirAutorizacion")
    public String nuevaAutorizacion(){
        return "subirAutorizacion";
    }

    @PostMapping("/guardarAutorizacion")
    public String guardarAutorizacion(@RequestParam Map<String,String> allParams){
        autorizacionService.guardarAutorizacion(allParams);
        return "redirect:/subirAutorizacion";
    }
}
