package consultoria.beans;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Documentacion implements Serializable {

	/**
	 * 
	 */
	private static final long					serialVersionUID	= 7312075806756466445L;
	private Integer										id;
	private ProyectoCliente						proyectoCliente;
	private TareaProyecto							tareaProyecto;

	private String										recomendacionesConsultor;
	private String										recomendacionesCliente;
	private String										explicacionConsultor;

	private String										estadoActualDocumentacion;
	private Date											fechaRevision;
	private Date											fechaAprobacion;

	private EstructuraTabla						estructuraTabla;

	private List<DocumentoActividad>	tDocumentos;

	public Documentacion() {
		this.proyectoCliente = new ProyectoCliente();
		this.tareaProyecto = new TareaProyecto();
		this.estructuraTabla = new EstructuraTabla();
	}

	public void getCamposBD() {

		this.estructuraTabla.setTabla("documentacion");
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

		this.estructuraTabla.getPersistencia().put("recomendaciones_consultor", this.recomendacionesConsultor);
		this.estructuraTabla.getPersistencia().put("recomendaciones_cliente", this.recomendacionesCliente);
		this.estructuraTabla.getPersistencia().put("explicacion_consultor", this.explicacionConsultor);

		this.estructuraTabla.getPersistencia().put("fecha_revision", this.fechaRevision);
		this.estructuraTabla.getPersistencia().put("fecha_aprobacion", this.fechaAprobacion);

		this.estructuraTabla.getPersistencia().put("estado_actual_documentacion", this.estadoActualDocumentacion);

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

	public String getRecomendacionesConsultor() {
		return recomendacionesConsultor;
	}

	public void setRecomendacionesConsultor(String recomendacionesConsultor) {
		this.recomendacionesConsultor = recomendacionesConsultor;
	}

	public String getRecomendacionesCliente() {
		return recomendacionesCliente;
	}

	public void setRecomendacionesCliente(String recomendacionesCliente) {
		this.recomendacionesCliente = recomendacionesCliente;
	}

	public String getExplicacionConsultor() {
		return explicacionConsultor;
	}

	public void setExplicacionConsultor(String explicacionConsultor) {
		this.explicacionConsultor = explicacionConsultor;
	}

	public Date getFechaRevision() {
		return fechaRevision;
	}

	public void setFechaRevision(Date fechaRevision) {
		this.fechaRevision = fechaRevision;
	}

	public Date getFechaAprobacion() {
		return fechaAprobacion;
	}

	public void setFechaAprobacion(Date fechaAprobacion) {
		this.fechaAprobacion = fechaAprobacion;
	}

	public String getEstadoActualDocumentacion() {
		return estadoActualDocumentacion;
	}

	public void setEstadoActualDocumentacion(String estadoActualDocumentacion) {
		this.estadoActualDocumentacion = estadoActualDocumentacion;
	}

	public List<DocumentoActividad> gettDocumentos() {
		return tDocumentos;
	}

	public void settDocumentos(List<DocumentoActividad> tDocumentos) {
		this.tDocumentos = tDocumentos;
	}

}
