package consultoria.beans;

import java.io.Serializable;
import java.util.Date;

public class Compromiso implements Serializable {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 8893099791680764232L;
	private Integer						id;
	private ProyectoCliente		proyectoCliente;

	private String						copromisos;
	private String						responsable;
	private Date							fechaInicioCompromiso;
	private Date							fechaFinCompromiso;
	private String						cumplimiento;

	private Date							fechaSeguimiento;
	private String						estado;
	private String						observacionesEstado;
	private Date							fechaParaCuando;
	
	private String tRangoFechas;

	private EstructuraTabla		estructuraTabla;

	public Compromiso() {
		this.proyectoCliente = new ProyectoCliente();
		this.estructuraTabla = new EstructuraTabla();

	}

	public void getCamposBD() {

		this.estructuraTabla.setTabla("compromisos");

		this.estructuraTabla.getLlavePrimaria().put("id", this.id);

		this.estructuraTabla.getPersistencia().put("id_proyecto_cliente", this.proyectoCliente.getId());
		this.estructuraTabla.getPersistencia().put("compromisos", this.copromisos);
		this.estructuraTabla.getPersistencia().put("responsable", this.responsable);
		this.estructuraTabla.getPersistencia().put("fecha_inicio_compromiso", this.fechaInicioCompromiso);
		this.estructuraTabla.getPersistencia().put("fecha_fin_compromiso", this.fechaFinCompromiso);
		this.estructuraTabla.getPersistencia().put("cumplimiento", this.cumplimiento);
		this.estructuraTabla.getPersistencia().put("fecha_seguimiento", this.fechaSeguimiento);
		this.estructuraTabla.getPersistencia().put("estado", this.estado);

		this.estructuraTabla.getPersistencia().put("observaciones", this.observacionesEstado);
		this.estructuraTabla.getPersistencia().put("para_cuando", this.fechaParaCuando);

	}

	public ProyectoCliente getProyectoCliente() {
		return proyectoCliente;
	}

	public void setProyectoCliente(ProyectoCliente proyectoCliente) {
		this.proyectoCliente = proyectoCliente;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCopromisos() {
		return copromisos;
	}

	public void setCopromisos(String copromisos) {
		this.copromisos = copromisos;
	}

	public String getResponsable() {
		return responsable;
	}

	public void setResponsable(String responsable) {
		this.responsable = responsable;
	}

	public Date getFechaInicioCompromiso() {
		return fechaInicioCompromiso;
	}

	public void setFechaInicioCompromiso(Date fechaInicioCompromiso) {
		this.fechaInicioCompromiso = fechaInicioCompromiso;
	}

	public Date getFechaFinCompromiso() {
		return fechaFinCompromiso;
	}

	public void setFechaFinCompromiso(Date fechaFinCompromiso) {
		this.fechaFinCompromiso = fechaFinCompromiso;
	}

	public String getCumplimiento() {
		return cumplimiento;
	}

	public void setCumplimiento(String cumplimiento) {
		this.cumplimiento = cumplimiento;
	}

	public Date getFechaSeguimiento() {
		return fechaSeguimiento;
	}

	public void setFechaSeguimiento(Date fechaSeguimiento) {
		this.fechaSeguimiento = fechaSeguimiento;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getObservacionesEstado() {
		return observacionesEstado;
	}

	public void setObservacionesEstado(String observacionesEstado) {
		this.observacionesEstado = observacionesEstado;
	}

	public Date getFechaParaCuando() {
		return fechaParaCuando;
	}

	public void setFechaParaCuando(Date fechaParaCuando) {
		this.fechaParaCuando = fechaParaCuando;
	}

	public EstructuraTabla getEstructuraTabla() {
		return estructuraTabla;
	}

	public void setEstructuraTabla(EstructuraTabla estructuraTabla) {
		this.estructuraTabla = estructuraTabla;
	}

	public String gettRangoFechas() {
		return tRangoFechas;
	}

	public void settRangoFechas(String tRangoFechas) {
		this.tRangoFechas = tRangoFechas;
	}
	
	
	

}
