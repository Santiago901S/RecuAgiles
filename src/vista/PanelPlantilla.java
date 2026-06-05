package vista;

import modelo.Equipo;
import modelo.Jugador;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * =============================================================================
 * SUPREMA MANAGER 2026 - PLANTILLA MASTER CORE (V6.5 DEFINITIVE)
 * =============================================================================
 * Pizarra táctica interactiva con cuadrícula geométrica 1-4-2-4 forzada.
 * - Garantiza la visualización de los 11 activos (Portero incluido).
 * - Integra motor de permutación táctica interna (Swap de Titular/Reserva).
 */
public class PanelPlantilla extends JPanel {

    // --- PALETA DE COLORES CYBERPUNK HIGH-FIDELITY ---
    private final Color COLOR_MAESTRO_FONDO  = new Color(11, 14, 23);
    private final Color COLOR_PANEL_RELEVOS  = new Color(19, 23, 36);
    private final Color COLOR_TABLA_FONDO    = new Color(16, 20, 30);
    private final Color VERDE_NEON_INTERFAZ = new Color(57, 255, 20);
    private final Color CIAN_NEON_ELECTRICO = new Color(0, 240, 255);
    private final Color AMARILLO_CYBERPUNK  = new Color(255, 230, 0);
    private final Color BLANCO_TITULO        = new Color(240, 244, 255);
    private final Color ROJO_ALERTA          = new Color(255, 50, 70);
    private final Color GRIS_CONTORNO        = new Color(35, 45, 65);
    private final Color TEXTO_DESACTIVADO    = new Color(110, 120, 140);

    // --- COMPONENTES VISUALES ---
    private JTable tablaFutbolistas;
    private DefaultTableModel modeloTabla;
    private JLabel lblNombreEquipo;
    private JLabel lblPresupuesto;
    private JLabel lblEstadio;
    private JLabel lblMediaGlobal;
    
    private PizarraTactica pizarraTactica;
    private JLabel lblFichaNombre;
    private JLabel lblFichaPosicion;
    private JLabel lblFichaMedia;

    private KPIBadge badgeTitulares;
    private KPIBadge badgeSuplentes;
    private KPIBadge badgeValorTotal;

    private JButton btnVenderJugador;
    private JButton btnPermutarSustitucion;
    private JButton btnFiltrarTodos;
    private JButton btnFiltrarPor;
    private JButton btnFiltrarDef;
    private JButton btnFiltrarMed;
    private JButton btnFiltrarDel;

    private PanelMenu panelMenuLateral; 
    private Equipo equipoDatosMemoria;
    private String filtroActual = "TODOS";
    private List<JButton> coleccionBotonesFiltro;

    // Punteros de selección de swap bidireccional
    private Jugador jugadorSeleccionadoTabla = null;

    public PanelPlantilla() {
        setName("PLANTILLA");
        setLayout(new BorderLayout(15, 15));
        setBackground(COLOR_MAESTRO_FONDO);
        setBorder(new EmptyBorder(15, 15, 15, 15));
        
        this.coleccionBotonesFiltro = new ArrayList<>();
        this.panelMenuLateral = new PanelMenu();
        add(panelMenuLateral, BorderLayout.WEST);

        JPanel panelCentralContenedor = new JPanel(new BorderLayout(15, 15));
        panelCentralContenedor.setOpaque(false);

        construirCabeceraDespacho(panelCentralContenedor);
        construirAreaCentralDeDatos(panelCentralContenedor);
        construirSubPanelInferiorLogs(panelCentralContenedor);

        add(panelCentralContenedor, BorderLayout.CENTER);
    }

    private void construirCabeceraDespacho(JPanel contenedor) {
        JPanel panelCabeceraMaestra = new JPanel(new BorderLayout(10, 10));
        panelCabeceraMaestra.setOpaque(false);
        panelCabeceraMaestra.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(GRIS_CONTORNO, 1, true), new EmptyBorder(12, 18, 12, 18)
        ));

        JPanel panelIzquierdo = new JPanel(new GridLayout(2, 1, 2, 2));
        panelIzquierdo.setOpaque(false);
        
        lblNombreEquipo = new JLabel("DESPACHO INTERNO: CONECTANDO DATA-CORE...", SwingConstants.LEFT);
        lblNombreEquipo.setFont(new Font("Segoe UI Black", Font.BOLD, 18));
        lblNombreEquipo.setForeground(BLANCO_TITULO);
        
        lblEstadio = new JLabel("⚡ SISTEMA DE ALINEACIONES OPERATIVO (4-2-4 TACTICAL MODE)", SwingConstants.LEFT);
        lblEstadio.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 12));
        lblEstadio.setForeground(CIAN_NEON_ELECTRICO);
        
        panelIzquierdo.add(lblNombreEquipo);
        panelIzquierdo.add(lblEstadio);

        JPanel panelDerechoMando = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 5));
        panelDerechoMando.setOpaque(false);

        JPanel panelTextosMeta = new JPanel(new GridLayout(2, 1, 2, 2));
        panelTextosMeta.setOpaque(false);
        
        lblPresupuesto = new JLabel("0,00 €", SwingConstants.RIGHT);
        lblPresupuesto.setFont(new Font("Consolas", Font.BOLD, 20));
        lblPresupuesto.setForeground(VERDE_NEON_INTERFAZ);

        lblMediaGlobal = new JLabel("RENDIMIENTO: -- OVR", SwingConstants.RIGHT);
        lblMediaGlobal.setFont(new Font("Segoe UI Black", Font.BOLD, 11));
        lblMediaGlobal.setForeground(AMARILLO_CYBERPUNK);
        
        panelTextosMeta.add(lblPresupuesto);
        panelTextosMeta.add(lblMediaGlobal);
        
        panelDerechoMando.add(panelTextosMeta);
        panelCabeceraMaestra.add(panelIzquierdo, BorderLayout.WEST);
        panelCabeceraMaestra.add(panelDerechoMando, BorderLayout.EAST);
        
        contenedor.add(panelCabeceraMaestra, BorderLayout.NORTH);
    }

    private void construirAreaCentralDeDatos(JPanel contenedor) {
        JPanel panelCuerpo = new JPanel(new BorderLayout(15, 15));
        panelCuerpo.setOpaque(false);

        JPanel panelTopCuerpo = new JPanel(new BorderLayout(10, 10));
        panelTopCuerpo.setOpaque(false);

        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        panelFiltros.setOpaque(false);
        
        btnFiltrarTodos = crearBotonPremiumFiltro("MOSTRAR TODO", "TODOS", CIAN_NEON_ELECTRICO);
        btnFiltrarPor   = crearBotonPremiumFiltro("PORTEROS", "POR", AMARILLO_CYBERPUNK);
        btnFiltrarDef   = crearBotonPremiumFiltro("DEFENSAS", "DEF", BLANCO_TITULO);
        btnFiltrarMed   = crearBotonPremiumFiltro("MEDIOS", "MED", BLANCO_TITULO);
        btnFiltrarDel   = crearBotonPremiumFiltro("DELANTEROS", "DEL", ROJO_ALERTA);

        panelFiltros.add(btnFiltrarTodos);
        panelFiltros.add(btnFiltrarPor);
        panelFiltros.add(btnFiltrarDef);
        panelFiltros.add(btnFiltrarMed);
        panelFiltros.add(btnFiltrarDel);
        panelTopCuerpo.add(panelFiltros, BorderLayout.WEST);

        JPanel panelKPIs = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        panelKPIs.setOpaque(false);
        badgeTitulares = new KPIBadge("TITULARES", "0/11", CIAN_NEON_ELECTRICO);
        badgeSuplentes = new KPIBadge("RESERVAS", "0/5", AMARILLO_CYBERPUNK);
        badgeValorTotal = new KPIBadge("VALOR TOTAL", "0 €", VERDE_NEON_INTERFAZ);
        panelKPIs.add(badgeTitulares);
        panelKPIs.add(badgeSuplentes);
        panelKPIs.add(badgeValorTotal);
        
        panelTopCuerpo.add(panelKPIs, BorderLayout.EAST);
        panelCuerpo.add(panelTopCuerpo, BorderLayout.NORTH);

        JPanel panelDivisionEstructural = new JPanel(new GridLayout(1, 2, 15, 0));
        panelDivisionEstructural.setOpaque(false);

        // Configuración de Tabla
        String[] columnas = {"FUTBOLISTA", "POS", "EDAD", "OVR", "VALOR NETO", "ESTADO"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };

        tablaFutbolistas = new JTable(modeloTabla);
        tablaFutbolistas.setBackground(COLOR_TABLA_FONDO);
        tablaFutbolistas.setForeground(BLANCO_TITULO);
        tablaFutbolistas.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 12));
        tablaFutbolistas.setRowHeight(40);
        tablaFutbolistas.setGridColor(new Color(28, 36, 54));
        tablaFutbolistas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaFutbolistas.setShowVerticalLines(false);

        JTableHeader header = tablaFutbolistas.getTableHeader();
        header.setBackground(COLOR_PANEL_RELEVOS);
        header.setForeground(CIAN_NEON_ELECTRICO);
        header.setFont(new Font("Segoe UI Black", Font.BOLD, 11));
        header.setPreferredSize(new Dimension(0, 35));

        tablaFutbolistas.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object val, boolean isSel, boolean hasF, int r, int c) {
                Component cell = super.getTableCellRendererComponent(table, val, isSel, hasF, r, c);
                cell.setBackground(isSel ? new Color(34, 46, 74) : COLOR_TABLA_FONDO);
                if (c == 1 || c == 3 || c == 5) {
                    setHorizontalAlignment(SwingConstants.CENTER);
                    if (c == 1) cell.setForeground(CIAN_NEON_ELECTRICO);
                    if (c == 3) { cell.setForeground(VERDE_NEON_INTERFAZ); setFont(new Font("Consolas", Font.BOLD, 13)); }
                    if (c == 5) {
                        String est = val.toString();
                        cell.setForeground(est.equals("TITULAR") ? AMARILLO_CYBERPUNK : TEXTO_DESACTIVADO);
                    }
                } else if (c == 4) {
                    setHorizontalAlignment(SwingConstants.RIGHT);
                    cell.setForeground(Color.WHITE);
                } else {
                    setHorizontalAlignment(SwingConstants.LEFT);
                }
                return cell;
            }
        });

        JScrollPane scroll = new JScrollPane(tablaFutbolistas);
        scroll.setBorder(new LineBorder(GRIS_CONTORNO, 1, true));
        scroll.getViewport().setBackground(COLOR_TABLA_FONDO);
        panelDivisionEstructural.add(scroll);

        // Módulo de Pizarra Táctica Campo
        JPanel panelModuloDerecho = new JPanel(new BorderLayout(0, 10));
        panelModuloDerecho.setOpaque(false);

        pizarraTactica = new PizarraTactica();
        panelModuloDerecho.add(pizarraTactica, BorderLayout.CENTER);

        JPanel panelDockFicha = new JPanel(new BorderLayout(10, 5));
        panelDockFicha.setBackground(COLOR_PANEL_RELEVOS);
        panelDockFicha.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(GRIS_CONTORNO, 1, true), new EmptyBorder(10, 12, 10, 12)
        ));

        JPanel panelTextosFicha = new JPanel(new GridLayout(3, 1, 2, 2));
        panelTextosFicha.setOpaque(false);
        lblFichaNombre = new JLabel("SELECCIONE ELEMENTOS PARA INTERCAMBIO TÁCTICO", SwingConstants.LEFT);
        lblFichaNombre.setFont(new Font("Segoe UI Black", Font.BOLD, 11));
        lblFichaNombre.setForeground(Color.WHITE);
        lblFichaPosicion = new JLabel("TABLA: NINGUNO SELECCIONADO", SwingConstants.LEFT);
        lblFichaPosicion.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 11));
        lblFichaPosicion.setForeground(CIAN_NEON_ELECTRICO);
        lblFichaMedia = new JLabel("CAMPO: NINGUNO SELECCIONADO", SwingConstants.LEFT);
        lblFichaMedia.setFont(new Font("Segoe UI Semibold", Font.BOLD, 11));
        lblFichaMedia.setForeground(VERDE_NEON_INTERFAZ);
        
        panelTextosFicha.add(lblFichaNombre);
        panelTextosFicha.add(lblFichaPosicion);
        panelTextosFicha.add(lblFichaMedia);
        panelDockFicha.add(panelTextosFicha, BorderLayout.CENTER);

        JPanel panelAccionesDock = new JPanel(new GridLayout(2, 1, 0, 5));
        panelAccionesDock.setOpaque(false);

        btnPermutarSustitucion = new JButton("PERMUTAR SUSTITUCIÓN") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create(); g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground()); g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8); g2.dispose();
                super.paintComponent(g);
            }
        };
        btnPermutarSustitucion.setFont(new Font("Segoe UI Black", Font.BOLD, 10));
        btnPermutarSustitucion.setBackground(COLOR_MAESTRO_FONDO);
        btnPermutarSustitucion.setForeground(TEXTO_DESACTIVADO);
        btnPermutarSustitucion.setContentAreaFilled(false);
        btnPermutarSustitucion.setFocusPainted(false);
        btnPermutarSustitucion.setEnabled(false);
        btnPermutarSustitucion.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // --- SOLUCIÓN INTEGRADA: Lógica de Intercambio Táctico ---
        btnPermutarSustitucion.addActionListener(e -> {
            Jugador jCampo = pizarraTactica.getJugadorSeleccionadoCampo();
            Jugador jTabla = jugadorSeleccionadoTabla;
            
            if (jCampo != null && jTabla != null && (jCampo.isEsTitular() != jTabla.isEsTitular())) {
                // Hacemos el Swap directo de Titular a Reserva y viceversa
                boolean estadoTemp = jCampo.isEsTitular();
                jCampo.setEsTitular(jTabla.isEsTitular());
                jTabla.setEsTitular(estadoTemp);
                
                // Limpiar la visualización
                pizarraTactica.limpiarSeleccion();
                jugadorSeleccionadoTabla = null;
                tablaFutbolistas.clearSelection();
                
                // Refrescar el panel
                actualizarDatosEquipo(equipoDatosMemoria);
            }
        });

        btnVenderJugador = new JButton("RESCINDIR CONTRATO") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create(); g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground()); g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8); g2.dispose();
                super.paintComponent(g);
            }
        };
        btnVenderJugador.setFont(new Font("Segoe UI Black", Font.BOLD, 10));
        btnVenderJugador.setBackground(ROJO_ALERTA);
        btnVenderJugador.setForeground(Color.WHITE);
        btnVenderJugador.setContentAreaFilled(false);
        btnVenderJugador.setFocusPainted(false);

        panelAccionesDock.add(btnPermutarSustitucion);
        panelAccionesDock.add(btnVenderJugador);
        panelDockFicha.add(panelAccionesDock, BorderLayout.EAST);

        panelModuloDerecho.add(panelDockFicha, BorderLayout.SOUTH);
        panelDivisionEstructural.add(panelModuloDerecho);

        panelCuerpo.add(panelDivisionEstructural, BorderLayout.CENTER);
        configurarEventosFiltros();

        tablaFutbolistas.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                actualizarDetalleDesdeTabla();
            }
        });

        contenedor.add(panelCuerpo, BorderLayout.CENTER);
    }

    private void construirSubPanelInferiorLogs(JPanel contenedor) {
        JPanel panelFooter = new JPanel(new BorderLayout());
        panelFooter.setOpaque(false);
        panelFooter.setBorder(new EmptyBorder(5, 5, 0, 5));
        
        JLabel lblInfo = new JLabel("📡 TRASPASOS LAB: Selecciona un Reserva en la tabla y un Titular en el campo para habilitar la permutación.");
        lblInfo.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblInfo.setForeground(TEXTO_DESACTIVADO);
        
        panelFooter.add(lblInfo, BorderLayout.WEST);
        contenedor.add(panelFooter, BorderLayout.SOUTH);
    }

    // =========================================================================
    // LÓGICA DE ACTUALIZACIÓN Y OBTENCIÓN DE DATOS
    // =========================================================================

    public void actualizarDatosEquipo(Equipo equipo) {
        if (equipo == null) return;
        this.equipoDatosMemoria = equipo;
        
        lblNombreEquipo.setText("DESPACHO DE DIRECCIÓN: " + equipo.getNombre().toUpperCase());
        lblEstadio.setText("🏟️ SEDE DEPORTIVA: " + equipo.getEstadio().toUpperCase());
        lblPresupuesto.setText(String.format("%,.2f €", equipo.getPresupuesto()));
        lblMediaGlobal.setText("RENDIMIENTO: " + equipo.getMediaEquipo() + " OVR");

        modeloTabla.setRowCount(0);
        List<Jugador> listaCompleta = equipo.getPlantilla();
        
        if (listaCompleta != null) {
            long titularesCount = listaCompleta.stream().filter(Jugador::isEsTitular).count();
            long suplentesCount = listaCompleta.stream().filter(j -> !j.isEsTitular()).count();
            double valorTotalPlantel = listaCompleta.stream().mapToDouble(Jugador::getPrecioMercado).sum();

            badgeTitulares.setValor(titularesCount + " / 11");
            badgeSuplentes.setValor(suplentesCount + " / 5");
            badgeValorTotal.setValor(String.format("%,.0f €", valorTotalPlantel));

            List<Jugador> listaFiltrada = listaCompleta.stream()
                .filter(j -> filtroActual.equals("TODOS") || j.getPosicion().equalsIgnoreCase(filtroActual))
                .collect(Collectors.toList());

            for (Jugador j : listaFiltrada) {
                modeloTabla.addRow(new Object[]{
                    j.getNombre().toUpperCase(), j.getPosicion().toUpperCase(), j.getEdad() + " AÑOS",
                    j.getMedia() + " OVR", String.format("%,.0f €", j.getPrecioMercado()),
                    j.isEsTitular() ? "TITULAR" : "RESERVA"
                });
            }
            
            pizarraTactica.cargarTitulares(listaCompleta.stream().filter(Jugador::isEsTitular).collect(Collectors.toList()));
        }
        evaluarEstadoBotonPermutacion();
    }

    private void actualizarDetalleDesdeTabla() {
        int fila = tablaFutbolistas.getSelectedRow();
        if (fila == -1 || equipoDatosMemoria == null) {
            jugadorSeleccionadoTabla = null;
            lblFichaPosicion.setText("TABLA: NINGUNO SELECCIONADO");
            evaluarEstadoBotonPermutacion();
            return;
        }

        String nombre = (String) modeloTabla.getValueAt(fila, 0);
        Jugador j = equipoDatosMemoria.getPlantilla().stream()
                .filter(p -> p.getNombre().equalsIgnoreCase(nombre)).findFirst().orElse(null);

        if (j != null) {
            jugadorSeleccionadoTabla = j;
            lblFichaPosicion.setText("TABLA: " + j.getNombre().toUpperCase() + " [" + j.getPosicion() + " - " + (j.isEsTitular() ? "TITULAR" : "RESERVA") + "]");
        }
        evaluarEstadoBotonPermutacion();
    }

    private void evaluarEstadoBotonPermutacion() {
        Jugador jugadorCampo = pizarraTactica.getJugadorSeleccionadoCampo();
        
        if (jugadorCampo != null && jugadorSeleccionadoTabla != null && (jugadorCampo.isEsTitular() != jugadorSeleccionadoTabla.isEsTitular())) {
            btnPermutarSustitucion.setEnabled(true);
            btnPermutarSustitucion.setBackground(VERDE_NEON_INTERFAZ);
            btnPermutarSustitucion.setForeground(Color.BLACK);
            lblFichaNombre.setText("SISTEMA DE INTERCAMBIO DE ACTIVOS CONFIGURADO");
        } else {
            btnPermutarSustitucion.setEnabled(false);
            btnPermutarSustitucion.setBackground(COLOR_MAESTRO_FONDO);
            btnPermutarSustitucion.setForeground(TEXTO_DESACTIVADO);
            lblFichaNombre.setText("SELECCIONE ELEMENTOS PARA INTERCAMBIO TÁCTICO");
        }
    }

    public Jugador getJugadorSeleccionadoParaVender() {
        if (jugadorSeleccionadoTabla != null) return jugadorSeleccionadoTabla;
        return pizarraTactica.getJugadorSeleccionadoCampo();
    }

    public PanelMenu getPanelMenu() { return this.panelMenuLateral; }
    public JButton getBtnVenderJugador() { return this.btnVenderJugador; }

    private void configurarEventosFiltros() {
        for (JButton btn : coleccionBotonesFiltro) {
            btn.addActionListener(e -> {
                this.filtroActual = btn.getActionCommand();
                for (JButton b : coleccionBotonesFiltro) {
                    b.setBackground(COLOR_PANEL_RELEVOS); b.setForeground(BLANCO_TITULO);
                }
                btn.setBackground(GRIS_CONTORNO); btn.setForeground(VERDE_NEON_INTERFAZ);
                actualizarDatosEquipo(equipoDatosMemoria);
            });
        }
    }

    private JButton crearBotonPremiumFiltro(String texto, String command, Color colorBorde) {
        JButton btn = new JButton(texto) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create(); g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground()); g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setColor(colorBorde.darker().darker()); g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8); g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setActionCommand(command); btn.setFont(new Font("Segoe UI Black", Font.BOLD, 10));
        btn.setBackground(COLOR_PANEL_RELEVOS); btn.setForeground(BLANCO_TITULO);
        btn.setContentAreaFilled(false); btn.setFocusPainted(false); btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR)); btn.setPreferredSize(new Dimension(105, 30));
        coleccionBotonesFiltro.add(btn);
        return btn;
    }

    // =========================================================================
    // PIZARRA URBANA (1-4-2-4 COORD-SYSTEM FIJO PARA DIBUJAR LOS 11 JUGADORES)
    // =========================================================================
    
    private class PizarraTactica extends JPanel {
        private final List<NodoGraficoJugador> nodosCampo = new ArrayList<>();
        private NodoGraficoJugador nodoSeleccionado = null;

        public PizarraTactica() {
            setBackground(COLOR_TABLA_FONDO);
            setBorder(new LineBorder(GRIS_CONTORNO, 1, true));
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    nodoSeleccionado = null;
                    for (NodoGraficoJugador nodo : nodosCampo) {
                        if (nodo.contienePunto(e.getPoint(), getWidth(), getHeight())) {
                            nodoSeleccionado = nodo;
                            break;
                        }
                    }
                    if (nodoSeleccionado != null) {
                        lblFichaMedia.setText("CAMPO: " + nodoSeleccionado.jugador.getNombre().toUpperCase() + " [" + nodoSeleccionado.jugador.getMedia() + " OVR]");
                    } else {
                        lblFichaMedia.setText("CAMPO: NINGUNO SELECCIONADO");
                    }
                    evaluarEstadoBotonPermutacion();
                    repaint();
                }
            });
        }

        public void limpiarSeleccion() {
            this.nodoSeleccionado = null;
            lblFichaMedia.setText("CAMPO: NINGUNO SELECCIONADO");
            repaint();
        }

        public void cargarTitulares(List<Jugador> titulares) {
            nodosCampo.clear();
            nodoSeleccionado = null;
            
            int w = 360, h = 280; // Dimensiones virtuales de cálculo
            
            // 1. ORDENAMOS PARA QUE EL PORTERO SIEMPRE SE DIBUJE EL PRIMERO Y LOS DELANTEROS AL FINAL
            List<Jugador> ordenados = new ArrayList<>(titulares);
            ordenados.sort((j1, j2) -> Integer.compare(pesoPosicional(j1.getPosicion()), pesoPosicional(j2.getPosicion())));

            // 2. MATRIZ DE COORDENADAS FIJAS (1-4-2-4) Evita que se solapen o falte alguno
            double[][] layoutCoordenadas = {
                {0.12, 0.50}, // 0: PORTERO (Dentro del área izquierda)
                {0.28, 0.18}, {0.28, 0.38}, {0.28, 0.62}, {0.28, 0.82}, // 1-4: DEFENSAS
                {0.52, 0.35}, {0.52, 0.65}, // 5-6: MEDIOS
                {0.78, 0.15}, {0.78, 0.38}, {0.78, 0.62}, {0.78, 0.85}  // 7-10: DELANTEROS
            };

            int limiteMaximo = Math.min(11, ordenados.size());
            for (int i = 0; i < limiteMaximo; i++) {
                Jugador j = ordenados.get(i);
                int px = (int) (layoutCoordenadas[i][0] * w);
                int py = (int) (layoutCoordenadas[i][1] * h);
                nodosCampo.add(new NodoGraficoJugador(j, px, py));
            }
            repaint();
        }

        private int pesoPosicional(String pos) {
            if (pos == null) return 5;
            switch (pos.trim().toUpperCase()) {
                case "POR": return 1;
                case "DEF": return 2;
                case "MED": return 3;
                case "DEL": return 4;
                default: return 5;
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth(), h = getHeight();

            g2.setColor(new Color(13, 18, 30));
            g2.fillRect(0, 0, w, h);

            g2.setColor(new Color(36, 48, 72));
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawRect(15, 15, w - 30, h - 30); 
            g2.draw(new Line2D.Double(w / 2.0, 15, w / 2.0, h - 15)); 
            g2.draw(new Ellipse2D.Double((w / 2.0) - 40, (h / 2.0) - 40, 80, 80)); 
            g2.drawRect(15, (h / 2) - 60, 50, 120); 
            g2.drawRect(w - 65, (h / 2) - 60, 50, 120); 

            for (NodoGraficoJugador nodo : nodosCampo) {
                // Escalado proporcional de la coordenada base a la resolución actual de la pantalla
                int realX = (int) ((nodo.x / 360.0) * w);
                int realY = (int) ((nodo.y / 280.0) * h);

                boolean seleccionado = (nodoSeleccionado != null && nodoSeleccionado.jugador == nodo.jugador);
                
                if (seleccionado) {
                    g2.setColor(new Color(0, 240, 255, 45));
                    g2.fill(new Ellipse2D.Double(realX - 20, realY - 20, 40, 40));
                }

                g2.setColor(COLOR_PANEL_RELEVOS);
                g2.fill(new Ellipse2D.Double(realX - 14, realY - 14, 28, 28));

                g2.setStroke(new BasicStroke(2.5f));
                switch (nodo.jugador.getPosicion().trim().toUpperCase()) {
                    case "POR": g2.setColor(AMARILLO_CYBERPUNK); break;
                    case "DEF": g2.setColor(CIAN_NEON_ELECTRICO); break;
                    case "MED": g2.setColor(new Color(160, 100, 255)); break;
                    default: g2.setColor(ROJO_ALERTA); break;
                }
                
                if (seleccionado) g2.setColor(VERDE_NEON_INTERFAZ);
                g2.draw(new Ellipse2D.Double(realX - 14, realY - 14, 28, 28));

                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Consolas", Font.BOLD, 11));
                String ovrTxt = String.valueOf(nodo.jugador.getMedia());
                g2.drawString(ovrTxt, realX - g2.getFontMetrics().stringWidth(ovrTxt) / 2, realY + 4);

                g2.setFont(new Font("Segoe UI", Font.BOLD, 10));
                g2.setColor(BLANCO_TITULO);
                String ape = nodo.jugador.getNombre().split(" ")[0].toUpperCase();
                if(ape.length() > 8) ape = ape.substring(0, 7) + ".";
                g2.drawString(ape, realX - g2.getFontMetrics().stringWidth(ape) / 2, realY + 26);
            }
            g2.dispose();
        }

        public Jugador getJugadorSeleccionadoCampo() {
            return (nodoSeleccionado != null) ? nodoSeleccionado.jugador : null;
        }
    }

    private class NodoGraficoJugador {
        Jugador jugador;
        int x, y;
        public NodoGraficoJugador(Jugador j, int x, int y) { this.jugador = j; this.x = x; this.y = y; }
        
        public boolean contienePunto(Point p, int w, int h) {
            int rx = (int) ((x / 360.0) * w);
            int ry = (int) ((y / 280.0) * h);
            return p.distance(rx, ry) <= 18;
        }
    }

    private class KPIBadge extends JPanel {
        private JLabel lblValor;
        public KPIBadge(String titulo, String valorInicial, Color colorFoco) {
            setLayout(new GridLayout(2, 1, 0, 0)); setOpaque(false); setPreferredSize(new Dimension(95, 34));
            JLabel lblTitulo = new JLabel(titulo, SwingConstants.CENTER);
            lblTitulo.setFont(new Font("Segoe UI Black", Font.BOLD, 8)); lblTitulo.setForeground(Color.GRAY);
            lblValor = new JLabel(valorInicial, SwingConstants.CENTER);
            lblValor.setFont(new Font("Consolas", Font.BOLD, 11)); lblValor.setForeground(colorFoco);
            add(lblTitulo); add(lblValor);
            setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, GRIS_CONTORNO));
        }
        public void setValor(String nuevoValor) { lblValor.setText(nuevoValor); }
    }
}