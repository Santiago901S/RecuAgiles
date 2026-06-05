package controlador;

import modelo.Equipo;
import modelo.Liga;
import modelo.Liga.Partido;
import modelo.SimuladorPartido.EventoPartido; 
import modelo.EstadoJuego; 
import modelo.Jugador;
import vista.PanelClasificacion;
import vista.VentanaPrincipal; 
import vista.PanelSimulacion;
import vista.PanelPlantilla;
import vista.PanelMenu;
import vista.PanelVictoria;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * =============================================================================
 * SUPREMA MANAGER 2026 - ARCHITECTURAL GAME CONTROLLER (PROFESSIONAL EDITION)
 * =============================================================================
 * Componente central del patrón MVC encargado de coordinar el flujo de estado,
 * las finanzas corporativas, las transferencias y la navegación multi-panel.
 */
public class ControladorJuego {

    // --- INSTANCIAS DEL PATRÓN MVC ---
    private final VentanaPrincipal vistaPrincipal; 
    private final PanelClasificacion panelClasificacion;
    private final PanelSimulacion panelSimulacion;
    private Liga modeloLiga;

    // --- ATRIBUTOS DE CONTROL HUMANO ---
    private String nombreEquipoUsuario;
    private Equipo equipoUsuario;
    private boolean modoDesarrolladorActivo;

    // --- BITÁCORA DE CONTROL DE TRAZABILIDAD (LOGGING) ---
    private final List<String> logSesion;

    /**
     * Construye el controlador maestro unificando el ecosistema gráfico y lógico.
     */
    public ControladorJuego(VentanaPrincipal vistaPrincipal, 
                            PanelClasificacion panelClasificacion, 
                            PanelSimulacion panelSimulacion) {
        
        this.vistaPrincipal = vistaPrincipal;
        this.panelClasificacion = panelClasificacion;
        this.panelSimulacion = panelSimulacion;
        this.logSesion = new ArrayList<>();
        this.modoDesarrolladorActivo = true;

        registrarLogsInternos("Inicializando subsistemas del controlador core...");
        inicializarManejadoresEventos();
    }

    /**
     * Establece el estado inicial de la nueva partida del mánager.
     */
    public void arrancarNuevaTemporada(Liga nuevaLiga, String nombreEquipoSeleccionado) {
        if (nuevaLiga == null || nombreEquipoSeleccionado == null) {
            registrarLogsInternos("[ERROR] Intento de arranque con parámetros nulos.");
            return;
        }

        this.modeloLiga = nuevaLiga;
        this.nombreEquipoUsuario = nombreEquipoSeleccionado;
        this.equipoUsuario = nuevaLiga.buscarEquipo(nombreEquipoSeleccionado);

        if (this.equipoUsuario == null) {
            registrarLogsInternos("[ALERTA] No se encontró al usuario en la base de datos de la Liga: " + nombreEquipoSeleccionado);
            if (!nuevaLiga.getEquipos().isEmpty()) {
                this.equipoUsuario = nuevaLiga.getEquipos().get(0);
                this.nombreEquipoUsuario = this.equipoUsuario.getNombre();
            }
        }

        registrarLogsInternos("Partida iniciada con éxito. Usuario capitanea: " + this.nombreEquipoUsuario);
        
        // --- REFRESCAR PANELES CON LOS DATOS INICIALES ---
        this.panelClasificacion.sincronizarYRefrescarDatos(this.modeloLiga, this.nombreEquipoUsuario);
        
        if (this.vistaPrincipal.getPanelPlantilla() != null && this.equipoUsuario != null) {
            this.vistaPrincipal.getPanelPlantilla().actualizarDatosEquipo(this.equipoUsuario);
            registrarLogsInternos("Futbolistas titulares y suplentes cargados con éxito en el vestuario.");
        }
        
        // Sincronizar el menú maestro superior/lateral
        actualizarContadoresFinancierosMenu();
    }

    // =========================================================================
    // I. ENRUTAMIENTO Y ASIGNACIÓN DE EVENTOS GRÁFICOS (LISTENERS)
    // =========================================================================

    private void inicializarManejadoresEventos() {
        registrarLogsInternos("Estructurando puente de eventos de interfaz...");

        // --- ENLACE MAESTRO: PANEL INICIO (BOTÓN COMENZAR CARRERA) ---
        if (this.vistaPrincipal != null && this.vistaPrincipal.getPanelInicio() != null) {
            this.vistaPrincipal.getPanelInicio().btnComenzar.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    registrarLogsInternos("Acción detectada: El mánager inicia su trayectoria deportiva.");
                    
                    EstadoJuego estado = EstadoJuego.getInstancia();
                    String equipoPorDefecto = EstadoJuego.NOMBRES_EQUIPOS[0]; 
                    arrancarNuevaTemporada(estado.getLiga(), equipoPorDefecto);
                    
                    vistaPrincipal.cambiarPantalla("PLANTILLA"); 
                    
                    if (equipoUsuario != null && vistaPrincipal.getPanelPlantilla() != null) {
                        vistaPrincipal.getPanelPlantilla().actualizarDatosEquipo(equipoUsuario);
                    }
                }
            });
        }

        // --- MANEJO DE EVENTOS: PANEL CLASIFICACIÓN ---
        this.panelClasificacion.btnSiguientePartido.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                procesarPeticionJugarJornada();
            }
        });

        this.panelClasificacion.btnVolverMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registrarLogsInternos("Usuario navega de regreso al despacho del club (Pantalla Plantilla).");
                refrescarYMostrarPlantilla();
            }
        });

        // --- MANEJO DE EVENTOS: PANEL SIMULACIÓN (BOTÓN CONTINUAR POST-PARTIDO) ---
        if (this.panelSimulacion.getBtnContinuar() != null) {
            this.panelSimulacion.getBtnContinuar().addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    concluirYConfirmarJornadaActual();
                }
            });
        }

        // --- INTERCEPTOR DE COMPORTAMIENTO: MENÚ LATERAL RAÍZ DE LA VENTANA ---
        if (this.vistaPrincipal.getPanelMenu() != null) {
            configurarListenersMenuLateral(this.vistaPrincipal.getPanelMenu());
        }

        // =====================================================================
        // CONEXIÓN INTEGRAL: LOGICA OPERATIVA DE PANEL PLANTILLA
        // =====================================================================
        if (this.vistaPrincipal != null && this.vistaPrincipal.getPanelPlantilla() != null) {
            PanelPlantilla panelP = this.vistaPrincipal.getPanelPlantilla();

            panelP.getBtnVenderJugador().addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    ejecutarProcesoVentaFutbolista(panelP);
                }
            });

            if (panelP.getPanelMenu() != null) {
                registrarLogsInternos("Conectando el menú táctico embebido del Despacho de Plantilla...");
                configurarListenersMenuLateral(panelP.getPanelMenu());
            }
        }

        // =====================================================================
        // CONEXIÓN MAESTRA: ACCIONES INTERNAS DEL PANEL MERCADO
        // =====================================================================
        if (this.vistaPrincipal != null && this.vistaPrincipal.getPanelMercado() != null) {
            var panelM = this.vistaPrincipal.getPanelMercado();
            
            panelM.btnVolverPlantilla.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    registrarLogsInternos("Acción: Botón de retorno clickeado dentro de Mercado. Redirigiendo a Plantilla.");
                    refrescarYMostrarPlantilla();
                }
            });

            panelM.btnFichar.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Jugador seleccionado = panelM.getJugadorSeleccionado();
                    if (seleccionado != null) {
                        if (equipoUsuario != null && equipoUsuario.getPresupuesto() < seleccionado.getPrecioMercado()) {
                            registrarLogsInternos("[ALERTA] Fondos insuficientes para la adquisición de: " + seleccionado.getNombre());
                            return;
                        }

                        boolean exito = EstadoJuego.getInstancia().ejecutarFichaje(seleccionado);
                        if (exito) {
                            panelM.actualizarMercado(EstadoJuego.getInstancia().getMercadoGlobal());
                            actualizarContadoresFinancierosMenu();
                            
                            if (vistaPrincipal.getPanelPlantilla() != null && equipoUsuario != null) {
                                vistaPrincipal.getPanelPlantilla().actualizarDatosEquipo(equipoUsuario);
                            }
                            registrarLogsInternos("Fichaje procesado silenciosamente.");
                        }
                    }
                }
            });
        }

        // =====================================================================
        // CONEXIÓN INTEGRAL DE BOTONES: PANEL VICTORIA
        // =====================================================================
        if (this.vistaPrincipal != null && this.vistaPrincipal.getPanelVictoria() != null) {
            PanelVictoria panelV = this.vistaPrincipal.getPanelVictoria();

            panelV.btnNuevaPartida.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    registrarLogsInternos("Mánager solicita revancha. Reiniciando base de datos deportiva...");
                    
                    EstadoJuego.reiniciarInstancia(); 
                    EstadoJuego nuevoEstado = EstadoJuego.getInstancia();
                    
                    if (nombreEquipoUsuario != null) {
                        Equipo clubEnNuevaLiga = nuevoEstado.getLiga().buscarEquipo(nombreEquipoUsuario);
                        if (clubEnNuevaLiga != null) {
                            nuevoEstado.setEquipoUsuario(clubEnNuevaLiga);
                        }
                    } else {
                        nombreEquipoUsuario = EstadoJuego.NOMBRES_EQUIPOS[0];
                        nuevoEstado.setEquipoUsuario(nuevoEstado.getLiga().buscarEquipo(nombreEquipoUsuario));
                    }
                    
                    arrancarNuevaTemporada(nuevoEstado.getLiga(), nombreEquipoUsuario);
                    vistaPrincipal.cambiarPantalla("PLANTILLA");
                }
            });

            panelV.btnSalir.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    registrarLogsInternos("Cierre de sesión desde la pantalla de campeones.");
                    System.exit(0);
                }
            });
        }
    }

    /**
     * Centraliza las acciones de cualquier menú del sistema para evitar duplicidad de comportamiento.
     */
    private void configurarListenersMenuLateral(PanelMenu menu) {
        menu.btnPlantilla.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registrarLogsInternos("Navegación lateral detectada -> PLANTILLA");
                refrescarYMostrarPlantilla();
            }
        });

        menu.btnMercado.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registrarLogsInternos("Navegación lateral detectada -> Abriendo panel de TRANSFERENCIAS (MERCADO)");
                if (vistaPrincipal.getPanelMercado() != null) {
                    List<Jugador> jugadoresMercado = EstadoJuego.getInstancia().getMercadoGlobal();
                    vistaPrincipal.getPanelMercado().actualizarMercado(jugadoresMercado);
                }
                vistaPrincipal.cambiarPantalla("MERCADO");
            }
        });

        menu.btnLiga.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registrarLogsInternos("Navegación lateral detectada -> LIGA");
                if (modeloLiga != null && panelClasificacion != null) {
                    panelClasificacion.sincronizarYRefrescarDatos(modeloLiga, nombreEquipoUsuario);
                }
                vistaPrincipal.cambiarPantalla("LIGA");
            }
        });
        
        menu.btnSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    // =========================================================================
    // II. GESTIÓN FINANCIERA INTERNA (VENTAS SILENCIOSAS)
    // =========================================================================

    private void ejecutarProcesoVentaFutbolista(PanelPlantilla panelP) {
        if (equipoUsuario == null) return;

        Jugador objetivo = panelP.getJugadorSeleccionadoParaVender();
        if (objetivo == null) return;

        if (equipoUsuario.getPlantilla().size() <= 11) {
            registrarLogsInternos("[ALERTA] Venta rechazada: El club no puede quedarse con menos de 11 efectivos.");
            return;
        }

        double valorReintegro = objetivo.getPrecioMercado() * 0.90;
        
        equipoUsuario.setPresupuesto(equipoUsuario.getPresupuesto() + valorReintegro);
        equipoUsuario.getPlantilla().remove(objetivo);
        
        EstadoJuego.getInstancia().getMercadoGlobal().add(objetivo);

        registrarLogsInternos("Operación de baja confirmada: " + objetivo.getNombre() + " | Reintegro: " + valorReintegro + " €");
        
        panelP.actualizarDatosEquipo(equipoUsuario);
        actualizarContadoresFinancierosMenu();
    }

    private void refrescarYMostrarPlantilla() {
        if (equipoUsuario != null && vistaPrincipal.getPanelPlantilla() != null) {
            vistaPrincipal.getPanelPlantilla().actualizarDatosEquipo(equipoUsuario);
        }
        vistaPrincipal.cambiarPantalla("PLANTILLA");
    }

    private void actualizarContadoresFinancierosMenu() {
        if (this.equipoUsuario == null) return;
        
        int jornadaActual = (modeloLiga != null) ? 
            modeloLiga.getNumeroJornadaActual() : EstadoJuego.getInstancia().getJornadaActual();

        if (this.vistaPrincipal.getPanelMenu() != null) {
            this.vistaPrincipal.getPanelMenu().actualizarDatos(
                this.nombreEquipoUsuario, this.equipoUsuario.getPresupuesto(), jornadaActual);
        }
        
        if (this.vistaPrincipal.getPanelPlantilla() != null && this.vistaPrincipal.getPanelPlantilla().getPanelMenu() != null) {
            this.vistaPrincipal.getPanelPlantilla().getPanelMenu().actualizarDatos(
                this.nombreEquipoUsuario, this.equipoUsuario.getPresupuesto(), jornadaActual);
        }
    }

    // =========================================================================
    // III. PROCESAMIENTO CORE DEL CALENDARIO Y EMPAREJAMIENTOS
    // =========================================================================

    private void procesarPeticionJugarJornada() {
        if (modeloLiga.isFinalizada() || modeloLiga.esFinalDeTemporada()) {
            registrarLogsInternos("Campeonato finalizado. Redirigiendo a pantalla de honores.");
            if (this.vistaPrincipal.getPanelVictoria() != null) {
                this.vistaPrincipal.getPanelVictoria().mostrarCampeon(modeloLiga.getCampeon());
                vistaPrincipal.cambiarPantalla("VICTORIA");
            }
            return;
        }

        int jornadaActiva = modeloLiga.getNumeroJornadaActual();
        registrarLogsInternos("Procesando emparejamientos oficiales para Matchday: " + jornadaActiva);

        Partido partidoUsuario = modeloLiga.obtenerPartidoDeEquipo(nombreEquipoUsuario);

        if (partidoUsuario == null) {
            registrarLogsInternos("[CRÍTICO] Error de consistencia interna: Calendario vacío.");
            return;
        }

        if (partidoUsuario.isJugado()) {
            concluirYConfirmarJornadaActual();
            return;
        }

        Equipo local = partidoUsuario.getLocal();
        Equipo visitante = partidoUsuario.getVisitante();

        try {
            List<EventoPartido> listaEventosAmbiente = new ArrayList<>();
            vistaPrincipal.getPanelSimulacion().comenzarSimulacion(local, visitante, listaEventosAmbiente);
            partidoUsuario.setJugado(true);

            vistaPrincipal.cambiarPantalla("SIMULACION");

        } catch (Exception ex) {
            int gL = (int)(Math.random() * 3);
            int gV = (int)(Math.random() * 3);
            partidoUsuario.finalizarPartido(gL, gV);
            concluirYConfirmarJornadaActual();
        }
    }

    // =========================================================================
    // IV. CIERRE DE JORNADA, AVANCE DE CONTADORES Y SIMULACIÓN IA
    // =========================================================================

    private void concluirYConfirmarJornadaActual() {
        if (modeloLiga == null) return;

        modeloLiga.simularPartidosRestantesIA(nombreEquipoUsuario);
        modeloLiga.avanzarJornada();

        this.panelClasificacion.sincronizarYRefrescarDatos(this.modeloLiga, this.nombreEquipoUsuario);

        if (modeloLiga.esFinalDeTemporada() || modeloLiga.isFinalizada()) {
            procesarFinalDeCompeticion();
        } else {
            vistaPrincipal.cambiarPantalla("LIGA");
        }
    }

    private void procesarFinalDeCompeticion() {
        Equipo campeon = modeloLiga.getCampeon();
        if (campeon != null && this.vistaPrincipal.getPanelVictoria() != null) {
            this.vistaPrincipal.getPanelVictoria().mostrarCampeon(campeon);
            vistaPrincipal.cambiarPantalla("VICTORIA"); 
        } else {
            vistaPrincipal.cambiarPantalla("LIGA");
        }
    }

    // =========================================================================
    // V. AUDITORÍA INTERNA DE CONSOLA
    // =========================================================================

    private void registrarLogsInternos(String traza) {
        String logFormateado = String.format("[SUPREMA CONTROLLER 2026] %s", traza);
        logSesion.add(logFormateado);
        if (modoDesarrolladorActivo) {
            System.out.println(logFormateado);
        }
    }

    // --- ACCESORES ---
    public Liga getModeloLiga() { return modeloLiga; }
    public String getNombreEquipoUsuario() { return nombreEquipoUsuario; }
    public Equipo getEquipoUsuario() { return equipoUsuario; }
    public List<String> getLogSesion() { return logSesion; }
    public void setModoDesarrolladorActivo(boolean activo) { this.modoDesarrolladorActivo = activo; }
}