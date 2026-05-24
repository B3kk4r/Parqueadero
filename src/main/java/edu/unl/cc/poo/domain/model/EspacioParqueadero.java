package edu.unl.cc.poo.domain.model;

import edu.unl.cc.poo.domain.enums.EstadoEspacio;

/**
 * Representa un espacio físico dentro del parqueadero.
 */

public class EspacioParqueadero {

    private final int numero;
    private final int fila;
    private final int columna;
    private EstadoEspacio estado;
    private String idRegistroActivo;

    public EspacioParqueadero(int numero, int fila, int columna) {
        this.numero = numero;
        this.fila = fila;
        this.columna = columna;
        this.estado = EstadoEspacio.LIBRE;
        this.idRegistroActivo = null;
    }


    /**
     * Devuelve una etiqueta legible del espacio, ej. "A-01".
     */
    public String getEtiqueta() {
        char letraFila = (char) ('A' + fila);
        return String.format("%c-%02d", letraFila, columna + 1);
    }

    public boolean estaLibre() {
        return estado == EstadoEspacio.LIBRE;
    }


    /**
     * Marca el espacio como OCUPADO y asocia el ID del registro activo.
     */
    public void ocupar(String idRegistro) {
        if (!estaLibre())
            throw new IllegalStateException("El espacio " + getEtiqueta() + " no está libre.");
        this.estado = EstadoEspacio.OCUPADO;
        this.idRegistroActivo = idRegistro;
    }

    /**
     * Libera el espacio y elimina el registro.
     */
    public void liberar() {
        if (estado == EstadoEspacio.INHABILITADO)
            throw new IllegalStateException("No se puede liberar un espacio inhabilitado.");
        this.estado = EstadoEspacio.LIBRE;
        this.idRegistroActivo = null;
    }

    public void setEstado(EstadoEspacio estado) {
        if (estado == null) throw new IllegalArgumentException("Estado no puede ser nulo.");
        this.estado = estado;
        if (estado == EstadoEspacio.LIBRE) this.idRegistroActivo = null;
    }


    public int getNumero()          { return numero; }
    public int getFila()            { return fila; }
    public int getColumna()         { return columna; }
    public EstadoEspacio getEstado(){ return estado; }
    public String getIdRegistroActivo() { return idRegistroActivo; }

    @Override
    public String toString() {
        return String.format("Espacio{%s, estado=%s}", getEtiqueta(), estado);
    }
}
