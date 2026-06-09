package modelo;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * =============================================================================
 * SUPREMA MANAGER 2026 - NÚCLEO DE PERSISTENCIA Y ESTADO
 * =============================================================================
 * Esta clase actúa como el "Cerebro de Datos". 
 * Centraliza la Liga, el Mercado Global y el progreso del Usuario.
 * Implementa el patrón Singleton para acceso global desde cualquier Panel.
 */
public class EstadoJuego implements Serializable {

    private static EstadoJuego instancia;
    
    // --- COMPONENTES DEL MUNDO ---
    private Liga liga;
    private List<Jugador> mercadoGlobal;
    private Equipo equipoUsuario;
    
    // --- ESTADO DE PROGRESIÓN ---
    private int jornadaActual;
    private final int TOTAL_JORNADAS = 12; // Solicitado: 12 jornadas
    private boolean ligaFinalizada;
    private List<String> noticiasRecientes;
    
    // --- CONFIGURACIÓN DE EQUIPOS ---
    public static final String[] NOMBRES_EQUIPOS = {
        "Real Madrid", "PSG", "Milan", "Arsenal", "Ajax", "Benfica"
    };
    
    public static final String[] LOGOS_EQUIPOS = {
        "madrid.png", "psg.png", "milan.png", "arsenal.png", "ajax.png", "benfica.png"
    };

    /**
     * Constructor privado para inicializar una nueva partida.
     */
    private EstadoJuego() {
        this.noticiasRecientes = new ArrayList<>();
        this.mercadoGlobal = new ArrayList<>();
        this.jornadaActual = 1;
        this.ligaFinalizada = false;
        
        inicializarMundo();
    }

    /**
     * Patrón Singleton: Garantiza que todos los paneles vean los mismos datos.
     */
    public static EstadoJuego getInstancia() {
        if (instancia == null) {
            instancia = new EstadoJuego();
        }
        return instancia;
    }

    // =========================================================================
    // I. INICIALIZACIÓN DEL UNIVERSO (LOS 6 EQUIPOS ÉLITE)
    // =========================================================================

    private void inicializarMundo() {
        List<Equipo> listaEquipos = new ArrayList<>();

        for (int i = 0; i < NOMBRES_EQUIPOS.length; i++) {
            // Presupuesto variado según el equipo
            double presupuesto = 100_000_000 + (new Random().nextInt(200_000_000));
            Equipo eq = new Equipo(NOMBRES_EQUIPOS[i], LOGOS_EQUIPOS[i], presupuesto);
            
            // Generar plantilla inicial de 16 jugadores para cada equipo
            generarPlantillaInicial(eq);
            listaEquipos.add(eq);
        }

        // Crear la Liga con los 6 equipos
        this.liga = new Liga("SUPERLIGA 2026", listaEquipos);
        
        // El usuario por defecto toma el Real Madrid (se puede cambiar en el controlador)
        this.equipoUsuario = listaEquipos.get(0);
        
        // Generar jugadores libres para el Mercado
        generarMercadoTransferencias();
        
        registrarNoticia("¡Bienvenido a Suprema Manager! La temporada 2026 comienza hoy.");
    }

    /**
     * Genera automáticamente 11 titulares y 5 suplentes para un equipo.
     */
    private void generarPlantillaInicial(Equipo eq) {
        String[] nombresEjemplo = {"García", "Müller", "Silva", "Larsen", "Dubois", "Rossi", "Hansen", "Pérez", "Vidic", "Zizou", "Henry", "Kahn", "Baresi", "Xavi", "Iniesta", "Eto'o"};
        
        for (int i = 0; i < 16; i++) {
            String pos;
            if (i == 0) pos = "POR";
            else if (i < 5) pos = "DEF";
            else if (i < 9) pos = "MED";
            else pos = "DEL";

            int mediaBase = 75 + new Random().nextInt(15); // Medias entre 75 y 90
            Jugador j = new Jugador(nombresEjemplo[i] + " (" + eq.getNombre() + ")", mediaBase, pos, eq.getNombre(), mediaBase * 1_200_000);
            eq.getPlantilla().add(j);
        }
        eq.autoGestionarTitulares();
    }

    /**
     * Genera la lista de "Agentes Libres" que aparecerán en el botón MERCADO.
     */
    private void generarMercadoTransferencias() {
        String[] cracks = {"Mbappé", "Haaland", "Vinícius", "Bellingham", "Pedri", "Musiala", "Rodri", "Kane", "Salah", "De Bruyne"};
        Random rnd = new Random();

        for (String nombre : cracks) {
            int media = 88 + rnd.nextInt(10);
            String[] posiciones = {"DEF", "MED", "DEL"};
            Jugador crack = new Jugador(nombre, media, posiciones[rnd.nextInt(3)], "Libre", media * 2_500_000);
            mercadoGlobal.add(crack);
        }
    }

    // =========================================================================
    // II. LÓGICA DE PROGRESIÓN (CONEXIÓN CON EL BOTÓN JUGAR)
    // =========================================================================

    /**
     * Avanza el calendario y verifica si hay campeón.
     */
    public void procesarFinDeJornada() {
        if (jornadaActual < TOTAL_JORNADAS) {
            jornadaActual++;
            registrarNoticia("Iniciando preparativos para la Jornada " + jornadaActual);
            
            // Los jugadores recuperan algo de energía entre jornadas
            for (Equipo e : liga.getEquipos()) {
                e.getPlantilla().forEach(Jugador::recuperarEnergia);
            }
        } else {
            this.ligaFinalizada = true;
            determinarGanador();
        }
    }

    private void determinarGanador() {
        Equipo campeon = liga.getCampeon();
        registrarNoticia("¡LA LIGA HA TERMINADO! El " + campeon.getNombre() + " se corona campeón.");
    }

    // =========================================================================
    // III. GESTIÓN DE NOTICIAS Y EVENTOS
    // =========================================================================

    public void registrarNoticia(String msg) {
        if (noticiasRecientes.size() > 10) noticiasRecientes.remove(noticiasRecientes.size() - 1);
        noticiasRecientes.add(0, "[DÍA " + jornadaActual + "] " + msg);
    }

    public List<String> getNoticias() {
        return noticiasRecientes;
    }

    // =========================================================================
    // IV. PERSISTENCIA (GUARDAR Y CARGAR)
    // =========================================================================

    public void guardarPartida(String archivo) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivo))) {
            oos.writeObject(this);
            System.out.println("Partida guardada con éxito.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void cargarPartida(String archivo) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
            instancia = (EstadoJuego) ois.readObject();
            System.out.println("Partida cargada.");
        } catch (Exception e) {
            System.out.println("No se pudo cargar la partida, iniciando nueva.");
        }
    }

    // =========================================================================
    // V. GETTERS Y SETTERS (CONEXIÓN CON PANELES)
    // =========================================================================

    public Liga getLiga() { return liga; }
    public List<Jugador> getMercadoGlobal() { return mercadoGlobal; }
    public Equipo getEquipoUsuario() { return equipoUsuario; }
    public void setEquipoUsuario(Equipo equipoUsuario) { this.equipoUsuario = equipoUsuario; }
    
    public int getJornadaActual() { return jornadaActual; }
    public boolean isLigaFinalizada() { return ligaFinalizada; }
    
    /**
     * Método para el Panel de Mercado: Realiza la transacción.
     */
    public boolean ejecutarFichaje(Jugador j) {
        if (equipoUsuario.ficharJugador(j)) {
            mercadoGlobal.remove(j);
            registrarNoticia("¡FICHAJE BOMBA! " + j.getNombre() + " se une al " + equipoUsuario.getNombre());
            return true;
        }
        return false;
    }

    /**
     * Método para el Panel de Mercado: Realiza la venta.
     */
    public void ejecutarVenta(Jugador j) {
        equipoUsuario.venderJugador(j);
        j.setEquipoActual("Libre");
        mercadoGlobal.add(j);
        registrarNoticia("Venta confirmada: " + j.getNombre() + " sale del club.");
    }
    
  
    public static void reiniciarInstancia() {
        instancia = new EstadoJuego();
    }
}