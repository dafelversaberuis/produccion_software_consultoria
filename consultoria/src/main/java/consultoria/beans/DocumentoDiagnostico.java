package consultoria.beans;

import java.io.Serializable;

import javax.validation.constraints.Size;

import org.primefaces.model.UploadedFile;

import consultoria.generales.IConstantes;

public class DocumentoDiagnostico implements Serializable {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -2399484154939038854L;
	private Integer						id;
	private String						nombre;
	private Diagnostico				diagnostico;

	private byte[]						archivo;
	private String						contentType;
	private String						extensionArchivo;

	private UploadedFile			tFile;

	private EstructuraTabla		estructuraTabla;

	public DocumentoDiagnostico() {
		this.estructuraTabla = new EstructuraTabla();
		this.diagnostico = new Diagnostico();

	}

	public void getCamposBD() {

		this.estructuraTabla.setTabla("documentos_diagnostico");
		this.estructuraTabla.getLlavePrimaria().put("id", this.id);
		this.estructuraTabla.getPersistencia().put("nombre", this.nombre);
		this.estructuraTabla.getPersistencia().put("archivo", this.archivo);
		this.estructuraTabla.getPersistencia().put("content_type", this.contentType);
		this.estructuraTabla.getPersistencia().put("extension_archivo", this.extensionArchivo);

		if (this.diagnostico != null && this.diagnostico.getId() != null) {
			this.estructuraTabla.getPersistencia().put("id_diagnostico", this.diagnostico.getId());
		} else {
			this.estructuraTabla.getPersistencia().put("id_diagnostico", this.diagnostico.getId());
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

	public Diagnostico getDiagnostico() {
		return diagnostico;
	}

	public void setDiagnostico(Diagnostico diagnostico) {
		this.diagnostico = diagnostico;
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

}
