package consultoria.beans;

import java.io.Serializable;
import java.util.Date;

public class PlanAccionIndicador implements Serializable {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 5879803658981338288L;
	private Integer						id;
	private String						actividad;
	private String						responsable;
	private Date							fecha;

	private Indicador					indicador;

	private EstructuraTabla		estructuraTabla;

	public PlanAccionIndicador() {
		this.estructuraTabla = new EstructuraTabla();
		this.indicador = new Indicador();

	}

	public void getCamposBD() {

		this.estructuraTabla.setTabla("plan_accion_indicador");
		this.estructuraTabla.getLlavePrimaria().put("id", this.id);

		this.estructuraTabla.getPersistencia().put("id_indicador", this.indicador.getId());
		this.estructuraTabla.getPersistencia().put("fecha", this.fecha);
		this.estructuraTabla.getPersistencia().put("responsable", this.responsable);
		this.estructuraTabla.getPersistencia().put("actividad", this.actividad);

	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getActividad() {
		return actividad;
	}

	public void setActividad(String actividad) {
		this.actividad = actividad;
	}

	public String getResponsable() {
		return responsable;
	}

	public void setResponsable(String responsable) {
		this.responsable = responsable;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Indicador getIndicador() {
		return indicador;
	}

	public void setIndicador(Indicador indicador) {
		this.indicador = indicador;
	}

	public EstructuraTabla getEstructuraTabla() {
		return estructuraTabla;
	}

	public void setEstructuraTabla(EstructuraTabla estructuraTabla) {
		this.estructuraTabla = estructuraTabla;
	}

}
