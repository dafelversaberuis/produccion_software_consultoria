package consultoria.generales;

import java.io.Serializable;
import java.util.List;

public class Estadistica implements Serializable {

	/**
	 * 
	 */
	private static final long					serialVersionUID	= -107215895334962285L;
	private String										numeral;
	private Integer										fortaleza;
	private Integer										recomendacion;
	private Integer										noConformidad;

	private Integer										cumple;
	private Integer										noAplica;

	private Integer										tDiagnostico;

	private List<GraficoEstadistica>	valoresGrafico;
	private List<GraficoEstadistica>	valoresGrafico2;

	private Integer										d1;
	private Integer										d2;
	private Integer										d3;
	private Integer										d4;

	private Integer										s1;
	private Integer										s2;
	private Integer										s3;
	private Integer										s4;

	private Integer										i1;
	private Integer										i2;
	private Integer										i3;
	private Integer										i4;

	private Integer										a1;
	private Integer										a2;
	private Integer										a3;
	private Integer										a4;

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

	public Integer gettDiagnostico() {
		return tDiagnostico;
	}

	public void settDiagnostico(Integer tDiagnostico) {
		this.tDiagnostico = tDiagnostico;
	}

	public Integer getD1() {
		return d1;
	}

	public void setD1(Integer d1) {
		this.d1 = d1;
	}

	public Integer getD2() {
		return d2;
	}

	public void setD2(Integer d2) {
		this.d2 = d2;
	}

	public Integer getD3() {
		return d3;
	}

	public void setD3(Integer d3) {
		this.d3 = d3;
	}

	public Integer getD4() {
		return d4;
	}

	public void setD4(Integer d4) {
		this.d4 = d4;
	}

	public Integer getS1() {
		return s1;
	}

	public void setS1(Integer s1) {
		this.s1 = s1;
	}

	public Integer getS2() {
		return s2;
	}

	public void setS2(Integer s2) {
		this.s2 = s2;
	}

	public Integer getS3() {
		return s3;
	}

	public void setS3(Integer s3) {
		this.s3 = s3;
	}

	public Integer getS4() {
		return s4;
	}

	public void setS4(Integer s4) {
		this.s4 = s4;
	}

	public Integer getI1() {
		return i1;
	}

	public void setI1(Integer i1) {
		this.i1 = i1;
	}

	public Integer getI2() {
		return i2;
	}

	public void setI2(Integer i2) {
		this.i2 = i2;
	}

	public Integer getI3() {
		return i3;
	}

	public void setI3(Integer i3) {
		this.i3 = i3;
	}

	public Integer getI4() {
		return i4;
	}

	public void setI4(Integer i4) {
		this.i4 = i4;
	}

	public Integer getA1() {
		return a1;
	}

	public void setA1(Integer a1) {
		this.a1 = a1;
	}

	public Integer getA2() {
		return a2;
	}

	public void setA2(Integer a2) {
		this.a2 = a2;
	}

	public Integer getA3() {
		return a3;
	}

	public void setA3(Integer a3) {
		this.a3 = a3;
	}

	public Integer getA4() {
		return a4;
	}

	public void setA4(Integer a4) {
		this.a4 = a4;
	}

	public Integer getCumple() {
		return cumple;
	}

	public void setCumple(Integer cumple) {
		this.cumple = cumple;
	}

	public Integer getNoAplica() {
		return noAplica;
	}

	public void setNoAplica(Integer noAplica) {
		this.noAplica = noAplica;
	}

	public List<GraficoEstadistica> getValoresGrafico2() {
		return valoresGrafico2;
	}

	public void setValoresGrafico2(List<GraficoEstadistica> valoresGrafico2) {
		this.valoresGrafico2 = valoresGrafico2;
	}
	
	

}
