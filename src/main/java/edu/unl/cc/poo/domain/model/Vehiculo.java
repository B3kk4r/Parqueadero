package com.parqueadero.model;

import com.parqueadero.enums.TipoVehiculo;

/**
 * Representa un vehículo que ingresa al parqueadero.
 */

public class Vehiculo {

    private String placa;
    private TipoVehiculo tipo;
    private String nombreConductor;

    public Vehiculo(String placa, TipoVehiculo tipo, String nombreConductor) {
        setPlaca(placa);
        if (tipo == null) throw new IllegalArgumentException("El tipo de vehículo no puede ser nulo.");
        this.tipo = tipo;
        this.nombreConductor = nombreConductor;
    }


    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        if (placa == null || placa.isBlank())
            throw new IllegalArgumentException("La placa no puede estar vacía.");
        this.placa = placa.toUpperCase().trim();
    }

    public TipoVehiculo getTipo() {
        return tipo;
    }

    public void setTipo(TipoVehiculo tipo) {
        if (tipo == null) throw new IllegalArgumentException("El tipo de vehículo no puede ser nulo.");
        this.tipo = tipo;
    }

    public String getNombreConductor() {
        return nombreConductor;
    }

    public void setNombreConductor(String nombreConductor) {
        this.nombreConductor = nombreConductor;
    }

    @Override
    public String toString() {
        return String.format("Vehiculo{placa='%s', tipo=%s, conductor='%s'}",
                placa, tipo, nombreConductor);
    }
}
