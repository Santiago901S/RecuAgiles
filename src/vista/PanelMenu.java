package vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * =============================================================================
 * SUPERLIGA MANAGER 2026 - PANEL DE CONTROL Y DESPACHO LATERAL (PREMIUM EDITION)
 * =============================================================================
 * Interfaz de navegación de alta fidelidad con estética Dark Cyberpunk.
 * Gestiona los focos e iluminaciones de selección de manera atómica.
 */
public class PanelMenu extends JPanel {

    // --- PALETA DE COLORES PREMIUM (DARK FUTURISTIC) ---
    private final Color AZUL_FONDO      = new Color(11, 14, 23);
    private final Color FONDO_BOTON     = new Color(22, 27, 43);
    private final Color SELECCION_BG    = new Color(32, 42, 68);
    private final Color NEON_CIAN       = new Color(0, 240, 255);
    private final Color ROJO_SALIR      = new Color(255, 75, 75);

    // --- COMPONENTES GRÁFICOS (API MAESTRA) ---
    public JButton btnPlantilla;
    public JButton btnMercado;
    public JButton btnLiga;
    public JButton btnSalir;

    // Estructura interna de indexación de botones
    private final List<JButton> listaBotones;

    public PanelMenu() {
        this.listaBotones = new ArrayList<>();
        
        this.setBackground(AZUL_FONDO);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(new EmptyBorder(30, 15, 30, 15));
        this.setPreferredSize(new Dimension(240, 720));

        inicializarComponentes();
    }

    private void inicializarComponentes() {
        // =====================================================================
        // 1. BRANDING & MARCA DE AGUA DEL SIMULADOR
        // =====================================================================
        JLabel lblTitulo = new JLabel("SUPERLIGA MANAGER"); 
        lblTitulo.setFont(new Font("Segoe UI Black", Font.BOLD, 16));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.add(lblTitulo);

        JLabel lblVersion = new JLabel("ARCHITECT EDITION v2.6");
        lblVersion.setFont(new Font("Consolas", Font.BOLD, 10));
        lblVersion.setForeground(NEON_CIAN);
        lblVersion.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.add(lblVersion);

        // Espaciado estructural generoso al quitar el bloque de información intermedio
        this.add(Box.createVerticalStrut(60));

        // =====================================================================
        // 2. MATRIZ DE COMPONENTES DE NAVEGACIÓN (BOTONES TOTALMENTE LIMPIOS)
        // =====================================================================
        btnPlantilla = crearBotonNavegacion("MI PLANTILLA", NEON_CIAN);
        btnMercado   = crearBotonNavegacion("FICHAJES", NEON_CIAN); 
        btnLiga      = crearBotonNavegacion("TABLA LIGA", NEON_CIAN);
        btnSalir     = crearBotonNavegacion("SALIR DEL JUEGO", ROJO_SALIR);

        // Diseñar botón de salida
        btnSalir.setBackground(new Color(45, 20, 25));
        btnSalir.setForeground(ROJO_SALIR);

        // Añadir elementos al contenedor con espaciado uniforme
        this.add(btnPlantilla);
        this.add(Box.createVerticalStrut(12));
        this.add(btnMercado);
        this.add(Box.createVerticalStrut(12));
        this.add(btnLiga);
        
        // Colocar el botón de salida al fondo de la interfaz lateral
        this.add(Box.createVerticalGlue());
        this.add(btnSalir);
    }

    /**
     * Factoría de desarrollo para la instanciación unificada de botones.
     */
    private JButton crearBotonNavegacion(String texto, Color colorNeon) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setForeground(Color.WHITE);
        btn.setBackground(FONDO_BOTON);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(true);
        btn.setMaximumSize(new Dimension(210, 42));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setHorizontalAlignment(SwingConstants.CENTER); 
        
        // Borde exterior uniforme de 1 píxel muy fino
        btn.setBorder(BorderFactory.createLineBorder(new Color(45, 55, 75).darker(), 1));

        // Eventos dinámicos de iluminación (Hover)
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
                if (btn.getBackground() != SELECCION_BG) {
                    btn.setBackground(new Color(28, 35, 56));
                    btn.setBorder(BorderFactory.createLineBorder(colorNeon, 1));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (btn.getBackground() != SELECCION_BG) {
                    btn.setBackground(FONDO_BOTON);
                    btn.setBorder(BorderFactory.createLineBorder(new Color(45, 55, 75).darker(), 1));
                }
            }
        });

        listaBotones.add(btn);
        return btn;
    }

    /**
     * Modifica el foco estético global para indicar en qué pantalla se encuentra el mánager.
     */
    public void marcarBotonSeleccionado(JButton seleccionado) {
        for (JButton b : listaBotones) {
            b.setBackground(FONDO_BOTON);
            b.setBorder(BorderFactory.createLineBorder(new Color(45, 55, 75).darker(), 1));
        }
        
        if (seleccionado != null && seleccionado != btnSalir) {
            seleccionado.setBackground(SELECCION_BG);
            seleccionado.setBorder(BorderFactory.createLineBorder(NEON_CIAN, 1));
        }
    }

    /**
     * Sincroniza los indicadores del menú lateral.
     * MANTENIDO VACÍO E INTACTO PARA PREVENIR ERRORES DE COMPILACIÓN EN EL CONTROLADOR.
     */
    public void actualizarDatos(String nombreEquipo, double dinero, int jornada) {
        // Los datos visuales han sido removidos a petición para un diseño minimalista
    }
}