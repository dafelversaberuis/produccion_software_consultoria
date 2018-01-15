package consultoria.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;

import consultoria.generales.IConstantes;

public class CronogramaGeneral implements Serializable {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -492316105988202614L;

	private ProyectoCliente		proyectoCliente;

	private String						observaciones;
	private String						requiereFirma;
	private String						firma;
	
	private String						implementacionObservaciones;
	private String						implementacionRequiereFirma;
	private String						implementacionFirma;

	private BigDecimal				metaSuperiorDocumentacion;
	private BigDecimal				metaSuperiorSocializacion;
	private BigDecimal				metaSuperiorImplementacion;
	private BigDecimal				metaSuperiorAuditoria;

	private BigDecimal				metaIntermediaDocumentacion;
	private BigDecimal				metaIntermediaSocializacion;
	private BigDecimal				metaIntermediaImplementacion;
	private BigDecimal				metaIntermediaAuditoria;

	private BigDecimal				metaInferiorDocumentacion;
	private BigDecimal				metaInferiorSocializacion;
	private BigDecimal				metaInferiorImplementacion;
	private BigDecimal				metaInferiorAuditoria;

	private Date							fechaGeneracionTodo;

	private EstructuraTabla		estructuraTabla;

	public CronogramaGeneral() {
		this.proyectoCliente = new ProyectoCliente();
		this.estructuraTabla = new EstructuraTabla();

	}

	public void getCamposBD() {

		this.estructuraTabla.setTabla("cronograma_general");

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
		
		this.estructuraTabla.getPersistencia().put("implementacion_observaciones", this.implementacionObservaciones);
		this.estructuraTabla.getPersistencia().put("implementacion_firma", this.implementacionFirma);
		this.estructuraTabla.getPersistencia().put("implementacion_requiere_firma", this.implementacionRequiereFirma);
		
		
		
		this.estructuraTabla.getPersistencia().put("meta_superior_documentacion", this.metaSuperiorDocumentacion);
		this.estructuraTabla.getPersistencia().put("meta_intermedia_documentacion", this.metaIntermediaDocumentacion);
		this.estructuraTabla.getPersistencia().put("meta_inferior_documentacion", this.metaInferiorDocumentacion);
		this.estructuraTabla.getPersistencia().put("meta_superior_socializacion", this.metaSuperiorSocializacion);
		this.estructuraTabla.getPersistencia().put("meta_intermedia_socializacion", this.metaIntermediaSocializacion);
		this.estructuraTabla.getPersistencia().put("meta_inferior_socializacion", this.metaInferiorSocializacion);
		this.estructuraTabla.getPersistencia().put("meta_superior_implementacion", this.metaInferiorImplementacion);
		this.estructuraTabla.getPersistencia().put("meta_intermedia_implementacion", this.metaIntermediaSocializacion);
		this.estructuraTabla.getPersistencia().put("meta_inferior_implementacion", this.metaInferiorImplementacion);

		this.estructuraTabla.getPersistencia().put("meta_superior_auditoria", this.metaSuperiorAuditoria);
		this.estructuraTabla.getPersistencia().put("meta_intermedia_auditoria", this.metaIntermediaAuditoria);
		this.estructuraTabla.getPersistencia().put("meta_inferior_auditoria", this.metaInferiorAuditoria);

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

	@Digits(integer = 4, fraction = 2, message = IConstantes.VALIDACION_MAXIMO_DECIMAL)
	@DecimalMin(value = "0.00", message = IConstantes.VALIDACION_MINIMO_ENTERO)
	public BigDecimal getMetaSuperiorDocumentacion() {
		return metaSuperiorDocumentacion;
	}

	public void setMetaSuperiorDocumentacion(BigDecimal metaSuperiorDocumentacion) {
		this.metaSuperiorDocumentacion = metaSuperiorDocumentacion;
	}

	@Digits(integer = 4, fraction = 2, message = IConstantes.VALIDACION_MAXIMO_DECIMAL)
	@DecimalMin(value = "0.00", message = IConstantes.VALIDACION_MINIMO_ENTERO)
	public BigDecimal getMetaSuperiorSocializacion() {
		return metaSuperiorSocializacion;
	}

	public void setMetaSuperiorSocializacion(BigDecimal metaSuperiorSocializacion) {
		this.metaSuperiorSocializacion = metaSuperiorSocializacion;
	}

	@Digits(integer = 4, fraction = 2, message = IConstantes.VALIDACION_MAXIMO_DECIMAL)
	@DecimalMin(value = "0.00", message = IConstantes.VALIDACION_MINIMO_ENTERO)
	public BigDecimal getMetaSuperiorImplementacion() {
		return metaSuperiorImplementacion;
	}

	public void setMetaSuperiorImplementacion(BigDecimal metaSuperiorImplementacion) {
		this.metaSuperiorImplementacion = metaSuperiorImplementacion;
	}

	@Digits(integer = 4, fraction = 2, message = IConstantes.VALIDACION_MAXIMO_DECIMAL)
	@DecimalMin(value = "0.00", message = IConstantes.VALIDACION_MINIMO_ENTERO)
	public BigDecimal getMetaSuperiorAuditoria() {
		return metaSuperiorAuditoria;
	}

	public void setMetaSuperiorAuditoria(BigDecimal metaSuperiorAuditoria) {
		this.metaSuperiorAuditoria = metaSuperiorAuditoria;
	}

	@Digits(integer = 4, fraction = 2, message = IConstantes.VALIDACION_MAXIMO_DECIMAL)
	@DecimalMin(value = "0.00", message = IConstantes.VALIDACION_MINIMO_ENTERO)
	public BigDecimal getMetaIntermediaDocumentacion() {
		return metaIntermediaDocumentacion;
	}

	public void setMetaIntermediaDocumentacion(BigDecimal metaIntermediaDocumentacion) {
		this.metaIntermediaDocumentacion = metaIntermediaDocumentacion;
	}

	@Digits(integer = 4, fraction = 2, message = IConstantes.VALIDACION_MAXIMO_DECIMAL)
	@DecimalMin(value = "0.00", message = IConstantes.VALIDACION_MINIMO_ENTERO)
	public BigDecimal getMetaIntermediaSocializacion() {
		return metaIntermediaSocializacion;
	}

	public void setMetaIntermediaSocializacion(BigDecimal metaIntermediaSocializacion) {
		this.metaIntermediaSocializacion = metaIntermediaSocializacion;
	}

	@Digits(integer = 4, fraction = 2, message = IConstantes.VALIDACION_MAXIMO_DECIMAL)
	@DecimalMin(value = "0.00", message = IConstantes.VALIDACION_MINIMO_ENTERO)
	public BigDecimal getMetaIntermediaImplementacion() {
		return metaIntermediaImplementacion;
	}

	public void setMetaIntermediaImplementacion(BigDecimal metaIntermediaImplementacion) {
		this.metaIntermediaImplementacion = metaIntermediaImplementacion;
	}

	@Digits(integer = 4, fraction = 2, message = IConstantes.VALIDACION_MAXIMO_DECIMAL)
	@DecimalMin(value = "0.00", message = IConstantes.VALIDACION_MINIMO_ENTERO)
	public BigDecimal getMetaIntermediaAuditoria() {
		return metaIntermediaAuditoria;
	}

	public void setMetaIntermediaAuditoria(BigDecimal metaIntermediaAuditoria) {
		this.metaIntermediaAuditoria = metaIntermediaAuditoria;
	}

	@Digits(integer = 4, fraction = 2, message = IConstantes.VALIDACION_MAXIMO_DECIMAL)
	@DecimalMin(value = "0.00", message = IConstantes.VALIDACION_MINIMO_ENTERO)
	public BigDecimal getMetaInferiorDocumentacion() {
		return metaInferiorDocumentacion;
	}

	public void setMetaInferiorDocumentacion(BigDecimal metaInferiorDocumentacion) {
		this.metaInferiorDocumentacion = metaInferiorDocumentacion;
	}

	@Digits(integer = 4, fraction = 2, message = IConstantes.VALIDACION_MAXIMO_DECIMAL)
	@DecimalMin(value = "0.00", message = IConstantes.VALIDACION_MINIMO_ENTERO)
	public BigDecimal getMetaInferiorSocializacion() {
		return metaInferiorSocializacion;
	}

	public void setMetaInferiorSocializacion(BigDecimal metaInferiorSocializacion) {
		this.metaInferiorSocializacion = metaInferiorSocializacion;
	}

	@Digits(integer = 4, fraction = 2, message = IConstantes.VALIDACION_MAXIMO_DECIMAL)
	@DecimalMin(value = "0.00", message = IConstantes.VALIDACION_MINIMO_ENTERO)
	public BigDecimal getMetaInferiorImplementacion() {
		return metaInferiorImplementacion;
	}

	public void setMetaInferiorImplementacion(BigDecimal metaInferiorImplementacion) {
		this.metaInferiorImplementacion = metaInferiorImplementacion;
	}

	@Digits(integer = 4, fraction = 2, message = IConstantes.VALIDACION_MAXIMO_DECIMAL)
	@DecimalMin(value = "0.00", message = IConstantes.VALIDACION_MINIMO_ENTERO)
	public BigDecimal getMetaInferiorAuditoria() {
		return metaInferiorAuditoria;
	}

	public void setMetaInferiorAuditoria(BigDecimal metaInferiorAuditoria) {
		this.metaInferiorAuditoria = metaInferiorAuditoria;
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

	public String getImplementacionObservaciones() {
		return implementacionObservaciones;
	}

	public void setImplementacionObservaciones(String implementacionObservaciones) {
		this.implementacionObservaciones = implementacionObservaciones;
	}

	public String getImplementacionRequiereFirma() {
		return implementacionRequiereFirma;
	}

	public void setImplementacionRequiereFirma(String implementacionRequiereFirma) {
		this.implementacionRequiereFirma = implementacionRequiereFirma;
	}

	public String getImplementacionFirma() {
		return implementacionFirma;
	}

	public void setImplementacionFirma(String implementacionFirma) {
		this.implementacionFirma = implementacionFirma;
	}
	
	

}
