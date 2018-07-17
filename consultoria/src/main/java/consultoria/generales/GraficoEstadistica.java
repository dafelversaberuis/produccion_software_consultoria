package consultoria.generales;

import java.io.Serializable;

public class GraficoEstadistica implements Serializable {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -5110589877354946523L;
	private String						hallazgo;
	
	private String						hallazgoD;
	private String						hallazgoS;
	private String						hallazgoI;
	private String						hallazgoA;
	
	private Integer						valor;
	
	private Integer						valorNA;
	private Integer						valorNC;
	private Integer						valorCP;
	private Integer						valorCS;

	public GraficoEstadistica() {
		valorNA = 0;
		valorNC = 0;
		valorCP = 0;
		valorCS = 0;

	}

	public String getHallazgo() {
		return hallazgo;
	}

	public void setHallazgo(String hallazgo) {
		this.hallazgo = hallazgo;
	}

	public Integer getValor() {
		return valor;
	}

	public void setValor(Integer valor) {
		this.valor = valor;
	}

	public String getHallazgoD() {
		return hallazgoD;
	}

	public void setHallazgoD(String hallazgoD) {
		this.hallazgoD = hallazgoD;
	}

	public String getHallazgoS() {
		return hallazgoS;
	}

	public void setHallazgoS(String hallazgoS) {
		this.hallazgoS = hallazgoS;
	}

	public String getHallazgoI() {
		return hallazgoI;
	}

	public void setHallazgoI(String hallazgoI) {
		this.hallazgoI = hallazgoI;
	}

	public String getHallazgoA() {
		return hallazgoA;
	}

	public void setHallazgoA(String hallazgoA) {
		this.hallazgoA = hallazgoA;
	}

	public Integer getValorNA() {
		return valorNA;
	}

	public void setValorNA(Integer valorNA) {
		this.valorNA = valorNA;
	}

	public Integer getValorNC() {
		return valorNC;
	}

	public void setValorNC(Integer valorNC) {
		this.valorNC = valorNC;
	}

	public Integer getValorCP() {
		return valorCP;
	}

	public void setValorCP(Integer valorCP) {
		this.valorCP = valorCP;
	}

	public Integer getValorCS() {
		return valorCS;
	}

	public void setValorCS(Integer valorCS) {
		this.valorCS = valorCS;
	}
	
	
	
	
	

}
