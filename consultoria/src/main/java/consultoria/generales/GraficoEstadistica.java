package consultoria.generales;

import java.io.Serializable;

public class GraficoEstadistica implements Serializable {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -5110589877354946523L;
	private String						hallazgo;
	private Integer						valor;

	public GraficoEstadistica() {

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

}
