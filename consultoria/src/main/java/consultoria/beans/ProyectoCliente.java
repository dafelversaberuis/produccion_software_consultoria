package consultoria.beans;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class ProyectoCliente implements Serializable {

	/**
	 * 
	 */
	private static final long						serialVersionUID	= -9091014121078102621L;
	private Integer											id;
	private Consultor										consultor;
	private Cliente											cliente;
	private Proyecto										proyecto;
	private Date												fechaInicio;
	private Date												fechaFin;
	private Date												fechaCertificacion;
	private Date												tFechaInicioCertificacion;
	private Date												tFechaFinCertificacion;
	private String											tEstadoCosnultoria;
	private String											tEstadoCertificacion;
	private String											tEstado;
	private List<EstadoProyectoCliente>	tEstadosProyecto;

	private EstructuraTabla							estructuraTabla;

	public ProyectoCliente() {
		this.estructuraTabla = new EstructuraTabla();
		this.consultor = new Consultor();
		this.cliente = new Cliente();
		this.proyecto = new Proyecto();
	}

	public void getCamposBD() {

		this.estructuraTabla.setTabla("proyectos_cliente");
		this.estructuraTabla.getLlavePrimaria().put("id", this.id);
		this.estructuraTabla.getPersistencia().put("fecha_inicio", this.fechaInicio);
		this.estructuraTabla.getPersistencia().put("fecha_fin", this.fechaFin);
		this.estructuraTabla.getPersistencia().put("fecha_certificacion", this.fechaCertificacion);

		if (this.consultor != null && this.consultor.getId() != null) {
			this.estructuraTabla.getPersistencia().put("id_consultor", this.consultor.getId());
		}

		if (this.cliente != null && this.cliente.getId() != null) {
			this.estructuraTabla.getPersistencia().put("id_cliente", this.cliente.getId());
		}

		if (this.proyecto != null && this.proyecto.getId() != null) {
			this.estructuraTabla.getPersistencia().put("id_proyecto", this.proyecto.getId());
		}

	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Consultor getConsultor() {
		return consultor;
	}

	public void setConsultor(Consultor consultor) {
		this.consultor = consultor;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Proyecto getProyecto() {
		return proyecto;
	}

	public void setProyecto(Proyecto proyecto) {
		this.proyecto = proyecto;
	}

	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Date getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	public EstructuraTabla getEstructuraTabla() {
		return estructuraTabla;
	}

	public void setEstructuraTabla(EstructuraTabla estructuraTabla) {
		this.estructuraTabla = estructuraTabla;
	}

	public Date getFechaCertificacion() {
		return fechaCertificacion;
	}

	public void setFechaCertificacion(Date fechaCertificacion) {
		this.fechaCertificacion = fechaCertificacion;
	}

	public Date gettFechaInicioCertificacion() {
		return tFechaInicioCertificacion;
	}

	public void settFechaInicioCertificacion(Date tFechaInicioCertificacion) {
		this.tFechaInicioCertificacion = tFechaInicioCertificacion;
	}

	public Date gettFechaFinCertificacion() {
		return tFechaFinCertificacion;
	}

	public void settFechaFinCertificacion(Date tFechaFinCertificacion) {
		this.tFechaFinCertificacion = tFechaFinCertificacion;
	}

	public String gettEstadoCosnultoria() {
		return tEstadoCosnultoria;
	}

	public void settEstadoCosnultoria(String tEstadoCosnultoria) {
		this.tEstadoCosnultoria = tEstadoCosnultoria;
	}

	public String gettEstadoCertificacion() {
		return tEstadoCertificacion;
	}

	public void settEstadoCertificacion(String tEstadoCertificacion) {
		this.tEstadoCertificacion = tEstadoCertificacion;
	}

	public String gettEstado() {
		return tEstado;
	}

	public void settEstado(String tEstado) {
		this.tEstado = tEstado;
	}

	public List<EstadoProyectoCliente> gettEstadosProyecto() {
		return tEstadosProyecto;
	}

	public void settEstadosProyecto(List<EstadoProyectoCliente> tEstadosProyecto) {
		this.tEstadosProyecto = tEstadosProyecto;
	}

}
