package consultoria.beans;

import java.io.Serializable;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import consultoria.generales.IConstantes;

public class Personal implements Serializable {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 6403513485321497563L;
	private Integer						id;
	private String						nombres;

	private String						apellidos;
	private String						correoElectronico;
	private String						estadoVigencia;
	private String						clave;

	private String						tTipoClave;
	private String						tipoUsuario;
	private String						nombreCompleto;

	private EstructuraTabla		estructuraTabla;

	public Personal() {
		this.estructuraTabla = new EstructuraTabla();

	}

	public void getCamposBD() {

		this.estructuraTabla.setTabla("personal");
		this.estructuraTabla.getLlavePrimaria().put("id", this.id);

		this.estructuraTabla.getPersistencia().put("nombres", this.nombres);
		this.estructuraTabla.getPersistencia().put("apellidos", this.apellidos);
		this.estructuraTabla.getPersistencia().put("correo_electronico", this.correoElectronico);
		this.estructuraTabla.getPersistencia().put("estado_vigencia", this.estadoVigencia);
		this.estructuraTabla.getPersistencia().put("clave", this.clave);

	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Size(max = 250, message = IConstantes.VALIDACION_MAXIMA_LONGITUD)
	public String getNombres() {
		return nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	@Size(max = 250, message = IConstantes.VALIDACION_MAXIMA_LONGITUD)
	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	@Pattern(regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$", message = IConstantes.VALIDACION_EMAIL_INCORRECTO)
	@Size(max = 250, message = IConstantes.VALIDACION_MAXIMA_LONGITUD)
	public String getCorreoElectronico() {
		return correoElectronico;
	}

	public void setCorreoElectronico(String correoElectronico) {
		this.correoElectronico = correoElectronico;
	}

	@Pattern(regexp = "[AI]", message = IConstantes.VALIDACION_ACTIVO_INACTIVO)
	public String getEstadoVigencia() {
		return estadoVigencia;
	}

	public void setEstadoVigencia(String estadoVigencia) {
		this.estadoVigencia = estadoVigencia;
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

	public String getTipoUsuario() {
		return tipoUsuario;
	}

	public void setTipoUsuario(String tipoUsuario) {
		this.tipoUsuario = tipoUsuario;
	}

	public String getNombreCompleto() {
		return nombreCompleto;
	}

	public void setNombreCompleto(String nombreCompleto) {
		this.nombreCompleto = nombreCompleto;
	}

}
