package com.example.tingeso.services;

import com.example.tingeso.entities.SubirDataEntity;
import com.example.tingeso.repositories.SubirDataRepository;
import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class SubirDataService {

    @Autowired
    private SubirDataRepository dataRepository;

    @Autowired
    private AutorizacionService autorizaciones;

    @Autowired
    private JustificativoService justificativos;

    private final Logger logg = LoggerFactory.getLogger(SubirDataService.class);

    public ArrayList<SubirDataEntity> obtenerData(){
        return (ArrayList<SubirDataEntity>) dataRepository.findAll();
    }

    @Generated
    public String guardar(MultipartFile file){
        String filename = file.getOriginalFilename();
        if(filename != null){
            if((!file.isEmpty()) && (filename.toUpperCase().equals("DATA.TXT"))){
                try{
                    byte [] bytes = file.getBytes();
                    Path path  = Paths.get(file.getOriginalFilename());
                    Files.write(path, bytes);
                    logg.info("Archivo guardado");
                }
                catch (IOException e){
                    logg.error("ERROR", e);
                }
            }
            return "Archivo guardado con exito!";
        }
        else{
            return "No se pudo guardar el archivo";
        }
    }

    @Generated
    public void leerTxt(String direccion){
        String texto = "";
        BufferedReader bf = null;
        dataRepository.deleteAll();
        justificativos.eliminarJustificativos();
        autorizaciones.eliminarAutorizaciones();
        try{
            bf = new BufferedReader(new FileReader(direccion));
            String temp = "";
            String bfRead;
            while((bfRead = bf.readLine()) != null){
                guardarDataDB(bfRead.split(";")[0], bfRead.split(";")[1], bfRead.split(";")[2]);
                temp = temp + "\n" + bfRead;
            }
            texto = temp;
            System.out.println("Archivo leido exitosamente");
        }catch(Exception e){
            System.err.println("No se encontro el archivo");
        }finally{
            if(bf != null){
                try{
                    bf.close();
                }catch(IOException e){
                    logg.error("ERROR", e);
                }
            }
        }
    }

    public void guardarData(SubirDataEntity data){
        dataRepository.save(data);
    }

    public void guardarDataDB(String fecha, String hora, String rut){
         SubirDataEntity newData = new SubirDataEntity();
         newData.setFecha(fecha);
         newData.setRut(rut);
         newData.setHora(hora);
         guardarData(newData);
    }

    public SubirDataEntity obtenerEspecifico(String rut, String fecha){
        return dataRepository.buscarData(rut, fecha);
    }

    public SubirDataEntity obtenerEspecifico2(String rut, String fecha){
        return dataRepository.buscarData2(rut, fecha);
    }

    public List<String> obtenerRuts(){
        return dataRepository.findDistinctRut();
    }

    public String obtenerFechaRut(String rut){
        return dataRepository.buscarFechaRut(rut);
    }

    public void insertarData(String rut, String fechaInicial) throws ParseException {
        boolean primer_ciclo = true;
        Calendar calendario = prepararCalendario(fechaInicial);
        int lastDay = calendario.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int day = 1; day <= lastDay; day++) {
            calendario.set(calendario.get(Calendar.YEAR), calendario.get(Calendar.MONTH), day);
            if(!(comprobarFinesSemana(calendario))){
                String fecha_real = formatDate(calendario);
                SubirDataEntity data1 = new SubirDataEntity();
                SubirDataEntity data2 = new SubirDataEntity();
                data1.setRut(rut);
                data2.setRut(rut);
                data1.setFecha(fecha_real);
                data2.setFecha(fecha_real);
                if(primer_ciclo){
                    data1.setHora("08:30");
                    guardarData(data1);
                    data2.setHora("20:00");
                    guardarData(data2);
                    primer_ciclo = false;
                }
                else{
                    data1.setHora("08:00");
                    guardarData(data1);
                    data2.setHora("18:00");
                    guardarData(data2);
                }
            }
        }
    }

    public Calendar prepararCalendario(String fecha) throws ParseException {
        Calendar calendario = Calendar.getInstance();
        DateFormat date1=new SimpleDateFormat("yyyy/MM/dd");
        Date real_fecha = date1.parse(fecha);
        calendario.setTime(real_fecha);
        return calendario;
    }

    public Boolean comprobarFinesSemana(Calendar calendario){
        int dia = calendario.get(Calendar.DAY_OF_WEEK);
        return dia == Calendar.SATURDAY || dia == Calendar.SUNDAY;
    }

    public String formatDate(Calendar calendario){
        DateFormat date1=new SimpleDateFormat("yyyy/MM/dd");
        return date1.format(calendario.getTime());
    }

    public void eliminarData(ArrayList<SubirDataEntity> datas){
        dataRepository.deleteAll(datas);
    }

    public ArrayList<SubirDataEntity> obtenerData(String rut){
        return dataRepository.eliminarData(rut);
    }
}
