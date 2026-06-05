package modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * =============================================================================
 * SUPREMA MANAGER 2026 - GESTOR CORE DE LA COMPETICIÓN DE LIGA
 * =============================================================================
 * Arquitectura de control deportivo encargada de la persistencia, planificación
 * de calendarios mediante algoritmo Round Robin circular y protección de estado.
 */
public class Liga implements Serializable {

    private static final long serialVersionUID = 20260601L;

    // --- ATRIBUTOS DE ESTADO GENERAL ---
    private String nombre;
    private List<Equipo> equipos;
    private List<Jornada> calendario;
    private int jornadaActual;
    private final int MAX_JORNADAS = 10; // 6 Equipos = 5 jornadas de ida + 5 de vuelta
    
    // --- HISTORIAL DE AUDITORÍA DEPORTIVA ---
    private List<String> registroGlobalGoles;
    private boolean ligaIniciada;

    /**
     * Construye e inicializa el ecosistema completo de la liga de élite.
     */
    public Liga(String nombre, List<Equipo> listaEquipos) {
        if (listaEquipos == null || listaEquipos.size() != 6) {
            throw new IllegalArgumentException("La competición requiere exactamente 6 equipos élite.");
        }
        this.nombre = nombre;
        this.equipos = new ArrayList<>(listaEquipos);
        this.calendario = new ArrayList<>();
        this.jornadaActual = 1;
        this.registroGlobalGoles = new ArrayList<>();
        this.ligaIniciada = true;

        generarCalendarioOficial();
    }

    // =========================================================================
    // I. ALGORITMO INTEGRAL ROUND ROBIN (IDA Y VUELTA PERFECTO)
    // =========================================================================

    private void generarCalendarioOficial() {
        int numEquipos = equipos.size();
        List<Equipo> listadoRotativo = new ArrayList<>(equipos);

        for (int i = 0; i < (numEquipos - 1); i++) {
            Jornada jornadaIda = new Jornada(i + 1);

            for (int j = 0; j < numEquipos / 2; j++) {
                Equipo local = listadoRotativo.get(j);
                Equipo visitante = listadoRotativo.get(numEquipos - 1 - j);
                
                if (i % 2 == 0) {
                    jornadaIda.agregarPartido(new Partido(local, visitante));
                } else {
                    jornadaIda.agregarPartido(new Partido(visitante, local));
                }
            }
            calendario.add(jornadaIda);

            Equipo ultimoElemento = listadoRotativo.remove(listadoRotativo.size() - 1);
            listadoRotativo.add(1, ultimoElemento);
        }

        for (int i = 0; i < 5; i++) {
            Jornada jornadaIdaOriginal = calendario.get(i);
            Jornada jornadaVuelta = new Jornada(i + 6);

            for (Partido partidoIda : jornadaIdaOriginal.getPartidos()) {
                jornadaVuelta.agregarPartido(new Partido(partidoIda.getVisitante(), partidoIda.getLocal()));
            }
            calendario.add(jornadaVuelta);
        }
    }

    // =========================================================================
    // II. MOTOR DE SIMULACIÓN AUTOMÁTICA PARA PARTIDOS DE LA IA
    // =========================================================================

    public void simularPartidosRestantesIA(String nombreEquipoUsuario) {
        Jornada jornadaDeHoy = getJornadaProxima();
        if (jornadaDeHoy == null) return;

        Random generadorGoles = new Random();

        for (Partido partido : jornadaDeHoy.getPartidos()) {
            if (partido.getLocal().getNombre().equalsIgnoreCase(nombreEquipoUsuario) ||
                partido.getVisitante().getNombre().equalsIgnoreCase(nombreEquipoUsuario)) {
                continue; 
            }

            if (partido.isJugado()) {
                continue;
            }

            int golesLocal = calcularGolesSimulados(partido.getLocal(), true, generadorGoles);
            int golesVisitante = calcularGolesSimulados(partido.getVisitante(), false, generadorGoles);

            partido.finalizarPartido(golesLocal, golesVisitante);
            
            String log = String.format("Simulación IA - Jornada %d: %s %d - %d %s", 
                jornadaDeHoy.getNumero(), 
                partido.getLocal().getNombre(), golesLocal, 
                golesVisitante, partido.getVisitante().getNombre());
            registroGlobalGoles.add(log);
        }
    }

    private int calcularGolesSimulados(Equipo equipo, boolean esLocal, Random rand) {
        int factorSuerte = rand.nextInt(100);
        int baseGoles = 0;

        if (factorSuerte < 45) baseGoles = 1;      
        else if (factorSuerte < 75) baseGoles = 2; 
        else if (factorSuerte < 92) baseGoles = 3; 
        else baseGoles = rand.nextInt(3) + 4;      

        if (esLocal && rand.nextBoolean()) {
            baseGoles++; 
        }
        return baseGoles;
    }

    // =========================================================================
    // III. FILTROS Y BÚSQUEDAS TÁCTICAS DE CALENDARIO
    // =========================================================================

    public Partido obtenerPartidoDeEquipo(String nombreEquipo) {
        Jornada jornadaActualObjeto = getJornadaProxima();
        if (jornadaActualObjeto == null) return null;

        for (Partido p : jornadaActualObjeto.getPartidos()) {
            if (p.getLocal().getNombre().equalsIgnoreCase(nombreEquipo) || 
                p.getVisitante().getNombre().equalsIgnoreCase(nombreEquipo)) {
                return p;
            }
        }
        return null;
    }

    // =========================================================================
    // IV. MÉTODOS DE COMPILACIÓN EXACTOS
    // =========================================================================

    public int getNumeroJornadaActual() {
        return this.jornadaActual;
    }

    public boolean isFinalizada() {
        return this.jornadaActual > MAX_JORNADAS;
    }

    public List<Equipo> getClasificacion() {
        List<Equipo> tablaPosiciones = new ArrayList<>(this.equipos);
        
        tablaPosiciones.sort(new Comparator<Equipo>() {
            @Override
            public int compare(Equipo e1, Equipo e2) {
                if (e2.getPuntos() != e1.getPuntos()) {
                    return Integer.compare(e2.getPuntos(), e1.getPuntos());
                }
                if (e2.getDiferenciaGoles() != e1.getDiferenciaGoles()) {
                    return Integer.compare(e2.getDiferenciaGoles(), e1.getDiferenciaGoles());
                }
                return Integer.compare(e2.getGolesFavor(), e1.getGolesFavor());
            }
        });
        
        return tablaPosiciones;
    }

    public Jornada getJornadaProxima() {
        if (jornadaActual <= calendario.size() && jornadaActual > 0) {
            return calendario.get(jornadaActual - 1);
        }
        return null;
    }

    public void avanzarJornada() {
        if (jornadaActual <= MAX_JORNADAS) {
            jornadaActual++;
        }
    }

    public boolean esFinalDeTemporada() {
        return this.jornadaActual > MAX_JORNADAS;
    }

    public Equipo getCampeon() {
        if (!esFinalDeTemporada()) return null;
        return getClasificacion().get(0);
    }

    // =========================================================================
    // V. CLASES INTERNAS ASOCIADAS (ESTRUCTURAS COMPONENTES)
    // =========================================================================

    public class Jornada implements Serializable {
        private static final long serialVersionUID = 20260602L;
        
        private int numero;
        private List<Partido> partidos;

        public Jornada(int numero) {
            this.numero = numero;
            this.partidos = new ArrayList<>();
        }

        public void agregarPartido(Partido p) { 
            if (partidos.size() < 3) {
                partidos.add(p); 
            }
        }
        
        public List<Partido> getPartidos() { return this.partidos; }
        public int getNumero() { return this.numero; }
    }

    /**
     * Entidad de emparejamiento. Se añade setJugado() para compatibilidad con el controlador.
     */
    public class Partido implements Serializable {
        private static final long serialVersionUID = 20260603L;
        
        private Equipo local;
        private Equipo visitante;
        private int golesLocal;
        private int golesVisitante;
        private boolean jugado;

        public Partido(Equipo local, Equipo visitante) {
            this.local = local;
            this.visitante = visitante;
            this.golesLocal = 0;
            this.golesVisitante = 0;
            this.jugado = false;
        }

        /**
         * Registra los goles y delega de forma directa la asignación de puntos
         * a la capa de datos a través de 'registrarResultado'.
         */
        public void finalizarPartido(int golesL, int golesV) {
            if (this.jugado) {
                System.out.println("[ALERTA CRÍTICA] Partido ya disputado previamente: " + 
                                   local.getNombre() + " vs " + visitante.getNombre());
                return; 
            }

            this.golesLocal = golesL;
            this.golesVisitante = golesV;
            this.jugado = true;
            
            // Los equipos calculan de manera autónoma sus puntos ganados/perdidos y estadísticas
            this.local.registrarResultado(golesL, golesV);
            this.visitante.registrarResultado(golesV, golesL);
        }

        // --- CORRECCIÓN REQUERIDA POR CONTROLADORJUEGO ---
        public void setJugado(boolean jugado) {
            this.jugado = jugado;
        }

     // --- GETTERS CORREGIDOS ---
        public Equipo getLocal() { 
            return this.local; 
        }
        
        public Equipo getVisitante() { 
            return this.visitante; 
        }
        
        public int getGolesLocal() { 
            return this.golesLocal; 
        }
        
        // CORRECCIÓN: Retorna la variable directa en minúsculas y sin paréntesis
        public int getGolesVisitante() { 
            return this.golesVisitante; 
        }
        
        public boolean isJugado() { 
            return this.jugado; 
        }
    }

    // =========================================================================
    // VI. DIAGNÓSTICO Y ACCESORES
    // =========================================================================

    public String getNombre() { return nombre; }
    public int getJornadaActual() { return jornadaActual; }
    public List<Equipo> getEquipos() { return equipos; }
    public List<Jornada> getCalendario() { return calendario; }
    public List<String> getRegistroGlobalGoles() { return registroGlobalGoles; }

    public Equipo buscarEquipo(String nombreEq) {
        return equipos.stream()
                .filter(e -> e.getNombre().equalsIgnoreCase(nombreEq))
                .findFirst()
                .orElse(null);
    }
}
