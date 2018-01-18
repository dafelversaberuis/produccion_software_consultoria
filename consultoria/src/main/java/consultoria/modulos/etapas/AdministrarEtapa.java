package consultoria.modulos.etapas;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.ScheduleEntryResizeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;

import consultoria.Conexion;
import consultoria.beans.Cita;
import consultoria.beans.Cliente;
import consultoria.beans.Compromiso;
import consultoria.beans.CompromisoGeneral;
import consultoria.beans.Cronograma;
import consultoria.beans.CronogramaGeneral;
import consultoria.beans.Diagnostico;
import consultoria.beans.Documentacion;
import consultoria.beans.DocumentacionGeneral;
import consultoria.beans.DocumentoActividad;
import consultoria.beans.DocumentoCronograma;
import consultoria.beans.DocumentoDiagnostico;
import consultoria.beans.Estado;
import consultoria.beans.EstadoDiagnostico;
import consultoria.beans.EstadoProyectoCliente;
import consultoria.beans.Indicador;
import consultoria.beans.InformacionEtapaIndicador;
import consultoria.beans.ObjetivoEtapaIndicador;
import consultoria.beans.PersonaDiagnostico;
import consultoria.beans.PlanAccionIndicador;
import consultoria.beans.PlanCliente;
import consultoria.beans.Planificacion;
import consultoria.beans.PlanificacionGeneral;
import consultoria.beans.PreguntaProyecto;
import consultoria.beans.ProyectoCliente;
import consultoria.beans.TareaProyecto;
import consultoria.beans.TiempoPlanificacion;
import consultoria.generales.ConsultarFuncionesAPI;
import consultoria.generales.Estadistica;
import consultoria.generales.FirmaComoImagen;
import consultoria.generales.IConstantes;
import consultoria.generales.IEmail;
import consultoria.modulos.IConsultasDAO;
import consultoria.modulos.personal.AdministrarSesionCliente;
import consultoria.modulos.tablasSoporte.HacerMantenimiento;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@ManagedBean
@ViewScoped
public class AdministrarEtapa extends ConsultarFuncionesAPI implements Serializable {

	/**
	 * 
	 */
	private static final long						serialVersionUID	= 7580332715225946309L;

	// inyecta el bean de sesion
	@ManagedProperty(value = "#{administrarSesionCliente}")
	private AdministrarSesionCliente		administrarSesionCliente;

	private String											parametroCliente;
	private String											parametroProyecto;

	private DocumentoDiagnostico				documentoDiagnostico;
	private DocumentoDiagnostico				documentoDiagnosticoTransaccion;

	private DocumentoCronograma					documentoCronograma;
	private DocumentoCronograma					documentoCronogramaTransaccion;

	private Diagnostico									diagnosicoTransaccion;
	private Cronograma									cronogramaTransaccion;

	private Cliente											cliente;
	private ProyectoCliente							proyectoCliente;

	private ObjetivoEtapaIndicador			objetivoTransaccion;

	private Indicador										indicadorTransaccion;
	private InformacionEtapaIndicador		informacionGeneralIndicador;

	private Planificacion								registroPlanificacion;
	private PlanificacionGeneral				planificacionGeneral;

	private CronogramaGeneral						cronogramaGeneral;
	private DocumentacionGeneral				documentacionGeneral;

	private Documentacion								documentacionTransaccion;

	private CompromisoGeneral						compromisoGeneral;

	private Planificacion								planificacionSeleccionada;

	private PersonaDiagnostico					personaDiagnostico;
	private DocumentoActividad					documentoActividadTransaccion;

	private BarChartModel								graficoBarras;
	private BarChartModel								graficoIndicador;

	private DocumentoActividad					documentoActividad;

	private String											tipoDocumentoSeleccionado;

	private List<SelectItem>						itemsMesesAno;
	private List<SelectItem>						itemsClientes;
	private List<SelectItem>						itemsIndicadores;
	private List<SelectItem>						itemsProyectosCliente;
	private List<Diagnostico>						diagnostico;
	private List<Planificacion>					planificacion;
	private List<Cronograma>						cronogramas;

	private List<Documentacion>					documentaciones;
	private List<Compromiso>						compromisos;
	private List<DocumentoActividad>		documentos;
	private List<DocumentoDiagnostico>	documentosDiagnostico;
	private List<DocumentoCronograma>		documentosCronograma;

	private Integer											vistaActual;

	// CITAS
	public String												textoConsulta;
	private ScheduleModel								calendario;
	private List<Cita>									citas;
	private Cita												citaSeleccionada;

	private int													tiempoConsumido;
	private int													tiempoMinutos;
	private boolean											asesoriaIniciada;

	private String											etapaCompartida;

	private List<SelectItem>						itemsPlanesDisponiblesCliente;
	
	private String hacerOnComplete;
	
	private String abiertoCerrado;
	


	public void actualizarPlanCliente() {

		Conexion conexion = new Conexion();
		Map<String, Object> condiciones = new HashMap<String, Object>();
		condiciones.put("id", this.citaSeleccionada.getTPlanSeleccionado().getId());
		PlanCliente pc = new PlanCliente();
		// con costo y total
		Map<String, Object> actualizables = new HashMap<String, Object>();
		actualizables.put("minutos_con_costo", this.citaSeleccionada.getTPlanSeleccionado().getMinutosConCosto().intValue() + 1);
		actualizables.put("minutos_gastados", this.citaSeleccionada.getTPlanSeleccionado().getMinutosConCosto().intValue() + 1);

		try {
			conexion.setAutoCommitBD(false);

			pc.getCamposBD();
			conexion.actualizarBD(pc.getEstructuraTabla().getTabla(), actualizables, condiciones, null);

			conexion.commitBD();

		} catch (Exception e) {
			conexion.rollbackBD();
			this.mostrarMensajeGlobal("transaccionFallida", "error");
			this.mostrarMensajeGlobal("eliminacionFallida", "error");
		} finally {
			conexion.cerrarConexion();
		}

	}

	/**
	 * Terminar asesoria
	 */
	public void terminarAsesoria() {

		this.asesoriaIniciada = false;
		this.mostrarMensajeGlobalPersonalizado("ASESORIA TERMINADA", "exito");

	}

	public void cambiarPlan() {
		try {

			this.citaSeleccionada.setTPlanSeleccionado(new PlanCliente());
			if (this.proyectoCliente.getCliente().getTPlanClienteSeleccionado() != null) {

				PlanCliente pc = new PlanCliente();
				pc.setId(this.proyectoCliente.getCliente().getTPlanClienteSeleccionado());
				this.citaSeleccionada.setTPlanSeleccionado(IConsultasDAO.getPlanesCliente(pc).get(0));
			}

		} catch (Exception e) {

			IConstantes.log.error(e, e);

		}
	}

	public void iniciarAsesoria() {

		try {
			this.mostrarMensajeGlobalPersonalizado("ASESORIA INICIADA", "exito");
			this.tiempoConsumido = 0;
			this.tiempoMinutos = 0;
			this.asesoriaIniciada = true;

			this.citaSeleccionada.setTPlanSeleccionado(new PlanCliente());
			PlanCliente pc = new PlanCliente();
			pc.setId(this.proyectoCliente.getCliente().getTPlanClienteSeleccionado());
			this.citaSeleccionada.setTPlanSeleccionado(IConsultasDAO.getPlanesCliente(pc).get(0));
		} catch (Exception e) {

			IConstantes.log.error(e, e);

		}

	}

	/**
	 * Procesa el tiempo
	 */
	public void procesarTiempo() {
		try {
			// por ahora viene segundo
			this.tiempoConsumido++;
			if (this.tiempoConsumido == 60) {
				// guarda el tiempo y actualiza contador
				this.tiempoConsumido = 0;
				this.tiempoMinutos++;

				actualizarPlanCliente();

				this.citaSeleccionada.setTPlanSeleccionado(new PlanCliente());
				PlanCliente pc = new PlanCliente();
				pc.setId(this.proyectoCliente.getCliente().getTPlanClienteSeleccionado());
				this.citaSeleccionada.setTPlanSeleccionado(IConsultasDAO.getPlanesCliente(pc).get(0));

			}
		} catch (Exception e) {

			IConstantes.log.error(e, e);

		}

	}

	/**
	 * Inciar conferencia
	 */
	public void abrirAsesoria() {
		try {

			// guarda
			agregarDesdeModal();

			this.mostrarMensajeGlobalPersonalizado("AREA DE ASESOR�A/CONFERENCIA...LEA LAS INSTRUCCIONES", "advertencia");

			this.abrirModal("panelConferencia");

			this.tiempoConsumido = 0;
			this.tiempoMinutos = 0;

			this.asesoriaIniciada = false;

			this.citaSeleccionada.setTPlanSeleccionado(new PlanCliente());

		} catch (Exception e) {

			IConstantes.log.error(e, e);

		}

	}

	/**
	 * Env�a recordatorio de la cita
	 */
	public void enviarCorreoCitas() {
		try {

			// guarda
			agregarDesdeModal();
			// envia correo

			this.getMensaje("mensajeDiagnostico", this.proyectoCliente.getCliente().getCliente(), this.proyectoCliente.getProyecto().getNombre());

			// correos de los involucrados
			List<String> aCorreos = new ArrayList<String>();
			aCorreos.add(this.proyectoCliente.getCliente().getCorreoElectronico());
			aCorreos.add(this.proyectoCliente.getConsultor().getCorreoElectronico());
			IEmail.enviarCorreoMasivo(this.getMensaje("mensajeCita", this.proyectoCliente.getCliente().getCliente(), this.proyectoCliente.getProyecto().getNombre(), this.proyectoCliente.getConsultor().getNombres() + " " + this.proyectoCliente.getConsultor().getApellidos(), this.getFechaHoraColombia(this.citaSeleccionada.getFechaInicio()) + " a " + this.getFechaHoraColombia(this.citaSeleccionada.getFechaFin()), getEstadoTexto(this.citaSeleccionada.getEstado())), "RECORDATORIO CITA CONSULTORIA ISOLUCIONES", aCorreos);

			this.mostrarMensajeGlobalPersonalizado("CORREOS RECORDATORIOS ENVIADOS A CONSULTOR Y CLIENTE", "exito");

		} catch (Exception e) {

			IConstantes.log.error(e, e);

		}

	}

	public String getEstadoTexto(String estado) {
		if (estado != null) {
			if (estado.equals("R"))
				return "Registrado/Programada";

			if (estado.equals("A"))
				return "Aprobada para atenci�n";

			if (estado.equals("T"))
				return "En Atenci�n";

			if (estado.equals("C"))
				return "Cancelada";

			if (estado.equals("P"))
				return "Aplazada";

			if (estado.equals("F"))
				return "Finalizada";

		}

		return "";
	}

	// para la agenda
	public void cancelarFormularioCita() {
		this.citaSeleccionada = new Cita(new DefaultScheduleEvent());
		this.textoConsulta = "";
		this.cerrarModal("eventDialog");
	}

	public void agregarDesdeModal() {
		if (proyectoCliente != null && proyectoCliente.getId() != null) {
			this.citaSeleccionada.setProyectoCliente(this.proyectoCliente);
			agregarCita();
		}

	}

	// Se selecciona un espacio vacio del calendario, luego se crea una nueva
	// instancia de la cita
	public void onDateSelect(SelectEvent selectEvent) {

		try {
			this.textoConsulta = "";
			Integer tiempoConsulta = 30;
			Calendar fechaFin = Calendar.getInstance();
			fechaFin.setTime((Date) selectEvent.getObject()); // Configuramos la fecha
																												// que se recibe
			fechaFin.add(Calendar.MINUTE, tiempoConsulta);
			this.citaSeleccionada = new Cita(new DefaultScheduleEvent("", (Date) selectEvent.getObject(), fechaFin.getTime()));
			this.citaSeleccionada.setFechaInicio((Date) selectEvent.getObject());
			this.citaSeleccionada.setFechaFin(fechaFin.getTime());

			if (this.citaSeleccionada.getProyectoCliente() != null && this.citaSeleccionada.getProyectoCliente().getId() != null) {

				ProyectoCliente proyecto = new ProyectoCliente();
				proyecto.setId(this.citaSeleccionada.getProyectoCliente().getId());
				this.proyectoCliente = IConsultasDAO.getProyectosCliente(proyecto).get(0);
				this.cliente = this.proyectoCliente.getCliente();

			} else {

				this.cliente = null;
				this.getCliente();

				this.proyectoCliente = null;
				this.getProyectoCliente();

			}

		} catch (Exception e) {

			IConstantes.log.error(e, e);

		}

	}

	// Se selecciona un evento previamente creado
	public void onEventSelect(SelectEvent selectEvent) {
		buscarCita((ScheduleEvent) selectEvent.getObject());
		this.abrirModal("eventDialog");
	}

	// Busca un evento y lo asigna al objeto cita
	public void buscarCita(ScheduleEvent evento) {
		try {
			getCitas();
			for (Cita c : this.citas) {
				if (c.getEvent().equals(evento)) {
					c.setEvent(evento);
					this.citaSeleccionada = c;
					// Actualizo la info del evento
					this.citaSeleccionada.setEvent(evento);
					this.citaSeleccionada.setModoEdicion(true);
					this.textoConsulta = c.getProyectoCliente().getCliente().getCliente() + "-" + c.getProyectoCliente().getProyecto().getNombre();

					// bloque para castear
					if (this.citaSeleccionada.getProyectoCliente() != null && this.citaSeleccionada.getProyectoCliente().getId() != null) {
						ProyectoCliente proyecto = new ProyectoCliente();
						proyecto.setId(this.citaSeleccionada.getProyectoCliente().getId());
						this.proyectoCliente = IConsultasDAO.getProyectosCliente(proyecto).get(0);
						this.cliente = this.proyectoCliente.getCliente();
					} else {
						this.cliente = null;
						this.getCliente();
						this.proyectoCliente = null;
						this.getProyectoCliente();
					}

					break;
				}
			}

		} catch (Exception e) {

			IConstantes.log.error(e, e);

		}

	}

	public void onEventMove(ScheduleEntryMoveEvent event) {
		buscarCita(event.getScheduleEvent());
		agregarCita();
	}

	public void onEventResize(ScheduleEntryResizeEvent event) {
		buscarCita(event.getScheduleEvent());
		agregarCita();
	}

	public void agregarCita() {
		getCitas();
		// Actualizo el evento con el nombre del paciente
		// this.mostrarMensajeGlobal("Entr� a guardar cita", "exito");
		Conexion conexion = new Conexion();
		if (this.citaSeleccionada.getProyectoCliente() != null && this.citaSeleccionada.getProyectoCliente().getId() != null) {
			((DefaultScheduleEvent) this.citaSeleccionada.getEvent()).setTitle(this.citaSeleccionada.getProyectoCliente().getCliente().getCliente() + "-" + this.citaSeleccionada.getProyectoCliente().getProyecto().getNombre());
			this.citaSeleccionada.setFechaInicio(this.citaSeleccionada.getEvent().getStartDate());
			this.citaSeleccionada.setFechaFin(this.citaSeleccionada.getEvent().getEndDate());
			try {
				conexion.setAutoCommitBD(false);
				// Si no existe la creo y la asigno al listado
				if (!this.citaSeleccionada.getModoEdicion()) {

					this.citaSeleccionada.setId(conexion.getConsecutivo(Cita.MAX).intValue());
					this.citaSeleccionada.getCamposBD();
					conexion.insertarBD(this.citaSeleccionada.getEstructuraTabla().getTabla(), this.citaSeleccionada.getEstructuraTabla().getPersistencia());
					this.citaSeleccionada.setModoEdicion(true);
					this.citas.add(this.citaSeleccionada);
					if (this.calendario != null)
						this.calendario.addEvent(this.citaSeleccionada.getEvent());
				} else {
					this.citaSeleccionada.getCamposBD();
					conexion.actualizarBD(this.citaSeleccionada.getEstructuraTabla().getTabla(), this.citaSeleccionada.getEstructuraTabla().getPersistencia(), this.citaSeleccionada.getEstructuraTabla().getLlavePrimaria(), null);

					if (this.calendario != null)
						this.calendario.updateEvent(this.citaSeleccionada.getEvent());

				}
				conexion.commitBD();
				this.mostrarMensajeGlobal("Cita/Agenda guardada", "exito");

				// this.citas = null;
				// this.getCitas();

			} catch (Exception e) {
				conexion.rollbackBD();
				this.mostrarMensajeGlobal("transaccionFallida", "error");
				IConstantes.log.error(e, e);
			} finally {
				conexion.cerrarConexion();
			}
		}

	}

	public Integer getVistaActual() {
		if (this.vistaActual == null) {
			this.vistaActual = 0;
		}
		return vistaActual;
	}

	public void setVistaActual(Integer vistaActual) {
		this.vistaActual = vistaActual;
	}

	public ScheduleModel getCalendario() {
		if (this.calendario == null)
			getCitas();
		return calendario;
	}

	public void setCalendario(ScheduleModel calendario) {
		this.calendario = calendario;
	}

	public void actualizarFecha() {
		Integer tiempoConsulta = 30;
		Calendar fechaFin = Calendar.getInstance();
		// fechaFin.set(Calendar.YEAR, Calendar.MONTH, Calendar.DATE,
		// Calendar.HOUR_OF_DAY, Calendar.MINUTE);
		fechaFin.setTime((Date) this.citaSeleccionada.getEvent().getStartDate()); // Configuramos
																																							// la
																																							// fecha
																																							// que
																																							// se
																																							// recibe
		fechaFin.add(Calendar.MINUTE, tiempoConsulta);
		((DefaultScheduleEvent) this.citaSeleccionada.getEvent()).setEndDate(fechaFin.getTime());
	}

	public List<Cita> getCitas() {
		Integer tiempoConsulta = 10000; // 3 d�as
		Calendar fechaFin = Calendar.getInstance();
		fechaFin.setTime(new Date()); // Configuramos la fecha que se recibe
		fechaFin.add(Calendar.MINUTE, tiempoConsulta);

		ProyectoCliente proyectoClienteConsulta = null;
		if (this.citas == null) {
			try {

				if (this.administrarSesionCliente != null && this.administrarSesionCliente.getPersonalSesion() != null && this.administrarSesionCliente.getPersonalSesion().getId() != null && this.administrarSesionCliente.getPersonalSesion().getTipoUsuario().equals("CO")) {
					proyectoClienteConsulta = new ProyectoCliente();
					proyectoClienteConsulta.getConsultor().setId(this.administrarSesionCliente.getPersonalSesion().getId());
				} else {
					if (this.administrarSesionCliente != null && this.administrarSesionCliente.getPersonalSesion() != null && this.administrarSesionCliente.getPersonalSesion().getId() != null && this.administrarSesionCliente.getPersonalSesion().getTipoUsuario().equals("CL")) {
						proyectoClienteConsulta = new ProyectoCliente();
						proyectoClienteConsulta.getCliente().setId(this.administrarSesionCliente.getPersonalSesion().getId());
					} else {
						proyectoClienteConsulta = null;
					}
				}

				this.citas = IConsultasDAO.getAgenda(new Date(), fechaFin.getTime(), proyectoClienteConsulta);
				this.calendario = new DefaultScheduleModel();
				this.citas.forEach(c -> this.calendario.addEvent(c.getEvent()));

			} catch (Exception e) {
				IConstantes.log.error(e, e);
			}
		}
		return citas;
	}

	public void setCitas(List<Cita> citas) {
		this.citas = citas;
	}

	public String getTextoConsulta() {
		return textoConsulta;
	}

	public void setTextoConsulta(String textoConsulta) {
		this.textoConsulta = textoConsulta;
	}

	public Cita getCitaSeleccionada() {
		// if(this.citaSeleccionada==null)
		// this.citaSeleccionada= new Cita();
		return citaSeleccionada;
	}

	public void setCitaSeleccionada(Cita citaSeleccionada) {
		this.citaSeleccionada = citaSeleccionada;
	}

	public void guardarArchivoDisco(String directorio_ruta_con_nombre_archivo, byte[] array) {

		if (array != null) {
			FileOutputStream fileOuputStream = null;
			try {
				fileOuputStream = new FileOutputStream(directorio_ruta_con_nombre_archivo);
				fileOuputStream.write(array);
			} catch (Exception e) {

			} finally {
				try {
					fileOuputStream.close();
				} catch (IOException e) {

				}
			}
		}

	}

	// privados

	/**
	 * Cancela la eliminacion de un docuemnto en transacci�n
	 * 
	 * @param aVista
	 */
	public void cancelarDocumentoTransaccion(String aVista) {
		try {

			this.documentoActividadTransaccion = null;
			this.getDocumentoActividadTransaccion();
			this.documentos = null;
			this.getDocumentos();

			if (aVista != null && aVista.equals("MODAL_ELIMINAR_DOCUMENTO")) {
				this.cerrarModal("panelEliminacionDocumento");

			} else {

				this.cerrarModal("panelEdicionDocumento");

			}
		} catch (Exception e) {

			IConstantes.log.error(e, e);

		}

	}

	private void guardarTemporalCronograma() {
		Conexion conexion = new Conexion();
		List<Cronograma> temps = null;

		try {
			SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
			// validaciones
			boolean ok = true;
			if (this.cronogramas != null && this.cronogramas.size() > 0) {
				for (Cronograma p : this.cronogramas) {

					// Revisa fechas doc
					if (p.getFechaInicioDocumentacion() != null) {
						if (p.getFechaFinDocumentacion() != null) {
							if (!(formato.format(p.getFechaInicioDocumentacion()).compareTo(formato.format(p.getFechaFinDocumentacion())) <= 0)) {
								ok = false;
								break;
							}
						}
					}
					if (p.getFechaFinDocumentacion() != null) {
						if (p.getFechaInicioDocumentacion() != null) {
							if (!(formato.format(p.getFechaFinDocumentacion()).compareTo(formato.format(p.getFechaInicioDocumentacion())) >= 0)) {
								ok = false;
								break;
							}
						}
					}
					if (p.getFechaSeguimientoDocumentacion() != null) {
						if (p.getFechaInicioDocumentacion() != null) {
							if (!(formato.format(p.getFechaSeguimientoDocumentacion()).compareTo(formato.format(p.getFechaInicioDocumentacion())) >= 0)) {
								ok = false;
								break;
							}
						}
						if (p.getFechaFinDocumentacion() != null) {
							if (!(formato.format(p.getFechaSeguimientoDocumentacion()).compareTo(formato.format(p.getFechaFinDocumentacion())) <= 0)) {
								ok = false;
								break;
							}
						}

					}

					// socializacion
					if (p.getFechaInicioSocializacion() != null) {
						if (p.getFechaFinSocializacion() != null) {
							if (!(formato.format(p.getFechaInicioSocializacion()).compareTo(formato.format(p.getFechaFinSocializacion())) <= 0)) {
								ok = false;
								break;
							}
						}
					}
					if (p.getFechaFinSocializacion() != null) {
						if (p.getFechaInicioSocializacion() != null) {
							if (!(formato.format(p.getFechaFinSocializacion()).compareTo(formato.format(p.getFechaInicioSocializacion())) >= 0)) {
								ok = false;
								break;
							}
						}
					}
					if (p.getFechaSeguimientoSocializacion() != null) {
						if (p.getFechaInicioSocializacion() != null) {
							if (!(formato.format(p.getFechaSeguimientoSocializacion()).compareTo(formato.format(p.getFechaInicioSocializacion())) >= 0)) {
								ok = false;
								break;
							}
						}
						if (p.getFechaFinSocializacion() != null) {
							if (!(formato.format(p.getFechaSeguimientoSocializacion()).compareTo(formato.format(p.getFechaFinSocializacion())) <= 0)) {
								ok = false;
								break;
							}
						}

					}

					// implementaci�n
					if (p.getFechaInicioImplementacion() != null) {
						if (p.getFechaFinImplementacion() != null) {
							if (!(formato.format(p.getFechaInicioImplementacion()).compareTo(formato.format(p.getFechaFinImplementacion())) <= 0)) {
								ok = false;
								break;
							}
						}
					}
					if (p.getFechaFinImplementacion() != null) {
						if (p.getFechaInicioImplementacion() != null) {
							if (!(formato.format(p.getFechaFinImplementacion()).compareTo(formato.format(p.getFechaInicioImplementacion())) >= 0)) {
								ok = false;
								break;
							}
						}
					}
					if (p.getFechaSeguimientoImplementacion() != null) {
						if (p.getFechaInicioImplementacion() != null) {
							if (!(formato.format(p.getFechaSeguimientoImplementacion()).compareTo(formato.format(p.getFechaInicioImplementacion())) >= 0)) {
								ok = false;
								break;
							}
						}
						if (p.getFechaFinImplementacion() != null) {
							if (!(formato.format(p.getFechaSeguimientoImplementacion()).compareTo(formato.format(p.getFechaFinImplementacion())) <= 0)) {
								ok = false;
								break;
							}
						}

					}

					// auditor�a
					if (p.getFechaInicioAuditoria() != null) {
						if (p.getFechaFinAuditoria() != null) {
							if (!(formato.format(p.getFechaInicioAuditoria()).compareTo(formato.format(p.getFechaFinAuditoria())) <= 0)) {
								ok = false;
								break;
							}
						}
					}
					if (p.getFechaFinAuditoria() != null) {
						if (p.getFechaInicioAuditoria() != null) {
							if (!(formato.format(p.getFechaFinAuditoria()).compareTo(formato.format(p.getFechaInicioAuditoria())) >= 0)) {
								ok = false;
								break;
							}
						}
					}
					if (p.getFechaSeguimientoAuditoria() != null) {
						if (p.getFechaInicioAuditoria() != null) {
							if (!(formato.format(p.getFechaSeguimientoAuditoria()).compareTo(formato.format(p.getFechaInicioAuditoria())) >= 0)) {
								ok = false;
								break;
							}
						}
						if (p.getFechaFinAuditoria() != null) {
							if (!(formato.format(p.getFechaSeguimientoAuditoria()).compareTo(formato.format(p.getFechaFinAuditoria())) <= 0)) {
								ok = false;
								break;
							}
						}

					}

				}
			} else {
				ok = false;

			}

			conexion.setAutoCommitBD(false);

			if (this.cronogramaGeneral.getFechaGeneracionTodo() == null) {
				this.cronogramaGeneral.setFechaGeneracionTodo(new Date());
			}

			Cronograma registroCronograma = new Cronograma();
			registroCronograma.getProyectoCliente().setId(this.proyectoCliente.getId());
			temps = IConsultasDAO.getCronograma(registroCronograma);
			if (temps != null && temps.size() > 0) {

				// actualizar
				this.cronogramaGeneral.getCamposBD();
				conexion.actualizarBD(this.cronogramaGeneral.getEstructuraTabla().getTabla(), this.cronogramaGeneral.getEstructuraTabla().getPersistencia(), this.cronogramaGeneral.getEstructuraTabla().getLlavePrimaria(), null);

				for (Cronograma p : this.cronogramas) {
					p.getCamposBD();
					conexion.actualizarBD(p.getEstructuraTabla().getTabla(), p.getEstructuraTabla().getPersistencia(), p.getEstructuraTabla().getLlavePrimaria(), null);

				}

			} else {
				// insertar

				this.cronogramaGeneral.getCamposBD();
				conexion.insertarBD(this.cronogramaGeneral.getEstructuraTabla().getTabla(), this.cronogramaGeneral.getEstructuraTabla().getPersistencia());

				for (Cronograma p : this.cronogramas) {

					p.getCamposBD();
					conexion.insertarBD(p.getEstructuraTabla().getTabla(), p.getEstructuraTabla().getPersistencia());
					p.setId(conexion.getUltimoSerialTransaccion(p.getEstructuraTabla().getTabla()));

				}

			}

			conexion.commitBD();

			this.mostrarMensajeGlobalPersonalizado("SE GUARDO TEMPORALMENTE EL CRONOGRAMA PARA NO PERDER INFORMACION", "exito");

			// reseteo de variables
			this.cronogramas = null;
			armarEstructuraCronograma();

		} catch (Exception e) {
			conexion.rollbackBD();
			this.mostrarMensajeGlobal("transaccionFallida", "error");
		} finally {
			conexion.cerrarConexion();
		}
	}

	public void asignarDocumentoCronograma(Cronograma aCronograma) {
		try {

			this.cronogramaTransaccion = aCronograma;

			guardarTemporalCronograma();

			this.documentoCronograma = null;
			this.getDocumentoCronograma();
			this.documentoCronogramaTransaccion = null;
			this.getDocumentoCronogramaTransaccion();
			this.documentosCronograma = null;
			this.getDocumentosCronograma();

			this.abrirModal("panelVerDocumento");

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}

	}

	public void asignarIndicador(Cronograma aCronograma) {
		try {

			this.cronogramaTransaccion = aCronograma;

			guardarTemporalCronograma();

			this.documentoCronograma = null;
			this.getDocumentoCronograma();
			this.documentoCronogramaTransaccion = null;
			this.getDocumentoCronogramaTransaccion();
			this.documentosCronograma = null;

			this.getDocumentosCronograma();

			this.abrirModal("panelVerDocumento");

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}

	}

	/**
	 * Asigna un documento de diagnostico
	 * 
	 * @param aDiagnostico
	 */
	public void asignarDocumentoDiagnostico(Diagnostico aDiagnostico) {
		try {

			this.diagnosicoTransaccion = aDiagnostico;

			actualizarDiagnostico("A");

			this.documentoDiagnostico = null;
			this.getDocumentoDiagnostico();
			this.documentoDiagnosticoTransaccion = null;
			this.getDocumentoDiagnosticoTransaccion();
			this.documentosDiagnostico = null;
			this.getDocumentosDiagnostico();

			this.abrirModal("panelVerDocumento");

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}

	}

	public void cancelarDocumentoCronogramaTransaccion(String aVista) {
		try {

			this.documentoCronogramaTransaccion = null;
			this.getDocumentoCronogramaTransaccion();
			this.documentosCronograma = null;
			this.getDocumentosCronograma();

			if (aVista != null && aVista.equals("MODAL_ELIMINAR_DOCUMENTO")) {
				this.cerrarModal("panelEliminacionDocumento");

			}
		} catch (Exception e) {

			IConstantes.log.error(e, e);

		}

	}

	/**
	 * Cancela la eliminacion de un docuemnto en transacci�n
	 * 
	 * @param aVista
	 */
	public void cancelarDocumentoDiagnosticoTransaccion(String aVista) {
		try {

			this.documentoDiagnosticoTransaccion = null;
			this.getDocumentoDiagnosticoTransaccion();
			this.documentosDiagnostico = null;
			this.getDocumentosDiagnostico();

			if (aVista != null && aVista.equals("MODAL_ELIMINAR_DOCUMENTO")) {
				this.cerrarModal("panelEliminacionDocumento");

			}
		} catch (Exception e) {

			IConstantes.log.error(e, e);

		}

	}

	public void eliminarDocumentoCronograma() {

		Conexion conexion = new Conexion();
		try {

			conexion.setAutoCommitBD(false);
			this.documentoCronogramaTransaccion.getCamposBD();
			conexion.eliminarBD(this.documentoCronogramaTransaccion.getEstructuraTabla().getTabla(), this.documentoCronogramaTransaccion.getEstructuraTabla().getLlavePrimaria());
			conexion.commitBD();
			this.mostrarMensajeGlobal("eliminacionExitosa", "exito");

			this.cerrarModal("panelEliminacionDocumento");

			armarEstructuraCronograma();

		} catch (Exception e) {
			conexion.rollbackBD();
			this.mostrarMensajeGlobal("transaccionFallida", "error");
			this.mostrarMensajeGlobal("eliminacionFallida", "error");

		} finally {
			conexion.cerrarConexion();
		}

		// reseteo de variables
		this.documentoCronogramaTransaccion = null;
		this.getDocumentoCronogramaTransaccion();
		this.documentosCronograma = null;
		this.getDocumentosCronograma();

	}

	/**
	 * Elimina el documento elegido
	 */
	public void eliminarDocumentoDiagnostico() {

		Conexion conexion = new Conexion();
		try {

			conexion.setAutoCommitBD(false);
			this.documentoDiagnosticoTransaccion.getCamposBD();
			conexion.eliminarBD(this.documentoDiagnosticoTransaccion.getEstructuraTabla().getTabla(), this.documentoDiagnosticoTransaccion.getEstructuraTabla().getLlavePrimaria());
			conexion.commitBD();
			this.mostrarMensajeGlobal("eliminacionExitosa", "exito");

			this.cerrarModal("panelEliminacionDocumento");

			armarEstructuraDiagnostico();

		} catch (Exception e) {
			conexion.rollbackBD();
			this.mostrarMensajeGlobal("transaccionFallida", "error");
			this.mostrarMensajeGlobal("eliminacionFallida", "error");

		} finally {
			conexion.cerrarConexion();
		}

		// reseteo de variables
		this.documentoDiagnosticoTransaccion = null;
		this.getDocumentoDiagnosticoTransaccion();
		this.documentosDiagnostico = null;
		this.getDocumentosDiagnostico();

	}

	public void editarDocumento() {

		Conexion conexion = new Conexion();
		try {

			Map<String, Object> act = new HashMap<String, Object>();
			act.put("estado", this.documentoActividadTransaccion.getEstado());
			act.put("fecha", new Date());

			conexion.setAutoCommitBD(false);
			this.documentoActividadTransaccion.getCamposBD();
			conexion.actualizarBD(this.documentoActividadTransaccion.getEstructuraTabla().getTabla(), act, this.documentoActividadTransaccion.getEstructuraTabla().getLlavePrimaria(), null);
			conexion.commitBD();
			this.mostrarMensajeGlobal("edicionExitosa", "exito");

			this.cerrarModal("panelEdicionDocumento");

		} catch (Exception e) {
			conexion.rollbackBD();
			this.mostrarMensajeGlobal("transaccionFallida", "error");

		} finally {
			conexion.cerrarConexion();
		}

		// reseteo de variables
		this.documentoActividadTransaccion = null;
		this.getDocumentoActividadTransaccion();
		this.documentos = null;
		this.getDocumentos();

	}

	/**
	 * Elimina el documento elegido
	 */
	public void eliminarDocumento() {

		Conexion conexion = new Conexion();
		try {

			conexion.setAutoCommitBD(false);
			this.documentoActividadTransaccion.getCamposBD();
			conexion.eliminarBD(this.documentoActividadTransaccion.getEstructuraTabla().getTabla(), this.documentoActividadTransaccion.getEstructuraTabla().getLlavePrimaria());
			conexion.commitBD();
			this.mostrarMensajeGlobal("eliminacionExitosa", "exito");

			this.cerrarModal("panelEliminacionDocumento");

		} catch (Exception e) {
			conexion.rollbackBD();
			this.mostrarMensajeGlobal("transaccionFallida", "error");
			this.mostrarMensajeGlobal("eliminacionFallida", "error");

		} finally {
			conexion.cerrarConexion();
		}

		// reseteo de variables
		this.documentoActividadTransaccion = null;
		this.getDocumentoActividadTransaccion();
		this.documentos = null;
		this.getDocumentos();

	}

	/**
	 * Obtiene los tiempos de planificaci�n entre fechas
	 * 
	 * @return tiempos
	 */
	private List<TiempoPlanificacion> getTiemposEntreFechas() {
		List<TiempoPlanificacion> tiempos = new ArrayList<TiempoPlanificacion>();
		try {
			TiempoPlanificacion tiempo = null;
			Date fechaIterada = null;
			SimpleDateFormat formatoInicioMes = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat formatoAnoMes = new SimpleDateFormat("yyyy-MM");

			Date fechaInicio = formatoInicioMes.parse(formatoAnoMes.format(this.proyectoCliente.getFechaInicio()) + "-01");
			Date fechaFin = formatoInicioMes.parse(formatoAnoMes.format(this.proyectoCliente.getFechaFin()) + "-01");

			if (fechaInicio.compareTo(fechaFin) == 0) {
				tiempo = new TiempoPlanificacion();
				tiempo.setEjecutado(false);
				tiempo.setProgramado(false);
				tiempo.setNumeroSemana(1);
				tiempo.setFecha(fechaInicio);
				tiempos.add(tiempo);

			} else {

				fechaIterada = fechaInicio;
				do {
					for (int i = 1; i <= IConstantes.NUMERO_SEMANAS.intValue(); i++) {
						tiempo = new TiempoPlanificacion();
						tiempo.setEjecutado(false);
						tiempo.setProgramado(false);
						tiempo.setNumeroSemana(i);
						tiempo.setFecha(fechaIterada);
						tiempos.add(tiempo);
					}

					fechaIterada = this.getFechaDiasSumados(fechaIterada);

				} while (fechaIterada.compareTo(fechaFin) <= 0);

			}

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
		return tiempos;

	}

	public String getLabel(String aSigla) {
		String label = "";

		if (aSigla != null && aSigla.equals("A")) {
			label = "ISOLUCIONES Y CLIENTE";
		}
		if (aSigla != null && aSigla.equals("CL")) {
			label = "CLIENTE";
		}
		if (aSigla != null && aSigla.equals("CO")) {
			label = "ISOLUCIONES";
		}

		return label;

	}

	/**
	 * Agrega por cada estado el total de veces que est� seleccionado
	 */
	private void agregarNumeroVecesEstado() {
		int numeroEstados = 0;
		if (this.proyectoCliente != null && this.proyectoCliente.gettEstadosProyecto() != null && this.proyectoCliente.gettEstadosProyecto().size() > 0) {
			for (EstadoProyectoCliente e : this.proyectoCliente.gettEstadosProyecto()) {
				numeroEstados = 0;
				for (Diagnostico d : this.diagnostico) {
					numeroEstados += d.gettEstadosDiagnostico().stream().filter(ed -> ed.istSeleccionado() && ed.getEstado().getId().intValue() == e.getEstado().getId().intValue()).count();
				}
				e.settNumeroVeces(numeroEstados);
			}

		}
	}

	/**
	 * Determina si es v�lido el diagn�stico
	 * 
	 * @return ok
	 */
	private boolean isValidoDiagnostico() {
		boolean ok = true;
		boolean evidenciaCompleta = true;
		boolean analisisCausas = true;
		boolean alMenosUnEstado = true;
		int indice = 0;
		int sw = 0;
		String sarta = "";
		List<Integer> indicesIncompletos = new ArrayList<Integer>();
		if (this.diagnostico != null && this.diagnostico.size() > 0) {
			for (Diagnostico d : this.diagnostico) {
				sw = 0;
				indice++;
				if (!(d.getEvidenciaEncontrada() != null && !d.getEvidenciaEncontrada().trim().equals(""))) {
					evidenciaCompleta = false;
					sw = 1;
				}
				if (!(d.gettEstadosDiagnostico() != null && d.gettEstadosDiagnostico().size() > 0 && d.gettEstadosDiagnostico().stream().anyMatch(p -> p.istSeleccionado()))) {
					alMenosUnEstado = false;
					sw = 1;
				}

				if (!(d.getAnalisisCausa() != null && !d.getAnalisisCausa().trim().equals(""))) {
					analisisCausas = false;
					// sw = 1;
				}
				if (!(d.getAccionesRealizar() != null && !d.getAccionesRealizar().trim().equals(""))) {
					analisisCausas = false;
					// sw = 1;
				}

				if (sw == 1) {
					indicesIncompletos.add(indice);
				}

			}

			if (!analisisCausas) {
				this.mostrarMensajeGlobalPersonalizado("EXISTEN ANALISIS CAUSAS O ACCIONES REALIZAR SIN DILIGENCIAR. RECUERDE QUE SI HAY NO CONFORMIDAD O RECOMENDACION DEBE DILIGENCIARLOS. IGUALMENTE SE DEJA SEGUIR", "advertencia");
			}

			if (!evidenciaCompleta || !alMenosUnEstado) {
				ok = false;
				if (!evidenciaCompleta) {
					this.mostrarMensajeGlobal("evidenciaIncompleta", "advertencia");
				}
				if (!alMenosUnEstado) {
					this.mostrarMensajeGlobal("estadosIncompletos", "advertencia");
				}

				if (indicesIncompletos != null && indicesIncompletos.size() > 0) {
					if (indicesIncompletos.size() == 1) {
						sarta = "" + indicesIncompletos.get(0);
					} else {
						sw = 0;
						for (Integer i : indicesIncompletos) {
							sw++;
							if (sw == 1) {
								sarta += "" + i;
							} else {
								sarta += "," + i;

							}
						}
					}

					this.mostrarMensajeGlobalPersonalizado("Los items que est�n incompletos y pendientes: " + sarta, "advertencia");
				}

				this.abrirModal("panelDiagnosticoIncompleto");
			}

		} else {
			ok = false;

		}

		return ok;
	}

	// publicos

	/**
	 * Elimina un indicador
	 */
	public void eliminarIndicador() {
		Conexion conexion = new Conexion();
		try {
			conexion.setAutoCommitBD(false);

			// Guardamos todos los datos en memoria para evitar p�dida de
			// informaci�n

			// guardamos informaci�n general
			this.informacionGeneralIndicador.getCamposBD();
			conexion.actualizarBD(this.informacionGeneralIndicador.getEstructuraTabla().getTabla(), this.informacionGeneralIndicador.getEstructuraTabla().getPersistencia(), this.informacionGeneralIndicador.getEstructuraTabla().getLlavePrimaria(), null);

			// guardamos la informaci�n de los objetivos
			for (ObjetivoEtapaIndicador o : this.informacionGeneralIndicador.getObjetivos()) {

				o.getCamposBD();
				conexion.actualizarBD(o.getEstructuraTabla().getTabla(), o.getEstructuraTabla().getPersistencia(), o.getEstructuraTabla().getLlavePrimaria(), null);

				// guardamos la informaci�n de los indicadores
				for (Indicador i : o.getIndicadores()) {
					i.getCamposBD();
					conexion.actualizarBD(i.getEstructuraTabla().getTabla(), i.getEstructuraTabla().getPersistencia(), i.getEstructuraTabla().getLlavePrimaria(), null);
				}

			}

			// elimina info de planes de acci�n
			Map<String, Object> llaveEliminar = new HashMap<String, Object>();
			llaveEliminar.put("id_indicador", this.indicadorTransaccion.getId());
			PlanAccionIndicador plan = new PlanAccionIndicador();
			plan.getCamposBD();
			conexion.eliminarBD(plan.getEstructuraTabla().getTabla(), llaveEliminar);

			// elimina el indicador
			this.indicadorTransaccion.getCamposBD();
			conexion.eliminarBD(this.indicadorTransaccion.getEstructuraTabla().getTabla(), this.indicadorTransaccion.getEstructuraTabla().getLlavePrimaria());

			conexion.commitBD();
			this.mostrarMensajeGlobal("indicadorEliminadoExitosamente", "exito");
			this.mostrarMensajeGlobal("tambienGuardoTemporalmente", "advertencia");
			this.indicadorTransaccion = null;
			this.getIndicadorTransaccion();

			armarEstructuraIndicadores();

		} catch (Exception e) {
			conexion.rollbackBD();
			this.mostrarMensajeGlobal("transaccionFallida", "error");
			this.mostrarMensajeGlobal("eliminacionFallida", "error");
		} finally {
			conexion.cerrarConexion();
		}

	}

	public void removerCompromiso(Compromiso aCompromiso) {
		this.compromisos.remove(aCompromiso);
		this.mostrarMensajeGlobalPersonalizado("Se removió el compromiso del listado. Todos los cambios se verán reflejados al guatrdar cambios", "exito");

	}

	public void asignarDocumentacion(Documentacion aDocumentacion, String aRol) {

		this.documentacionTransaccion = aDocumentacion;

		this.tipoDocumentoSeleccionado = aRol;

		this.documentos = null;
		this.getDocumentos();

		this.documentoActividad = null;
		this.getDocumentoActividad();

		this.abrirModal("panelDocumento");

	}

	/**
	 * Asigna un indicador para realizar transacciones
	 * 
	 * @param aIndicador
	 * @param aVista
	 */
	public void asignarIndicador(Indicador aIndicador, String aVista) {
		boolean ok = true;
		this.indicadorTransaccion = aIndicador;
		if (aVista != null && aVista.equals("ELIMINAR_INDICADOR")) {
			this.abrirModal("panelEliminacionIndicador");
		} else if (aVista != null && aVista.equals("RESULTADOS_INDICADOR")) {

			if (!(aIndicador != null && aIndicador.getNombreIndicador() != null && !aIndicador.getNombreIndicador().trim().equals(""))) {
				ok = false;
			}

			if (!(aIndicador != null && aIndicador.getFormula() != null && !aIndicador.getFormula().trim().equals(""))) {
				ok = false;
			}

			if (!(aIndicador != null && aIndicador.getResponsableDecision() != null && !aIndicador.getResponsableDecision().trim().equals(""))) {
				ok = false;
			}

			if (!(aIndicador != null && aIndicador.getResponsableCalculo() != null && !aIndicador.getResponsableCalculo().trim().equals(""))) {
				ok = false;
			}

			if (!(aIndicador != null && aIndicador.getSistemaInformacion() != null && !aIndicador.getSistemaInformacion().trim().equals(""))) {
				ok = false;
			}

			if (!(aIndicador != null && aIndicador.getFrecuencia() != null)) {
				ok = false;
			}

			if (ok) {

				this.guardarResultadosIndicador(null);

				this.abrirModal("panelResultadosIndicador");

			} else {
				this.mostrarMensajeGlobalPersonalizado("todos los campos de informaci�n general de este indicador deben estar diligenciados para poder ver/calcular los resultados", "advertencia");
			}
		}

	}

	/**
	 * Asigna un objetivo para realizar operaciones sobre este
	 * 
	 * @param aObjetivoEtapaIndicador
	 */
	public void asignarObjetivo(ObjetivoEtapaIndicador aObjetivoEtapaIndicador) {

		this.objetivoTransaccion = aObjetivoEtapaIndicador;
		this.abrirModal("panelEliminacionObjetivo");

	}

	/**
	 * Elimina un objetivo y toda su informaci�n asociada
	 */
	public void eliminarObjetivo() {

		Conexion conexion = new Conexion();
		Map<String, Object> condiciones = null;

		Indicador indicadorTemp = null;

		try {
			conexion.setAutoCommitBD(false);

			condiciones = new HashMap<String, Object>();

			// Guardamos todos los datos en memoria para evitar p�dida de
			// informaci�n

			// guardamos informaci�n general
			this.informacionGeneralIndicador.getCamposBD();
			conexion.actualizarBD(this.informacionGeneralIndicador.getEstructuraTabla().getTabla(), this.informacionGeneralIndicador.getEstructuraTabla().getPersistencia(), this.informacionGeneralIndicador.getEstructuraTabla().getLlavePrimaria(), null);

			// guardamos la informaci�n de los objetivos
			for (ObjetivoEtapaIndicador o : this.informacionGeneralIndicador.getObjetivos()) {

				o.getCamposBD();
				conexion.actualizarBD(o.getEstructuraTabla().getTabla(), o.getEstructuraTabla().getPersistencia(), o.getEstructuraTabla().getLlavePrimaria(), null);

				// guardamos la informaci�n de los indicadores
				for (Indicador i : o.getIndicadores()) {
					i.getCamposBD();
					conexion.actualizarBD(i.getEstructuraTabla().getTabla(), i.getEstructuraTabla().getPersistencia(), i.getEstructuraTabla().getLlavePrimaria(), null);

					// elimina los planes de acci�n asociados a lso indicadores
					// del
					// objetivo
					if (o.getId().intValue() == this.objetivoTransaccion.getId().intValue()) {

						Map<String, Object> llaveEliminar = new HashMap<String, Object>();
						llaveEliminar.put("id_indicador", i.getId());
						PlanAccionIndicador plan = new PlanAccionIndicador();
						plan.getCamposBD();
						conexion.eliminarBD(plan.getEstructuraTabla().getTabla(), llaveEliminar);
					}

				}

			}

			// elimina los planes de acci�n asociados a cada indicador de este
			// objetivo

			// elimina sus indicadores asociados
			condiciones = new HashMap<String, Object>();
			condiciones.put("id_objetivo", this.objetivoTransaccion.getId());
			indicadorTemp = new Indicador();
			indicadorTemp.getCamposBD();
			conexion.eliminarBD(indicadorTemp.getEstructuraTabla().getTabla(), condiciones);

			// elimina el objetivo padre

			this.objetivoTransaccion.getCamposBD();
			conexion.eliminarBD(this.objetivoTransaccion.getEstructuraTabla().getTabla(), this.objetivoTransaccion.getEstructuraTabla().getLlavePrimaria());

			conexion.commitBD();
			this.mostrarMensajeGlobal("objetivoEliminadoExitosamente", "exito");
			this.mostrarMensajeGlobal("tambienGuardoTemporalmente", "advertencia");
			this.objetivoTransaccion = null;
			this.getObjetivoTransaccion();

			armarEstructuraIndicadores();

		} catch (Exception e) {
			conexion.rollbackBD();
			this.mostrarMensajeGlobal("transaccionFallida", "error");
			this.mostrarMensajeGlobal("eliminacionFallida", "error");
		} finally {
			conexion.cerrarConexion();
		}

	}

	/**
	 * Obtiene el estado del indicador
	 * 
	 * @param aIndicador
	 * @return estado
	 */
	public String getEstadoIndicador(Indicador aIndicador) {
		String estado = "VERDE";

		try {

			if (!(aIndicador != null && aIndicador.getNombreIndicador() != null && !aIndicador.getNombreIndicador().trim().equals(""))) {
				estado = "ROJO";
			}

			if (!(aIndicador != null && aIndicador.getFormula() != null && !aIndicador.getFormula().trim().equals(""))) {
				estado = "ROJO";
			}

			if (!(aIndicador != null && aIndicador.getResponsableDecision() != null && !aIndicador.getResponsableDecision().trim().equals(""))) {
				estado = "ROJO";
			}

			if (!(aIndicador != null && aIndicador.getResponsableCalculo() != null && !aIndicador.getResponsableCalculo().trim().equals(""))) {
				estado = "ROJO";
			}

			if (!(aIndicador != null && aIndicador.getSistemaInformacion() != null && !aIndicador.getSistemaInformacion().trim().equals(""))) {
				estado = "ROJO";
			}

			if (!(aIndicador != null && aIndicador.getFrecuencia() != null)) {
				estado = "ROJO";
			}

			if (isTodosPeriodosNulos(aIndicador)) {
				estado = "ROJO";
			}

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
		return estado;
	}

	/**
	 * Agerga un nuevo plan de acci�n
	 */
	public void agregarPlanAccion() {

		PlanAccionIndicador plan = new PlanAccionIndicador();
		plan.getIndicador().setId(this.indicadorTransaccion.getId());

		this.indicadorTransaccion.getPlanesAccionIndicador().add(plan);
	}

	/**
	 * Remueve el plan de acci�n seleccionado
	 * 
	 * @param aPlanAccionIndicador
	 */
	public void removerPlanAccion(PlanAccionIndicador aPlanAccionIndicador) {

		this.indicadorTransaccion.getPlanesAccionIndicador().remove(aPlanAccionIndicador);
	}

	public void guardarFormaCompletaCompromiso() {
		Conexion conexion = new Conexion();

		try {

			conexion.setAutoCommitBD(false);

			// Gurdamos todos los datos en memoria para evitar p�dida de
			// informaci�n

			this.compromisoGeneral.getCamposBD();
			conexion.actualizarBD(this.compromisoGeneral.getEstructuraTabla().getTabla(), this.compromisoGeneral.getEstructuraTabla().getPersistencia(), this.compromisoGeneral.getEstructuraTabla().getLlavePrimaria(), null);

			conexion.commitBD();

			this.mostrarMensajeGlobalPersonalizado("Compromisos actualizados (Conclusiones y/o firma). Revise diálogo", "exito");

			// vuelve y consulta lo �ltimo
			armarEstructuraCronograma();

			this.abrirModal("panelResumen");
			this.cerrarModal("panelCompromisoCompleto");

		} catch (

		Exception e) {
			conexion.rollbackBD();
			this.mostrarMensajeGlobal("transaccionFallida", "error");
		} finally {
			conexion.cerrarConexion();
		}

	}

	public void guardarFormaCompletaDocumentacion() {
		Conexion conexion = new Conexion();

		try {

			conexion.setAutoCommitBD(false);

			// guarda temporalmente lo de la frma y otros
			this.documentacionGeneral.getCamposBD();
			conexion.actualizarBD(this.documentacionGeneral.getEstructuraTabla().getTabla(), this.documentacionGeneral.getEstructuraTabla().getPersistencia(), this.documentacionGeneral.getEstructuraTabla().getLlavePrimaria(), null);

			conexion.commitBD();

			this.mostrarMensajeGlobalPersonalizado("Documentaci�n actualizada (Conclusiones y/o firma). Revise di�logo", "exito");

			// vuelve y consulta lo �ltimo
			armarEstructuraDocumentacion();

			this.abrirModal("panelResumen");
			this.cerrarModal("panelDocumentacionCompleto");

		} catch (

		Exception e) {
			conexion.rollbackBD();
			this.mostrarMensajeGlobal("transaccionFallida", "error");
		} finally {
			conexion.cerrarConexion();
		}

	}

	public void guardarFormaCompletaCronograma() {
		Conexion conexion = new Conexion();

		try {

			conexion.setAutoCommitBD(false);

			// Gurdamos todos los datos en memoria para evitar p�dida de
			// informaci�n

			this.cronogramaGeneral.getCamposBD();
			conexion.actualizarBD(this.cronogramaGeneral.getEstructuraTabla().getTabla(), this.cronogramaGeneral.getEstructuraTabla().getPersistencia(), this.cronogramaGeneral.getEstructuraTabla().getLlavePrimaria(), null);

			conexion.commitBD();

			this.mostrarMensajeGlobalPersonalizado("ETAPA ACTUALIZADA (Conclusiones y/o firma).REVISE DIALOGO", "exito");

			// vuelve y consulta lo �ltimo
			armarEstructuraCronograma();

			this.abrirModal("panelResumen");
			this.cerrarModal("panelCronogramaCompleto");

		} catch (

		Exception e) {
			conexion.rollbackBD();
			this.mostrarMensajeGlobal("transaccionFallida", "error");
		} finally {
			conexion.cerrarConexion();
		}

	}

	/**
	 * Guarda de forma completa seg�n su vista
	 * 
	 * @param aVista
	 */
	public void guardarFormaCompleta(String aVista) {
		Conexion conexion = new Conexion();

		try {

			conexion.setAutoCommitBD(false);

			// Gurdamos todos los datos en memoria para evitar p�dida de
			// informaci�n

			// guardamos informaci�n general
			if (this.informacionGeneralIndicador.getFechaGeneracionTodo() == null) {
				this.informacionGeneralIndicador.setFechaGeneracionTodo(new Date());
			}

			this.informacionGeneralIndicador.getCamposBD();
			conexion.actualizarBD(this.informacionGeneralIndicador.getEstructuraTabla().getTabla(), this.informacionGeneralIndicador.getEstructuraTabla().getPersistencia(), this.informacionGeneralIndicador.getEstructuraTabla().getLlavePrimaria(), null);

			// guardamos la informaci�n de los objetivos
			for (ObjetivoEtapaIndicador o : this.informacionGeneralIndicador.getObjetivos()) {

				o.getCamposBD();
				conexion.actualizarBD(o.getEstructuraTabla().getTabla(), o.getEstructuraTabla().getPersistencia(), o.getEstructuraTabla().getLlavePrimaria(), null);

				// guardamos la informaci�n de los indicadores de todos
				for (Indicador i : o.getIndicadores()) {
					i.getCamposBD();
					conexion.actualizarBD(i.getEstructuraTabla().getTabla(), i.getEstructuraTabla().getPersistencia(), i.getEstructuraTabla().getLlavePrimaria(), null);
				}

			}

			conexion.commitBD();
			if (aVista != null && aVista.equals("guardar")) {
				this.mostrarMensajeGlobalPersonalizado("Informaci�n de indicadores actualizada (Conclusiones y firma). Revise di�logo", "exito");
			}
			// vuelve y consulta lo �ltimo
			armarEstructuraIndicadores();

			this.abrirModal("panelResumen");
			this.cerrarModal("panelIndicadoresCompleto");

		} catch (

		Exception e) {
			conexion.rollbackBD();
			this.mostrarMensajeGlobal("transaccionFallida", "error");
		} finally {
			conexion.cerrarConexion();
		}

	}

	/**
	 * Guarda de forma incompleta los indicadores
	 */
	public void guardarFormaIncompleta() {
		Conexion conexion = new Conexion();
		boolean incompleto = false;

		try {

			conexion.setAutoCommitBD(false);

			// Gurdamos todos los datos en memoria para evitar p�dida de
			// informaci�n

			// guardamos informaci�n general
			this.informacionGeneralIndicador.getCamposBD();
			conexion.actualizarBD(this.informacionGeneralIndicador.getEstructuraTabla().getTabla(), this.informacionGeneralIndicador.getEstructuraTabla().getPersistencia(), this.informacionGeneralIndicador.getEstructuraTabla().getLlavePrimaria(), null);

			// guardamos la informaci�n de los objetivos
			for (ObjetivoEtapaIndicador o : this.informacionGeneralIndicador.getObjetivos()) {

				o.getCamposBD();
				conexion.actualizarBD(o.getEstructuraTabla().getTabla(), o.getEstructuraTabla().getPersistencia(), o.getEstructuraTabla().getLlavePrimaria(), null);

				// guardamos la informaci�n de los indicadores de todos
				for (Indicador i : o.getIndicadores()) {
					i.getCamposBD();
					conexion.actualizarBD(i.getEstructuraTabla().getTabla(), i.getEstructuraTabla().getPersistencia(), i.getEstructuraTabla().getLlavePrimaria(), null);
				}

			}

			conexion.commitBD();

			this.mostrarMensajeGlobalPersonalizado("Informaci�n de indicadores guardada, revise di�logo", "exito");

			// vuelve y consulta lo �ltimo
			armarEstructuraIndicadores();

			for (ObjetivoEtapaIndicador o : this.informacionGeneralIndicador.getObjetivos()) {
				for (Indicador i : o.getIndicadores()) {
					if (getEstadoIndicador(i).equals("ROJO")) {
						incompleto = true;
						break;

					}
				}
				if (incompleto) {
					break;
				}
			}

			if (incompleto) {
				this.abrirModal("panelIndicadoresIncompleto");
			} else {
				this.abrirModal("panelIndicadoresCompleto");

			}

		} catch (Exception e) {
			conexion.rollbackBD();
			this.mostrarMensajeGlobal("transaccionFallida", "error");
		} finally {
			conexion.cerrarConexion();
		}

	}

	/**
	 * Guarda los resultados de indicador
	 */
	public void guardarResultadosIndicador(String aTipo) {
		Conexion conexion = new Conexion();

		try {

			if (this.indicadorTransaccion != null) {
				conexion.setAutoCommitBD(false);

				// Gurdamos todos los datos en memoria para evitar p�dida de
				// informaci�n

				// guardamos informaci�n general
				this.informacionGeneralIndicador.getCamposBD();
				conexion.actualizarBD(this.informacionGeneralIndicador.getEstructuraTabla().getTabla(), this.informacionGeneralIndicador.getEstructuraTabla().getPersistencia(), this.informacionGeneralIndicador.getEstructuraTabla().getLlavePrimaria(), null);

				// guardamos la informaci�n de los objetivos
				for (ObjetivoEtapaIndicador o : this.informacionGeneralIndicador.getObjetivos()) {

					o.getCamposBD();
					conexion.actualizarBD(o.getEstructuraTabla().getTabla(), o.getEstructuraTabla().getPersistencia(), o.getEstructuraTabla().getLlavePrimaria(), null);

					// guardamos la informaci�n de los indicadores de todos
					for (Indicador i : o.getIndicadores()) {
						i.getCamposBD();
						conexion.actualizarBD(i.getEstructuraTabla().getTabla(), i.getEstructuraTabla().getPersistencia(), i.getEstructuraTabla().getLlavePrimaria(), null);
					}

				}

				// guardamos el actual modificado
				this.indicadorTransaccion.getCamposBD();
				conexion.actualizarBD(this.indicadorTransaccion.getEstructuraTabla().getTabla(), this.indicadorTransaccion.getEstructuraTabla().getPersistencia(), this.indicadorTransaccion.getEstructuraTabla().getLlavePrimaria(), null);

				// eliminamos planes de acci�n de bd e ingresamos los nuevos
				if (aTipo != null) {
					Map<String, Object> llaveEliminar = new HashMap<String, Object>();
					llaveEliminar.put("id_indicador", this.indicadorTransaccion.getId());
					PlanAccionIndicador plan = new PlanAccionIndicador();
					plan.getCamposBD();
					conexion.eliminarBD(plan.getEstructuraTabla().getTabla(), llaveEliminar);

					// insertamos los nuevos registros
					if (this.indicadorTransaccion.getPlanesAccionIndicador() != null && this.indicadorTransaccion.getPlanesAccionIndicador().size() > 0) {

						for (PlanAccionIndicador p : this.indicadorTransaccion.getPlanesAccionIndicador()) {
							p.getCamposBD();
							conexion.insertarBD(p.getEstructuraTabla().getTabla(), p.getEstructuraTabla().getPersistencia());
						}

					}
				}

				conexion.commitBD();

				if (aTipo != null) {
					this.mostrarMensajeGlobal("resultadosIndicadorGuardado", "exito");

					this.cerrarModal("panelResultadosIndicador");

				}

				this.mostrarMensajeGlobal("tambienGuardoTemporalmente", "advertencia");

				// vuelve y consulta lo �ltimo
				armarEstructuraIndicadores();

			}

		} catch (Exception e) {
			conexion.rollbackBD();
			this.mostrarMensajeGlobal("transaccionFallida", "error");
		} finally {
			conexion.cerrarConexion();
		}

	}

	/**
	 * Crea un indicador vac�o
	 * 
	 * @param aObjetivoEtapaIndicador
	 */
	public void crearIndicadorVacio(ObjetivoEtapaIndicador aObjetivoEtapaIndicador) {
		Conexion conexion = new Conexion();

		Indicador indicadorTemp = null;

		try {

			if (aObjetivoEtapaIndicador != null && aObjetivoEtapaIndicador.getId() != null) {
				conexion.setAutoCommitBD(false);

				// Gurdamos todos los datos en memoria para evitar p�dida de
				// informaci�n

				// guardamos informaci�n general
				this.informacionGeneralIndicador.getCamposBD();
				conexion.actualizarBD(this.informacionGeneralIndicador.getEstructuraTabla().getTabla(), this.informacionGeneralIndicador.getEstructuraTabla().getPersistencia(), this.informacionGeneralIndicador.getEstructuraTabla().getLlavePrimaria(), null);

				// guardamos la informaci�n de los objetivos
				for (ObjetivoEtapaIndicador o : this.informacionGeneralIndicador.getObjetivos()) {

					o.getCamposBD();
					conexion.actualizarBD(o.getEstructuraTabla().getTabla(), o.getEstructuraTabla().getPersistencia(), o.getEstructuraTabla().getLlavePrimaria(), null);

					// guardamos la informaci�n de los indicadores
					for (Indicador i : o.getIndicadores()) {
						i.getCamposBD();
						conexion.actualizarBD(i.getEstructuraTabla().getTabla(), i.getEstructuraTabla().getPersistencia(), i.getEstructuraTabla().getLlavePrimaria(), null);
					}

				}

				// Crea un indicador vac�o y con valores por defecto
				indicadorTemp = new Indicador();
				indicadorTemp.getObjetivo().setId(aObjetivoEtapaIndicador.getId());
				indicadorTemp.setMetaSuperior(IConstantes.META_SUPERIOR_INICIAL);
				indicadorTemp.setMetaIntermedia(IConstantes.META_INTERMEDIA_INICIAL);
				indicadorTemp.setMetaInferior(IConstantes.META_INFERIOR_INICIAL);

				indicadorTemp.getCamposBD();
				conexion.insertarBD(indicadorTemp.getEstructuraTabla().getTabla(), indicadorTemp.getEstructuraTabla().getPersistencia());
				indicadorTemp.setId(conexion.getUltimoSerialTransaccion(indicadorTemp.getEstructuraTabla().getTabla()));

				conexion.commitBD();

				this.mostrarMensajeGlobal("indicadorAgregado", "exito");
				this.mostrarMensajeGlobal("tambienGuardoTemporalmente", "advertencia");

				// vuelve y consulta lo �ltimo
				armarEstructuraIndicadores();

			}

		} catch (Exception e) {
			conexion.rollbackBD();
			this.mostrarMensajeGlobal("transaccionFallida", "error");
		} finally {
			conexion.cerrarConexion();
		}
	}

	/**
	 * Cancela la eliminaci�n del indicador
	 */
	public void cancelarResultadosIndicador() {

		armarEstructuraIndicadores();
		this.cerrarModal("panelResultadosIndicador");

	}

	/**
	 * Cancela la eliminaci�n del indicador
	 */
	public void cancelarEliminacionIndicador() {

		armarEstructuraIndicadores();
		this.cerrarModal("panelEliminacionIndicador");

	}

	/**
	 * Cancela la eliminaci�n del obstivo seleccionado
	 */
	public void cancelarEliminacionObjetivo() {

		armarEstructuraIndicadores();
		this.cerrarModal("panelEliminacionObjetivo");

	}

	public void agregarCompromiso() {

		Compromiso compromiso = new Compromiso();
		this.compromisos.add(compromiso);
		this.mostrarMensajeGlobalPersonalizado("Se agregó el compromiso al listado. La información quedará guardada al presionar guardar", "exito");
	}

	/**
	 * Crea un nuevo objetivo de forma vac�a
	 * 
	 * @param aPrimeraTransaccion
	 */
	public void crearObjetivoVacio(String aPrimeraTransaccion) {
		Conexion conexion = new Conexion();
		ObjetivoEtapaIndicador objetivoTemp = null;
		Indicador indicadorTemp = null;

		try {

			conexion.setAutoCommitBD(false);

			if (aPrimeraTransaccion != null && aPrimeraTransaccion.equals(IConstantes.AFIRMACION)) {
				// crea la informaci�n general de indicadores

				this.informacionGeneralIndicador.getCamposBD();
				conexion.insertarBD(this.informacionGeneralIndicador.getEstructuraTabla().getTabla(), this.informacionGeneralIndicador.getEstructuraTabla().getPersistencia());
				this.informacionGeneralIndicador.setId(conexion.getUltimoSerialTransaccion(this.informacionGeneralIndicador.getEstructuraTabla().getTabla()));

			} else {

				// guarda la informaci�n que lleve en el momento para que no se
				// pierda
				this.informacionGeneralIndicador.getCamposBD();
				conexion.actualizarBD(this.informacionGeneralIndicador.getEstructuraTabla().getTabla(), this.informacionGeneralIndicador.getEstructuraTabla().getPersistencia(), this.informacionGeneralIndicador.getEstructuraTabla().getLlavePrimaria(), null);

				for (ObjetivoEtapaIndicador o : this.informacionGeneralIndicador.getObjetivos()) {
					o.getCamposBD();
					conexion.actualizarBD(o.getEstructuraTabla().getTabla(), o.getEstructuraTabla().getPersistencia(), o.getEstructuraTabla().getLlavePrimaria(), null);
					for (Indicador i : o.getIndicadores()) {
						i.getCamposBD();
						conexion.actualizarBD(i.getEstructuraTabla().getTabla(), i.getEstructuraTabla().getPersistencia(), i.getEstructuraTabla().getLlavePrimaria(), null);
					}
				}
			}

			// crea un registro vac�o de objetivo
			objetivoTemp = new ObjetivoEtapaIndicador();
			objetivoTemp.getInformacionEtapaIndicador().setId(this.informacionGeneralIndicador.getId());

			objetivoTemp.getCamposBD();
			conexion.insertarBD(objetivoTemp.getEstructuraTabla().getTabla(), objetivoTemp.getEstructuraTabla().getPersistencia());
			objetivoTemp.setId(conexion.getUltimoSerialTransaccion(objetivoTemp.getEstructuraTabla().getTabla()));

			// a su vez le crea un indicador vac�o y con valores por defecto
			indicadorTemp = new Indicador();
			indicadorTemp.getObjetivo().setId(objetivoTemp.getId());
			indicadorTemp.setMetaSuperior(IConstantes.META_SUPERIOR_INICIAL);
			indicadorTemp.setMetaIntermedia(IConstantes.META_INTERMEDIA_INICIAL);
			indicadorTemp.setMetaInferior(IConstantes.META_INFERIOR_INICIAL);

			indicadorTemp.getCamposBD();
			conexion.insertarBD(indicadorTemp.getEstructuraTabla().getTabla(), indicadorTemp.getEstructuraTabla().getPersistencia());
			indicadorTemp.setId(conexion.getUltimoSerialTransaccion(indicadorTemp.getEstructuraTabla().getTabla()));

			conexion.commitBD();
			if (!(aPrimeraTransaccion != null && aPrimeraTransaccion.equals(IConstantes.AFIRMACION))) {

				this.mostrarMensajeGlobal("objetivoCreadoExitosamente", "exito");
				this.mostrarMensajeGlobal("tambienGuardoTemporalmente", "advertencia");

			} else {

				this.mostrarMensajeGlobal("bienvenidoIndicador", "exito");
			}

			// Vuelve y lo llama para recargar con lo �ltimo
			armarEstructuraIndicadores();

		} catch (Exception e) {
			conexion.rollbackBD();
			this.mostrarMensajeGlobal("transaccionFallida", "error");
		} finally {
			conexion.cerrarConexion();
		}
	}

	/**
	 * Elabora un nuevo diagn�stico
	 */
	public void elaborarNuevamenteDiagnostico() {

		this.cliente = null;
		this.getCliente();
		this.proyectoCliente = null;
		this.getProyectoCliente();

		this.cerrarModal("panelResumen");

	}

	public void descargarDocumentoCronograma(DocumentoCronograma aDocumentoCronograma) {
		try {
			DocumentoCronograma temp = IConsultasDAO.getAdjuntoDocumentoCronograma(aDocumentoCronograma);
			if (temp != null) {
				descargarAdjunto(temp.getArchivo(), temp.getExtensionArchivo(), temp.getContentType());
			}

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}

	}

	/**
	 * Descarga el doumento de diagnostico
	 * 
	 * @param aDocumentoEquipo
	 */
	/**
	 * Descarga el adjunto
	 */
	public void descargarDocumentoDiagnostico(DocumentoDiagnostico aDocumentoDiagnostico) {
		try {
			DocumentoDiagnostico temp = IConsultasDAO.getAdjuntoDocumentoDiagnostico(aDocumentoDiagnostico);
			if (temp != null) {
				descargarAdjunto(temp.getArchivo(), temp.getExtensionArchivo(), temp.getContentType());
			}

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}

	}

	/**
	 * Descarga el archivo requerido
	 * 
	 * @param aArchivo
	 * @param aExtension
	 * @param aContentType
	 */
	private void descargarAdjunto(byte[] aArchivo, String aExtension, String aContentType) {

		try {

			FacesContext context = FacesContext.getCurrentInstance();
			ExternalContext ext = context.getExternalContext();
			SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");

			HttpServletResponse response = (HttpServletResponse) ext.getResponse();
			response.setContentType(aContentType);
			response.setHeader("Content-Disposition", "attachment; filename=" + formato.format(new Date()) + "." + aExtension.toLowerCase());
			response.setContentLength(aArchivo.length);
			ServletOutputStream servletOutputStream = response.getOutputStream();

			servletOutputStream.write(aArchivo, 0, aArchivo.length);

			servletOutputStream.flush();
			servletOutputStream.close();
			FacesContext.getCurrentInstance().responseComplete();

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
	}

	/**
	 * Descarga el adjunto solicitado
	 */
	public void descargarDocumento(DocumentoActividad aDocumentoEquipo) {
		try {
			HacerMantenimiento hacerMantenimiento = new HacerMantenimiento();
			this.documentoActividadTransaccion = aDocumentoEquipo;
			DocumentoActividad temp = IConsultasDAO.getAdjuntoDocumento(aDocumentoEquipo);
			if (temp != null) {
				if (aDocumentoEquipo.getDescargable() != null && aDocumentoEquipo.getDescargable().trim().equals("N")) {
					descargarAdjunto(temp.getArchivo(), temp.getExtensionArchivo(), temp.getContentType());
				} else {
					hacerMantenimiento.crearPdf(temp.getArchivo(), aDocumentoEquipo);
				}

			}

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}

	}

	/**
	 * Obtiene la url de la etapa
	 * 
	 * @return url
	 */
	public String getUrlEtapaDiagnostico() {
		if (this.cliente != null && this.proyectoCliente != null && this.cliente.getId() != null && this.proyectoCliente.getId() != null) {
			return "/etapas/realizarDiagnosticoInicial.xhtml?faces-redirect=true&c=" + this.cliente.getId() + "&p=" + this.proyectoCliente.getId();
		} else {
			return "";
		}

	}

	/**
	 * Selecciona la actividad de docuemntaci�n en la consulta
	 */
	public void seleccionarActividadDocumentacionConsulta(Planificacion aPlanificacion) {

		try {
			this.planificacionSeleccionada = aPlanificacion;
			this.documentos = null;
			// **************ojo*****para que sirviera nueva
			// interfaz*****this.getDocumentos();
			this.abrirModal("panelDocumento");

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}

	}

	/**
	 * Selecciona la opci�n de documentaci�n
	 */
	public void seleccionarOpcionDocumentacion() {

		this.documentos = null;
		// ***********ojo para que sirviera nueva interfaz*****this.getDocumentos();

		this.abrirModal("panelDocumento");
		this.cerrarModal("panelResumenSeleccion");

	}

	/**
	 * Selecciona una fila y la asocia al objeto de actividad seleccionada
	 * 
	 * @param event
	 */
	public void onRowSelect(SelectEvent event) {

		this.mostrarMensajeGlobalPersonalizado("Actividad seleccionada:" + this.planificacionSeleccionada.getTareaProyecto().getTarea() + ". Espere un momento se esta abriendo di�logo con opciones...", "advertencia");

		this.abrirModal("panelResumenSeleccion");

		// FacesMessage msg = new FacesMessage("Car Selected", ((Car)
		// event.getObject()).getId());
		// FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	/**
	 * Deseleciona una fila de la tabla
	 * 
	 * @param event
	 */
	public void onRowUnselect(UnselectEvent event) {
		// FacesMessage msg = new FacesMessage("Car Unselected", ((Car)
		// event.getObject()).getId());
		// FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	/**
	 * Obtiene semanas del mes
	 * 
	 * @return NUMERO_SEMANAS
	 */
	public Integer getSemanasMes() {

		return IConstantes.NUMERO_SEMANAS;

	}

	/**
	 * Obtiene la cabecera de los meses
	 * 
	 * @return meses
	 */
	public List<String> getCabecerasMeses() {
		SimpleDateFormat formato = new SimpleDateFormat("MMMM'/'yyyy");
		List<String> meses = new ArrayList<String>();
		int i = 0;
		if (this.planificacion != null && this.planificacion.size() > 0 && this.planificacion.get(0).gettTiemposPlanificacion().get(0) != null && this.planificacion.get(0).gettTiemposPlanificacion() != null && this.planificacion.get(0).gettTiemposPlanificacion().size() > 0) {
			for (TiempoPlanificacion p : this.planificacion.get(0).gettTiemposPlanificacion()) {
				i++;
				if (i == 1) {
					meses.add(formato.format(p.getFecha()));
				}

				if (i == IConstantes.NUMERO_SEMANAS.intValue()) {
					i = 0;
				}

			}
		}

		return meses;

	}

	/**
	 * Obtiene el valor programado
	 * 
	 * @param aPlanificacion
	 * @param aNumero
	 * @param aFecha
	 * @return ok
	 */
	public boolean isValorProgramado(Planificacion aPlanificacion, Integer aNumero, Date aFecha) {
		boolean ok = false;
		SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
		for (TiempoPlanificacion t : aPlanificacion.gettTiemposPlanificacion()) {
			if (t.getNumeroSemana().intValue() == aNumero.intValue() && formatoFecha.format(aFecha).compareTo(formatoFecha.format(t.getFecha())) == 0) {
				if (t.isProgramado()) {
					ok = true;

				} else {
					ok = false;

				}
				break;
			}
		}
		return ok;

	}

	/**
	 * Selecciona la ejecuci�n
	 * 
	 * @param aPlanificacion
	 * @param aNumero
	 * @param aFecha
	 * @return ok
	 */
	public void seleccionarEjecucion(TiempoPlanificacion aTiempo, Planificacion aPlanificacion) {

		SimpleDateFormat formatoFecha = new SimpleDateFormat("MMMM 'de' yyyy");
		if (aTiempo.isEjecutado()) {
			this.mostrarMensajeGlobalPersonalizado("MARCADO COMO EJECUTADO: Semana " + aTiempo.getNumeroSemana() + " del mes de " + formatoFecha.format(aTiempo.getFecha()) + " de la actividad " + aPlanificacion.getTareaProyecto().getTarea(), "exito");
		} else {
			this.mostrarMensajeGlobalPersonalizado("DESMARCADO COMO EJECUTADO: Semana " + aTiempo.getNumeroSemana() + " del mes de " + formatoFecha.format(aTiempo.getFecha()) + " de la actividad sleccionada", "exito");
		}

	}

	/**
	 * Selecciona la programaci�n
	 * 
	 * @param aPlanificacion
	 * @param aNumero
	 * @param aFecha
	 * @return ok
	 */
	public void seleccionarProgramacion(TiempoPlanificacion aTiempo, Planificacion aPlanificacion) {

		SimpleDateFormat formatoFecha = new SimpleDateFormat("MMMM 'de' yyyy");
		if (aTiempo.isProgramado()) {
			this.mostrarMensajeGlobalPersonalizado("PROGRAMADO: Semana " + aTiempo.getNumeroSemana() + " del mes de " + formatoFecha.format(aTiempo.getFecha()) + " de la actividad " + aPlanificacion.getTareaProyecto().getTarea(), "exito");
		} else {
			this.mostrarMensajeGlobalPersonalizado("DESPROGRAMADO: Semana " + aTiempo.getNumeroSemana() + " del mes de " + formatoFecha.format(aTiempo.getFecha()) + " de la actividad sleccionada", "exito");
		}

	}

	public boolean isExistenteCompromiso() {
		List<Compromiso> compromisos = null;
		try {
			Compromiso comConsulta = new Compromiso();
			comConsulta.getProyectoCliente().setId(this.proyectoCliente.getId());
			compromisos = IConsultasDAO.getCompromisos(comConsulta);
		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}

		return compromisos != null && compromisos.size() > 0 ? true : false;
	}

	public boolean isExistenteCronograma() {
		List<Cronograma> cronogramas = null;
		try {
			Cronograma cronogramaConsulta = new Cronograma();
			cronogramaConsulta.getProyectoCliente().setId(this.proyectoCliente.getId());
			cronogramas = IConsultasDAO.getCronograma(cronogramaConsulta);
		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}

		return cronogramas != null && cronogramas.size() > 0 ? true : false;
	}

	public boolean isExistenteDocumentacion() {
		List<Documentacion> documentaciones = null;
		try {
			Documentacion documentacionConsulta = new Documentacion();
			documentacionConsulta.getProyectoCliente().setId(this.proyectoCliente.getId());
			documentaciones = IConsultasDAO.getDocumentacion(documentacionConsulta);
		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}

		return documentaciones != null && documentaciones.size() > 0 ? true : false;
	}

	/**
	 * Detremina si una planificaci�n est� o no registrada
	 * 
	 * @return true:false
	 */
	public boolean isExistentePlanificacion() {
		List<Planificacion> planificaciones = null;
		try {
			Planificacion planificacionConsulta = new Planificacion();
			planificacionConsulta.getProyectoCliente().setId(this.proyectoCliente.getId());
			planificaciones = IConsultasDAO.getPlanificacion(planificacionConsulta);
		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}

		return planificaciones != null && planificaciones.size() > 0 ? true : false;
	}

	/**
	 * Determina si un diagn�stico est� o no incompleto
	 * 
	 * @return ok
	 */
	public boolean isDiagnosticoCompleto() {
		boolean ok = true;
		if (this.diagnostico != null && this.diagnostico.size() > 0 && this.diagnostico.stream().anyMatch(d -> (!(d.getEvidenciaEncontrada() != null && !d.getEvidenciaEncontrada().trim().equals("")) || (!(d.gettEstadosDiagnostico() != null && d.gettEstadosDiagnostico().size() > 0 && d.gettEstadosDiagnostico().stream().anyMatch(p -> p.istSeleccionado())))))) {
			ok = false;
		}
		return ok;
	}

	/**
	 * Imprime el estado de las tareas
	 */
	public void imprimirEstadosTareas() {
		try {

			// para que no env�e correo siempre
			actualizarDiagnostico(IConstantes.FORMA_INCOMPLETA);

			HacerMantenimiento hacerMantenimiento = new HacerMantenimiento();
			Map<String, Object> parametros = new HashMap<String, Object>();

			agregarNumeroVecesEstado();

			parametros.put("SUBREPORT_DIR", this.getPath(IConstantes.PAQUETE_MODULO_REPORTES) + "/");
			parametros.put("pEstadosProyecto", this.proyectoCliente.gettEstadosProyecto());
			parametros.put("pSistema", this.getMensaje("nombreSoftware"));
			parametros.put("pModulo", this.getMensaje("consultoriaEn", this.proyectoCliente.getProyecto().getNombre()));
			parametros.put("pCasoUso", this.getMensaje("consultorAsignado", this.proyectoCliente.getConsultor().getNombres() + " " + this.proyectoCliente.getConsultor().getApellidos()));
			parametros.put("pTituloAdicional", this.getMensaje("vigenciaCosnultoria", hacerMantenimiento.getTextoVigenciaProyecto(this.proyectoCliente)));
			parametros.put("pRutaLogo", this.getPath(IConstantes.PAQUETE_IMAGENES + IConstantes.LOGO1));

			if (!isDiagnosticoCompleto()) {
				parametros.put("pParcial", IConstantes.AFIRMACION);
			}

			this.generarListado(new JRBeanCollectionDataSource(this.planificacion), "imprimirTarreasPlanificacion.jasper", "ESTADO_TAREAS_PLANIFICACION", null, parametros);

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
	}

	/**
	 * Guarda en disco como imagen una firma signature
	 * 
	 * @param aSignature
	 * @param aNombreFirma
	 */
	public void guardarFirmaComoImagen(String aSignature, String aNombreFirma) {
		try {
			FirmaComoImagen firma = new FirmaComoImagen();
			byte[] archivo = firma.getImagenComoByte(aSignature);
			File outputfile = new File(this.getPath(IConstantes.PAQUETE_IMAGENES) + "/fotosFirmas/" + aNombreFirma + ".png");

			BufferedImage img = ImageIO.read(new ByteArrayInputStream(archivo));
			if (img != null) {
				ImageIO.write(img, "png", outputfile);
			}

		} catch (Exception e) {
			// IConstantes.log.error(e, e);

		}
	}

	/**
	 * Obtiene la decisi�n a tomar de imprimir
	 * 
	 * @param aDecision
	 */
	public void decidirReporte(String aDecision) {

		if (aDecision != null && aDecision.equals("G")) {
			// NUEVO
			this.personaDiagnostico.settTipoReporte("G");

		} else if (aDecision != null && aDecision.equals("P")) {
			// NUEVO PLAN DE ACCION
			this.personaDiagnostico.settTipoReporte("P");

		} else {
			// LISTA CHEQUEO
			this.personaDiagnostico.settTipoReporte("L");

			// CON NO APLICA O SIN NO APLICA
			// N o S
			this.personaDiagnostico.settAperece(aDecision);

		}

		imprimirDiagnosticoV1();

	}

	public void imprimirCompromisos() {
		try {

			String reporte = "imprimirInformCompromiso.jasper";
			SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
			SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
			// firma del representante del cliente
			if (this.compromisoGeneral.getFirma() != null) {
				this.guardarFirmaComoImagen(this.compromisoGeneral.getFirma(), "cliente_compromiso" + this.proyectoCliente.getId());
			}

			Map<String, Object> parametros = new HashMap<String, Object>();

			parametros.put("SUBREPORT_DIR", this.getPath(IConstantes.PAQUETE_MODULO_REPORTES) + "/");
			parametros.put("rutaFirmas", this.getPath(IConstantes.PAQUETE_IMAGENES) + "/fotosFirmas/");
			parametros.put("pProyectoCliente", this.proyectoCliente);
			parametros.put("pCliente", this.proyectoCliente.getCliente());

			// maneja la misma variable pero es el compromiso
			parametros.put("pCronogramaGeneral", this.compromisoGeneral);
			parametros.put("pNombreCompletoConsultor", this.proyectoCliente.getConsultor().getNombres() + " " + this.proyectoCliente.getConsultor().getApellidos());
			parametros.put("pRutaLogo", this.getPath(IConstantes.PAQUETE_IMAGENES + IConstantes.LOGO1));

			for (Compromiso c : this.compromisos) {
				c.settRangoFechas(formato.format(c.getFechaInicioCompromiso()) + "-" + formato.format(c.getFechaFinCompromiso()));

			}

			this.generarListado(new JRBeanCollectionDataSource(this.compromisos), reporte, "INFORME-" + formatoFecha.format(new Date()), null, parametros);

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
	}

	public void imprimirDocumentacion() {
		try {

			String reporte = "imprimirInformeDocumentacion.jasper";
			SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
			SimpleDateFormat formatoFecha2 = new SimpleDateFormat("dd/MM/yyyy");
			// firma del representante del cliente
			if (this.documentacionGeneral.getFirma() != null) {
				this.guardarFirmaComoImagen(this.documentacionGeneral.getFirma(), "cliente_documentacion" + this.proyectoCliente.getId());
			}

			Map<String, Object> parametros = new HashMap<String, Object>();

			parametros.put("SUBREPORT_DIR", this.getPath(IConstantes.PAQUETE_MODULO_REPORTES) + "/");
			parametros.put("rutaFirmas", this.getPath(IConstantes.PAQUETE_IMAGENES) + "/fotosFirmas/");
			parametros.put("pProyectoCliente", this.proyectoCliente);
			parametros.put("pCliente", this.proyectoCliente.getCliente());
			parametros.put("pCronogramaGeneral", this.documentacionGeneral);
			parametros.put("pNombreCompletoConsultor", this.proyectoCliente.getConsultor().getNombres() + " " + this.proyectoCliente.getConsultor().getApellidos());
			parametros.put("pRutaLogo", this.getPath(IConstantes.PAQUETE_IMAGENES + IConstantes.LOGO1));

			for (Documentacion d : this.documentaciones) {
				d.settDocumentos(new ArrayList<DocumentoActividad>());
				DocumentoActividad doc = new DocumentoActividad();
				doc.getTareaProyecto().setId(d.getTareaProyecto().getId());

				doc.getProyectoCliente().setId(this.proyectoCliente.getId());

				d.settDocumentos(IConsultasDAO.getDocumentos(doc));
				if (d.gettDocumentos() != null && d.gettDocumentos().size() > 0) {
					for (DocumentoActividad da : d.gettDocumentos()) {
						if (da.getFecha() != null) {
							da.setNombre(da.getNombre() + " (" + formatoFecha2.format(da.getFecha()) + ")");
						}
					}
				}

			}

			this.generarListado(new JRBeanCollectionDataSource(this.documentaciones), reporte, "INFORME-" + formatoFecha.format(new Date()), null, parametros);

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
	}

	public void imprimirCronograma() {
		try {

			String reporte = "imprimirInformCronograma.jasper";
			SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
			// firma del representante del cliente
			if (this.cronogramaGeneral.getFirma() != null) {
				this.guardarFirmaComoImagen(this.cronogramaGeneral.getFirma(), "cliente_cronograma" + this.proyectoCliente.getId());
			}

			Map<String, Object> parametros = new HashMap<String, Object>();

			parametros.put("SUBREPORT_DIR", this.getPath(IConstantes.PAQUETE_MODULO_REPORTES) + "/");
			parametros.put("rutaFirmas", this.getPath(IConstantes.PAQUETE_IMAGENES) + "/fotosFirmas/");
			parametros.put("pProyectoCliente", this.proyectoCliente);
			parametros.put("pCliente", this.proyectoCliente.getCliente());
			parametros.put("pCronogramaGeneral", this.cronogramaGeneral);
			parametros.put("pNombreCompletoConsultor", this.proyectoCliente.getConsultor().getNombres() + " " + this.proyectoCliente.getConsultor().getApellidos());
			parametros.put("pRutaLogo", this.getPath(IConstantes.PAQUETE_IMAGENES + IConstantes.LOGO1));

			this.generarListado(new JRBeanCollectionDataSource(this.cronogramas), reporte, "INFORME-" + formatoFecha.format(new Date()), null, parametros);

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
	}

	public void imprimirIndicadoresGlobal() {
		try {
			List<Indicador> indicadoresImpresion = new ArrayList<Indicador>();
			String reporte = "imprimirInformeIndicadoresGlobal.jasper";
			SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
			// firma del representante del cliente
			this.guardarFirmaComoImagen(this.informacionGeneralIndicador.getFirma(), "cliente_indicador" + this.proyectoCliente.getId());

			Map<String, Object> parametros = new HashMap<String, Object>();

			for (ObjetivoEtapaIndicador o : this.informacionGeneralIndicador.getObjetivos()) {
				for (Indicador i : o.getIndicadores()) {
					i.setObjetivo(new ObjetivoEtapaIndicador());
					o.settResultadoObjetivo(getResultadoObjetivo(o));
					i.setObjetivo(o);
					i.settTextoFrecuencia(getTextoFrecuencia(i));
					i.settSumatoriasResultados(getResultadoIndicador(i));
					indicadoresImpresion.add(i);

				}
			}

			parametros.put("nombrePeriodo_1", (getMesAnoF2(getFechaPeriodo(1))));
			parametros.put("nombrePeriodo_2", (getMesAnoF2(getFechaPeriodo(2))));
			parametros.put("nombrePeriodo_3", (getMesAnoF2(getFechaPeriodo(3))));
			parametros.put("nombrePeriodo_4", (getMesAnoF2(getFechaPeriodo(4))));
			parametros.put("nombrePeriodo_5", (getMesAnoF2(getFechaPeriodo(5))));
			parametros.put("nombrePeriodo_6", (getMesAnoF2(getFechaPeriodo(6))));
			parametros.put("nombrePeriodo_7", (getMesAnoF2(getFechaPeriodo(7))));
			parametros.put("nombrePeriodo_8", (getMesAnoF2(getFechaPeriodo(8))));
			parametros.put("nombrePeriodo_9", (getMesAnoF2(getFechaPeriodo(9))));
			parametros.put("nombrePeriodo_10", (getMesAnoF2(getFechaPeriodo(10))));
			parametros.put("nombrePeriodo_11", (getMesAnoF2(getFechaPeriodo(11))));
			parametros.put("nombrePeriodo_12", (getMesAnoF2(getFechaPeriodo(12))));

			parametros.put("SUBREPORT_DIR", this.getPath(IConstantes.PAQUETE_MODULO_REPORTES) + "/");
			parametros.put("rutaFirmas", this.getPath(IConstantes.PAQUETE_IMAGENES) + "/fotosFirmas/");
			parametros.put("pProyectoCliente", this.proyectoCliente);
			parametros.put("pCliente", this.proyectoCliente.getCliente());
			parametros.put("pInformacionIndicador", this.informacionGeneralIndicador);
			parametros.put("pNombreCompletoConsultor", this.proyectoCliente.getConsultor().getNombres() + " " + this.proyectoCliente.getConsultor().getApellidos());
			parametros.put("pRutaLogo", this.getPath(IConstantes.PAQUETE_IMAGENES + IConstantes.LOGO1));

			this.generarListado(new JRBeanCollectionDataSource(indicadoresImpresion), reporte, "INFORME-" + formatoFecha.format(new Date()), null, parametros);

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
	}

	public void imprimirIndicadores() {
		try {
			List<Indicador> indicadoresImpresion = new ArrayList<Indicador>();
			String reporte = "imprimirInformeIndicadores.jasper";
			SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
			// firma del representante del cliente
			this.guardarFirmaComoImagen(this.informacionGeneralIndicador.getFirma(), "cliente_indicador" + this.proyectoCliente.getId());

			Map<String, Object> parametros = new HashMap<String, Object>();

			for (ObjetivoEtapaIndicador o : this.informacionGeneralIndicador.getObjetivos()) {
				for (Indicador i : o.getIndicadores()) {
					i.setObjetivo(new ObjetivoEtapaIndicador());
					i.setObjetivo(o);
					i.settTextoFrecuencia(getTextoFrecuencia(i));

					i.settPlanesAccion(null);
					if (!isTodosPeriodosNulos(i) && isTomarPlanAccion(i)) {
						i.settPlanesAccion(new ArrayList<PlanAccionIndicador>());
						if (i.getPlanesAccionIndicador() != null && i.getPlanesAccionIndicador().size() > 0) {
							for (PlanAccionIndicador p : i.getPlanesAccionIndicador()) {
								if (p.getActividad() != null && !p.getActividad().trim().equals("") && p.getResponsable() != null && !p.getResponsable().trim().equals("") && p.getFecha() != null) {
									i.gettPlanesAccion().add(p);
								}

							}

						}
					}

					i.settResultadosPeriodos(new ArrayList<Indicador>());

					if (getColorCeldaPeriodo(i, 1).equals("NORMAL")) {
						Indicador ii = new Indicador();
						ii.settNombrePeriodo(getMesAnoF2(getFechaPeriodo(1)));
						ii.settNumerador(i.getNumerador1());
						ii.settDenominador(i.getDenominador1());
						ii.settResultado1(i.getPeriodo1());

						ii.settColorTexto(getColorTextoPeriodo(i, i.getPeriodo1()));
						i.gettResultadosPeriodos().add(ii);
					}

					if (getColorCeldaPeriodo(i, 2).equals("NORMAL")) {
						Indicador ii = new Indicador();
						ii.settNombrePeriodo(getMesAnoF2(getFechaPeriodo(2)));
						ii.settNumerador(i.getNumerador2());
						ii.settDenominador(i.getDenominador2());
						ii.settResultado1(i.getPeriodo2());

						ii.settColorTexto(getColorTextoPeriodo(i, i.getPeriodo2()));
						i.gettResultadosPeriodos().add(ii);
					}

					if (getColorCeldaPeriodo(i, 3).equals("NORMAL")) {
						Indicador ii = new Indicador();
						ii.settNombrePeriodo(getMesAnoF2(getFechaPeriodo(3)));
						ii.settNumerador(i.getNumerador3());
						ii.settDenominador(i.getDenominador3());
						ii.settResultado1(i.getPeriodo3());

						ii.settColorTexto(getColorTextoPeriodo(i, i.getPeriodo3()));
						i.gettResultadosPeriodos().add(ii);
					}

					if (getColorCeldaPeriodo(i, 4).equals("NORMAL")) {
						Indicador ii = new Indicador();
						ii.settNombrePeriodo(getMesAnoF2(getFechaPeriodo(4)));
						ii.settNumerador(i.getNumerador4());
						ii.settDenominador(i.getDenominador4());
						ii.settResultado1(i.getPeriodo4());

						ii.settColorTexto(getColorTextoPeriodo(i, i.getPeriodo4()));
						i.gettResultadosPeriodos().add(ii);
					}

					if (getColorCeldaPeriodo(i, 5).equals("NORMAL")) {
						Indicador ii = new Indicador();
						ii.settNombrePeriodo(getMesAnoF2(getFechaPeriodo(5)));
						ii.settNumerador(i.getNumerador5());
						ii.settDenominador(i.getDenominador5());
						ii.settResultado1(i.getPeriodo5());

						ii.settColorTexto(getColorTextoPeriodo(i, i.getPeriodo5()));
						i.gettResultadosPeriodos().add(ii);
					}

					if (getColorCeldaPeriodo(i, 6).equals("NORMAL")) {
						Indicador ii = new Indicador();
						ii.settNombrePeriodo(getMesAnoF2(getFechaPeriodo(6)));
						ii.settNumerador(i.getNumerador6());
						ii.settDenominador(i.getDenominador6());
						ii.settResultado1(i.getPeriodo6());

						ii.settColorTexto(getColorTextoPeriodo(i, i.getPeriodo6()));
						i.gettResultadosPeriodos().add(ii);
					}

					if (getColorCeldaPeriodo(i, 7).equals("NORMAL")) {
						Indicador ii = new Indicador();
						ii.settNombrePeriodo(getMesAnoF2(getFechaPeriodo(7)));
						ii.settNumerador(i.getNumerador7());
						ii.settDenominador(i.getDenominador7());
						ii.settResultado1(i.getPeriodo7());

						ii.settColorTexto(getColorTextoPeriodo(i, i.getPeriodo7()));
						i.gettResultadosPeriodos().add(ii);
					}

					if (getColorCeldaPeriodo(i, 8).equals("NORMAL")) {
						Indicador ii = new Indicador();
						ii.settNombrePeriodo(getMesAnoF2(getFechaPeriodo(8)));
						ii.settNumerador(i.getNumerador8());
						ii.settDenominador(i.getDenominador8());
						ii.settResultado1(i.getPeriodo8());

						ii.settColorTexto(getColorTextoPeriodo(i, i.getPeriodo8()));
						i.gettResultadosPeriodos().add(ii);
					}

					if (getColorCeldaPeriodo(i, 9).equals("NORMAL")) {
						Indicador ii = new Indicador();
						ii.settNombrePeriodo(getMesAnoF2(getFechaPeriodo(9)));
						ii.settNumerador(i.getNumerador9());
						ii.settDenominador(i.getDenominador9());
						ii.settResultado1(i.getPeriodo9());

						ii.settColorTexto(getColorTextoPeriodo(i, i.getPeriodo9()));
						i.gettResultadosPeriodos().add(ii);
					}

					if (getColorCeldaPeriodo(i, 10).equals("NORMAL")) {
						Indicador ii = new Indicador();
						ii.settNombrePeriodo(getMesAnoF2(getFechaPeriodo(10)));
						ii.settNumerador(i.getNumerador10());
						ii.settDenominador(i.getDenominador10());
						ii.settResultado1(i.getPeriodo10());

						ii.settColorTexto(getColorTextoPeriodo(i, i.getPeriodo10()));
						i.gettResultadosPeriodos().add(ii);
					}

					if (getColorCeldaPeriodo(i, 11).equals("NORMAL")) {
						Indicador ii = new Indicador();
						ii.settNombrePeriodo(getMesAnoF2(getFechaPeriodo(11)));
						ii.settNumerador(i.getNumerador11());
						ii.settDenominador(i.getDenominador11());
						ii.settResultado1(i.getPeriodo11());

						ii.settColorTexto(getColorTextoPeriodo(i, i.getPeriodo11()));
						i.gettResultadosPeriodos().add(ii);
					}

					if (getColorCeldaPeriodo(i, 12).equals("NORMAL")) {
						Indicador ii = new Indicador();
						ii.settNombrePeriodo(getMesAnoF2(getFechaPeriodo(12)));
						ii.settNumerador(i.getNumerador12());
						ii.settDenominador(i.getDenominador12());
						ii.settResultado1(i.getPeriodo12());

						ii.settColorTexto(getColorTextoPeriodo(i, i.getPeriodo12()));
						i.gettResultadosPeriodos().add(ii);
					}
					i.settSumatoriasResultados(getResultadoIndicador(i));
					indicadoresImpresion.add(i);

				}

			}

			parametros.put("SUBREPORT_DIR", this.getPath(IConstantes.PAQUETE_MODULO_REPORTES) + "/");
			parametros.put("rutaFirmas", this.getPath(IConstantes.PAQUETE_IMAGENES) + "/fotosFirmas/");
			parametros.put("pProyectoCliente", this.proyectoCliente);
			parametros.put("pCliente", this.proyectoCliente.getCliente());
			parametros.put("pInformacionIndicador", this.informacionGeneralIndicador);
			parametros.put("pNombreCompletoConsultor", this.proyectoCliente.getConsultor().getNombres() + " " + this.proyectoCliente.getConsultor().getApellidos());
			parametros.put("pRutaLogo", this.getPath(IConstantes.PAQUETE_IMAGENES + IConstantes.LOGO1));

			this.generarListado(new JRBeanCollectionDataSource(indicadoresImpresion), reporte, "INFORME-" + formatoFecha.format(new Date()), null, parametros);

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
	}

	/**
	 * Imprime el diagnostico
	 */
	public void imprimirDiagnosticoV1() {
		try {
			List<Diagnostico> diagnosticoImpresion = new ArrayList<Diagnostico>();
			String reporte = "";
			SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
			// firma del representante del cliente
			if (this.personaDiagnostico.getFirma() != null) {
				this.guardarFirmaComoImagen(this.personaDiagnostico.getFirma(), "cliente" + this.proyectoCliente.getId());
			}

			Map<String, Object> parametros = new HashMap<String, Object>();
			int sw = 0;
			int swNuevo = 0;
			int swnNoConforme = 0;

			for (Diagnostico d : this.diagnostico) {

				d.getPreguntaProyecto().settFortaleza("N");
				d.getPreguntaProyecto().settRecomendacion("N");
				d.getPreguntaProyecto().settNoConformidad("N");
				d.getPreguntaProyecto().settNoAplica("N");
				sw = 0;
				swNuevo = 0;
				swnNoConforme = 0;
				d.settHallazgoSeleccionado(null);
				for (EstadoDiagnostico e : d.gettEstadosDiagnostico()) {
					if (e.istSeleccionado() && e.getEstado().getId().intValue() == 7) {
						// fortaleza
						d.getPreguntaProyecto().settFortaleza("S");
						d.settHallazgoSeleccionado("1");

					}
					if (e.istSeleccionado() && e.getEstado().getId().intValue() == 8) {
						// recomedacion
						d.getPreguntaProyecto().settRecomendacion("S");

						d.settHallazgoSeleccionado("2");

						swnNoConforme = 1; // COMO SE HABILITA PARA PLAN DE ACCION TAMBIEN
																// RECOMENDACION LO MANEJA ESTA BANDERA
						
						d.settTipo("ABIERTA");
					}
					if (e.istSeleccionado() && e.getEstado().getId().intValue() == 9) {
						// noconformidad
						d.getPreguntaProyecto().settNoConformidad("S");

						d.settHallazgoSeleccionado("3");
						swnNoConforme = 1;
						d.settTipo("CERRADA");

					}
					if (e.istSeleccionado() && e.getEstado().getId().intValue() == 6) {
						// no aplica
						d.getPreguntaProyecto().settNoAplica("S");
						d.settHallazgoSeleccionado("4");
						sw = 1;

					}
					if (e.istSeleccionado() && e.getEstado().getId().intValue() == 10) {
						// cumple
						d.getPreguntaProyecto().settCumple("S");
						d.settHallazgoSeleccionado("5");
						swNuevo = 1;

					}
				}
				if (this.personaDiagnostico.gettTipoReporte() != null && this.personaDiagnostico.gettTipoReporte().equals("G")) {
					if (sw == 0 && swNuevo == 0) {
						diagnosticoImpresion.add(d);
					}
				} else if (this.personaDiagnostico.gettTipoReporte() != null && this.personaDiagnostico.gettTipoReporte().equals("P")) {
					if (swnNoConforme == 1) {
						// plan de accion solo no conformes Y RECOMEDACIONES
						diagnosticoImpresion.add(d);
					}
				} else {
					if (this.personaDiagnostico.gettAperece() != null && this.personaDiagnostico.gettAperece().equals("N")) {
						if (sw == 0) {
							diagnosticoImpresion.add(d);
						}
					} else {

						diagnosticoImpresion.add(d);
					}
				}

			}

			List<EstadoProyectoCliente> estadosGrafica = new ArrayList<EstadoProyectoCliente>();
			List<EstadoProyectoCliente> estadosGraficaSinNoAplica = new ArrayList<EstadoProyectoCliente>();
			EstadoProyectoCliente estadoGrafica = null;
			if (this.proyectoCliente != null && this.proyectoCliente.gettEstadosProyecto() != null && this.proyectoCliente.gettEstadosProyecto().size() > 0) {
				int numeroEstados = 0;
				for (EstadoProyectoCliente e : this.proyectoCliente.gettEstadosProyecto()) {
					numeroEstados = 0;
					for (Diagnostico d : this.diagnostico) {
						numeroEstados += d.gettEstadosDiagnostico().stream().filter(ed -> ed.istSeleccionado() && ed.getEstado().getId().intValue() == e.getEstado().getId().intValue()).count();
					}
					estadoGrafica = new EstadoProyectoCliente();
					estadoGrafica.getEstado().setNombre(e.getEstado().getNombre());
					estadoGrafica.settNumeroVeces(numeroEstados);

					estadosGrafica.add(estadoGrafica);
					if (e.getEstado().getId().intValue() != IConstantes.ID_ESTADO_NO_APLICA.intValue() && e.getEstado().getId().intValue() != IConstantes.ID_ESTADO_CUMPLE.intValue()) {
						estadosGraficaSinNoAplica.add(estadoGrafica);
					}

				}

			}

			if (this.personaDiagnostico.gettTipoReporte() != null && this.personaDiagnostico.gettTipoReporte().equals("G")) {
				reporte = "imprimirInformeDiagnosticoV1G.jasper";
				// el nuevo reporte
				// ORDENA

				Collections.sort(diagnosticoImpresion, (o1, o2) -> o1.gettHallazgoSeleccionado().compareTo(o2.gettHallazgoSeleccionado()));

				parametros.put("pEstadosGrafica", estadosGraficaSinNoAplica);

			} else if (this.personaDiagnostico.gettTipoReporte() != null && this.personaDiagnostico.gettTipoReporte().equals("P")) {
				reporte = "imprimirInformePlanAccionDiagnostico.jasper";
				// el nuevo reporte PLAN DE ACCION

			} else {
				reporte = "imprimirInformeDiagnosticoV1.jasper";
				// el anterior

				parametros.put("pEstadosGrafica", estadosGrafica);

			}

			parametros.put("SUBREPORT_DIR", this.getPath(IConstantes.PAQUETE_MODULO_REPORTES) + "/");
			parametros.put("rutaFirmas", this.getPath(IConstantes.PAQUETE_IMAGENES) + "/fotosFirmas/");
			parametros.put("pProyectoCliente", this.proyectoCliente);
			parametros.put("pCliente", this.proyectoCliente.getCliente());

			parametros.put("pParametroAuditoria", IConsultasDAO.getParametroAuditoria());
			parametros.put("pPersonaDiagnostico", this.personaDiagnostico);
			parametros.put("pNombreCompletoConsultor", this.proyectoCliente.getConsultor().getNombres() + " " + this.proyectoCliente.getConsultor().getApellidos());
			parametros.put("pRutaLogo", this.getPath(IConstantes.PAQUETE_IMAGENES + IConstantes.LOGO1));

			this.generarListado(new JRBeanCollectionDataSource(diagnosticoImpresion), reporte, "INFORME-" + formatoFecha.format(new Date()), null, parametros);

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
	}
	
	

	

	/**
	 * Imprime el diagnostico
	 */
	public void imprimirDiagnostico() {
		try {

			// para que no env�e correo siempre
			actualizarDiagnostico(IConstantes.FORMA_INCOMPLETA);

			HacerMantenimiento hacerMantenimiento = new HacerMantenimiento();
			Map<String, Object> parametros = new HashMap<String, Object>();

			agregarNumeroVecesEstado();

			parametros.put("SUBREPORT_DIR", this.getPath(IConstantes.PAQUETE_MODULO_REPORTES) + "/");
			parametros.put("pEstadosProyecto", this.proyectoCliente.gettEstadosProyecto());
			parametros.put("pSistema", this.getMensaje("nombreSoftware"));
			parametros.put("pModulo", this.getMensaje("consultoriaEn", this.proyectoCliente.getProyecto().getNombre()));
			parametros.put("pCasoUso", this.getMensaje("consultorAsignado", this.proyectoCliente.getConsultor().getNombres() + " " + this.proyectoCliente.getConsultor().getApellidos()));
			parametros.put("pTituloAdicional", this.getMensaje("vigenciaCosnultoria", hacerMantenimiento.getTextoVigenciaProyecto(this.proyectoCliente)));
			parametros.put("pRutaLogo", this.getPath(IConstantes.PAQUETE_IMAGENES + IConstantes.LOGO1));

			if (!isDiagnosticoCompleto()) {
				parametros.put("pParcial", IConstantes.AFIRMACION);
			}

			this.generarListado(new JRBeanCollectionDataSource(this.diagnostico), IConstantes.REPORTE_DIAGNOSTICO, IConstantes.NOMBRE_REPORTE_DIAGNOSTICO, null, parametros);

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
	}

	/**
	 * Determina si existe ya un diagn�stico previo guardado
	 * 
	 * @return existente
	 */
	public boolean isDiagnosticoExistente() {
		boolean existente = false;
		Diagnostico diagnostico = new Diagnostico();
		diagnostico.getProyectoCliente().setId(this.proyectoCliente.getId());
		try {
			List<Diagnostico> diagnosticos = IConsultasDAO.getDiagnostico(diagnostico);
			if (diagnosticos != null && diagnosticos.size() > 0) {
				existente = true;
			}

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
		return existente;
	}

	/**
	 * Elimina la planificaci�n
	 */
	public void eliminarPlanificacion() {

		Conexion conexion = new Conexion();
		Map<String, Object> condiciones = null;

		Planificacion planificacionExistente = null;
		PlanificacionGeneral planificacionGeneralExistente = null;

		try {
			conexion.setAutoCommitBD(false);

			condiciones = new HashMap<String, Object>();

			// elimina los tiempos registrados

			if (this.planificacion != null && this.planificacion.size() > 0) {
				for (Planificacion p : this.planificacion) {
					TiempoPlanificacion tiempo = new TiempoPlanificacion();
					condiciones = new HashMap<String, Object>();
					condiciones.put("id_planificacion", p.getId());
					tiempo.getCamposBD();
					conexion.eliminarBD(tiempo.getEstructuraTabla().getTabla(), condiciones);
				}

			}

			// elimina la planificacion
			planificacionExistente = new Planificacion();
			condiciones = new HashMap<String, Object>();
			condiciones.put("id_proyecto_cliente", this.proyectoCliente.getId());
			planificacionExistente.getCamposBD();
			conexion.eliminarBD(planificacionExistente.getEstructuraTabla().getTabla(), condiciones);

			planificacionGeneralExistente = new PlanificacionGeneral();
			condiciones = new HashMap<String, Object>();
			condiciones.put("id_proyecto_cliente", this.proyectoCliente.getId());
			planificacionGeneralExistente.getCamposBD();
			conexion.eliminarBD(planificacionGeneralExistente.getEstructuraTabla().getTabla(), condiciones);

			conexion.commitBD();
			this.mostrarMensajeGlobal("planificacionEliminada", "exito");

			this.planificacion = null;
			this.armarEstructuraPlanificacion();

		} catch (Exception e) {
			conexion.rollbackBD();
			this.mostrarMensajeGlobal("transaccionFallida", "error");
		} finally {
			conexion.cerrarConexion();
		}
	}

	public void eliminarCompromiso() {

		Conexion conexion = new Conexion();
		Map<String, Object> condiciones = null;
		try {
			conexion.setAutoCommitBD(false);

			Compromiso compromiso = new Compromiso();
			condiciones = new HashMap<String, Object>();
			condiciones.put("id_proyecto_cliente", this.proyectoCliente.getId());
			compromiso.getCamposBD();
			conexion.eliminarBD(compromiso.getEstructuraTabla().getTabla(), condiciones);

			CompromisoGeneral compromisoGeneral = new CompromisoGeneral();
			condiciones = new HashMap<String, Object>();
			condiciones.put("id_proyecto_cliente", this.proyectoCliente.getId());
			compromisoGeneral.getCamposBD();
			conexion.eliminarBD(compromisoGeneral.getEstructuraTabla().getTabla(), condiciones);

			conexion.commitBD();
			this.mostrarMensajeGlobalPersonalizado("Compromisos eliminados con éxito. Puede comenzar de nuevo!", "exito");

			this.compromisoGeneral = null;
			this.getCompromisoGeneral();
			this.compromisos = null;
			this.getCompromisos();
			this.armarEstructuraCompromiso();

		} catch (Exception e) {
			conexion.rollbackBD();
			this.mostrarMensajeGlobal("transaccionFallida", "error");
		} finally {
			conexion.cerrarConexion();
		}
	}

	public void eliminarCronograma() {

		Conexion conexion = new Conexion();
		Map<String, Object> condiciones = null;
		try {
			conexion.setAutoCommitBD(false);

			// elimina los documentos del cronograma
			for (Cronograma c : cronogramas) {
				DocumentoCronograma documento = new DocumentoCronograma();
				condiciones = new HashMap<String, Object>();
				condiciones.put("id_cronograma", c.getId());
				documento.getCamposBD();
				conexion.eliminarBD(documento.getEstructuraTabla().getTabla(), condiciones);
			}

			// Elimina lso cronogramas guardados
			Cronograma cronograma = new Cronograma();
			condiciones = new HashMap<String, Object>();
			condiciones.put("id_proyecto_cliente", this.proyectoCliente.getId());
			cronograma.getCamposBD();
			conexion.eliminarBD(cronograma.getEstructuraTabla().getTabla(), condiciones);

			// elimina el cronograma general
			CronogramaGeneral cronogramaGeneral = new CronogramaGeneral();
			condiciones = new HashMap<String, Object>();
			condiciones.put("id_proyecto_cliente", this.proyectoCliente.getId());
			cronogramaGeneral.getCamposBD();
			conexion.eliminarBD(cronogramaGeneral.getEstructuraTabla().getTabla(), condiciones);

			conexion.commitBD();
			this.mostrarMensajeGlobalPersonalizado("PLANIFICACION DETALLADA E IMPLEMENTACION ELIMINADAS CON EXITO", "exito");

			this.cronogramaGeneral = null;
			this.getCronogramaGeneral();
			this.cronogramas = null;
			this.getCronogramas();
			this.armarEstructuraCronograma();

		} catch (Exception e) {
			conexion.rollbackBD();
			this.mostrarMensajeGlobal("transaccionFallida", "error");
		} finally {
			conexion.cerrarConexion();
		}
	}

	public void eliminarDocumentacion() {

		Conexion conexion = new Conexion();
		Map<String, Object> condiciones = null;
		try {
			conexion.setAutoCommitBD(false);

			// Elimina los documentos guardados para ese proyecto cliente

			// Elimina los documentos en la tabla de documentos para ese cliente
			DocumentoActividad documentoActividad = new DocumentoActividad();
			condiciones = new HashMap<String, Object>();
			condiciones.put("id_proyecto_cliente", this.proyectoCliente.getId());
			documentoActividad.getCamposBD();
			conexion.eliminarBD(documentoActividad.getEstructuraTabla().getTabla(), condiciones);

			// Elimina la documentaci�n guardada
			Documentacion documentacion = new Documentacion();
			condiciones = new HashMap<String, Object>();
			condiciones.put("id_proyecto_cliente", this.proyectoCliente.getId());
			documentacion.getCamposBD();
			conexion.eliminarBD(documentacion.getEstructuraTabla().getTabla(), condiciones);

			// elimina la documentaci�n general
			DocumentacionGeneral documentacionGeneral = new DocumentacionGeneral();
			condiciones = new HashMap<String, Object>();
			condiciones.put("id_proyecto_cliente", this.proyectoCliente.getId());
			documentacionGeneral.getCamposBD();
			conexion.eliminarBD(documentacionGeneral.getEstructuraTabla().getTabla(), condiciones);

			conexion.commitBD();
			this.mostrarMensajeGlobalPersonalizado("Documentaci�n eliminada con �xito para el proyecto/cliente", "exito");

			this.documentacionGeneral = null;
			this.getDocumentacionGeneral();
			this.documentaciones = null;
			this.getDocumentaciones();
			this.armarEstructuraDocumentacion();

		} catch (Exception e) {
			conexion.rollbackBD();
			this.mostrarMensajeGlobal("transaccionFallida", "error");
		} finally {
			conexion.cerrarConexion();
		}
	}

	/**
	 * Elimina el diagnostico guardado
	 */
	public void eliminarDiagnostico() {

		Conexion conexion = new Conexion();
		Map<String, Object> condiciones = null;
		EstadoProyectoCliente estadoProyectoCliente = null;
		EstadoDiagnostico estadoDiagnostico = null;
		Diagnostico diagnosticoExistente = null;
		DocumentoDiagnostico documento = null;

		try {
			conexion.setAutoCommitBD(false);

			// Elimina Persona diagnostico

			PersonaDiagnostico persona = new PersonaDiagnostico();
			condiciones = new HashMap<String, Object>();
			condiciones.put("id_proyecto_cliente", this.proyectoCliente.getId());
			persona.getCamposBD();
			conexion.eliminarBD(persona.getEstructuraTabla().getTabla(), condiciones);

			// Elimina los estados del proyecto cliente
			estadoProyectoCliente = new EstadoProyectoCliente();
			condiciones = new HashMap<String, Object>();
			condiciones.put("id_proyecto_cliente", this.proyectoCliente.getId());
			estadoProyectoCliente.getCamposBD();
			conexion.eliminarBD(estadoProyectoCliente.getEstructuraTabla().getTabla(), condiciones);

			// Elimina los estados del diagnostico y tambien los documentos asociados
			for (Diagnostico d : this.diagnostico) {
				estadoDiagnostico = new EstadoDiagnostico();
				condiciones = new HashMap<String, Object>();
				condiciones.put("id_diagnostico", d.getId());
				estadoDiagnostico.getCamposBD();
				conexion.eliminarBD(estadoDiagnostico.getEstructuraTabla().getTabla(), condiciones);

				documento = new DocumentoDiagnostico();
				condiciones = new HashMap<String, Object>();
				condiciones.put("id_diagnostico", d.getId());
				documento.getCamposBD();
				conexion.eliminarBD(documento.getEstructuraTabla().getTabla(), condiciones);

			}

			// elimina el diagnostico
			diagnosticoExistente = new Diagnostico();
			condiciones = new HashMap<String, Object>();
			condiciones.put("id_proyecto_cliente", this.proyectoCliente.getId());
			diagnosticoExistente.getCamposBD();
			conexion.eliminarBD(diagnosticoExistente.getEstructuraTabla().getTabla(), condiciones);

			conexion.commitBD();
			this.mostrarMensajeGlobal("diagnosticoEliminado", "exito");

			this.diagnostico = null;
			this.armarEstructuraDiagnostico();

		} catch (Exception e) {
			conexion.rollbackBD();
			this.mostrarMensajeGlobal("transaccionFallida", "error");
		} finally {
			conexion.cerrarConexion();
		}
	}

	/**
	 * Verfica que posea documentaci�n, aunque solamente falta que muestre las
	 * que el consultor ingres�. Completar en la parte del comentario 1=2 las
	 * validaciones del que ingers� el propio ocnsultor
	 * 
	 * @return ok
	 */
	public boolean isValidacionesDocumentacion(TareaProyecto aTareaProyecto) {
		boolean ok = false;
		List<DocumentoActividad> docs = null;

		try {

			if ((aTareaProyecto != null && aTareaProyecto.getExplicacionDocumentacion() != null && !aTareaProyecto.getExplicacionDocumentacion().trim().equals(""))) {
				ok = true;
			}

			if (aTareaProyecto != null && aTareaProyecto.getId() != null) {
				DocumentoActividad doc = new DocumentoActividad();
				doc.getTareaProyecto().setId(aTareaProyecto.getId());
				docs = IConsultasDAO.getDocumentos(doc);

			} else {
				ok = false;

			}

			// Aqu� se valida tanto los documentos generales como los del
			// consultor
			if (ok && ((docs != null && docs.size() > 0))) {
				ok = true;
			} else {
				ok = false;
			}

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
		return ok;
	}

	public boolean isExisteDocumentos(TareaProyecto aTareaProyecto, String aRol) {
		boolean ok = false;
		List<DocumentoActividad> docs = null;

		try {

			if (aTareaProyecto != null && aTareaProyecto.getId() != null) {
				DocumentoActividad doc = new DocumentoActividad();
				doc.getTareaProyecto().setId(aTareaProyecto.getId());

				if (aRol != null && aRol.equals("CLIENTE") && this.proyectoCliente != null && this.proyectoCliente.getId() != null) {

					doc.getProyectoCliente().setId(this.proyectoCliente.getId());

				}

				docs = IConsultasDAO.getDocumentos(doc);

				if (docs != null && docs.size() > 0) {
					ok = true;
				} else {

					ok = false;
				}

			} else {
				ok = false;

			}

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
		return ok;
	}

	/**
	 * Verfica que posea documentaci�n
	 * 
	 * @return ok
	 */
	public boolean isPoseeDocumentacion() {

		boolean ok = false;

		try {

			if (this.planificacionSeleccionada != null && this.planificacionSeleccionada.getTareaProyecto() != null) {
				TareaProyecto aTareaProyecto = this.planificacionSeleccionada.getTareaProyecto();

				ok = isValidacionesDocumentacion(aTareaProyecto);

			} else {

				ok = false;
			}

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}

		return ok;

	}

	/**
	 * Actualiza la tarea planificada, env�a correos si es necesario
	 */
	public void actualizarTareaPlanificada() {

	}

	public void enviarCorreoDiagnostico() {
		try {
			IEmail.enviarCorreo(this.getMensaje("mensajeDiagnostico", this.proyectoCliente.getCliente().getCliente(), this.proyectoCliente.getProyecto().getNombre()), this.getMensaje("asuntoDiagnostico", this.proyectoCliente.getProyecto().getNombre()), this.proyectoCliente.getCliente().getCorreoElectronico());
			this.mostrarMensajeGlobalPersonalizado(this.getMensaje("correoDiagnostico", this.proyectoCliente.getCliente().getCorreoElectronico()), "exito");
		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
	}

	public void limpiarFirmaCompromiso() {
		this.compromisoGeneral.setFirma(null);
	}

	public void limpiarFirmaCronograma() {
		this.cronogramaGeneral.setFirma(null);
	}

	public void limpiarFirmaImplementacion() {
		this.cronogramaGeneral.setImplementacionFirma(null);
	}

	public void limpiarFirmaDocumentacion() {
		this.documentacionGeneral.setFirma(null);
	}

	public void limpiarFirmaIndicador() {
		this.informacionGeneralIndicador.setFirma(null);
	}

	/**
	 * Limpia la firma del sujeto entervistado
	 */
	public void limpiarFirmaEntrevistado() {
		this.personaDiagnostico.setFirma(null);
	}

	/**
	 * Decide que transacci�n realizar
	 * 
	 * @param aDecision
	 */
	public void decidirPostGuardado(String aDecision) {
		try {
			if (aDecision != null && aDecision.equals("CERRAR")) {

				this.cerrarModal("panelResumen");

			} else if (aDecision != null && aDecision.equals("CERRAR_ELABORAR_NUEVO")) {

				this.planificacion = null;
				this.planificacionGeneral = null;
				this.getPlanificacionGeneral();
				this.cliente = null;
				this.getCliente();

				this.proyectoCliente = null;
				this.getProyectoCliente();

				this.cerrarModal("panelResumen");

			} else if (aDecision != null && aDecision.equals("APROBAR_PLANIFICACION")) {

				aprobarPlanificacion();

			} else if (aDecision != null && aDecision.equals("AVISAR_APROBACION_CLIENTE")) {

				// IEmail.enviarCorreo(this.getMensaje("mensajeAprobacionDisponible",
				// this.informeMantenimiento.getCronograma().getEquipo().getCliente().getCliente()
				// + ", " +
				// this.informeMantenimiento.getCronograma().getEquipo().getCliente().getUbicacion(),
				// this.informeMantenimiento.getCronograma().getEquipo().getNombreEquipo()
				// + "," +
				// this.informeMantenimiento.getCronograma().getEquipo().getCodigoQr(),
				// "" + this.informeMantenimiento.getCronograma().getId()),
				// this.getMensaje("asuntoDisponibleAprobacion",
				// this.informeMantenimiento.getCronograma().getEquipo().getCodigoQr()),
				// this.informeMantenimiento.getCronograma().getEquipo().getCliente().getCorreoElectronico());
				// this.mostrarMensajeGlobalPersonalizado(this.getMensaje("mailAprobacionCliente",
				// this.informeMantenimiento.getCronograma().getEquipo().getCliente().getCorreoElectronico()),
				// "exito");

				// decidirPostGuardado("CERRAR_ELABORAR_NUEVO");

			} else if (aDecision != null && aDecision.equals("IMPRIMIR")) {
				//
			} else if (aDecision != null && aDecision.equals("ELIMINAR")) {
				// this.abrirModal("panelEliminarInforme");
			}

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}

	}

	/**
	 * Aprueba la planificaci�n
	 */
	public void aprobarPlanificacion() {
		Conexion conexion = new Conexion();

		try {

			conexion.setAutoCommitBD(false);

			this.planificacionGeneral.setEstadoActual("A");// APROBADA
			this.planificacionGeneral.getCamposBD();
			conexion.actualizarBD(this.planificacionGeneral.getEstructuraTabla().getTabla(), this.planificacionGeneral.getEstructuraTabla().getPersistencia(), this.planificacionGeneral.getEstructuraTabla().getLlavePrimaria(), null);

			conexion.commitBD();

			this.mostrarMensajeGlobalPersonalizado("Planificaci�n aprobada! Ya puede comenzar a ejecutarse", "exito");
			// this.abrirModal("panelTomarDecisiones");

			// reseteo de variables
			this.planificacion = null;
			armarEstructuraPlanificacion();

		} catch (Exception e) {
			conexion.rollbackBD();
			this.mostrarMensajeGlobal("transaccionFallida", "error");
		} finally {
			conexion.cerrarConexion();
		}

	}

	public void actualizarCompromisos() {
		Conexion conexion = new Conexion();
		List<Compromiso> temps = null;

		try {
			SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
			// validaciones
			boolean ok = true;
			if (this.compromisos != null && this.compromisos.size() > 0) {
				for (Compromiso p : this.compromisos) {

					// Revisa fechas doc

					if (!(formato.format(p.getFechaFinCompromiso()).compareTo(formato.format(p.getFechaInicioCompromiso())) >= 0)) {
						ok = false;
						this.mostrarMensajeGlobalPersonalizado("Las fechas hasta del compromiso deben ser mayor o igual a la fechas desde", "advertencia");
						break;
					}

					if (p.getFechaSeguimiento() != null && p.getFechaParaCuando() != null) {
						if (!(formato.format(p.getFechaParaCuando()).compareTo(formato.format(p.getFechaSeguimiento())) >= 0)) {
							ok = false;
							this.mostrarMensajeGlobalPersonalizado("Las fechas para cuando del seguimiento deben ser mayor o igual a la fechas de seguimiento si éstas se diligencian", "advertencia");
							break;
						}
					}

					if (p.getFechaSeguimiento() != null || (p.getEstado() != null && !p.getEstado().trim().equals("")) || (p.getObservacionesEstado() != null && !p.getObservacionesEstado().trim().equals("")) || p.getFechaParaCuando() != null) {

						if (!(p.getFechaSeguimiento() != null && (p.getEstado() != null && !p.getEstado().trim().equals("")) && (p.getObservacionesEstado() != null && !p.getObservacionesEstado().trim().equals("")))) {
							ok = false;
							this.mostrarMensajeGlobalPersonalizado("Recuerde que si diligenció para una fila espeífica algún campo con (**) ésta fila debe diligenciar todos sus campos con (**)", "advertencia");
							break;
						}

					}

				}

			} else {
				ok = false;

			}

			if (ok) {

				conexion.setAutoCommitBD(false);

				if (this.compromisoGeneral.getFechaGeneracionTodo() == null) {
					this.compromisoGeneral.setFechaGeneracionTodo(new Date());
				}

				this.compromisoGeneral.setProyectoCliente(this.proyectoCliente);

				Compromiso registro = new Compromiso();
				registro.getProyectoCliente().setId(this.proyectoCliente.getId());
				temps = IConsultasDAO.getCompromisos(registro);
				if (temps != null && temps.size() > 0) {

					// actualizar
					this.compromisoGeneral.getCamposBD();
					conexion.actualizarBD(this.compromisoGeneral.getEstructuraTabla().getTabla(), this.compromisoGeneral.getEstructuraTabla().getPersistencia(), this.compromisoGeneral.getEstructuraTabla().getLlavePrimaria(), null);

				} else {
					// insertar

					this.compromisoGeneral.getCamposBD();
					conexion.insertarBD(this.compromisoGeneral.getEstructuraTabla().getTabla(), this.compromisoGeneral.getEstructuraTabla().getPersistencia());

				}

				Compromiso compro = new Compromiso();
				Map<String, Object> condiciones = new HashMap<String, Object>();
				condiciones = new HashMap<String, Object>();
				condiciones.put("id_proyecto_cliente", this.proyectoCliente.getId());
				compro.getCamposBD();
				conexion.eliminarBD(compro.getEstructuraTabla().getTabla(), condiciones);

				for (Compromiso p : this.compromisos) {

					p.setProyectoCliente(this.proyectoCliente);

					p.getCamposBD();
					conexion.insertarBD(p.getEstructuraTabla().getTabla(), p.getEstructuraTabla().getPersistencia());
					p.setId(conexion.getUltimoSerialTransaccion(p.getEstructuraTabla().getTabla()));

				}

				conexion.commitBD();

				this.mostrarMensajeGlobalPersonalizado("Cambios en compromisos actualizados exitosamente", "exito");
				this.abrirModal("panelCompromisoCompleto");

				// reseteo de variables
				this.compromisos = null;
				armarEstructuraCompromiso();

			}

		} catch (Exception e) {
			conexion.rollbackBD();
			this.mostrarMensajeGlobal("transaccionFallida", "error");
		} finally {
			conexion.cerrarConexion();
		}
	}

	public void actualizarDocumentacion() {
		Conexion conexion = new Conexion();
		List<Documentacion> temps = null;

		try {
			SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
			// validaciones
			boolean ok = true;
			if (this.documentaciones != null && this.documentaciones.size() > 0) {
				for (Documentacion p : this.documentaciones) {

					if (p.getFechaAprobacion() != null && p.getFechaRevision() != null) {

						if (formato.format(p.getFechaRevision()).compareTo(formato.format(p.getFechaAprobacion())) > 0) {
							ok = false;
							break;
						}

					}

				}
			} else {
				ok = false;

			}

			if (ok) {

				conexion.setAutoCommitBD(false);

				if (this.documentacionGeneral.getFechaGeneracionTodo() == null) {
					this.documentacionGeneral.setFechaGeneracionTodo(new Date());
				}

				Documentacion registroDocumentacion = new Documentacion();
				registroDocumentacion.getProyectoCliente().setId(this.proyectoCliente.getId());
				temps = IConsultasDAO.getDocumentacion(registroDocumentacion);
				if (temps != null && temps.size() > 0) {

					// actualizar
					this.documentacionGeneral.getCamposBD();
					conexion.actualizarBD(this.documentacionGeneral.getEstructuraTabla().getTabla(), this.documentacionGeneral.getEstructuraTabla().getPersistencia(), this.documentacionGeneral.getEstructuraTabla().getLlavePrimaria(), null);

					for (Documentacion p : this.documentaciones) {
						p.getCamposBD();
						conexion.actualizarBD(p.getEstructuraTabla().getTabla(), p.getEstructuraTabla().getPersistencia(), p.getEstructuraTabla().getLlavePrimaria(), null);

					}

				} else {
					// insertar

					this.documentacionGeneral.getCamposBD();
					conexion.insertarBD(this.documentacionGeneral.getEstructuraTabla().getTabla(), this.documentacionGeneral.getEstructuraTabla().getPersistencia());

					for (Documentacion p : this.documentaciones) {

						p.getCamposBD();
						conexion.insertarBD(p.getEstructuraTabla().getTabla(), p.getEstructuraTabla().getPersistencia());
						p.setId(conexion.getUltimoSerialTransaccion(p.getEstructuraTabla().getTabla()));

					}

				}

				conexion.commitBD();

				this.mostrarMensajeGlobalPersonalizado("Documentaci�n guardada exitosamente", "exito");
				this.abrirModal("panelDocumentacionCompleto");

				// reseteo de variables
				this.documentaciones = null;
				armarEstructuraDocumentacion();

			} else {

				this.mostrarMensajeGlobalPersonalizado("Revise las fechas. La fecha de aprobaci�n debe ser mayor o igual a la de revisi�n", "advertencia");

			}

		} catch (Exception e) {
			conexion.rollbackBD();
			this.mostrarMensajeGlobal("transaccionFallida", "error");
		} finally {
			conexion.cerrarConexion();
		}
	}

	public void actualizarImplementacion() {
		Conexion conexion = new Conexion();
		List<Cronograma> temps = null;

		try {
			SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
			// validaciones
			boolean ok = true;
			if (this.cronogramas != null && this.cronogramas.size() > 0) {
				for (Cronograma p : this.cronogramas) {

					// implementaci�n
					if (p.getFechaInicioImplementacion() != null) {
						if (p.getFechaFinImplementacion() != null) {
							if (!(formato.format(p.getFechaInicioImplementacion()).compareTo(formato.format(p.getFechaFinImplementacion())) <= 0)) {
								ok = false;
								break;
							}
						}
					}
					if (p.getFechaFinImplementacion() != null) {
						if (p.getFechaInicioImplementacion() != null) {
							if (!(formato.format(p.getFechaFinImplementacion()).compareTo(formato.format(p.getFechaInicioImplementacion())) >= 0)) {
								ok = false;
								break;
							}
						}
					}
					if (p.getFechaSeguimientoImplementacion() != null) {
						if (p.getFechaInicioImplementacion() != null) {
							if (!(formato.format(p.getFechaSeguimientoImplementacion()).compareTo(formato.format(p.getFechaInicioImplementacion())) >= 0)) {
								ok = false;
								break;
							}
						}
						if (p.getFechaFinImplementacion() != null) {
							if (!(formato.format(p.getFechaSeguimientoImplementacion()).compareTo(formato.format(p.getFechaFinImplementacion())) <= 0)) {
								ok = false;
								break;
							}
						}

					}

				}
			} else {
				ok = false;

			}

			if (ok) {

				conexion.setAutoCommitBD(false);

				if (this.cronogramaGeneral.getFechaGeneracionTodo() == null) {
					this.cronogramaGeneral.setFechaGeneracionTodo(new Date());
				}

				Cronograma registroCronograma = new Cronograma();
				registroCronograma.getProyectoCliente().setId(this.proyectoCliente.getId());
				temps = IConsultasDAO.getCronograma(registroCronograma);
				if (temps != null && temps.size() > 0) {

					// actualizar
					this.cronogramaGeneral.getCamposBD();
					conexion.actualizarBD(this.cronogramaGeneral.getEstructuraTabla().getTabla(), this.cronogramaGeneral.getEstructuraTabla().getPersistencia(), this.cronogramaGeneral.getEstructuraTabla().getLlavePrimaria(), null);

					for (Cronograma p : this.cronogramas) {
						p.getCamposBD();
						conexion.actualizarBD(p.getEstructuraTabla().getTabla(), p.getEstructuraTabla().getPersistencia(), p.getEstructuraTabla().getLlavePrimaria(), null);

					}

				} else {
					// insertar

					this.cronogramaGeneral.getCamposBD();
					conexion.insertarBD(this.cronogramaGeneral.getEstructuraTabla().getTabla(), this.cronogramaGeneral.getEstructuraTabla().getPersistencia());

					for (Cronograma p : this.cronogramas) {

						p.getCamposBD();
						conexion.insertarBD(p.getEstructuraTabla().getTabla(), p.getEstructuraTabla().getPersistencia());
						p.setId(conexion.getUltimoSerialTransaccion(p.getEstructuraTabla().getTabla()));

					}

				}

				conexion.commitBD();

				this.mostrarMensajeGlobalPersonalizado("IMPLEMENTACION GUARDADA CON EXITO", "exito");
				this.abrirModal("panelCronogramaCompleto");

				// reseteo de variables
				this.cronogramas = null;
				armarEstructuraCronograma();

			} else {

				this.mostrarMensajeGlobalPersonalizado("REVISE LA INFORMACION DE LA IMPLEMENTACION. SI DILIGENCIO FECHAS, LAS FINALES DEBEN SER MAYORES O IGUALES A LAS INICIALES Y LAS FECHAS DE SEGUIMIENTO REALIZACION EN EL RANGO", "advertencia");

			}

		} catch (Exception e) {
			conexion.rollbackBD();
			this.mostrarMensajeGlobal("transaccionFallida", "error");
		} finally {
			conexion.cerrarConexion();
		}
	}

	public void actualizarCronograma() {
		Conexion conexion = new Conexion();
		List<Cronograma> temps = null;

		try {
			SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
			// validaciones
			boolean ok = true;
			if (this.cronogramas != null && this.cronogramas.size() > 0) {
				for (Cronograma p : this.cronogramas) {

					// Revisa fechas doc
					if (p.getFechaInicioDocumentacion() != null) {
						if (p.getFechaFinDocumentacion() != null) {
							if (!(formato.format(p.getFechaInicioDocumentacion()).compareTo(formato.format(p.getFechaFinDocumentacion())) <= 0)) {
								ok = false;
								break;
							}
						}
					}
					if (p.getFechaFinDocumentacion() != null) {
						if (p.getFechaInicioDocumentacion() != null) {
							if (!(formato.format(p.getFechaFinDocumentacion()).compareTo(formato.format(p.getFechaInicioDocumentacion())) >= 0)) {
								ok = false;
								break;
							}
						}
					}
					if (p.getFechaSeguimientoDocumentacion() != null) {
						if (p.getFechaInicioDocumentacion() != null) {
							if (!(formato.format(p.getFechaSeguimientoDocumentacion()).compareTo(formato.format(p.getFechaInicioDocumentacion())) >= 0)) {
								ok = false;
								break;
							}
						}
						if (p.getFechaFinDocumentacion() != null) {
							if (!(formato.format(p.getFechaSeguimientoDocumentacion()).compareTo(formato.format(p.getFechaFinDocumentacion())) <= 0)) {
								ok = false;
								break;
							}
						}

					}

					// socializacion
					if (p.getFechaInicioSocializacion() != null) {
						if (p.getFechaFinSocializacion() != null) {
							if (!(formato.format(p.getFechaInicioSocializacion()).compareTo(formato.format(p.getFechaFinSocializacion())) <= 0)) {
								ok = false;
								break;
							}
						}
					}
					if (p.getFechaFinSocializacion() != null) {
						if (p.getFechaInicioSocializacion() != null) {
							if (!(formato.format(p.getFechaFinSocializacion()).compareTo(formato.format(p.getFechaInicioSocializacion())) >= 0)) {
								ok = false;
								break;
							}
						}
					}
					if (p.getFechaSeguimientoSocializacion() != null) {
						if (p.getFechaInicioSocializacion() != null) {
							if (!(formato.format(p.getFechaSeguimientoSocializacion()).compareTo(formato.format(p.getFechaInicioSocializacion())) >= 0)) {
								ok = false;
								break;
							}
						}
						if (p.getFechaFinSocializacion() != null) {
							if (!(formato.format(p.getFechaSeguimientoSocializacion()).compareTo(formato.format(p.getFechaFinSocializacion())) <= 0)) {
								ok = false;
								break;
							}
						}

					}

					// implementaci�n
					if (p.getFechaInicioImplementacion() != null) {
						if (p.getFechaFinImplementacion() != null) {
							if (!(formato.format(p.getFechaInicioImplementacion()).compareTo(formato.format(p.getFechaFinImplementacion())) <= 0)) {
								ok = false;
								break;
							}
						}
					}
					if (p.getFechaFinImplementacion() != null) {
						if (p.getFechaInicioImplementacion() != null) {
							if (!(formato.format(p.getFechaFinImplementacion()).compareTo(formato.format(p.getFechaInicioImplementacion())) >= 0)) {
								ok = false;
								break;
							}
						}
					}
					if (p.getFechaSeguimientoImplementacion() != null) {
						if (p.getFechaInicioImplementacion() != null) {
							if (!(formato.format(p.getFechaSeguimientoImplementacion()).compareTo(formato.format(p.getFechaInicioImplementacion())) >= 0)) {
								ok = false;
								break;
							}
						}
						if (p.getFechaFinImplementacion() != null) {
							if (!(formato.format(p.getFechaSeguimientoImplementacion()).compareTo(formato.format(p.getFechaFinImplementacion())) <= 0)) {
								ok = false;
								break;
							}
						}

					}

					// auditor�a
					if (p.getFechaInicioAuditoria() != null) {
						if (p.getFechaFinAuditoria() != null) {
							if (!(formato.format(p.getFechaInicioAuditoria()).compareTo(formato.format(p.getFechaFinAuditoria())) <= 0)) {
								ok = false;
								break;
							}
						}
					}
					if (p.getFechaFinAuditoria() != null) {
						if (p.getFechaInicioAuditoria() != null) {
							if (!(formato.format(p.getFechaFinAuditoria()).compareTo(formato.format(p.getFechaInicioAuditoria())) >= 0)) {
								ok = false;
								break;
							}
						}
					}
					if (p.getFechaSeguimientoAuditoria() != null) {
						if (p.getFechaInicioAuditoria() != null) {
							if (!(formato.format(p.getFechaSeguimientoAuditoria()).compareTo(formato.format(p.getFechaInicioAuditoria())) >= 0)) {
								ok = false;
								break;
							}
						}
						if (p.getFechaFinAuditoria() != null) {
							if (!(formato.format(p.getFechaSeguimientoAuditoria()).compareTo(formato.format(p.getFechaFinAuditoria())) <= 0)) {
								ok = false;
								break;
							}
						}

					}

				}
			} else {
				ok = false;

			}

			if (ok) {

				conexion.setAutoCommitBD(false);

				if (this.cronogramaGeneral.getFechaGeneracionTodo() == null) {
					this.cronogramaGeneral.setFechaGeneracionTodo(new Date());
				}

				Cronograma registroCronograma = new Cronograma();
				registroCronograma.getProyectoCliente().setId(this.proyectoCliente.getId());
				temps = IConsultasDAO.getCronograma(registroCronograma);
				if (temps != null && temps.size() > 0) {

					// actualizar
					this.cronogramaGeneral.getCamposBD();
					conexion.actualizarBD(this.cronogramaGeneral.getEstructuraTabla().getTabla(), this.cronogramaGeneral.getEstructuraTabla().getPersistencia(), this.cronogramaGeneral.getEstructuraTabla().getLlavePrimaria(), null);

					for (Cronograma p : this.cronogramas) {
						p.getCamposBD();
						conexion.actualizarBD(p.getEstructuraTabla().getTabla(), p.getEstructuraTabla().getPersistencia(), p.getEstructuraTabla().getLlavePrimaria(), null);

					}

				} else {
					// insertar

					this.cronogramaGeneral.getCamposBD();
					conexion.insertarBD(this.cronogramaGeneral.getEstructuraTabla().getTabla(), this.cronogramaGeneral.getEstructuraTabla().getPersistencia());

					for (Cronograma p : this.cronogramas) {

						p.getCamposBD();
						conexion.insertarBD(p.getEstructuraTabla().getTabla(), p.getEstructuraTabla().getPersistencia());
						p.setId(conexion.getUltimoSerialTransaccion(p.getEstructuraTabla().getTabla()));

					}

				}

				conexion.commitBD();

				this.mostrarMensajeGlobalPersonalizado("Planificaci�n/Cronograma de forma detallada guardado exitosamente", "exito");
				this.abrirModal("panelCronogramaCompleto");

				// reseteo de variables
				this.cronogramas = null;
				armarEstructuraCronograma();

			} else {

				this.mostrarMensajeGlobalPersonalizado("Revise la informaci�n de la planificaci�n/cronograma detallado. Si se diligenci�, las fechas finales deben ser mayores o iguales a las iniciales y las fechas de seguimiento dentro del rango inicial-final", "advertencia");

			}

		} catch (Exception e) {
			conexion.rollbackBD();
			this.mostrarMensajeGlobal("transaccionFallida", "error");
		} finally {
			conexion.cerrarConexion();
		}
	}

	/**
	 * Actualiza la planificaci�n siendo inserci�n o actualizaci�n
	 * 
	 */
	public void actualizarPlanificacion() {
		Conexion conexion = new Conexion();
		List<Planificacion> temps = null;
		int sw = 0;
		try {

			if (this.planificacionGeneral.getEstadoActual().equals("P")) {
				sw = 0;
				for (Planificacion p : this.planificacion) {
					if (p.gettTiemposPlanificacion() != null && p.gettTiemposPlanificacion().size() > 0 && p.gettTiemposPlanificacion().stream().anyMatch(t -> t.isProgramado())) {
						sw = 1;
					}
				}
				if (sw == 0) {

					this.mostrarMensajeGlobalPersonalizado("Debe programar al menos una actividad", "advertencia");
					return;
				}

			} else {
				sw = 0;
				for (Planificacion p : this.planificacion) {
					if (p.gettTiemposPlanificacion() != null && p.gettTiemposPlanificacion().size() > 0 && p.gettTiemposPlanificacion().stream().anyMatch(t -> t.isEjecutado())) {
						sw = 1;
					}
				}
				if (sw == 0) {

					this.mostrarMensajeGlobalPersonalizado("Debe ejecutar al menos una actividad", "advertencia");
					return;
				}

			}

			conexion.setAutoCommitBD(false);

			Planificacion registroPlanificacion = new Planificacion();
			registroPlanificacion.getProyectoCliente().setId(this.proyectoCliente.getId());
			temps = IConsultasDAO.getPlanificacion(registroPlanificacion);
			if (temps != null && temps.size() > 0) {

				// actualizar
				this.planificacionGeneral.getCamposBD();
				conexion.actualizarBD(this.planificacionGeneral.getEstructuraTabla().getTabla(), this.planificacionGeneral.getEstructuraTabla().getPersistencia(), this.planificacionGeneral.getEstructuraTabla().getLlavePrimaria(), null);

				for (Planificacion p : this.planificacion) {
					p.getCamposBD();
					conexion.actualizarBD(p.getEstructuraTabla().getTabla(), p.getEstructuraTabla().getPersistencia(), p.getEstructuraTabla().getLlavePrimaria(), null);
					for (TiempoPlanificacion t : p.gettTiemposPlanificacion()) {
						t.getPlanificacion().setId(p.getId());
						t.getCamposBD();
						conexion.actualizarBD(t.getEstructuraTabla().getTabla(), t.getEstructuraTabla().getPersistencia(), t.getEstructuraTabla().getLlavePrimaria(), null);
					}

				}

			} else {
				// insertar

				this.planificacionGeneral.getCamposBD();
				conexion.insertarBD(this.planificacionGeneral.getEstructuraTabla().getTabla(), this.planificacionGeneral.getEstructuraTabla().getPersistencia());

				for (Planificacion p : this.planificacion) {

					p.getCamposBD();
					conexion.insertarBD(p.getEstructuraTabla().getTabla(), p.getEstructuraTabla().getPersistencia());
					p.setId(conexion.getUltimoSerialTransaccion(p.getEstructuraTabla().getTabla()));
					for (TiempoPlanificacion t : p.gettTiemposPlanificacion()) {
						t.getPlanificacion().setId(p.getId());
						t.getCamposBD();
						conexion.insertarBD(t.getEstructuraTabla().getTabla(), t.getEstructuraTabla().getPersistencia());
					}

				}

			}

			conexion.commitBD();

			this.mostrarMensajeGlobalPersonalizado("Planificaci�n guardada exitosamente", "exito");
			this.abrirModal("panelResumen");

			// reseteo de variables
			this.planificacion = null;
			armarEstructuraPlanificacion();

		} catch (Exception e) {
			conexion.rollbackBD();
			this.mostrarMensajeGlobal("transaccionFallida", "error");
		} finally {
			conexion.cerrarConexion();
		}
	}

	/**
	 * Actualiza el diagn�stico luego de validado
	 */
	public void actualizarDiagnostico(String aForma) {
		Conexion conexion = new Conexion();

		try {
			String copiaAparece = this.personaDiagnostico.gettAperece();
			
			//Date copiaFecha = this.personaDiagnostico.getFecha();
			conexion.setAutoCommitBD(false);
			Diagnostico diagnostico = new Diagnostico();
			diagnostico.getProyectoCliente().setId(this.proyectoCliente.getId());
			List<Diagnostico> diagnosticos = IConsultasDAO.getDiagnostico(diagnostico);

			// valida si existe persona
			PersonaDiagnostico persona = IConsultasDAO.getPersonaDiagnostico(this.proyectoCliente.getId());
			
			
			
			
			
			/// lo refreente a la estadistica

			if (diagnosticos != null && diagnosticos.size() > 0) {
				// editar

				for (Diagnostico d : this.diagnostico) {
					if (!(d.getEvidenciaEncontrada() != null && !d.getEvidenciaEncontrada().trim().equals(""))) {
						d.setEvidenciaEncontrada("");
					} 

					d.getCamposBD();
					conexion.actualizarBD(d.getEstructuraTabla().getTabla(), d.getEstructuraTabla().getPersistencia(), d.getEstructuraTabla().getLlavePrimaria(), null);

					EstadoDiagnostico estadoDiagnostico = new EstadoDiagnostico();
					Map<String, Object> condiciones = new HashMap<String, Object>();
					condiciones.put("id_diagnostico", d.getId());
					estadoDiagnostico.getCamposBD();
					conexion.eliminarBD(estadoDiagnostico.getEstructuraTabla().getTabla(), condiciones);

					if (d.gettEstadosDiagnostico() != null && d.gettEstadosDiagnostico().size() > 0) {
						for (EstadoDiagnostico e : d.gettEstadosDiagnostico()) {
							if (e.istSeleccionado()) {
								e.getCamposBD();
								conexion.insertarBD(e.getEstructuraTabla().getTabla(), e.getEstructuraTabla().getPersistencia());
							}

						}
					}

				}

			} else {
				// insertar
				for (EstadoProyectoCliente e : this.proyectoCliente.gettEstadosProyecto()) {
					e.getCamposBD();
					conexion.insertarBD(e.getEstructuraTabla().getTabla(), e.getEstructuraTabla().getPersistencia());
				}

				for (Diagnostico d : this.diagnostico) {
					if (!(d.getEvidenciaEncontrada() != null && !d.getEvidenciaEncontrada().trim().equals(""))) {
						d.setEvidenciaEncontrada("");
					}

					d.getCamposBD();
					conexion.insertarBD(d.getEstructuraTabla().getTabla(), d.getEstructuraTabla().getPersistencia());

					d.setId(conexion.getUltimoSerialTransaccion(d.getEstructuraTabla().getTabla()));
					if (d.gettEstadosDiagnostico() != null && d.gettEstadosDiagnostico().size() > 0) {
						for (EstadoDiagnostico e : d.gettEstadosDiagnostico()) {
							if (e.istSeleccionado()) {
								e.setDiagnostico(d);
								e.getCamposBD();
								conexion.insertarBD(e.getEstructuraTabla().getTabla(), e.getEstructuraTabla().getPersistencia());
							}

						}
					}

				}

			}

			if (persona != null && persona.getProyectoCliente() != null && persona.getProyectoCliente().getId() != null) {
				//this.personaDiagnostico.setFecha(copiaFecha);
				this.personaDiagnostico.getCamposBD();
				conexion.actualizarBD(this.personaDiagnostico.getEstructuraTabla().getTabla(), this.personaDiagnostico.getEstructuraTabla().getPersistencia(), this.personaDiagnostico.getEstructuraTabla().getLlavePrimaria(), null);

			} else {
				//this.personaDiagnostico.setFecha(copiaFecha);
				this.personaDiagnostico.getProyectoCliente().setId(this.proyectoCliente.getId());
				this.personaDiagnostico.getCamposBD();

				conexion.insertarBD(this.personaDiagnostico.getEstructuraTabla().getTabla(), this.personaDiagnostico.getEstructuraTabla().getPersistencia());
			}

			conexion.commitBD();

			if (aForma.equals(IConstantes.FORMA_COMPLETA)) {

				this.mostrarMensajeGlobalPersonalizado("SE HA ACTUALIZADO EXITOSAMENTE LA INFORMACION", "exito");

				// Ya el correo no es autom�tico, m�s bien el consultor decide
				// decide
				// cuando enviarlo
				// IEmail.enviarCorreo(this.getMensaje("mensajeDiagnostico",
				// this.proyectoCliente.getCliente().getCliente(),
				// this.proyectoCliente.getProyecto().getNombre()),
				// this.getMensaje("asuntoDiagnostico",
				// this.proyectoCliente.getProyecto().getNombre()),
				// this.proyectoCliente.getCliente().getCorreoElectronico());
				// this.mostrarMensajeGlobalPersonalizado(this.getMensaje("correoDiagnostico",
				// this.proyectoCliente.getCliente().getCorreoElectronico()),
				// "exito");

				this.abrirModal("panelResumen");
				this.cerrarModal("panelDiagnosticoCompleto");

			} else {
				if (aForma.equals("A")) {
					// guarda para subir archivo plano
					this.mostrarMensajeGlobalPersonalizado("PARA EVITAR PERDIDA DE LA INFORMACION ESCRITA HASTA EL MOEMNTO EL SISTEMA HA GUARDADO AUTOMATICAMENTE LOS CAMBIOS", "exito");
				} else {
					this.mostrarMensajeGlobal("diagnosticoExitosoIncompleto", "exito");
				}

			}

			// reseteo de variables
			this.diagnostico = null;
			armarEstructuraDiagnostico();
			this.personaDiagnostico.settAperece(copiaAparece);

		} catch (Exception e) {
			conexion.rollbackBD();
			this.mostrarMensajeGlobal("transaccionFallida", "error");
		} finally {
			conexion.cerrarConexion();
		}
	}

	/**
	 * Actualiza de una vez el diagn�stico o advierte
	 */
	public void actualizarValidarDiagnostico() {

		try {
			if (isValidoDiagnostico()) {
				// actualizarDiagnostico(IConstantes.FORMA_COMPLETA);
				this.abrirModal("panelDiagnosticoCompleto");
			}

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}

	}

	/**
	 * Valida que si selecion� no aplica como estado deselecciona los dem�s.
	 * Cambi� a solo seleccionar el que es
	 * 
	 * @param aEstados
	 * @param aEstado
	 */
	public void vaidarNoAplica(List<EstadoDiagnostico> aEstados, EstadoDiagnostico aEstado, Diagnostico aDiagnostico) {
		// if (aEstado != null && aEstado.getEstado() != null &&
		// aEstado.getEstado().getId() != null &&
		// aEstado.getEstado().getId().intValue() ==
		// IConstantes.ID_ESTADO_NO_APLICA.intValue()) {
		// if (aEstado.istSeleccionado()) {
		// aEstados.stream().filter(p -> p.getEstado().getId().intValue() !=
		// IConstantes.ID_ESTADO_NO_APLICA.intValue()).forEach(p -> {
		// p.settSeleccionado(false);
		//
		// });
		// if (aEstado.getEstado().getId().intValue() ==
		// IConstantes.ID_ESTADO_NO_APLICA) {
		// aDiagnostico.setEvidenciaEncontrada("NO APLICA");
		// }
		// }
		// } else {
		// if (aEstado.istSeleccionado()) {
		// aEstados.stream().filter(p -> p.getEstado().getId().intValue() ==
		// IConstantes.ID_ESTADO_NO_APLICA.intValue()).forEach(p -> {
		// p.settSeleccionado(false);
		// });
		//
		// }
		//
		// }

		if (aEstado != null && aEstado.getEstado() != null && aEstado.getEstado().getId() != null) {
			if (aEstado.istSeleccionado()) {

				if (aEstado.getEstado().getId().intValue() == IConstantes.ID_ESTADO_NO_APLICA.intValue()) {
					aDiagnostico.setEvidenciaEncontrada("NO APLICA");
				}
				if (aEstado.getEstado().getId().intValue() == IConstantes.ID_ESTADO_CUMPLE.intValue()) {
					aDiagnostico.setEvidenciaEncontrada("CUMPLE");
				}

				if (aEstado.getEstado().getId().intValue() == IConstantes.ID_ESTADO_NO_CONFORMIDAD.intValue() || aEstado.getEstado().getId().intValue() == IConstantes.ID_ESTADO_RECOMENDACION.intValue()) {
					aDiagnostico.setAnalisisCausa("-");
					aDiagnostico.setAccionesRealizar("-");

					if (aEstado.getEstado().getId().intValue() == IConstantes.ID_ESTADO_RECOMENDACION.intValue()) {
						aDiagnostico.setAnalisisCausa("NO APLICA");
					}

				} else {
					aDiagnostico.setAnalisisCausa("NO APLICA");
					aDiagnostico.setAccionesRealizar("NO APLICA");
				}

				aEstados.stream().filter(p -> p.getEstado().getId().intValue() != aEstado.getEstado().getId().intValue()).forEach(p -> {
					p.settSeleccionado(false);
				});

			}

		}

	}

	public boolean isActivoCausasRealizar(Diagnostico aDiagnostico) {
		boolean ok = false;
		if (aDiagnostico.gettEstadosDiagnostico() != null && aDiagnostico.gettEstadosDiagnostico().size() > 0) {
			for (EstadoDiagnostico e : aDiagnostico.gettEstadosDiagnostico()) {
				if (e.istSeleccionado() && e.getEstado() != null && e.getEstado().getId() != null && (e.getEstado().getId().intValue() == IConstantes.ID_ESTADO_NO_CONFORMIDAD.intValue() || e.getEstado().getId().intValue() == IConstantes.ID_ESTADO_RECOMENDACION.intValue())) {
					ok = true;
					break;
				}
			}
		}

		return ok;

	}

	/**
	 * Guarda la planificaci�n
	 */
	public void enviarCorrero() {

		this.mostrarMensajeGlobalPersonalizado("Se ha enviado un correo informando sobre aprobaci�n del plan de trabajo", "exito");

	}

	/**
	 * Guarda la planificaci�n
	 */
	public void guardarPlanificacion() {

		this.mostrarMensajeGlobalPersonalizado("Se ha guardado y aprobado el plan de trabajo a ejecutar", "exito");

	}

	/**
	 * Determina si todos los periodos de un indicador est�n nulos
	 * 
	 * @param aIndicador
	 * @return nulos
	 */
	public boolean isTodosPeriodosNulos(Indicador aIndicador) {
		boolean nulos = true;

		if (aIndicador != null) {
			if (aIndicador.getPeriodo1() != null) {
				nulos = false;
			}
			if (aIndicador.getPeriodo2() != null) {
				nulos = false;
			}
			if (aIndicador.getPeriodo3() != null) {
				nulos = false;
			}
			if (aIndicador.getPeriodo4() != null) {
				nulos = false;
			}
			if (aIndicador.getPeriodo5() != null) {
				nulos = false;
			}
			if (aIndicador.getPeriodo6() != null) {
				nulos = false;
			}
			if (aIndicador.getPeriodo7() != null) {
				nulos = false;
			}
			if (aIndicador.getPeriodo8() != null) {
				nulos = false;
			}
			if (aIndicador.getPeriodo9() != null) {
				nulos = false;
			}
			if (aIndicador.getPeriodo10() != null) {
				nulos = false;
			}

			if (aIndicador.getPeriodo11() != null) {
				nulos = false;
			}

			if (aIndicador.getPeriodo12() != null) {
				nulos = false;
			}

		}

		return nulos;
	}

	/**
	 * Obtiene la suma de indicadores y da ponderado objetivo
	 * 
	 * @param aObjetivoEtapaIndicador
	 * @return suma
	 */
	public BigDecimal getResultadoObjetivo(ObjetivoEtapaIndicador aObjetivoEtapaIndicador) {
		BigDecimal suma = new BigDecimal(0);
		try {
			if (aObjetivoEtapaIndicador != null && aObjetivoEtapaIndicador.getIndicadores() != null && aObjetivoEtapaIndicador.getIndicadores().size() > 0) {

				for (Indicador i : aObjetivoEtapaIndicador.getIndicadores()) {
					suma = suma.add(getResultadoIndicador(i));
				}

				suma = suma.divide(new BigDecimal(aObjetivoEtapaIndicador.getIndicadores().size()), 10, RoundingMode.HALF_UP);

				suma = this.getValorRedondeado(suma, IConstantes.DECIMALES_REDONDEAR);

			}

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}

		return suma;
	}

	/**
	 * Obtiene la suma o resultado de indicador
	 * 
	 * @param aIndicador
	 * @return suma
	 */
	public BigDecimal getResultadoIndicador(Indicador aIndicador) {
		BigDecimal suma = new BigDecimal(0);
		Integer multiplo = null;
		this.graficoIndicador = null;
		ChartSeries periodos = new ChartSeries();
		ChartSeries metaReferencia = new ChartSeries();

		try {
			if (aIndicador != null && aIndicador.getFrecuencia() != null) {
				Integer calculo = IConstantes.NUMERO_PERIODOS_INDICADORES.intValue() / aIndicador.getFrecuencia().intValue();

				this.graficoIndicador = new BarChartModel();
				periodos.setLabel("RESULTADO INDICADOR");
				metaReferencia.setLabel("META REFERENCIA");

				for (int i = 1; i <= IConstantes.NUMERO_PERIODOS_INDICADORES.intValue(); i++) {
					multiplo = i % calculo;

					if (multiplo != null && multiplo.intValue() == 0) {

						if (i == 1) {
							if (aIndicador.getPeriodo1() != null) {
								suma = suma.add(aIndicador.getPeriodo1());
								periodos.set(this.getMesAnoF2(this.getFechaPeriodo(1)), aIndicador.getPeriodo1());
							} else {
								periodos.set(this.getMesAnoF2(this.getFechaPeriodo(1)), 0);
							}
							metaReferencia.set(this.getMesAnoF2(this.getFechaPeriodo(1)), (aIndicador.getMetaInferior().add(new BigDecimal(1))));
						}
						if (i == 2) {
							if (aIndicador.getPeriodo2() != null) {
								suma = suma.add(aIndicador.getPeriodo2());
								periodos.set(this.getMesAnoF2(this.getFechaPeriodo(2)), aIndicador.getPeriodo2());
							} else {
								periodos.set(this.getMesAnoF2(this.getFechaPeriodo(2)), 0);
							}
							metaReferencia.set(this.getMesAnoF2(this.getFechaPeriodo(2)), (aIndicador.getMetaInferior().add(new BigDecimal(1))));
						}
						if (i == 3) {
							if (aIndicador.getPeriodo3() != null) {
								suma = suma.add(aIndicador.getPeriodo3());
								periodos.set(this.getMesAnoF2(this.getFechaPeriodo(3)), aIndicador.getPeriodo3());
							} else {
								periodos.set(this.getMesAnoF2(this.getFechaPeriodo(3)), 0);
							}
							metaReferencia.set(this.getMesAnoF2(this.getFechaPeriodo(3)), (aIndicador.getMetaInferior().add(new BigDecimal(1))));
						}
						if (i == 4) {
							if (aIndicador.getPeriodo4() != null) {
								suma = suma.add(aIndicador.getPeriodo4());
								periodos.set(this.getMesAnoF2(this.getFechaPeriodo(4)), aIndicador.getPeriodo4());
							} else {
								periodos.set(this.getMesAnoF2(this.getFechaPeriodo(4)), 0);
							}
							metaReferencia.set(this.getMesAnoF2(this.getFechaPeriodo(4)), (aIndicador.getMetaInferior().add(new BigDecimal(1))));
						}
						if (i == 5) {
							if (aIndicador.getPeriodo5() != null) {
								suma = suma.add(aIndicador.getPeriodo5());
								periodos.set(this.getMesAnoF2(this.getFechaPeriodo(5)), aIndicador.getPeriodo5());
							} else {
								periodos.set(this.getMesAnoF2(this.getFechaPeriodo(5)), 0);
							}
							metaReferencia.set(this.getMesAnoF2(this.getFechaPeriodo(5)), (aIndicador.getMetaInferior().add(new BigDecimal(1))));
						}
						if (i == 6) {
							if (aIndicador.getPeriodo6() != null) {
								suma = suma.add(aIndicador.getPeriodo6());
								periodos.set(this.getMesAnoF2(this.getFechaPeriodo(6)), aIndicador.getPeriodo6());
							} else {
								periodos.set(this.getMesAnoF2(this.getFechaPeriodo(6)), 0);
							}
							metaReferencia.set(this.getMesAnoF2(this.getFechaPeriodo(6)), (aIndicador.getMetaInferior().add(new BigDecimal(1))));
						}
						if (i == 7) {
							if (aIndicador.getPeriodo7() != null) {
								suma = suma.add(aIndicador.getPeriodo7());
								periodos.set(this.getMesAnoF2(this.getFechaPeriodo(7)), aIndicador.getPeriodo7());
							} else {
								periodos.set(this.getMesAnoF2(this.getFechaPeriodo(7)), 0);
							}
							metaReferencia.set(this.getMesAnoF2(this.getFechaPeriodo(1)), (aIndicador.getMetaInferior().add(new BigDecimal(1))));
						}
						if (i == 8) {
							if (aIndicador.getPeriodo8() != null) {
								suma = suma.add(aIndicador.getPeriodo8());
								periodos.set(this.getMesAnoF2(this.getFechaPeriodo(8)), aIndicador.getPeriodo8());
							} else {
								periodos.set(this.getMesAnoF2(this.getFechaPeriodo(8)), 0);
							}
							metaReferencia.set(this.getMesAnoF2(this.getFechaPeriodo(8)), (aIndicador.getMetaInferior().add(new BigDecimal(1))));
						}
						if (i == 9) {
							if (aIndicador.getPeriodo9() != null) {
								suma = suma.add(aIndicador.getPeriodo9());
								periodos.set(this.getMesAnoF2(this.getFechaPeriodo(9)), aIndicador.getPeriodo9());
							} else {
								periodos.set(this.getMesAnoF2(this.getFechaPeriodo(9)), 0);
							}
							metaReferencia.set(this.getMesAnoF2(this.getFechaPeriodo(9)), (aIndicador.getMetaInferior().add(new BigDecimal(1))));
						}
						if (i == 10) {
							if (aIndicador.getPeriodo10() != null) {
								suma = suma.add(aIndicador.getPeriodo10());
								periodos.set(this.getMesAnoF2(this.getFechaPeriodo(10)), aIndicador.getPeriodo10());
							} else {
								periodos.set(this.getMesAnoF2(this.getFechaPeriodo(10)), 0);
							}
							metaReferencia.set(this.getMesAnoF2(this.getFechaPeriodo(10)), (aIndicador.getMetaInferior().add(new BigDecimal(1))));
						}
						if (i == 11) {
							if (aIndicador.getPeriodo11() != null) {
								suma = suma.add(aIndicador.getPeriodo11());
								periodos.set(this.getMesAnoF2(this.getFechaPeriodo(11)), aIndicador.getPeriodo11());
							} else {
								periodos.set(this.getMesAnoF2(this.getFechaPeriodo(11)), 0);
							}
							metaReferencia.set(this.getMesAnoF2(this.getFechaPeriodo(11)), (aIndicador.getMetaInferior().add(new BigDecimal(1))));
						}
						if (i == 12) {
							if (aIndicador.getPeriodo12() != null) {
								suma = suma.add(aIndicador.getPeriodo12());
								periodos.set(this.getMesAnoF2(this.getFechaPeriodo(12)), aIndicador.getPeriodo12());
							} else {
								periodos.set(this.getMesAnoF2(this.getFechaPeriodo(12)), 0);
							}
							metaReferencia.set(this.getMesAnoF2(this.getFechaPeriodo(12)), (aIndicador.getMetaInferior().add(new BigDecimal(1))));
						}

					} else {

						if (i == 1) {
							aIndicador.setNumerador1(null);
							aIndicador.setDenominador1(null);
							aIndicador.setPeriodo1(null);
						}
						if (i == 2) {
							aIndicador.setNumerador2(null);
							aIndicador.setDenominador2(null);
							aIndicador.setPeriodo2(null);
						}
						if (i == 3) {
							aIndicador.setNumerador3(null);
							aIndicador.setDenominador3(null);
							aIndicador.setPeriodo3(null);
						}
						if (i == 4) {
							aIndicador.setNumerador4(null);
							aIndicador.setDenominador4(null);
							aIndicador.setPeriodo4(null);
						}
						if (i == 5) {
							aIndicador.setNumerador5(null);
							aIndicador.setDenominador5(null);
							aIndicador.setPeriodo5(null);
						}
						if (i == 6) {
							aIndicador.setNumerador6(null);
							aIndicador.setDenominador6(null);
							aIndicador.setPeriodo6(null);
						}
						if (i == 7) {
							aIndicador.setNumerador7(null);
							aIndicador.setDenominador7(null);
							aIndicador.setPeriodo7(null);
						}
						if (i == 8) {
							aIndicador.setNumerador1(null);
							aIndicador.setDenominador1(null);
							aIndicador.setPeriodo1(null);
						}
						if (i == 9) {
							aIndicador.setNumerador9(null);
							aIndicador.setDenominador9(null);
							aIndicador.setPeriodo9(null);
						}
						if (i == 10) {
							aIndicador.setNumerador10(null);
							aIndicador.setDenominador10(null);
							aIndicador.setPeriodo10(null);
						}
						if (i == 11) {
							aIndicador.setNumerador11(null);
							aIndicador.setDenominador11(null);
							aIndicador.setPeriodo11(null);
						}
						if (i == 12) {
							aIndicador.setNumerador12(null);
							aIndicador.setDenominador12(null);
							aIndicador.setPeriodo12(null);
						}

					}

				}

				this.graficoIndicador.addSeries(periodos);
				this.graficoIndicador.addSeries(metaReferencia);

				this.graficoIndicador.setTitle("RESULTADOS INDICADOR POR PERIODO");
				this.graficoIndicador.setAnimate(true);
				this.graficoIndicador.setLegendPosition("nw");
				Axis ejeX = this.graficoIndicador.getAxis(AxisType.X);
				ejeX.setTickAngle(-30);
				ejeX.setLabel("PERIODO");

				Axis ejeY = this.graficoIndicador.getAxis(AxisType.Y);
				ejeY.setLabel("RESULTADO INDICADOR");

				suma = suma.divide(new BigDecimal(aIndicador.getFrecuencia()), 10, RoundingMode.HALF_UP);
				suma = this.getValorRedondeado(suma, IConstantes.DECIMALES_REDONDEAR);

			}

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
		return suma;
	}

	/**
	 * Determina si se debe o no tomar plan de acci�n
	 * 
	 * @return tomar
	 */
	public boolean isTomarPlanAccion(Indicador aIndicador) {
		boolean tomar = false;
		try {

			if (aIndicador.getPeriodo1() != null) {

				if (this.getColorTextoPeriodo(aIndicador, aIndicador.getPeriodo1()).equals("ROJO")) {
					tomar = true;
				}

			}
			if (aIndicador.getPeriodo2() != null) {
				if (this.getColorTextoPeriodo(aIndicador, aIndicador.getPeriodo2()).equals("ROJO")) {
					tomar = true;
				}
			}
			if (aIndicador.getPeriodo3() != null) {
				if (this.getColorTextoPeriodo(aIndicador, aIndicador.getPeriodo3()).equals("ROJO")) {
					tomar = true;
				}
			}
			if (aIndicador.getPeriodo4() != null) {
				if (this.getColorTextoPeriodo(aIndicador, aIndicador.getPeriodo4()).equals("ROJO")) {
					tomar = true;
				}
			}
			if (aIndicador.getPeriodo5() != null) {
				if (this.getColorTextoPeriodo(aIndicador, aIndicador.getPeriodo5()).equals("ROJO")) {
					tomar = true;
				}
			}
			if (aIndicador.getPeriodo6() != null) {
				if (this.getColorTextoPeriodo(aIndicador, aIndicador.getPeriodo6()).equals("ROJO")) {
					tomar = true;
				}
			}
			if (aIndicador.getPeriodo7() != null) {
				if (this.getColorTextoPeriodo(aIndicador, aIndicador.getPeriodo7()).equals("ROJO")) {
					tomar = true;
				}
			}
			if (aIndicador.getPeriodo8() != null) {
				if (this.getColorTextoPeriodo(aIndicador, aIndicador.getPeriodo8()).equals("ROJO")) {
					tomar = true;
				}
			}
			if (aIndicador.getPeriodo9() != null) {
				if (this.getColorTextoPeriodo(aIndicador, aIndicador.getPeriodo9()).equals("ROJO")) {
					tomar = true;
				}
			}
			if (aIndicador.getPeriodo10() != null) {
				if (this.getColorTextoPeriodo(aIndicador, aIndicador.getPeriodo10()).equals("ROJO")) {
					tomar = true;
				}
			}

			if (aIndicador.getPeriodo11() != null) {
				if (this.getColorTextoPeriodo(aIndicador, aIndicador.getPeriodo11()).equals("ROJO")) {
					tomar = true;
				}
			}

			if (aIndicador.getPeriodo12() != null) {
				if (this.getColorTextoPeriodo(aIndicador, aIndicador.getPeriodo12()).equals("ROJO")) {
					tomar = true;
				}
			}

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
		return tomar;

	}

	/**
	 * Obtiene el color del texto seg�n frecuencia elegida y el periodo
	 * 
	 * @return color
	 */
	public String getColorTextoPeriodo(Indicador aIndicador, BigDecimal aValorPeriodo) {
		String color = "ROJO";
		try {
			if (aIndicador != null && aIndicador.getFrecuencia() != null && aValorPeriodo != null) {

				if (aValorPeriodo.compareTo(aIndicador.getMetaInferior()) <= 0) {
					color = "ROJO";
				} else if (aValorPeriodo.compareTo(aIndicador.getMetaInferior()) > 0 && aValorPeriodo.compareTo(aIndicador.getMetaIntermedia()) <= 0) {
					color = "NARANJA";
				} else {
					color = "VERDE";

				}

			}

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
		return color;
	}

	public String getColorCeldaPlanificacion(Cronograma aCronograma, String aEtapa) {
		String color = "ROJO";
		try {
			if (aEtapa != null && aEtapa.equals("DOCUMENTACION")) {

				if (aCronograma.getMetaDocumentacion().compareTo(this.cronogramaGeneral.getMetaInferiorDocumentacion()) <= 0) {
					color = "ROJO";
				} else if (aCronograma.getMetaDocumentacion().compareTo(this.cronogramaGeneral.getMetaInferiorDocumentacion()) > 0 && aCronograma.getMetaDocumentacion().compareTo(this.cronogramaGeneral.getMetaIntermediaDocumentacion()) <= 0) {
					color = "NARANJA";
				} else {
					color = "VERDE";

				}

			} else if (aEtapa != null && aEtapa.equals("SOCIALIZACION")) {

				if (aCronograma.getMetaSocializacion().compareTo(this.cronogramaGeneral.getMetaInferiorSocializacion()) <= 0) {
					color = "ROJO";
				} else if (aCronograma.getMetaSocializacion().compareTo(this.cronogramaGeneral.getMetaInferiorSocializacion()) > 0 && aCronograma.getMetaSocializacion().compareTo(this.cronogramaGeneral.getMetaIntermediaSocializacion()) <= 0) {
					color = "NARANJA";
				} else {
					color = "VERDE";

				}

			} else if (aEtapa != null && aEtapa.equals("IMPLEMENTACION")) {

				if (aCronograma.getMetaImplementacion().compareTo(this.cronogramaGeneral.getMetaInferiorImplementacion()) <= 0) {
					color = "ROJO";
				} else if (aCronograma.getMetaImplementacion().compareTo(this.cronogramaGeneral.getMetaInferiorImplementacion()) > 0 && aCronograma.getMetaImplementacion().compareTo(this.cronogramaGeneral.getMetaIntermediaImplementacion()) <= 0) {
					color = "NARANJA";
				} else {
					color = "VERDE";

				}

			} else {

				if (aCronograma.getMetaAuditoria().compareTo(this.cronogramaGeneral.getMetaInferiorAuditoria()) <= 0) {
					color = "ROJO";
				} else if (aCronograma.getMetaAuditoria().compareTo(this.cronogramaGeneral.getMetaInferiorAuditoria()) > 0 && aCronograma.getMetaAuditoria().compareTo(this.cronogramaGeneral.getMetaIntermediaAuditoria()) <= 0) {
					color = "NARANJA";
				} else {
					color = "VERDE";

				}

			}

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
		return color;
	}

	/**
	 * Obtiene el color de la celda seg�n frecuencia elegida y el periodo
	 * 
	 * @return color
	 */
	public String getColorCeldaPeriodo(Indicador aIndicador, Integer aPeriodo) {
		String color = "BLOQUEADO";
		try {
			if (aIndicador != null && aIndicador.getFrecuencia() != null) {
				Integer calculo = IConstantes.NUMERO_PERIODOS_INDICADORES.intValue() / aIndicador.getFrecuencia().intValue();
				Integer multiplo = aPeriodo % calculo;
				if (multiplo != null && multiplo.intValue() == 0) {
					color = "NORMAL";
				} else {
					color = "BLOQUEADO";
				}

			}

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
		return color;
	}

	/**
	 * Obtiene la fecha del periodo
	 * 
	 * @param aNumeroPeriodo
	 * @return fechaIterada
	 */
	public Date getFechaPeriodo(Integer aNumeroPeriodo) {
		Date fechaIterada = null;
		try {
			if (aNumeroPeriodo != null && this.informacionGeneralIndicador.getFechaInicio() != null) {
				fechaIterada = this.informacionGeneralIndicador.getFechaInicio();
				for (int i = 1; i <= aNumeroPeriodo - 1; i++) {
					fechaIterada = this.getFechaDiasSumados(fechaIterada);
				}
			}
		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
		return fechaIterada;
	}

	/**
	 * Retrocede al inicio del mes anterior, para ello se supone que est� en mes
	 * y d�a 01, entonces resta un dia, saca el mes y arma el inicio nuevo
	 * 
	 * @param aFecha
	 */
	public void retrocederMesAno() {

		SimpleDateFormat mesAno = new SimpleDateFormat("yyyy-MM");
		SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
		try {
			String mesAnoNuevo = null;
			if (this.informacionGeneralIndicador.getFechaInicio() != null) {

				mesAnoNuevo = mesAno.format(this.getFechaDiasSumados(this.informacionGeneralIndicador.getFechaInicio(), -1)) + "-01";
				this.informacionGeneralIndicador.setFechaInicio(formato.parse(mesAnoNuevo));

			}

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}

	}

	/**
	 * Adelanta el mes de la fecha actual que es el inicio del mes para ello
	 * simplemente sumamos los dias de su mes
	 * 
	 * @param aFecha
	 */
	public void adelantarMesAno() {
		if (this.informacionGeneralIndicador.getFechaInicio() != null) {
			this.informacionGeneralIndicador.setFechaInicio(this.getFechaDiasSumados(this.informacionGeneralIndicador.getFechaInicio()));
		}

	}

	/**
	 * Calcula el resultado de un determinado periodo
	 * 
	 * @param aNumeroPeriodo
	 */
	public void calcularResultadoPeriodo(Integer aNumeroPeriodo) {
		boolean exitoso = true;
		if (aNumeroPeriodo != null && aNumeroPeriodo.intValue() == 1) {
			if (this.indicadorTransaccion.getNumerador1() != null && this.indicadorTransaccion.getDenominador1() != null) {
				if (this.indicadorTransaccion.getDenominador1().equals(new BigDecimal(0))) {
					exitoso = false;
					this.indicadorTransaccion.setDenominador1(null);
					this.indicadorTransaccion.setPeriodo1(null);
				} else {
					this.indicadorTransaccion.setPeriodo1(this.indicadorTransaccion.getNumerador1().divide(this.indicadorTransaccion.getDenominador1(), 10, RoundingMode.HALF_UP));
					this.indicadorTransaccion.setPeriodo1(this.getValorRedondeado(this.indicadorTransaccion.getPeriodo1().multiply(new BigDecimal(100)), IConstantes.DECIMALES_REDONDEAR));
				}
			} else {
				this.indicadorTransaccion.setPeriodo1(null);
			}
		}

		if (aNumeroPeriodo != null && aNumeroPeriodo.intValue() == 2) {
			if (this.indicadorTransaccion.getNumerador2() != null && this.indicadorTransaccion.getDenominador2() != null) {
				if (this.indicadorTransaccion.getDenominador2().equals(new BigDecimal(0))) {
					exitoso = false;
					this.indicadorTransaccion.setDenominador2(null);
					this.indicadorTransaccion.setPeriodo2(null);
				} else {
					this.indicadorTransaccion.setPeriodo2(this.indicadorTransaccion.getNumerador2().divide(this.indicadorTransaccion.getDenominador2(), 10, RoundingMode.HALF_UP));
					this.indicadorTransaccion.setPeriodo2(this.getValorRedondeado(this.indicadorTransaccion.getPeriodo2().multiply(new BigDecimal(100)), IConstantes.DECIMALES_REDONDEAR));
				}
			} else {
				this.indicadorTransaccion.setPeriodo2(null);
			}
		}

		if (aNumeroPeriodo != null && aNumeroPeriodo.intValue() == 3) {
			if (this.indicadorTransaccion.getNumerador3() != null && this.indicadorTransaccion.getDenominador3() != null) {
				if (this.indicadorTransaccion.getDenominador3().equals(new BigDecimal(0))) {
					exitoso = false;
					this.indicadorTransaccion.setDenominador3(null);
					this.indicadorTransaccion.setPeriodo3(null);
				} else {
					this.indicadorTransaccion.setPeriodo3(this.indicadorTransaccion.getNumerador3().divide(this.indicadorTransaccion.getDenominador3(), 10, RoundingMode.HALF_UP));
					this.indicadorTransaccion.setPeriodo3(this.getValorRedondeado(this.indicadorTransaccion.getPeriodo3().multiply(new BigDecimal(100)), IConstantes.DECIMALES_REDONDEAR));
				}
			} else {
				this.indicadorTransaccion.setPeriodo3(null);
			}
		}

		if (aNumeroPeriodo != null && aNumeroPeriodo.intValue() == 4) {
			if (this.indicadorTransaccion.getNumerador4() != null && this.indicadorTransaccion.getDenominador4() != null) {
				if (this.indicadorTransaccion.getDenominador4().equals(new BigDecimal(0))) {
					exitoso = false;
					this.indicadorTransaccion.setDenominador4(null);
					this.indicadorTransaccion.setPeriodo4(null);
				} else {
					this.indicadorTransaccion.setPeriodo4(this.indicadorTransaccion.getNumerador4().divide(this.indicadorTransaccion.getDenominador4(), 10, RoundingMode.HALF_UP));
					this.indicadorTransaccion.setPeriodo4(this.getValorRedondeado(this.indicadorTransaccion.getPeriodo4().multiply(new BigDecimal(100)), IConstantes.DECIMALES_REDONDEAR));
				}
			} else {
				this.indicadorTransaccion.setPeriodo4(null);
			}
		}

		if (aNumeroPeriodo != null && aNumeroPeriodo.intValue() == 5) {
			if (this.indicadorTransaccion.getNumerador5() != null && this.indicadorTransaccion.getDenominador5() != null) {
				if (this.indicadorTransaccion.getDenominador5().equals(new BigDecimal(0))) {
					exitoso = false;
					this.indicadorTransaccion.setDenominador5(null);
					this.indicadorTransaccion.setPeriodo5(null);
				} else {
					this.indicadorTransaccion.setPeriodo5(this.indicadorTransaccion.getNumerador5().divide(this.indicadorTransaccion.getDenominador5(), 10, RoundingMode.HALF_UP));
					this.indicadorTransaccion.setPeriodo5(this.getValorRedondeado(this.indicadorTransaccion.getPeriodo5().multiply(new BigDecimal(100)), IConstantes.DECIMALES_REDONDEAR));
				}
			} else {
				this.indicadorTransaccion.setPeriodo5(null);
			}
		}

		if (aNumeroPeriodo != null && aNumeroPeriodo.intValue() == 6) {
			if (this.indicadorTransaccion.getNumerador6() != null && this.indicadorTransaccion.getDenominador6() != null) {
				if (this.indicadorTransaccion.getDenominador6().equals(new BigDecimal(0))) {
					exitoso = false;
					this.indicadorTransaccion.setDenominador6(null);
					this.indicadorTransaccion.setPeriodo6(null);
				} else {
					this.indicadorTransaccion.setPeriodo6(this.indicadorTransaccion.getNumerador6().divide(this.indicadorTransaccion.getDenominador6(), 10, RoundingMode.HALF_UP));
					this.indicadorTransaccion.setPeriodo6(this.getValorRedondeado(this.indicadorTransaccion.getPeriodo6().multiply(new BigDecimal(100)), IConstantes.DECIMALES_REDONDEAR));
				}
			} else {
				this.indicadorTransaccion.setPeriodo6(null);
			}
		}

		if (aNumeroPeriodo != null && aNumeroPeriodo.intValue() == 7) {
			if (this.indicadorTransaccion.getNumerador7() != null && this.indicadorTransaccion.getDenominador7() != null) {
				if (this.indicadorTransaccion.getDenominador7().equals(new BigDecimal(0))) {
					exitoso = false;
					this.indicadorTransaccion.setDenominador7(null);
					this.indicadorTransaccion.setPeriodo7(null);
				} else {
					this.indicadorTransaccion.setPeriodo7(this.indicadorTransaccion.getNumerador7().divide(this.indicadorTransaccion.getDenominador7(), 10, RoundingMode.HALF_UP));
					this.indicadorTransaccion.setPeriodo7(this.getValorRedondeado(this.indicadorTransaccion.getPeriodo7().multiply(new BigDecimal(100)), IConstantes.DECIMALES_REDONDEAR));
				}
			} else {
				this.indicadorTransaccion.setPeriodo7(null);
			}
		}

		if (aNumeroPeriodo != null && aNumeroPeriodo.intValue() == 8) {
			if (this.indicadorTransaccion.getNumerador8() != null && this.indicadorTransaccion.getDenominador8() != null) {
				if (this.indicadorTransaccion.getDenominador8().equals(new BigDecimal(0))) {
					exitoso = false;
					this.indicadorTransaccion.setDenominador8(null);
					this.indicadorTransaccion.setPeriodo8(null);
				} else {
					this.indicadorTransaccion.setPeriodo8(this.indicadorTransaccion.getNumerador8().divide(this.indicadorTransaccion.getDenominador8(), 10, RoundingMode.HALF_UP));
					this.indicadorTransaccion.setPeriodo8(this.getValorRedondeado(this.indicadorTransaccion.getPeriodo8().multiply(new BigDecimal(100)), IConstantes.DECIMALES_REDONDEAR));
				}
			} else {
				this.indicadorTransaccion.setPeriodo8(null);
			}
		}

		if (aNumeroPeriodo != null && aNumeroPeriodo.intValue() == 9) {
			if (this.indicadorTransaccion.getNumerador9() != null && this.indicadorTransaccion.getDenominador9() != null) {
				if (this.indicadorTransaccion.getDenominador9().equals(new BigDecimal(0))) {
					exitoso = false;
					this.indicadorTransaccion.setDenominador9(null);
					this.indicadorTransaccion.setPeriodo9(null);
				} else {
					this.indicadorTransaccion.setPeriodo9(this.indicadorTransaccion.getNumerador9().divide(this.indicadorTransaccion.getDenominador9(), 10, RoundingMode.HALF_UP));
					this.indicadorTransaccion.setPeriodo9(this.getValorRedondeado(this.indicadorTransaccion.getPeriodo9().multiply(new BigDecimal(100)), IConstantes.DECIMALES_REDONDEAR));
				}
			} else {
				this.indicadorTransaccion.setPeriodo9(null);
			}
		}

		if (aNumeroPeriodo != null && aNumeroPeriodo.intValue() == 10) {
			if (this.indicadorTransaccion.getNumerador10() != null && this.indicadorTransaccion.getDenominador10() != null) {
				if (this.indicadorTransaccion.getDenominador10().equals(new BigDecimal(0))) {
					exitoso = false;
					this.indicadorTransaccion.setDenominador10(null);
					this.indicadorTransaccion.setPeriodo10(null);
				} else {
					this.indicadorTransaccion.setPeriodo10(this.indicadorTransaccion.getNumerador10().divide(this.indicadorTransaccion.getDenominador10(), 10, RoundingMode.HALF_UP));
					this.indicadorTransaccion.setPeriodo10(this.getValorRedondeado(this.indicadorTransaccion.getPeriodo10().multiply(new BigDecimal(100)), IConstantes.DECIMALES_REDONDEAR));
				}
			} else {
				this.indicadorTransaccion.setPeriodo10(null);
			}
		}

		if (aNumeroPeriodo != null && aNumeroPeriodo.intValue() == 11) {
			if (this.indicadorTransaccion.getNumerador11() != null && this.indicadorTransaccion.getDenominador11() != null) {
				if (this.indicadorTransaccion.getDenominador11().equals(new BigDecimal(0))) {
					exitoso = false;
					this.indicadorTransaccion.setDenominador11(null);
					this.indicadorTransaccion.setPeriodo11(null);
				} else {
					this.indicadorTransaccion.setPeriodo11(this.indicadorTransaccion.getNumerador11().divide(this.indicadorTransaccion.getDenominador11(), 11, RoundingMode.HALF_UP));
					this.indicadorTransaccion.setPeriodo11(this.getValorRedondeado(this.indicadorTransaccion.getPeriodo11().multiply(new BigDecimal(100)), IConstantes.DECIMALES_REDONDEAR));
				}
			} else {
				this.indicadorTransaccion.setPeriodo11(null);
			}
		}

		if (aNumeroPeriodo != null && aNumeroPeriodo.intValue() == 12) {
			if (this.indicadorTransaccion.getNumerador12() != null && this.indicadorTransaccion.getDenominador12() != null) {
				if (this.indicadorTransaccion.getDenominador12().equals(new BigDecimal(0))) {
					exitoso = false;
					this.indicadorTransaccion.setDenominador12(null);
					this.indicadorTransaccion.setPeriodo12(null);
				} else {
					this.indicadorTransaccion.setPeriodo12(this.indicadorTransaccion.getNumerador12().divide(this.indicadorTransaccion.getDenominador12(), 12, RoundingMode.HALF_UP));
					this.indicadorTransaccion.setPeriodo12(this.getValorRedondeado(this.indicadorTransaccion.getPeriodo12().multiply(new BigDecimal(100)), IConstantes.DECIMALES_REDONDEAR));
				}
			} else {
				this.indicadorTransaccion.setPeriodo12(null);
			}
		}

		if (!exitoso) {
			this.mostrarMensajeGlobalPersonalizado("El denominador no puede ser cero!", "advertencia");
		}

	}

	/**
	 * Obtiene el texto de frecuencia
	 * 
	 * @return texto
	 */
	public String getTextoFrecuencia() {
		String texto = "";
		if (this.indicadorTransaccion != null && this.indicadorTransaccion.getFrecuencia() != null) {
			if (this.indicadorTransaccion.getFrecuencia().intValue() == 12) {
				texto = "Mensual";

			}
			if (this.indicadorTransaccion.getFrecuencia().intValue() == 6) {

				texto = "Bimestral (Cada 2 meses)";

			}
			if (this.indicadorTransaccion.getFrecuencia().intValue() == 4) {

				texto = "Trimestral (Cada 3 meses)";

			}
			if (this.indicadorTransaccion.getFrecuencia().intValue() == 2) {

				texto = "Semestral (Cada 6 meses)";

			}
			if (this.indicadorTransaccion.getFrecuencia().intValue() == 1) {

				texto = "Anual";
			}
		}

		return texto;

	}

	public String getTextoFrecuencia(Indicador aIndicador) {
		String texto = "";
		if (aIndicador != null && aIndicador.getFrecuencia() != null) {
			if (aIndicador.getFrecuencia().intValue() == 12) {
				texto = "Mensual";

			}
			if (aIndicador.getFrecuencia().intValue() == 6) {

				texto = "Bimestral (Cada 2 meses)";

			}
			if (aIndicador.getFrecuencia().intValue() == 4) {

				texto = "Trimestral (Cada 3 meses)";

			}
			if (aIndicador.getFrecuencia().intValue() == 2) {

				texto = "Semestral (Cada 6 meses)";

			}
			if (aIndicador.getFrecuencia().intValue() == 1) {

				texto = "Anual";
			}
		}

		return texto;

	}

	/**
	 * Arma la estructura de indicadores
	 */
	public void armarEstructuraIndicadores() {
		try {
			SimpleDateFormat mesAno = new SimpleDateFormat("yyyy-MM");
			SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
			String fechaInicioMesActual = mesAno.format(new Date()) + "-01";
			InformacionEtapaIndicador temp = new InformacionEtapaIndicador();
			ObjetivoEtapaIndicador objetivoTemp = null;
			Indicador indicadorTemp = null;
			temp.getProyectoCliente().setId(this.proyectoCliente.getId());

			// Verifica si existe un registro con este proyecto cliente
			this.informacionGeneralIndicador = IConsultasDAO.getInformacionGeneralIndicadores(temp);

			// si no encuentra es porque es nuevo
			if (this.informacionGeneralIndicador == null) {
				this.informacionGeneralIndicador = new InformacionEtapaIndicador();
				this.informacionGeneralIndicador.getProyectoCliente().setId(this.proyectoCliente.getId());
				// la fecha actual
				this.informacionGeneralIndicador.setFechaInicio(formato.parse(fechaInicioMesActual));

				// como es nuevo le carga al menos un objetivo con un indicador,
				// as� sea
				// vac�o

				crearObjetivoVacio(IConstantes.AFIRMACION);

			} else {

				this.informacionGeneralIndicador.setObjetivos(new ArrayList<ObjetivoEtapaIndicador>());

				objetivoTemp = new ObjetivoEtapaIndicador();
				objetivoTemp.getInformacionEtapaIndicador().setId(this.informacionGeneralIndicador.getId());
				this.informacionGeneralIndicador.setObjetivos(IConsultasDAO.getObjetivos(objetivoTemp));
				for (ObjetivoEtapaIndicador o : this.informacionGeneralIndicador.getObjetivos()) {
					indicadorTemp = new Indicador();
					indicadorTemp.getObjetivo().setId(o.getId());
					o.setIndicadores(new ArrayList<Indicador>());
					o.setIndicadores(IConsultasDAO.getIndicadores(indicadorTemp));
					for (Indicador i : o.getIndicadores()) {
						i.setObjetivo(o);
						i.setPlanesAccionIndicador(new ArrayList<PlanAccionIndicador>());

						i.setPlanesAccionIndicador(IConsultasDAO.getPlanesAccion(i));
						if (!(i.getPlanesAccionIndicador() != null && i.getPlanesAccionIndicador().size() > 0)) {
							i.setPlanesAccionIndicador(new ArrayList<PlanAccionIndicador>());
							PlanAccionIndicador tempPI = new PlanAccionIndicador();
							tempPI.getIndicador().setId(i.getId());
							i.getPlanesAccionIndicador().add(tempPI);

						}

					}

				}

			}

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}

	}

	public void armarEstructuraCompromiso() {
		try {

			Compromiso registroCompromiso = new Compromiso();

			registroCompromiso.getProyectoCliente().setId(this.proyectoCliente.getId());

			this.compromisos = IConsultasDAO.getCompromisos(registroCompromiso);

			// si no encuentra una planificacion esta en modo nuevo
			if (!(this.compromisos != null && this.compromisos.size() > 0)) {

				this.compromisos = new ArrayList<Compromiso>();

				Compromiso compromiso = new Compromiso();
				this.compromisos.add(compromiso);

				this.compromisoGeneral = new CompromisoGeneral();
				this.compromisoGeneral.getProyectoCliente().setId(this.proyectoCliente.getId());

			} else {

				this.compromisoGeneral = IConsultasDAO.getCompromisoGeneral(this.proyectoCliente);

			}

		} catch (

		Exception e)

		{
			IConstantes.log.error(e, e);
		}

	}

	public void armarEstructuraCronograma() {
		try {
			List<TareaProyecto> tareas = null;
			TareaProyecto tarea = null;

			Cronograma registroCronograma = new Cronograma();

			registroCronograma.getProyectoCliente().setId(this.proyectoCliente.getId());

			// si existe se debe ajustar a lo nuevo que es lo guardado a la
			// fecha, con
			// numero_etapa, producto, etc que no lo tiene

			this.cronogramas = IConsultasDAO.getCronograma(registroCronograma);

			this.itemsIndicadores = null;
			this.getItemsIndicadores();

			// si no encuentra una planificacion esta en modo nuevo
			if (!(this.cronogramas != null && this.cronogramas.size() > 0)) {

				this.cronogramas = new ArrayList<Cronograma>();

				// buscamos las tareas activas y ordenadas
				tarea = new TareaProyecto();
				tarea.setEstado(IConstantes.ACTIVO);
				tarea.getProyecto().setId(this.proyectoCliente.getProyecto().getId());
				tareas = IConsultasDAO.getTareas(tarea);
				int i = 0;
				if (tareas != null && tareas.size() > 0) {
					for (TareaProyecto p : tareas) {
						registroCronograma = new Cronograma();

						registroCronograma.getTareaProyecto().setId(p.getId());
						registroCronograma.getTareaProyecto().setTarea(p.getTarea());
						registroCronograma.getTareaProyecto().setExplicacionDocumentacion(p.getExplicacionDocumentacion());
						registroCronograma.getTareaProyecto().setProducto(p.getProducto());
						registroCronograma.getTareaProyecto().setResponsable(p.getResponsable());
						registroCronograma.getTareaProyecto().setNumeroEtapa(p.getNumeroEtapa());
						registroCronograma.getProyectoCliente().setId(this.proyectoCliente.getId());
						registroCronograma.setId(i); // es temporal no reviente

						registroCronograma.setMetaDocumentacion(new BigDecimal(0));
						registroCronograma.setMetaSocializacion(new BigDecimal(0));
						registroCronograma.setMetaImplementacion(new BigDecimal(0));
						registroCronograma.setMetaAuditoria(new BigDecimal(0));

						registroCronograma.setFechaInicioDocumentacion(this.proyectoCliente.getFechaInicio());
						registroCronograma.setFechaInicioSocializacion(this.proyectoCliente.getFechaInicio());
						registroCronograma.setFechaInicioImplementacion(this.proyectoCliente.getFechaInicio());
						registroCronograma.setFechaInicioAuditoria(this.proyectoCliente.getFechaInicio());

						registroCronograma.setFechaFinDocumentacion(this.proyectoCliente.getFechaFin());
						registroCronograma.setFechaFinSocializacion(this.proyectoCliente.getFechaFin());
						registroCronograma.setFechaFinImplementacion(this.proyectoCliente.getFechaFin());
						registroCronograma.setFechaFinAuditoria(this.proyectoCliente.getFechaFin());

						registroCronograma.setResponsableDocumentacion(this.getLabel(p.getResponsable()));
						registroCronograma.setResponsableSocializacion(this.getLabel(p.getResponsable()));
						registroCronograma.setResponsableImplementacion(this.getLabel(p.getResponsable()));
						registroCronograma.setResponsableAuditoria(this.getLabel(p.getResponsable()));

						this.cronogramas.add(registroCronograma);
						i++;
					}

					this.cronogramaGeneral = new CronogramaGeneral();
					this.cronogramaGeneral.getProyectoCliente().setId(this.proyectoCliente.getId());

					this.cronogramaGeneral.setMetaInferiorDocumentacion(new BigDecimal(0));
					this.cronogramaGeneral.setMetaInferiorSocializacion(new BigDecimal(0));
					this.cronogramaGeneral.setMetaInferiorImplementacion(new BigDecimal(0));
					this.cronogramaGeneral.setMetaInferiorAuditoria(new BigDecimal(0));

					this.cronogramaGeneral.setMetaIntermediaDocumentacion(new BigDecimal(50));
					this.cronogramaGeneral.setMetaIntermediaSocializacion(new BigDecimal(50));
					this.cronogramaGeneral.setMetaIntermediaImplementacion(new BigDecimal(50));
					this.cronogramaGeneral.setMetaIntermediaAuditoria(new BigDecimal(50));

					this.cronogramaGeneral.setMetaSuperiorDocumentacion(new BigDecimal(100));
					this.cronogramaGeneral.setMetaSuperiorSocializacion(new BigDecimal(100));
					this.cronogramaGeneral.setMetaSuperiorImplementacion(new BigDecimal(100));
					this.cronogramaGeneral.setMetaSuperiorAuditoria(new BigDecimal(100));

				} else {

					this.planificacion = null;
					this.planificacionGeneral = new PlanificacionGeneral();
					this.mostrarMensajeGlobalPersonalizado("No existen actividades cargadas en el proyecto para realizar/mostrar esta etapa ", "advertencia");
					return;
				}

			} else {

				this.cronogramaGeneral = IConsultasDAO.getCronogramaGeneral(this.proyectoCliente);

				for (Cronograma p : this.cronogramas) {
					TareaProyecto tareaT = new TareaProyecto();
					tareaT.setId(p.getTareaProyecto().getId());
					p.setTareaProyecto(IConsultasDAO.getTareas(tareaT).get(0));

					p.settDocumentos(new ArrayList<DocumentoCronograma>());
					DocumentoCronograma doc = new DocumentoCronograma();
					doc.getCronograma().setId(p.getId());
					doc.setEtapa(this.etapaCompartida);
					p.settDocumentos(IConsultasDAO.getDocumentosCronograma(doc));

				}

			}

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}

	}

	public void armarEstructuraDocumentacion() {
		try {
			List<TareaProyecto> tareas = null;
			TareaProyecto tarea = null;

			Documentacion registroDocumentacion = new Documentacion();

			registroDocumentacion.getProyectoCliente().setId(this.proyectoCliente.getId());

			// si existe se debe ajustar a lo nuevo que es lo guardado a la
			// fecha, con
			// numero_etapa, producto, etc que no lo tiene

			this.documentaciones = IConsultasDAO.getDocumentacion(registroDocumentacion);

			// si no encuentra una planificacion esta en modo nuevo
			if (!(this.documentaciones != null && this.documentaciones.size() > 0)) {

				this.documentaciones = new ArrayList<Documentacion>();

				// buscamos las tareas activas y ordenadas
				tarea = new TareaProyecto();
				tarea.setEstado(IConstantes.ACTIVO);
				tarea.getProyecto().setId(this.proyectoCliente.getProyecto().getId());
				tareas = IConsultasDAO.getTareas(tarea);
				int i = 0;
				if (tareas != null && tareas.size() > 0) {
					for (TareaProyecto p : tareas) {
						registroDocumentacion = new Documentacion();

						registroDocumentacion.getTareaProyecto().setId(p.getId());
						registroDocumentacion.getTareaProyecto().setTarea(p.getTarea());
						registroDocumentacion.getTareaProyecto().setExplicacionDocumentacion(p.getExplicacionDocumentacion());
						registroDocumentacion.getTareaProyecto().setProducto(p.getProducto());
						registroDocumentacion.getTareaProyecto().setResponsable(p.getResponsable());
						registroDocumentacion.getTareaProyecto().setNumeroEtapa(p.getNumeroEtapa());
						registroDocumentacion.getProyectoCliente().setId(this.proyectoCliente.getId());
						registroDocumentacion.setId(i); // es temporal no reviente
						registroDocumentacion.setExplicacionConsultor(p.getExplicacionDocumentacion());

						this.documentaciones.add(registroDocumentacion);
						i++;
					}

					this.documentacionGeneral = new DocumentacionGeneral();
					this.documentacionGeneral.getProyectoCliente().setId(this.proyectoCliente.getId());

				} else {

					this.documentaciones = null;
					this.documentacionGeneral = new DocumentacionGeneral();
					this.mostrarMensajeGlobalPersonalizado("No existen actividades cargadas en el proyecto para realizar/mostrar esta etapa ", "advertencia");
					return;
				}

			} else {

				this.documentacionGeneral = IConsultasDAO.getDocumentacionGeneral(this.proyectoCliente);

				for (Documentacion p : this.documentaciones) {
					TareaProyecto tareaT = new TareaProyecto();
					tareaT.setId(p.getTareaProyecto().getId());
					p.setTareaProyecto(IConsultasDAO.getTareas(tareaT).get(0));

				}

			}

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}

	}

	/**
	 * Arma la estructura de la planificaci�n
	 */
	public void armarEstructuraPlanificacion() {
		try {
			List<TareaProyecto> tareas = null;
			TareaProyecto tarea = null;
			Planificacion registroPlanificacion = new Planificacion();

			registroPlanificacion.getProyectoCliente().setId(this.proyectoCliente.getId());

			// si existe se debe ajustar a lo nuevo que es lo guardado a la
			// fecha, con
			// numero_etapa, producto, etc que no lo tiene
			this.planificacion = IConsultasDAO.getPlanificacion(registroPlanificacion);

			// si no encuentra una planificacion esta en modo nuevo
			if (!(this.planificacion != null && this.planificacion.size() > 0)) {

				this.planificacion = new ArrayList<Planificacion>();

				// buscamos las tareas activas y ordenadas
				tarea = new TareaProyecto();
				tarea.setEstado(IConstantes.ACTIVO);
				tarea.getProyecto().setId(this.proyectoCliente.getProyecto().getId());
				tareas = IConsultasDAO.getTareas(tarea);
				int i = 0;
				if (tareas != null && tareas.size() > 0) {
					for (TareaProyecto p : tareas) {
						registroPlanificacion = new Planificacion();
						registroPlanificacion.getTareaProyecto().setId(p.getId());
						registroPlanificacion.getTareaProyecto().setTarea(p.getTarea());
						registroPlanificacion.getTareaProyecto().setExplicacionDocumentacion(p.getExplicacionDocumentacion());
						registroPlanificacion.getTareaProyecto().setProducto(p.getProducto());
						registroPlanificacion.getTareaProyecto().setResponsable(p.getResponsable());
						registroPlanificacion.getTareaProyecto().setNumeroEtapa(p.getNumeroEtapa());
						registroPlanificacion.getProyectoCliente().setId(this.proyectoCliente.getId());

						registroPlanificacion.settTiemposPlanificacion(new ArrayList<TiempoPlanificacion>());
						registroPlanificacion.settTiemposPlanificacion(this.getTiemposEntreFechas());
						registroPlanificacion.setId(i); // es temporapara que
						// cuando sea
						// nuevo no se reviente
						// el modo de
						// selecci�n
						this.planificacion.add(registroPlanificacion);
						i++;
					}

					this.planificacionGeneral = new PlanificacionGeneral();
					this.planificacionGeneral.setEstadoActual("P"); // programado
					this.planificacionGeneral.getProyectoCliente().setId(this.proyectoCliente.getId());

				} else {

					this.planificacion = null;
					this.planificacionGeneral = new PlanificacionGeneral();
					this.mostrarMensajeGlobalPersonalizado("No existen actividades cargadas en el proyecto para realizar/mostrar esta etapa ", "advertencia");
					return;
				}

			} else {

				this.planificacionGeneral = IConsultasDAO.getPlanificacionGeneral(this.proyectoCliente);

				for (Planificacion p : this.planificacion) {
					TareaProyecto tareaT = new TareaProyecto();
					tareaT.setId(p.getTareaProyecto().getId());

					p.setTareaProyecto(IConsultasDAO.getTareas(tareaT).get(0));

					p.settTiemposPlanificacion(new ArrayList<TiempoPlanificacion>());
					p.settTiemposPlanificacion(this.getTiemposEntreFechas());

					for (TiempoPlanificacion t : p.gettTiemposPlanificacion()) {
						t.getPlanificacion().setId(p.getId());
						List<TiempoPlanificacion> temps = IConsultasDAO.getTiemposPlanificacion(t);
						if (temps != null && temps.size() > 0) {
							t.setId(temps.get(0).getId());
							if (temps.get(0).isProgramado()) {
								t.setProgramado(true);
							}
							if (temps.get(0).isEjecutado()) {
								t.setEjecutado(true);
							}
						}
					}

				}

			}

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}

	}

	/**
	 * Arma la estructura del diagnostico
	 */
	public void armarEstructuraDiagnostico() {

		try {
			List<PreguntaProyecto> preguntasProyecto = null;
			List<EstadoProyectoCliente> estadosProyecto = null;
			List<EstadoDiagnostico> tempEstadosDiagnostico = null;
			List<Estado> estados = null;
			EstadoDiagnostico estadoDiagnostico = null;
			int contadorRegistrosDiagnistico = 0;

			EstadoProyectoCliente estado = null;
			Estado tempEstado = null;
			PreguntaProyecto pregunta = null;
			Diagnostico registroDiagnostico = new Diagnostico();
			this.personaDiagnostico = null;
			this.getPersonaDiagnostico();

			registroDiagnostico.getProyectoCliente().setId(this.proyectoCliente.getId());
			this.diagnostico = IConsultasDAO.getDiagnostico(registroDiagnostico);

			this.personaDiagnostico = IConsultasDAO.getPersonaDiagnostico(this.proyectoCliente.getId());

			if (this.personaDiagnostico == null) {
				this.personaDiagnostico = new PersonaDiagnostico();
				this.personaDiagnostico.getProyectoCliente().setId(this.proyectoCliente.getId());
				this.personaDiagnostico.setPersonaEntrevistada(this.proyectoCliente.getCliente().getRepresentante());
				this.personaDiagnostico.setCargoEntrevistado("LIDER DE SOSTENIMIENTO");
				this.personaDiagnostico.setFecha(new Date());
			}

			// si encuentra un diagnostico esta en modo edici�n
			if (this.diagnostico != null && this.diagnostico.size() > 0) {

				// cada registro de diagnistico lo arma con sus estados
				// seleccionados o
				// no
				estado = new EstadoProyectoCliente();
				estado.getProyectoCliente().setId(this.proyectoCliente.getId());
				estadosProyecto = IConsultasDAO.getEstadosProyectoCliente(estado);
				this.proyectoCliente.settEstadosProyecto(new ArrayList<EstadoProyectoCliente>());
				this.proyectoCliente.gettEstadosProyecto().addAll(estadosProyecto);

				if (estadosProyecto != null && estadosProyecto.size() > 0) {
					for (Diagnostico d : this.diagnostico) {

						d.settEstadosDiagnostico(new ArrayList<EstadoDiagnostico>());
						tempEstadosDiagnostico = IConsultasDAO.getEstadosDiagnostico(d);

						for (EstadoProyectoCliente p : estadosProyecto) {

							estadoDiagnostico = new EstadoDiagnostico();
							estadoDiagnostico.getDiagnostico().setId(d.getId());
							estadoDiagnostico.setEstado(p.getEstado());
							if (tempEstadosDiagnostico != null && tempEstadosDiagnostico.size() > 0 && tempEstadosDiagnostico.stream().anyMatch(h -> h.getEstado().getId().intValue() == p.getEstado().getId().intValue())) {
								estadoDiagnostico.settSeleccionado(true);
							} else {
								estadoDiagnostico.settSeleccionado(false);
							}

							d.gettEstadosDiagnostico().add(estadoDiagnostico);
						}

						contadorRegistrosDiagnistico++;
						d.settIdRegistro(contadorRegistrosDiagnistico);

						d.settDocumentos(new ArrayList<DocumentoDiagnostico>());
						DocumentoDiagnostico doc = new DocumentoDiagnostico();
						doc.getDiagnostico().setId(d.getId());
						d.settDocumentos(IConsultasDAO.getDocumentosDiagnostico(doc));

					}

				}

			} else {
				// caso contrario todo es nuevo

				this.diagnostico = new ArrayList<Diagnostico>();

				// buscamos las preguntas activas y ordenadas
				pregunta = new PreguntaProyecto();
				pregunta.setEstado(IConstantes.ACTIVO);
				pregunta.getProyecto().setId(this.proyectoCliente.getProyecto().getId());
				preguntasProyecto = IConsultasDAO.getPreguntas(pregunta);

				// buscamos los estados vigentes para asignarlos a estados
				// proyecto

				this.proyectoCliente.settEstadosProyecto(new ArrayList<EstadoProyectoCliente>());
				tempEstado = new Estado();
				tempEstado.setIndicativoVigencia(IConstantes.ACTIVO);
				estados = IConsultasDAO.getEstados(tempEstado);

				EstadoDiagnostico en = null;

				if (estados != null && estados.size() > 0) {

					for (Estado e : estados) {
						estado = new EstadoProyectoCliente();
						estado.setEstado(e);
						estado.getProyectoCliente().setId(this.proyectoCliente.getId());
						this.proyectoCliente.gettEstadosProyecto().add(estado);
					}

					if (preguntasProyecto != null && preguntasProyecto.size() > 0) {
						for (PreguntaProyecto p : preguntasProyecto) {
							registroDiagnostico = new Diagnostico();
							registroDiagnostico.setPreguntaProyecto(p);
							registroDiagnostico.getProyectoCliente().setId(this.proyectoCliente.getId());
							registroDiagnostico.settEstadosDiagnostico(new ArrayList<EstadoDiagnostico>());

							en = null;
							for (EstadoProyectoCliente e : this.proyectoCliente.gettEstadosProyecto()) {

								estadoDiagnostico = new EstadoDiagnostico();
								estadoDiagnostico.getDiagnostico().setId(e.getId());
								estadoDiagnostico.setEstado(e.getEstado());

								if (e.getEstado() != null && e.getEstado().getId() != null && e.getEstado().getId().intValue() == IConstantes.ID_ESTADO_CUMPLE.intValue()) {

									en = estadoDiagnostico;

									// por defecto cumple
									estadoDiagnostico.settSeleccionado(true);
									// registroDiagnostico.setEvidenciaEncontrada("CUMPLE");
									// registroDiagnostico.setAccionesRealizar("NO APLICA");
									// registroDiagnostico.setAnalisisCausa("NO APLICA");
								} else {
									estadoDiagnostico.settSeleccionado(false);
								}

								registroDiagnostico.gettEstadosDiagnostico().add(estadoDiagnostico);

							}
							contadorRegistrosDiagnistico++;
							registroDiagnostico.settIdRegistro(contadorRegistrosDiagnistico);

							if (en != null) {
								vaidarNoAplica(registroDiagnostico.gettEstadosDiagnostico(), en, registroDiagnostico);
							}

							this.diagnostico.add(registroDiagnostico);

						}
					}
				}

			}

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}

	}

	/**
	 * Consulta todos los datos para la etapa de indicadores
	 */
	public void consultarDatosParaIndicadores() {

		try {
			if (this.proyectoCliente != null && this.proyectoCliente.getId() != null) {
				ProyectoCliente proyecto = new ProyectoCliente();
				proyecto.setId(this.proyectoCliente.getId());
				this.proyectoCliente = IConsultasDAO.getProyectosCliente(proyecto).get(0);
				this.armarEstructuraIndicadores();
			}

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
	}

	public void consultarDatosParaCompromisos() {

		try {
			if (this.proyectoCliente != null && this.proyectoCliente.getId() != null) {
				ProyectoCliente proyecto = new ProyectoCliente();
				proyecto.setId(this.proyectoCliente.getId());
				this.proyectoCliente = IConsultasDAO.getProyectosCliente(proyecto).get(0);
				this.armarEstructuraCompromiso();
			}

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
	}

	public void consultarDatosParaDocumentacion() {

		try {
			if (this.proyectoCliente != null && this.proyectoCliente.getId() != null) {
				ProyectoCliente proyecto = new ProyectoCliente();
				proyecto.setId(this.proyectoCliente.getId());
				this.proyectoCliente = IConsultasDAO.getProyectosCliente(proyecto).get(0);
				this.armarEstructuraDocumentacion();
			}

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
	}

	public void consultarDatosParaCronograma() {

		try {
			if (this.proyectoCliente != null && this.proyectoCliente.getId() != null) {
				ProyectoCliente proyecto = new ProyectoCliente();
				proyecto.setId(this.proyectoCliente.getId());
				this.proyectoCliente = IConsultasDAO.getProyectosCliente(proyecto).get(0);
				this.armarEstructuraCronograma();
			}

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
	}

	public void consultarDatosParaAgenda() {

		try {
			if (this.proyectoCliente != null && this.proyectoCliente.getId() != null) {
				ProyectoCliente proyecto = new ProyectoCliente();
				proyecto.setId(this.proyectoCliente.getId());
				this.proyectoCliente = IConsultasDAO.getProyectosCliente(proyecto).get(0);
				// lo cambia y lo asigna
				this.citaSeleccionada.setProyectoCliente(this.proyectoCliente);
			}

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
	}

	/**
	 * Consulta todos los datos para la etapa de planificaci�n
	 */
	public void consultarDatosParaPlanificacion() {

		try {
			if (this.proyectoCliente != null && this.proyectoCliente.getId() != null) {
				ProyectoCliente proyecto = new ProyectoCliente();
				proyecto.setId(this.proyectoCliente.getId());
				this.proyectoCliente = IConsultasDAO.getProyectosCliente(proyecto).get(0);
				this.armarEstructuraPlanificacion();
			}

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
	}

	/**
	 * Consulta los datos completos de un proyecto de consultor�a de un cliente
	 * 
	 * @param aProyectoCliente
	 */
	public void consultarDatosCompletosConsultoria() {

		try {
			
			this.hacerOnComplete = "";
			if(abiertoCerrado!=null && abiertoCerrado.equals("A")){
				
				//this.hacerOnComplete = "PF('dtlRegistros').clearFilters()";
				abiertoCerrado = null;
			}
			
			
			if (this.proyectoCliente != null && this.proyectoCliente.getId() != null) {
				ProyectoCliente proyecto = new ProyectoCliente();
				proyecto.setId(this.proyectoCliente.getId());
				this.proyectoCliente = IConsultasDAO.getProyectosCliente(proyecto).get(0);
				this.armarEstructuraDiagnostico();
				
				
			}

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
	}

	/**
	 * Obtiene el cliente analizado
	 * 
	 * @return cliente
	 */
	public Cliente getCliente() {
		try {
			if (this.cliente == null) {
				this.cliente = new Cliente();
			}
		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
		return cliente;
	}

	/**
	 * Establece el cliente analizado
	 * 
	 * @param cliente
	 */
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	/**
	 * Obtiene el proyecto cliente
	 * 
	 * @return proyectoCliente
	 */
	public ProyectoCliente getProyectoCliente() {
		try {
			if (this.proyectoCliente == null) {
				this.proyectoCliente = new ProyectoCliente();
			}
		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
		return proyectoCliente;
	}

	/**
	 * Establece el proyecto cliente
	 * 
	 * @param proyectoCliente
	 */
	public void setProyectoCliente(ProyectoCliente proyectoCliente) {
		this.proyectoCliente = proyectoCliente;
	}

	// get/set

	/**
	 * Obtiene el registro de planificaci�n
	 * 
	 * @return registroPlanificacion seleccionado
	 */
	public Planificacion getRegistroPlanificacion() {
		try {
			if (this.registroPlanificacion == null) {
				this.registroPlanificacion = new Planificacion();
			}
		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
		return registroPlanificacion;
	}

	/**
	 * Establece el registro de planificaci�n
	 * 
	 * @param registroPlanificacion
	 */
	public void setRegistroPlanificacion(Planificacion registroPlanificacion) {
		this.registroPlanificacion = registroPlanificacion;
	}

	/**
	 * Obtiene informaci�n general de un indicador
	 * 
	 * @return informacionGeneralIndicador
	 */
	public InformacionEtapaIndicador getInformacionGeneralIndicador() {
		try {
			if (this.informacionGeneralIndicador == null) {
				this.informacionGeneralIndicador = new InformacionEtapaIndicador();
			}
		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
		return informacionGeneralIndicador;
	}

	public void setInformacionGeneralIndicador(InformacionEtapaIndicador informacionGeneralIndicador) {
		this.informacionGeneralIndicador = informacionGeneralIndicador;
	}

	/**
	 * Obtiene planificaci�n general
	 * 
	 * @return registroPlanificacion
	 */
	public PlanificacionGeneral getPlanificacionGeneral() {
		try {
			if (this.registroPlanificacion == null) {
				this.registroPlanificacion = new Planificacion();
			}
		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}

		return planificacionGeneral;
	}

	/**
	 * Establece planificaci�n general
	 * 
	 * @param planificacionGeneral
	 */
	public void setPlanificacionGeneral(PlanificacionGeneral planificacionGeneral) {
		this.planificacionGeneral = planificacionGeneral;
	}

	public CronogramaGeneral getCronogramaGeneral() {
		try {
			if (this.cronogramaGeneral == null) {
				this.cronogramaGeneral = new CronogramaGeneral();
			}
		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
		return cronogramaGeneral;
	}

	public void setCronogramaGeneral(CronogramaGeneral cronogramaGeneral) {
		this.cronogramaGeneral = cronogramaGeneral;
	}

	// listas
	/**
	 * Obtiene el listado de clientes que puede manipular el diagn�stico
	 * 
	 * @return itemsClientes
	 */
	public List<SelectItem> getItemsClientes() {
		try {
			if (this.itemsClientes == null) {
				this.itemsClientes = new ArrayList<SelectItem>();
				ProyectoCliente proyectoCliente = new ProyectoCliente();

				if (this.administrarSesionCliente != null && this.administrarSesionCliente.getPersonalSesion() != null && this.administrarSesionCliente.getPersonalSesion().getId() != null && this.administrarSesionCliente.getPersonalSesion().getTipoUsuario().equals("CO")) {
					proyectoCliente.getConsultor().setId(this.administrarSesionCliente.getPersonalSesion().getId());
					proyectoCliente.setCliente(null);
				} else {

					if (this.administrarSesionCliente != null && this.administrarSesionCliente.getPersonalSesion() != null && this.administrarSesionCliente.getPersonalSesion().getId() != null && this.administrarSesionCliente.getPersonalSesion().getTipoUsuario().equals("CL")) {
						proyectoCliente.getCliente().setId(this.administrarSesionCliente.getPersonalSesion().getId());
						proyectoCliente.setConsultor(null);
					} else {
						proyectoCliente.setCliente(null);
						proyectoCliente.setConsultor(null);

					}
				}

				List<Cliente> clientes = IConsultasDAO.getClientesNormas(proyectoCliente);

				this.itemsClientes.add(new SelectItem("", this.getMensaje("comboVacio")));
				if (clientes != null && clientes.size() > 0) {

					if (clientes.stream().anyMatch(p -> p.gettEstado() != null && p.gettEstado().equals(IConstantes.ACTIVO))) {
						SelectItemGroup g1 = new SelectItemGroup(this.getMensaje("clientesProyectosActivos"));
						List<SelectItem> itemsG1 = new ArrayList<SelectItem>();
						clientes.stream().filter(p -> p.gettEstado() != null && p.gettEstado().equals(IConstantes.ACTIVO)).forEach(p -> itemsG1.add(new SelectItem(p.getId(), p.getCliente())));
						g1.setSelectItems(itemsG1.stream().toArray(SelectItem[]::new));
						this.itemsClientes.add(g1);
					}

					if (clientes.stream().anyMatch(p -> p.gettEstado() != null && p.gettEstado().equals(IConstantes.INACTIVO))) {
						SelectItemGroup g2 = new SelectItemGroup(this.getMensaje("clientesProyectosInactivos"));
						List<SelectItem> itemsG2 = new ArrayList<SelectItem>();
						clientes.stream().filter(p -> p.gettEstado() != null && p.gettEstado().equals(IConstantes.INACTIVO)).forEach(p -> itemsG2.add(new SelectItem(p.getId(), p.getCliente())));
						g2.setSelectItems(itemsG2.stream().toArray(SelectItem[]::new));
						this.itemsClientes.add(g2);
					}

				}

			}
		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
		return itemsClientes;
	}

	/**
	 * Establece el listado de clientes
	 * 
	 * @param itemsClientes
	 */
	public void setItemsClientes(List<SelectItem> itemsClientes) {
		this.itemsClientes = itemsClientes;
	}

	/**
	 * Obtiene los proyectos
	 * 
	 * @return itemsProyectosCliente
	 */
	public List<SelectItem> getItemsProyectosCliente() {
		try {

			this.itemsProyectosCliente = new ArrayList<SelectItem>();
			this.itemsProyectosCliente.add(new SelectItem("", this.getMensaje("comboVacio")));
			ProyectoCliente proyectoCliente = new ProyectoCliente();

			if (this.cliente != null && this.cliente.getId() != null) {
				proyectoCliente.setCliente(this.cliente);

				if (this.administrarSesionCliente != null && this.administrarSesionCliente.getPersonalSesion() != null && this.administrarSesionCliente.getPersonalSesion().getId() != null && this.administrarSesionCliente.getPersonalSesion().getTipoUsuario().equals("CO")) {
					proyectoCliente.getConsultor().setId(this.administrarSesionCliente.getPersonalSesion().getId());
				} else {

					proyectoCliente.setConsultor(null);
				}

				List<ProyectoCliente> proyectoClientes = IConsultasDAO.getProyectosClientesNormas(proyectoCliente);

				if (proyectoClientes != null && proyectoClientes.size() > 0) {

					if (proyectoClientes.stream().anyMatch(p -> p.gettEstado() != null && p.gettEstado().equals(IConstantes.ACTIVO))) {
						SelectItemGroup g1 = new SelectItemGroup(this.getMensaje("proyectosActivos"));
						List<SelectItem> itemsG1 = new ArrayList<SelectItem>();
						proyectoClientes.stream().filter(p -> p.gettEstado() != null && p.gettEstado().equals(IConstantes.ACTIVO)).forEach(p -> itemsG1.add(new SelectItem(p.getId(), p.getProyecto().getNombre())));
						g1.setSelectItems(itemsG1.stream().toArray(SelectItem[]::new));
						this.itemsProyectosCliente.add(g1);
					}

					if (proyectoClientes.stream().anyMatch(p -> p.gettEstado() != null && p.gettEstado().equals(IConstantes.INACTIVO))) {
						SelectItemGroup g2 = new SelectItemGroup(this.getMensaje("proyectosInctivos"));
						List<SelectItem> itemsG2 = new ArrayList<SelectItem>();
						proyectoClientes.stream().filter(p -> p.gettEstado() != null && p.gettEstado().equals(IConstantes.INACTIVO)).forEach(p -> itemsG2.add(new SelectItem(p.getId(), p.getProyecto().getNombre())));
						g2.setSelectItems(itemsG2.stream().toArray(SelectItem[]::new));
						this.itemsProyectosCliente.add(g2);
					}

				}
			}

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
		return itemsProyectosCliente;
	}

	/**
	 * Establece los proyectos
	 * 
	 * @param itemsProyectosCliente
	 */
	public void setItemsProyectosCliente(List<SelectItem> itemsProyectosCliente) {
		this.itemsProyectosCliente = itemsProyectosCliente;
	}

	/**
	 * Obtiene el listado de diagnostico
	 * 
	 * @return diagnostico
	 */
	public List<Diagnostico> getDiagnostico() {
		return diagnostico;
	}

	/**
	 * Establece el listado de diagnostico
	 * 
	 * @param diagnostico
	 */
	public void setDiagnostico(List<Diagnostico> diagnostico) {
		this.diagnostico = diagnostico;
	}

	/**
	 * Obtiene un listado de planificaci�n
	 * 
	 * @return planificacion
	 */
	public List<Planificacion> getPlanificacion() {
		return planificacion;
	}

	/**
	 * Establece un listado de planificaci�n
	 * 
	 * @param planificacion
	 */
	public void setPlanificacion(List<Planificacion> planificacion) {
		this.planificacion = planificacion;
	}

	// /**
	// * Obtiene los documentos de una actividad
	// *
	// * @return documentos
	// */
	// public List<DocumentoActividad> getDocumentos() {
	// try {
	// if (this.documentos == null && this.planificacionSeleccionada != null &&
	// this.planificacionSeleccionada.getTareaProyecto() != null &&
	// this.planificacionSeleccionada.getTareaProyecto().getId() != null) {
	// DocumentoActividad doc = new DocumentoActividad();
	// doc.getTareaProyecto().setId(this.planificacionSeleccionada.getTareaProyecto().getId());
	// this.documentos = IConsultasDAO.getDocumentos(doc);
	//
	// }
	// } catch (Exception e) {
	// IConstantes.log.error(e, e);
	// }
	// return documentos;
	// }
	//
	// /**
	// * Establece los documentos
	// *
	// * @param documentos
	// */
	// public void setDocumentos(List<DocumentoActividad> documentos) {
	// this.documentos = documentos;
	// }

	/**
	 * Obtiene los datos actuales del gr�fico
	 * 
	 * @return graficoBarras
	 */
	public BarChartModel getGraficoBarras() {
		ChartSeries estados = new ChartSeries();
		this.graficoBarras = null;
		int numeroEstados = 0;
		if (this.proyectoCliente != null && this.proyectoCliente.gettEstadosProyecto() != null && this.proyectoCliente.gettEstadosProyecto().size() > 0) {
			this.graficoBarras = new BarChartModel();
			estados.setLabel(this.getMensaje("estadosLabel"));
			for (EstadoProyectoCliente e : this.proyectoCliente.gettEstadosProyecto()) {
				numeroEstados = 0;
				for (Diagnostico d : this.diagnostico) {
					numeroEstados += d.gettEstadosDiagnostico().stream().filter(ed -> ed.istSeleccionado() && ed.getEstado().getId().intValue() == e.getEstado().getId().intValue()).count();
				}
				estados.set(e.getEstado().getNombre(), numeroEstados);
			}

			this.graficoBarras.addSeries(estados);

			this.graficoBarras.setTitle(this.getMensaje("totalEstadosDiagnostico"));
			this.graficoBarras.setAnimate(true);
			this.graficoBarras.setLegendPosition("nw");
			Axis ejeX = this.graficoBarras.getAxis(AxisType.X);
			ejeX.setTickAngle(-30);
		}

		return graficoBarras;
	}

	public void setGraficoBarras(BarChartModel graficoBarras) {
		this.graficoBarras = graficoBarras;
	}

	public PersonaDiagnostico getPersonaDiagnostico() {
		try {
			if (this.personaDiagnostico == null) {
				this.personaDiagnostico = new PersonaDiagnostico();
			}
		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
		return personaDiagnostico;
	}

	public void setPersonaDiagnostico(PersonaDiagnostico personaDiagnostico) {
		this.personaDiagnostico = personaDiagnostico;
	}

	public Planificacion getPlanificacionSeleccionada() {
		try {
			if (this.planificacionSeleccionada == null) {
				this.planificacionSeleccionada = new Planificacion();
			}
		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
		return planificacionSeleccionada;
	}

	public void setPlanificacionSeleccionada(Planificacion planificacionSeleccionada) {
		this.planificacionSeleccionada = planificacionSeleccionada;
	}

	// /**
	// *
	// * @return documentoActividadTransaccion
	// */
	// public DocumentoActividad getDocumentoActividadTransaccion() {
	// try {
	// if (this.documentoActividadTransaccion == null) {
	// this.documentoActividadTransaccion = new DocumentoActividad();
	// if (this.planificacionSeleccionada != null &&
	// this.planificacionSeleccionada.getTareaProyecto() != null &&
	// this.planificacionSeleccionada.getTareaProyecto().getId() != null) {
	// this.documentoActividadTransaccion.getTareaProyecto().setId(this.planificacionSeleccionada.getTareaProyecto().getId());
	// }
	// }
	// } catch (Exception e) {
	// IConstantes.log.error(e, e);
	// }
	// return documentoActividadTransaccion;
	// }
	//
	// public void setDocumentoActividadTransaccion(DocumentoActividad
	// documentoActividadTransaccion) {
	// this.documentoActividadTransaccion = documentoActividadTransaccion;
	// }

	/**
	 * 
	 * @return objetivoTransaccion
	 */
	public ObjetivoEtapaIndicador getObjetivoTransaccion() {
		try {

			if (this.objetivoTransaccion == null) {
				this.objetivoTransaccion = new ObjetivoEtapaIndicador();
			}

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
		return objetivoTransaccion;
	}

	public void setObjetivoTransaccion(ObjetivoEtapaIndicador objetivoTransaccion) {
		this.objetivoTransaccion = objetivoTransaccion;
	}

	/**
	 * 
	 * @return indicadorTransaccion
	 */
	public Indicador getIndicadorTransaccion() {
		try {

			if (this.indicadorTransaccion == null) {
				this.indicadorTransaccion = new Indicador();
			}

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
		return indicadorTransaccion;
	}

	public void setIndicadorTransaccion(Indicador indicadorTransaccion) {
		this.indicadorTransaccion = indicadorTransaccion;
	}

	public String getParametroCliente() {
		return parametroCliente;
	}

	public void setParametroCliente(String parametroCliente) {
		this.parametroCliente = parametroCliente;
	}

	public String getParametroProyecto() {
		// try {
		// if (this.parametroCliente != null && this.parametroProyecto != null)
		// {
		//
		// this.cliente.setId(Integer.parseInt(this.parametroCliente));
		// this.proyectoCliente.setId(Integer.parseInt(this.parametroProyecto));
		//
		// consultarDatosCompletosConsultoria();
		// }
		// } catch (Exception e) {
		// IConstantes.log.error(e, e);
		// }
		return parametroProyecto;
	}

	public void setParametroProyecto(String parametroProyecto) {
		this.parametroProyecto = parametroProyecto;
	}

	public AdministrarSesionCliente getAdministrarSesionCliente() {
		return administrarSesionCliente;
	}

	public void setAdministrarSesionCliente(AdministrarSesionCliente administrarSesionCliente) {
		this.administrarSesionCliente = administrarSesionCliente;
	}

	public List<SelectItem> getItemsMesesAno() {
		return itemsMesesAno;
	}

	public void setItemsMesesAno(List<SelectItem> itemsMesesAno) {
		this.itemsMesesAno = itemsMesesAno;
	}

	public BarChartModel getGraficoIndicador() {
		return graficoIndicador;
	}

	public void setGraficoIndicador(BarChartModel graficoIndicador) {
		this.graficoIndicador = graficoIndicador;
	}

	public List<Cronograma> getCronogramas() {
		return cronogramas;
	}

	public void setCronogramas(List<Cronograma> cronogramas) {
		this.cronogramas = cronogramas;
	}

	public List<Documentacion> getDocumentaciones() {
		return documentaciones;
	}

	public void setDocumentaciones(List<Documentacion> documentaciones) {
		this.documentaciones = documentaciones;
	}

	public List<Compromiso> getCompromisos() {
		return compromisos;
	}

	public void setCompromisos(List<Compromiso> compromisos) {
		this.compromisos = compromisos;
	}

	public CompromisoGeneral getCompromisoGeneral() {

		try {

			if (this.compromisoGeneral == null) {
				this.compromisoGeneral = new CompromisoGeneral();
			}

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
		return compromisoGeneral;
	}

	public void setCompromisoGeneral(CompromisoGeneral compromisoGeneral) {
		this.compromisoGeneral = compromisoGeneral;
	}

	public DocumentacionGeneral getDocumentacionGeneral() {
		try {

			if (this.documentacionGeneral == null) {
				this.documentacionGeneral = new DocumentacionGeneral();
			}

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
		return documentacionGeneral;
	}

	public void setDocumentacionGeneral(DocumentacionGeneral documentacionGeneral) {
		this.documentacionGeneral = documentacionGeneral;
	}

	public Documentacion getDocumentacionTransaccion() {
		try {

			if (this.documentacionTransaccion == null) {
				this.documentacionTransaccion = new Documentacion();
			}

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
		return documentacionTransaccion;
	}

	public void setDocumentacionTransaccion(Documentacion documentacionTransaccion) {
		this.documentacionTransaccion = documentacionTransaccion;
	}

	/**
	 * Obtiene el tipo de archivo
	 * 
	 * @param aNombre
	 * @return tipo
	 */
	private String getTipoArchivo(String aNombre) {
		String tipo = "";
		int ultimoPunto = 0;
		try {

			ultimoPunto = aNombre.lastIndexOf('.');
			tipo = aNombre.substring(ultimoPunto + 1);

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
		return tipo;
	}

	public void recibirArchivoCronograma(FileUploadEvent event) {

		try {
			this.documentoCronograma.settFile(event.getFile());
			this.documentoCronograma.setArchivo(event.getFile().getContents());
			this.documentoCronograma.setContentType(event.getFile().getContentType());
			this.documentoCronograma.setExtensionArchivo(this.getTipoArchivo(event.getFile().getFileName()));
			this.mostrarMensajeGlobal("archivoRecibido", "advertencia");
		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}

	}

	/**
	 * Recibir el archivo y lo asigna al objeto
	 * 
	 * @param event
	 */
	public void recibirArchivoDiagnostico(FileUploadEvent event) {

		try {
			this.documentoDiagnostico.settFile(event.getFile());
			this.documentoDiagnostico.setArchivo(event.getFile().getContents());
			this.documentoDiagnostico.setContentType(event.getFile().getContentType());
			this.documentoDiagnostico.setExtensionArchivo(this.getTipoArchivo(event.getFile().getFileName()));
			this.mostrarMensajeGlobal("archivoRecibido", "advertencia");
		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}

	}

	/**
	 * Recibir el archivo y lo asigna al objeto
	 * 
	 * @param event
	 */
	public void recibirArchivo(FileUploadEvent event) {

		try {
			this.documentoActividad.settFile(event.getFile());
			this.documentoActividad.setArchivo(event.getFile().getContents());

			this.documentoActividad.setContentType(event.getFile().getContentType());
			this.documentoActividad.setExtensionArchivo(this.getTipoArchivo(event.getFile().getFileName()));

			this.mostrarMensajeGlobalPersonalizado("Archivo recibido, guárdelo.", "advertencia");
		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}

	}

	/**
	 * Valida la creaci�n de un documento
	 * 
	 * @return ok
	 */
	private boolean isValidoDocumento() {
		boolean ok = true;

		if (this.isVacio(this.documentoActividad.getNombre())) {
			ok = false;
			this.mostrarMensajeGlobal("campoEstaVacio", "advertencia");
		} else {
			this.documentoActividad.setNombre(this.documentoActividad.getNombre().trim());
		}

		if (this.documentoActividad.getArchivo() == null) {
			ok = false;
			this.mostrarMensajeGlobal("archivoAdjuntoRequerido", "advertencia");
		}

		if (this.documentoActividad.getDescargable().trim().equals("S") && !this.documentoActividad.getExtensionArchivo().equals("pdf")) {
			ok = false;
			this.mostrarMensajeGlobal("SOLO DEBE SER PDF PUES ES DE SOLO LECTURA", "advertencia");
		}

		return ok;
	}

	/**
	 * Valida la creaci�n de un documento
	 * 
	 * @return ok
	 */
	private boolean isValidoDocumentoCronograma() {
		boolean ok = true;

		if (this.isVacio(this.documentoCronograma.getNombre())) {
			ok = false;
			this.mostrarMensajeGlobal("campoEstaVacio", "advertencia");
		} else {
			this.documentoCronograma.setNombre(this.documentoCronograma.getNombre().trim());
		}

		if (this.documentoCronograma.getArchivo() == null) {
			ok = false;
			this.mostrarMensajeGlobal("archivoAdjuntoRequerido", "advertencia");
		}

		return ok;
	}

	/**
	 * Valida la creaci�n de un documento
	 * 
	 * @return ok
	 */
	private boolean isValidoDocumentoDiagnostico() {
		boolean ok = true;

		if (this.isVacio(this.documentoDiagnostico.getNombre())) {
			ok = false;
			this.mostrarMensajeGlobal("campoEstaVacio", "advertencia");
		} else {
			this.documentoDiagnostico.setNombre(this.documentoDiagnostico.getNombre().trim());
		}

		if (this.documentoDiagnostico.getArchivo() == null) {
			ok = false;
			this.mostrarMensajeGlobal("archivoAdjuntoRequerido", "advertencia");
		}

		return ok;
	}

	/**
	 * Permite crear un documento
	 */
	public void crearDocumentoCronograma() {
		Conexion conexion = new Conexion();

		try {
			if (isValidoDocumentoCronograma()) {
				conexion.setAutoCommitBD(false);

				this.documentoCronograma.setEtapa(this.etapaCompartida);
				this.documentoCronograma.getCamposBD();
				conexion.insertarBD(this.documentoCronograma.getEstructuraTabla().getTabla(), this.documentoCronograma.getEstructuraTabla().getPersistencia());
				conexion.commitBD();
				this.mostrarMensajeGlobal("creacionExitosa", "exito");

				// reseteo de variables
				this.documentoCronograma = null;
				this.getDocumentoCronograma();
				this.documentosCronograma = null;
				this.getDocumentosCronograma();

				armarEstructuraCronograma();

			}

		} catch (Exception e) {
			conexion.rollbackBD();
			this.mostrarMensajeGlobal("transaccionFallida", "error");
		} finally {
			conexion.cerrarConexion();
		}

	}

	/**
	 * Permite crear un documento
	 */
	public void crearDocumentoDiagnostico() {
		Conexion conexion = new Conexion();

		try {
			if (isValidoDocumentoDiagnostico()) {
				conexion.setAutoCommitBD(false);

				this.documentoDiagnostico.getCamposBD();
				conexion.insertarBD(this.documentoDiagnostico.getEstructuraTabla().getTabla(), this.documentoDiagnostico.getEstructuraTabla().getPersistencia());
				conexion.commitBD();
				this.mostrarMensajeGlobal("creacionExitosa", "exito");

				// reseteo de variables
				this.documentoDiagnostico = null;
				this.getDocumentoDiagnostico();
				this.documentosDiagnostico = null;
				this.getDocumentosDiagnostico();

				armarEstructuraDiagnostico();
			}

		} catch (Exception e) {
			conexion.rollbackBD();
			this.mostrarMensajeGlobal("transaccionFallida", "error");
		} finally {
			conexion.cerrarConexion();
		}

	}

	/**
	 * Permite crear un documento
	 */
	public void crearDocumento() {
		Conexion conexion = new Conexion();

		try {
			if (isValidoDocumento()) {
				conexion.setAutoCommitBD(false);

				// if (this.administrarSesionCliente != null &&
				// this.administrarSesionCliente.getPersonalSesion() != null &&
				// this.administrarSesionCliente.getPersonalSesion().getTipoUsuario() !=
				// null &&
				// this.administrarSesionCliente.getPersonalSesion().getTipoUsuario().equals("CL"))
				// {
				// this.documentoActividad.getProyectoCliente().setId(this.proyectoCliente.getId());
				// } else {
				// // EL CONSULTOR ASIGNADO EN EL MOMENTO
				// this.documentoActividad.getConsultor().setId(this.proyectoCliente.getConsultor().getId());
				// }

				if (this.tipoDocumentoSeleccionado != null && this.tipoDocumentoSeleccionado.equals("CLIENTE")) {
					this.documentoActividad.getProyectoCliente().setId(this.proyectoCliente.getId());

					// si se logueo alguien que no es cliente significa que es pal cliente
					// el doc pero responsable consultor
					if (this.administrarSesionCliente != null && this.administrarSesionCliente.getPersonalSesion() != null && this.administrarSesionCliente.getPersonalSesion().getTipoUsuario() != null && !this.administrarSesionCliente.getPersonalSesion().getTipoUsuario().equals("CL")) {

						// no teine fk
						this.documentoActividad.getPersonal().setId(this.proyectoCliente.getConsultor().getId());
					}

				} else {
					// EL CONSULTOR ASIGNADO EN EL MOMENTO
					this.documentoActividad.getConsultor().setId(this.proyectoCliente.getConsultor().getId());
				}

				this.documentoActividad.setFecha(new Date());

				this.documentoActividad.getCamposBD();
				conexion.insertarBD(this.documentoActividad.getEstructuraTabla().getTabla(), this.documentoActividad.getEstructuraTabla().getPersistencia());
				// recupera el id generado
				this.documentoActividad.setId(conexion.getUltimoSerialTransaccion(this.documentoActividad.getEstructuraTabla().getTabla()));

				// guarda el archivo en capeta por si acaso sirve sin subir a servidor
				String nombre = "isoluciones" + this.documentoActividad.getId().intValue() * this.documentoActividad.getId().intValue();
				OutputStream out = new FileOutputStream(this.getPath("/archivosTemporales/") + "/" + nombre + ".pdf");
				out.write(this.documentoActividad.getArchivo());
				out.close();

				conexion.commitBD();
				this.mostrarMensajeGlobal("creacionExitosa", "exito");

				// reseteo de variables
				this.documentoActividad = null;
				this.getDocumentoActividad();
				this.documentos = null;
				this.getDocumentos();
			}

		} catch (Exception e) {
			conexion.rollbackBD();
			this.mostrarMensajeGlobal("transaccionFallida", "error");
		} finally {
			conexion.cerrarConexion();
		}

	}

	public void cancelarDocumentoCronograma() {

		try {
			this.documentoCronograma = null;
			this.getDocumentoCronograma();
			this.documentoCronogramaTransaccion = null;
			this.getDocumentoCronogramaTransaccion();
			this.documentosCronograma = null;
			this.getDocumentosCronograma();

			this.cerrarModal("panelVerDocumento");

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}

	}

	/**
	 * Cancela la creaci�n de un documento diagnostico
	 */
	public void cancelarDocumentoDiagnostico() {

		try {
			this.documentoDiagnostico = null;
			this.getDocumentoDiagnostico();
			this.documentoDiagnosticoTransaccion = null;
			this.getDocumentoDiagnosticoTransaccion();
			this.documentosDiagnostico = null;
			this.getDocumentosDiagnostico();

			this.cerrarModal("panelVerDocumento");

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}

	}

	/**
	 * Cancela la creaci�n de un documento
	 */
	public void cancelarDocumento() {

		try {
			this.documentoActividad = null;
			this.getDocumentoActividad();
			this.documentoActividadTransaccion = null;
			this.getDocumentoActividadTransaccion();
			this.documentos = null;
			this.getDocumentos();
			this.documentacionTransaccion = null;
			this.getDocumentacionTransaccion();

			this.cerrarModal("panelDocumento");

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}

	}

	public void asignarDocumentoCronograma(DocumentoCronograma aDocumentoCronograma, String aVista) {

		try {

			this.documentoCronogramaTransaccion = aDocumentoCronograma;

			this.abrirModal("panelEliminacionDocumento");

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}

	}

	/**
	 * Asigna un documento
	 * 
	 * @param aDocumentoEquipo
	 * @param aVista
	 */
	public void asignarDocumentoDiagnostico(DocumentoDiagnostico aDocumentoEquipo, String aVista) {

		try {

			this.documentoDiagnosticoTransaccion = aDocumentoEquipo;

			this.abrirModal("panelEliminacionDocumento");

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}

	}

	/**
	 * Asigna un documento para realizarle operaciones
	 * 
	 * @param aDocumentoEquipo
	 * @param aVista
	 */
	public void asignarDocumento(DocumentoActividad aDocumentoEquipo, String aVista) {

		try {

			this.documentoActividadTransaccion = aDocumentoEquipo;
			if (aVista != null && aVista.equals("MODAL_ELIMINAR_DOCUMENTO")) {
				this.abrirModal("panelEliminacionDocumento");
			} else {
				this.abrirModal("panelEdicionDocumento");
			}

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}

	}

	public void limpiarArchivoCargadoCronograma() {
		this.documentoCronograma.settFile(null);
		this.documentoCronograma.setArchivo(null);
	}

	/**
	 * Limpia el archivo cargado para volver a ingresar otro
	 */
	public void limpiarArchivoCargadoDiagnostico() {
		this.documentoDiagnostico.settFile(null);
		this.documentoDiagnostico.setArchivo(null);
	}

	/**
	 * Limpia el archivo cargado para volver a ingresar otro
	 */
	public void limpiarArchivoCargado() {
		this.documentoActividad.settFile(null);
		this.documentoActividad.setArchivo(null);
	}

	/**
	 * Obtiene los documentos de una actividad
	 * 
	 * @return documentos
	 */
	public List<DocumentoActividad> getDocumentos() {
		try {
			SimpleDateFormat formatoFecha2 = new SimpleDateFormat("dd/MM/yyyy");
			if (this.documentos == null && this.proyectoCliente != null && this.proyectoCliente.getId() != null && this.documentacionTransaccion != null && this.documentacionTransaccion.getTareaProyecto() != null && this.documentacionTransaccion.getTareaProyecto().getId() != null) {
				DocumentoActividad doc = new DocumentoActividad();
				doc.getTareaProyecto().setId(this.documentacionTransaccion.getTareaProyecto().getId());

				if (tipoDocumentoSeleccionado != null && tipoDocumentoSeleccionado.equals("CLIENTE")) {
					doc.getProyectoCliente().setId(this.proyectoCliente.getId());

				}
				this.documentos = IConsultasDAO.getDocumentos(doc);
				if (this.documentos != null && this.documentos.size() > 0) {
					for (DocumentoActividad da : this.documentos) {
						if (da.getFecha() != null) {
							da.setNombre(da.getNombre() + " (" + formatoFecha2.format(da.getFecha()) + ")");
						}
					}
				}

			}
		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
		return documentos;
	}

	/**
	 * Establece los documentos
	 * 
	 * @param documentos
	 */
	public void setDocumentos(List<DocumentoActividad> documentos) {
		this.documentos = documentos;
	}

	public String getTipoDocumentoSeleccionado() {
		return tipoDocumentoSeleccionado;
	}

	public void setTipoDocumentoSeleccionado(String tipoDocumentoSeleccionado) {
		this.tipoDocumentoSeleccionado = tipoDocumentoSeleccionado;
	}

	/**
	 * Obtiene un documento de actividad
	 * 
	 * @return documentoActividad
	 */
	public DocumentoActividad getDocumentoActividad() {
		try {
			if (this.documentoActividad == null) {
				this.documentoActividad = new DocumentoActividad();
				if (this.documentacionTransaccion != null && this.documentacionTransaccion.getTareaProyecto() != null && this.documentacionTransaccion.getTareaProyecto().getId() != null) {
					this.documentoActividad.getTareaProyecto().setId(this.documentacionTransaccion.getTareaProyecto().getId());
				}
				if (this.tipoDocumentoSeleccionado != null && this.tipoDocumentoSeleccionado.equals("CLIENTE")) {
					this.documentoActividad.setDescargable("N");
				}
			}
		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
		return documentoActividad;
	}

	/**
	 * 
	 * @param documentoActividad
	 */
	public void setDocumentoActividad(DocumentoActividad documentoActividad) {
		this.documentoActividad = documentoActividad;
	}

	/**
	 * 
	 * @return
	 */
	public DocumentoActividad getDocumentoActividadTransaccion() {
		try {
			if (this.documentoActividadTransaccion == null) {
				this.documentoActividadTransaccion = new DocumentoActividad();
				if (this.documentacionTransaccion != null && this.documentacionTransaccion.getTareaProyecto() != null && this.documentacionTransaccion.getTareaProyecto().getId() != null) {
					this.documentoActividadTransaccion.getTareaProyecto().setId(this.documentacionTransaccion.getTareaProyecto().getId());
				}

			}
		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
		return documentoActividadTransaccion;
	}

	public void setDocumentoActividadTransaccion(DocumentoActividad documentoActividadTransaccion) {
		this.documentoActividadTransaccion = documentoActividadTransaccion;
	}

	public DocumentoDiagnostico getDocumentoDiagnostico() {
		try {
			if (this.documentoDiagnostico == null && this.diagnosicoTransaccion != null) {
				this.documentoDiagnostico = new DocumentoDiagnostico();
				this.documentoDiagnostico.getDiagnostico().setId(this.diagnosicoTransaccion.getId());
			}
		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
		return documentoDiagnostico;
	}

	public void setDocumentoDiagnostico(DocumentoDiagnostico documentoDiagnostico) {
		this.documentoDiagnostico = documentoDiagnostico;
	}

	public DocumentoCronograma getDocumentoCronograma() {
		try {
			if (this.documentoCronograma == null && this.cronogramaTransaccion != null) {
				this.documentoCronograma = new DocumentoCronograma();
				this.documentoCronograma.getCronograma().setId(this.cronogramaTransaccion.getId());
			}
		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
		return documentoCronograma;
	}

	public void setDocumentoCronograma(DocumentoCronograma documentoCronograma) {
		this.documentoCronograma = documentoCronograma;
	}

	public DocumentoDiagnostico getDocumentoDiagnosticoTransaccion() {
		try {
			if (this.documentoDiagnosticoTransaccion == null && this.diagnosicoTransaccion != null) {
				this.documentoDiagnosticoTransaccion = new DocumentoDiagnostico();
				this.documentoDiagnosticoTransaccion.getDiagnostico().setId(this.diagnosicoTransaccion.getId());
			}
		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
		return documentoDiagnosticoTransaccion;
	}

	public void setDocumentoDiagnosticoTransaccion(DocumentoDiagnostico documentoDiagnosticoTransaccion) {
		this.documentoDiagnosticoTransaccion = documentoDiagnosticoTransaccion;
	}

	public DocumentoCronograma getDocumentoCronogramaTransaccion() {
		try {
			if (this.documentoCronogramaTransaccion == null && this.cronogramaTransaccion != null) {
				this.documentoCronogramaTransaccion = new DocumentoCronograma();
				this.documentoCronogramaTransaccion.getCronograma().setId(this.cronogramaTransaccion.getId());
			}
		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
		return documentoCronogramaTransaccion;
	}

	public void setDocumentoCronogramaTransaccion(DocumentoCronograma documentoCronogramaTransaccion) {
		this.documentoCronogramaTransaccion = documentoCronogramaTransaccion;
	}

	public Diagnostico getDiagnosicoTransaccion() {
		try {
			if (this.diagnosicoTransaccion == null) {
				this.diagnosicoTransaccion = new Diagnostico();
			}
		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
		return diagnosicoTransaccion;
	}

	public void setDiagnosicoTransaccion(Diagnostico diagnosicoTransaccion) {
		this.diagnosicoTransaccion = diagnosicoTransaccion;
	}

	public Cronograma getCronogramaTransaccion() {
		try {
			if (this.cronogramaTransaccion == null) {
				this.cronogramaTransaccion = new Cronograma();
			}
		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
		return cronogramaTransaccion;
	}

	public void setCronogramaTransaccion(Cronograma cronogramaTransaccion) {
		this.cronogramaTransaccion = cronogramaTransaccion;
	}

	public List<DocumentoDiagnostico> getDocumentosDiagnostico() {
		try {
			if (this.documentosDiagnostico == null && this.diagnosicoTransaccion != null && this.diagnosicoTransaccion.getId() != null) {
				DocumentoDiagnostico doc = new DocumentoDiagnostico();
				doc.getDiagnostico().setId(this.diagnosicoTransaccion.getId());
				this.documentosDiagnostico = IConsultasDAO.getDocumentosDiagnostico(doc);

			}
		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
		return documentosDiagnostico;
	}

	public void setDocumentosDiagnostico(List<DocumentoDiagnostico> documentosDiagnostico) {
		this.documentosDiagnostico = documentosDiagnostico;
	}

	public List<DocumentoCronograma> getDocumentosCronograma() {
		try {
			if (this.documentosCronograma == null && this.cronogramaTransaccion != null && this.cronogramaTransaccion.getId() != null) {
				DocumentoCronograma doc = new DocumentoCronograma();
				doc.getCronograma().setId(this.cronogramaTransaccion.getId());

				doc.setEtapa(this.etapaCompartida);

				this.documentosCronograma = IConsultasDAO.getDocumentosCronograma(doc);

			}
		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
		return documentosCronograma;
	}

	public void setDocumentosCronograma(List<DocumentoCronograma> documentosCronograma) {
		this.documentosCronograma = documentosCronograma;
	}

	public int getTiempoConsumido() {
		return tiempoConsumido;
	}

	public void setTiempoConsumido(int tiempoConsumido) {
		this.tiempoConsumido = tiempoConsumido;
	}

	public boolean isAsesoriaIniciada() {
		return asesoriaIniciada;
	}

	public void setAsesoriaIniciada(boolean asesoriaIniciada) {
		this.asesoriaIniciada = asesoriaIniciada;
	}

	public int getTiempoMinutos() {
		return tiempoMinutos;
	}

	public void setTiempoMinutos(int tiempoMinutos) {
		this.tiempoMinutos = tiempoMinutos;
	}

	public List<SelectItem> getItemsPlanesDisponiblesCliente() {
		try {

			this.itemsPlanesDisponiblesCliente = new ArrayList<SelectItem>();
			this.itemsPlanesDisponiblesCliente.add(new SelectItem("", this.getMensaje("comboVacio")));

			if (this.proyectoCliente != null && this.proyectoCliente.getCliente() != null && this.proyectoCliente.getCliente().getId() != null) {
				PlanCliente planCliente = new PlanCliente();
				planCliente.getCliente().setId(this.proyectoCliente.getCliente().getId());
				List<PlanCliente> planes = IConsultasDAO.getPlanesCliente(planCliente);

				if (planes != null && planes.size() > 0) {
					for (PlanCliente p : planes) {

						// si no est� gratis validar luego
						if ((p.getMinutosComprados().intValue() - p.getMinutosConCosto()) > 0) {
							this.itemsPlanesDisponiblesCliente.add(new SelectItem(p.getId(), "Minutos del plan: " + (p.getMinutosComprados().intValue() - p.getMinutosConCosto()) + " de " + p.getMinutosComprados() + " " + this.getHoras((p.getMinutosComprados().intValue() - p.getMinutosConCosto())) + ", Precio plan: " + this.getMoneda(p.getPrecioVentaPesosConIva()) + ", Fecha adquirido: " + this.getFechaHoraColombia(p.getFechaCompra())));
						}

					}

					if (!(this.itemsPlanesDisponiblesCliente != null && this.itemsPlanesDisponiblesCliente.size() > 0)) {
						this.itemsPlanesDisponiblesCliente = new ArrayList<SelectItem>();
						this.itemsPlanesDisponiblesCliente.add(new SelectItem("", "EL CLIENTE NO POSEE PLAN CON MINUTOS DISPONIBLES, DEBE IR AL MODULO PERSONAL>CLIENTES Y ADQUIRIR UNO"));
					}

				}
			}

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
		return itemsPlanesDisponiblesCliente;
	}

	public String asignarEtapaCompartida(String aEtapa) {
		if (aEtapa == null || aEtapa.equals("CRONOGRAMA") || aEtapa.trim().equals("")) {
			aEtapa = "CRONOGRAMA";
		} else {
			aEtapa = "IMPLEMENTACION";
		}
		this.etapaCompartida = aEtapa;
		return "";
	}

	public void setItemsPlanesDisponiblesCliente(List<SelectItem> itemsPlanesDisponiblesCliente) {
		this.itemsPlanesDisponiblesCliente = itemsPlanesDisponiblesCliente;
	}

	public String getEtapaCompartida() {
		return etapaCompartida;
	}

	public void setEtapaCompartida(String etapaCompartida) {
		this.etapaCompartida = etapaCompartida;
	}

	public List<SelectItem> getItemsIndicadores() {
		try {

			this.itemsIndicadores = new ArrayList<SelectItem>();
			this.itemsIndicadores.add(new SelectItem("", "Seleccione.."));
			if (proyectoCliente != null && proyectoCliente.getId() != null) {
				Indicador i = new Indicador();
				i.getObjetivo().getInformacionEtapaIndicador().setProyectoCliente(this.proyectoCliente);
				List<Indicador> indicadores = IConsultasDAO.getIndicadoresProyectoCliente(i);

				if (indicadores != null && indicadores.size() > 0) {
					indicadores.forEach(p -> this.itemsIndicadores.add(new SelectItem(p.getId(), p.getNombreIndicador() + "(F�rmula: " + p.getFormula() + ")")));
				}
			}

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}

		return itemsIndicadores;
	}

	public void setItemsIndicadores(List<SelectItem> itemsIndicadores) {
		this.itemsIndicadores = itemsIndicadores;
	}

	public String getHacerOnComplete() {
		return hacerOnComplete;
	}

	public void setHacerOnComplete(String hacerOnComplete) {
		this.hacerOnComplete = hacerOnComplete;
	}
	
	
public String getAbrir(){
	
	String a = "A";
	abiertoCerrado = a;
	return "";
	
}

	public String getAbiertoCerrado() {
		return abiertoCerrado;
	}

	public void setAbiertoCerrado(String abiertoCerrado) {
		this.abiertoCerrado = abiertoCerrado;
	}


	
	
	
	

}
