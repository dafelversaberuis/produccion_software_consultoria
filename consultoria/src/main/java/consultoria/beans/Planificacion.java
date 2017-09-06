package consultoria.beans;

import java.io.Serializable;
import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

public class Planificacion extends ListDataModel<Planificacion> implements Serializable, SelectableDataModel<Planificacion>   {

	/**
	 * 
	 */
	private static final long					serialVersionUID	= 4273917095367114144L;
	private Integer										id;
	private ProyectoCliente						proyectoCliente;
	private TareaProyecto							tareaProyecto;

	private String										observaciones;

	private EstructuraTabla						estructuraTabla;

	private List<TiempoPlanificacion>	tTiemposPlanificacion;

	public Planificacion() {
		this.proyectoCliente = new ProyectoCliente();
		this.tareaProyecto = new TareaProyecto();
		this.estructuraTabla = new EstructuraTabla();
	}

	public void getCamposBD() {

		this.estructuraTabla.setTabla("planificacion");
		this.estructuraTabla.getLlavePrimaria().put("id", this.id);

		if (this.proyectoCliente != null && this.proyectoCliente.getId() != null) {
			this.estructuraTabla.getPersistencia().put("id_proyecto_cliente", this.proyectoCliente.getId());
		} else {
			this.estructuraTabla.getPersistencia().put("id_proyecto_cliente", null);
		}
		if (this.tareaProyecto != null && this.tareaProyecto.getId() != null) {
			this.estructuraTabla.getPersistencia().put("id_tarea_proyecto", this.tareaProyecto.getId());
		} else {
			this.estructuraTabla.getPersistencia().put("id_tarea_proyecto", null);
		}

		this.estructuraTabla.getPersistencia().put("observaciones", this.observaciones);

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

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public List<TiempoPlanificacion> gettTiemposPlanificacion() {
		return tTiemposPlanificacion;
	}

	public void settTiemposPlanificacion(List<TiempoPlanificacion> tTiemposPlanificacion) {
		this.tTiemposPlanificacion = tTiemposPlanificacion;
	}

	@Override
	public Planificacion getRowData(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getRowKey(Planificacion arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
