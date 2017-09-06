package consultoria.beans;

import java.io.Serializable;

public class PlanificacionGeneral implements Serializable {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -104775035711973238L;
	private ProyectoCliente		proyectoCliente;
	private String						estadoActual;

	private String						firma;

	private EstructuraTabla		estructuraTabla;

	public PlanificacionGeneral() {
		this.proyectoCliente = new ProyectoCliente();

		this.estructuraTabla = new EstructuraTabla();
	}

	public void getCamposBD() {

		this.estructuraTabla.setTabla("planificacion_general");

		if (this.proyectoCliente != null && this.proyectoCliente.getId() != null) {

			this.estructuraTabla.getLlavePrimaria().put("id_proyecto_cliente", this.proyectoCliente.getId());
			this.estructuraTabla.getPersistencia().put("id_proyecto_cliente", this.proyectoCliente.getId());
		} else {
			this.estructuraTabla.getLlavePrimaria().put("id_proyecto_cliente", null);
			this.estructuraTabla.getPersistencia().put("id_proyecto_cliente", null);
		}

		this.estructuraTabla.getPersistencia().put("estado_actual", this.estadoActual);
		this.estructuraTabla.getPersistencia().put("firma", this.firma);

	}

	public ProyectoCliente getProyectoCliente() {
		return proyectoCliente;
	}

	public void setProyectoCliente(ProyectoCliente proyectoCliente) {
		this.proyectoCliente = proyectoCliente;
	}

	public String getEstadoActual() {
		return estadoActual;
	}

	public void setEstadoActual(String estadoActual) {
		this.estadoActual = estadoActual;
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

}
