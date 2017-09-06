package consultoria.beans;

import java.io.Serializable;
import java.util.Date;

public class DocumentacionGeneral implements Serializable {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -5302719029598892580L;

	private ProyectoCliente		proyectoCliente;
	private String						observaciones;
	private String						requiereFirma;
	private String						firma;
	private Date							fechaGeneracionTodo;

	private EstructuraTabla		estructuraTabla;

	public DocumentacionGeneral() {
		this.proyectoCliente = new ProyectoCliente();
		this.estructuraTabla = new EstructuraTabla();

	}

	public void getCamposBD() {

		this.estructuraTabla.setTabla("documentacion_general");

		if (this.proyectoCliente != null && this.proyectoCliente.getId() != null) {

			this.estructuraTabla.getLlavePrimaria().put("id_proyecto_cliente", this.proyectoCliente.getId());
			this.estructuraTabla.getPersistencia().put("id_proyecto_cliente", this.proyectoCliente.getId());
		} else {
			this.estructuraTabla.getLlavePrimaria().put("id_proyecto_cliente", null);
			this.estructuraTabla.getPersistencia().put("id_proyecto_cliente", null);
		}

		this.estructuraTabla.getPersistencia().put("observaciones", this.observaciones);
		this.estructuraTabla.getPersistencia().put("firma", this.firma);
		this.estructuraTabla.getPersistencia().put("requiere_firma", this.requiereFirma);

		this.estructuraTabla.getPersistencia().put("fecha_generacion_todo", this.fechaGeneracionTodo);

	}

	public ProyectoCliente getProyectoCliente() {
		return proyectoCliente;
	}

	public void setProyectoCliente(ProyectoCliente proyectoCliente) {
		this.proyectoCliente = proyectoCliente;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public String getRequiereFirma() {
		return requiereFirma;
	}

	public void setRequiereFirma(String requiereFirma) {
		this.requiereFirma = requiereFirma;
	}

	public String getFirma() {
		return firma;
	}

	public void setFirma(String firma) {
		this.firma = firma;
	}

	public EstructuraTabla getEstructuraTabla() {
		return estructuraTabla;
	}

	public void setEstructuraTabla(EstructuraTabla estructuraTabla) {
		this.estructuraTabla = estructuraTabla;
	}

	public Date getFechaGeneracionTodo() {
		return fechaGeneracionTodo;
	}

	public void setFechaGeneracionTodo(Date fechaGeneracionTodo) {
		this.fechaGeneracionTodo = fechaGeneracionTodo;
	}

}
