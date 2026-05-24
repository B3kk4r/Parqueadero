package edu.unl.cc.poo.domain;

public class EspacioParqueadero {

    private int numero;
    private int fila;
    private int columna;
    private EstadoEspacio estado;
    private String idRegistroActivo;

    public EspacioParqueadero(int numero, int fila, int columna) {
        this.numero = numero;
        this.fila = fila;
        this.columna = columna;
    }

    public String getEtiqueta() {
        return String.valueOf((char)('A'+fila))+(columna+1);
    }

    private boolean estaLibre (){
        return estado == EstadoEspacio.LIBRE;

    }

    public void ocupar(String idRegistro){

    }

    public void liberar (){

    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public int getFila() {
        return fila;
    }

    public void setFila(int fila) {
        this.fila = fila;
    }

    public int getColumna() {
        return columna;
    }

    public void setColumna(int columna) {
        this.columna = columna;
    }

    public EstadoEspacio getEstado() {
        return estado;
    }

    public void setEstado(EstadoEspacio estado) {
        this.estado = estado;
    }

    public String getIdRegistroActivo() {
        return idRegistroActivo;
    }

    public void setIdRegistroActivo(String idRegistroActivo) {
        this.idRegistroActivo = idRegistroActivo;
    }

    @Override
    public String toString() {
        return "Espacio{" + getEtiqueta() + ", estado =" + estado + "}";
    }

}
