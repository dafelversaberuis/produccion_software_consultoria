package consultoria.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Size;

import consultoria.generales.IConstantes;

public class Indicador implements Serializable {

	/**
	 * 
	 */
	private static final long					serialVersionUID	= 9189418394981370674L;
	private Integer										id;

	private ObjetivoEtapaIndicador		objetivo;

	private String										nombreIndicador;
	private String										formula;
	private String										responsableDecision;
	private String										responsableCalculo;
	private String										sistemaInformacion;
	private Integer										frecuencia;
	private BigDecimal								metaSuperior;
	private BigDecimal								metaIntermedia;
	private BigDecimal								metaInferior;

	private BigDecimal								periodo1;
	private BigDecimal								periodo2;
	private BigDecimal								periodo3;
	private BigDecimal								periodo4;
	private BigDecimal								periodo5;
	private BigDecimal								periodo6;
	private BigDecimal								periodo7;
	private BigDecimal								periodo8;
	private BigDecimal								periodo9;
	private BigDecimal								periodo10;
	private BigDecimal								periodo11;
	private BigDecimal								periodo12;

	private BigDecimal								numerador1;
	private BigDecimal								numerador2;
	private BigDecimal								numerador3;
	private BigDecimal								numerador4;
	private BigDecimal								numerador5;
	private BigDecimal								numerador6;
	private BigDecimal								numerador7;
	private BigDecimal								numerador8;
	private BigDecimal								numerador9;
	private BigDecimal								numerador10;
	private BigDecimal								numerador11;
	private BigDecimal								numerador12;

	private BigDecimal								denominador1;
	private BigDecimal								denominador2;
	private BigDecimal								denominador3;
	private BigDecimal								denominador4;
	private BigDecimal								denominador5;
	private BigDecimal								denominador6;
	private BigDecimal								denominador7;
	private BigDecimal								denominador8;
	private BigDecimal								denominador9;
	private BigDecimal								denominador10;
	private BigDecimal								denominador11;
	private BigDecimal								denominador12;

	private String										tTextoFrecuencia;

	private String										tNombrePeriodo;
	private String										tNombrePeriodo2;
	private String										tNombrePeriodo3;
	private String										tNombrePeriodo4;
	private String										tNombrePeriodo5;
	private String										tNombrePeriodo6;
	private String										tNombrePeriodo7;
	private String										tNombrePeriodo8;
	private String										tNombrePeriodo9;
	private String										tNombrePeriodo10;
	private String										tNombrePeriodo11;
	private String										tNombrePeriodo12;

	private BigDecimal								tNumerador;
	private BigDecimal								tDenominador;
	private BigDecimal								tResultado1;
	private BigDecimal								tResultado2;
	private String										tColorTexto;

	private List<Indicador>						tResultadosPeriodos;
	private BigDecimal								tSumatoriasResultados;
	private String										observacionesResultados;

	private EstructuraTabla						estructuraTabla;

	private List<PlanAccionIndicador>	planesAccionIndicador;

	private List<PlanAccionIndicador>	tPlanesAccion;

	public Indicador() {
		this.estructuraTabla = new EstructuraTabla();
		this.objetivo = new ObjetivoEtapaIndicador();
	}

	public void getCamposBD() {

		this.estructuraTabla.setTabla("indicadores_objetivo");
		this.estructuraTabla.getLlavePrimaria().put("id", this.id);

		this.estructuraTabla.getPersistencia().put("id_objetivo", this.objetivo.getId());
		this.estructuraTabla.getPersistencia().put("nombre_indicador", this.nombreIndicador);
		this.estructuraTabla.getPersistencia().put("formula", this.formula);
		this.estructuraTabla.getPersistencia().put("responsable_decision", this.responsableDecision);
		this.estructuraTabla.getPersistencia().put("responsable_calculo", this.responsableCalculo);
		this.estructuraTabla.getPersistencia().put("sistema_informacion", this.sistemaInformacion);
		this.estructuraTabla.getPersistencia().put("frecuencia", this.frecuencia);
		this.estructuraTabla.getPersistencia().put("meta_superior", this.metaSuperior);
		this.estructuraTabla.getPersistencia().put("meta_intermedia", this.metaIntermedia);
		this.estructuraTabla.getPersistencia().put("meta_inferior", this.metaInferior);

		this.estructuraTabla.getPersistencia().put("periodo1", this.periodo1);
		this.estructuraTabla.getPersistencia().put("periodo2", this.periodo2);
		this.estructuraTabla.getPersistencia().put("periodo3", this.periodo3);
		this.estructuraTabla.getPersistencia().put("periodo4", this.periodo4);
		this.estructuraTabla.getPersistencia().put("periodo5", this.periodo5);
		this.estructuraTabla.getPersistencia().put("periodo6", this.periodo6);
		this.estructuraTabla.getPersistencia().put("periodo7", this.periodo7);
		this.estructuraTabla.getPersistencia().put("periodo8", this.periodo8);
		this.estructuraTabla.getPersistencia().put("periodo9", this.periodo9);
		this.estructuraTabla.getPersistencia().put("periodo10", this.periodo10);
		this.estructuraTabla.getPersistencia().put("periodo11", this.periodo11);
		this.estructuraTabla.getPersistencia().put("periodo12", this.periodo12);

		this.estructuraTabla.getPersistencia().put("numerador1", this.numerador1);
		this.estructuraTabla.getPersistencia().put("numerador2", this.numerador2);
		this.estructuraTabla.getPersistencia().put("numerador3", this.numerador3);
		this.estructuraTabla.getPersistencia().put("numerador4", this.numerador4);
		this.estructuraTabla.getPersistencia().put("numerador5", this.numerador5);
		this.estructuraTabla.getPersistencia().put("numerador6", this.numerador6);
		this.estructuraTabla.getPersistencia().put("numerador7", this.numerador7);
		this.estructuraTabla.getPersistencia().put("numerador8", this.numerador8);
		this.estructuraTabla.getPersistencia().put("numerador9", this.numerador9);
		this.estructuraTabla.getPersistencia().put("numerador10", this.numerador10);
		this.estructuraTabla.getPersistencia().put("numerador11", this.numerador11);
		this.estructuraTabla.getPersistencia().put("numerador12", this.numerador12);

		this.estructuraTabla.getPersistencia().put("denominador1", this.denominador1);
		this.estructuraTabla.getPersistencia().put("denominador2", this.denominador2);
		this.estructuraTabla.getPersistencia().put("denominador3", this.denominador3);
		this.estructuraTabla.getPersistencia().put("denominador4", this.denominador4);
		this.estructuraTabla.getPersistencia().put("denominador5", this.denominador5);
		this.estructuraTabla.getPersistencia().put("denominador6", this.denominador6);
		this.estructuraTabla.getPersistencia().put("denominador7", this.denominador7);
		this.estructuraTabla.getPersistencia().put("denominador8", this.denominador8);
		this.estructuraTabla.getPersistencia().put("denominador9", this.denominador9);
		this.estructuraTabla.getPersistencia().put("denominador10", this.denominador10);
		this.estructuraTabla.getPersistencia().put("denominador11", this.denominador11);
		this.estructuraTabla.getPersistencia().put("denominador12", this.denominador12);

		this.estructuraTabla.getPersistencia().put("observaciones_resultados", this.observacionesResultados);

	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public ObjetivoEtapaIndicador getObjetivo() {
		return objetivo;
	}

	public void setObjetivo(ObjetivoEtapaIndicador objetivo) {
		this.objetivo = objetivo;
	}

	public String getNombreIndicador() {
		return nombreIndicador;
	}

	public void setNombreIndicador(String nombreIndicador) {
		this.nombreIndicador = nombreIndicador;
	}

	public String getFormula() {
		return formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}

	@Size(max = 250, message = IConstantes.VALIDACION_MAXIMA_LONGITUD)
	public String getResponsableDecision() {
		return responsableDecision;
	}

	public void setResponsableDecision(String responsableDecision) {
		this.responsableDecision = responsableDecision;
	}

	@Size(max = 250, message = IConstantes.VALIDACION_MAXIMA_LONGITUD)
	public String getResponsableCalculo() {
		return responsableCalculo;
	}

	public void setResponsableCalculo(String responsableCalculo) {
		this.responsableCalculo = responsableCalculo;
	}

	@Size(max = 250, message = IConstantes.VALIDACION_MAXIMA_LONGITUD)
	public String getSistemaInformacion() {
		return sistemaInformacion;
	}

	public void setSistemaInformacion(String sistemaInformacion) {
		this.sistemaInformacion = sistemaInformacion;
	}

	public Integer getFrecuencia() {
		return frecuencia;
	}

	public void setFrecuencia(Integer frecuencia) {
		this.frecuencia = frecuencia;
	}

	@Digits(integer = 4, fraction = 2, message = IConstantes.VALIDACION_MAXIMO_DECIMAL)
	@DecimalMin(value = "0.00", message = IConstantes.VALIDACION_MINIMO_ENTERO)
	public BigDecimal getMetaSuperior() {
		return metaSuperior;
	}

	public void setMetaSuperior(BigDecimal metaSuperior) {
		this.metaSuperior = metaSuperior;
	}

	@Digits(integer = 4, fraction = 2, message = IConstantes.VALIDACION_MAXIMO_DECIMAL)
	@DecimalMin(value = "0.00", message = IConstantes.VALIDACION_MINIMO_ENTERO)
	public BigDecimal getMetaIntermedia() {
		return metaIntermedia;
	}

	public void setMetaIntermedia(BigDecimal metaIntermedia) {
		this.metaIntermedia = metaIntermedia;
	}

	@Digits(integer = 4, fraction = 2, message = IConstantes.VALIDACION_MAXIMO_DECIMAL)
	@DecimalMin(value = "0.00", message = IConstantes.VALIDACION_MINIMO_ENTERO)
	public BigDecimal getMetaInferior() {
		return metaInferior;
	}

	public void setMetaInferior(BigDecimal metaInferior) {
		this.metaInferior = metaInferior;
	}

	@Digits(integer = 4, fraction = 2, message = IConstantes.VALIDACION_MAXIMO_DECIMAL)
	@DecimalMin(value = "0.00", message = IConstantes.VALIDACION_MINIMO_ENTERO)
	public BigDecimal getPeriodo1() {
		return periodo1;
	}

	public void setPeriodo1(BigDecimal periodo1) {
		this.periodo1 = periodo1;
	}

	@Digits(integer = 4, fraction = 2, message = IConstantes.VALIDACION_MAXIMO_DECIMAL)
	@DecimalMin(value = "0.00", message = IConstantes.VALIDACION_MINIMO_ENTERO)
	public BigDecimal getPeriodo2() {
		return periodo2;
	}

	public void setPeriodo2(BigDecimal periodo2) {
		this.periodo2 = periodo2;
	}

	@Digits(integer = 4, fraction = 2, message = IConstantes.VALIDACION_MAXIMO_DECIMAL)
	@DecimalMin(value = "0.00", message = IConstantes.VALIDACION_MINIMO_ENTERO)
	public BigDecimal getPeriodo3() {
		return periodo3;
	}

	public void setPeriodo3(BigDecimal periodo3) {
		this.periodo3 = periodo3;
	}

	@Digits(integer = 4, fraction = 2, message = IConstantes.VALIDACION_MAXIMO_DECIMAL)
	@DecimalMin(value = "0.00", message = IConstantes.VALIDACION_MINIMO_ENTERO)
	public BigDecimal getPeriodo4() {
		return periodo4;
	}

	public void setPeriodo4(BigDecimal periodo4) {
		this.periodo4 = periodo4;
	}

	@Digits(integer = 4, fraction = 2, message = IConstantes.VALIDACION_MAXIMO_DECIMAL)
	@DecimalMin(value = "0.00", message = IConstantes.VALIDACION_MINIMO_ENTERO)
	public BigDecimal getPeriodo5() {
		return periodo5;
	}

	public void setPeriodo5(BigDecimal periodo5) {
		this.periodo5 = periodo5;
	}

	@Digits(integer = 4, fraction = 2, message = IConstantes.VALIDACION_MAXIMO_DECIMAL)
	@DecimalMin(value = "0.00", message = IConstantes.VALIDACION_MINIMO_ENTERO)
	public BigDecimal getPeriodo6() {
		return periodo6;
	}

	public void setPeriodo6(BigDecimal periodo6) {
		this.periodo6 = periodo6;
	}

	@Digits(integer = 4, fraction = 2, message = IConstantes.VALIDACION_MAXIMO_DECIMAL)
	@DecimalMin(value = "0.00", message = IConstantes.VALIDACION_MINIMO_ENTERO)
	public BigDecimal getPeriodo7() {
		return periodo7;
	}

	public void setPeriodo7(BigDecimal periodo7) {
		this.periodo7 = periodo7;
	}

	@Digits(integer = 4, fraction = 2, message = IConstantes.VALIDACION_MAXIMO_DECIMAL)
	@DecimalMin(value = "0.00", message = IConstantes.VALIDACION_MINIMO_ENTERO)
	public BigDecimal getPeriodo8() {
		return periodo8;
	}

	public void setPeriodo8(BigDecimal periodo8) {
		this.periodo8 = periodo8;
	}

	@Digits(integer = 4, fraction = 2, message = IConstantes.VALIDACION_MAXIMO_DECIMAL)
	@DecimalMin(value = "0.00", message = IConstantes.VALIDACION_MINIMO_ENTERO)
	public BigDecimal getPeriodo9() {
		return periodo9;
	}

	public void setPeriodo9(BigDecimal periodo9) {
		this.periodo9 = periodo9;
	}

	@Digits(integer = 4, fraction = 2, message = IConstantes.VALIDACION_MAXIMO_DECIMAL)
	@DecimalMin(value = "0.00", message = IConstantes.VALIDACION_MINIMO_ENTERO)
	public BigDecimal getPeriodo10() {
		return periodo10;
	}

	public void setPeriodo10(BigDecimal periodo10) {
		this.periodo10 = periodo10;
	}

	@Digits(integer = 4, fraction = 2, message = IConstantes.VALIDACION_MAXIMO_DECIMAL)
	@DecimalMin(value = "0.00", message = IConstantes.VALIDACION_MINIMO_ENTERO)
	public BigDecimal getPeriodo11() {
		return periodo11;
	}

	public void setPeriodo11(BigDecimal periodo11) {
		this.periodo11 = periodo11;
	}

	@Digits(integer = 4, fraction = 2, message = IConstantes.VALIDACION_MAXIMO_DECIMAL)
	@DecimalMin(value = "0.00", message = IConstantes.VALIDACION_MINIMO_ENTERO)
	public BigDecimal getPeriodo12() {
		return periodo12;
	}

	public void setPeriodo12(BigDecimal periodo12) {
		this.periodo12 = periodo12;
	}

	@Digits(integer = 11, fraction = 2, message = IConstantes.VALIDACION_MAXIMO_DECIMAL)
	public BigDecimal getNumerador1() {
		return numerador1;
	}

	public void setNumerador1(BigDecimal numerador1) {
		this.numerador1 = numerador1;
	}

	@Digits(integer = 11, fraction = 2, message = IConstantes.VALIDACION_MAXIMO_DECIMAL)
	public BigDecimal getNumerador2() {
		return numerador2;
	}

	public void setNumerador2(BigDecimal numerador2) {
		this.numerador2 = numerador2;
	}

	@Digits(integer = 11, fraction = 2, message = IConstantes.VALIDACION_MAXIMO_DECIMAL)
	public BigDecimal getNumerador3() {
		return numerador3;
	}

	public void setNumerador3(BigDecimal numerador3) {
		this.numerador3 = numerador3;
	}

	@Digits(integer = 11, fraction = 2, message = IConstantes.VALIDACION_MAXIMO_DECIMAL)
	public BigDecimal getNumerador4() {
		return numerador4;
	}

	public void setNumerador4(BigDecimal numerador4) {
		this.numerador4 = numerador4;
	}

	@Digits(integer = 11, fraction = 2, message = IConstantes.VALIDACION_MAXIMO_DECIMAL)
	public BigDecimal getNumerador5() {
		return numerador5;
	}

	public void setNumerador5(BigDecimal numerador5) {
		this.numerador5 = numerador5;
	}

	@Digits(integer = 11, fraction = 2, message = IConstantes.VALIDACION_MAXIMO_DECIMAL)
	public BigDecimal getNumerador6() {
		return numerador6;
	}

	public void setNumerador6(BigDecimal numerador6) {
		this.numerador6 = numerador6;
	}

	@Digits(integer = 11, fraction = 2, message = IConstantes.VALIDACION_MAXIMO_DECIMAL)
	public BigDecimal getNumerador7() {
		return numerador7;
	}

	public void setNumerador7(BigDecimal numerador7) {
		this.numerador7 = numerador7;
	}

	@Digits(integer = 11, fraction = 2, message = IConstantes.VALIDACION_MAXIMO_DECIMAL)
	public BigDecimal getNumerador8() {
		return numerador8;
	}

	public void setNumerador8(BigDecimal numerador8) {
		this.numerador8 = numerador8;
	}

	@Digits(integer = 11, fraction = 2, message = IConstantes.VALIDACION_MAXIMO_DECIMAL)
	public BigDecimal getNumerador9() {
		return numerador9;
	}

	public void setNumerador9(BigDecimal numerador9) {
		this.numerador9 = numerador9;
	}

	@Digits(integer = 11, fraction = 2, message = IConstantes.VALIDACION_MAXIMO_DECIMAL)
	public BigDecimal getNumerador10() {
		return numerador10;
	}

	public void setNumerador10(BigDecimal numerador10) {
		this.numerador10 = numerador10;
	}

	@Digits(integer = 11, fraction = 2, message = IConstantes.VALIDACION_MAXIMO_DECIMAL)
	public BigDecimal getNumerador11() {
		return numerador11;
	}

	public void setNumerador11(BigDecimal numerador11) {
		this.numerador11 = numerador11;
	}

	@Digits(integer = 11, fraction = 2, message = IConstantes.VALIDACION_MAXIMO_DECIMAL)
	public BigDecimal getNumerador12() {
		return numerador12;
	}

	public void setNumerador12(BigDecimal numerador12) {
		this.numerador12 = numerador12;
	}

	@Digits(integer = 11, fraction = 2, message = IConstantes.VALIDACION_MAXIMO_DECIMAL)
	public BigDecimal getDenominador1() {
		return denominador1;
	}

	public void setDenominador1(BigDecimal denominador1) {
		this.denominador1 = denominador1;
	}

	@Digits(integer = 11, fraction = 2, message = IConstantes.VALIDACION_MAXIMO_DECIMAL)
	public BigDecimal getDenominador2() {
		return denominador2;
	}

	public void setDenominador2(BigDecimal denominador2) {
		this.denominador2 = denominador2;
	}

	@Digits(integer = 11, fraction = 2, message = IConstantes.VALIDACION_MAXIMO_DECIMAL)
	public BigDecimal getDenominador3() {
		return denominador3;
	}

	public void setDenominador3(BigDecimal denominador3) {
		this.denominador3 = denominador3;
	}

	@Digits(integer = 11, fraction = 2, message = IConstantes.VALIDACION_MAXIMO_DECIMAL)
	public BigDecimal getDenominador4() {
		return denominador4;
	}

	public void setDenominador4(BigDecimal denominador4) {
		this.denominador4 = denominador4;
	}

	@Digits(integer = 11, fraction = 2, message = IConstantes.VALIDACION_MAXIMO_DECIMAL)
	public BigDecimal getDenominador5() {
		return denominador5;
	}

	public void setDenominador5(BigDecimal denominador5) {
		this.denominador5 = denominador5;
	}

	@Digits(integer = 11, fraction = 2, message = IConstantes.VALIDACION_MAXIMO_DECIMAL)
	public BigDecimal getDenominador6() {
		return denominador6;
	}

	public void setDenominador6(BigDecimal denominador6) {
		this.denominador6 = denominador6;
	}

	@Digits(integer = 11, fraction = 2, message = IConstantes.VALIDACION_MAXIMO_DECIMAL)
	public BigDecimal getDenominador7() {
		return denominador7;
	}

	public void setDenominador7(BigDecimal denominador7) {
		this.denominador7 = denominador7;
	}

	@Digits(integer = 11, fraction = 2, message = IConstantes.VALIDACION_MAXIMO_DECIMAL)
	public BigDecimal getDenominador8() {
		return denominador8;
	}

	public void setDenominador8(BigDecimal denominador8) {
		this.denominador8 = denominador8;
	}

	@Digits(integer = 11, fraction = 2, message = IConstantes.VALIDACION_MAXIMO_DECIMAL)
	public BigDecimal getDenominador9() {
		return denominador9;
	}

	public void setDenominador9(BigDecimal denominador9) {
		this.denominador9 = denominador9;
	}

	@Digits(integer = 11, fraction = 2, message = IConstantes.VALIDACION_MAXIMO_DECIMAL)
	public BigDecimal getDenominador10() {
		return denominador10;
	}

	public void setDenominador10(BigDecimal denominador10) {
		this.denominador10 = denominador10;
	}

	@Digits(integer = 11, fraction = 2, message = IConstantes.VALIDACION_MAXIMO_DECIMAL)
	public BigDecimal getDenominador11() {
		return denominador11;
	}

	public void setDenominador11(BigDecimal denominador11) {
		this.denominador11 = denominador11;
	}

	@Digits(integer = 11, fraction = 2, message = IConstantes.VALIDACION_MAXIMO_DECIMAL)
	public BigDecimal getDenominador12() {
		return denominador12;
	}

	public void setDenominador12(BigDecimal denominador12) {
		this.denominador12 = denominador12;
	}

	public String getObservacionesResultados() {
		return observacionesResultados;
	}

	public void setObservacionesResultados(String observacionesResultados) {
		this.observacionesResultados = observacionesResultados;
	}

	public EstructuraTabla getEstructuraTabla() {
		return estructuraTabla;
	}

	public void setEstructuraTabla(EstructuraTabla estructuraTabla) {
		this.estructuraTabla = estructuraTabla;
	}

	public List<PlanAccionIndicador> getPlanesAccionIndicador() {
		return planesAccionIndicador;
	}

	public void setPlanesAccionIndicador(List<PlanAccionIndicador> planesAccionIndicador) {
		this.planesAccionIndicador = planesAccionIndicador;
	}

	public BigDecimal gettNumerador() {
		return tNumerador;
	}

	public void settNumerador(BigDecimal tNumerador) {
		this.tNumerador = tNumerador;
	}

	public BigDecimal gettDenominador() {
		return tDenominador;
	}

	public void settDenominador(BigDecimal tDenominador) {
		this.tDenominador = tDenominador;
	}

	public BigDecimal gettResultado1() {
		return tResultado1;
	}

	public void settResultado1(BigDecimal tResultado1) {
		this.tResultado1 = tResultado1;
	}

	public BigDecimal gettResultado2() {
		return tResultado2;
	}

	public void settResultado2(BigDecimal tResultado2) {
		this.tResultado2 = tResultado2;
	}

	public BigDecimal gettSumatoriasResultados() {
		return tSumatoriasResultados;
	}

	public void settSumatoriasResultados(BigDecimal tSumatoriasResultados) {
		this.tSumatoriasResultados = tSumatoriasResultados;
	}

	public List<Indicador> gettResultadosPeriodos() {
		return tResultadosPeriodos;
	}

	public void settResultadosPeriodos(List<Indicador> tResultadosPeriodos) {
		this.tResultadosPeriodos = tResultadosPeriodos;
	}

	public String gettNombrePeriodo() {
		return tNombrePeriodo;
	}

	public void settNombrePeriodo(String tNombrePeriodo) {
		this.tNombrePeriodo = tNombrePeriodo;
	}

	public String gettColorTexto() {
		return tColorTexto;
	}

	public void settColorTexto(String tColorTexto) {
		this.tColorTexto = tColorTexto;
	}

	public String gettTextoFrecuencia() {
		return tTextoFrecuencia;
	}

	public void settTextoFrecuencia(String tTextoFrecuencia) {
		this.tTextoFrecuencia = tTextoFrecuencia;
	}

	public List<PlanAccionIndicador> gettPlanesAccion() {
		return tPlanesAccion;
	}

	public void settPlanesAccion(List<PlanAccionIndicador> tPlanesAccion) {
		this.tPlanesAccion = tPlanesAccion;
	}

	public String gettNombrePeriodo2() {
		return tNombrePeriodo2;
	}

	public void settNombrePeriodo2(String tNombrePeriodo2) {
		this.tNombrePeriodo2 = tNombrePeriodo2;
	}

	public String gettNombrePeriodo3() {
		return tNombrePeriodo3;
	}

	public void settNombrePeriodo3(String tNombrePeriodo3) {
		this.tNombrePeriodo3 = tNombrePeriodo3;
	}

	public String gettNombrePeriodo4() {
		return tNombrePeriodo4;
	}

	public void settNombrePeriodo4(String tNombrePeriodo4) {
		this.tNombrePeriodo4 = tNombrePeriodo4;
	}

	public String gettNombrePeriodo5() {
		return tNombrePeriodo5;
	}

	public void settNombrePeriodo5(String tNombrePeriodo5) {
		this.tNombrePeriodo5 = tNombrePeriodo5;
	}

	public String gettNombrePeriodo6() {
		return tNombrePeriodo6;
	}

	public void settNombrePeriodo6(String tNombrePeriodo6) {
		this.tNombrePeriodo6 = tNombrePeriodo6;
	}

	public String gettNombrePeriodo7() {
		return tNombrePeriodo7;
	}

	public void settNombrePeriodo7(String tNombrePeriodo7) {
		this.tNombrePeriodo7 = tNombrePeriodo7;
	}

	public String gettNombrePeriodo8() {
		return tNombrePeriodo8;
	}

	public void settNombrePeriodo8(String tNombrePeriodo8) {
		this.tNombrePeriodo8 = tNombrePeriodo8;
	}

	public String gettNombrePeriodo9() {
		return tNombrePeriodo9;
	}

	public void settNombrePeriodo9(String tNombrePeriodo9) {
		this.tNombrePeriodo9 = tNombrePeriodo9;
	}

	public String gettNombrePeriodo10() {
		return tNombrePeriodo10;
	}

	public void settNombrePeriodo10(String tNombrePeriodo10) {
		this.tNombrePeriodo10 = tNombrePeriodo10;
	}

	public String gettNombrePeriodo11() {
		return tNombrePeriodo11;
	}

	public void settNombrePeriodo11(String tNombrePeriodo11) {
		this.tNombrePeriodo11 = tNombrePeriodo11;
	}

	public String gettNombrePeriodo12() {
		return tNombrePeriodo12;
	}

	public void settNombrePeriodo12(String tNombrePeriodo12) {
		this.tNombrePeriodo12 = tNombrePeriodo12;
	}

}
