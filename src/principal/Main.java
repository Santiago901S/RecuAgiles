package principal;

import controlador.ControladorJuego;
import vista.VentanaPrincipal;

public class Main {
    public static void main(String[] args) {
        // Ejecución segura de la interfaz en el hilo de eventos de Swing
        javax.swing.SwingUtilities.invokeLater(() -> {
            // 1. Creamos la ventana principal (que ya auto-instancia todos sus paneles internos)
            VentanaPrincipal vista = new VentanaPrincipal();
            
            // 2. Creamos el controlador pasándole la vista y sus sub-paneles correspondientes
            ControladorJuego controlador = new ControladorJuego(
                vista, 
                vista.getPanelClasificacion(), 
                vista.getPanelSimulacion()
            );
            
            // 3. Nos aseguramos de que la pantalla inicial sea el Panel de Inicio
            vista.cambiarPantalla("INICIO"); 
            
            // 4. Mostramos la aplicación
            vista.setVisible(true);
        });
    }
}