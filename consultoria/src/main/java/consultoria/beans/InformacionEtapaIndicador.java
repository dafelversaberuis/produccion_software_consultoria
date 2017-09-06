package consultoria.beans;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class InformacionEtapaIndicador implements Serializable {

	/**
	 * 
	 */
	private static final long							serialVersionUID	= -2838948791314126716L;

	private Integer												id;
	private ProyectoCliente								proyectoCliente;
	private Date													fechaInicio;
	private String												firma;
	private String												conclusiones;
	private Date													fechaGeneracionTodo;

	private List<ObjetivoEtapaIndicador>	objetivos;

	private EstructuraTabla								estructuraTabla;

	public InformacionEtapaIndicador() {
		this.estructuraTabla = new EstructuraTabla();
		this.proyectoCliente = new ProyectoCliente();
	}

	public void getCamposBD() {

		this.estructuraTabla.setTabla("informacion_etapa_indicadores");
		this.estructuraTabla.getLlavePrimaria().put("id", this.id);

		this.estructuraTabla.getPersistencia().put("id_proyecto_cliente", this.proyectoCliente.getId());
		this.estructuraTabla.getPersistencia().put("fecha_inicio", this.fechaInicio);

		this.estructuraTabla.getPersistencia().put("firma", this.firma);
		this.estructuraTabla.getPersistencia().put("conclusiones", this.conclusiones);
		this.estructuraTabla.getPersistencia().put("fecha_generacion_todo", this.fechaGeneracionTodo);

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

	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public EstructuraTabla getEstructuraTabla() {
		return estructuraTabla;
	}

	public void setEstructuraTabla(EstructuraTabla estructuraTabla) {
		this.estructuraTabla = estructuraTabla;
	}

	public List<ObjetivoEtapaIndicador> getObjetivos() {
		return objetivos;
	}

	public void setObjetivos(List<ObjetivoEtapaIndicador> objetivos) {
		this.objetivos = objetivos;
	}

	public String getFirma() {
		return firma;
	}

	public void setFirma(String firma) {
		this.firma = firma;
	}

	public String getConclusiones() {
		return conclusiones;
	}

	public void setConclusiones(String conclusiones) {
		this.conclusiones = conclusiones;
	}

	public Date getFechaGeneracionTodo() {
		return fechaGeneracionTodo;
	}

	public void setFechaGeneracionTodo(Date fechaGeneracionTodo) {
		this.fechaGeneracionTodo = fechaGeneracionTodo;
	}

}
