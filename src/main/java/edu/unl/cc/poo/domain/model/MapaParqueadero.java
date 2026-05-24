package edu.unl.cc.poo.domain.model;

import edu.unl.cc.poo.domain.enums.EstadoEspacio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Gestiona la cuadrícula de espacios del parqueadero.
 */

public class MapaParqueadero {

    private int filas;
    private int columnas;
    private final List<EspacioParqueadero> espacios;

    public MapaParqueadero(int filas, int columnas) {
        if (filas <= 0 || columnas <= 0)
            throw new IllegalArgumentException("Filas y columnas deben ser mayores a cero.");
        this.filas    = filas;
        this.columnas = columnas;
        this.espacios = new ArrayList<>();
        inicializarEspacios();
    }


    public void inicializarEspacios() {
        espacios.clear();
        int numero = 1;
        for (int f = 0; f < filas; f++) {
            for (int c = 0; c < columnas; c++) {
                espacios.add(new EspacioParqueadero(numero++, f, c));
            }
        }
    }


    /**
     * Retorna el espacio en la posición.
     */
    public EspacioParqueadero getEspacio(int fila, int columna) {
        validarPosicion(fila, columna);
        return espacios.get(fila * columnas + columna);
    }

    /**
     * Retorna el primer espacio libre disponible, o null si no hay ninguno.
     */
    public EspacioParqueadero getPrimerEspacioLibre() {
        return espacios.stream()
                .filter(EspacioParqueadero::estaLibre)
                .findFirst()
                .orElse(null);
    }

    /**
     * Redimensiona el mapa.
     */
    public void redimensionar(int nuevasFilas, int nuevasColumnas) {
        if (nuevasFilas <= 0 || nuevasColumnas <= 0)
            throw new IllegalArgumentException("Las nuevas dimensiones deben ser mayores a cero.");
        this.filas    = nuevasFilas;
        this.columnas = nuevasColumnas;
        inicializarEspacios();
    }

    public long contarLibres() {
        return espacios.stream().filter(e -> e.getEstado() == EstadoEspacio.LIBRE).count();
    }

    public long contarOcupados() {
        return espacios.stream().filter(e -> e.getEstado() == EstadoEspacio.OCUPADO).count();
    }

    public long contarInhabilitados() {
        return espacios.stream().filter(e -> e.getEstado() == EstadoEspacio.INHABILITADO).count();
    }


    public int getFilas()    { return filas; }
    public int getColumnas() { return columnas; }

    public List<EspacioParqueadero> getEspacios() {
        return Collections.unmodifiableList(espacios);
    }


    private void validarPosicion(int fila, int columna) {
        if (fila < 0 || fila >= filas || columna < 0 || columna >= columnas) {
            throw new IndexOutOfBoundsException(
                    String.format("Posición (%d,%d) fuera del rango %dx%d.", fila, columna, filas, columnas));
        }
    }

    @Override
    public String toString() {
        return String.format("MapaParqueadero{%dx%d, libres=%d, ocupados=%d}",
                filas, columnas, contarLibres(), contarOcupados());
    }
}
