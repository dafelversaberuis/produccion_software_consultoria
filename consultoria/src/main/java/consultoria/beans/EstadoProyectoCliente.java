package consultoria.beans;

import java.io.Serializable;

public class EstadoProyectoCliente implements Serializable {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 4907080637235397693L;
	private Integer						id;
	private ProyectoCliente		proyectoCliente;
	private Estado						estado;
	private Integer						tNumeroVeces;

	private EstructuraTabla		estructuraTabla;

	public EstadoProyectoCliente() {
		this.estructuraTabla = new EstructuraTabla();
		this.proyectoCliente = new ProyectoCliente();
		this.estado = new Estado();
	}

	public void getCamposBD() {

		this.estructuraTabla.setTabla("estados_proyecto_cliente");
		this.estructuraTabla.getLlavePrimaria().put("id", this.id);

		if (this.proyectoCliente != null && this.proyectoCliente.getId() != null) {
			this.estructuraTabla.getPersistencia().put("id_proyecto_cliente", this.proyectoCliente.getId());
		} else {
			this.estructuraTabla.getPersistencia().put("id_proyecto_cliente", null);
		}
		if (this.estado != null && this.estado.getId() != null) {
			this.estructuraTabla.getPersistencia().put("id_estado", this.estado.getId());
		} else {
			this.estructuraTabla.getPersistencia().put("id_estado", null);
		}

	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public ProyectoCliente getProyectoCliente() {
		return proyectoCliente;
	}

	public void setProyectoCliente(ProyectoCliente proyectoCliente) {
		this.proyectoCliente = proyectoCliente;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public EstructuraTabla getEstructuraTabla() {
		return estructuraTabla;
	}

	public void setEstructuraTabla(EstructuraTabla estructuraTabla) {
		this.estructuraTabla = estructuraTabla;
	}

	public Integer gettNumeroVeces() {
		return tNumeroVeces;
	}

	public void settNumeroVeces(Integer tNumeroVeces) {
		this.tNumeroVeces = tNumeroVeces;
	}

}
