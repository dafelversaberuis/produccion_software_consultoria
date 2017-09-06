package consultoria.beans;

import java.io.Serializable;

import javax.validation.constraints.Size;

import consultoria.generales.IConstantes;

public class Proyecto implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 5039295001305288750L;
	private Integer						id;
	private String						nombre;
	private String						indicativoVigencia;

	private EstructuraTabla		estructuraTabla;

	public Proyecto() {
		this.estructuraTabla = new EstructuraTabla();
	}

	public void getCamposBD() {

		this.estructuraTabla.setTabla("proyectos");
		this.estructuraTabla.getLlavePrimaria().put("id", this.id);
		this.estructuraTabla.getPersistencia().put("nombre", this.nombre);
		this.estructuraTabla.getPersistencia().put("indicativo_vigencia", this.indicativoVigencia);

	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Size(max = 250, message = IConstantes.VALIDACION_MAXIMA_LONGITUD)
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getIndicativoVigencia() {
		return indicativoVigencia;
	}

	public void setIndicativoVigencia(String indicativoVigencia) {
		this.indicativoVigencia = indicativoVigencia;
	}

	public EstructuraTabla getEstructuraTabla() {
		return estructuraTabla;
	}

	public void setEstructuraTabla(EstructuraTabla estructuraTabla) {
		this.estructuraTabla = estructuraTabla;
	}

}
