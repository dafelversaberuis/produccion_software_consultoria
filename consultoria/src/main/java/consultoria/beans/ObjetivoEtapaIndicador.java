package consultoria.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class ObjetivoEtapaIndicador implements Serializable {

	/**
	 * 
	 */
	private static final long					serialVersionUID	= 4751213227189472167L;
	private Integer										id;
	private InformacionEtapaIndicador	informacionEtapaIndicador;
	private String										objetivo;
	private String										objetivoRelacionado;

	private BigDecimal								tResultadoObjetivo;

	private EstructuraTabla						estructuraTabla;

	private List<Indicador>						indicadores;

	public ObjetivoEtapaIndicador() {
		this.estructuraTabla = new EstructuraTabla();
		this.informacionEtapaIndicador = new InformacionEtapaIndicador();
	}

	public void getCamposBD() {

		this.estructuraTabla.setTabla("objetivos_etapa_indicadores");
		this.estructuraTabla.getLlavePrimaria().put("id", this.id);

		this.estructuraTabla.getPersistencia().put("id_informacion_etapa", this.informacionEtapaIndicador.getId());
		this.estructuraTabla.getPersistencia().put("objetivo", this.objetivo);
		this.estructuraTabla.getPersistencia().put("objetivos_relacionados", this.objetivoRelacionado);

	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public InformacionEtapaIndicador getInformacionEtapaIndicador() {
		return informacionEtapaIndicador;
	}

	public void setInformacionEtapaIndicador(InformacionEtapaIndicador informacionEtapaIndicador) {
		this.informacionEtapaIndicador = informacionEtapaIndicador;
	}

	public String getObjetivo() {
		return objetivo;
	}

	public void setObjetivo(String objetivo) {
		this.objetivo = objetivo;
	}

	public String getObjetivoRelacionado() {
		return objetivoRelacionado;
	}

	public void setObjetivoRelacionado(String objetivoRelacionado) {
		this.objetivoRelacionado = objetivoRelacionado;
	}

	public EstructuraTabla getEstructuraTabla() {
		return estructuraTabla;
	}

	public void setEstructuraTabla(EstructuraTabla estructuraTabla) {
		this.estructuraTabla = estructuraTabla;
	}

	public List<Indicador> getIndicadores() {
		return indicadores;
	}

	public void setIndicadores(List<Indicador> indicadores) {
		this.indicadores = indicadores;
	}

	public BigDecimal gettResultadoObjetivo() {
		return tResultadoObjetivo;
	}

	public void settResultadoObjetivo(BigDecimal tResultadoObjetivo) {
		this.tResultadoObjetivo = tResultadoObjetivo;
	}

}
