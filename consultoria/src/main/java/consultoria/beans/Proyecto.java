package consultoria.beans;

import java.io.Serializable;

import javax.validation.constraints.Size;

import consultoria.generales.IConstantes;

public class Proyecto implements Serializable {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 5039295001305288750L;
	private Integer						id;
	private String						nombre;
	private String						indicativoVigencia;

	private String						objetivos;
	private String						alcance;
	private String						documentosReferencia;
	private String						observaciones;

	private EstructuraTabla		estructuraTabla;

	public Proyecto() {
		this.estructuraTabla = new EstructuraTabla();
	}

	public void getCamposBD() {

		this.estructuraTabla.setTabla("proyectos");
		this.estructuraTabla.getLlavePrimaria().put("id", this.id);
		this.estructuraTabla.getPersistencia().put("nombre", this.nombre);
		this.estructuraTabla.getPersistencia().put("indicativo_vigencia", this.indicativoVigencia);

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

	@Size(max = 250, message = IConstantes.VALIDACION_MAXIMA_LONGITUD)
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getIndicativoVigencia() {
		return indicativoVigencia;
	}

	public void setIndicativoVigencia(String indicativoVigencia) {
		this.indicativoVigencia = indicativoVigencia;
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
