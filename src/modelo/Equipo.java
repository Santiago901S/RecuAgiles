package modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * =============================================================================
 * SUPREMA MANAGER 2026 - ENTIDAD EQUIPO CORE (V3.5 PRO)
 * =============================================================================
 
 */
public class Equipo implements Serializable {

    private static final long serialVersionUID = 2026L;

    // --- IDENTIDAD INSTITUCIONAL ---
    private String nombre;
    private String nombreDirector;
    private String estadio;
    private String rutaEscudo; 
    
    // --- BALANCES ECONÓMICOS ---
    private double presupuesto;
    private double ingresosPorEntradas;
    private final double GASTO_MANTENIMIENTO = 50000.0;

    // --- ESTRUCTURA DE LA PLANTILLA DE FUTBOLISTAS ---
    private List<Jugador> plantilla;
    private String formacionActual; 
    private final int MAX_PLANTILLA = 16; 

    // --- REGISTROS ESTADÍSTICOS DE LA LIGA (12 JORNADAS) ---
    private int puntos;
    private int partidosJugados;
    private int partidosGanados;
    private int partidosEmpatados;
    private int partidosPerdidos;
    private int golesFavor;
    private int golesContra;
   
    

    /**
     * Constructor principal para la fundación de una entidad deportiva de élite.
     * @param nombre Nombre oficial del club de fútbol.
     * @param rutaEscudo Ubicación o nombre de recurso del identificador gráfico.
     * @param presupuestoInicial Capital bancario líquido asignado para operaciones.
     */
    public Equipo(String nombre, String rutaEscudo, double presupuestoInicial) {
        this.nombre = nombre;
        this.rutaEscudo = rutaEscudo;
        this.presupuesto = presupuestoInicial;
        this.estadio = "Estadio " + nombre;
        this.nombreDirector = "Mánager General";
        this.plantilla = new ArrayList<>();
        this.formacionActual = "4-4-2";
        
        reiniciarEstadisticas();
    }

    /**
     * Blanquea de forma absoluta el rendimiento deportivo a valores cero.
     */
    public final void reiniciarEstadisticas() {
        this.puntos = 0;
        this.partidosJugados = 0;
        this.partidosGanados = 0;
        this.partidosEmpatados = 0;
        this.partidosPerdidos = 0;
        this.golesFavor = 0;
        this.golesContra = 0;
        this.ingresosPorEntradas = 0.0;
        
       
       
    }

    // =========================================================================
    // I. MOTOR DE ANÁLISIS DE RENDIMIENTO (MATH & STATS RATINGS)
    // =========================================================================

    /**
     * Evalúa aritméticamente el nivel del plantel basándose exclusivamente 
     * en el grupo selecto de los 11 jugadores configurados como titulares.
     */
    public int getMediaGlobal() {
        List<Jugador> titulares = getTitulares();
        if (titulares.isEmpty()) return 0;

        double suma = titulares.stream().mapToInt(Jugador::getMedia).sum();
        return (int) Math.round(suma / titulares.size());
    }

    /**
     * Calcula la potencia de fuego en fase ofensiva analizando la media ponderada
     * de los jugadores con etiquetas posicionales de ataque ("DEL") y mediocampo ("MED").
     */
    public int getNivelAtaque() {
        return (int) Math.round(getTitulares().stream()
                .filter(j -> j.getPosicion().equals("DEL") || j.getPosicion().equals("MED"))
                .mapToInt(Jugador::getMedia)
                .average().orElse(0.0));
    }

    /**
     * Determina el muro defensivo analizando el rendimiento promedio del arquero 
     * ("POR") junto al bloque de zagueros y laterales ("DEF").
     */
    public int getNivelDefensa() {
        return (int) Math.round(getTitulares().stream()
                .filter(j -> j.getPosicion().equals("DEF") || j.getPosicion().equals("POR"))
                .mapToInt(Jugador::getMedia)
                .average().orElse(0.0));
    }

    /**
     * Obtiene el promedio anímico y de concentración de los futbolistas del primer equipo.
     */
    public int getMoralMedia() {
        if (plantilla.isEmpty()) return 0;
        return (int) Math.round(plantilla.stream()
                .mapToInt(Jugador::getMoral)
                .average().orElse(0.0));
    }

    // =========================================================================
    // II. SISTEMA INTELIGENTE DE SELECCIÓN Y FILTRADO POSICIONAL
    // =========================================================================

    /**
     * Aplica un algoritmo codicioso automático (Greedy) para reestructurar el once 
     * inicial asignando la etiqueta de titularidad a los 11 jugadores con mayor puntaje OVR.
     */
    public void autoGestionarTitulares() {
        if (plantilla.isEmpty()) return;

        // Reset general preventivo
        plantilla.forEach(j -> j.setEsTitular(false));
        
        // Extracción ordenada mediante streams de la cúspide de la plantilla
        List<Jugador> mejoresElementos = plantilla.stream()
                .sorted(Comparator.comparingInt(Jugador::getMedia).reversed())
                .limit(11)
                .collect(Collectors.toList());
        
        mejoresElementos.forEach(j -> j.setEsTitular(true));
    }

    public List<Jugador> getTitulares() {
        return plantilla.stream().filter(Jugador::isEsTitular).collect(Collectors.toList());
    }

    public List<Jugador> getSuplentes() {
        return plantilla.stream().filter(j -> !j.isEsTitular()).collect(Collectors.toList());
    }

    public List<Jugador> getPorteros() {
        return plantilla.stream().filter(j -> j.getPosicion().equals("POR")).collect(Collectors.toList());
    }

    public List<Jugador> getDefensas() {
        return plantilla.stream().filter(j -> j.getPosicion().equals("DEF")).collect(Collectors.toList());
    }

    public List<Jugador> getMediocampistas() {
        return plantilla.stream().filter(j -> j.getPosicion().equals("MED")).collect(Collectors.toList());
    }

    public List<Jugador> getDelanteros() {
        return plantilla.stream().filter(j -> j.getPosicion().equals("DEL")).collect(Collectors.toList());
    }

    // =========================================================================
    // III. OPERACIONES DE MERCADO DE TRANSFERENCIAS Y AUDITORÍA
    // =========================================================================

    /**
     * Ejecuta el protocolo corporativo para incorporar un nuevo elemento a la disciplina.
     * Valida límites estructurales de plantilla y liquidez económica del club.
     */
    public boolean ficharJugador(Jugador nuevoFichaje) {
        if (plantilla.size() >= MAX_PLANTILLA) {
            System.err.println("[OPERACIÓN NEGADA]: Cupo institucional saturado (" + MAX_PLANTILLA + " futbolistas).");
            return false;
        }
        if (presupuesto < nuevoFichaje.getPrecioMercado()) {
            System.err.println("[OPERACIÓN NEGADA]: Insolvencia para afrontar cláusula de rescisión.");
            return false;
        }

        this.presupuesto -= nuevoFichaje.getPrecioMercado();
        nuevoFichaje.setEquipoActual(this.nombre);
        this.plantilla.add(nuevoFichaje);
        
        // Re-calibrar la alineación tras alterar las fuerzas del plantel
        autoGestionarTitulares();
        return true;
    }

    /**
     * Declara transferible a un jugador, removiéndolo de la plantilla y reintegrando
     * un valor de amortización correspondiente al 90% de su valor comercial actual.
     */
    public void venderJugador(Jugador transferido) {
        if (plantilla.contains(transferido)) {
            double capitalRecuperado = transferido.getPrecioMercado() * 0.9;
            this.presupuesto += capitalRecuperado;
            this.plantilla.remove(transferido);
            
            autoGestionarTitulares();
            System.out.println("[VENTA CONFIRMADA]: Liquidación exitosa de " + transferido.getNombre());
        }
    }

    /**
     * Rastrea de forma secuencial la existencia de un futbolista mediante su clave alfabética.
     */
    public Jugador buscarJugador(String criterioNombre) {
        return plantilla.stream()
                .filter(j -> j.getNombre().equalsIgnoreCase(criterioNombre))
                .findFirst()
                .orElse(null);
    }
    
    /**
     * Calcula y devuelve la valoración media global (OVR) del equipo actual.
     * Es utilizado por el motor de simulación para determinar el peso táctico en los partidos.
     * * @return Promedio entero de la valoración de la plantilla, o 0 si está vacía.
     */
    public int getMediaEquipo() {
        if (this.plantilla == null || this.plantilla.isEmpty()) {
            return 0;
        }
        
        int sumaMedia = 0;
        for (Jugador jugador : this.plantilla) {
            sumaMedia += jugador.getMedia(); // Asegúrate de que tu clase Jugador use getMedia()
        }
        
        return sumaMedia / this.plantilla.size();
    }

    // =========================================================================
    // IV. RUTINAS DE CONTABILIDAD Y EVALUACIÓN POST-PARTIDO
    // =========================================================================

    /**
     * Actualiza las finanzas, goles e historiales del club. 
     * (Útil cuando los cálculos se centralizan en subrutinas del controlador).
     */
    public void registrarResultado(int golesPropios, int golesRival) {
        this.partidosJugados++;
        this.golesFavor += golesPropios;
        this.golesContra += golesRival;

        if (golesPropios > golesRival) {
            this.puntos += 3;
            this.partidosGanados++;
            this.presupuesto += 1000000.0; 
        } else if (golesPropios == golesRival) {
            this.puntos += 1;
            this.partidosEmpatados++;
            this.presupuesto += 400000.0; 
        } else {
            this.partidosPerdidos++;
        }
        
        this.presupuesto -= GASTO_MANTENIMIENTO;
    }

    /**
     * Cobra de forma directa ingresos por la venta de boletos del estadio.
     */
    public void inyectarTaquilla(double recaudacion) {
        this.ingresosPorEntradas += recaudacion;
        this.presupuesto += recaudacion;
    }

    // =========================================================================
    // V. SECCIÓN CRÍTICA CORREGIDA: ACCESSORS & MUTATORS (GETTERS Y SETTERS)
    // =========================================================================

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getRutaEscudo() { return rutaEscudo; }
    public void setRutaEscudo(String rutaEscudo) { this.rutaEscudo = rutaEscudo; }

    public double getPresupuesto() { return presupuesto; }
    public void setPresupuesto(double presupuesto) { this.presupuesto = presupuesto; }

    public List<Jugador> getPlantilla() { return plantilla; }
    public void setPlantilla(List<Jugador> plantilla) { this.plantilla = plantilla; }

    // --- SETTERS IMPRESCINDIBLES EXIGIDOS POR EL PANEL DE SIMULACIÓN Y CONTROLADOR ---
    public int getPuntos() { return puntos; }
    public void setPuntos(int puntos) { this.puntos = puntos; }

    public int getPartidosJugados() { return partidosJugados; }
    public void setPartidosJugados(int partidosJugados) { this.partidosJugados = partidosJugados; }

    public int getPartidosGanados() { return partidosGanados; }
    public void setPartidosGanados(int partidosGanados) { this.partidosGanados = partidosGanados; }

    public int getPartidosEmpatados() { return partidosEmpatados; }
    public void setPartidosEmpatados(int partidosEmpatados) { this.partidosEmpatados = partidosEmpatados; }

    public int getPartidosPerdidos() { return partidosPerdidos; }
    public void setPartidosPerdidos(int partidosPerdidos) { this.partidosPerdidos = partidosPerdidos; }

    public int getGolesFavor() { return golesFavor; }
    public void setGolesFavor(int golesFavor) { this.golesFavor = golesFavor; }

    public int getGolesContra() { return golesContra; }
    public void setGolesContra(int golesContra) { this.golesContra = golesContra; }

    // --- GETTERS COMPLEMENTARIOS Y METADATOS ---
    public int getDiferenciaGoles() { return golesFavor - golesContra; }
    
    public String getEstadio() { return estadio; }
    public void setEstadio(String estadio) { this.estadio = estadio; }

    public String getNombreDirector() { return nombreDirector; }
    public void setNombreDirector(String nombreDirector) { this.nombreDirector = nombreDirector; }

    public String getFormacionActual() { return formacionActual; }
    public void setFormacionActual(String formacionActual) { this.formacionActual = formacionActual; }

    public double getIngresosPorEntradas() { return ingresosPorEntradas; }
    public double getGastoMantenimiento() { return GASTO_MANTENIMIENTO; }
    public int getMaxPlantilla() { return MAX_PLANTILLA; }

    @Override
    public String toString() {
        return String.format("%s [OVR: %d | PTS: %d]", nombre, getMediaGlobal(), puntos);
    }
}