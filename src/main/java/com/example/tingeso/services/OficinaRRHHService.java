package com.example.tingeso.services;

import com.example.tingeso.entities.EmpleadoEntity;
import com.example.tingeso.entities.OficinaRRHHEntity;
import com.example.tingeso.repositories.OficinaRRHHRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class OficinaRRHHService {

    @Autowired
    private OficinaRRHHRepository oficinaRepository;

    @Autowired
    private EmpleadoService empleadoService;

    @Autowired
    private SubirDataService subirData;

    @Autowired
    private JustificativoService justificativo;

    @Autowired
    private AutorizacionService autorizacion;

    public void reportePlanilla() throws ParseException {
        oficinaRepository.deleteAll();
        List<String>  listaRuts = subirData.obtenerRuts();
        for (String listaRut : listaRuts) {
            calculoPlanilla(listaRut);
        }
    }

    public void calculoPlanilla(String rut) throws ParseException {
        EmpleadoEntity empleadoActual = empleadoService.findByRut(rut);
        OficinaRRHHEntity empleado_reporte = new OficinaRRHHEntity();
        empleado_reporte.setRut(empleadoActual.getRut());
        empleado_reporte.setNombre_empleado(empleadoActual.getApellidos() + " " + empleadoActual.getNombres());
        empleado_reporte.setCategoria(empleadoActual.getCategoria());
        empleado_reporte.setDedicacion(calcularDedicacion(empleadoActual.getFecha_ingreso(), subirData.obtenerFechaRut(empleadoActual.getRut())));
        empleado_reporte.setSueldo_mensual(obtenerSueldo(empleadoActual.getCategoria()));
        empleado_reporte.setBonificacion_dedicacion(calcularBonificacionDedicacion(empleado_reporte.getSueldo_mensual(), empleado_reporte.getDedicacion()));
        empleado_reporte.setHoras_extras(calcularMontoExtra(empleadoActual.getRut(), subirData.obtenerFechaRut(empleadoActual.getRut())));
        empleado_reporte.setDescuentos(comprobarDescuentos(empleadoActual.getRut(), subirData.obtenerFechaRut(empleadoActual.getRut())));
        empleado_reporte.setSueldo_bruto(calcularSueldoBruto(empleado_reporte));
        empleado_reporte.setPrevisional(empleado_reporte.getSueldo_bruto() * 0.1);
        empleado_reporte.setSalud(empleado_reporte.getSueldo_bruto() * 0.08);
        empleado_reporte.setSueldo_final(empleado_reporte.getSueldo_bruto() - empleado_reporte.getPrevisional() - empleado_reporte.getSalud());
        oficinaRepository.save(empleado_reporte);
    }

    public double calcularSueldoBruto(OficinaRRHHEntity empleado){
        double sueldoBruto = (empleado.getSueldo_mensual() + empleado.getBonificacion_dedicacion() + empleado.getHoras_extras() - empleado.getDescuentos());
        return Math.max(sueldoBruto, 0.0);
    }
    public double calcularMontoExtra(String rut, String fecha_inicial) throws ParseException {
        Calendar calendario = subirData.prepararCalendario(fecha_inicial);
        int lastDay = calendario.getActualMaximum(Calendar.DAY_OF_MONTH);
        double montoExtra = 0.0;
        for(int day = 1; day<= lastDay; day++) {
            calendario.set(calendario.get(Calendar.YEAR), calendario.get(Calendar.MONTH), day);
            if (!(subirData.comprobarFinesSemana(calendario))) {
                String fecha_real = subirData.formatDate(calendario);
                if(subirData.obtenerEspecifico2(rut, fecha_real) != null){
                    if(autorizacion.buscarAutorizacion(rut,fecha_real) != null){
                        String hora = subirData.obtenerEspecifico2(rut,fecha_real).getHora();
                        montoExtra = montoExtra + extraCategoria(empleadoService.findByRut(rut).getCategoria(), contarHoras(hora));
                    }
                }
            }
        }
        return montoExtra;
    }

    public double extraCategoria(String categoria, Integer contador){
        switch (categoria) {
            case "A":
                return (25000 * contador);
            case "B":
                return (20000 * contador);
            case "C":
                return (10000 * contador);
            default:
                return 0.0;
        }
    }

    public Integer contarHoras(String hora_string) throws ParseException {
        SimpleDateFormat dt = new SimpleDateFormat("hh:mm");
        Date hora = dt.parse(hora_string);
        int contador = 0;
        if(hora.after(dt.parse("18:00"))){
            contador = (int)((hora.getTime() - dt.parse("18:00").getTime()) / (60 * 60 * 1000));
        }
        return contador;
    }

    public Integer calcularDedicacion(String fecha_inicio, String fecha_temporal) throws ParseException {
        Calendar calendario = subirData.prepararCalendario(fecha_inicio);
        Calendar calendario2 = subirData.prepararCalendario(fecha_temporal);
        calendario2.set(calendario2.get(Calendar.YEAR), calendario2.get(Calendar.MONTH), calendario2.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date date1 = calendario.getTime();
        Date date2 =calendario2.getTime();
        return Math.toIntExact(((date2.getTime() - date1.getTime()) / 86400000 / 365));
    }

    public double calcularBonificacionDedicacion(Integer sueldo_mensual, Integer dedicacion){
        if(sueldo_mensual < 0){
            return 0.0;
        }
        if((dedicacion >= 5) && (dedicacion < 10)){
            return (sueldo_mensual * 0.05);
        } else if((dedicacion >= 10) && (dedicacion < 15)){
            return (sueldo_mensual * 0.08);
        } else if((dedicacion >= 15) && (dedicacion < 20)){
            return (sueldo_mensual * 0.11);
        } else if((dedicacion >= 20) && (dedicacion < 25)){
            return (sueldo_mensual * 0.14);
        } else if(dedicacion >= 25){
            return (sueldo_mensual * 0.17);
        } else{
         return 0.0;
        }
    }

    public double comprobarDescuentos(String rut, String fecha) throws ParseException {
        Calendar calendario = subirData.prepararCalendario(fecha);
        int lastDay = calendario.getActualMaximum(Calendar.DAY_OF_MONTH);
        double descuentos = 0.0;
        for(int day = 1; day<= lastDay; day++) {
            calendario.set(calendario.get(Calendar.YEAR), calendario.get(Calendar.MONTH), day);
            if (!(subirData.comprobarFinesSemana(calendario))){
                String fecha_real = subirData.formatDate(calendario);
                if (subirData.obtenerEspecifico(rut, fecha_real) == null){ // NO hay registro del rut y fecha en DATA TXT
                    descuentos = comprobarJustificativo(rut, fecha_real, descuentos);
                }
                else{
                    String hora_string = subirData.obtenerEspecifico(rut, fecha_real).getHora();
                    descuentos = comprobarHoras(hora_string, rut, descuentos);
                    if(comprobarAtrasado(hora_string)){
                        descuentos = comprobarJustificativo(rut, fecha_real, descuentos);
                    }
                }
            }
        }
        return descuentos;
    }

    public Integer obtenerSueldo(String categoria){
        if(categoria.equals("A")){
            return 1700000;
        } else if (categoria.equals("B")) {
            return 1200000;
        }
        else{
            return 800000;
        }
    }

    public double comprobarHoras(String hora_string,String rut, double descuentos) throws ParseException {
        int sueldo_mensual = obtenerSueldo(empleadoService.obtenerCategoria(rut));
        SimpleDateFormat dt = new SimpleDateFormat("hh:mm");
        Date hora = dt.parse(hora_string);
        if((hora.after(dt.parse("08:10"))) && (hora.before(dt.parse("08:25")))){
            descuentos = descuentos + (sueldo_mensual* 0.01);
        } else if((hora.after(dt.parse("08:25"))) && (hora.before(dt.parse("08:45")))){
            descuentos = descuentos + (sueldo_mensual * 0.03);
        } else if((hora.after(dt.parse("08:45"))) && (hora.before(dt.parse("09:10")))) {
            descuentos = descuentos + (sueldo_mensual * 0.06);
        }
        return descuentos;
    }
    public Boolean comprobarAtrasado(String hora_string) throws ParseException {
        SimpleDateFormat dt = new SimpleDateFormat("hh:mm");
        Date hora = dt.parse(hora_string);
        return hora.after(dt.parse("09:10"));
    }

    public double comprobarJustificativo(String rut, String fecha, double descuentos){
        int sueldo_mensual = obtenerSueldo(empleadoService.obtenerCategoria(rut));
        if (justificativo.buscarJustificativo(rut, fecha) != null) {
            return descuentos;
        } else{
            descuentos = descuentos + (sueldo_mensual * 0.15);
            return descuentos;
        }
    }

    public ArrayList<OficinaRRHHEntity> obtenerData(){
        return (ArrayList<OficinaRRHHEntity>) oficinaRepository.findAll();
    }

    public OficinaRRHHEntity encontrarRut(String rut){
        return oficinaRepository.findByRut(rut);
    }

    public void eliminarData(OficinaRRHHEntity reporte){
        oficinaRepository.delete(reporte);
    }
}
