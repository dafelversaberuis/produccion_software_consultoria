package consultoria.beans;

import java.io.Serializable;
import java.util.Date;

public class PersonaDiagnostico implements Serializable {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 2537090399266561786L;
	private String						personaEntrevistada;
	private String						cargoEntrevistado;
	private String						firma;
	private String						requiereFirma;
	private String						conclusiones;
	private Date							fecha;
	private ProyectoCliente		proyectoCliente;

	private String						tAperece;
	private String						tTipoReporte;

	private EstructuraTabla		estructuraTabla;

	public PersonaDiagnostico() {
		this.proyectoCliente = new ProyectoCliente();
		this.estructuraTabla = new EstructuraTabla();
	}

	public void getCamposBD() {

		this.estructuraTabla.setTabla("persona_diagnostico");

		this.estructuraTabla.getPersistencia().put("persona_entrevistada", this.personaEntrevistada);
		this.estructuraTabla.getPersistencia().put("cargo_entrevistado", this.cargoEntrevistado);
		this.estructuraTabla.getPersistencia().put("firma", this.firma);
		this.estructuraTabla.getPersistencia().put("conclusiones", this.conclusiones);
		this.estructuraTabla.getPersistencia().put("fecha", this.fecha);
		this.estructuraTabla.getPersistencia().put("requiere_firma", this.requiereFirma);

		if (this.proyectoCliente != null && this.proyectoCliente.getId() != null) {
			this.estructuraTabla.getLlavePrimaria().put("id_proyecto_cliente", this.proyectoCliente.getId());
			this.estructuraTabla.getPersistencia().put("id_proyecto_cliente", this.proyectoCliente.getId());
		} else {
			this.estructuraTabla.getLlavePrimaria().put("id_proyecto_cliente", null);
			this.estructuraTabla.getPersistencia().put("id_proyecto_cliente", null);
		}

	}

	public String getPersonaEntrevistada() {
		return personaEntrevistada;
	}

	public void setPersonaEntrevistada(String personaEntrevistada) {
		this.personaEntrevistada = personaEntrevistada;
	}

	public String getCargoEntrevistado() {
		return cargoEntrevistado;
	}

	public void setCargoEntrevistado(String cargoEntrevistado) {
		this.cargoEntrevistado = cargoEntrevistado;
	}

	public String getFirma() {
		return firma;
	}

	public void setFirma(String firma) {
		this.firma = firma;
	}

	public ProyectoCliente getProyectoCliente() {
		return proyectoCliente;
	}

	public void setProyectoCliente(ProyectoCliente proyectoCliente) {
		this.proyectoCliente = proyectoCliente;
	}

	public EstructuraTabla getEstructuraTabla() {
		return estructuraTabla;
	}

	public void setEstructuraTabla(EstructuraTabla estructuraTabla) {
		this.estructuraTabla = estructuraTabla;
	}

	public String getConclusiones() {
		return conclusiones;
	}

	public void setConclusiones(String conclusiones) {
		this.conclusiones = conclusiones;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String gettAperece() {
		return tAperece;
	}

	public void settAperece(String tAperece) {
		this.tAperece = tAperece;
	}

	public String gettTipoReporte() {
		return tTipoReporte;
	}

	public void settTipoReporte(String tTipoReporte) {
		this.tTipoReporte = tTipoReporte;
	}

	public String getRequiereFirma() {
		return requiereFirma;
	}

	public void setRequiereFirma(String requiereFirma) {
		this.requiereFirma = requiereFirma;
	}

}
