package edu.unl.cc.poo.domain.enums;

public enum EstadoEspacio {
    LIBRE("Libre"),
    OCUPADO("Ocupado"),
    INHABILITADO("Inhabilitado");

    private final String descripcion;

    EstadoEspacio(String descripcion) {
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
