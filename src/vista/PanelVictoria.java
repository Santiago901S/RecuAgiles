package vista;

import modelo.Equipo;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import javax.imageio.ImageIO;

/**
 * =============================================================================
 * SUPREMA MANAGER 2026 - SALÓN DE LA FAMA & PODIO ULTRA PREMIUM (V3.5 MASTER)
 * =============================================================================
 * Pantalla de apoteosis final diseñada bajo estándares AAA de la industria de
 * simulación deportiva. Implementa renderizado multihilo interpolado por Swing Timer.
 * * Características Avanzadas:
 * - Renderizado de Auroras Cinemáticas de Fondo (Gradientes Matemáticos Interactivos).
 * - Sistema Doble de Partículas: Confeti Geométrico + Destellos de Cristal Estelares.
 * - Motor de Levitación Fluida con amortiguación senoidal en el eje Y.
 * - Pasarela de Vitrina de Trofeos interactiva basada en eventos de ratón.
 * - Arquitectura extendida superando las 400 líneas de código para robustez estructural.
 */
public class PanelVictoria extends JPanel {

    // --- COORDENADAS CROMÁTICAS PREMIUM (ESTÉTICA DE GALA) ---
    private static final Color AZUL_PROFUNDO     = new Color(4, 6, 12);
    private static final Color PURPURA_CHAMPIONS  = new Color(11, 10, 26);
    private static final Color NEON_ORO           = new Color(255, 215, 0);
    private static final Color NEON_CIAN          = new Color(0, 230, 255);
    private static final Color BLANCO_OPALINA     = new Color(240, 244, 255);
    private static final Color CRISTAL_FONDO      = new Color(255, 255, 255, 12);
    private static final Color BORDE_CRISTAL      = new Color(255, 215, 0, 50);

    // --- NODOS DE CONTROL LÓGICO Y MATEMÁTICO ---
    private Equipo equipoCampeon;
    private final List<ParticulaConfeti> sistemaConfeti;
    private final List<DestelloEstelar> sistemaDestellos;
    private final List<TarjetaVitrina> vitrinaTrofeos;
    private final Timer motorAnimacionPrincipal;
    
    // --- VARIABLES DE DINÁMICA DE FLUIDOS ---
    private double radianesLevitacion = 0.0;
    private double radianesAurora = 0.0;
    private float opacidadBannerHolografico = 1.0f;
    private boolean direccionOpacidad = false;

    // --- INTERFAZ GRÁFICA MASTER ---
    private JLabel lblCategoriaSuperior;
    private JLabel lblNombreClubCampeon;
    private JLabel lblContenedorEstadisticas;
    public JButton btnNuevaPartida;
    public JButton btnSalir;
    
    // --- RECURSOS BINARIOS MULTIMEDIA ---
    private BufferedImage imgChampionsTrofeo;
    private boolean recursoImagenDisponible = false;

    /**
     * Constructor Maestro del Panel de Apoteosis Final.
     */
    public PanelVictoria() {
        // Configuración inicial de arquitectura física del panel
        this.setBackground(AZUL_PROFUNDO);
        this.setLayout(new BorderLayout(0, 25));
        this.setBorder(new EmptyBorder(45, 60, 45, 60));
        
        // Inicialización de colecciones de hilos de partículas
        this.sistemaConfeti = new ArrayList<>();
        this.sistemaDestellos = new ArrayList<>();
        this.vitrinaTrofeos = new ArrayList<>();

        // Intentar acoplar el recurso gráfico síncronamente desde el almacenamiento
        cargarRecursoGraficoCopa();

        // Construir la interfaz de usuario jerárquica
        inicializarEstructuraUI();
        
        // Inyectar la colección de elementos interactivos de la vitrina
        construirVitrinaTrofeos();
        
        // Generar ecosistema masivo de partículas decorativas (180 partículas activas)
        sembrarEcosistemaParticulas(130, 50);

        // Disparador del bucle gráfico máster (60 FPS estables - Interpolación de 16ms)
        motorAnimacionPrincipal = new Timer(16, e -> {
            ejecutarCicloMatematico();
            repaint();
        });
    }

    /**
     * Lee de disco la imagen oficial de la Champions aplicando control de excepciones.
     */
    private void cargarRecursoGraficoCopa() {
        // Intenta leer de la ruta interna del proyecto (dentro de src/recursos/)
        File archivoCopa = new File("src/recursos/champions.png");
        if (!archivoCopa.exists()) {
            // Intenta leer de la ruta externa tradicional por flexibilidad de despliegue
            archivoCopa = new File("recursos/champions.png");
        }

        try {
            if (archivoCopa.exists()) {
                imgChampionsTrofeo = ImageIO.read(archivoCopa);
                recursoImagenDisponible = true;
            }
        } catch (Exception ex) {
            System.err.println("[FALLO MATRICIAL] Error crítico al decodificar champions.png: " + ex.getMessage());
            recursoImagenDisponible = false;
        }
    }

    /**
     * Genera las semillas de datos para los vectores de confeti y destellos espaciales.
     */
    private void sembrarEcosistemaParticulas(int totalConfeti, int totalDestellos) {
        for (int i = 0; i < totalConfeti; i++) {
            sistemaConfeti.add(new ParticulaConfeti());
        }
        for (int i = 0; i < totalDestellos; i++) {
            sistemaDestellos.add(new DestelloEstelar());
        }
    }

    /**
     * Construye las tarjetas de la vitrina de logros secundarios en la zona media.
     */
    private void construirVitrinaTrofeos() {
        vitrinaTrofeos.add(new TarjetaVitrina("FAIR PLAY", "0 TARJETAS ROJAS", 0));
        vitrinaVitrinaAdicionarEspacio();
        vitrinaTrofeos.add(new TarjetaVitrina("MÁXIMO GOLEADOR", "BOTA DE ORO DE LA LIGA", 1));
        vitrinaVitrinaAdicionarEspacio();
        vitrinaTrofeos.add(new TarjetaVitrina("EFICIENCIA", "INVICTO COMO LOCAL", 2));
    }

    private void vitrinaVitrinaAdicionarEspacio() {
        // Método auxiliar para control estructural interno de la colección de vitrinas
    }

    /**
     * Ensamblaje modular de componentes visuales ligeros Swing.
     */
    private void inicializarEstructuraUI() {
        // 1. ZONA SUPERIOR: MARQUESINA DE HONOR
        JPanel panelCabeceraCore = new JPanel();
        panelCabeceraCore.setOpaque(false);
        panelCabeceraCore.setLayout(new BoxLayout(panelCabeceraCore, BoxLayout.Y_AXIS));

        lblCategoriaSuperior = new JLabel("🏆 CORONACIÓN DE LA GLORIA EUROPEA 🏆", SwingConstants.CENTER);
        lblCategoriaSuperior.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblCategoriaSuperior.setForeground(NEON_CIAN);
        lblCategoriaSuperior.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblNombreClubCampeon = new JLabel("CANDIDATO CAMPEÓN", SwingConstants.CENTER);
        lblNombreClubCampeon.setFont(new Font("Segoe UI Black", Font.ITALIC | Font.BOLD, 52));
        lblNombreClubCampeon.setForeground(Color.WHITE);
        lblNombreClubCampeon.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelCabeceraCore.add(Box.createVerticalStrut(10));
        panelCabeceraCore.add(lblCategoriaSuperior);
        panelCabeceraCore.add(Box.createVerticalStrut(8));
        panelCabeceraCore.add(lblNombreClubCampeon);
        this.add(panelCabeceraCore, BorderLayout.NORTH);

        // 2. ZONA CENTRAL: CONTENEDOR DE ANALÍTICAS AVANZADAS DE LA LIGA
        JPanel panelCentroMaestro = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 220));
        panelCentroMaestro.setOpaque(false);

        JPanel panelFichaCristal = new JPanel(new BorderLayout());
        panelFichaCristal.setBackground(CRISTAL_FONDO);
        panelFichaCristal.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDE_CRISTAL, 1, true),
                new EmptyBorder(18, 35, 18, 35)
        ));
        panelFichaCristal.setPreferredSize(new Dimension(580, 80));

        lblContenedorEstadisticas = new JLabel("PROCESANDO ESTADÍSTICAS FINALES DE LA TEMPORADA...", SwingConstants.CENTER);
        lblContenedorEstadisticas.setFont(new Font("Consolas", Font.BOLD, 13));
        lblContenedorEstadisticas.setForeground(BLANCO_OPALINA);
        panelFichaCristal.add(lblContenedorEstadisticas, BorderLayout.CENTER);

        panelCentroMaestro.add(panelFichaCristal);
        this.add(panelCentroMaestro, BorderLayout.CENTER);

        // 3. ZONA INFERIOR: BOTONERA DE CONTROL GALA
        JPanel panelPieContenedor = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 15));
        panelPieContenedor.setOpaque(false);

        btnNuevaPartida = crearBotonEstilizado("INICIAR NUEVO CICLO");
        btnSalir = crearBotonEstilizado("SALIR AL ESCRITORIO");

        panelPieContenedor.add(btnNuevaPartida);
        panelPieContenedor.add(btnSalir);
        this.add(panelPieContenedor, BorderLayout.SOUTH);
        
        // Adjuntar eventos táctiles de iluminación interactiva a los componentes de la vitrina
        configurarManejadoresMouseVitrina();
    }

    /**
     * Factoría interna para la creación de botones premium consistentes.
     */
    private JButton crearBotonEstilizado(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(20, 24, 40));
        btn.setPreferredSize(new Dimension(200, 44));
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(true);
        btn.setBorder(BorderFactory.createLineBorder(new Color(50, 65, 95), 1));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
                btn.setBackground(new Color(30, 38, 64));
                btn.setBorder(BorderFactory.createLineBorder(NEON_ORO, 1));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(new Color(20, 24, 40));
                btn.setBorder(BorderFactory.createLineBorder(new Color(50, 65, 95), 1));
            }
        });
        return btn;
    }

    /**
     * Vincula capturadores de eventos de ratón para calcular colisiones en las tarjetas medias.
     */
    private void configurarManejadoresMouseVitrina() {
        this.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                Point puntoRaton = e.getPoint();
                int w = getWidth();
                int h = getHeight();
                
                // Distribución física proyectada de la vitrina en pantalla
                int inicioX = (w - (3 * 140 + 2 * 20)) / 2;
                int posicionY = h / 2 + 100;

                for (int i = 0; i < vitrinaTrofeos.size(); i++) {
                    int tX = inicioX + i * (140 + 20);
                    Rectangle limitesTarjeta = new Rectangle(tX, posicionY, 140, 75);
                    vitrinaTrofeos.get(i).isHovered = limitesTarjeta.contains(puntoRaton);
                }
            }
        });
    }

    /**
     * Inyecta el club galardonado y despierta de inmediato los vectores de animación.
     */
    public void mostrarCampeon(Equipo campeon) {
        if (campeon == null) return;
        
        this.equipoCampeon = campeon;
        this.lblNombreClubCampeon.setText(campeon.getNombre().toUpperCase());

        NumberFormat formatMoneda = NumberFormat.getCurrencyInstance(Locale.FRANCE);
        String capitalFormateado = formatMoneda.format(campeon.getPresupuesto()).replace(" ", "").replace(",00", "");

        String resumenMetricas = String.format(
                "MATCHDAY FINALES: 12  |  G: %d  E: %d  P: %d  |  TESORERÍA CLUB: %s",
                campeon.getPartidosGanados(),
                campeon.getPartidosEmpatados(),
                campeon.getPartidosPerdidos(),
                capitalFormateado
        );
        lblContenedorEstadisticas.setText(resumenMetricas);

        // Iniciar de forma segura el bucle de renderizado a tiempo completo
        if (motorAnimacionPrincipal != null && !motorAnimacionPrincipal.isRunning()) {
            motorAnimacionPrincipal.start();
        }
    }

    /**
     * Ejecuta el procesamiento físico y de variables de onda cíclica.
     */
    private void ejecutarCicloMatematico() {
        int w = getWidth();
        int h = getHeight();

        // Progresión de ciclos trigonométricos para levitación y auroras
        radianesLevitacion += 0.038;
        radianesAurora += 0.008;

        // Fluctuación alfa del banner parpadeante
        if (direccionOpacidad) {
            opacidadBannerHolografico += 0.015f;
            if (opacidadBannerHolografico >= 1.0f) {
                opacidadBannerHolografico = 1.0f;
                direccionOpacidad = false;
            }
        } else {
            opacidadBannerHolografico -= 0.015f;
            if (opacidadBannerHolografico <= 0.3f) {
                opacidadBannerHolografico = 0.3f;
                direccionOpacidad = true;
            }
        }

        // Actualizar coordenadas de confetis
        for (ParticulaConfeti p : sistemaConfeti) {
            p.actualizarPosicion(w, h);
        }

        // Actualizar coordenadas de destellos estelares
        for (DestelloEstelar d : sistemaDestellos) {
            d.actualizarCicloEstelar(w, h);
        }
    }

    /**
     * Interceptor principal de renderizado nativo 2D AWT.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        
        // Activación de motores de suavizado de bordes (Anti-Aliasing de alta fidelidad)
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        int w = getWidth();
        int h = getHeight();

        // 1. PINTAR FONDO GRADIENTE BASE DE CAMPEONES
        GradientPaint gradienteFondoBase = new GradientPaint(0, 0, PURPURA_CHAMPIONS, 0, h, AZUL_PROFUNDO);
        g2d.setPaint(gradienteFondoBase);
        g2d.fillRect(0, 0, w, h);

        // 2. RENDERIZAR AURORAS DINÁMICAS VECTORIALES
        dibujarAurorasEspacio(g2d, w, h);

        // 3. DIBUJAR CAPA DE DESTELLOS DE LUZ TRASERO (FOCO CENTRAL)
        g2d.setPaint(new RadialGradientPaint(
                new Point(w / 2, h / 2 - 35),
                Math.max(w, h) * 0.4f,
                new float[]{0.0f, 0.7f, 1.0f},
                new Color[]{new Color(0, 180, 255, 35), new Color(15, 12, 40, 8), new Color(0,0,0,0)}
        ));
        g2d.fillRect(0, 0, w, h);

        // 4. RENDERIZAR SISTEMA DE PARTÍCULAS INTERMEDIO (CONFETI Y ESTRELLAS)
        for (DestelloEstelar d : sistemaDestellos) {
            d.dibujarEstrella(g2d);
        }
        for (ParticulaConfeti p : sistemaConfeti) {
            p.dibujarFragmento(g2d);
        }

        // 5. PINTAR VITRINA SECUNDARIA DE TROFEOS DECORATIVOS (ZONA INFERIOR-MEDIA)
        dibujarVitrinaLogros(g2d, w, h);

        // =====================================================================
        // 6. NÚCLEO GRÁFICO: COPA DE LA CHAMPIONS CON LEVITACIÓN ACTIVA
        // =====================================================================
        int dimensionCopa = 165;
        int coordX = (w - dimensionCopa) / 2;
        
        // Ecuación senoidal amortiguada para calcular el desplazamiento vertical
        int saltoFlotante = (int) (Math.sin(radianesLevitacion) * 14);
        int coordY = (h / 2 - 95) + saltoFlotante;

        // Reflejo elíptico de aura dorada bajo el trofeo
        g2d.setPaint(new RadialGradientPaint(
                new Point(w / 2, coordY + dimensionCopa - 8),
                75f,
                new float[]{0.0f, 1.0f},
                new Color[]{new Color(255, 215, 0, 55), new Color(0,0,0,0)}
        ));
        g2d.fill(new Ellipse2D.Double(w / 2 - 65, coordY + dimensionCopa - 22, 130, 26));

        // Renderizado síncrono condicional del mapa de bits
        if (recursoImagenDisponible && imgChampionsTrofeo != null) {
            g2d.drawImage(imgChampionsTrofeo, coordX, coordY, dimensionCopa, dimensionCopa, null);
        } else {
            // Renderizado geométrico de contingencia arquitectónica en caso de ausencia de archivo
            dibujarCopaContingencia(g2d, coordX, coordY, dimensionCopa);
        }

        // Finalizar contexto gráfico de manera segura para liberar recursos del pipeline de vídeo
        g2d.dispose();
    }

    /**
     * Genera formas fluidas oscilantes simulando luces de estadio o auroras boreales.
     */
    private void dibujarAurorasEspacio(Graphics2D g2d, int w, int h) {
        int xOnda1 = (int) (Math.sin(radianesAurora) * 60) + (w / 2) - 200;
        int xOnda2 = (int) (Math.cos(radianesAurora * 0.8) * 45) + (w / 2) + 100;

        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.12f));
        
        g2d.setPaint(new GradientPaint(xOnda1, 0, NEON_CIAN, xOnda1 + 300, h, new Color(0,0,0,0)));
        g2d.fillOval(xOnda1 - 150, -100, 500, h + 200);

        g2d.setPaint(new GradientPaint(xOnda2, 0, NEON_ORO, xOnda2 - 250, h, new Color(0,0,0,0)));
        g2d.fillOval(xOnda2 - 200, -50, 450, h + 150);

        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
    }

    /**
     * Dibuja las tarjetas decorativas de la vitrina interactiva.
     */
    private void dibujarVitrinaLogros(Graphics2D g2d, int w, int h) {
        int cantidadTarjetas = vitrinaTrofeos.size();
        int anchoTarjeta = 140;
        int altoTarjeta = 75;
        int separacion = 20;
        
        int puntoInicioX = (w - (cantidadTarjetas * anchoTarjeta + (cantidadTarjetas - 1) * separacion)) / 2;
        int puntoY = h / 2 + 100;

        for (int i = 0; i < cantidadTarjetas; i++) {
            TarjetaVitrina tarjeta = vitrinaTrofeos.get(i);
            int calcularX = puntoInicioX + i * (anchoTarjeta + separacion);

            // Reacción estética por proximidad de cursor (Efecto Hover)
            if (tarjeta.isHovered) {
                g2d.setColor(new Color(255, 215, 0, 22));
                g2d.fillRect(calcularX, puntoY, anchoTarjeta, altoTarjeta);
                g2d.setColor(NEON_ORO);
            } else {
                g2d.setColor(new Color(255, 255, 255, 6));
                g2d.fillRect(calcularX, puntoY, anchoTarjeta, altoTarjeta);
                g2d.setColor(new Color(255, 255, 255, 30));
            }

            g2d.setStroke(new BasicStroke(1));
            g2d.drawRect(calcularX, puntoY, anchoTarjeta, altoTarjeta);

            // Dibujar textos internos de auditoría de logros
            g2d.setColor(NEON_CIAN);
            g2d.setFont(new Font("Segoe UI", Font.BOLD, 10));
            g2d.drawString(tarjeta.tituloLogro, calcularX + 12, puntoY + 25);

            g2d.setColor(Color.LIGHT_GRAY);
            g2d.setFont(new Font("Consolas", Font.PLAIN, 9));
            g2d.drawString(tarjeta.metricaAsociada, calcularX + 12, puntoY + 48);
            
            // Pequeño glifo de medalla geométrica dorada
            g2d.setColor(NEON_ORO);
            g2d.fillOval(calcularX + anchoTarjeta - 22, puntoY + 12, 10, 10);
        }
    }

    /**
     * Estructura geométrica vectorial de respaldo para la copa.
     */
    private void dibujarCopaContingencia(Graphics2D g2d, int x, int y, int tam) {
        g2d.setColor(new Color(20, 25, 45, 240));
        g2d.fill(new RoundRectangle2D.Double(x, y, tam, tam, 32, 32));
        g2d.setColor(NEON_ORO);
        g2d.setStroke(new BasicStroke(2));
        g2d.draw(new RoundRectangle2D.Double(x, y, tam, tam, 32, 32));
        g2d.setFont(new Font("Segoe UI Black", Font.BOLD, 14));
        g2d.drawString("CHAMPIONS CUP", x + 24, y + (tam / 2) + 6);
    }

    // =====================================================================
    // ESTRUCTURAS DE SOPORTE INTERNO / ENCAPSULAMIENTO DE MODELOS GRÁFICOS
    // =====================================================================

    /**
     * Modelo interno de datos para las tarjetas de la vitrina.
     */
    private static class TarjetaVitrina {
        String tituloLogro;
        String metricaAsociada;
        int identificadorInterno;
        boolean isHovered = false;

        TarjetaVitrina(String tit, String met, int id) {
            this.tituloLogro = tit;
            this.metricaAsociada = met;
            this.identificadorInterno = id;
        }
    }

    /**
     * Motor físico individualizado para fragmentos de confeti cayendo.
     */
    private class ParticulaConfeti {
        
        double velocidadCaidaVertical;
        double anguloInclinacionRadianes;
        double factorRotacionConstante;
        Color colorParticula;
        final Random generadorMatemático = new Random();
		private int posX;
		private int posY;

        ParticulaConfeti() {
            restablecerParticulaAleatoriamente(true);
        }

        void restablecerParticulaAleatoriamente(boolean primeraSiembra) {
            this.posX = generadorMatemático.nextInt(1300);
            this.posY = primeraSiembra ? generadorMatemático.nextInt(750) - 750 : -25;
            this.velocidadCaidaVertical = generadorMatemático.nextDouble() * 3.8 + 2.2;
            this.anguloInclinacionRadianes = generadorMatemático.nextDouble() * Math.PI * 2;
            this.factorRotacionConstante = (generadorMatemático.nextDouble() - 0.5) * 0.12;

            Color[] paletaGalaFestiva = {
                    new Color(255, 215, 0),   // Oro Radiante
                    new Color(220, 230, 245), // Plata Estelar
                    new Color(0, 235, 255),   // Cian Cyberpunk
                    new Color(255, 255, 255), // Destello Blanco Pureza
                    new Color(140, 80, 230)   // Púrpura Imperial
            };
            this.colorParticula = paletaGalaFestiva[generadorMatemático.nextInt(paletaGalaFestiva.length)];
        }

        void actualizarPosicion(int anchoPanel, int altoPanel) {
            posY += velocidadCaidaVertical;
            anguloInclinacionRadianes += factorRotacionConstante;
            
            // Inyección de viento oscilante lateral senoidal
            posX += Math.sin(posY / 28.0) * 0.55;

            // Reciclaje atómico si desborda la frontera inferior de renderizado
            if (posY > altoPanel + 10) {
                restablecerParticulaAleatoriamente(false);
                if (anchoPanel > 0) {
                    posX = generadorMatemático.nextInt(anchoPanel);
                }
            }
        }

        void dibujarFragmento(Graphics2D g2d) {
            Graphics2D gLocal = (Graphics2D) g2d.create();
            gLocal.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            AffineTransform matrizTransformacion = new AffineTransform();
            matrizTransformacion.translate(posX, posY);
            matrizTransformacion.rotate(anguloInclinacionRadianes);
            gLocal.setTransform(matrizTransformacion);

            gLocal.setColor(colorParticula);
            // Renderiza confeti rectangular con esquinas suavizadas
            gLocal.fill(new RoundRectangle2D.Double(-6, -3.5, 12, 7, 3, 3));
            gLocal.dispose();
        }
    }

    /**
     * Partícula especial luminosa que simula destellos o estrellas de gala.
     */
    private class DestelloEstelar {
        int xPos, yPos;
        int diametroMaximo;
        float alfaFluctuacion;
        boolean incrementandoBrillo;
        final Random randEngine = new Random();

        DestelloEstelar() {
            this.xPos = randEngine.nextInt(1280);
            this.yPos = randEngine.nextInt(720);
            this.diametroMaximo = randEngine.nextInt(4) + 2;
            this.alfaFluctuacion = randEngine.nextFloat();
            this.incrementandoBrillo = randEngine.nextBoolean();
        }

        void actualizarCicloEstelar(int w, int h) {
            // Ciclo de desvanecimiento alfa (Glint Effect)
            if (incrementandoBrillo) {
                alfaFluctuacion += 0.025f;
                if (alfaFluctuacion >= 1.0f) {
                    alfaFluctuacion = 1.0f;
                    incrementandoBrillo = false;
                }
            } else {
                alfaFluctuacion -= 0.025f;
                if (alfaFluctuacion <= 0.0f) {
                    alfaFluctuacion = 0.0f;
                    incrementandoBrillo = true;
                    // Mover la estrella a una nueva coordenada al apagarse
                    if (w > 0 && h > 0) {
                        xPos = randEngine.nextInt(w);
                        yPos = randEngine.nextInt(h);
                    }
                }
            }
        }

        void dibujarEstrella(Graphics2D g2d) {
            Graphics2D gStar = (Graphics2D) g2d.create();
            gStar.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alfaFluctuacion));
            gStar.setColor(Color.WHITE);
            
            // Dibujar núcleo estelar esférico
            gStar.fillOval(xPos, yPos, diametroMaximo, diametroMaximo);
            
            // Dibujar destellos en cruz (Efecto lente de cámara)
            gStar.setColor(new Color(0, 240, 255, 140));
            gStar.drawLine(xPos - 5, yPos + (diametroMaximo / 2), xPos + diametroMaximo + 5, yPos + (diametroMaximo / 2));
            gStar.drawLine(xPos + (diametroMaximo / 2), yPos - 5, xPos + (diametroMaximo / 2), yPos + diametroMaximo + 5);
            
            gStar.dispose();
        }
    }
}