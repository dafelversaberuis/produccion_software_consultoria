package consultoria.beans;

import java.io.Serializable;

public class ParametroAuditoria implements Serializable {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1467195542587463051L;
	private Integer						id;
	private String						objetivos;
	private String						alcance;
	private String						documentosReferencia;
	private String 						observaciones;
	

	private EstructuraTabla		estructuraTabla;

	public ParametroAuditoria() {
		this.estructuraTabla = new EstructuraTabla();
	}

	public void getCamposBD() {

		this.estructuraTabla.setTabla("parametros_auditoria");
		this.estructuraTabla.getLlavePrimaria().put("id", this.id);
		this.estructuraTabla.getPersistencia().put("objetivos", this.objetivos);
		this.estructuraTabla.getPersistencia().put("alcance", this.alcance);
		this.estructuraTabla.getPersistencia().put("documentos_referencia", this.documentosReferencia);
		this.estructuraTabla.getPersistencia().put("observaciones", this.observaciones);

	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getObjetivos() {
		return objetivos;
	}

	public void setObjetivos(String objetivos) {
		this.objetivos = objetivos;
	}

	public String getAlcance() {
		return alcance;
	}

	public void setAlcance(String alcance) {
		this.alcance = alcance;
	}

	public String getDocumentosReferencia() {
		return documentosReferencia;
	}

	public void setDocumentosReferencia(String documentosReferencia) {
		this.documentosReferencia = documentosReferencia;
	}

	public EstructuraTabla getEstructuraTabla() {
		return estructuraTabla;
	}

	public void setEstructuraTabla(EstructuraTabla estructuraTabla) {
		this.estructuraTabla = estructuraTabla;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	
	
	

}
