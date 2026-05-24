package com.parqueadero.enums;

public enum TipoVehiculo {
    AUTOMOVIL("Automóvil"),
    MOTO("Moto"),
    CAMIONETA("Camioneta");

    private final String descripcion;

    TipoVehiculo(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public String toString() {
        return descripcion;
    }
}
