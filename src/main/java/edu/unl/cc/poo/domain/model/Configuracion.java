package edu.unl.cc.poo.domain.model;

import edu.unl.cc.poo.domain.enums.TipoVehiculo;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Properties;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

/**
 * Permite establecer las reglas clave del negocio
 */

public class Configuracion {


    private static final String ARCHIVO_CONFIG = "parqueadero.properties";

    private String nombreParqueadero;
    private String moneda;
    private String logoPath;
    private int filasDefecto;
    private int columnasDefecto;
    private final Map<TipoVehiculo, Tarifa> tarifas;

    public Configuracion() {
        this.tarifas          = new EnumMap<>(TipoVehiculo.class);
        this.nombreParqueadero = "Mi Parqueadero";
        this.moneda            = "USD";
        this.logoPath          = "";
        this.filasDefecto      = 5;
        this.columnasDefecto   = 10;
        inicializarTarifasDefecto();
    }

    private void inicializarTarifasDefecto() {
        tarifas.put(TipoVehiculo.AUTOMOVIL,  new Tarifa(TipoVehiculo.AUTOMOVIL,  2.0, 15));
        tarifas.put(TipoVehiculo.MOTO,       new Tarifa(TipoVehiculo.MOTO,       1.0, 15));
        tarifas.put(TipoVehiculo.CAMIONETA,  new Tarifa(TipoVehiculo.CAMIONETA,  3.0, 15));
    }

    public Tarifa getTarifaPorTipo(TipoVehiculo tipo) {
        Tarifa t = tarifas.get(tipo);
        if (t == null) throw new IllegalArgumentException("No hay tarifa configurada para: " + tipo);
        return t;
    }

    public void actualizarTarifa(TipoVehiculo tipo, double precioPorHora, double fraccionMinutos) {
        Tarifa t = tarifas.get(tipo);
        if (t != null) {
            t.setPrecioPorHora(precioPorHora);
            t.setFraccionMinutos(fraccionMinutos);
        } else {
            tarifas.put(tipo, new Tarifa(tipo, precioPorHora, fraccionMinutos));
        }
    }

    public void guardarConfiguracion() {
        Properties props = new Properties();
        props.setProperty("nombre",    nombreParqueadero);
        props.setProperty("moneda",    moneda);
        props.setProperty("logoPath",  logoPath);
        props.setProperty("filas",     String.valueOf(filasDefecto));
        props.setProperty("columnas",  String.valueOf(columnasDefecto));
        for (Map.Entry<TipoVehiculo, Tarifa> e : tarifas.entrySet()) {
            String key = e.getKey().name().toLowerCase();
            props.setProperty("tarifa." + key + ".precio",   String.valueOf(e.getValue().getPrecioPorHora()));
            props.setProperty("tarifa." + key + ".fraccion", String.valueOf(e.getValue().getFraccionMinutos()));
        }
        try (OutputStream out = new FileOutputStream(ARCHIVO_CONFIG)) {
            props.store(out, "Configuración del Parqueadero");
        } catch (IOException ex) {
            System.err.println("Error al guardar configuración: " + ex.getMessage());
        }
    }

    public void cargarConfiguracion() {
        File archivo = new File(ARCHIVO_CONFIG);
        if (!archivo.exists()) return;
        Properties props = new Properties();
        try (InputStream in = new FileInputStream(archivo)) {
            props.load(in);
            nombreParqueadero = props.getProperty("nombre",   nombreParqueadero);
            moneda            = props.getProperty("moneda",   moneda);
            logoPath          = props.getProperty("logoPath", logoPath);
            filasDefecto      = Integer.parseInt(props.getProperty("filas",    String.valueOf(filasDefecto)));
            columnasDefecto   = Integer.parseInt(props.getProperty("columnas", String.valueOf(columnasDefecto)));
            for (TipoVehiculo tipo : TipoVehiculo.values()) {
                String key = tipo.name().toLowerCase();
                String precioStr   = props.getProperty("tarifa." + key + ".precio");
                String fraccionStr = props.getProperty("tarifa." + key + ".fraccion");
                if (precioStr != null && fraccionStr != null) {
                    actualizarTarifa(tipo, Double.parseDouble(precioStr), Double.parseDouble(fraccionStr));
                }
            }
        } catch (IOException | NumberFormatException ex) {
            System.err.println("Error al cargar configuración: " + ex.getMessage());
        }
    }

    public String getNombreParqueadero()       { return nombreParqueadero; }
    public void   setNombreParqueadero(String n){ this.nombreParqueadero = n; }
    public String getMoneda()                  { return moneda; }
    public void   setMoneda(String moneda)     { this.moneda = moneda; }
    public String getLogoPath()                { return logoPath; }
    public void   setLogoPath(String logoPath) { this.logoPath = logoPath; }
    public int    getFilasDefecto()            { return filasDefecto; }
    public void   setFilasDefecto(int f)       { this.filasDefecto = f; }
    public int    getColumnasDefecto()         { return columnasDefecto; }
    public void   setColumnasDefecto(int c)    { this.columnasDefecto = c; }
    public Map<TipoVehiculo, Tarifa> getTarifas() { return Collections.unmodifiableMap(tarifas); }
}
