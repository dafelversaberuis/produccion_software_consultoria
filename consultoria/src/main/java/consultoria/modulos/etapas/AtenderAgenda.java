package consultoria.modulos.etapas;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.ScheduleEntryResizeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

import consultoria.Conexion;
import consultoria.beans.Cita;
import consultoria.generales.ConsultarFuncionesAPI;
import consultoria.generales.IConstantes;
import consultoria.modulos.IConsultasDAO;

@ManagedBean
@ViewScoped
public class AtenderAgenda extends ConsultarFuncionesAPI implements Serializable {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1357454669894306268L;

	private Integer						vistaActual;

	// CITAS
	public String							textoConsulta;
	private ScheduleModel			calendario;
	private List<Cita>				citas;
	private Cita							citaSeleccionada;

	public void cancelarFormularioCita() {
		this.citaSeleccionada = new Cita(new DefaultScheduleEvent());
		this.textoConsulta = "";
		this.cerrarModal("eventDialog");
	}

	// Se selecciona un espacio vacio del calendario, luego se crea una nueva
	// instancia de la cita
	public void onDateSelect(SelectEvent selectEvent) {
		this.textoConsulta = "";
		Integer tiempoConsulta = 30;
		Calendar fechaFin = Calendar.getInstance();
		fechaFin.setTime((Date) selectEvent.getObject()); // Configuramos la fecha
																											// que se recibe
		fechaFin.add(Calendar.MINUTE, tiempoConsulta);
		this.citaSeleccionada = new Cita(new DefaultScheduleEvent("", (Date) selectEvent.getObject(), fechaFin.getTime()));
		this.citaSeleccionada.setFechaInicio((Date) selectEvent.getObject());
		this.citaSeleccionada.setFechaFin(fechaFin.getTime());
	}

	// Se selecciona un evento previamente creado
	public void onEventSelect(SelectEvent selectEvent) {
		buscarCita((ScheduleEvent) selectEvent.getObject());
		this.abrirModal("eventDialog");
	}

	// Busca un evento y lo asigna al objeto cita
	public void buscarCita(ScheduleEvent evento) {
		getCitas();
		for (Cita c : this.citas) {
			if (c.getEvent().equals(evento)) {
				c.setEvent(evento);
				this.citaSeleccionada = c;
				// Actualizo la info del evento
				this.citaSeleccionada.setEvent(evento);
				this.citaSeleccionada.setModoEdicion(true);
				this.textoConsulta = c.getProyectoCliente().getCliente().getCliente() + "-" + c.getProyectoCliente().getProyecto().getNombre();
				break;
			}
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
		// this.mostrarMensajeGlobal("Entró a guardar cita", "exito");
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
		fechaFin.setTime((Date) this.citaSeleccionada.getEvent().getStartDate()); // fecha_q_recibe
		fechaFin.add(Calendar.MINUTE, tiempoConsulta);
		((DefaultScheduleEvent) this.citaSeleccionada.getEvent()).setEndDate(fechaFin.getTime());
	}

	public List<Cita> getCitas() {
		Integer tiempoConsulta = 10000; // 3 días
		Calendar fechaFin = Calendar.getInstance();
		fechaFin.setTime(new Date()); // Configuramos la fecha que se recibe
		fechaFin.add(Calendar.MINUTE, tiempoConsulta);
		if (this.citas == null) {
			try {
				this.citas = IConsultasDAO.getAgenda(new Date(), fechaFin.getTime(), null);
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

}
