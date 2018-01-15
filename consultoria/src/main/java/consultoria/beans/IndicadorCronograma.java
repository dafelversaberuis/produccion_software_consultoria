package consultoria.beans;

import java.io.Serializable;

public class IndicadorCronograma implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 5940681069142650616L;
	private Cronograma cronograma;
	private Indicador indicador;
	private EstructuraTabla				estructuraTabla;

	
	public IndicadorCronograma() {
		this.cronograma = new Cronograma();
		this.indicador = new Indicador();
		this.estructuraTabla = new EstructuraTabla();
	}

	public void getCamposBD() {

		this.estructuraTabla.setTabla("cronograma");
		this.estructuraTabla.getLlavePrimaria().put("id_cronograma", this.cronograma.getId());
		this.estructuraTabla.getLlavePrimaria().put("id_indicador", this.indicador.getId());

		this.estructuraTabla.getPersistencia().put("id_cronograma", this.cronograma.getId());
		this.estructuraTabla.getPersistencia().put("id_indicador", this.indicador.getId());
		

	}

	public Cronograma getCronograma() {
		return cronograma;
	}

	public void setCronograma(Cronograma cronograma) {
		this.cronograma = cronograma;
	}

	public Indicador getIndicador() {
		return indicador;
	}

	public void setIndicador(Indicador indicador) {
		this.indicador = indicador;
	}

	public EstructuraTabla getEstructuraTabla() {
		return estructuraTabla;
	}

	public void setEstructuraTabla(EstructuraTabla estructuraTabla) {
		this.estructuraTabla = estructuraTabla;
	}

	
	
	
	

}
