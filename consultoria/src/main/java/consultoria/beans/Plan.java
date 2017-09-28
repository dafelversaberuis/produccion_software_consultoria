package consultoria.beans;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import consultoria.generales.IConstantes;

public class Plan implements Serializable {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 7828602730771867664L;
	private Integer						id;
	private String						nombre;
	private BigDecimal				precioVentaPesos;
	private BigDecimal				ivaPesos;
	private BigDecimal				precioVentaPesosConIva;
	private String						estadoVigencia;
	private Iva								iva;
	private Integer						minutos;

	private EstructuraTabla		estructuraTabla;

	// temporal

	private BigDecimal				TPrecioTotalPesosSinIva;
	private BigDecimal				TTotalIvaPesos;
	private BigDecimal				TPrecioTotalPesosConIva;

	public Plan() {
		this.estructuraTabla = new EstructuraTabla();
		this.iva = new Iva();
		// la bolsa como es temp y esta es 1-1 no lo instanciamos para que no quede
		// c�clico.

	}

	public void getCamposBD() {

		this.estructuraTabla.setTabla("planes");
		this.estructuraTabla.getLlavePrimaria().put("id", this.id);

		this.estructuraTabla.getPersistencia().put("nombre", this.nombre);

		this.estructuraTabla.getPersistencia().put("valor_iva_cop", this.ivaPesos);

		this.estructuraTabla.getPersistencia().put("precio_cop_sin_iva", this.precioVentaPesos);
		
		this.estructuraTabla.getPersistencia().put("precio_cop_con_iva", this.precioVentaPesosConIva);

		this.estructuraTabla.getPersistencia().put("estado_vigencia", this.estadoVigencia);

		this.estructuraTabla.getPersistencia().put("minutos", this.minutos);

		if (this.iva != null && this.iva.getId() != null) {
			this.estructuraTabla.getPersistencia().put("id_iva", this.iva.getId());
		} else {
			this.estructuraTabla.getPersistencia().put("id_iva", null);
		}

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

	@Digits(integer = 10, fraction = 2, message = IConstantes.VALIDACION_MAXIMO_DECIMAL)
	@DecimalMin("0.00")
	public BigDecimal getPrecioVentaPesos() {
		return precioVentaPesos;
	}

	public void setPrecioVentaPesos(BigDecimal precioVentaPesos) {
		this.precioVentaPesos = precioVentaPesos;
	}

	@Digits(integer = 11, fraction = 2, message = IConstantes.VALIDACION_MAXIMO_DECIMAL)
	@DecimalMin("0.00")
	public BigDecimal getPrecioVentaPesosConIva() {
		return precioVentaPesosConIva;
	}

	public void setPrecioVentaPesosConIva(BigDecimal precioVentaPesosConIva) {
		this.precioVentaPesosConIva = precioVentaPesosConIva;
	}

	@Digits(integer = 11, fraction = 2, message = IConstantes.VALIDACION_MAXIMO_DECIMAL)
	@DecimalMin("0.00")
	public BigDecimal getIvaPesos() {
		return ivaPesos;
	}

	public void setIvaPesos(BigDecimal ivaPesos) {
		this.ivaPesos = ivaPesos;
	}

	@Pattern(regexp = "[AI]", message = IConstantes.VALIDACION_ACTIVO_INACTIVO)
	public String getEstadoVigencia() {
		return estadoVigencia;
	}

	public void setEstadoVigencia(String estadoVigencia) {
		this.estadoVigencia = estadoVigencia;
	}

	public Iva getIva() {
		return iva;
	}

	public void setIva(Iva iva) {
		this.iva = iva;
	}

	public EstructuraTabla getEstructuraTabla() {
		return estructuraTabla;
	}

	public void setEstructuraTabla(EstructuraTabla estructuraTabla) {
		this.estructuraTabla = estructuraTabla;
	}

	public BigDecimal getTPrecioTotalPesosSinIva() {
		return TPrecioTotalPesosSinIva;
	}

	public void setTPrecioTotalPesosSinIva(BigDecimal tPrecioTotalPesosSinIva) {
		TPrecioTotalPesosSinIva = tPrecioTotalPesosSinIva;
	}

	public BigDecimal getTTotalIvaPesos() {
		return TTotalIvaPesos;
	}

	public void setTTotalIvaPesos(BigDecimal tTotalIvaPesos) {
		TTotalIvaPesos = tTotalIvaPesos;
	}

	public BigDecimal getTPrecioTotalPesosConIva() {
		return TPrecioTotalPesosConIva;
	}

	public void setTPrecioTotalPesosConIva(BigDecimal tPrecioTotalPesosConIva) {
		TPrecioTotalPesosConIva = tPrecioTotalPesosConIva;
	}

	@Min(value = 1, message = IConstantes.VALIDACION_MINIMO_ENTERO)
	public Integer getMinutos() {
		return minutos;
	}

	public void setMinutos(Integer minutos) {
		this.minutos = minutos;
	}

}
