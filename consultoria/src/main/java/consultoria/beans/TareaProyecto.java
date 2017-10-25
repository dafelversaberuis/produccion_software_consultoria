package consultoria.beans;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.Pattern;

import org.primefaces.model.UploadedFile;

import consultoria.generales.IConstantes;

public class TareaProyecto implements Serializable {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 812341773854950278L;
	private Integer						id;
	private String						tarea;
	private String						estado;
	private String						explicacionDocumentacion;

	private Date							fechaEstado;
	private Proyecto					proyecto;
	private Integer						posicion;

	private String						responsable;
	private String						producto;
	private Integer						numeroEtapa;

	private String						tFormaAgregar;

	private String						requisito;

	private EstructuraTabla		estructuraTabla;

	private byte[]						archivo;
	private UploadedFile			tFile;
	private String						tConcepto;
	private boolean						tApto;

	public TareaProyecto() {
		this.estructuraTabla = new EstructuraTabla();
		this.proyecto = new Proyecto();
	}

	public void getCamposBD() {

		this.estructuraTabla.setTabla("tareas_proyecto");
		this.estructuraTabla.getLlavePrimaria().put("id", this.id);
		this.estructuraTabla.getPersistencia().put("tarea", this.tarea);
		this.estructuraTabla.getPersistencia().put("estado", this.estado);
		this.estructuraTabla.getPersistencia().put("fecha_estado", this.fechaEstado);
		this.estructuraTabla.getPersistencia().put("posicion", this.posicion);
		this.estructuraTabla.getPersistencia().put("producto", this.producto);
		this.estructuraTabla.getPersistencia().put("responsable", this.responsable);
		this.estructuraTabla.getPersistencia().put("numero_etapa", this.numeroEtapa);
		this.estructuraTabla.getPersistencia().put("explicacion_documentacion", this.explicacionDocumentacion);

		if (this.proyecto != null && this.proyecto.getId() != null) {
			this.estructuraTabla.getPersistencia().put("id_proyecto", this.proyecto.getId());
		}

		this.estructuraTabla.getPersistencia().put("requisito", this.requisito);

	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTarea() {
		return tarea;
	}

	public void setTarea(String tarea) {
		this.tarea = tarea;
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

	public String getResponsable() {
		return responsable;
	}

	public void setResponsable(String responsable) {
		this.responsable = responsable;
	}

	public String getProducto() {
		return producto;
	}

	public void setProducto(String producto) {
		this.producto = producto;
	}

	public Integer getNumeroEtapa() {
		return numeroEtapa;
	}

	public void setNumeroEtapa(Integer numeroEtapa) {
		this.numeroEtapa = numeroEtapa;
	}

	public String getExplicacionDocumentacion() {
		return explicacionDocumentacion;
	}

	public void setExplicacionDocumentacion(String explicacionDocumentacion) {
		this.explicacionDocumentacion = explicacionDocumentacion;
	}

	public String getRequisito() {
		return requisito;
	}

	public void setRequisito(String requisito) {
		this.requisito = requisito;
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

}
