package vista;

import modelo.Equipo;
import modelo.SimuladorPartido.EventoPartido;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Random;

/**
 * =============================================================================
 * SUPREMA MANAGER 2026 - ADVANCED MATCH BROADCAST ENGINE (PRO EDITION)
 * =============================================================================
 * Panel de simulación interactiva minuto a minuto en tiempo real.
 * Renderiza el marcador dinámico, procesa el cálculo de goles/tiros basado en OVR,
 * gestiona la fluctuación de estadísticas y actualiza las actas oficiales de la Liga.
 */
public class PanelSimulacion extends JPanel {

    // --- PALETA DE COLORES PREMIUM (ESTILO TV DEPORTIVA) ---
    private final Color COLOR_FONDO         = new Color(11, 14, 23);
    private final Color COLOR_TARJETA       = new Color(19, 23, 36);
    private final Color CIAN_NEON           = new Color(0, 240, 255);
    private final Color VERDE_CESPED        = new Color(46, 204, 113);
    private final Color AMARILLO_LIVE       = new Color(241, 196, 15);
    private final Color ROJO_ALERTA         = new Color(231, 76, 60);
    private final Color BLANCO_TEXTO        = new Color(240, 244, 255);
    private final Color GRIS_PANEL          = new Color(33, 41, 61);

    // --- COMPONENTES GRÁFICOS CORE ---
    private JLabel lblNombreLocal, lblNombreVisitante;
    private JLabel lblGolesLocal, lblGolesVisitante;
    private JLabel lblMarcadorTiempo, lblEstadoPartido;
    private JTextArea txtCronica;
    private JButton btnContinuar;

    // --- COMPONENTES DE ESTADÍSTICAS ---
    private JLabel lblPosesionL, lblPosesionV;
    private JLabel lblTirosL, lblTirosV;
    private JLabel lblFaltasL, lblFaltasV;

    // --- VARIABLES DE CONTROL INTERNO DEL MOTOR ---
    private Equipo equipoLocal;
    private Equipo equipoVisitante;
    private Timer timerMotor;
    private int minutoActual;
    private final Random random;

    // --- CONTADORES ESTADÍSTICOS EN VIVO ---
    private int golesL, golesV;
    private int tirosL, tirosV;
    private int faltasL, faltasV;
    private int posesionL, posesionV;

    public PanelSimulacion() {
        this.random = new Random();
        setLayout(new BorderLayout());
        setBackground(COLOR_FONDO);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        inicializarComponentes();
    }

    private void inicializarComponentes() {
        // =====================================================================
        // 1. MARCADOR PRINCIPAL SUPERIOR (ESTILO RETRANSMISIÓN DE TV)
        // =====================================================================
        JPanel pnlMarcador = new JPanel(new GridLayout(1, 5, 10, 0));
        pnlMarcador.setBackground(COLOR_TARJETA);
        pnlMarcador.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(GRIS_PANEL, 2),
                new EmptyBorder(15, 20, 15, 20)
        ));

        lblNombreLocal = new JLabel("LOCAL", SwingConstants.RIGHT);
        lblNombreLocal.setFont(new Font("Segoe UI Black", Font.BOLD, 18));
        lblNombreLocal.setForeground(BLANCO_TEXTO);

        lblGolesLocal = new JLabel("0", SwingConstants.CENTER);
        lblGolesLocal.setFont(new Font("Consolas", Font.BOLD, 36));
        lblGolesLocal.setForeground(CIAN_NEON);

        // Bloque central de tiempo de juego
        JPanel pnlTiempo = new JPanel(new GridLayout(2, 1));
        pnlTiempo.setOpaque(false);
        lblMarcadorTiempo = new JLabel("00:00", SwingConstants.CENTER);
        lblMarcadorTiempo.setFont(new Font("Consolas", Font.BOLD, 22));
        lblMarcadorTiempo.setForeground(AMARILLO_LIVE);
        lblEstadoPartido = new JLabel("PRE-MATCH", SwingConstants.CENTER);
        lblEstadoPartido.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lblEstadoPartido.setForeground(Color.GRAY);
        pnlTiempo.add(lblMarcadorTiempo);
        pnlTiempo.add(lblEstadoPartido);

        lblGolesVisitante = new JLabel("0", SwingConstants.CENTER);
        lblGolesVisitante.setFont(new Font("Consolas", Font.BOLD, 36));
        lblGolesVisitante.setForeground(CIAN_NEON);

        lblNombreVisitante = new JLabel("VISITANTE", SwingConstants.LEFT);
        lblNombreVisitante.setFont(new Font("Segoe UI Black", Font.BOLD, 18));
        lblNombreVisitante.setForeground(BLANCO_TEXTO);

        pnlMarcador.add(lblNombreLocal);
        pnlMarcador.add(lblGolesLocal);
        pnlMarcador.add(pnlTiempo);
        pnlMarcador.add(lblGolesVisitante);
        pnlMarcador.add(lblNombreVisitante);

        add(pnlMarcador, BorderLayout.NORTH);

        // =====================================================================
        // 2. PANEL CENTRAL DE DATOS (CRÓNICA + ESTADÍSTICAS)
        // =====================================================================
        JPanel pnlCentral = new JPanel(new BorderLayout(20, 0));
        pnlCentral.setOpaque(false);
        pnlCentral.setBorder(new EmptyBorder(20, 0, 20, 0));

        // Crónica Minuto a Minuto
        txtCronica = new JTextArea();
        txtCronica.setEditable(false);
        txtCronica.setBackground(new Color(14, 18, 29));
        txtCronica.setForeground(Color.LIGHT_GRAY);
        txtCronica.setFont(new Font("Consolas", Font.PLAIN, 13));
        txtCronica.setMargin(new Insets(10, 10, 10, 10));
        JScrollPane scrollCronica = new JScrollPane(txtCronica);
        scrollCronica.setBorder(BorderFactory.createLineBorder(GRIS_PANEL, 1));

        // Subpanel Lateral de Estadísticas en Vivo
        JPanel pnlEstadisticas = new JPanel();
        pnlEstadisticas.setLayout(new BoxLayout(pnlEstadisticas, BoxLayout.Y_AXIS));
        pnlEstadisticas.setBackground(COLOR_TARJETA);
        pnlEstadisticas.setPreferredSize(new Dimension(300, 400));
        pnlEstadisticas.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(GRIS_PANEL, 1),
                new EmptyBorder(15, 15, 15, 15)
        ));

        JLabel lblTituloEst = new JLabel("ESTADÍSTICAS DEL PARTIDO", SwingConstants.CENTER);
        lblTituloEst.setFont(new Font("Segoe UI Black", Font.BOLD, 12));
        lblTituloEst.setForeground(CIAN_NEON);
        lblTituloEst.setAlignmentX(CENTER_ALIGNMENT);
        pnlEstadisticas.add(lblTituloEst);
        pnlEstadisticas.add(Box.createVerticalStrut(25));

        // Filas de estadísticas
        lblPosesionL = new JLabel("50%"); lblPosesionV = new JLabel("50%");
        pnlEstadisticas.add(crearFilaEstadistica("POSESIÓN", lblPosesionL, lblPosesionV));
        
        lblTirosL = new JLabel("0"); lblTirosV = new JLabel("0");
        pnlEstadisticas.add(crearFilaEstadistica("TIROS A PUERTA", lblTirosL, lblTirosV));
        
        lblFaltasL = new JLabel("0"); lblFaltasV = new JLabel("0");
        pnlEstadisticas.add(crearFilaEstadistica("FALTAS", lblFaltasL, lblFaltasV));

        pnlCentral.add(scrollCronica, BorderLayout.CENTER);
        pnlCentral.add(pnlEstadisticas, BorderLayout.EAST);

        add(pnlCentral, BorderLayout.CENTER);

        // =====================================================================
        // 3. BARRA INFERIOR DE ACCIÓN (BOTÓN CONTINUAR COMPETICIÓN)
        // =====================================================================
        btnContinuar = new JButton("SIMULANDO ENCUENTRO...");
        btnContinuar.setEnabled(false);
        btnContinuar.setFont(new Font("Segoe UI Black", Font.BOLD, 14));
        btnContinuar.setBackground(GRIS_PANEL);
        btnContinuar.setForeground(Color.GRAY);
        btnContinuar.setPreferredSize(new Dimension(0, 50));
        btnContinuar.setFocusPainted(false);
        btnContinuar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        add(btnContinuar, BorderLayout.SOUTH);
    }

    private JPanel crearFilaEstadistica(String titulo, JLabel lblL, JLabel lblV) {
        JPanel pnl = new JPanel(new BorderLayout());
        pnl.setOpaque(false);
        pnl.setMaximumSize(new Dimension(280, 45));

        lblL.setFont(new Font("Consolas", Font.BOLD, 14));
        lblL.setForeground(BLANCO_TEXTO);
        lblV.setFont(new Font("Consolas", Font.BOLD, 14));
        lblV.setForeground(BLANCO_TEXTO);

        JLabel lblMid = new JLabel(titulo, SwingConstants.CENTER);
        lblMid.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lblMid.setForeground(Color.GRAY);

        pnl.add(lblL, BorderLayout.WEST);
        pnl.add(lblMid, BorderLayout.CENTER);
        pnl.add(lblV, BorderLayout.EAST);
        
        // Separador sutil inferior
        pnl.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(40, 50, 70)));
        return pnl;
    }

    /**
     * Inicializa y arranca el loop del motor de simulación para el Matchday.
     */
    public void comenzarSimulacion(Equipo local, Equipo visitante, List<EventoPartido> eventosIgnorados) {
        this.equipoLocal = local;
        this.equipoVisitante = visitante;

        // Reset completo de contadores del partido
        this.minutoActual = 0;
        this.golesL = 0; this.golesV = 0;
        this.tirosL = 0; this.tirosV = 0;
        this.faltasL = 0; this.faltasV = 0;
        this.posesionL = 50; this.posesionV = 50;

        // Inyección de textos iniciales
        lblNombreLocal.setText(local.getNombre().toUpperCase());
        lblNombreVisitante.setText(visitante.getNombre().toUpperCase());
        lblGolesLocal.setText("0");
        lblGolesVisitante.setText("0");
        lblMarcadorTiempo.setText("00:00");
        lblEstadoPartido.setText("LIVE");
        txtCronica.setText(">> ¡PITIDO INICIAL! Comienza el partido en el coliseo del club local.\n");

        btnContinuar.setEnabled(false);
        btnContinuar.setText("SIMULANDO ENCUENTRO...");
        btnContinuar.setBackground(GRIS_PANEL);
        btnContinuar.setForeground(Color.GRAY);

        // Configuración y ejecución del bucle del cronómetro (100ms por minuto de juego)
        if (timerMotor != null && timerMotor.isRunning()) {
            timerMotor.stop();
        }

        timerMotor = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ejecutarMinutoSimulacion();
            }
        });
        timerMotor.start();
    }

    /**
     * Ejecuta los cálculos algoritmos de probabilidad e interacciones por cada minuto corrido.
     */
    private void ejecutarMinutoSimulacion() {
        minutoActual++;
        lblMarcadorTiempo.setText(String.format("%02d:00", minutoActual));

        // 1. Algoritmo de Fluctuación Dinámica de Posesión basado en la media (OVR)
        int balanceOvr = equipoLocal.getMediaEquipo() - equipoVisitante.getMediaEquipo();
        int basePosesion = 50 + (balanceOvr / 2);
        posesionL = basePosesion + random.nextInt(11) - 5; // Ruido de oscilación aleatoria +/- 5%
        posesionL = Math.max(25, Math.min(75, posesionL)); // Límites lógicos de fútbol
        posesionV = 100 - posesionL;

        // 2. Probabilidades de Ataque por Minuto (Tiros, Goles y Faltas)
        double factorAtaqueLocal = (equipoLocal.getMediaEquipo() * 0.04) + (posesionL * 0.02);
        double factorAtaqueVisitante = (equipoVisitante.getMediaEquipo() * 0.04) + (posesionV * 0.02);

        // ¿Sucedió un ataque del Local?
        if (random.nextDouble() * 100 < factorAtaqueLocal) {
            tirosL++;
            if (random.nextDouble() * 100 < 22) { // 22% de probabilidad de que el tiro sea gol
                golesL++;
                lblGolesLocal.setText(String.valueOf(golesL));
                txtCronica.append(String.format("[%02d'] ⚽ ¡GOOOL DEL %s! Excelente remate a la escuadra.\n", 
                        minutoActual, equipoLocal.getNombre().toUpperCase()));
            } else {
                txtCronica.append(String.format("[%02d'] 🔥 Ocasión de peligro del %s. El disparo se marcha desviado.\n", 
                        minutoActual, equipoLocal.getNombre().toUpperCase()));
            }
        }

        // ¿Sucedió un ataque del Visitante?
        if (random.nextDouble() * 100 < factorAtaqueVisitante) {
            tirosV++;
            if (random.nextDouble() * 100 < 22) {
                golesV++;
                lblGolesVisitante.setText(String.valueOf(golesV));
                txtCronica.append(String.format("[%02d'] ⚽ ¡GOOOL DEL %s! Contraataque letal que bate al portero.\n", 
                        minutoActual, equipoVisitante.getNombre().toUpperCase()));
            } else {
                txtCronica.append(String.format("[%02d'] ⚠️ Remate potente del %s que detiene el guardameta en dos tiempos.\n", 
                        minutoActual, equipoVisitante.getNombre().toUpperCase()));
            }
        }

        // Simulación esporádica de faltas reglamentarias
        if (random.nextInt(100) < 8) {
            if (random.nextBoolean()) {
                faltasL++;
                if (random.nextInt(10) < 2) {
                    txtCronica.append(String.format("[%02d'] 🟨 Tarjeta amarilla para el centrocampista del %s.\n", minutoActual, equipoLocal.getNombre().toUpperCase()));
                }
            } else {
                faltasV++;
                if (random.nextInt(10) < 2) {
                    txtCronica.append(String.format("[%02d'] 🟨 Tarjeta amarilla para el defensor del %s.\n", minutoActual, equipoVisitante.getNombre().toUpperCase()));
                }
            }
        }

        // Refrescar los textos del Panel de Estadísticas Lateral en tiempo real
        actualizarLabelsEstadisticas();

        // 3. Cierre y Conclusión del Partido (Minuto 90)
        if (minutoActual >= 90) {
            timerMotor.stop();
            lblEstadoPartido.setText("FINISHED");
            txtCronica.append(">> ¡FINAL DEL PARTIDO! El colegiado decreta el cierre definitivo del choque.\n");
            
            inyectarResultadosYAsignarPuntos();
        }
    }

    private void actualizarLabelsEstadisticas() {
        lblPosesionL.setText(posesionL + "%");
        lblPosesionV.setText(posesionV + "%");
        lblTirosL.setText(String.valueOf(tirosL));
        lblTirosV.setText(String.valueOf(tirosV));
        lblFaltasL.setText(String.valueOf(faltasL));
        lblFaltasV.setText(String.valueOf(faltasV));
    }

    /**
     * Aplica la lógica reglamentaria de asignación de puntos en las entidades del Modelo de datos.
     */
    private void inyectarResultadosYAsignarPuntos() {
        int puntosL = 0;
        int puntosV = 0;

        // Suma de goles en el histórico del equipo
        equipoLocal.setGolesFavor(equipoLocal.getGolesFavor() + golesL);
        equipoLocal.setGolesContra(equipoLocal.getGolesContra() + golesV);
        equipoVisitante.setGolesFavor(equipoVisitante.getGolesFavor() + golesV);
        equipoVisitante.setGolesContra(equipoVisitante.getGolesContra() + golesL);

        // Evaluación de victorias / empates
        if (golesL > golesV) {
            puntosL = 3;
            equipoLocal.setPartidosGanados(equipoLocal.getPartidosGanados() + 1);
            equipoVisitante.setPartidosPerdidos(equipoVisitante.getPartidosPerdidos() + 1);
            txtCronica.append(String.format(">> CONCLUSIÓN: +3 puntos adjudicados a %s.\n", equipoLocal.getNombre().toUpperCase()));
        } else if (golesL < golesV) {
            puntosV = 3;
            equipoVisitante.setPartidosGanados(equipoVisitante.getPartidosGanados() + 1);
            equipoLocal.setPartidosPerdidos(equipoLocal.getPartidosPerdidos() + 1);
            txtCronica.append(String.format(">> CONCLUSIÓN: +3 puntos adjudicados a %s.\n", equipoVisitante.getNombre().toUpperCase()));
        } else {
            puntosL = 1;
            puntosV = 1;
            equipoLocal.setPartidosEmpatados(equipoLocal.getPartidosEmpatados() + 1);
            equipoVisitante.setPartidosEmpatados(equipoVisitante.getPartidosEmpatados() + 1);
            txtCronica.append(">> CONCLUSIÓN: Empate táctico. Reparto equilibrado (+1 punto a cada club).\n");
        }

        equipoLocal.setPuntos(equipoLocal.getPuntos() + puntosL);
        equipoVisitante.setPuntos(equipoVisitante.getPuntos() + puntosV);

        equipoLocal.setPartidosJugados(equipoLocal.getPartidosJugados() + 1);
        equipoVisitante.setPartidosJugados(equipoVisitante.getPartidosJugados() + 1);

        // Habilitar el botón de salida del panel de forma segura
        btnContinuar.setEnabled(true);
        btnContinuar.setText("ACTUALIZAR Y VER LA CLASIFICACION");
        btnContinuar.setBackground(VERDE_CESPED);
        btnContinuar.setForeground(Color.BLACK);
    }

    // Accesor para que el Controlador acople el click del botón Continuar
    public JButton getBtnContinuar() {
        return btnContinuar;
    }
}