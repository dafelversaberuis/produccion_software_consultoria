package consultoria.beans;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.Pattern;

import consultoria.generales.IConstantes;

public class PreguntaProyecto implements Serializable {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -476721856780459982L;
	private Integer						id;
	private String						pregunta;
	private String						estado;
	private Date							fechaEstado;
	private String						posibleEvidencia;
	private Proyecto					proyecto;
	private Integer						posicion;

	private String						tFortaleza;
	private String						tRecomendacion;
	private String						tNoConformidad;
	private String						tNoAplica;
	private String						tCumple;

	private EstructuraTabla		estructuraTabla;

	public PreguntaProyecto() {
		this.estructuraTabla = new EstructuraTabla();
		this.proyecto = new Proyecto();
	}

	public void getCamposBD() {

		this.estructuraTabla.setTabla("preguntas_proyecto");
		this.estructuraTabla.getLlavePrimaria().put("id", this.id);
		this.estructuraTabla.getPersistencia().put("posible_evidencia", this.posibleEvidencia);
		this.estructuraTabla.getPersistencia().put("pregunta", this.pregunta);
		this.estructuraTabla.getPersistencia().put("estado", this.estado);
		this.estructuraTabla.getPersistencia().put("fecha_estado", this.fechaEstado);
		this.estructuraTabla.getPersistencia().put("posicion", this.posicion);

		if (this.proyecto != null && this.proyecto.getId() != null) {
			this.estructuraTabla.getPersistencia().put("id_proyecto", this.proyecto.getId());
		}

	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPregunta() {
		return pregunta;
	}

	public void setPregunta(String pregunta) {
		this.pregunta = pregunta;
	}

	public String getPosibleEvidencia() {
		return posibleEvidencia;
	}

	public void setPosibleEvidencia(String posibleEvidencia) {
		this.posibleEvidencia = posibleEvidencia;
	}

	public Proyecto getProyecto() {
		return proyecto;
	}

	public void setProyecto(Proyecto proyecto) {
		this.proyecto = proyecto;
	}

	@Pattern(regexp = "[AI]", message = IConstantes.VALIDACION_ACTIVO_INACTIVO)
	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Date getFechaEstado() {
		return fechaEstado;
	}

	public void setFechaEstado(Date fechaEstado) {
		this.fechaEstado = fechaEstado;
	}

	public EstructuraTabla getEstructuraTabla() {
		return estructuraTabla;
	}

	public void setEstructuraTabla(EstructuraTabla estructuraTabla) {
		this.estructuraTabla = estructuraTabla;
	}

	public Integer getPosicion() {
		return posicion;
	}

	public void setPosicion(Integer posicion) {
		this.posicion = posicion;
	}

	public String gettFortaleza() {
		return tFortaleza;
	}

	public void settFortaleza(String tFortaleza) {
		this.tFortaleza = tFortaleza;
	}

	public String gettRecomendacion() {
		return tRecomendacion;
	}

	public void settRecomendacion(String tRecomendacion) {
		this.tRecomendacion = tRecomendacion;
	}

	public String gettNoConformidad() {
		return tNoConformidad;
	}

	public void settNoConformidad(String tNoConformidad) {
		this.tNoConformidad = tNoConformidad;
	}

	public String gettNoAplica() {
		return tNoAplica;
	}

	public void settNoAplica(String tNoAplica) {
		this.tNoAplica = tNoAplica;
	}

	public String gettCumple() {
		return tCumple;
	}

	public void settCumple(String tCumple) {
		this.tCumple = tCumple;
	}
	
	

}
