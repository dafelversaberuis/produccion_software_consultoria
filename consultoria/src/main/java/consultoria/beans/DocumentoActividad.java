package consultoria.beans;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.Size;

import org.primefaces.model.UploadedFile;

import consultoria.generales.IConstantes;

public class DocumentoActividad implements Serializable {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -5536959797879771531L;
	private Integer						id;
	private String						nombre;
	private TareaProyecto			tareaProyecto;

	private byte[]						archivo;
	private String						descargable;

	private UploadedFile			tFile;
	private String						tNombre;

	private Date							fecha;

	private ProyectoCliente		proyectoCliente;

	private Consultor					consultor;
	private Personal					personal;

	private String						contentType;
	private String						extensionArchivo;

	private String						estado;

	private EstructuraTabla		estructuraTabla;

	public DocumentoActividad() {

		this.estructuraTabla = new EstructuraTabla();
		this.tareaProyecto = new TareaProyecto();
		this.proyectoCliente = new ProyectoCliente();
		this.consultor = new Consultor();
		this.personal = new Personal();

	}

	public void getCamposBD() {

		this.estructuraTabla.setTabla("documentos_actividad");
		this.estructuraTabla.getLlavePrimaria().put("id", this.id);
		this.estructuraTabla.getPersistencia().put("nombre", this.nombre);
		this.estructuraTabla.getPersistencia().put("archivo", this.archivo);
		this.estructuraTabla.getPersistencia().put("descargable", this.descargable);

		this.estructuraTabla.getPersistencia().put("fecha", this.fecha);
		
		
		this.estructuraTabla.getPersistencia().put("estado", this.estado);

		this.estructuraTabla.getPersistencia().put("content_type", this.contentType);
		this.estructuraTabla.getPersistencia().put("extension_archivo", this.extensionArchivo);

		if (this.tareaProyecto != null && this.tareaProyecto.getId() != null) {
			this.estructuraTabla.getPersistencia().put("id_tarea_proyecto", this.tareaProyecto.getId());
		} else {
			this.estructuraTabla.getPersistencia().put("id_tarea_proyecto", this.tareaProyecto.getId());
		}

		if (this.proyectoCliente != null && this.proyectoCliente.getId() != null) {
			this.estructuraTabla.getPersistencia().put("id_proyecto_cliente", this.proyectoCliente.getId());
		} else {
			this.estructuraTabla.getPersistencia().put("id_proyecto_cliente", null);
		}

		if (this.consultor != null && this.consultor.getId() != null) {
			this.estructuraTabla.getPersistencia().put("id_consultor", this.consultor.getId());
		} else {
			this.estructuraTabla.getPersistencia().put("id_consultor", null);
		}

		if (this.personal != null && this.personal.getId() != null) {
			this.estructuraTabla.getPersistencia().put("id_administrador", this.personal.getId());
		} else {
			this.estructuraTabla.getPersistencia().put("id_administrador", null);
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

	public UploadedFile gettFile() {
		return tFile;
	}

	public void settFile(UploadedFile tFile) {
		this.tFile = tFile;
	}

	public TareaProyecto getTareaProyecto() {
		return tareaProyecto;
	}

	public void setTareaProyecto(TareaProyecto tareaProyecto) {
		this.tareaProyecto = tareaProyecto;
	}

	public String getDescargable() {
		return descargable;
	}

	public void setDescargable(String descargable) {
		this.descargable = descargable;
	}

	public String gettNombre() {
		return tNombre;
	}

	public void settNombre(String tNombre) {
		this.tNombre = tNombre;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public ProyectoCliente getProyectoCliente() {
		return proyectoCliente;
	}

	public void setProyectoCliente(ProyectoCliente proyectoCliente) {
		this.proyectoCliente = proyectoCliente;
	}

	public Consultor getConsultor() {
		return consultor;
	}

	public void setConsultor(Consultor consultor) {
		this.consultor = consultor;
	}

	public Personal getPersonal() {
		return personal;
	}

	public void setPersonal(Personal personal) {
		this.personal = personal;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getExtensionArchivo() {
		return extensionArchivo;
	}

	public void setExtensionArchivo(String extensionArchivo) {
		this.extensionArchivo = extensionArchivo;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

}
