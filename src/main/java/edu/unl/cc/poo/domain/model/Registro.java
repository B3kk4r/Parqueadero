package edu.unl.cc.poo.domain.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Representa el registro de entrada/salida de un vehículo.
 */

public class Registro {

    private static final List<Registro> registros = new ArrayList<>();

    private final String id;
    private final Vehiculo vehiculo;
    private final EspacioParqueadero espacio;
    private final LocalDateTime fechaHoraEntrada;
    private LocalDateTime fechaHoraSalida;
    private long duracionMinutos;
    private double totalCobrado;
    private final String moneda;

    public Registro(Vehiculo vehiculo, EspacioParqueadero espacio, String moneda) {
        if (vehiculo == null) throw new IllegalArgumentException("El vehículo no puede ser nulo.");
        if (espacio  == null) throw new IllegalArgumentException("El espacio no puede ser nulo.");
        this.id               = UUID.randomUUID().toString();
        this.vehiculo         = vehiculo;
        this.espacio          = espacio;
        this.fechaHoraEntrada = LocalDateTime.now();
        this.fechaHoraSalida  = null;
        this.duracionMinutos  = 0;
        this.totalCobrado     = 0;
        this.moneda           = moneda != null ? moneda : "USD";
    }


    public long calcularDuracion() {
        LocalDateTime fin = (fechaHoraSalida != null) ? fechaHoraSalida : LocalDateTime.now();
        return ChronoUnit.MINUTES.between(fechaHoraEntrada, fin);
    }

    public double calcularTotal(Tarifa tarifa) {
        return tarifa.calcularCosto(calcularDuracion());
    }

    public boolean estaActivo() {
        return fechaHoraSalida == null;
    }

    /**
     * @param tarifa Tarifa aplicable según el tipo de vehículo.
     */

    public void registrarSalida(Tarifa tarifa) {
        if (!estaActivo())
            throw new IllegalStateException("Este registro ya tiene una salida registrada.");
        this.fechaHoraSalida  = LocalDateTime.now();
        this.duracionMinutos  = calcularDuracion();
        this.totalCobrado     = calcularTotal(tarifa);
    }


    public static void agregarRegistro(Registro r) {
        if (r == null) throw new IllegalArgumentException("El registro no puede ser nulo.");
        registros.add(r);
    }

    public static List<Registro> getRegistros() {
        return Collections.unmodifiableList(registros);
    }

    public static List<Registro> buscarPorPlaca(String placa) {
        if (placa == null) return Collections.emptyList();
        String placaNorm = placa.toUpperCase().trim();
        return registros.stream()
                .filter(r -> r.getVehiculo().getPlaca().equals(placaNorm))
                .collect(Collectors.toList());
    }

    public static List<Registro> filtrarPorFecha(LocalDate fecha) {
        return registros.stream()
                .filter(r -> r.getFechaHoraEntrada().toLocalDate().equals(fecha))
                .collect(Collectors.toList());
    }

    public static double getTotalRecaudado() {
        return registros.stream()
                .filter(r -> !r.estaActivo())
                .mapToDouble(Registro::getTotalCobrado)
                .sum();
    }

    public static List<Registro> getRegistrosActivos() {
        return registros.stream()
                .filter(Registro::estaActivo)
                .collect(Collectors.toList());
    }


    public static void limpiarRegistros() {
        registros.clear();
    }


    public String          getId()                { return id; }
    public Vehiculo        getVehiculo()          { return vehiculo; }
    public EspacioParqueadero getEspacio()        { return espacio; }
    public LocalDateTime   getFechaHoraEntrada()  { return fechaHoraEntrada; }
    public LocalDateTime   getFechaHoraSalida()   { return fechaHoraSalida; }
    public long            getDuracionMinutos()   { return duracionMinutos; }
    public double          getTotalCobrado()      { return totalCobrado; }
    public String          getMoneda()            { return moneda; }

    @Override
    public String toString() {
        return String.format(
                "Registro{id='%.8s', placa='%s', espacio=%s, entrada=%s, activo=%b}",
                id, vehiculo.getPlaca(), espacio.getEtiqueta(), fechaHoraEntrada, estaActivo());
    }
}
