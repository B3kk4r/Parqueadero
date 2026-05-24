package edu.unl.cc.poo.domain;

import java.util.Map;

public class Configuracion {

    private String nombreParqueadero;
    private Map tarifa;


    public Configuracion(String nombreParqueadero, Map tarifa) {
        this.nombreParqueadero = nombreParqueadero;
        this.tarifa = tarifa;
    }

    public void guardarConfiguracion() {

    }

    public void cargarConfiguracion() {

    }

    public Map getTarifa() {
        return tarifa;
    }

    public void setTarifa(Map tarifa) {
        this.tarifa = tarifa;
    }

    public String getNombreParqueadero() {
        return nombreParqueadero;
    }

    public void setNombreParqueadero(String nombreParqueadero) {
        this.nombreParqueadero = nombreParqueadero;
    }

}
