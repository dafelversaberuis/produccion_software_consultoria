package consultoria.beans;

import java.io.Serializable;
import java.util.List;

public class Diagnostico implements Serializable {

	/**
	 * 
	 */
	private static final long						serialVersionUID	= -3092341487158825224L;
	private Integer											id;
	private String											evidenciaEncontrada;
	private ProyectoCliente							proyectoCliente;
	private PreguntaProyecto						preguntaProyecto;
	private Integer											tIdRegistro;
	private String											tHallazgoSeleccionado;

	private List<EstadoDiagnostico>			tEstadosDiagnostico;

	private List<DocumentoDiagnostico>	tDocumentos;
	
	
	private String tTipo;

	private EstructuraTabla							estructuraTabla;
	
	//nuevos
	
	private String analisisCausa;
	private String accionesRealizar;
	
	
	//

	public Diagnostico() {
		this.proyectoCliente = new ProyectoCliente();
		this.preguntaProyecto = new PreguntaProyecto();
		this.estructuraTabla = new EstructuraTabla();
	}

	public void getCamposBD() {

		this.estructuraTabla.setTabla("diagnostico");
		this.estructuraTabla.getLlavePrimaria().put("id", this.id);
		this.estructuraTabla.getPersistencia().put("evidencia_encontrada", this.evidenciaEncontrada);

		if (this.proyectoCliente != null && this.proyectoCliente.getId() != null) {
			this.estructuraTabla.getPersistencia().put("id_proyecto_cliente", this.proyectoCliente.getId());
		} else {
			this.estructuraTabla.getPersistencia().put("id_proyecto_cliente", null);
		}
		if (this.preguntaProyecto != null && this.preguntaProyecto.getId() != null) {
			this.estructuraTabla.getPersistencia().put("id_pregunta_proyecto", this.preguntaProyecto.getId());
		} else {
			this.estructuraTabla.getPersistencia().put("id_pregunta_proyecto", null);
		}
		
		this.estructuraTabla.getPersistencia().put("analisis_causas", this.analisisCausa);
		this.estructuraTabla.getPersistencia().put("accciones_realizar", this.accionesRealizar);

	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEvidenciaEncontrada() {
		return evidenciaEncontrada;
	}

	public void setEvidenciaEncontrada(String evidenciaEncontrada) {
		this.evidenciaEncontrada = evidenciaEncontrada;
	}

	public ProyectoCliente getProyectoCliente() {
		return proyectoCliente;
	}

	public void setProyectoCliente(ProyectoCliente proyectoCliente) {
		this.proyectoCliente = proyectoCliente;
	}

	public PreguntaProyecto getPreguntaProyecto() {
		return preguntaProyecto;
	}

	public void setPreguntaProyecto(PreguntaProyecto preguntaProyecto) {
		this.preguntaProyecto = preguntaProyecto;
	}

	public EstructuraTabla getEstructuraTabla() {
		return estructuraTabla;
	}

	public void setEstructuraTabla(EstructuraTabla estructuraTabla) {
		this.estructuraTabla = estructuraTabla;
	}

	public List<EstadoDiagnostico> gettEstadosDiagnostico() {
		return tEstadosDiagnostico;
	}

	public void settEstadosDiagnostico(List<EstadoDiagnostico> tEstadosDiagnostico) {
		this.tEstadosDiagnostico = tEstadosDiagnostico;
	}

	public Integer gettIdRegistro() {
		return tIdRegistro;
	}

	public void settIdRegistro(Integer tIdRegistro) {
		this.tIdRegistro = tIdRegistro;
	}

	public String gettHallazgoSeleccionado() {
		return tHallazgoSeleccionado;
	}

	public void settHallazgoSeleccionado(String tHallazgoSeleccionado) {
		this.tHallazgoSeleccionado = tHallazgoSeleccionado;
	}

	public List<DocumentoDiagnostico> gettDocumentos() {
		return tDocumentos;
	}

	public void settDocumentos(List<DocumentoDiagnostico> tDocumentos) {
		this.tDocumentos = tDocumentos;
	}

	public String getAnalisisCausa() {
		return analisisCausa;
	}

	public void setAnalisisCausa(String analisisCausa) {
		this.analisisCausa = analisisCausa;
	}

	public String getAccionesRealizar() {
		return accionesRealizar;
	}

	public void setAccionesRealizar(String accionesRealizar) {
		this.accionesRealizar = accionesRealizar;
	}

	public String gettTipo() {
		return tTipo;
	}

	public void settTipo(String tTipo) {
		this.tTipo = tTipo;
	}
	
	
	
	

}
