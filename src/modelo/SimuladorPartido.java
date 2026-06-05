package modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * =============================================================================
 * SUPREMA MANAGER 2026 - ELITE MATCH ENGINE (V3.0)
 * =============================================================================

 */
public class SimuladorPartido implements Serializable {

    private final Random random = new Random();
    private List<EventoPartido> cronica;
    private int golesL, golesV;
    private int tirosL, tirosV;
    private int faltasL, faltasV;
    private double posesionL;

    // --- DICCIONARIOS EXPANDIDOS PARA VARIABILIDAD ---
    private final String[] COMENTARIOS_GOL = {
        "¡BRUTAL! Zarpazo tremendo de ",
        "¡GOLAZO! La puso en la escuadra ",
        "¡GOOOOOL! No perdonó en el mano a mano ",
        "¡Sentencia con clase! Define perfecto "
    };

    private final String[] COMENTARIOS_FALLO = {
        "¡Al palo! El sonido de la madera retumba en el estadio.",
        "¡Paradón estratosférico! El portero vuela para evitar el gol.",
        "Se va por encima del larguero... mala ejecución.",
        "¡Increíble! La defensa la saca sobre la misma línea de cal."
    };

    private final String[] COMENTARIOS_FALTA = {
        "Entrada durísima. El árbitro señala la infracción.",
        "Juego parado por falta táctica en el centro del campo.",
        "¡Amarilla clara! El jugador llega tarde y derriba al rival.",
        "Choque de trenes. Se duele el delantero en el césped."
    };

    public SimuladorPartido() {
        this.cronica = new ArrayList<>();
    }

    /**
     * MÉTODO PRINCIPAL: Simula los 90 minutos y genera la lista de eventos.
     */
    public List<EventoPartido> simular(Equipo local, Equipo visitante) {
        resetearEstadisticas();
        
        // 1. Cálculo de Posesión Base según calidad de Medios
        this.posesionL = calcularPosesionBase(local, visitante);

        // 2. Evento Inicial
        registrarEvento(0, "INFO", "¡Arranca el espectáculo! Se enfrentan " + local.getNombre() + " y " + visitante.getNombre());

        // 3. Simulación Minuto a Minuto
        for (int min = 1; min <= 90; min++) {
            
            // Lógica de fatiga: a más minuto, más probabilidad de error y faltas
            double factorCansancio = 1.0 + (min / 100.0);
            
            // ¿Ocurre algo en este minuto?
            if (random.nextInt(100) < (15 * factorCansancio)) {
                procesarMinuto(min, local, visitante);
            }

            // Eventos fijos
            if (min == 45) registrarEvento(45, "INFO", "--- DESCANSO: Los jugadores toman aire ---");
            if (min == 80) registrarEvento(80, "INFO", "¡Tramo final! La tensión se corta en el ambiente.");
        }

        // 4. Cierre del partido
        registrarEvento(90, "FIN", "¡FINAL! " + local.getNombre() + " " + golesL + " - " + golesV + " " + visitante.getNombre());
        actualizarEstadoJugadores(local, visitante);

        return cronica;
    }

    private void procesarMinuto(int minuto, Equipo local, Equipo visitante) {
        // Determinar quién tiene el balón este minuto (basado en posesión)
        boolean atacaLocal = random.nextInt(100) < posesionL;
        Equipo atacante = atacaLocal ? local : visitante;
        Equipo defensor = atacaLocal ? visitante : local;

        int dado = random.nextInt(100);

        if (dado < 25) { // 25% de que la acción sea un TIRO
            intentarGol(minuto, atacante, defensor, atacaLocal);
        } 
        else if (dado < 45) { // 20% de que sea una FALTA
            cometerFalta(minuto, atacante, defensor, atacaLocal);
        }
        else { // El resto es circulación de balón (solo para la crónica si es relevante)
            if (random.nextInt(100) < 5) {
                registrarEvento(minuto, "INFO", "El " + atacante.getNombre() + " domina el balón buscando huecos.");
            }
        }
    }

    private void intentarGol(int minuto, Equipo atq, Equipo def, boolean esLocal) {
        if (esLocal) tirosL++; else tirosV++;

        // Fórmula de éxito: (Ataque Atacante - Defensa Defensor) + Bonus de Moral
        int potenciaAtq = atq.getNivelAtaque() + (atq.getMoralMedia() / 10);
        int muroDef = def.getNivelDefensa();
        
        int probabilidadExito = 15 + (potenciaAtq - muroDef);
        
        // Seleccionamos al jugador que realiza la acción
        Jugador j = seleccionarJugadorActivo(atq, "DEL");

        if (random.nextInt(100) < probabilidadExito) {
            // ¡GOOOL!
            if (esLocal) golesL++; else golesV++;
            String desc = COMENTARIOS_GOL[random.nextInt(COMENTARIOS_GOL.length)] + j.getNombre();
            registrarEvento(minuto, "GOL", desc);
            j.registrarGol(); // Sumar a sus stats individuales
        } else {
            // FALLO
            String desc = COMENTARIOS_FALLO[random.nextInt(COMENTARIOS_FALLO.length)] + " (" + j.getNombre() + ")";
            registrarEvento(minuto, "TIRO", desc);
        }
    }

    private void cometerFalta(int minuto, Equipo atq, Equipo def, boolean esLocal) {
        if (esLocal) faltasL++; else faltasV++;
        
        Jugador infractor = seleccionarJugadorActivo(def, "DEF");
        String desc = COMENTARIOS_FALTA[random.nextInt(COMENTARIOS_FALTA.length)] + " de " + infractor.getNombre();
        
        registrarEvento(minuto, "FALTA", desc);

        // Posibilidad de tarjeta
        if (random.nextInt(100) < 20) {
            registrarEvento(minuto, "TARJETA", "¡AMARILLA! El árbitro amonesta a " + infractor.getNombre());
            infractor.setMoral(infractor.getMoral() - 10);
        }
    }

    /**
     * Calcula la posesión basándose en la media de los equipos.
     */
    private double calcularPosesionBase(Equipo l, Equipo v) {
        int total = l.getMediaGlobal() + v.getMediaGlobal();
        return ((double) l.getMediaGlobal() / total) * 100;
    }

    /**
     
     */
    private Jugador seleccionarJugadorActivo(Equipo eq, String posicionPreferencia) {
        List<Jugador> titulares = eq.getTitulares();
        if (titulares.isEmpty()) return null;

        // Sistema de pesos: los delanteros tienen más peso para goles, defensas para faltas
        List<Jugador> urna = new ArrayList<>();
        for (Jugador j : titulares) {
            int peso = 1;
            if (j.getPosicion().equals(posicionPreferencia)) peso = 5;
            for (int i = 0; i < peso; i++) urna.add(j);
        }
        return urna.get(random.nextInt(urna.size()));
    }

    private void actualizarEstadoJugadores(Equipo l, Equipo v) {
        // Reducir energía (Stamina) y ajustar moral tras el resultado
        l.getPlantilla().forEach(j -> j.jugarPartido(90));
        v.getPlantilla().forEach(j -> j.jugarPartido(90));
    }

    private void registrarEvento(int min, String tipo, String desc) {
        cronica.add(new EventoPartido(min, tipo, desc));
    }

    private void resetearEstadisticas() {
        this.cronica.clear();
        this.golesL = 0; this.golesV = 0;
        this.tirosL = 0; this.tirosV = 0;
        this.faltasL = 0; this.faltasV = 0;
    }

    // --- GETTERS ---
    public int getGolesLocal() { return golesL; }
    public int getGolesVisitante() { return golesV; }
    public int getTirosL() { return tirosL; }
    public int getTirosV() { return tirosV; }
    public double getPosesionL() { return posesionL; }

    // --- CLASE INTERNA DE EVENTO ---
    public static class EventoPartido implements Serializable {
        private int minuto;
        private String tipo;
        private String descripcion;

        public EventoPartido(int min, String tipo, String desc) {
            this.minuto = min;
            this.tipo = tipo;
            this.descripcion = desc;
        }

        public int getMinuto() { return minuto; }
        public String getTipo() { return tipo; }
        public String getDescripcion() { return descripcion; }

        @Override
        public String toString() { return "[" + minuto + "'] " + descripcion; }
    }
    
    
    
}