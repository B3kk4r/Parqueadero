package edu.unl.cc.poo.domain.model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Genera el comprobante de pago de un registro de parqueadero.
 */

public class Ticket {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    private final String nombreParqueadero;
    private final LocalDateTime fechaGeneracion;
    private String rutaArchivoPDF;

    private final Registro registro;

    public Ticket(String nombreParqueadero, Registro registro) {
        if (registro == null) throw new IllegalArgumentException("El registro no puede ser nulo.");
        this.nombreParqueadero = nombreParqueadero;
        this.fechaGeneracion   = LocalDateTime.now();
        this.registro          = registro;
        this.rutaArchivoPDF    = "";
    }


    public String generarContenido() {
        StringBuilder sb = new StringBuilder();
        String sep = "=".repeat(40);

        sb.append(sep).append("\n");
        sb.append(centrar(nombreParqueadero, 40)).append("\n");
        sb.append(centrar("COMPROBANTE DE PAGO", 40)).append("\n");
        sb.append(sep).append("\n");
        sb.append(String.format("Fecha emisión : %s%n", fechaGeneracion.format(FMT)));
        sb.append(String.format("Placa         : %s%n", registro.getVehiculo().getPlaca()));
        sb.append(String.format("Tipo vehículo : %s%n", registro.getVehiculo().getTipo()));
        sb.append(String.format("Conductor     : %s%n", registro.getVehiculo().getNombreConductor()));
        sb.append(String.format("Espacio       : %s%n", registro.getEspacio().getEtiqueta()));
        sb.append("-".repeat(40)).append("\n");
        sb.append(String.format("Entrada       : %s%n", registro.getFechaHoraEntrada().format(FMT)));
        if (registro.getFechaHoraSalida() != null) {
            sb.append(String.format("Salida        : %s%n", registro.getFechaHoraSalida().format(FMT)));
        }
        sb.append(String.format("Duración      : %d min%n", registro.getDuracionMinutos()));
        sb.append("-".repeat(40)).append("\n");
        sb.append(String.format("TOTAL         : %s %.2f%n",
                registro.getMoneda(), registro.getTotalCobrado()));
        sb.append(sep).append("\n");
        sb.append(centrar("¡Gracias por su visita!", 40)).append("\n");
        sb.append(sep).append("\n");

        return sb.toString();
    }

    /**
     * Genera el ticket como archivo .txt
     */

    public File generarPDF() {
        String nombre = "ticket_" + registro.getVehiculo().getPlaca() + "_"
                + System.currentTimeMillis() + ".txt";
        File archivo = new File(nombre);
        try (PrintWriter pw = new PrintWriter(new FileWriter(archivo))) {
            pw.print(generarContenido());
            this.rutaArchivoPDF = archivo.getAbsolutePath();
        } catch (IOException e) {
            System.err.println("Error al generar ticket: " + e.getMessage());
            return null;
        }
        return archivo;
    }


    public void abrirPDF() {
        if (rutaArchivoPDF == null || rutaArchivoPDF.isBlank()) throw new IllegalArgumentException("No hay ticket generado. Llame primero a generarPDF().");

        try {
            java.awt.Desktop.getDesktop().open(new File(rutaArchivoPDF));
        } catch (IOException | UnsupportedOperationException e) {
            System.err.println("No se pudo abrir el ticket: " + e.getMessage());
        }
    }

    private String centrar(String texto, int ancho) {
        if (texto.length() >= ancho) return texto;
        int padding = (ancho - texto.length()) / 2;
        return " ".repeat(padding) + texto;
    }


    public String        getNombreParqueadero() { return nombreParqueadero; }
    public LocalDateTime getFechaGeneracion()   { return fechaGeneracion; }
    public String        getRutaArchivoPDF()    { return rutaArchivoPDF; }
}
