package consultoria.beans;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.primefaces.model.UploadedFile;

import consultoria.generales.IConstantes;

public class Consultor implements Serializable {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 893779259347336898L;
	private Integer						id;
	private String						nombres;
	private String						apellidos;
	private String						correoElectronico;
	private String						estadoVigencia;
	private Date							fechaInicio;
	private Date							fechaFin;
	private String						clave;
	private String						perfil;
	private String						telefono;

	private String						tTipoClave;
	private String						tTipoEstado;

	private String						tFotoDecodificada;

	private byte[]						archivo;
	private UploadedFile			tFile;

	private EstructuraTabla		estructuraTabla;

	public Consultor() {
		this.estructuraTabla = new EstructuraTabla();

	}

	public void getCamposBD() {

		this.estructuraTabla.setTabla("consultores");
		this.estructuraTabla.getLlavePrimaria().put("id", this.id);

		this.estructuraTabla.getPersistencia().put("nombres", this.nombres);
		this.estructuraTabla.getPersistencia().put("apellidos", this.apellidos);
		this.estructuraTabla.getPersistencia().put("correo_electronico", this.correoElectronico);
		this.estructuraTabla.getPersistencia().put("estado_vigencia", this.estadoVigencia);
		this.estructuraTabla.getPersistencia().put("clave", this.clave);
		this.estructuraTabla.getPersistencia().put("perfil", this.perfil);
		this.estructuraTabla.getPersistencia().put("fecha_inicio", this.fechaInicio);
		this.estructuraTabla.getPersistencia().put("fecha_fin", this.fechaFin);
		this.estructuraTabla.getPersistencia().put("telefono", this.telefono);
		this.estructuraTabla.getPersistencia().put("archivo", this.archivo);

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

	public String getPerfil() {
		return perfil;
	}

	public void setPerfil(String perfil) {
		this.perfil = perfil;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String gettTipoEstado() {
		return tTipoEstado;
	}

	public void settTipoEstado(String tTipoEstado) {
		this.tTipoEstado = tTipoEstado;
	}

	public byte[] getArchivo() {
		return archivo;
	}

	public void setArchivo(byte[] archivo) {
		this.archivo = archivo;
	}

	public String gettFotoDecodificada() {
		return tFotoDecodificada;
	}

	public void settFotoDecodificada(String tFotoDecodificada) {
		this.tFotoDecodificada = tFotoDecodificada;
	}

	public UploadedFile gettFile() {
		return tFile;
	}

	public void settFile(UploadedFile tFile) {
		this.tFile = tFile;
	}

}
