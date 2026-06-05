package vista;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * =============================================================================
 * SUPREMA MANAGER 2026 - FRAME PRINCIPAL (EL CONTENEDOR)
 * =============================================================================
 * Esta clase orquestra la navegación global. 
 * Estructura:
 * - West (Oeste): PanelMenu (Navegación siempre visible).
 * - Center (Centro): Panel de Contenido con CardLayout (Cambia según la sección).
 */
public class VentanaPrincipal extends JFrame {

    // --- PANELES DEL SISTEMA ---
    private PanelMenu panelMenu;
    private JPanel panelContenedor; // El que tiene el CardLayout
    private CardLayout navegador;

    // --- DICCIONARIO DE VISTAS ---
    private PanelInicio panelInicio;
    private PanelPlantilla panelPlantilla;
    private PanelMercado panelMercado;
    private PanelClasificacion panelClasificacion;
    private PanelSimulacion panelSimulacion;
    private PanelVictoria panelVictoria;

    public VentanaPrincipal() {
        configurarVentana();
        inicializarComponentes();
        setVisible(true);
    }

    private void configurarVentana() {
        setTitle("SUPREMA MANAGER 2026 - Professional Edition");
        setSize(1280, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrar en pantalla
        setLayout(new BorderLayout());
        
        // Icono de la aplicación (opcional, si tienes un logo.png)
        try {
            setIconImage(new ImageIcon("src/logo.png").getImage());
        } catch (Exception e) {
            System.out.println("Logo no encontrado, usando icono por defecto.");
        }
    }

    private void inicializarComponentes() {
        // 1. INICIALIZAR EL NAVEGADOR (CARDLAYOUT)
        navegador = new CardLayout();
        panelContenedor = new JPanel(navegador);
        panelContenedor.setBackground(new Color(20, 24, 35));

        // 2. INSTANCIAR TODOS LOS PANELES QUE YA HEMOS CREADO
        panelInicio = new PanelInicio();
        panelMenu = new PanelMenu();
        panelPlantilla = new PanelPlantilla();
        panelMercado = new PanelMercado();
        panelClasificacion = new PanelClasificacion();
        panelSimulacion = new PanelSimulacion();
        panelVictoria = new PanelVictoria();

        // 3. REGISTRAR PANELES EN EL CONTENEDOR
        // Cada String es la "llave" para llamar a esa pantalla
        panelContenedor.add(panelInicio, "INICIO");
        panelContenedor.add(panelPlantilla, "PLANTILLA");
        panelContenedor.add(panelMercado, "MERCADO");
        panelContenedor.add(panelClasificacion, "LIGA");
        panelContenedor.add(panelSimulacion, "SIMULACION");
        panelContenedor.add(panelVictoria, "VICTORIA");

        // 4. DISPOSICIÓN INICIAL
        // Al principio solo añadimos el Panel de Inicio ocupando todo el ancho
        add(panelContenedor, BorderLayout.CENTER);
        
        // Mostrar la pantalla de inicio
        navegador.show(panelContenedor, "INICIO");
    }

    // =========================================================================
    // LÓGICA DE NAVEGACIÓN (CONEXIÓN CON EL CONTROLADOR)
    // =========================================================================

    /**
     * Activa la interfaz de juego real después de pulsar "Comenzar".
     * Inserta el menú lateral y cambia al panel de plantilla.
     */
    public void iniciarJuegoReal() {
        add(panelMenu, BorderLayout.WEST); // Añadir el menú lateral
        cambiarPantalla("PLANTILLA");
        
        // Forzar actualización visual
        revalidate();
        repaint();
    }

    /**
     * Cambia el panel central por el solicitado mediante su nombre clave.
     */
    public void cambiarPantalla(String nombrePanel) {
        navegador.show(panelContenedor, nombrePanel);
        
        // Si no es el inicio, asegurarnos de que el menú está visible
        if (nombrePanel.equals("INICIO")) {
            remove(panelMenu);
        } else {
            // Actualizar resaltado de botones en el menú
            actualizarFocoMenu(nombrePanel);
        }
        
        revalidate();
        repaint();
    }

    private void actualizarFocoMenu(String nombre) {
        switch (nombre) {
            case "PLANTILLA": panelMenu.marcarBotonSeleccionado(panelMenu.btnPlantilla); break;
            case "MERCADO":   panelMenu.marcarBotonSeleccionado(panelMenu.btnMercado); break;
            case "LIGA":      panelMenu.marcarBotonSeleccionado(panelMenu.btnLiga); break;
        }
    }

    // =========================================================================
    // GETTERS PARA EL CONTROLADOR
    // =========================================================================

    public PanelInicio getPanelInicio() { return panelInicio; }
    public PanelMenu getPanelMenu() { return panelMenu; }
    public PanelPlantilla getPanelPlantilla() { return panelPlantilla; }
    public PanelMercado getPanelMercado() { return panelMercado; }
    public PanelClasificacion getPanelClasificacion() { return panelClasificacion; }
    public PanelSimulacion getPanelSimulacion() { return panelSimulacion; }
    public PanelVictoria getPanelVictoria() { return panelVictoria; }

    /**
     * Método de utilidad para mostrar errores globales.
     */
    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "SISTEMA SUPREMA", JOptionPane.ERROR_MESSAGE);
    }
}