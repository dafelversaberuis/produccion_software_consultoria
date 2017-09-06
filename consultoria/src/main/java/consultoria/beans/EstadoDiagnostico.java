package consultoria.beans;

import java.io.Serializable;

public class EstadoDiagnostico implements Serializable {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 5792120189529491499L;
	private Integer						id;
	private Diagnostico				diagnostico;
	private Estado						estado;
	private boolean						tSeleccionado;

	private EstructuraTabla		estructuraTabla;

	public EstadoDiagnostico() {
		this.estructuraTabla = new EstructuraTabla();
		this.diagnostico = new Diagnostico();
		this.estado = new Estado();
	}

	public void getCamposBD() {

		this.estructuraTabla.setTabla("estados_diagnostico");
		this.estructuraTabla.getLlavePrimaria().put("id", this.id);

		if (this.diagnostico != null && this.diagnostico.getId() != null) {
			this.estructuraTabla.getPersistencia().put("id_diagnostico", this.diagnostico.getId());
		} else {
			this.estructuraTabla.getPersistencia().put("id_diagnostico", null);
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

	public Diagnostico getDiagnostico() {
		return diagnostico;
	}

	public void setDiagnostico(Diagnostico diagnostico) {
		this.diagnostico = diagnostico;
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

	public boolean istSeleccionado() {
		return tSeleccionado;
	}

	public void settSeleccionado(boolean tSeleccionado) {
		this.tSeleccionado = tSeleccionado;
	}

}
