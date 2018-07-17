package consultoria.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;

import consultoria.generales.IConstantes;

public class Diagnostico implements Serializable {

	/**
	 * 
	 */
	private static final long						serialVersionUID	= -3092341487158825224L;
	private Integer											id;
	private String											evidenciaEncontrada;
	private ProyectoCliente							proyectoCliente;
	private PreguntaProyecto						preguntaProyecto;
	private Integer											tIdRegistro;
	private String											tHallazgoSeleccionado;

	private List<EstadoDiagnostico>			tEstadosDiagnostico;

	private List<DocumentoDiagnostico>	tDocumentos;

	private String											tTipo;

	private EstructuraTabla							estructuraTabla;

	// nuevos

	private String											analisisCausa;
	private String											accionesRealizar;

	//

	private BigDecimal									metaDocumentacion1;
	private BigDecimal									metaSocializacion1;
	private BigDecimal									metaImplementacion1;
	private BigDecimal									metaAuditoria1;
	private String											observacion1;

	private BigDecimal									metaDocumentacion2;
	private BigDecimal									metaSocializacion2;
	private BigDecimal									metaImplementacion2;
	private BigDecimal									metaAuditoria2;
	private String											observacion2;

	private BigDecimal									metaDocumentacion3;
	private BigDecimal									metaSocializacion3;
	private BigDecimal									metaImplementacion3;
	private BigDecimal									metaAuditoria3;
	private String											observacion3;

	private BigDecimal									metaDocumentacion4;
	private BigDecimal									metaSocializacion4;
	private BigDecimal									metaImplementacion4;
	private BigDecimal									metaAuditoria4;
	private String											observacion4;

	private String											diagnostico;
	
	
	//nuevo again
	
	private String											evidenciaNoEncontrada;
	private String											responsable;
	

	public Diagnostico() {
		this.proyectoCliente = new ProyectoCliente();
		this.preguntaProyecto = new PreguntaProyecto();
		this.estructuraTabla = new EstructuraTabla();
	}

	public void getCamposBD() {

		this.estructuraTabla.setTabla("diagnostico");
		this.estructuraTabla.getLlavePrimaria().put("id", this.id);
		this.estructuraTabla.getPersistencia().put("evidencia_encontrada", this.evidenciaEncontrada);

		if (this.proyectoCliente != null && this.proyectoCliente.getId() != null) {
			this.estructuraTabla.getPersistencia().put("id_proyecto_cliente", this.proyectoCliente.getId());
		} else {
			this.estructuraTabla.getPersistencia().put("id_proyecto_cliente", null);
		}
		if (this.preguntaProyecto != null && this.preguntaProyecto.getId() != null) {
			this.estructuraTabla.getPersistencia().put("id_pregunta_proyecto", this.preguntaProyecto.getId());
		} else {
			this.estructuraTabla.getPersistencia().put("id_pregunta_proyecto", null);
		}

		this.estructuraTabla.getPersistencia().put("analisis_causas", this.analisisCausa);
		this.estructuraTabla.getPersistencia().put("accciones_realizar", this.accionesRealizar);

		this.estructuraTabla.getPersistencia().put("meta_documentacion1", this.metaDocumentacion1);
		this.estructuraTabla.getPersistencia().put("meta_socializacion1", this.metaSocializacion1);
		this.estructuraTabla.getPersistencia().put("meta_implementacion1", this.metaImplementacion1);
		this.estructuraTabla.getPersistencia().put("meta_auditoria1", this.metaAuditoria1);
		this.estructuraTabla.getPersistencia().put("observacion1", this.observacion1);

		this.estructuraTabla.getPersistencia().put("meta_documentacion2", this.metaDocumentacion2);
		this.estructuraTabla.getPersistencia().put("meta_socializacion2", this.metaSocializacion2);
		this.estructuraTabla.getPersistencia().put("meta_implementacion2", this.metaImplementacion2);
		this.estructuraTabla.getPersistencia().put("meta_auditoria2", this.metaAuditoria2);
		this.estructuraTabla.getPersistencia().put("observacion2", this.observacion2);

		this.estructuraTabla.getPersistencia().put("meta_documentacion3", this.metaDocumentacion3);
		this.estructuraTabla.getPersistencia().put("meta_socializacion3", this.metaSocializacion3);
		this.estructuraTabla.getPersistencia().put("meta_implementacion3", this.metaImplementacion3);
		this.estructuraTabla.getPersistencia().put("meta_auditoria3", this.metaAuditoria3);
		this.estructuraTabla.getPersistencia().put("observacion3", this.observacion3);

		this.estructuraTabla.getPersistencia().put("meta_documentacion4", this.metaDocumentacion4);
		this.estructuraTabla.getPersistencia().put("meta_socializacion4", this.metaSocializacion4);
		this.estructuraTabla.getPersistencia().put("meta_implementacion4", this.metaImplementacion4);
		this.estructuraTabla.getPersistencia().put("meta_auditoria4", this.metaAuditoria4);
		this.estructuraTabla.getPersistencia().put("observacion4", this.observacion4);

		this.estructuraTabla.getPersistencia().put("diagnostico", this.diagnostico);
		
		
		this.estructuraTabla.getPersistencia().put("evidencia_no_encontrada", this.evidenciaNoEncontrada);
		this.estructuraTabla.getPersistencia().put("responsable", this.responsable);
		
		

	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEvidenciaEncontrada() {
		return evidenciaEncontrada;
	}

	public void setEvidenciaEncontrada(String evidenciaEncontrada) {
		this.evidenciaEncontrada = evidenciaEncontrada;
	}

	public ProyectoCliente getProyectoCliente() {
		return proyectoCliente;
	}

	public void setProyectoCliente(ProyectoCliente proyectoCliente) {
		this.proyectoCliente = proyectoCliente;
	}

	public PreguntaProyecto getPreguntaProyecto() {
		return preguntaProyecto;
	}

	public void setPreguntaProyecto(PreguntaProyecto preguntaProyecto) {
		this.preguntaProyecto = preguntaProyecto;
	}

	public EstructuraTabla getEstructuraTabla() {
		return estructuraTabla;
	}

	public void setEstructuraTabla(EstructuraTabla estructuraTabla) {
		this.estructuraTabla = estructuraTabla;
	}

	public List<EstadoDiagnostico> gettEstadosDiagnostico() {
		return tEstadosDiagnostico;
	}

	public void settEstadosDiagnostico(List<EstadoDiagnostico> tEstadosDiagnostico) {
		this.tEstadosDiagnostico = tEstadosDiagnostico;
	}

	public Integer gettIdRegistro() {
		return tIdRegistro;
	}

	public void settIdRegistro(Integer tIdRegistro) {
		this.tIdRegistro = tIdRegistro;
	}

	public String gettHallazgoSeleccionado() {
		return tHallazgoSeleccionado;
	}

	public void settHallazgoSeleccionado(String tHallazgoSeleccionado) {
		this.tHallazgoSeleccionado = tHallazgoSeleccionado;
	}

	public List<DocumentoDiagnostico> gettDocumentos() {
		return tDocumentos;
	}

	public void settDocumentos(List<DocumentoDiagnostico> tDocumentos) {
		this.tDocumentos = tDocumentos;
	}

	public String getAnalisisCausa() {
		return analisisCausa;
	}

	public void setAnalisisCausa(String analisisCausa) {
		this.analisisCausa = analisisCausa;
	}

	public String getAccionesRealizar() {
		return accionesRealizar;
	}

	public void setAccionesRealizar(String accionesRealizar) {
		this.accionesRealizar = accionesRealizar;
	}

	public String gettTipo() {
		return tTipo;
	}

	public void settTipo(String tTipo) {
		this.tTipo = tTipo;
	}

	@Digits(integer = 4, fraction = 2, message = IConstantes.VALIDACION_MAXIMO_DECIMAL)
	@DecimalMin(value = "0.00", message = IConstantes.VALIDACION_MINIMO_ENTERO)
	public BigDecimal getMetaDocumentacion1() {
		if (metaDocumentacion1 == null) {
			metaDocumentacion1 = new BigDecimal(0);
		}
		return metaDocumentacion1;
	}

	public void setMetaDocumentacion1(BigDecimal metaDocumentacion1) {
		this.metaDocumentacion1 = metaDocumentacion1;
	}

	@Digits(integer = 4, fraction = 2, message = IConstantes.VALIDACION_MAXIMO_DECIMAL)
	@DecimalMin(value = "0.00", message = IConstantes.VALIDACION_MINIMO_ENTERO)
	public BigDecimal getMetaSocializacion1() {
		if (metaSocializacion1 == null) {
			metaSocializacion1 = new BigDecimal(0);
		}
		return metaSocializacion1;
	}

	public void setMetaSocializacion1(BigDecimal metaSocializacion1) {
		this.metaSocializacion1 = metaSocializacion1;
	}

	@Digits(integer = 4, fraction = 2, message = IConstantes.VALIDACION_MAXIMO_DECIMAL)
	@DecimalMin(value = "0.00", message = IConstantes.VALIDACION_MINIMO_ENTERO)
	public BigDecimal getMetaImplementacion1() {
		if (metaImplementacion1 == null) {
			metaImplementacion1 = new BigDecimal(0);
		}
		return metaImplementacion1;
	}

	public void setMetaImplementacion1(BigDecimal metaImplementacion1) {
		this.metaImplementacion1 = metaImplementacion1;
	}

	@Digits(integer = 4, fraction = 2, message = IConstantes.VALIDACION_MAXIMO_DECIMAL)
	@DecimalMin(value = "0.00", message = IConstantes.VALIDACION_MINIMO_ENTERO)
	public BigDecimal getMetaAuditoria1() {
		if (metaAuditoria1 == null) {
			metaAuditoria1 = new BigDecimal(0);
		}
		return metaAuditoria1;
	}

	public void setMetaAuditoria1(BigDecimal metaAuditoria1) {
		this.metaAuditoria1 = metaAuditoria1;
	}

	public String getObservacion1() {
		return observacion1;
	}

	public void setObservacion1(String observacion1) {
		this.observacion1 = observacion1;
	}

	@Digits(integer = 4, fraction = 2, message = IConstantes.VALIDACION_MAXIMO_DECIMAL)
	@DecimalMin(value = "0.00", message = IConstantes.VALIDACION_MINIMO_ENTERO)
	public BigDecimal getMetaDocumentacion2() {
		if (metaDocumentacion2 == null) {
			metaDocumentacion2 = new BigDecimal(0);
		}
		return metaDocumentacion2;
	}

	public void setMetaDocumentacion2(BigDecimal metaDocumentacion2) {
		this.metaDocumentacion2 = metaDocumentacion2;
	}

	@Digits(integer = 4, fraction = 2, message = IConstantes.VALIDACION_MAXIMO_DECIMAL)
	@DecimalMin(value = "0.00", message = IConstantes.VALIDACION_MINIMO_ENTERO)
	public BigDecimal getMetaSocializacion2() {
		if (metaSocializacion2 == null) {
			metaSocializacion2 = new BigDecimal(0);
		}
		return metaSocializacion2;
	}

	public void setMetaSocializacion2(BigDecimal metaSocializacion2) {
		this.metaSocializacion2 = metaSocializacion2;
	}

	@Digits(integer = 4, fraction = 2, message = IConstantes.VALIDACION_MAXIMO_DECIMAL)
	@DecimalMin(value = "0.00", message = IConstantes.VALIDACION_MINIMO_ENTERO)
	public BigDecimal getMetaImplementacion2() {
		if (metaImplementacion2 == null) {
			metaImplementacion2 = new BigDecimal(0);
		}
		return metaImplementacion2;
	}

	public void setMetaImplementacion2(BigDecimal metaImplementacion2) {
		this.metaImplementacion2 = metaImplementacion2;
	}

	@Digits(integer = 4, fraction = 2, message = IConstantes.VALIDACION_MAXIMO_DECIMAL)
	@DecimalMin(value = "0.00", message = IConstantes.VALIDACION_MINIMO_ENTERO)
	public BigDecimal getMetaAuditoria2() {
		if (metaAuditoria2 == null) {
			metaAuditoria2 = new BigDecimal(0);
		}
		return metaAuditoria2;
	}

	public void setMetaAuditoria2(BigDecimal metaAuditoria2) {
		this.metaAuditoria2 = metaAuditoria2;
	}

	public String getObservacion2() {
		return observacion2;
	}

	public void setObservacion2(String observacion2) {
		this.observacion2 = observacion2;
	}

	@Digits(integer = 4, fraction = 2, message = IConstantes.VALIDACION_MAXIMO_DECIMAL)
	@DecimalMin(value = "0.00", message = IConstantes.VALIDACION_MINIMO_ENTERO)
	public BigDecimal getMetaDocumentacion3() {
		if (metaDocumentacion3 == null) {
			metaDocumentacion3 = new BigDecimal(0);
		}
		return metaDocumentacion3;
	}

	public void setMetaDocumentacion3(BigDecimal metaDocumentacion3) {
		this.metaDocumentacion3 = metaDocumentacion3;
	}

	@Digits(integer = 4, fraction = 2, message = IConstantes.VALIDACION_MAXIMO_DECIMAL)
	@DecimalMin(value = "0.00", message = IConstantes.VALIDACION_MINIMO_ENTERO)
	public BigDecimal getMetaSocializacion3() {
		if (metaSocializacion3 == null) {
			metaSocializacion3 = new BigDecimal(0);
		}
		return metaSocializacion3;
	}

	public void setMetaSocializacion3(BigDecimal metaSocializacion3) {
		this.metaSocializacion3 = metaSocializacion3;
	}

	@Digits(integer = 4, fraction = 2, message = IConstantes.VALIDACION_MAXIMO_DECIMAL)
	@DecimalMin(value = "0.00", message = IConstantes.VALIDACION_MINIMO_ENTERO)
	public BigDecimal getMetaImplementacion3() {
		if (metaImplementacion3 == null) {
			metaImplementacion3 = new BigDecimal(0);
		}
		return metaImplementacion3;
	}

	public void setMetaImplementacion3(BigDecimal metaImplementacion3) {
		this.metaImplementacion3 = metaImplementacion3;
	}

	@Digits(integer = 4, fraction = 2, message = IConstantes.VALIDACION_MAXIMO_DECIMAL)
	@DecimalMin(value = "0.00", message = IConstantes.VALIDACION_MINIMO_ENTERO)
	public BigDecimal getMetaAuditoria3() {
		if (metaAuditoria3 == null) {
			metaAuditoria3 = new BigDecimal(0);
		}
		return metaAuditoria3;
	}

	public void setMetaAuditoria3(BigDecimal metaAuditoria3) {
		this.metaAuditoria3 = metaAuditoria3;
	}

	public String getObservacion3() {
		return observacion3;
	}

	public void setObservacion3(String observacion3) {
		this.observacion3 = observacion3;
	}

	@Digits(integer = 4, fraction = 2, message = IConstantes.VALIDACION_MAXIMO_DECIMAL)
	@DecimalMin(value = "0.00", message = IConstantes.VALIDACION_MINIMO_ENTERO)
	public BigDecimal getMetaDocumentacion4() {
		if (metaDocumentacion4 == null) {
			metaDocumentacion4 = new BigDecimal(0);
		}
		return metaDocumentacion4;
	}

	public void setMetaDocumentacion4(BigDecimal metaDocumentacion4) {
		this.metaDocumentacion4 = metaDocumentacion4;
	}

	@Digits(integer = 4, fraction = 2, message = IConstantes.VALIDACION_MAXIMO_DECIMAL)
	@DecimalMin(value = "0.00", message = IConstantes.VALIDACION_MINIMO_ENTERO)
	public BigDecimal getMetaSocializacion4() {
		if (metaSocializacion4 == null) {
			metaSocializacion4 = new BigDecimal(0);
		}
		return metaSocializacion4;
	}

	public void setMetaSocializacion4(BigDecimal metaSocializacion4) {
		this.metaSocializacion4 = metaSocializacion4;
	}

	@Digits(integer = 4, fraction = 2, message = IConstantes.VALIDACION_MAXIMO_DECIMAL)
	@DecimalMin(value = "0.00", message = IConstantes.VALIDACION_MINIMO_ENTERO)
	public BigDecimal getMetaImplementacion4() {
		if (metaImplementacion4 == null) {
			metaImplementacion4 = new BigDecimal(0);
		}
		return metaImplementacion4;
	}

	public void setMetaImplementacion4(BigDecimal metaImplementacion4) {
		this.metaImplementacion4 = metaImplementacion4;
	}

	@Digits(integer = 4, fraction = 2, message = IConstantes.VALIDACION_MAXIMO_DECIMAL)
	@DecimalMin(value = "0.00", message = IConstantes.VALIDACION_MINIMO_ENTERO)
	public BigDecimal getMetaAuditoria4() {
		if (metaAuditoria4 == null) {
			metaAuditoria4 = new BigDecimal(0);
		}
		return metaAuditoria4;
	}

	public void setMetaAuditoria4(BigDecimal metaAuditoria4) {
		this.metaAuditoria4 = metaAuditoria4;
	}

	public String getObservacion4() {
		return observacion4;
	}

	public void setObservacion4(String observacion4) {
		this.observacion4 = observacion4;
	}

	public String getDiagnostico() {
		return diagnostico;
	}

	public void setDiagnostico(String diagnostico) {
		this.diagnostico = diagnostico;
	}

	public String getEvidenciaNoEncontrada() {
		return evidenciaNoEncontrada;
	}

	public void setEvidenciaNoEncontrada(String evidenciaNoEncontrada) {
		this.evidenciaNoEncontrada = evidenciaNoEncontrada;
	}

	public String getResponsable() {
		return responsable;
	}

	public void setResponsable(String responsable) {
		this.responsable = responsable;
	}
	
	
	
	
	
	

}
