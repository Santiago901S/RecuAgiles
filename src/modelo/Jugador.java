package modelo;

import java.io.Serializable;
import java.util.Random;

/**
 * =============================================================================
 * SUPREMA MANAGER 2026 - ENTIDAD JUGADOR
 * =============================================================================
 * Gestiona la lógica individual, progresión, estados y valor de mercado.
 */
public class Jugador implements Serializable {

    // --- IDENTIDAD ---
    private String nombre;
    private String nacionalidad;
    private int edad;
    private String posicion; // POR, DEF, MED, DEL
    private boolean esTitular;
    private String equipoActual;

    // --- ATRIBUTOS DE HABILIDAD (0-100) ---
    private int media;          // Valoración actual
    private int potencial;      // Hasta dónde puede llegar
    private int moral;          // Afecta al rendimiento en simulación
    private int energia;        // Baja al jugar, sube al descansar
    private int formaFisica;    // Capacidad de recuperación

    // --- ESTADÍSTICAS DE TEMPORADA ---
    private int goles;
    private int asistencias;
    private int partidosJugados;
    private double calificacionMedia;

    // --- ATRIBUTOS ECONÓMICOS ---
    private double precioMercado;
    private double salario;

    // --- UTILIDADES ---
    private static final Random random = new Random();

    /**
     * CONSTRUCTOR PRINCIPAL
     * @param nombre Nombre del crack
     * @param media Habilidad inicial
     * @param posicion POR, DEF, MED o DEL
     * @param precioBase Valor inicial para el mercado
     */
    public Jugador(String nombre, int media, String posicion, String equipo, double precioBase) {
        this.nombre = nombre;
        this.media = media;
        this.posicion = posicion;
        this.equipoActual = equipo;
        this.precioMercado = precioBase;
        
        // Inicialización lógica de atributos ocultos
        this.edad = 18 + random.nextInt(17); // Entre 18 y 35 años
        this.nacionalidad = generarNacionalidadAleatoria();
        this.potencial = calcularPotencial(media, edad);
        this.moral = 75; // Empiezan con moral neutra-alta
        this.energia = 100;
        this.formaFisica = 70 + random.nextInt(30);
        this.esTitular = false;
        
        // Stats iniciales
        this.goles = 0;
        this.asistencias = 0;
        this.partidosJugados = 0;
        this.calificacionMedia = 0.0;
        
        // Cálculo de salario proporcional a la calidad
        this.salario = (media * 1200.0) + (potencial * 500.0);
    }

    // =========================================================================
    // I. LÓGICA DE PROGRESIÓN Y ENTRENAMIENTO
    // =========================================================================

    /**
     * Calcula el potencial máximo. Los jóvenes tienen más margen de mejora.
     */
    private int calcularPotencial(int media, int edad) {
        if (edad < 22) return Math.min(99, media + random.nextInt(15) + 5);
        if (edad < 27) return Math.min(99, media + random.nextInt(8));
        return media; // Veteranos ya no suelen mejorar
    }

    /**
     * Simula la progresión tras entrenar o jugar.
     * Si es joven y tiene moral alta, sube de media.
     */
    public void entrenar() {
        if (edad < 28 && media < potencial) {
            double azar = random.nextDouble();
            if (moral > 85 && azar > 0.6) {
                media++;
                actualizarPrecioMercado();
            } else if (azar > 0.8) {
                media++;
                actualizarPrecioMercado();
            }
        } else if (edad > 32 && random.nextDouble() > 0.7) {
            media--; // Declive por edad
            actualizarPrecioMercado();
        }
    }

    // =========================================================================
    // II. LÓGICA DE MERCADO Y RENDIMIENTO
    // =========================================================================

    /**
     * Ajusta el precio según la media, la edad y los goles marcados.
     */
    public void actualizarPrecioMercado() {
        double factorEdad = (edad < 24) ? 1.4 : (edad > 31 ? 0.7 : 1.0);
        double factorGoles = 1.0 + (goles * 0.02);
        this.precioMercado = (media * 1_500_000.0) * factorEdad * factorGoles;
    }

    /**
     * Gestiona el cansancio físico tras un partido.
     */
    public void jugarPartido(int minutos) {
        this.partidosJugados++;
        int desgaste = (int) (minutos * 0.7);
        this.energia = Math.max(0, this.energia - desgaste);
        
        // Jugar sube la moral, ser suplente la baja
        this.moral = Math.min(100, this.moral + 5);
    }

    /**
     * Recuperación en días de descanso.
     */
    public void recuperarEnergia() {
        int recuperacion = 20 + (formaFisica / 5);
        this.energia = Math.min(100, this.energia + recuperacion);
    }

    // =========================================================================
    // III. GETTERS Y SETTERS (CONEXIÓN CON CONTROLADOR Y VISTA)
    // =========================================================================

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public int getMedia() { return media; }
    public void setMedia(int media) { this.media = media; }

    public String getPosicion() { return posicion; }
    public void setPosicion(String posicion) { this.posicion = posicion; }

    public boolean isEsTitular() { return esTitular; }
    public void setEsTitular(boolean esTitular) { this.esTitular = esTitular; }

    public int getMoral() { return moral; }
    public void setMoral(int moral) { this.moral = Math.max(0, Math.min(100, moral)); }

    public int getEnergia() { return energia; }
    public void setEnergia(int energia) { this.energia = energia; }

    public double getPrecioMercado() { return precioMercado; }
    public void setPrecioMercado(double precioMercado) { this.precioMercado = precioMercado; }

    public int getGoles() { return goles; }
    public void registrarGol() { 
        this.goles++; 
        this.moral = Math.min(100, this.moral + 10);
        actualizarPrecioMercado();
    }

    public int getAsistencias() { return asistencias; }
    public void registrarAsistencia() { this.asistencias++; }

    public int getEdad() { return edad; }
    public void setEdad(int edad) { this.edad = edad; }

    public String getNacionalidad() { return nacionalidad; }
    
    public String getEquipoActual() { return equipoActual; }
    public void setEquipoActual(String equipoActual) { this.equipoActual = equipoActual; }

    // =========================================================================
    // IV. MÉTODOS AUXILIARES
    // =========================================================================

    private String generarNacionalidadAleatoria() {
        String[] paises = {"España", "Brasil", "Argentina", "Francia", "Holanda", "Italia", "Portugal", "Alemania"};
        return paises[random.nextInt(paises.length)];
    }

    /**
     * Determina si el jugador puede jugar o está demasiado cansado.
     */
    public boolean puedeJugar() {
        return energia > 40;
    }

    /**
     * Devuelve una versión corta de la posición para la vista del campo.
     */
    public String getPosicionAbreviada() {
        return posicion.substring(0, 3).toUpperCase();
    }

    @Override
    public String toString() {
        return nombre + " (" + posicion + ") - Media: " + media + " - " + String.format("%.1f", precioMercado/1000000) + "M€";
    }

    /**
     * Lógica para el Panel de Táctica: Devuelve el color según la calidad.
     */
    public String getColorCalidad() {
        if (media >= 90) return "#FFD700"; // Oro para cracks
        if (media >= 80) return "#C0C0C0"; // Plata
        return "#CD7F32"; // Bronce
    }

    // Métodos para persistencia y comparación
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Jugador other = (Jugador) obj;
        return nombre.equals(other.nombre) && equipoActual.equals(other.equipoActual);
    }

    @Override
    public int hashCode() {
        return nombre.hashCode() + equipoActual.hashCode();
    }
}