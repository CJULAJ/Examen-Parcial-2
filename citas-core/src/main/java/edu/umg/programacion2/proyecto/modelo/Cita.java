package edu.umg.programacion2.proyecto.modelo;

import java.time.LocalDateTime;

public class Cita {

    private int id;
    private String cliente;
    private LocalDateTime fechaHora;
    private String servicio;
    private int duracionMinutos;
    private String estado;

    // Para crear una cita nueva
    public Cita(String cliente, LocalDateTime fechaHora,
                String servicio, int duracionMinutos, String estado) {
        this.cliente = cliente;
        this.fechaHora = fechaHora;
        this.servicio = servicio;
        this.duracionMinutos = duracionMinutos;
        this.estado = estado;
    }

    // Para representar una cita que ya existe en la base de datos
    public Cita(int id, String cliente, LocalDateTime fechaHora,
                String servicio, int duracionMinutos, String estado) {
        this.id = id;
        this.cliente = cliente;
        this.fechaHora = fechaHora;
        this.servicio = servicio;
        this.duracionMinutos = duracionMinutos;
        this.estado = estado;
    }

    public int getId() {
        return id;
    }

    public String getCliente() {
        return cliente;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public String getServicio() {
        return servicio;
    }

    public int getDuracionMinutos() {
        return duracionMinutos;
    }

    public String getEstado() {
        return estado;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public void setServicio(String servicio) {
        this.servicio = servicio;
    }

    public void setDuracionMinutos(int duracionMinutos) {
        this.duracionMinutos = duracionMinutos;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}