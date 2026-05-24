package com.parqueadero.model;

import com.parqueadero.enums.EstadoEspacio;


public class Parqueadero {

    private final String nombre;
    private final int capacidad;
    private final MapaParqueadero mapa;
    private final Configuracion configuracion;

    public Parqueadero(String nombre, int filas, int columnas) {
        if (nombre == null || nombre.isBlank())
            throw new IllegalArgumentException("El nombre no puede estar vacío.");
        this.configuracion = new Configuracion();
        this.configuracion.setNombreParqueadero(nombre);
        this.nombre     = nombre;
        this.mapa       = new MapaParqueadero(filas, columnas);
        this.capacidad  = filas * columnas;
    }

    /** Constructor que carga la configuración desde archivo. */
    public Parqueadero() {
        this.configuracion = new Configuracion();
        this.configuracion.cargarConfiguracion();
        this.nombre    = configuracion.getNombreParqueadero();
        this.mapa      = new MapaParqueadero(configuracion.getFilasDefecto(),
                                              configuracion.getColumnasDefecto());
        this.capacidad = configuracion.getFilasDefecto() * configuracion.getColumnasDefecto();
    }


    /**
     * Registra la entrada de un vehículo y ocupa el primer espacio libre.
     *
     * @param vehiculo Vehículo que ingresa.
     * @return El registro creado.
     * @throws IllegalStateException si no hay espacios disponibles.
     */

    public Registro registrarEntrada(Vehiculo vehiculo) {
        EspacioParqueadero espacio = mapa.getPrimerEspacioLibre();
        if (espacio == null)
            throw new IllegalStateException("No hay espacios disponibles en el parqueadero.");

        Registro registro = new Registro(vehiculo, espacio, configuracion.getMoneda());
        espacio.ocupar(registro.getId());
        Registro.agregarRegistro(registro);

        System.out.printf("[ENTRADA] Placa: %-10s | Espacio: %s | %s%n",
                vehiculo.getPlaca(), espacio.getEtiqueta(), registro.getFechaHoraEntrada());
        return registro;

    }

    /**
     * Registra la entrada en un espacio específico (por etiqueta, ej. "A-01").
     */

    public Registro registrarEntradaEnEspacio(Vehiculo vehiculo, int fila, int columna) {
        EspacioParqueadero espacio = mapa.getEspacio(fila, columna);
        if (!espacio.estaLibre())
            throw new IllegalStateException("El espacio seleccionado no está libre: " + espacio.getEtiqueta());

        Registro registro = new Registro(vehiculo, espacio, configuracion.getMoneda());
        espacio.ocupar(registro.getId());
        Registro.agregarRegistro(registro);

        System.out.printf("[ENTRADA] Placa: %-10s | Espacio: %s | %s%n",
                vehiculo.getPlaca(), espacio.getEtiqueta(), registro.getFechaHoraEntrada());
        return registro;
    }

    /**
     * Registra la salida de un vehículo, calcula el cobro y libera el espacio.
     *
     * @param registroId ID del registro de entrada.
     * @return El registro actualizado con datos de salida.
     */

    public Registro registrarSalida(String registroId) {
        Registro registro = buscarRegistroActivo(registroId);
        Tarifa tarifa = configuracion.getTarifaPorTipo(registro.getVehiculo().getTipo());

        registro.registrarSalida(tarifa);
        registro.getEspacio().liberar();

        System.out.printf("[SALIDA]  Placa: %-10s | Espacio: %s | Duración: %d min | Total: %s %.2f%n",
                registro.getVehiculo().getPlaca(),
                registro.getEspacio().getEtiqueta(),
                registro.getDuracionMinutos(),
                registro.getMoneda(),
                registro.getTotalCobrado());
        return registro;
    }

    /**
     * Inhabilita o habilita un espacio del mapa.
     */

    public void setEstadoEspacio(int fila, int columna, EstadoEspacio estado) {
        EspacioParqueadero espacio = mapa.getEspacio(fila, columna);
        espacio.setEstado(estado);
    }

    public int getEspaciosDisponibles() {
        return (int) mapa.contarLibres();
    }


    private Registro buscarRegistroActivo(String id) {
        return Registro.getRegistros().stream()
                .filter(r -> r.getId().equals(id) && r.estaActivo())
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontró registro activo con id: " + id));
    }


    public void imprimirResumen() {
        System.out.println("=".repeat(50));
        System.out.println("  RESUMEN: " + nombre);
        System.out.printf("  Capacidad total : %d%n", capacidad);
        System.out.printf("  Espacios libres : %d%n", mapa.contarLibres());
        System.out.printf("  Espacios ocupados: %d%n", mapa.contarOcupados());
        System.out.printf("  Inhabilitados   : %d%n", mapa.contarInhabilitados());
        System.out.printf("  Registros activos: %d%n", Registro.getRegistrosActivos().size());
        System.out.printf("  Total recaudado : %s %.2f%n",
                configuracion.getMoneda(), Registro.getTotalRecaudado());
        System.out.println("=".repeat(50));
    }


    public String          getNombre()        { return nombre; }
    public int             getCapacidad()     { return capacidad; }
    public MapaParqueadero getMapa()          { return mapa; }
    public Configuracion   getConfiguracion() { return configuracion; }
}
