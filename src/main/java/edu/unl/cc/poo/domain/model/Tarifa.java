package edu.unl.cc.poo.domain.model;

import edu.unl.cc.poo.domain.enums.TipoVehiculo;

/**
 * Define el costo por hora para un tipo de vehículo
 */
public class Tarifa {

    private TipoVehiculo tipoVehiculo;
    private double precioPorHora;
    private double fraccionMinutos;

    public Tarifa(TipoVehiculo tipoVehiculo, double precioPorHora, double fraccionMinutos) {
        if (tipoVehiculo == null) throw new IllegalArgumentException("TipoVehiculo no puede ser nulo.");
        if (precioPorHora < 0)   throw new IllegalArgumentException("El precio no puede ser negativo.");
        if (fraccionMinutos <= 0) throw new IllegalArgumentException("La fracción debe ser mayor a cero.");
        this.tipoVehiculo    = tipoVehiculo;
        this.precioPorHora   = precioPorHora;
        this.fraccionMinutos = fraccionMinutos;
    }

    /**
     * Calcula el costo según los minutos de permanencia.
     * @param minutos, Duración real en minutos.
     * @return Costo total a cobrar.
     */
    public double calcularCosto(long minutos) {
        if (minutos <= 0) return 0;
        double fracciones = Math.ceil(minutos / fraccionMinutos);
        double horasFraccionadas = (fracciones * fraccionMinutos) / 60.0;
        return horasFraccionadas * precioPorHora;
    }


    public TipoVehiculo getTipoVehiculo()  { return tipoVehiculo; }
    public double getPrecioPorHora()       { return precioPorHora; }
    public double getFraccionMinutos()     { return fraccionMinutos; }

    public void setPrecioPorHora(double precioPorHora) {
        if (precioPorHora < 0) throw new IllegalArgumentException("El precio no puede ser negativo.");
        this.precioPorHora = precioPorHora;
    }

    public void setFraccionMinutos(double fraccionMinutos) {
        if (fraccionMinutos <= 0) throw new IllegalArgumentException("La fracción debe ser mayor a cero.");
        this.fraccionMinutos = fraccionMinutos;
    }

    @Override
    public String toString() {
        return String.format("Tarifa{tipo=%s, precio=%.2f/h, fraccion=%.0f min}",
                tipoVehiculo, precioPorHora, fraccionMinutos);
    }
}
