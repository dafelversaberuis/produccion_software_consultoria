package consultoria.beans;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import consultoria.generales.IConstantes;

public class Cliente implements Serializable {

	/**
	 * 
	 */
	private static final long			serialVersionUID	= -8783745923057789313L;
	private Integer								id;
	private String								cliente;
	private String								nit;
	private String								representante;
	private String								correoElectronico;
	private String								clave;
	private String								telefono;
	private String								tTipoClave;
	private String								tCopiaNit;
	private String								tEstado;

	private List<ProyectoCliente>	tProyectosCliente;

	private EstructuraTabla				estructuraTabla;

	private Integer						TPlanClienteSeleccionado;

	public Cliente() {
		this.estructuraTabla = new EstructuraTabla();



	}

	public void getCamposBD() {

		this.estructuraTabla.setTabla("clientes");
		this.estructuraTabla.getLlavePrimaria().put("id", this.id);

		this.estructuraTabla.getPersistencia().put("cliente", this.cliente);
		this.estructuraTabla.getPersistencia().put("nit", this.nit);
		this.estructuraTabla.getPersistencia().put("representante", this.representante);
		this.estructuraTabla.getPersistencia().put("correo_electronico", this.correoElectronico);
		this.estructuraTabla.getPersistencia().put("clave", this.clave);
		this.estructuraTabla.getPersistencia().put("telefono", this.telefono);

	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Size(max = 250, message = IConstantes.VALIDACION_MAXIMA_LONGITUD)
	public String getCliente() {
		return cliente;
	}

	public void setCliente(String cliente) {
		this.cliente = cliente;
	}

	@Size(max = 100, message = IConstantes.VALIDACION_MAXIMA_LONGITUD)
	public String getNit() {
		return nit;
	}

	public void setNit(String nit) {
		this.nit = nit;
	}

	@Size(max = 250, message = IConstantes.VALIDACION_MAXIMA_LONGITUD)
	public String getRepresentante() {
		return representante;
	}

	public void setRepresentante(String representante) {
		this.representante = representante;
	}

	@Pattern(regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$", message = IConstantes.VALIDACION_EMAIL_INCORRECTO)
	@Size(max = 250, message = IConstantes.VALIDACION_MAXIMA_LONGITUD)
	public String getCorreoElectronico() {
		return correoElectronico;
	}

	public void setCorreoElectronico(String correoElectronico) {
		this.correoElectronico = correoElectronico;
	}

	@Size(max = 250, message = IConstantes.VALIDACION_MAXIMA_LONGITUD)
	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public EstructuraTabla getEstructuraTabla() {
		return estructuraTabla;
	}

	public void setEstructuraTabla(EstructuraTabla estructuraTabla) {
		this.estructuraTabla = estructuraTabla;
	}

	public String gettTipoClave() {
		return tTipoClave;
	}

	public void settTipoClave(String tTipoClave) {
		this.tTipoClave = tTipoClave;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String gettCopiaNit() {
		return tCopiaNit;
	}

	public void settCopiaNit(String tCopiaNit) {
		this.tCopiaNit = tCopiaNit;
	}

	public List<ProyectoCliente> gettProyectosCliente() {
		return tProyectosCliente;
	}

	public void settProyectosCliente(List<ProyectoCliente> tProyectosCliente) {
		this.tProyectosCliente = tProyectosCliente;
	}

	public String gettEstado() {
		return tEstado;
	}

	public void settEstado(String tEstado) {
		this.tEstado = tEstado;
	}

	public Integer getTPlanClienteSeleccionado() {
		return TPlanClienteSeleccionado;
	}

	public void setTPlanClienteSeleccionado(Integer tPlanClienteSeleccionado) {
		TPlanClienteSeleccionado = tPlanClienteSeleccionado;
	}

	

}
