package consultoria.generales;

import java.io.Serializable;
import java.util.List;

public class Estadistica implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -107215895334962285L;
	private String numeral;
	private Integer fortaleza;
	private Integer recomendacion;
	private Integer noConformidad;
	
	private List<GraficoEstadistica> valoresGrafico;
	
	
	
	public Estadistica() {
		
	}
	public String getNumeral() {
		return numeral;
	}
	public void setNumeral(String numeral) {
		this.numeral = numeral;
	}
	public Integer getFortaleza() {
		return fortaleza;
	}
	public void setFortaleza(Integer fortaleza) {
		this.fortaleza = fortaleza;
	}
	public Integer getRecomendacion() {
		return recomendacion;
	}
	public void setRecomendacion(Integer recomendacion) {
		this.recomendacion = recomendacion;
	}
	public Integer getNoConformidad() {
		return noConformidad;
	}
	public void setNoConformidad(Integer noConformidad) {
		this.noConformidad = noConformidad;
	}
	public List<GraficoEstadistica> getValoresGrafico() {
		return valoresGrafico;
	}
	public void setValoresGrafico(List<GraficoEstadistica> valoresGrafico) {
		this.valoresGrafico = valoresGrafico;
	}
	
	
	
	
	

}
