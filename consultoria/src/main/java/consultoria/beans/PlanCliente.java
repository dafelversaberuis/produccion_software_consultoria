package consultoria.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;

import consultoria.generales.IConstantes;

public class PlanCliente implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7541574549119559648L;
	private Integer					id;
	private Plan						plan;
	private Cliente					cliente;
	private BigDecimal			precioVentaPesos;
	private BigDecimal			ivaPesos;
	private BigDecimal			precioVentaPesosConIva;
	private Integer					minutosComprados;
	private Integer					minutosGastados;
	private Integer					minutosConCosto;
	private Integer					minutosSinCosto;

	private Date						fechaCompra;
	private Date						fechaVencimiento;

	private EstructuraTabla	estructuraTabla;

	// temporal

	private BigDecimal			TPrecioTotalPesosSinIva;
	private BigDecimal			TTotalIvaPesos;
	private BigDecimal			TPrecioTotalPesosConIva;

	public PlanCliente() {
		this.estructuraTabla = new EstructuraTabla();
		this.plan = new Plan();

	}

	public void getCamposBD() {

		this.estructuraTabla.setTabla("planes_cliente");
		this.estructuraTabla.getLlavePrimaria().put("id", this.id);
		this.estructuraTabla.getPersistencia().put("valor_iva_cop", this.ivaPesos);
		this.estructuraTabla.getPersistencia().put("precio_cop_sin_iva", this.precioVentaPesos);
		this.estructuraTabla.getPersistencia().put("precio_cop_con_iva", this.precioVentaPesosConIva);

		this.estructuraTabla.getPersistencia().put("minutos_comprados", this.minutosComprados);
		this.estructuraTabla.getPersistencia().put("minutos_gastados", this.minutosGastados);
		this.estructuraTabla.getPersistencia().put("minutos_con_costo", this.minutosConCosto);
		this.estructuraTabla.getPersistencia().put("minutos_sin_costo", this.minutosSinCosto);

		this.estructuraTabla.getPersistencia().put("fecha_compra", this.fechaCompra);
		this.estructuraTabla.getPersistencia().put("fecha_vencimiento", this.fechaVencimiento);

		if (this.plan != null && this.plan.getId() != null) {
			this.estructuraTabla.getPersistencia().put("id_plan", this.plan.getId());
		} else {
			this.estructuraTabla.getPersistencia().put("id_plan", null);
		}
		
		if (this.cliente != null && this.cliente.getId() != null) {
			this.estructuraTabla.getPersistencia().put("id_cliente", this.cliente.getId());
		} else {
			this.estructuraTabla.getPersistencia().put("id_cliente", null);
		}

	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Plan getPlan() {
		return plan;
	}

	public void setPlan(Plan plan) {
		this.plan = plan;
	}

	@Digits(integer = 11, fraction = 2, message = IConstantes.VALIDACION_MAXIMO_DECIMAL)
	@DecimalMin("0.00")
	public BigDecimal getPrecioVentaPesos() {
		return precioVentaPesos;
	}

	public void setPrecioVentaPesos(BigDecimal precioVentaPesos) {
		this.precioVentaPesos = precioVentaPesos;
	}

	@Digits(integer = 11, fraction = 2, message = IConstantes.VALIDACION_MAXIMO_DECIMAL)
	@DecimalMin("0.00")
	public BigDecimal getIvaPesos() {
		return ivaPesos;
	}

	public void setIvaPesos(BigDecimal ivaPesos) {
		this.ivaPesos = ivaPesos;
	}

	@Digits(integer = 11, fraction = 2, message = IConstantes.VALIDACION_MAXIMO_DECIMAL)
	@DecimalMin("0.00")
	public BigDecimal getPrecioVentaPesosConIva() {
		return precioVentaPesosConIva;
	}

	public void setPrecioVentaPesosConIva(BigDecimal precioVentaPesosConIva) {
		this.precioVentaPesosConIva = precioVentaPesosConIva;
	}

	@Min(value = 1, message = IConstantes.VALIDACION_MINIMO_ENTERO)
	public Integer getMinutosComprados() {
		return minutosComprados;
	}

	public void setMinutosComprados(Integer minutosComprados) {
		this.minutosComprados = minutosComprados;
	}

	@Min(value = 0, message = IConstantes.VALIDACION_MINIMO_ENTERO)
	public Integer getMinutosGastados() {
		return minutosGastados;
	}

	public void setMinutosGastados(Integer minutosGastados) {
		this.minutosGastados = minutosGastados;
	}

	@Min(value = 0, message = IConstantes.VALIDACION_MINIMO_ENTERO)
	public Integer getMinutosConCosto() {
		return minutosConCosto;
	}

	public void setMinutosConCosto(Integer minutosConCosto) {
		this.minutosConCosto = minutosConCosto;
	}

	@Min(value = 0, message = IConstantes.VALIDACION_MINIMO_ENTERO)
	public Integer getMinutosSinCosto() {
		return minutosSinCosto;
	}

	public void setMinutosSinCosto(Integer minutosSinCosto) {
		this.minutosSinCosto = minutosSinCosto;
	}

	public Date getFechaCompra() {
		return fechaCompra;
	}

	public void setFechaCompra(Date fechaCompra) {
		this.fechaCompra = fechaCompra;
	}

	public Date getFechaVencimiento() {
		return fechaVencimiento;
	}

	public void setFechaVencimiento(Date fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
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

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	
	

}
