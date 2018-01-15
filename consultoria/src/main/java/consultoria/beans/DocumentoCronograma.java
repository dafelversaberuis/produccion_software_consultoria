package consultoria.beans;

import java.io.Serializable;

import javax.validation.constraints.Size;

import org.primefaces.model.UploadedFile;

import consultoria.generales.IConstantes;

public class DocumentoCronograma implements Serializable {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 8003628525979401176L;
	private Integer						id;
	private String						nombre;
	private Cronograma				cronograma;

	private byte[]						archivo;
	private String						contentType;
	private String						extensionArchivo;

	private String						etapa;

	private UploadedFile			tFile;

	private EstructuraTabla		estructuraTabla;

	public DocumentoCronograma() {
		this.estructuraTabla = new EstructuraTabla();
		this.cronograma = new Cronograma();

	}

	public void getCamposBD() {

		this.estructuraTabla.setTabla("documentos_cronograma");
		this.estructuraTabla.getLlavePrimaria().put("id", this.id);
		this.estructuraTabla.getPersistencia().put("nombre", this.nombre);
		this.estructuraTabla.getPersistencia().put("archivo", this.archivo);
		this.estructuraTabla.getPersistencia().put("content_type", this.contentType);
		this.estructuraTabla.getPersistencia().put("extension_archivo", this.extensionArchivo);
		this.estructuraTabla.getPersistencia().put("etapa", this.etapa);

		if (this.cronograma != null && this.cronograma.getId() != null) {
			this.estructuraTabla.getPersistencia().put("id_cronograma", this.cronograma.getId());
		} else {
			this.estructuraTabla.getPersistencia().put("id_cronograma", this.cronograma.getId());
		}

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

	public byte[] getArchivo() {
		return archivo;
	}

	public void setArchivo(byte[] archivo) {
		this.archivo = archivo;
	}

	public EstructuraTabla getEstructuraTabla() {
		return estructuraTabla;
	}

	public void setEstructuraTabla(EstructuraTabla estructuraTabla) {
		this.estructuraTabla = estructuraTabla;
	}

	public Cronograma getCronograma() {
		return cronograma;
	}

	public void setCronograma(Cronograma cronograma) {
		this.cronograma = cronograma;
	}

	public UploadedFile gettFile() {
		return tFile;
	}

	public void settFile(UploadedFile tFile) {
		this.tFile = tFile;
	}

	@Size(max = 100, message = IConstantes.VALIDACION_MAXIMA_LONGITUD)
	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	@Size(max = 10, message = IConstantes.VALIDACION_MAXIMA_LONGITUD)
	public String getExtensionArchivo() {
		return extensionArchivo;
	}

	public void setExtensionArchivo(String extensionArchivo) {
		this.extensionArchivo = extensionArchivo;
	}

	public String getEtapa() {
		return etapa;
	}

	public void setEtapa(String etapa) {
		this.etapa = etapa;
	}

}
