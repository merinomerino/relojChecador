package app;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author santo
 */
public class Persona {

//    id_registro , id_trabajador, nombre, fecha, hora, entrada_salida;
    private String id_registro;
    private String id_trabajador;
    private String nombre;
    private String fecha;
    private String hora;
    private String entrada_salida;

    public void setId_registro(String no_control) {
        this.id_registro = no_control;
    }

    public void setId_trabajador(String id_trabajador) {
        this.id_trabajador = id_trabajador;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public void setEntrada_salida(String entrada_salida) {
        this.entrada_salida = entrada_salida;
    }

    public String getId_registro() {
        return id_registro;
    }

    public String getId_trabajador() {
        return id_trabajador;
    }

    public String getNombre() {
        return nombre;
    }

    public String getFecha() {
        return fecha;
    }

    public String getHora() {
        return hora;
    }

    public String getEntrada_salida() {
        return entrada_salida;
    }
}
