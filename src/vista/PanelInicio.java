package vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * =============================================================================
 * PANEL DE BIENVENIDA - VERSIÓN LIMPIA
 * =============================================================================
 */
public class PanelInicio extends JPanel {

    // --- COMPONENTES ---
    private BufferedImage imagenFondo;
    public JButton btnComenzar;
    public JButton btnSalir;
    private JLabel lblVersion;

    // --- ESTÉTICA ---
    private final Color AZUL_OSCURO = new Color(24, 28, 43); // Azul para el texto
    private final Color AZUL_HOVER = new Color(52, 152, 219); // Azul claro para el hover
    private final Font FUENTE_BOTONES = new Font("Segoe UI", Font.BOLD, 18);

    public PanelInicio() {
        setLayout(new BorderLayout());
        cargarRecursos();
        inicializarInterfaz();
    }

    private void cargarRecursos() {
        try {
            // Ruta confirmada según tu árbol de proyecto
            imagenFondo = ImageIO.read(new File("src/recursos/fondo.png"));
        } catch (IOException e) {
            System.err.println("Error: No se pudo encontrar src/recursos/fondo.png");
            imagenFondo = null;
        }
    }

    private void inicializarInterfaz() {
        JLayeredPane capas = new JLayeredPane();
        capas.setLayout(null);

        // 1. PANEL DE BOTONES (Centrado en la parte inferior)
        JPanel panelBotones = new JPanel();
        panelBotones.setOpaque(false);
        panelBotones.setLayout(new GridLayout(2, 1, 0, 20));
        panelBotones.setBounds(490, 450, 300, 150);

        // Creamos los botones con el estilo azul solicitado
        btnComenzar = crearBotonProfesional("COMENZAR CARRERA");
        btnSalir = crearBotonProfesional("SALIR DEL JUEGO");

        panelBotones.add(btnComenzar);
        panelBotones.add(btnSalir);

        // 2. PIE DE PÁGINA
        lblVersion = new JLabel("ESTUDIO DEPORTIVO 2026 - v1.0.4", SwingConstants.RIGHT);
        lblVersion.setForeground(new Color(200, 200, 200));
        lblVersion.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblVersion.setBounds(0, 650, 1250, 30);

        // Agregar a las capas (Sin el título anterior)
        capas.add(panelBotones, Integer.valueOf(1));
        capas.add(lblVersion, Integer.valueOf(2));

        add(capas, BorderLayout.CENTER);
    }

    private JButton crearBotonProfesional(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(FUENTE_BOTONES);
        
        // Texto en Azul Oscuro para que resalte sobre el fondo blanco del botón
        btn.setForeground(AZUL_OSCURO); 
        btn.setBackground(Color.WHITE);
        
        btn.setFocusPainted(false);
        // Borde azul elegante
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AZUL_OSCURO, 2),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Efectos de interacción
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(AZUL_HOVER);
                btn.setForeground(Color.WHITE); // Cambia a blanco al pasar el mouse
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(Color.WHITE);
                btn.setForeground(AZUL_OSCURO);
            }
        });

        return btn;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        if (imagenFondo != null) {
            g2d.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);
            
            // He bajado la opacidad del oscurecimiento (de 120 a 50) 
            // para que tu imagen se vea más clara ya que quitamos el título
            g2d.setColor(new Color(0, 0, 0, 50)); 
            g2d.fillRect(0, 0, getWidth(), getHeight());
        } else {
            g2d.setColor(new Color(30, 35, 50));
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}