package vista;

import modelo.Equipo;
import modelo.Liga;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/**
 * =============================================================================
 * SUPREMA MANAGER 2026 - PANEL DE CLASIFICACIÓN PROFESIONAL
 * =============================================================================
 * Interfaz UI de alto rendimiento encargada de la ordenación, renderizado
 * y control del estado deportivo del campeonato de liga de élite.
 * * Incorpora:
 * - Renderizadores cian/oro de alta fidelidad visual.
 * - Tarjetas dinámicas superiores con analíticas del torneo en tiempo real.
 * - Sistema de protección contra la corrupción e inflado de puntos.
 */
public class PanelClasificacion extends JPanel {

    // --- PALETA DE COLORES UI PREMIUM (DARK FUTURISTIC) ---
    private final Color COLOR_FONDO = new Color(11, 14, 22);
    private final Color COLOR_TARJETA = new Color(20, 25, 37);
    private final Color CYAN_NEON = new Color(0, 220, 220);
    private final Color ORO_LIDER = new Color(241, 196, 15);
    private final Color GRIS_SUBTITULO = new Color(140, 150, 170);
    private final Color BLANCO_TEXTO = new Color(245, 247, 250);
    private final Color ROJO_DESCENSO = new Color(231, 76, 60);

    // --- COMPONENTES DEL PANEL ---
    private JTable tablaClasificacion;
    private DefaultTableModel modeloTabla;
    private JLabel lblContadorJornada;
    private JLabel lblTotalJornadas;
    
    // --- TARJETAS DE MÉTRICAS (KPI CARDS) ---
    private JLabel lblLiderNombre;
    private JLabel lblMejorAtaqueNombre;
    private JLabel lblMejorDefensaNombre;
    private JLabel lblPresupuestoUsuario;

    // --- BOTONERA DE CONTROL DE FLUJO ---
    public JButton btnSiguientePartido;
    public JButton btnVolverMenu;

    /**
     * Construye e inicializa el entorno visual de la tabla general.
     */
    public PanelClasificacion() {
        this.setLayout(new BorderLayout(0, 20));
        this.setBackground(COLOR_FONDO);
        this.setBorder(new EmptyBorder(30, 40, 30, 40));

        inicializarCabeceraPanel();
        inicializarTarjetasMetricas();
        inicializarTablaDePosiciones();
        inicializarBotoneraInferior();
    }

    /**
     * Crea la marquesina superior que indica la jornada activa del torneo.
     */
    private void inicializarCabeceraPanel() {
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setOpaque(false);

        // Bloque Izquierdo: Título de la competición
        JPanel pnlTituloBloque = new JPanel(new GridLayout(2, 1, 0, 4));
        pnlTituloBloque.setOpaque(false);
        
        JLabel lblTitulo = new JLabel("CLASIFICACIÓN OFICIAL", SwingConstants.LEFT);
        lblTitulo.setFont(new Font("Segoe UI Black", Font.BOLD, 28));
        lblTitulo.setForeground(BLANCO_TEXTO);
        
        JLabel lblSubtitulo = new JLabel("SUPER LEAGUE DE ÉLITE - COMPETICIÓN REGULAR", SwingConstants.LEFT);
        lblSubtitulo.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 12));
        lblSubtitulo.setForeground(GRIS_SUBTITULO);
        
        pnlTituloBloque.add(lblTitulo);
        pnlTituloBloque.add(lblSubtitulo);

        // Bloque Derecho: Estado del calendario
        JPanel pnlJornadaBloque = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 10));
        pnlJornadaBloque.setOpaque(false);

        lblContadorJornada = new JLabel("MATCHDAY 1", SwingConstants.RIGHT);
        lblContadorJornada.setFont(new Font("Segoe UI Black", Font.BOLD, 18));
        lblContadorJornada.setForeground(CYAN_NEON);

        lblTotalJornadas = new JLabel(" OF 10", SwingConstants.RIGHT);
        lblTotalJornadas.setFont(new Font("Segoe UI Black", Font.BOLD, 14));
        lblTotalJornadas.setForeground(GRIS_SUBTITULO);

        pnlJornadaBloque.add(lblContadorJornada);
        pnlJornadaBloque.add(lblTotalJornadas);

        pnlHeader.add(pnlTituloBloque, BorderLayout.WEST);
        pnlHeader.add(pnlJornadaBloque, BorderLayout.EAST);
        add(pnlHeader, BorderLayout.NORTH);
    }

    /**
     * Construye las tarjetas analíticas de rendimiento para premiar al líder,
     * mejor ataque y mejor defensa del campeonato.
     */
    private void inicializarTarjetasMetricas() {
        JPanel pnlContenedorTarjetas = new JPanel(new GridLayout(1, 4, 15, 0));
        pnlContenedorTarjetas.setOpaque(false);

        lblLiderNombre = new JLabel("Calculando...", SwingConstants.CENTER);
        lblMejorAtaqueNombre = new JLabel("Calculando...", SwingConstants.CENTER);
        lblMejorDefensaNombre = new JLabel("Calculando...", SwingConstants.CENTER);
        lblPresupuestoUsuario = new JLabel("0,00 €", SwingConstants.CENTER);

        pnlContenedorTarjetas.add(crearTarjetaMilitar("LÍDER ACTUAL", lblLiderNombre, ORO_LIDER));
        pnlContenedorTarjetas.add(crearTarjetaMilitar("MÁXIMO GOLEADOR (CLUB)", lblMejorAtaqueNombre, CYAN_NEON));
        pnlContenedorTarjetas.add(crearTarjetaMilitar("MURO DEFENSIVO", lblMejorDefensaNombre, new Color(46, 204, 113)));
        pnlContenedorTarjetas.add(crearTarjetaMilitar("LIQUIDEZ MANAGER", lblPresupuestoUsuario, BLANCO_TEXTO));

        // Insertar en un contenedor intermedio para respetar el flujo vertical
        JPanel pnlEstructuraCentral = new JPanel(new BorderLayout(0, 20));
        pnlEstructuraCentral.setOpaque(false);
        pnlEstructuraCentral.add(pnlContenedorTarjetas, BorderLayout.NORTH);
        add(pnlEstructuraCentral, BorderLayout.CENTER);
    }

    /**
     * Factoría interna para generar módulos visuales de estadísticas (KPI Cards).
     */
    private JPanel crearTarjetaMilitar(String titulo, JLabel lblValor, Color colorTema) {
        JPanel tarjeta = new JPanel(new BorderLayout(0, 6));
        tarjeta.setBackground(COLOR_TARJETA);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(255, 255, 255, 8), 1, true),
                new EmptyBorder(12, 15, 12, 15)
        ));

        JLabel lblTit = new JLabel(titulo, SwingConstants.LEFT);
        lblTit.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lblTit.setForeground(GRIS_SUBTITULO);

        lblValor.setFont(new Font("Segoe UI Black", Font.BOLD, 15));
        lblValor.setForeground(colorTema);
        lblValor.setHorizontalAlignment(SwingConstants.LEFT);

        tarjeta.add(lblTit, BorderLayout.NORTH);
        tarjeta.add(lblValor, BorderLayout.CENTER);
        return tarjeta;
    }

    /**
     * Inicializa y formatea la JTable con estilos de alta fidelidad competitiva.
     */
    private void inicializarTablaDePosiciones() {
        String[] columnas = {"POS", "CLUB", "PTS", "PJ", "PG", "PE", "PP", "GF", "GC", "DG"};
        
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return (columnIndex == 1) ? String.class : Integer.class;
            }
        };

        tablaClasificacion = new JTable(modeloTabla);
        tablaClasificacion.setRowHeight(45);
        tablaClasificacion.setBackground(COLOR_TARJETA);
        tablaClasificacion.setForeground(BLANCO_TEXTO);
        tablaClasificacion.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
        tablaClasificacion.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaClasificacion.setShowVerticalLines(false);
        tablaClasificacion.setShowHorizontalLines(true);
        tablaClasificacion.setGridColor(new Color(255, 255, 255, 5));

        // Estilización rigurosa del Header de la tabla
        JTableHeader header = tablaClasificacion.getTableHeader();
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
        header.setBackground(new Color(16, 20, 30));
        header.setForeground(Color.LIGHT_GRAY);
        header.setFont(new Font("Segoe UI Black", Font.BOLD, 11));
        header.setReorderingAllowed(false);
        header.setResizingAllowed(false);

        // Inyección de renderizadores personalizados en las columnas
        configurarDimensionesYRenderizadores();

        JScrollPane scrollPane = new JScrollPane(tablaClasificacion);
        scrollPane.getViewport().setBackground(COLOR_FONDO);
        scrollPane.setBorder(new LineBorder(new Color(255, 255, 255, 10), 1));
        
        // Recuperar el panel central integrado de las tarjetas para incrustar la tabla debajo
        JPanel pnlCentral = (JPanel) ((BorderLayout) getLayout()).getLayoutComponent(BorderLayout.CENTER);
        pnlCentral.add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Distribuye los anchos proporcionales de las celdas y mapea los estilos internos.
     */
    private void configurarDimensionesYRenderizadores() {
        int[] anchos = {55, 240, 65, 55, 55, 55, 55, 55, 55, 65};
        ClasificacionCellRenderer renderizadorMaestro = new ClasificacionCellRenderer();

        for (int i = 0; i < tablaClasificacion.getColumnCount(); i++) {
            tablaClasificacion.getColumnModel().getColumn(i).setPreferredWidth(anchos[i]);
            tablaClasificacion.getColumnModel().getColumn(i).setCellRenderer(renderizadorMaestro);
        }
    }

    /**
     * Crea los controladores de navegación inferiores para interactuar con la jornada.
     */
    private void inicializarBotoneraInferior() {
        JPanel pnlBotonera = new JPanel(new BorderLayout());
        pnlBotonera.setOpaque(false);

        btnVolverMenu = new JButton("AL DESPACHO DEL CLUB");
        configurarBotonEstandar(btnVolverMenu, new Color(40, 48, 68), BLANCO_TEXTO);

        btnSiguientePartido = new JButton("JUGAR SIGUIENTE JORNADA");
        configurarBotonEstandar(btnSiguientePartido, CYAN_NEON, Color.BLACK);

        pnlBotonera.add(btnVolverMenu, BorderLayout.WEST);
        pnlBotonera.add(btnSiguientePartido, BorderLayout.EAST);
        add(pnlBotonera, BorderLayout.SOUTH);
    }

    /**
     * Aplica estilos modernos, bordes y listeners de animaciones de hover a los botones.
     */
    private void configurarBotonEstandar(JButton boton, Color fondo, Color texto) {
        boton.setFont(new Font("Segoe UI Black", Font.BOLD, 13));
        boton.setBackground(fondo);
        boton.setForeground(texto);
        boton.setFocusPainted(false);
        boton.setBorder(new EmptyBorder(14, 28, 14, 28));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        boton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                boton.setBackground(fondo.brighter());
            }
            @Override
            public void mouseExited(MouseEvent e) {
                boton.setBackground(fondo);
            }
        });
    }

    /**
     * MÉTODO CENTRAL DE COORDINACIÓN DE DATOS (ANTI-DUPLICACIÓN)
     * Toma los datos puros del modelo de la Liga, ejecuta una ordenación atómica por criterios 
     * oficiales de desempate y actualiza de forma segura todos los componentes visuales de la UI.
     * * @param liga Objeto del modelo con el estado real e incorrupto de la simulación.
     * @param nombreEquipoUsuario Nombre del club que maneja el usuario para destacar su balance de caja.
     */
    public void sincronizarYRefrescarDatos(Liga liga, String nombreEquipoUsuario) {
        // 1. Limpieza absoluta preventiva de filas
        modeloTabla.setRowCount(0);

        // 2. Actualización de contadores del calendario
        lblContadorJornada.setText("MATCHDAY " + liga.getNumeroJornadaActual());
        lblTotalJornadas.setText(" OF 10");

        // 3. Extracción de la lista ordenada de forma segura y unificada mediante el método de clasificación del modelo
        List<Equipo> ordenCompetitiva = liga.getClasificacion();

        // 4. Extracción de Analíticas para las tarjetas KPI superiores
        if (!ordenCompetitiva.isEmpty()) {
            lblLiderNombre.setText(ordenCompetitiva.get(0).getNombre().toUpperCase());
            
            Equipo mejorAtaque = ordenCompetitiva.stream().max(Comparator.comparingInt(Equipo::getGolesFavor)).orElse(ordenCompetitiva.get(0));
            lblMejorAtaqueNombre.setText(mejorAtaque.getNombre().toUpperCase() + " (" + mejorAtaque.getGolesFavor() + " GF)");

            Equipo mejorDefensa = ordenCompetitiva.stream().min(Comparator.comparingInt(Equipo::getGolesContra)).orElse(ordenCompetitiva.get(0));
            lblMejorDefensaNombre.setText(mejorDefensa.getNombre().toUpperCase() + " (" + mejorDefensa.getGolesContra() + " GC)");
            
            // Buscar la liquidez económica del jugador humano
            for (Equipo eq : ordenCompetitiva) {
                if (eq.getNombre().equalsIgnoreCase(nombreEquipoUsuario)) {
                    NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(Locale.FRANCE);
                    lblPresupuestoUsuario.setText(formatoMoneda.format(eq.getPresupuesto()));
                    break;
                }
            }
        }

        // 5. Inyección ordenada de filas reales en la tabla
        int posicionIndice = 1;
        for (Equipo equipo : ordenCompetitiva) {
            int diferenciaGoles = equipo.getDiferenciaGoles();
            String strDiferencia = (diferenciaGoles > 0) ? "+" + diferenciaGoles : String.valueOf(diferenciaGoles);

            modeloTabla.addRow(new Object[]{
                    posicionIndice + "º",
                    equipo.getNombre().toUpperCase(),
                    equipo.getPuntos(),
                    equipo.getPartidosJugados(),
                    equipo.getPartidosGanados(),
                    equipo.getPartidosEmpatados(),
                    equipo.getPartidosPerdidos(),
                    equipo.getGolesFavor(),
                    equipo.getGolesContra(),
                    strDiferencia
            });
            posicionIndice++;
        }

        // 6. Si la liga llega a la última jornada, se desactiva el botón de juego
        if (liga.isFinalizada()) {
            btnSiguientePartido.setText("CAMPEONATO CONCLUIDO");
            btnSiguientePartido.setEnabled(false);
            btnSiguientePartido.setBackground(new Color(30, 35, 45));
            btnSiguientePartido.setForeground(GRIS_SUBTITULO);
        } else {
            btnSiguientePartido.setText("JUGAR SIGUIENTE JORNADA ⚡");
            btnSiguientePartido.setEnabled(true);
        }
    }

    // =========================================================================
    // RENDERIZADOR PERSONALIZADO PARA CELDAS PROFESIONALES (INTERNAL CLASS)
    // =========================================================================
    private class ClasificacionCellRenderer extends DefaultTableCellRenderer {
        
        @Override
        public Component getTableCellRendererComponent(JTable tabla, Object valor, boolean isSelected, boolean hasFocus, int fila, int columna) {
            Component c = super.getTableCellRendererComponent(tabla, valor, isSelected, hasFocus, fila, columna);
            
            // Efecto cebra para las filas impares
            if (fila % 2 == 0) {
                c.setBackground(COLOR_TARJETA);
            } else {
                c.setBackground(new Color(25, 31, 46));
            }

            // Alineación estricta de las celdas
            if (columna == 1) {
                setHorizontalAlignment(SwingConstants.LEFT);
                setBorder(new EmptyBorder(0, 15, 0, 0)); // Margen interno para el nombre del club
            } else {
                setHorizontalAlignment(SwingConstants.CENTER);
            }

            // --- LÓGICA TÁCTICA DE COLORACIÓN SEGÚN COLUMNA Y POSICIÓN ---
            if (fila == 0) {
                // El primer clasificado viste la tipografía oro del líder
                if (columna == 0 || columna == 1) {
                    c.setForeground(ORO_LIDER);
                    c.setFont(new Font("Segoe UI Black", Font.BOLD, 14));
                } else if (columna == 2) {
                    c.setForeground(CYAN_NEON);
                    c.setFont(new Font("Segoe UI Black", Font.BOLD, 14));
                } else {
                    c.setForeground(BLANCO_TEXTO);
                }
            } else if (fila == tabla.getRowCount() - 1) {
                // El colista de la Super League se destaca sutilmente en advertencia
                if (columna == 0 || columna == 1) {
                    c.setForeground(ROJO_DESCENSO);
                } else {
                    c.setForeground(Color.LIGHT_GRAY);
                }
            } else {
                // Configuración estándar para el resto de competidores de la zona media
                if (columna == 2) {
                    c.setForeground(CYAN_NEON); // Puntos destacados en cian siempre
                    c.setFont(new Font("Segoe UI Black", Font.BOLD, 14));
                } else if (columna == 9) {
                    // Dar color verde o rojo a la diferencia de goles según su signo
                    String valDG = valor.toString();
                    if (valDG.startsWith("+")) {
                        c.setForeground(new Color(46, 204, 113));
                    } else if (valDG.startsWith("-") && !valDG.equals("0")) {
                        c.setForeground(ROJO_DESCENSO);
                    } else {
                        c.setForeground(GRIS_SUBTITULO);
                    }
                } else {
                    c.setForeground(BLANCO_TEXTO);
                    c.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
                }
            }

            // Resaltado de selección para auditoría del mánager
            if (isSelected) {
                c.setBackground(new Color(38, 50, 74));
                c.setForeground(Color.WHITE);
            }

            return c;
        }
    }
}