package consultoria.beans;

import java.io.Serializable;
import java.util.Date;

import org.primefaces.model.ScheduleEvent;

public class Cita implements Serializable {

	/**
	 * 
	 */
	private static final long		serialVersionUID	= -5660680211432578196L;
	private Integer							id;
	private ProyectoCliente			proyectoCliente;
	private Date								fechaInicio;
	private Date								fechaFin;
	private String							estado;
	private String							observacionesCliente;
	private String							observacionesConsultor;

	// Temporales
	private EstructuraTabla			estructuraTabla;
	private Boolean							modoEdicion;
	private ScheduleEvent				event;
	private Date								fechaActual;

	private PlanCliente					TPlanSeleccionado;

	public final static String	MAX								= "SELECT MAX(id) consecutivo  FROM cita";

	public Cita(ScheduleEvent event) {
		super();
		this.event = event;
		this.estructuraTabla = new EstructuraTabla();
		this.proyectoCliente = new ProyectoCliente();
		this.modoEdicion = false;
		this.estado = "R";
		this.fechaActual = new Date();
		this.TPlanSeleccionado = new  PlanCliente();
		
	
	}

	public Cita() {
		this.estructuraTabla = new EstructuraTabla();
		this.proyectoCliente = new ProyectoCliente();
		this.modoEdicion = false;
		this.estado = "R";
		this.TPlanSeleccionado = new  PlanCliente();
	}

	public void getCamposBD() {
		this.estructuraTabla.setTabla("cita");
		this.estructuraTabla.getLlavePrimaria().put("id", this.id);

		this.estructuraTabla.getPersistencia().put("id", this.id);
		this.estructuraTabla.getPersistencia().put("id_proyecto_cliente", this.proyectoCliente != null ? this.proyectoCliente.getId() : null);
		this.estructuraTabla.getPersistencia().put("fecha_inicio", this.fechaInicio);
		this.estructuraTabla.getPersistencia().put("fecha_fin", this.fechaFin);
		this.estructuraTabla.getPersistencia().put("estado", this.estado);

		this.estructuraTabla.getPersistencia().put("observaciones_cliente", this.observacionesCliente);
		this.estructuraTabla.getPersistencia().put("observaciones_consultor", this.observacionesConsultor);

	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Date getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public ProyectoCliente getProyectoCliente() {
		return proyectoCliente;
	}

	public void setProyectoCliente(ProyectoCliente proyectoCliente) {
		this.proyectoCliente = proyectoCliente;
	}

	// TEMPORALES

	public void setFechaActual(Date fechaActual) {
		this.fechaActual = fechaActual;
	}

	public EstructuraTabla getEstructuraTabla() {
		return estructuraTabla;
	}

	public void setEstructuraTabla(EstructuraTabla estructuraTabla) {
		this.estructuraTabla = estructuraTabla;
	}

	public Boolean getModoEdicion() {
		return modoEdicion;
	}

	public void setModoEdicion(Boolean modoEdicion) {
		this.modoEdicion = modoEdicion;
	}

	public Boolean modoVisualizacion() {
		return (this.estado.equals("F"));

	}

	public ScheduleEvent getEvent() {
		return event;
	}

	public void setEvent(ScheduleEvent event) {
		this.event = event;
	}

	public String getTituloCita() {
		String titulo = "";
		if (this.proyectoCliente != null)
			titulo = this.proyectoCliente.getCliente().getCliente() + "-" + this.proyectoCliente.getProyecto().getNombre();
		return titulo;
	}

	public Date getFechaActual() {
		return new Date();
	}

	public String getEstadoTexto() {
		if (this.estado != null) {
			if (this.estado.equals("R"))
				return "Registrado/Programada";

			if (this.estado.equals("A"))
				return "Aprobada para atenci�n";

			if (this.estado.equals("T"))
				return "En Atenci�n";

			if (this.estado.equals("C"))
				return "Cancelada";

			if (this.estado.equals("P"))
				return "Aplazada";

			if (this.estado.equals("F"))
				return "Finalizada";

		}

		return "";
	}

	public String getObservacionesCliente() {
		return observacionesCliente;
	}

	public void setObservacionesCliente(String observacionesCliente) {
		this.observacionesCliente = observacionesCliente;
	}

	public String getObservacionesConsultor() {
		return observacionesConsultor;
	}

	public void setObservacionesConsultor(String observacionesConsultor) {
		this.observacionesConsultor = observacionesConsultor;
	}

	public PlanCliente getTPlanSeleccionado() {
		return TPlanSeleccionado;
	}

	public void setTPlanSeleccionado(PlanCliente tPlanSeleccionado) {
		TPlanSeleccionado = tPlanSeleccionado;
	}

}
