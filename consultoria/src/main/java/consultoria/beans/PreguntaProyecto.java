package consultoria.beans;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.Pattern;

import org.primefaces.model.UploadedFile;

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
	private String						numeral;
	private Proyecto					proyecto;
	private Integer						posicion;

	private String						tFortaleza;
	private String						tRecomendacion;
	private String						tNoConformidad;
	private String						tNoAplica;
	private String						tCumple;

	private String						tFormaAgregar;
	private byte[]						archivo;
	private UploadedFile			tFile;
	private String						tConcepto;
	private boolean						tApto;

	private String						hallazgoPredeterminadoFortaleza;
	private String						hallazgoPredeterminadoRecomendacion;
	private String						hallazgoPredeterminadoNoConformidad;
	
	private String						hallazgoPredeterminadoCumple;
	private String						hallazgoPredeterminadoNoAplica;
	

	private String						analisisCausaPredeterminadoNoConformidad;
	private String						accionRealizarPredeterminadoNoConformidad;
	private String						evidenciaNoEncontradaPredeterminadoNoConformidad;
	private String						responsablePredeterminadoNoConformidad;

	private String						accionRealizarPredeterminadoRecomendacion;
	private String						responsablePredeterminadoRecomendacion;

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

		this.estructuraTabla.getPersistencia().put("numeral", this.numeral);

		// nuevos
		this.estructuraTabla.getPersistencia().put("hallazgo_pred_fortaleza",this.hallazgoPredeterminadoFortaleza);
		this.estructuraTabla.getPersistencia().put("hallazgo_pred_recomendacion",this.hallazgoPredeterminadoRecomendacion);
		this.estructuraTabla.getPersistencia().put("hallazgo_pred_no_conformidad",this.hallazgoPredeterminadoNoConformidad);
		this.estructuraTabla.getPersistencia().put("hallazgo_pred_cumple",this.hallazgoPredeterminadoCumple);
		this.estructuraTabla.getPersistencia().put("hallazgo_pred_no_aplica",this.hallazgoPredeterminadoNoAplica);

		this.estructuraTabla.getPersistencia().put("ac_pred_no_conformidad",this.analisisCausaPredeterminadoNoConformidad);
		this.estructuraTabla.getPersistencia().put("ar_pred_no_conformidad",this.accionRealizarPredeterminadoNoConformidad);
		this.estructuraTabla.getPersistencia().put("ene_pred_no_conformidad",this.evidenciaNoEncontradaPredeterminadoNoConformidad);
		this.estructuraTabla.getPersistencia().put("r_pred_no_conformidad",this.responsablePredeterminadoNoConformidad);

		this.estructuraTabla.getPersistencia().put("ar_pred_recomendacion",this.accionRealizarPredeterminadoRecomendacion);
		this.estructuraTabla.getPersistencia().put("r_pred_recomendacion",this.responsablePredeterminadoRecomendacion);

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

	public String getNumeral() {
		return numeral;
	}

	public void setNumeral(String numeral) {
		this.numeral = numeral;
	}

	public String gettFormaAgregar() {
		return tFormaAgregar;
	}

	public void settFormaAgregar(String tFormaAgregar) {
		this.tFormaAgregar = tFormaAgregar;
	}

	public byte[] getArchivo() {
		return archivo;
	}

	public void setArchivo(byte[] archivo) {
		this.archivo = archivo;
	}

	public UploadedFile gettFile() {
		return tFile;
	}

	public void settFile(UploadedFile tFile) {
		this.tFile = tFile;
	}

	public String gettConcepto() {
		return tConcepto;
	}

	public void settConcepto(String tConcepto) {
		this.tConcepto = tConcepto;
	}

	public boolean istApto() {
		return tApto;
	}

	public void settApto(boolean tApto) {
		this.tApto = tApto;
	}

	public String getHallazgoPredeterminadoFortaleza() {
		return hallazgoPredeterminadoFortaleza;
	}

	public void setHallazgoPredeterminadoFortaleza(String hallazgoPredeterminadoFortaleza) {
		this.hallazgoPredeterminadoFortaleza = hallazgoPredeterminadoFortaleza;
	}

	public String getHallazgoPredeterminadoRecomendacion() {
		return hallazgoPredeterminadoRecomendacion;
	}

	public void setHallazgoPredeterminadoRecomendacion(String hallazgoPredeterminadoRecomendacion) {
		this.hallazgoPredeterminadoRecomendacion = hallazgoPredeterminadoRecomendacion;
	}

	public String getHallazgoPredeterminadoNoConformidad() {
		return hallazgoPredeterminadoNoConformidad;
	}

	public void setHallazgoPredeterminadoNoConformidad(String hallazgoPredeterminadoNoConformidad) {
		this.hallazgoPredeterminadoNoConformidad = hallazgoPredeterminadoNoConformidad;
	}

	public String getAnalisisCausaPredeterminadoNoConformidad() {
		return analisisCausaPredeterminadoNoConformidad;
	}

	public void setAnalisisCausaPredeterminadoNoConformidad(String analisisCausaPredeterminadoNoConformidad) {
		this.analisisCausaPredeterminadoNoConformidad = analisisCausaPredeterminadoNoConformidad;
	}

	public String getAccionRealizarPredeterminadoNoConformidad() {
		return accionRealizarPredeterminadoNoConformidad;
	}

	public void setAccionRealizarPredeterminadoNoConformidad(String accionRealizarPredeterminadoNoConformidad) {
		this.accionRealizarPredeterminadoNoConformidad = accionRealizarPredeterminadoNoConformidad;
	}

	public String getEvidenciaNoEncontradaPredeterminadoNoConformidad() {
		return evidenciaNoEncontradaPredeterminadoNoConformidad;
	}

	public void setEvidenciaNoEncontradaPredeterminadoNoConformidad(String evidenciaNoEncontradaPredeterminadoNoConformidad) {
		this.evidenciaNoEncontradaPredeterminadoNoConformidad = evidenciaNoEncontradaPredeterminadoNoConformidad;
	}

	public String getResponsablePredeterminadoNoConformidad() {
		return responsablePredeterminadoNoConformidad;
	}

	public void setResponsablePredeterminadoNoConformidad(String responsablePredeterminadoNoConformidad) {
		this.responsablePredeterminadoNoConformidad = responsablePredeterminadoNoConformidad;
	}

	public String getAccionRealizarPredeterminadoRecomendacion() {
		return accionRealizarPredeterminadoRecomendacion;
	}

	public void setAccionRealizarPredeterminadoRecomendacion(String accionRealizarPredeterminadoRecomendacion) {
		this.accionRealizarPredeterminadoRecomendacion = accionRealizarPredeterminadoRecomendacion;
	}

	public String getResponsablePredeterminadoRecomendacion() {
		return responsablePredeterminadoRecomendacion;
	}

	public void setResponsablePredeterminadoRecomendacion(String responsablePredeterminadoRecomendacion) {
		this.responsablePredeterminadoRecomendacion = responsablePredeterminadoRecomendacion;
	}

	public String getHallazgoPredeterminadoCumple() {
		return hallazgoPredeterminadoCumple;
	}

	public void setHallazgoPredeterminadoCumple(String hallazgoPredeterminadoCumple) {
		this.hallazgoPredeterminadoCumple = hallazgoPredeterminadoCumple;
	}

	public String getHallazgoPredeterminadoNoAplica() {
		return hallazgoPredeterminadoNoAplica;
	}

	public void setHallazgoPredeterminadoNoAplica(String hallazgoPredeterminadoNoAplica) {
		this.hallazgoPredeterminadoNoAplica = hallazgoPredeterminadoNoAplica;
	}
	
	

}
