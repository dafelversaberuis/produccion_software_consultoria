package consultoria.beans;

import java.io.Serializable;
import java.util.Date;

public class TiempoPlanificacion implements Serializable {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -6352615653332611859L;
	private Integer						id;
	private Date							fecha;
	private Integer						numeroSemana;
	private boolean						programado;
	private boolean						ejecutado;
	private Planificacion			planificacion;

	private EstructuraTabla		estructuraTabla;

	public TiempoPlanificacion() {
		this.planificacion = new Planificacion();
		this.estructuraTabla = new EstructuraTabla();
	}

	public void getCamposBD() {

		this.estructuraTabla.setTabla("tiempos_planificacion");
		this.estructuraTabla.getLlavePrimaria().put("id", this.id);
		this.estructuraTabla.getPersistencia().put("fecha", this.fecha);
		this.estructuraTabla.getPersistencia().put("numero_semana", this.numeroSemana);
		this.estructuraTabla.getPersistencia().put("programado", this.programado);
		this.estructuraTabla.getPersistencia().put("ejecutado", this.ejecutado);

		if (this.planificacion != null && this.planificacion.getId() != null) {
			this.estructuraTabla.getPersistencia().put("id_planificacion", this.planificacion.getId());
		} else {
			this.estructuraTabla.getPersistencia().put("id_planificacion", null);
		}

	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Integer getNumeroSemana() {
		return numeroSemana;
	}

	public void setNumeroSemana(Integer numeroSemana) {
		this.numeroSemana = numeroSemana;
	}

	public boolean isProgramado() {
		return programado;
	}

	public void setProgramado(boolean programado) {
		this.programado = programado;
	}

	public boolean isEjecutado() {
		return ejecutado;
	}

	public void setEjecutado(boolean ejecutado) {
		this.ejecutado = ejecutado;
	}

	public Planificacion getPlanificacion() {
		return planificacion;
	}

	public void setPlanificacion(Planificacion planificacion) {
		this.planificacion = planificacion;
	}

	public EstructuraTabla getEstructuraTabla() {
		return estructuraTabla;
	}

	public void setEstructuraTabla(EstructuraTabla estructuraTabla) {
		this.estructuraTabla = estructuraTabla;
	}

}
