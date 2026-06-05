package vista;

import modelo.Jugador;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * =============================================================================
 * SUPREMA MANAGER 2026 - ARCHITECTURAL TRANSFER DESK (MERCADO PREMIUM V6.0)
 * =============================================================================
 * Centro avanzado de operaciones financieras y adquisición de activos deportivos.
 * Incorpora un ruteador de datos reactivo, bloques modulares de analítica predictiva,
 * renderizado vectorial de barras de potencial y un inspector de perfil lateral.
 */
public class PanelMercado extends JPanel {

    // --- PALETA DE COLORES CROMÁTICA CYBERPUNK HIGH-FIDELITY ---
    private final Color COLOR_MAESTRO_FONDO  = new Color(11, 14, 23);
    private final Color COLOR_PANEL_RELEVOS  = new Color(19, 23, 36);
    private final Color COLOR_TABLA_FONDO    = new Color(16, 20, 30);
    private final Color COLOR_CARD_FONDO     = new Color(24, 30, 47);
    private final Color VERDE_NEON_INTERFAZ = new Color(57, 255, 20);
    private final Color CIAN_NEON_ELECTRICO = new Color(0, 240, 255);
    private final Color AMARILLO_CYBERPUNK  = new Color(255, 230, 0);
    private final Color ROJO_ALERTA_GLOW     = new Color(255, 52, 85);
    private final Color BLANCO_TITULO        = new Color(240, 244, 255);
    private final Color TEXTO_DESACTIVADO    = new Color(115, 130, 155);
    private final Color CONTORNO_ESTRUCTURAL = new Color(38, 48, 72);

    // --- COMPONENTES VARIABLES PÚBLICOS PARA EL CONTROLADOR ---
    private JTable tablaJugadores;
    private DefaultTableModel modeloTabla;
    private JTextField txtBuscador;
    private JComboBox<String> comboFiltro;
    private JLabel lblMensajeEstado;
    
    public JButton btnFichar;
    public JButton btnVolverPlantilla; 
    
    // --- COMPONENTES DEL DOSSIER ANALÍTICO LATERAL ---
    private JPanel panelDossierDerecho;
    private JLabel lblDossierNombre;
    private JLabel lblDossierPosicion;
    private JLabel lblDossierEdad;
    private JLabel lblDossierValoracion;
    private JLabel lblDossierPrecio;
    private PanelBarrasProgreso panelGraficoAtributos;
    private JPanel panelContenedorVacioDossier;

    // --- METRICAS DE CABECERA (KPI LABELS) ---
    private JLabel lblKpiTotalActivos;
    private JLabel lblKpiMediaOvr;
    private JLabel lblKpiJugadorFranquicia;

    // --- CACHÉ DE MEMORIA INTERNA ---
    private List<Jugador> listaMostrada;
    private List<Jugador> listaFiltradaActual;

    /**
     * Inicializa y orquesta el ecosistema modular de transferencias.
     */
    public PanelMercado() {
        setName("MERCADO");
        setLayout(new BorderLayout(16, 16));
        setBackground(COLOR_MAESTRO_FONDO);
        setBorder(new EmptyBorder(22, 22, 22, 22));
        
        this.listaMostrada = new ArrayList<>();
        this.listaFiltradaActual = new ArrayList<>();
        
        construirArquitecturaEstructural();
    }

    /**
     * Ensambla los bloques operativos del mercado analítico.
     */
    private void construirArquitecturaEstructural() {
        // 1. Bloque de Cabecera Integrado (KPIs + Filtros Operativos)
        JPanel panelEjeSuperior = new JPanel(new BorderLayout(0, 12));
        panelEjeSuperior.setOpaque(false);
        panelEjeSuperior.add(crearMarquesinaKpiKounters(), BorderLayout.NORTH);
        panelEjeSuperior.add(crearFiltrosCabecera(), BorderLayout.SOUTH);
        add(panelEjeSuperior, BorderLayout.NORTH);

        // 2. Núcleo de Contenido Dividido (Tabla de Activos + Dossier de Inspección)
        JPanel panelEjeCentral = new JPanel(new BorderLayout(16, 0));
        panelEjeCentral.setOpaque(false);
        panelEjeCentral.add(crearContenedorTablaCentral(), BorderLayout.CENTER);
        
        panelDossierDerecho = crearInspectorDossierLateral();
        panelEjeCentral.add(panelDossierDerecho, BorderLayout.EAST);
        add(panelEjeCentral, BorderLayout.CENTER);

        // 3. Consola de Operación e Informes Inferior
        add(crearConsolaOperacionesInferior(), BorderLayout.SOUTH);
    }

    /**
     * Construye la marquesina analítica de contadores KPI globales de mercado.
     */
    private JPanel crearMarquesinaKpiKounters() {
        JPanel marquesina = new JPanel(new GridLayout(1, 3, 16, 0));
        marquesina.setOpaque(false);
        marquesina.setPreferredSize(new Dimension(0, 65));

        lblKpiTotalActivos = new JLabel("0", SwingConstants.CENTER);
        marquesina.add(crearTarjetaKpi("TALENTOS DISPONIBLES", lblKpiTotalActivos, CIAN_NEON_ELECTRICO));

        lblKpiMediaOvr = new JLabel("0.0 OVR", SwingConstants.CENTER);
        marquesina.add(crearTarjetaKpi("POTENCIAL MEDIO MERCADO", lblKpiMediaOvr, VERDE_NEON_INTERFAZ));

        lblKpiJugadorFranquicia = new JLabel("NINGUNO", SwingConstants.CENTER);
        marquesina.add(crearTarjetaKpi("ACTIVO MÁS VALORADO", lblKpiJugadorFranquicia, AMARILLO_CYBERPUNK));

        return marquesina;
    }

    private JPanel crearTarjetaKpi(String titulo, JLabel lblValor, Color colorEfecto) {
        JPanel card = new JPanel(new BorderLayout(0, 2)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(COLOR_PANEL_RELEVOS);
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 10, 10));
                g2.setColor(CONTORNO_ESTRUCTURAL);
                g2.draw(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 10, 10));
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(10, 15, 10, 15));

        JLabel lblTitulo = new JLabel(titulo, SwingConstants.LEFT);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 9));
        lblTitulo.setForeground(TEXTO_DESACTIVADO);
        
        lblValor.setFont(new Font("Consolas", Font.BOLD, 15));
        lblValor.setForeground(colorEfecto);
        lblValor.setHorizontalAlignment(SwingConstants.LEFT);

        card.add(lblTitulo, BorderLayout.NORTH);
        card.add(lblValor, BorderLayout.CENTER);
        return card;
    }

    /**
     * Ensambla el panel superior de filtrado y herramientas de búsqueda predictiva.
     */
    private JPanel crearFiltrosCabecera() {
        JPanel panelFiltros = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(COLOR_PANEL_RELEVOS);
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 10, 10));
                g2.setColor(CONTORNO_ESTRUCTURAL);
                g2.draw(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 10, 10));
                g2.dispose();
            }
        };
        panelFiltros.setOpaque(false);
        panelFiltros.setBorder(new EmptyBorder(12, 20, 12, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 5, 0, 15);
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.anchor = GridBagConstraints.WEST;

        JLabel lblBuscar = new JLabel("BÚSQUEDA NOMINAL:");
        lblBuscar.setFont(new Font("Segoe UI Black", Font.BOLD, 10));
        lblBuscar.setForeground(CIAN_NEON_ELECTRICO);
        
        txtBuscador = new JTextField(18);
        txtBuscador.setBackground(COLOR_MAESTRO_FONDO);
        txtBuscador.setForeground(Color.WHITE);
        txtBuscador.setCaretColor(VERDE_NEON_INTERFAZ);
        txtBuscador.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 12));
        txtBuscador.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(CONTORNO_ESTRUCTURAL, 1, true),
                new EmptyBorder(4, 8, 4, 8)
        ));
        txtBuscador.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) { 
                refrescarTabla(); 
            }
        });

        JLabel lblFiltrar = new JLabel("DEMARCACIÓN TÁCTICA:");
        lblFiltrar.setFont(new Font("Segoe UI Black", Font.BOLD, 10));
        lblFiltrar.setForeground(AMARILLO_CYBERPUNK);

        comboFiltro = new JComboBox<>(new String[]{"TODOS", "POR", "DEF", "MED", "DEL"});
        comboFiltro.setBackground(COLOR_MAESTRO_FONDO);
        comboFiltro.setForeground(Color.WHITE);
        comboFiltro.setFont(new Font("Segoe UI Black", Font.PLAIN, 11));
        comboFiltro.setBorder(new LineBorder(CONTORNO_ESTRUCTURAL, 1, true));
        comboFiltro.setPreferredSize(new Dimension(110, 26));
        comboFiltro.addActionListener(e -> refrescarTabla());

        gbc.gridx = 0; panelFiltros.add(lblBuscar, gbc);
        gbc.gridx = 1; panelFiltros.add(txtBuscador, gbc);
        gbc.gridx = 2; panelFiltros.add(lblFiltrar, gbc);
        gbc.gridx = 3; gbc.weightx = 1.0; panelFiltros.add(comboFiltro, gbc);

        return panelFiltros;
    }

    /**
     * Construye la rejilla de datos central embebida en un contenedor scroll premium.
     */
    private JComponent crearContenedorTablaCentral() {
        String[] columnas = {"NOMBRE FUTBOLISTA", "POSICIÓN", "EDAD", "VALORACIÓN (OVR)", "PRECIO DE MERCADO"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override 
            public boolean isCellEditable(int row, int col) { return false; }
        };

        tablaJugadores = new JTable(modeloTabla);
        tablaJugadores.setBackground(COLOR_TABLA_FONDO);
        tablaJugadores.setForeground(Color.WHITE);
        tablaJugadores.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 13));
        tablaJugadores.setRowHeight(45);
        tablaJugadores.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaJugadores.setGridColor(new Color(26, 33, 48));
        tablaJugadores.setShowVerticalLines(false);

        JTableHeader header = tablaJugadores.getTableHeader();
        header.setBackground(COLOR_PANEL_RELEVOS);
        header.setForeground(CIAN_NEON_ELECTRICO);
        header.setFont(new Font("Segoe UI Black", Font.BOLD, 11));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, CONTORNO_ESTRUCTURAL));
        header.setPreferredSize(new Dimension(0, 36));

        // Interceptación de eventos de fila para actualización reactiva del perfil lateral
        tablaJugadores.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                actualizarFocoInspectorLateral();
            }
        });

        tablaJugadores.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object val, boolean isSel, boolean hasF, int r, int c) {
                Component cell = super.getTableCellRendererComponent(table, val, isSel, hasF, r, c);
                setBorder(noFocusBorder);
                
                if (isSel) {
                    cell.setBackground(new Color(30, 42, 68));
                    cell.setForeground(Color.WHITE);
                } else {
                    cell.setBackground(r % 2 == 0 ? COLOR_TABLA_FONDO : new Color(20, 25, 38));
                    cell.setForeground(BLANCO_TITULO);
                }

                if (c == 1) { // Badges para Posiciones
                    setHorizontalAlignment(SwingConstants.CENTER);
                    String pos = (String) val;
                    cell.setFont(new Font("Segoe UI Black", Font.BOLD, 11));
                    if ("POR".equals(pos)) cell.setForeground(AMARILLO_CYBERPUNK);
                    else if ("DEF".equals(pos)) cell.setForeground(CIAN_NEON_ELECTRICO);
                    else if ("MED".equals(pos)) cell.setForeground(new Color(160, 100, 255));
                    else cell.setForeground(ROJO_ALERTA_GLOW);
                } else if (c == 2) {
                    setHorizontalAlignment(SwingConstants.CENTER);
                } else if (c == 3) { // OVR
                    setHorizontalAlignment(SwingConstants.CENTER);
                    cell.setForeground(VERDE_NEON_INTERFAZ);
                    cell.setFont(new Font("Impact", Font.PLAIN, 15));
                } else if (c == 4) { // Precio
                    setHorizontalAlignment(SwingConstants.RIGHT);
                    cell.setForeground(AMARILLO_CYBERPUNK);
                    cell.setFont(new Font("Consolas", Font.BOLD, 13));
                } else {
                    setHorizontalAlignment(SwingConstants.LEFT);
                }
                return cell;
            }
        });

        JScrollPane scroll = new JScrollPane(tablaJugadores);
        scroll.setBorder(new LineBorder(CONTORNO_ESTRUCTURAL, 1, true));
        scroll.getViewport().setBackground(COLOR_TABLA_FONDO);
        
        // Inyección de ScrollBar UI Custom
        scroll.getVerticalScrollBar().setUI(new CyberScrollUI());
        scroll.getHorizontalScrollBar().setUI(new CyberScrollUI());
        scroll.getVerticalScrollBar().setPreferredSize(new Dimension(8, 0));
        
        return scroll;
    }

    /**
     * Ensambla el lateral interactivo de análisis y telemetría de contratos.
     */
    private JPanel crearInspectorDossierLateral() {
        JPanel contenedor = new JPanel(new CardLayout());
        contenedor.setPreferredSize(new Dimension(280, 0));
        contenedor.setOpaque(false);

        // Capa A: Estado por defecto cuando no hay ninguna celda activa
        panelContenedorVacioDossier = new JPanel(new GridBagLayout());
        panelContenedorVacioDossier.setBackground(COLOR_PANEL_RELEVOS);
        panelContenedorVacioDossier.setBorder(new LineBorder(CONTORNO_ESTRUCTURAL, 1, true));
        
        JLabel lblVacio = new JLabel("⏳ SELECCIONE UN ACTIVO");
        lblVacio.setFont(new Font("Segoe UI Black", Font.BOLD, 11));
        lblVacio.setForeground(TEXTO_DESACTIVADO);
        panelContenedorVacioDossier.add(lblVacio);

        // Capa B: Ficha técnica avanzada con renderizado vectorial
        JPanel panelInfoActiva = new JPanel();
        panelInfoActiva.setLayout(new BoxLayout(panelInfoActiva, BoxLayout.Y_AXIS));
        panelInfoActiva.setBackground(COLOR_CARD_FONDO);
        panelInfoActiva.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(CONTORNO_ESTRUCTURAL, 1, true),
                new EmptyBorder(18, 16, 18, 16)
        ));

        lblDossierNombre = new JLabel("JUGADOR GENÉRICO");
        lblDossierNombre.setFont(new Font("Segoe UI Black", Font.BOLD, 16));
        lblDossierNombre.setForeground(Color.WHITE);
        lblDossierNombre.setAlignmentX(Component.LEFT_ALIGNMENT);

        lblDossierPosicion = new JLabel("POSICIÓN: N/A");
        lblDossierPosicion.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblDossierPosicion.setForeground(CIAN_NEON_ELECTRICO);
        lblDossierPosicion.setAlignmentX(Component.LEFT_ALIGNMENT);

        lblDossierEdad = new JLabel("EDAD: 00 AÑOS");
        lblDossierEdad.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblDossierEdad.setForeground(BLANCO_TITULO);
        lblDossierEdad.setAlignmentX(Component.LEFT_ALIGNMENT);

        lblDossierValoracion = new JLabel("CALIFICACIÓN CORE: 00 OVR");
        lblDossierValoracion.setFont(new Font("Segoe UI Semibold", Font.BOLD, 12));
        lblDossierValoracion.setForeground(VERDE_NEON_INTERFAZ);
        lblDossierValoracion.setAlignmentX(Component.LEFT_ALIGNMENT);

        lblDossierPrecio = new JLabel("0 €");
        lblDossierPrecio.setFont(new Font("Consolas", Font.BOLD, 18));
        lblDossierPrecio.setForeground(AMARILLO_CYBERPUNK);
        lblDossierPrecio.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblDossierPrecio.setBorder(new EmptyBorder(6, 0, 15, 0));

        JSeparator s = new JSeparator();
        s.setMaximumSize(new Dimension(300, 1));
        s.setBackground(CONTORNO_ESTRUCTURAL);
        s.setForeground(CONTORNO_ESTRUCTURAL);
        s.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblAtributosTitulo = new JLabel("BALANCE DE CAPACIDADES:");
        lblAtributosTitulo.setFont(new Font("Segoe UI Black", Font.BOLD, 10));
        lblAtributosTitulo.setForeground(TEXTO_DESACTIVADO);
        lblAtributosTitulo.setBorder(new EmptyBorder(15, 0, 8, 0));
        lblAtributosTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        panelGraficoAtributos = new PanelBarrasProgreso();
        panelGraficoAtributos.setAlignmentX(Component.LEFT_ALIGNMENT);

        panelInfoActiva.add(lblDossierNombre);
        panelInfoActiva.add(Box.createVerticalStrut(2));
        panelInfoActiva.add(lblDossierPosicion);
        panelInfoActiva.add(Box.createVerticalStrut(10));
        panelInfoActiva.add(s);
        panelInfoActiva.add(Box.createVerticalStrut(10));
        panelInfoActiva.add(lblDossierEdad);
        panelInfoActiva.add(lblDossierValoracion);
        panelInfoActiva.add(Box.createVerticalStrut(8));
        panelInfoActiva.add(lblDossierPrecio);
        panelInfoActiva.add(lblAtributosTitulo);
        panelInfoActiva.add(panelGraficoAtributos);

        contenedor.add(panelContenedorVacioDossier, "VACIO");
        contenedor.add(panelInfoActiva, "ACTIVO");
        
        return contenedor;
    }

    /**
     * Orquesta la consola operativa inferior, empaquetando disparadores e hilos de log.
     */
    private JPanel crearConsolaOperacionesInferior() {
        JPanel panelInferior = new JPanel(new BorderLayout(10, 8));
        panelInferior.setOpaque(false);

        lblMensajeEstado = new JLabel(" ", SwingConstants.CENTER);
        lblMensajeEstado.setFont(new Font("Segoe UI Semibold", Font.BOLD, 12));
        lblMensajeEstado.setForeground(VERDE_NEON_INTERFAZ);
        panelInferior.add(lblMensajeEstado, BorderLayout.NORTH);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 0));
        panelBotones.setOpaque(false);

        btnVolverPlantilla = new JButton("« TRANSFERENCIAS") {
            @Override 
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create(); 
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground()); 
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 8, 8));
                g2.setColor(CONTORNO_ESTRUCTURAL); 
                g2.draw(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 8, 8)); 
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btnVolverPlantilla.setFont(new Font("Segoe UI Black", Font.BOLD, 12));
        btnVolverPlantilla.setForeground(Color.WHITE);
        btnVolverPlantilla.setBackground(COLOR_PANEL_RELEVOS);
        btnVolverPlantilla.setContentAreaFilled(false);
        btnVolverPlantilla.setFocusPainted(false);
        btnVolverPlantilla.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnVolverPlantilla.setBorder(new EmptyBorder(12, 22, 12, 22));

        btnFichar = new JButton("EJECUTAR CLÁUSULA DE FICHAJE »") {
            @Override 
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create(); 
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground()); 
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 8, 8));
                g2.setColor(getForeground().darker()); 
                g2.draw(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 8, 8)); 
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btnFichar.setFont(new Font("Segoe UI Black", Font.BOLD, 12));
        btnFichar.setForeground(Color.BLACK);
        btnFichar.setBackground(VERDE_NEON_INTERFAZ);
        btnFichar.setContentAreaFilled(false);
        btnFichar.setFocusPainted(false);
        btnFichar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnFichar.setBorder(new EmptyBorder(12, 26, 12, 26));

        btnFichar.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override 
            public void mouseEntered(java.awt.event.MouseEvent e) { 
                if(btnFichar.isEnabled()) {
                    btnFichar.setBackground(Color.WHITE); 
                    btnFichar.setForeground(Color.BLACK);
                }
            }
            @Override 
            public void mouseExited(java.awt.event.MouseEvent e) { 
                if(btnFichar.isEnabled()) {
                    btnFichar.setBackground(VERDE_NEON_INTERFAZ); 
                    btnFichar.setForeground(Color.BLACK);
                }
            }
        });

        panelBotones.add(btnVolverPlantilla);
        panelBotones.add(btnFichar);
        panelInferior.add(panelBotones, BorderLayout.CENTER);

        return panelInferior;
    }

    /**
     * Intercepta la fila seleccionada por el usuario y actualiza reactivamente el dossier.
     */
    private void actualizarFocoInspectorLateral() {
        Jugador seleccionado = getJugadorSeleccionado();
        CardLayout cl = (CardLayout) panelDossierDerecho.getLayout();
        
        if (seleccionado == null) {
            cl.show(panelDossierDerecho, "VACIO");
        } else {
            lblDossierNombre.setText(seleccionado.getNombre().toUpperCase());
            lblDossierPosicion.setText("DEMARCACIÓN: " + seleccionado.getPosicion());
            lblDossierEdad.setText("EDAD CORRIENTE: " + seleccionado.getEdad() + " AÑOS");
            lblDossierValoracion.setText("CALIFICACIÓN CORE: " + seleccionado.getMedia() + " OVR");
            lblDossierPrecio.setText(String.format("%,.0f €", seleccionado.getPrecioMercado()));
            
            // Re-calcular proporciones del vector gráfico según la media del crack
            panelGraficoAtributos.actualizarAtributos(seleccionado.getMedia());
            cl.show(panelDossierDerecho, "ACTIVO");
        }
    }

    /**
     * Sincroniza la lista interna del panel y actualiza los indicadores macro de la marquesina.
     */
    public void actualizarMercado(List<Jugador> jugadores) {
        this.listaMostrada = jugadores != null ? jugadores : new ArrayList<>();
        
        // Recalcular métricas macro para los KPIs
        if (!listaMostrada.isEmpty()) {
            lblKpiTotalActivos.setText(String.valueOf(listaMostrada.size()));
            
            double sumMedia = listaMostrada.stream().mapToInt(Jugador::getMedia).sum();
            lblKpiMediaOvr.setText(String.format("%.1f OVR", (sumMedia / listaMostrada.size())));
            
            Jugador topValorado = listaMostrada.stream()
                    .max((j1, j2) -> Double.compare(j1.getPrecioMercado(), j2.getPrecioMercado()))
                    .orElse(null);
            if (topValorado != null) {
                lblKpiJugadorFranquicia.setText(topValorado.getNombre().toUpperCase());
            }
        } else {
            lblKpiTotalActivos.setText("0");
            lblKpiMediaOvr.setText("0.0 OVR");
            lblKpiJugadorFranquicia.setText("NINGUNO");
        }

        refrescarTabla();
    }

    /**
     * Ejecuta el pipeline de filtrado por flujos y reconstruye las filas físicas de la tabla.
     */
    public void refrescarTabla() {
        if (listaMostrada == null) return;
        modeloTabla.setRowCount(0);
        
        String filtro = comboFiltro.getSelectedItem() != null ? (String) comboFiltro.getSelectedItem() : "TODOS";
        String busqueda = txtBuscador.getText().toLowerCase();

        listaFiltradaActual = listaMostrada.stream()
            .filter(j -> filtro.equals("TODOS") || j.getPosicion().equalsIgnoreCase(filtro))
            .filter(j -> j.getNombre().toLowerCase().contains(busqueda))
            .collect(Collectors.toList());

        for (Jugador j : listaFiltradaActual) {
            modeloTabla.addRow(new Object[]{
                j.getNombre().toUpperCase(), 
                j.getPosicion().toUpperCase(), 
                j.getEdad(),
                j.getMedia() + " OVR", 
                String.format("%,.0f €", j.getPrecioMercado())
            });
        }
        
        actualizarFocoInspectorLateral();
    }

    /**
     * Devuelve la entidad pura mapeada detrás de la fila activa de la UI.
     */
    public Jugador getJugadorSeleccionado() {
        int fila = tablaJugadores.getSelectedRow();
        if (fila == -1 || fila >= listaFiltradaActual.size()) return null;
        return listaFiltradaActual.get(fila);
    }

    /**
     * Imprime un informe de estado sobre la consola de monitorización inferior.
     */
    public void mostrarMensaje(String msg, boolean esExito) {
        lblMensajeEstado.setText("📡 SYSTEM MONITOR: " + msg.toUpperCase());
        lblMensajeEstado.setForeground(esExito ? VERDE_NEON_INTERFAZ : ROJO_ALERTA_GLOW);
        Timer t = new Timer(5000, e -> lblMensajeEstado.setText(" "));
        t.setRepeats(false);
        t.start();
    }

    // =========================================================================
    // SUBSISTEMAS INTERNOS DE COMPONENTES CUSTOM VECTORIALES
    // =========================================================================

    /**
     * Componente gráfico interno que realiza renderizado matricial de barras de progreso
     * para representar analíticamente los niveles del jugador.
     */
    private class PanelBarrasProgreso extends JComponent {
        private int valorRitmo = 50;
        private int valorTecnica = 50;
        private int valorFisico = 50;

        public PanelBarrasProgreso() {
            setPreferredSize(new Dimension(0, 150));
        }

        public void actualizarAtributos(int mediaBase) {
            // Simulación balanceada de desglose a partir de su media OVR
            this.valorRitmo = Math.min(99, Math.max(30, mediaBase + (int)(Math.random() * 12 - 6)));
            this.valorTecnica = Math.min(99, Math.max(30, mediaBase + (int)(Math.random() * 10 - 4)));
            this.valorFisico = Math.min(99, Math.max(30, mediaBase + (int)(Math.random() * 14 - 7)));
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int anchoDisponible = getWidth() - 10;
            
            // Barra 1: Capacidad Dinámica (Ritmo)
            dibujarBarraMetrica(g2, "VELOCIDAD / RITMO", valorRitmo, 10, anchoDisponible, CIAN_NEON_ELECTRICO);
            
            // Barra 2: Destreza Operativa (Técnica)
            dibujarBarraMetrica(g2, "DESTREZA / TÉCNICA", valorTecnica, 55, anchoDisponible, VERDE_NEON_INTERFAZ);
            
            // Barra 3: Resistencia Estructural (Físico)
            dibujarBarraMetrica(g2, "POTENCIA / FÍSICO", valorFisico, 100, anchoDisponible, AMARILLO_CYBERPUNK);

            g2.dispose();
        }

        private void dibujarBarraMetrica(Graphics2D g2, String etiqueta, int valor, int coordY, int anchoMax, Color colorCarga) {
            g2.setFont(new Font("Segoe UI", Font.BOLD, 9));
            g2.setColor(TEXTO_DESACTIVADO);
            g2.drawString(etiqueta + " [" + valor + "/99]", 2, coordY);

            // Fondo del riel de la barra
            g2.setColor(COLOR_MAESTRO_FONDO);
            g2.fill(new RoundRectangle2D.Double(0, coordY + 6, anchoMax, 6, 4, 4));

            // Relleno dinámico proporcional
            int anchoCalculado = (int) ((valor / 99.0) * anchoMax);
            g2.setColor(colorCarga);
            g2.fill(new RoundRectangle2D.Double(0, coordY + 6, anchoCalculado, 6, 4, 4));
        }
    }

    /**
     * Controlador estético UI personalizado para homogeneizar el aspecto de las barras de scroll.
     */
    private class CyberScrollUI extends BasicScrollBarUI {
        protected void configureScrollBarParameters() {
            super.configureScrollBarColors();
        }
        @Override
        protected JButton createDecreaseButton(int orientation) { return crearBotonVacio(); }
        @Override
        protected JButton createIncreaseButton(int orientation) { return crearBotonVacio(); }
        
        private JButton crearBotonVacio() {
            JButton b = new JButton();
            b.setPreferredSize(new Dimension(0, 0));
            return b;
        }

        @Override
        protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
            g.setColor(COLOR_TABLA_FONDO);
            g.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
        }

        @Override
        protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(CONTORNO_ESTRUCTURAL.brighter());
            g2.fillRoundRect(thumbBounds.x + 2, thumbBounds.y + 2, thumbBounds.width - 4, thumbBounds.height - 4, 6, 6);
            g2.dispose();
        }
    }
}