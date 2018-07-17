package consultoria.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;

import consultoria.generales.IConstantes;

public class Cronograma implements Serializable {

	/**
	 * 
	 */
	private static final long					serialVersionUID	= 7312075806756466445L;
	private Integer										id;
	private ProyectoCliente						proyectoCliente;
	private TareaProyecto							tareaProyecto;

	private String										recomendaciones;

	private BigDecimal								metaDocumentacion;
	private BigDecimal								metaSocializacion;
	private BigDecimal								metaImplementacion;
	private BigDecimal								metaAuditoria;

	private Date											fechaInicioDocumentacion;
	private Date											fechaInicioSocializacion;
	private Date											fechaInicioImplementacion;
	private Date											fechaInicioAuditoria;

	private Date											fechaFinDocumentacion;
	private Date											fechaFinSocializacion;
	private Date											fechaFinImplementacion;
	private Date											fechaFinAuditoria;

	private Date											fechaSeguimientoDocumentacion;
	private Date											fechaSeguimientoSocializacion;
	private Date											fechaSeguimientoImplementacion;
	private Date											fechaSeguimientoAuditoria;

	private String										responsableDocumentacion;
	private String										responsableSocializacion;
	private String										responsableImplementacion;
	private String										responsableAuditoria;

	private String										implementacionEvidencias;
	private String										implementacionComentariosConsultor;
	private String										implementacionComentariosCliente;

	private String										imprimir;

	private List<DocumentoCronograma>	tDocumentos;

	private List<IndicadorCronograma>	tIndicadoresCronograma;

	private List<Indicador>						tIndicadores;

	private Indicador									indicador;

	private EstructuraTabla						estructuraTabla;


	public Cronograma() {
		this.proyectoCliente = new ProyectoCliente();
		this.tareaProyecto = new TareaProyecto();
		this.indicador = new Indicador();
		this.estructuraTabla = new EstructuraTabla();
	}

	public void getCamposBD() {

		this.estructuraTabla.setTabla("cronograma");
		this.estructuraTabla.getLlavePrimaria().put("id", this.id);

		if (this.proyectoCliente != null && this.proyectoCliente.getId() != null) {
			this.estructuraTabla.getPersistencia().put("id_proyecto_cliente", this.proyectoCliente.getId());
		} else {
			this.estructuraTabla.getPersistencia().put("id_proyecto_cliente", null);
		}
		if (this.tareaProyecto != null && this.tareaProyecto.getId() != null) {
			this.estructuraTabla.getPersistencia().put("id_tarea", this.tareaProyecto.getId());
		} else {
			this.estructuraTabla.getPersistencia().put("id_tarea", null);
		}

		this.estructuraTabla.getPersistencia().put("meta_documentacion", this.metaDocumentacion);
		this.estructuraTabla.getPersistencia().put("meta_socializacion", this.metaSocializacion);
		this.estructuraTabla.getPersistencia().put("meta_implementacion", this.metaImplementacion);
		this.estructuraTabla.getPersistencia().put("meta_auditoria", this.metaAuditoria);

		this.estructuraTabla.getPersistencia().put("fecha_inicio_documentacion", this.fechaInicioDocumentacion);
		this.estructuraTabla.getPersistencia().put("fecha_inicio_socializacion", this.fechaInicioSocializacion);
		this.estructuraTabla.getPersistencia().put("fecha_inicio_implementacion", this.fechaInicioImplementacion);
		this.estructuraTabla.getPersistencia().put("fecha_inicio_auditoria", this.fechaInicioAuditoria);

		this.estructuraTabla.getPersistencia().put("fecha_fin_documentacion", this.fechaFinDocumentacion);
		this.estructuraTabla.getPersistencia().put("fecha_fin_socializacion", this.fechaFinSocializacion);
		this.estructuraTabla.getPersistencia().put("fecha_fin_implementacion", this.fechaFinImplementacion);
		this.estructuraTabla.getPersistencia().put("fecha_fin_auditoria", this.fechaFinAuditoria);

		this.estructuraTabla.getPersistencia().put("fecha_seguimiento_documentacion", this.fechaSeguimientoDocumentacion);
		this.estructuraTabla.getPersistencia().put("fecha_seguimiento_socializacion", this.fechaSeguimientoSocializacion);
		this.estructuraTabla.getPersistencia().put("fecha_seguimiento_implementacion", this.fechaSeguimientoImplementacion);
		this.estructuraTabla.getPersistencia().put("fecha_seguimiento_auditoria", this.fechaSeguimientoAuditoria);

		this.estructuraTabla.getPersistencia().put("responsable_documentacion", this.responsableDocumentacion);
		this.estructuraTabla.getPersistencia().put("responsable_socializacion", this.responsableSocializacion);
		this.estructuraTabla.getPersistencia().put("responsable_implementacion", this.responsableImplementacion);
		this.estructuraTabla.getPersistencia().put("responsable_auditoria", this.responsableAuditoria);

		this.estructuraTabla.getPersistencia().put("recomendaciones", this.recomendaciones);

		this.estructuraTabla.getPersistencia().put("imprimir", this.imprimir);

		this.estructuraTabla.getPersistencia().put("implementacion_evidencias", this.implementacionEvidencias);
		this.estructuraTabla.getPersistencia().put("implementacion_comentarios_consultor", this.implementacionComentariosConsultor);
		this.estructuraTabla.getPersistencia().put("implementacion_comentarios_cliente", this.implementacionComentariosCliente);

		if (this.indicador != null && this.indicador.getId() != null) {
			this.estructuraTabla.getPersistencia().put("id_indicador", this.indicador.getId());
		} else {
			this.estructuraTabla.getPersistencia().put("id_indicador", null);
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

	public TareaProyecto getTareaProyecto() {
		return tareaProyecto;
	}

	public void setTareaProyecto(TareaProyecto tareaProyecto) {
		this.tareaProyecto = tareaProyecto;
	}

	public EstructuraTabla getEstructuraTabla() {
		return estructuraTabla;
	}

	public void setEstructuraTabla(EstructuraTabla estructuraTabla) {
		this.estructuraTabla = estructuraTabla;
	}

	public String getRecomendaciones() {
		return recomendaciones;
	}

	public void setRecomendaciones(String recomendaciones) {
		this.recomendaciones = recomendaciones;
	}

	@Digits(integer = 4, fraction = 2, message = IConstantes.VALIDACION_MAXIMO_DECIMAL)
	@DecimalMin(value = "0.00", message = IConstantes.VALIDACION_MINIMO_ENTERO)
	public BigDecimal getMetaDocumentacion() {
		return metaDocumentacion;
	}

	public void setMetaDocumentacion(BigDecimal metaDocumentacion) {
		this.metaDocumentacion = metaDocumentacion;
	}

	@Digits(integer = 4, fraction = 2, message = IConstantes.VALIDACION_MAXIMO_DECIMAL)
	@DecimalMin(value = "0.00", message = IConstantes.VALIDACION_MINIMO_ENTERO)
	public BigDecimal getMetaSocializacion() {
		return metaSocializacion;
	}

	public void setMetaSocializacion(BigDecimal metaSocializacion) {
		this.metaSocializacion = metaSocializacion;
	}

	@Digits(integer = 4, fraction = 2, message = IConstantes.VALIDACION_MAXIMO_DECIMAL)
	@DecimalMin(value = "0.00", message = IConstantes.VALIDACION_MINIMO_ENTERO)
	public BigDecimal getMetaImplementacion() {
		return metaImplementacion;
	}

	public void setMetaImplementacion(BigDecimal metaImplementacion) {
		this.metaImplementacion = metaImplementacion;
	}

	@Digits(integer = 4, fraction = 2, message = IConstantes.VALIDACION_MAXIMO_DECIMAL)
	@DecimalMin(value = "0.00", message = IConstantes.VALIDACION_MINIMO_ENTERO)
	public BigDecimal getMetaAuditoria() {
		return metaAuditoria;
	}

	public void setMetaAuditoria(BigDecimal metaAuditoria) {
		this.metaAuditoria = metaAuditoria;
	}

	public Date getFechaInicioDocumentacion() {
		return fechaInicioDocumentacion;
	}

	public void setFechaInicioDocumentacion(Date fechaInicioDocumentacion) {
		this.fechaInicioDocumentacion = fechaInicioDocumentacion;
	}

	public Date getFechaInicioSocializacion() {
		return fechaInicioSocializacion;
	}

	public void setFechaInicioSocializacion(Date fechaInicioSocializacion) {
		this.fechaInicioSocializacion = fechaInicioSocializacion;
	}

	public Date getFechaInicioImplementacion() {
		return fechaInicioImplementacion;
	}

	public void setFechaInicioImplementacion(Date fechaInicioImplementacion) {
		this.fechaInicioImplementacion = fechaInicioImplementacion;
	}

	public Date getFechaInicioAuditoria() {
		return fechaInicioAuditoria;
	}

	public void setFechaInicioAuditoria(Date fechaInicioAuditoria) {
		this.fechaInicioAuditoria = fechaInicioAuditoria;
	}

	public Date getFechaFinDocumentacion() {
		return fechaFinDocumentacion;
	}

	public void setFechaFinDocumentacion(Date fechaFinDocumentacion) {
		this.fechaFinDocumentacion = fechaFinDocumentacion;
	}

	public Date getFechaFinSocializacion() {
		return fechaFinSocializacion;
	}

	public void setFechaFinSocializacion(Date fechaFinSocializacion) {
		this.fechaFinSocializacion = fechaFinSocializacion;
	}

	public Date getFechaFinImplementacion() {
		return fechaFinImplementacion;
	}

	public void setFechaFinImplementacion(Date fechaFinImplementacion) {
		this.fechaFinImplementacion = fechaFinImplementacion;
	}

	public Date getFechaFinAuditoria() {
		return fechaFinAuditoria;
	}

	public void setFechaFinAuditoria(Date fechaFinAuditoria) {
		this.fechaFinAuditoria = fechaFinAuditoria;
	}

	public Date getFechaSeguimientoDocumentacion() {
		return fechaSeguimientoDocumentacion;
	}

	public void setFechaSeguimientoDocumentacion(Date fechaSeguimientoDocumentacion) {
		this.fechaSeguimientoDocumentacion = fechaSeguimientoDocumentacion;
	}

	public Date getFechaSeguimientoSocializacion() {
		return fechaSeguimientoSocializacion;
	}

	public void setFechaSeguimientoSocializacion(Date fechaSeguimientoSocializacion) {
		this.fechaSeguimientoSocializacion = fechaSeguimientoSocializacion;
	}

	public Date getFechaSeguimientoImplementacion() {
		return fechaSeguimientoImplementacion;
	}

	public void setFechaSeguimientoImplementacion(Date fechaSeguimientoImplementacion) {
		this.fechaSeguimientoImplementacion = fechaSeguimientoImplementacion;
	}

	public Date getFechaSeguimientoAuditoria() {
		return fechaSeguimientoAuditoria;
	}

	public void setFechaSeguimientoAuditoria(Date fechaSeguimientoAuditoria) {
		this.fechaSeguimientoAuditoria = fechaSeguimientoAuditoria;
	}

	public String getResponsableDocumentacion() {
		return responsableDocumentacion;
	}

	public void setResponsableDocumentacion(String responsableDocumentacion) {
		this.responsableDocumentacion = responsableDocumentacion;
	}

	public String getResponsableSocializacion() {
		return responsableSocializacion;
	}

	public void setResponsableSocializacion(String responsableSocializacion) {
		this.responsableSocializacion = responsableSocializacion;
	}

	public String getResponsableImplementacion() {
		return responsableImplementacion;
	}

	public void setResponsableImplementacion(String responsableImplementacion) {
		this.responsableImplementacion = responsableImplementacion;
	}

	public String getResponsableAuditoria() {
		return responsableAuditoria;
	}

	public void setResponsableAuditoria(String responsableAuditoria) {
		this.responsableAuditoria = responsableAuditoria;
	}

	public List<DocumentoCronograma> gettDocumentos() {
		return tDocumentos;
	}

	public void settDocumentos(List<DocumentoCronograma> tDocumentos) {
		this.tDocumentos = tDocumentos;
	}

	public String getImplementacionEvidencias() {
		return implementacionEvidencias;
	}

	public void setImplementacionEvidencias(String implementacionEvidencias) {
		this.implementacionEvidencias = implementacionEvidencias;
	}

	public String getImplementacionComentariosConsultor() {
		return implementacionComentariosConsultor;
	}

	public void setImplementacionComentariosConsultor(String implementacionComentariosConsultor) {
		this.implementacionComentariosConsultor = implementacionComentariosConsultor;
	}

	public String getImplementacionComentariosCliente() {
		return implementacionComentariosCliente;
	}

	public void setImplementacionComentariosCliente(String implementacionComentariosCliente) {
		this.implementacionComentariosCliente = implementacionComentariosCliente;
	}

	public List<IndicadorCronograma> gettIndicadoresCronograma() {
		return tIndicadoresCronograma;
	}

	public void settIndicadoresCronograma(List<IndicadorCronograma> tIndicadoresCronograma) {
		this.tIndicadoresCronograma = tIndicadoresCronograma;
	}

	public Indicador getIndicador() {
		return indicador;
	}

	public void setIndicador(Indicador indicador) {
		this.indicador = indicador;
	}

	public List<Indicador> gettIndicadores() {
		return tIndicadores;
	}

	public void settIndicadores(List<Indicador> tIndicadores) {
		this.tIndicadores = tIndicadores;
	}

	public String getImprimir() {
		return imprimir;
	}

	public void setImprimir(String imprimir) {
		this.imprimir = imprimir;
	}



}
