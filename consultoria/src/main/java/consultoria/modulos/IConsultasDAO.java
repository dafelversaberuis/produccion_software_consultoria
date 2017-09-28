package consultoria.modulos;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.primefaces.model.DefaultScheduleEvent;

import consultoria.Conexion;
import consultoria.beans.Cita;
import consultoria.beans.Cliente;
import consultoria.beans.Compromiso;
import consultoria.beans.CompromisoGeneral;
import consultoria.beans.Consultor;
import consultoria.beans.Cronograma;
import consultoria.beans.CronogramaGeneral;
import consultoria.beans.Diagnostico;
import consultoria.beans.Documentacion;
import consultoria.beans.DocumentacionGeneral;
import consultoria.beans.DocumentoActividad;
import consultoria.beans.DocumentoCronograma;
import consultoria.beans.DocumentoDiagnostico;
import consultoria.beans.Estado;
import consultoria.beans.EstadoDiagnostico;
import consultoria.beans.EstadoProyectoCliente;
import consultoria.beans.Indicador;
import consultoria.beans.InformacionEtapaIndicador;
import consultoria.beans.Iva;
import consultoria.beans.ObjetivoEtapaIndicador;
import consultoria.beans.ParametroAuditoria;
import consultoria.beans.PersonaDiagnostico;
import consultoria.beans.Personal;
import consultoria.beans.Plan;
import consultoria.beans.PlanAccionIndicador;
import consultoria.beans.PlanCliente;
import consultoria.beans.Planificacion;
import consultoria.beans.PlanificacionGeneral;
import consultoria.beans.PreguntaProyecto;
import consultoria.beans.Proyecto;
import consultoria.beans.ProyectoCliente;
import consultoria.beans.TareaProyecto;
import consultoria.beans.TiempoPlanificacion;
import consultoria.generales.IConstantes;

public interface IConsultasDAO {

	/**
	 * Obtiene planes del clente
	 * 
	 * @param aPregunta
	 * @return preguntas
	 * @throws Exception
	 */
	public static List<PlanCliente> getPlanesCliente(PlanCliente aPregunta) throws Exception {
		List<PlanCliente> preguntas = new ArrayList<PlanCliente>();
		List<Object> parametros = new ArrayList<Object>();
		PlanCliente pregunta = null;
		Conexion conexion = new Conexion();
		ResultSet rs = null;
		try {

			StringBuilder sql = new StringBuilder();
			sql.append("  SELECT p.*, pl.nombre");
			sql.append("  FROM planes_cliente p ");
			sql.append("  INNER JOIN planes pl ON p.id_plan = pl.id ");
			sql.append("  WHERE 1=1");

			if (aPregunta != null && aPregunta.getCliente() != null && aPregunta.getCliente().getId() != null) {

				sql.append("  AND p.id_cliente = ? ");
				parametros.add(aPregunta.getCliente().getId());
			}

			sql.append("  ORDER BY p.fecha_compra DESC");

			rs = conexion.consultarBD(sql.toString(), parametros);

			while (rs.next()) {

				pregunta = new PlanCliente();
				pregunta.setId((Integer) rs.getObject("id"));

				pregunta.getPlan().setId((Integer) rs.getObject("id_plan"));
				pregunta.getPlan().setNombre((String) rs.getObject("nombre"));

				pregunta.setMinutosComprados((Integer) rs.getObject("minutos_comprados"));
				pregunta.setMinutosConCosto((Integer) rs.getObject("minutos_con_costo"));
				pregunta.setMinutosSinCosto((Integer) rs.getObject("minutos_sin_costo"));
				pregunta.setMinutosGastados((Integer) rs.getObject("minutos_gastados"));

				pregunta.setPrecioVentaPesos((BigDecimal) rs.getObject("precio_cop_sin_iva"));
				pregunta.setIvaPesos((BigDecimal) rs.getObject("valor_iva_cop"));
				pregunta.setPrecioVentaPesosConIva((BigDecimal) rs.getObject("precio_cop_con_iva"));

				pregunta.setFechaCompra((Date) rs.getObject("fecha_compra"));
				pregunta.setFechaVencimiento((Date) rs.getObject("fecha_vencimiento"));

				preguntas.add(pregunta);
			}

		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			if (rs != null) {
				rs.close();
			}

			conexion.cerrarConexion();

		}
		return preguntas;

	}

	/**
	 * Obtiene los registros de iva de acuerdo a los parámetros especificados
	 * 
	 * @param aIva
	 * @return ivas
	 * @throws Exception
	 */
	public static List<Iva> getIvas(Iva aIva) throws Exception {
		List<Iva> ivas = new ArrayList<Iva>();
		List<Object> parametros = new ArrayList<Object>();
		Iva iva = null;
		Conexion conexion = new Conexion();
		ResultSet rs = null;
		try {

			StringBuilder sql = new StringBuilder();
			sql.append("  SELECT *");
			sql.append("  FROM iva ");
			sql.append("  WHERE 1=1 ");

			if (aIva != null && aIva.getId() != null) {
				sql.append(" AND id = ? ");
				parametros.add(aIva.getId());
			}

			sql.append("  ORDER BY valor_iva");

			rs = conexion.consultarBD(sql.toString(), parametros);

			while (rs.next()) {
				iva = new Iva();
				iva.setId((Integer) rs.getObject("id"));
				iva.setNombre((String) rs.getObject("nombre"));
				iva.setValorIva((BigDecimal) rs.getObject("valor_iva"));
				ivas.add(iva);
			}

		} catch (Exception e) {
			throw new Exception(e);

		} finally {

			if (rs != null) {
				rs.close();
			}
			conexion.cerrarConexion();

		}
		return ivas;

	}

	/**
	 * Obtiene los planes creados
	 * 
	 * @param aArbolito
	 * @return arbolitos
	 * @throws Exception
	 */
	public static List<Plan> getArbolitos(Plan aArbolito) throws Exception {
		List<Plan> arbolitos = new ArrayList<Plan>();
		List<Object> parametros = new ArrayList<Object>();
		Plan arbolito = null;
		Conexion conexion = new Conexion();
		ResultSet rs = null;
		try {

			StringBuilder sql = new StringBuilder();
			sql.append("  SELECT a.*, i.nombre nombre_iva, i.valor_iva");
			sql.append("  FROM planes a ");
			sql.append("  INNER JOIN iva i ON a.id_iva = i.id");
			sql.append("  WHERE 1=1");

			if (aArbolito != null && aArbolito.getId() != null) {
				sql.append("  AND a.id = ?");
				parametros.add(aArbolito.getId());
			}

			if (aArbolito != null && aArbolito.getEstadoVigencia() != null && !aArbolito.getEstadoVigencia().equals("")) {
				sql.append("  AND a.estado_vigencia = ?");
				parametros.add(aArbolito.getEstadoVigencia());
			}

			if (aArbolito != null && aArbolito.getIva() != null && aArbolito.getIva().getId() != null) {
				sql.append("  AND a.id_iva = ?");
				parametros.add(aArbolito.getIva().getId());
			}

			sql.append("  ORDER BY a.nombre");

			rs = conexion.consultarBD(sql.toString(), parametros);

			while (rs.next()) {

				arbolito = new Plan();

				// arbolitos
				arbolito.setId((Integer) rs.getObject("id"));
				arbolito.setNombre((String) rs.getObject("nombre"));
				arbolito.setEstadoVigencia((String) rs.getObject("estado_vigencia"));
				arbolito.setMinutos((Integer) rs.getObject("minutos"));
				arbolito.setPrecioVentaPesos((BigDecimal) rs.getObject("precio_cop_sin_iva"));
				arbolito.setIvaPesos((BigDecimal) rs.getObject("valor_iva_cop"));
				arbolito.setPrecioVentaPesosConIva((BigDecimal) rs.getObject("precio_cop_con_iva"));
				// iva
				arbolito.getIva().setId((Integer) rs.getObject("id_iva"));
				arbolito.getIva().setNombre((String) rs.getObject("nombre_iva"));
				arbolito.getIva().setValorIva((BigDecimal) rs.getObject("valor_iva"));

				arbolitos.add(arbolito);
			}

		} catch (Exception e) {
			throw new Exception(e);

		} finally {

			if (rs != null) {
				rs.close();
			}
			conexion.cerrarConexion();

		}
		return arbolitos;

	}

	// obtiene la agenda especificada
	public static List<Cita> getAgenda(Date fechaInicio, Date fechaFin, ProyectoCliente aProyectoCliente) throws Exception {

		List<Cita> lstCitas = new ArrayList<Cita>();
		List<Object> parametros = new ArrayList<Object>();
		Cita cita = null;
		Conexion conexion = new Conexion();
		ResultSet rs = null;
		try {

			StringBuilder sql = new StringBuilder();
			sql.append("  SELECT c.*, cl.cliente, cl.representante, cl.nit, pr.nombre FROM cita c");
			sql.append("  INNER JOIN proyectos_cliente p ON  p.id = c.id_proyecto_cliente");
			sql.append("  INNER JOIN clientes cl ON  cl.id = p.id_cliente");
			sql.append("  INNER JOIN proyectos pr ON  pr.id = p.id_proyecto WHERE 1=1");

			if (fechaInicio != null) {
				// sql.append(" AND c.fecha_inicio >= ?");
				// parametros.add(fechaInicio);
			}
			if (fechaFin != null) {
				// sql.append(" AND c.fecha_fin <= ?");
				// parametros.add(fechaFin);
			}
			if (aProyectoCliente != null) {
				if (aProyectoCliente.getConsultor() != null && aProyectoCliente.getConsultor().getId() != null) {
					sql.append("  AND p.id_consultor = ? ");
					parametros.add(aProyectoCliente.getConsultor().getId());
				}
				if (aProyectoCliente.getCliente() != null && aProyectoCliente.getCliente().getId() != null) {
					sql.append("  AND p.id_cliente = ? ");
					parametros.add(aProyectoCliente.getCliente().getId());
				}

			}
			sql.append(" ORDER BY fecha_inicio DESC ");
			rs = conexion.consultarBD(sql.toString(), parametros);

			while (rs.next()) {
				cita = new Cita();
				cita.setId((Integer) rs.getObject("id"));
				cita.getProyectoCliente().setId((Integer) rs.getObject("id_proyecto_cliente"));
				cita.getProyectoCliente().getProyecto().setNombre((String) rs.getObject("nombre"));

				cita.getProyectoCliente().getCliente().setNit((String) rs.getObject("nit"));
				cita.getProyectoCliente().getCliente().setRepresentante((String) rs.getObject("representante"));
				cita.getProyectoCliente().getCliente().setCliente((String) rs.getObject("cliente"));

				cita.setEvent(new DefaultScheduleEvent(cita.getTituloCita(), (Date) rs.getObject("fecha_inicio"), (Date) rs.getObject("fecha_fin")));
				cita.setFechaInicio((Date) rs.getObject("fecha_inicio"));
				cita.setFechaFin((Date) rs.getObject("fecha_fin"));
				cita.setEstado((String) rs.getObject("estado"));

				cita.setObservacionesCliente((String) rs.getObject("observaciones_cliente"));
				cita.setObservacionesConsultor((String) rs.getObject("observaciones_consultor"));
				cita.setModoEdicion(true);
				lstCitas.add(cita);
			}

		} catch (Exception e) {
			throw new Exception(e);

		} finally {
			if (rs != null) {
				rs.close();
			}
			conexion.cerrarConexion();
		}

		return lstCitas;

	}

	public static List<DocumentoCronograma> getDocumentosCronograma(DocumentoCronograma aDocumento) throws Exception {
		List<DocumentoCronograma> documentos = new ArrayList<DocumentoCronograma>();
		List<Object> prametros = new ArrayList<Object>();
		DocumentoCronograma documento = null;
		Conexion conexion = new Conexion();
		ResultSet rs = null;
		try {

			StringBuilder sql = new StringBuilder();
			sql.append("  SELECT p.id, p.nombre, p.id_cronograma, p.extension_archivo");
			sql.append("  FROM documentos_cronograma p");
			sql.append("  WHERE 1=1 ");

			if (aDocumento != null && aDocumento.getCronograma() != null && aDocumento.getCronograma().getId() != null) {
				sql.append("  AND p.id_cronograma =  ? ");
				prametros.add(aDocumento.getCronograma().getId());
			}

			sql.append("  ORDER BY nombre");

			rs = conexion.consultarBD(sql.toString(), prametros);

			while (rs.next()) {
				documento = new DocumentoCronograma();
				documento.setId((Integer) rs.getObject("id"));
				documento.setNombre((String) rs.getObject("nombre"));
				documento.setExtensionArchivo((String) rs.getObject("extension_archivo"));
				documento.getCronograma().setId((Integer) rs.getObject("id_cronograma"));
				documentos.add(documento);
			}

		} catch (Exception e) {
			throw new Exception(e);

		} finally {

			if (rs != null) {
				rs.close();
			}
			conexion.cerrarConexion();

		}
		return documentos;

	}

	/**
	 * Obtiene los documentos pero no precarag el archiv o hasta que se haga click
	 * 
	 * @param aDocumento
	 * @return documentos
	 * @throws Exception
	 */
	public static List<DocumentoDiagnostico> getDocumentosDiagnostico(DocumentoDiagnostico aDocumento) throws Exception {
		List<DocumentoDiagnostico> documentos = new ArrayList<DocumentoDiagnostico>();
		List<Object> prametros = new ArrayList<Object>();
		DocumentoDiagnostico documento = null;
		Conexion conexion = new Conexion();
		ResultSet rs = null;
		try {

			StringBuilder sql = new StringBuilder();
			sql.append("  SELECT p.id, p.nombre, p.id_diagnostico, p.extension_archivo");
			sql.append("  FROM documentos_diagnostico p");
			sql.append("  WHERE 1=1 ");

			if (aDocumento != null && aDocumento.getDiagnostico() != null && aDocumento.getDiagnostico().getId() != null) {
				sql.append("  AND p.id_diagnostico =  ? ");
				prametros.add(aDocumento.getDiagnostico().getId());
			}

			sql.append("  ORDER BY nombre");

			rs = conexion.consultarBD(sql.toString(), prametros);

			while (rs.next()) {
				documento = new DocumentoDiagnostico();
				documento.setId((Integer) rs.getObject("id"));
				documento.setNombre((String) rs.getObject("nombre"));
				documento.setExtensionArchivo((String) rs.getObject("extension_archivo"));
				documento.getDiagnostico().setId((Integer) rs.getObject("id_diagnostico"));
				documentos.add(documento);
			}

		} catch (Exception e) {
			throw new Exception(e);

		} finally {

			if (rs != null) {
				rs.close();
			}
			conexion.cerrarConexion();

		}
		return documentos;

	}

	/**
	 * Obtiene los planes de acciï¿½n de un indicador
	 * 
	 * @param aIndicador
	 * @return planes
	 * @throws Exception
	 */
	public static List<PlanAccionIndicador> getPlanesAccion(Indicador aIndicador) throws Exception {
		List<PlanAccionIndicador> planes = new ArrayList<PlanAccionIndicador>();
		List<Object> prametros = new ArrayList<Object>();
		PlanAccionIndicador registro = null;
		Conexion conexion = new Conexion();
		ResultSet rs = null;
		try {

			StringBuilder sql = new StringBuilder();
			sql.append("  SELECT d.*");
			sql.append("  FROM plan_accion_indicador d");
			sql.append("  WHERE 1=1 ");

			if (aIndicador != null && aIndicador.getId() != null) {
				sql.append("  AND d.id_indicador = ?");
				prametros.add(aIndicador.getId());
			}

			sql.append("  ORDER BY d.id");

			rs = conexion.consultarBD(sql.toString(), prametros);

			while (rs.next()) {
				registro = new PlanAccionIndicador();
				registro.setId((Integer) rs.getObject("id"));
				registro.getIndicador().setId((Integer) rs.getObject("id_indicador"));

				registro.setResponsable((String) rs.getObject("responsable"));
				registro.setActividad((String) rs.getObject("actividad"));
				registro.setFecha((Date) rs.getObject("fecha"));

				planes.add(registro);
			}

		} catch (Exception e) {
			throw new Exception(e);

		} finally {

			if (rs != null) {
				rs.close();
			}
			conexion.cerrarConexion();

		}
		return planes;

	}

	/**
	 * Obtiene los indicadores de acuerdo a ciertos criterios
	 * 
	 * @param aIndicador
	 * @return
	 * @throws Exception
	 */
	public static List<Indicador> getIndicadores(Indicador aIndicador) throws Exception {
		List<Indicador> indicadores = new ArrayList<Indicador>();
		List<Object> prametros = new ArrayList<Object>();
		Indicador registro = null;
		Conexion conexion = new Conexion();
		ResultSet rs = null;
		try {

			StringBuilder sql = new StringBuilder();
			sql.append("  SELECT d.*");
			sql.append("  FROM indicadores_objetivo d");
			sql.append("  WHERE 1=1 ");

			if (aIndicador != null && aIndicador.getObjetivo() != null && aIndicador.getObjetivo().getId() != null) {
				sql.append("  AND d.id_objetivo = ?");
				prametros.add(aIndicador.getObjetivo().getId());
			}

			sql.append("  ORDER BY d.id");

			rs = conexion.consultarBD(sql.toString(), prametros);

			while (rs.next()) {
				registro = new Indicador();
				registro.setId((Integer) rs.getObject("id"));
				registro.getObjetivo().setId((Integer) rs.getObject("id_objetivo"));

				registro.setNombreIndicador((String) rs.getObject("nombre_indicador"));
				registro.setFormula((String) rs.getObject("formula"));
				registro.setResponsableDecision((String) rs.getObject("responsable_decision"));
				registro.setResponsableCalculo((String) rs.getObject("responsable_calculo"));
				registro.setSistemaInformacion((String) rs.getObject("sistema_informacion"));
				registro.setFrecuencia((Integer) rs.getObject("frecuencia"));
				registro.setMetaSuperior((BigDecimal) rs.getObject("meta_superior"));
				registro.setMetaIntermedia((BigDecimal) rs.getObject("meta_intermedia"));
				registro.setMetaInferior((BigDecimal) rs.getObject("meta_inferior"));
				registro.setPeriodo1((BigDecimal) rs.getObject("periodo1"));
				registro.setPeriodo2((BigDecimal) rs.getObject("periodo2"));
				registro.setPeriodo3((BigDecimal) rs.getObject("periodo3"));
				registro.setPeriodo4((BigDecimal) rs.getObject("periodo4"));
				registro.setPeriodo5((BigDecimal) rs.getObject("periodo5"));
				registro.setPeriodo6((BigDecimal) rs.getObject("periodo6"));
				registro.setPeriodo7((BigDecimal) rs.getObject("periodo7"));
				registro.setPeriodo8((BigDecimal) rs.getObject("periodo8"));
				registro.setPeriodo9((BigDecimal) rs.getObject("periodo9"));
				registro.setPeriodo10((BigDecimal) rs.getObject("periodo10"));
				registro.setPeriodo11((BigDecimal) rs.getObject("periodo11"));
				registro.setPeriodo12((BigDecimal) rs.getObject("periodo12"));

				registro.setObservacionesResultados((String) rs.getObject("observaciones_resultados"));
				registro.setNumerador1((BigDecimal) rs.getObject("numerador1"));
				registro.setDenominador1((BigDecimal) rs.getObject("denominador1"));
				registro.setNumerador2((BigDecimal) rs.getObject("numerador2"));
				registro.setDenominador2((BigDecimal) rs.getObject("denominador2"));
				registro.setNumerador3((BigDecimal) rs.getObject("numerador3"));
				registro.setDenominador3((BigDecimal) rs.getObject("denominador3"));
				registro.setNumerador4((BigDecimal) rs.getObject("numerador4"));
				registro.setDenominador4((BigDecimal) rs.getObject("denominador4"));
				registro.setNumerador5((BigDecimal) rs.getObject("numerador5"));
				registro.setDenominador5((BigDecimal) rs.getObject("denominador5"));
				registro.setNumerador6((BigDecimal) rs.getObject("numerador6"));
				registro.setDenominador6((BigDecimal) rs.getObject("denominador6"));
				registro.setNumerador7((BigDecimal) rs.getObject("numerador7"));
				registro.setDenominador7((BigDecimal) rs.getObject("denominador7"));
				registro.setNumerador8((BigDecimal) rs.getObject("numerador8"));
				registro.setDenominador8((BigDecimal) rs.getObject("denominador8"));
				registro.setNumerador9((BigDecimal) rs.getObject("numerador9"));
				registro.setDenominador9((BigDecimal) rs.getObject("denominador9"));
				registro.setNumerador10((BigDecimal) rs.getObject("numerador10"));
				registro.setDenominador10((BigDecimal) rs.getObject("denominador10"));
				registro.setNumerador11((BigDecimal) rs.getObject("numerador11"));
				registro.setDenominador11((BigDecimal) rs.getObject("denominador11"));
				registro.setNumerador12((BigDecimal) rs.getObject("numerador12"));
				registro.setDenominador12((BigDecimal) rs.getObject("denominador12"));

				indicadores.add(registro);
			}

		} catch (Exception e) {
			throw new Exception(e);

		} finally {

			if (rs != null) {
				rs.close();
			}
			conexion.cerrarConexion();

		}
		return indicadores;

	}

	/**
	 * Obtiene los objetivos el indicador actual
	 * 
	 * @param aObjetivoEtapaIndicador
	 * @return objetivos
	 * @throws Exception
	 */
	public static List<ObjetivoEtapaIndicador> getObjetivos(ObjetivoEtapaIndicador aObjetivoEtapaIndicador) throws Exception {
		List<ObjetivoEtapaIndicador> objetivos = new ArrayList<ObjetivoEtapaIndicador>();
		List<Object> prametros = new ArrayList<Object>();
		ObjetivoEtapaIndicador registro = null;
		Conexion conexion = new Conexion();
		ResultSet rs = null;
		try {

			StringBuilder sql = new StringBuilder();
			sql.append("  SELECT d.*");
			sql.append("  FROM objetivos_etapa_indicadores d");
			sql.append("  WHERE 1=1 ");

			if (aObjetivoEtapaIndicador != null && aObjetivoEtapaIndicador.getInformacionEtapaIndicador() != null && aObjetivoEtapaIndicador.getInformacionEtapaIndicador().getId() != null) {
				sql.append("  AND d.id_informacion_etapa = ?");
				prametros.add(aObjetivoEtapaIndicador.getInformacionEtapaIndicador().getId());
			}

			sql.append("  ORDER BY d.id");

			rs = conexion.consultarBD(sql.toString(), prametros);

			while (rs.next()) {
				registro = new ObjetivoEtapaIndicador();
				registro.setId((Integer) rs.getObject("id"));
				registro.getInformacionEtapaIndicador().setId((Integer) rs.getObject("id_informacion_etapa"));
				registro.setObjetivo((String) rs.getObject("objetivo"));
				registro.setObjetivoRelacionado((String) rs.getObject("objetivos_relacionados"));

				objetivos.add(registro);
			}

		} catch (Exception e) {
			throw new Exception(e);

		} finally {

			if (rs != null) {
				rs.close();
			}
			conexion.cerrarConexion();

		}
		return objetivos;

	}

	public static DocumentoCronograma getAdjuntoDocumentoCronograma(DocumentoCronograma aDocumento) throws Exception {

		List<Object> prametros = new ArrayList<Object>();
		DocumentoCronograma documento = null;
		Conexion conexion = new Conexion();
		ResultSet rs = null;
		try {

			StringBuilder sql = new StringBuilder();
			sql.append("  SELECT archivo, p.content_type, p.extension_archivo");
			sql.append("  FROM documentos_cronograma p");
			sql.append("  WHERE 1=1 ");

			if (aDocumento != null && aDocumento.getId() != null) {
				sql.append("  AND p.id =  ? ");
				prametros.add(aDocumento.getId());
			}

			rs = conexion.consultarBD(sql.toString(), prametros);

			if (rs.next()) {

				documento = new DocumentoCronograma();
				documento.setArchivo(rs.getBytes("archivo"));
				documento.setContentType((String) rs.getObject("content_type"));
				documento.setExtensionArchivo((String) rs.getObject("extension_archivo"));

			}

		} catch (Exception e) {
			throw new Exception(e);

		} finally {

			if (rs != null) {
				rs.close();
			}
			conexion.cerrarConexion();

		}
		return documento;

	}

	/**
	 * Obtiene el adjunto de un documento si éste existe
	 * 
	 * @param aDocumento
	 * @return documento
	 * @throws Exception
	 */
	public static DocumentoDiagnostico getAdjuntoDocumentoDiagnostico(DocumentoDiagnostico aDocumento) throws Exception {

		List<Object> prametros = new ArrayList<Object>();
		DocumentoDiagnostico documento = null;
		Conexion conexion = new Conexion();
		ResultSet rs = null;
		try {

			StringBuilder sql = new StringBuilder();
			sql.append("  SELECT archivo, p.content_type, p.extension_archivo");
			sql.append("  FROM documentos_diagnostico p");
			sql.append("  WHERE 1=1 ");

			if (aDocumento != null && aDocumento.getId() != null) {
				sql.append("  AND p.id =  ? ");
				prametros.add(aDocumento.getId());
			}

			rs = conexion.consultarBD(sql.toString(), prametros);

			if (rs.next()) {

				documento = new DocumentoDiagnostico();
				documento.setArchivo(rs.getBytes("archivo"));
				documento.setContentType((String) rs.getObject("content_type"));
				documento.setExtensionArchivo((String) rs.getObject("extension_archivo"));

			}

		} catch (Exception e) {
			throw new Exception(e);

		} finally {

			if (rs != null) {
				rs.close();
			}
			conexion.cerrarConexion();

		}
		return documento;

	}

	/**
	 * Obtiene el archivo especï¿½fico
	 * 
	 * @param aDocumento
	 * @return documento
	 * @throws Exception
	 */
	public static DocumentoActividad getAdjuntoDocumento(DocumentoActividad aDocumento) throws Exception {

		List<Object> prametros = new ArrayList<Object>();
		DocumentoActividad documento = null;
		Conexion conexion = new Conexion();
		ResultSet rs = null;
		try {

			StringBuilder sql = new StringBuilder();
			sql.append("  SELECT archivo");
			sql.append("  FROM documentos_actividad p");
			sql.append("  WHERE 1=1 ");

			if (aDocumento != null && aDocumento.getId() != null) {
				sql.append("  AND p.id =  ? ");
				prametros.add(aDocumento.getId());
			}

			rs = conexion.consultarBD(sql.toString(), prametros);

			if (rs.next()) {

				documento = new DocumentoActividad();
				documento.setArchivo(rs.getBytes("archivo"));

			}

		} catch (Exception e) {
			throw new Exception(e);

		} finally {

			if (rs != null) {
				rs.close();
			}
			conexion.cerrarConexion();

		}
		return documento;

	}

	/**
	 * Obtiene los documentos de una actividad
	 * 
	 * @param aDocumento
	 * @return documentos
	 * @throws Exception
	 */
	public static List<DocumentoActividad> getDocumentos(DocumentoActividad aDocumento) throws Exception {
		List<DocumentoActividad> documentos = new ArrayList<DocumentoActividad>();
		List<Object> prametros = new ArrayList<Object>();
		DocumentoActividad documento = null;
		Conexion conexion = new Conexion();
		ResultSet rs = null;
		try {

			StringBuilder sql = new StringBuilder();
			sql.append("  SELECT p.id, p.nombre, p.descargable, p.id_tarea_proyecto, p.fecha, p.id_consultor, p.id_administrador, p.id_proyecto_cliente, co.nombres nombre_consultor, co.apellidos apellido_consultor, ad.nombres nombre_administrador, ad.apellidos apellido_administrador, cl.representante, cl.cliente");
			sql.append("  FROM documentos_actividad p");
			sql.append("  LEFT JOIN consultores co ON co.id = p.id_consultor");
			sql.append("  LEFT JOIN personal ad ON ad.id = p.id_administrador");
			sql.append("  LEFT JOIN proyectos_cliente pc ON pc.id = p.id_proyecto_cliente");
			sql.append("  LEFT JOIN clientes cl ON cl.id = pc.id_cliente");
			sql.append("  WHERE 1=1 ");

			if (aDocumento != null && aDocumento.getTareaProyecto() != null && aDocumento.getTareaProyecto().getId() != null) {
				sql.append("  AND p.id_tarea_proyecto =  ? ");
				prametros.add(aDocumento.getTareaProyecto().getId());
			}

			if (aDocumento != null && aDocumento.getProyectoCliente() != null && aDocumento.getProyectoCliente().getId() != null) {
				sql.append("  AND p.id_proyecto_cliente =  ? ");
				prametros.add(aDocumento.getProyectoCliente().getId());

			} else {

				sql.append("  AND p.id_proyecto_cliente IS NULL");

			}

			sql.append("  ORDER BY nombre");

			rs = conexion.consultarBD(sql.toString(), prametros);

			while (rs.next()) {
				documento = new DocumentoActividad();
				documento.setId((Integer) rs.getObject("id"));
				documento.setNombre((String) rs.getObject("nombre"));
				documento.setDescargable((String) rs.getObject("descargable"));
				documento.setFecha((Date) rs.getObject("fecha"));
				documento.getTareaProyecto().setId((Integer) rs.getObject("id_tarea_proyecto"));

				documento.getConsultor().setId((Integer) rs.getObject("id_consultor"));
				documento.getConsultor().setNombres((String) rs.getObject("nombre_consultor"));
				documento.getConsultor().setApellidos((String) rs.getObject("apellido_consultor"));

				documento.getPersonal().setId((Integer) rs.getObject("id_administrador"));
				documento.getPersonal().setNombres((String) rs.getObject("nombre_administrador"));
				documento.getPersonal().setApellidos((String) rs.getObject("apellido_administrador"));

				documento.getProyectoCliente().setId((Integer) rs.getObject("id_proyecto_cliente"));
				documento.getProyectoCliente().getCliente().setRepresentante((String) rs.getObject("representante"));
				documento.getProyectoCliente().getCliente().setCliente((String) rs.getObject("cliente"));

				documentos.add(documento);
			}

		} catch (Exception e) {
			throw new Exception(e);

		} finally {

			if (rs != null) {
				rs.close();
			}
			conexion.cerrarConexion();

		}
		return documentos;

	}

	public static CompromisoGeneral getCompromisoGeneral(ProyectoCliente aProyectoCliente) throws Exception {

		List<Object> prametros = new ArrayList<Object>();
		CompromisoGeneral registro = null;
		Conexion conexion = new Conexion();
		ResultSet rs = null;
		try {

			StringBuilder sql = new StringBuilder();
			sql.append("  SELECT p.*");
			sql.append("  FROM compromiso_general p");
			sql.append("  WHERE 1=1 ");

			if (aProyectoCliente != null && aProyectoCliente.getId() != null) {
				sql.append("  AND p.id_proyecto_cliente = ?");
				prametros.add(aProyectoCliente.getId());
			}

			rs = conexion.consultarBD(sql.toString(), prametros);

			if (rs.next()) {
				registro = new CompromisoGeneral();
				registro.getProyectoCliente().setId((Integer) rs.getObject("id_proyecto_cliente"));
				registro.setObservaciones((String) rs.getObject("observaciones"));
				registro.setRequiereFirma((String) rs.getObject("requiere_firma"));
				registro.setFirma((String) rs.getObject("firma"));

				registro.setFechaGeneracionTodo((Date) rs.getObject("fecha_generacion_todo"));

			}

		} catch (Exception e) {
			throw new Exception(e);

		} finally {

			if (rs != null) {
				rs.close();
			}
			conexion.cerrarConexion();

		}
		return registro;

	}

	public static DocumentacionGeneral getDocumentacionGeneral(ProyectoCliente aProyectoCliente) throws Exception {

		List<Object> prametros = new ArrayList<Object>();
		DocumentacionGeneral registro = null;
		Conexion conexion = new Conexion();
		ResultSet rs = null;
		try {

			StringBuilder sql = new StringBuilder();
			sql.append("  SELECT p.*");
			sql.append("  FROM documentacion_general p");
			sql.append("  WHERE 1=1 ");

			if (aProyectoCliente != null && aProyectoCliente.getId() != null) {
				sql.append("  AND p.id_proyecto_cliente = ?");
				prametros.add(aProyectoCliente.getId());
			}

			rs = conexion.consultarBD(sql.toString(), prametros);

			if (rs.next()) {
				registro = new DocumentacionGeneral();
				registro.getProyectoCliente().setId((Integer) rs.getObject("id_proyecto_cliente"));
				registro.setObservaciones((String) rs.getObject("observaciones"));
				registro.setRequiereFirma((String) rs.getObject("requiere_firma"));
				registro.setFirma((String) rs.getObject("firma"));

				registro.setFechaGeneracionTodo((Date) rs.getObject("fecha_generacion_todo"));

			}

		} catch (Exception e) {
			throw new Exception(e);

		} finally {

			if (rs != null) {
				rs.close();
			}
			conexion.cerrarConexion();

		}
		return registro;

	}

	public static CronogramaGeneral getCronogramaGeneral(ProyectoCliente aProyectoCliente) throws Exception {

		List<Object> prametros = new ArrayList<Object>();
		CronogramaGeneral registro = null;
		Conexion conexion = new Conexion();
		ResultSet rs = null;
		try {

			StringBuilder sql = new StringBuilder();
			sql.append("  SELECT p.*");
			sql.append("  FROM cronograma_general p");
			sql.append("  WHERE 1=1 ");

			if (aProyectoCliente != null && aProyectoCliente.getId() != null) {
				sql.append("  AND p.id_proyecto_cliente = ?");
				prametros.add(aProyectoCliente.getId());
			}

			rs = conexion.consultarBD(sql.toString(), prametros);

			if (rs.next()) {
				registro = new CronogramaGeneral();
				registro.getProyectoCliente().setId((Integer) rs.getObject("id_proyecto_cliente"));
				registro.setObservaciones((String) rs.getObject("observaciones"));
				registro.setRequiereFirma((String) rs.getObject("requiere_firma"));
				registro.setFirma((String) rs.getObject("firma"));
				registro.setMetaSuperiorDocumentacion((BigDecimal) rs.getObject("meta_superior_documentacion"));
				registro.setMetaSuperiorSocializacion((BigDecimal) rs.getObject("meta_superior_socializacion"));
				registro.setMetaSuperiorImplementacion((BigDecimal) rs.getObject("meta_superior_implementacion"));
				registro.setMetaSuperiorAuditoria((BigDecimal) rs.getObject("meta_superior_auditoria"));
				registro.setMetaIntermediaDocumentacion((BigDecimal) rs.getObject("meta_intermedia_documentacion"));
				registro.setMetaIntermediaSocializacion((BigDecimal) rs.getObject("meta_intermedia_socializacion"));
				registro.setMetaIntermediaImplementacion((BigDecimal) rs.getObject("meta_intermedia_implementacion"));
				registro.setMetaIntermediaAuditoria((BigDecimal) rs.getObject("meta_intermedia_auditoria"));
				registro.setMetaInferiorDocumentacion((BigDecimal) rs.getObject("meta_inferior_documentacion"));
				registro.setMetaInferiorSocializacion((BigDecimal) rs.getObject("meta_inferior_socializacion"));
				registro.setMetaInferiorImplementacion((BigDecimal) rs.getObject("meta_inferior_implementacion"));
				registro.setMetaInferiorAuditoria((BigDecimal) rs.getObject("meta_inferior_auditoria"));

				registro.setFechaGeneracionTodo((Date) rs.getObject("fecha_generacion_todo"));

			}

		} catch (Exception e) {
			throw new Exception(e);

		} finally {

			if (rs != null) {
				rs.close();
			}
			conexion.cerrarConexion();

		}
		return registro;

	}

	/**
	 * Obtiene un registro de planificaciï¿½n general
	 * 
	 * @param aPlanificacion
	 * @return registro
	 * @throws Exception
	 */
	public static PlanificacionGeneral getPlanificacionGeneral(ProyectoCliente aProyectoCliente) throws Exception {

		List<Object> prametros = new ArrayList<Object>();
		PlanificacionGeneral registro = null;
		Conexion conexion = new Conexion();
		ResultSet rs = null;
		try {

			StringBuilder sql = new StringBuilder();
			sql.append("  SELECT p.*");
			sql.append("  FROM planificacion_general p");
			sql.append("  WHERE 1=1 ");

			if (aProyectoCliente != null && aProyectoCliente.getId() != null) {
				sql.append("  AND p.id_proyecto_cliente = ?");
				prametros.add(aProyectoCliente.getId());
			}

			rs = conexion.consultarBD(sql.toString(), prametros);

			if (rs.next()) {
				registro = new PlanificacionGeneral();
				registro.getProyectoCliente().setId((Integer) rs.getObject("id_proyecto_cliente"));
				registro.setEstadoActual((String) rs.getObject("estado_actual"));
				registro.setFirma((String) rs.getObject("firma"));

			}

		} catch (Exception e) {
			throw new Exception(e);

		} finally {

			if (rs != null) {
				rs.close();
			}
			conexion.cerrarConexion();

		}
		return registro;

	}

	/**
	 * Obtiene un registro de tiempos planificaciï¿½n
	 * 
	 * @param aPlanificacion
	 * @return tiempos
	 * @throws Exception
	 */
	public static List<TiempoPlanificacion> getTiemposPlanificacion(TiempoPlanificacion aTiempoPlanificacion) throws Exception {
		List<TiempoPlanificacion> tiempos = new ArrayList<TiempoPlanificacion>();
		List<Object> prametros = new ArrayList<Object>();
		TiempoPlanificacion registro = null;
		SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
		Conexion conexion = new Conexion();
		ResultSet rs = null;
		try {

			StringBuilder sql = new StringBuilder();
			sql.append("  SELECT t.*");
			sql.append("  FROM tiempos_planificacion t");
			sql.append("  WHERE 1=1 ");

			if (aTiempoPlanificacion != null && aTiempoPlanificacion.getPlanificacion() != null && aTiempoPlanificacion.getPlanificacion().getId() != null) {
				sql.append("  AND t.id_planificacion = ?");
				prametros.add(aTiempoPlanificacion.getPlanificacion().getId());
			}

			if (aTiempoPlanificacion != null && aTiempoPlanificacion.getFecha() != null) {
				sql.append("  AND t.fecha = ?");
				prametros.add(formato.parse(formato.format(aTiempoPlanificacion.getFecha())));
			}

			if (aTiempoPlanificacion != null && aTiempoPlanificacion.getNumeroSemana() != null) {
				sql.append("  AND t.numero_semana = ?");
				prametros.add(aTiempoPlanificacion.getNumeroSemana());
			}

			rs = conexion.consultarBD(sql.toString(), prametros);

			while (rs.next()) {
				registro = new TiempoPlanificacion();
				registro.setId((Integer) rs.getObject("id"));
				registro.setEjecutado((boolean) rs.getObject("ejecutado"));
				registro.setProgramado((boolean) rs.getObject("programado"));
				registro.getPlanificacion().setId((Integer) rs.getObject("id_planificacion"));
				registro.setFecha((Date) rs.getObject("fecha"));
				registro.setNumeroSemana((Integer) rs.getObject("numero_semana"));

				tiempos.add(registro);
			}

		} catch (Exception e) {
			throw new Exception(e);

		} finally {

			if (rs != null) {
				rs.close();
			}
			conexion.cerrarConexion();

		}
		return tiempos;

	}

	/**
	 * Obtiene los estados del proyecto de un cliente si es que ya estï¿½n
	 * guardados
	 * 
	 * @param aEstadoProyectoCliente
	 * @return estados
	 * @throws Exception
	 */
	public static List<EstadoProyectoCliente> getEstadosProyectoCliente(EstadoProyectoCliente aEstadoProyectoCliente) throws Exception {
		List<EstadoProyectoCliente> estados = new ArrayList<EstadoProyectoCliente>();
		List<Object> prametros = new ArrayList<Object>();
		EstadoProyectoCliente estado = null;
		Conexion conexion = new Conexion();
		ResultSet rs = null;
		try {

			StringBuilder sql = new StringBuilder();
			sql.append("  SELECT d.*,p.nombre");
			sql.append("  FROM estados_proyecto_cliente d");
			sql.append("  INNER JOIN estados p ON p.id = d.id_estado");
			sql.append("  WHERE 1=1 ");

			if (aEstadoProyectoCliente != null && aEstadoProyectoCliente.getProyectoCliente() != null && aEstadoProyectoCliente.getProyectoCliente().getId() != null) {
				sql.append("  AND d.id_proyecto_cliente = ?");
				prametros.add(aEstadoProyectoCliente.getProyectoCliente().getId());
			}

			sql.append("  ORDER BY d.id_estado");

			rs = conexion.consultarBD(sql.toString(), prametros);

			while (rs.next()) {

				estado = new EstadoProyectoCliente();
				estado.setId((Integer) rs.getObject("id"));
				estado.getEstado().setNombre((String) rs.getObject("nombre"));
				estado.getEstado().setId((Integer) rs.getObject("id_estado"));

				estados.add(estado);
			}

		} catch (Exception e) {
			throw new Exception(e);

		} finally {

			if (rs != null) {
				rs.close();
			}
			conexion.cerrarConexion();

		}
		return estados;

	}

	/**
	 * Obtiene los estados de diagnostico creados
	 * 
	 * @param aDiagnostico
	 * @return estados
	 * @throws Exception
	 */
	public static List<EstadoDiagnostico> getEstadosDiagnostico(Diagnostico aDiagnostico) throws Exception {
		List<EstadoDiagnostico> estados = new ArrayList<EstadoDiagnostico>();
		List<Object> prametros = new ArrayList<Object>();
		EstadoDiagnostico estado = null;
		Conexion conexion = new Conexion();
		ResultSet rs = null;
		try {

			StringBuilder sql = new StringBuilder();
			sql.append("  SELECT d.*");
			sql.append("  FROM estados_diagnostico d");
			sql.append("  WHERE 1=1 ");

			if (aDiagnostico != null && aDiagnostico.getId() != null) {
				sql.append("  AND d.id_diagnostico = ?");
				prametros.add(aDiagnostico.getId());
			}

			rs = conexion.consultarBD(sql.toString(), prametros);

			while (rs.next()) {

				estado = new EstadoDiagnostico();
				estado.setId((Integer) rs.getObject("id"));
				estado.getEstado().setId((Integer) rs.getObject("id_estado"));
				estado.getDiagnostico().setId((Integer) rs.getObject("id_diagnostico"));

				estados.add(estado);
			}

		} catch (Exception e) {
			throw new Exception(e);

		} finally {

			if (rs != null) {
				rs.close();
			}
			conexion.cerrarConexion();

		}
		return estados;

	}

	/**
	 * Obtiene la informaciï¿½n general de los indicadores
	 * 
	 * @param aInformacion
	 * @return registro
	 * @throws Exception
	 */
	public static InformacionEtapaIndicador getInformacionGeneralIndicadores(InformacionEtapaIndicador aInformacion) throws Exception {

		List<Object> prametros = new ArrayList<Object>();
		InformacionEtapaIndicador registro = null;
		Conexion conexion = new Conexion();
		ResultSet rs = null;
		try {

			StringBuilder sql = new StringBuilder();
			sql.append("  SELECT i.*");
			sql.append("  FROM informacion_etapa_indicadores i");
			sql.append("  WHERE 1=1 ");

			if (aInformacion != null && aInformacion.getProyectoCliente() != null && aInformacion.getProyectoCliente().getId() != null) {
				sql.append("  AND i.id_proyecto_cliente = ?");
				prametros.add(aInformacion.getProyectoCliente().getId());
			}

			rs = conexion.consultarBD(sql.toString(), prametros);

			if (rs.next()) {
				registro = new InformacionEtapaIndicador();
				registro.setId((Integer) rs.getObject("id"));
				registro.getProyectoCliente().setId((Integer) rs.getObject("id_proyecto_cliente"));
				registro.setFechaInicio((Date) rs.getObject("fecha_inicio"));

				registro.setFirma((String) rs.getObject("firma"));
				registro.setConclusiones((String) rs.getObject("conclusiones"));
				registro.setFechaGeneracionTodo((Date) rs.getObject("fecha_generacion_todo"));

			}

		} catch (Exception e) {
			throw new Exception(e);

		} finally {

			if (rs != null) {
				rs.close();
			}
			conexion.cerrarConexion();

		}
		return registro;

	}

	public static List<Compromiso> getCompromisos(Compromiso aCompromiso) throws Exception {
		List<Compromiso> listado = new ArrayList<Compromiso>();
		List<Object> prametros = new ArrayList<Object>();
		Compromiso registro = null;
		Conexion conexion = new Conexion();
		ResultSet rs = null;
		try {

			StringBuilder sql = new StringBuilder();
			sql.append("  SELECT d.*");
			sql.append("  FROM compromisos d");
			sql.append("  WHERE 1=1 ");

			if (aCompromiso != null && aCompromiso.getProyectoCliente() != null && aCompromiso.getProyectoCliente().getId() != null) {
				sql.append("  AND d.id_proyecto_cliente = ?");
				prametros.add(aCompromiso.getProyectoCliente().getId());
			}

			sql.append("  ORDER BY id");

			rs = conexion.consultarBD(sql.toString(), prametros);

			while (rs.next()) {

				registro = new Compromiso();
				registro.setId((Integer) rs.getObject("id"));
				registro.getProyectoCliente().setId((Integer) rs.getObject("id_proyecto_cliente"));
				registro.setCopromisos((String) rs.getObject("compromisos"));
				registro.setResponsable((String) rs.getObject("responsable"));
				registro.setFechaInicioCompromiso((Date) rs.getObject("fecha_inicio_compromiso"));
				registro.setFechaFinCompromiso((Date) rs.getObject("fecha_fin_compromiso"));
				registro.setCumplimiento((String) rs.getObject("cumplimiento"));
				registro.setFechaSeguimiento((Date) rs.getObject("fecha_seguimiento"));
				registro.setEstado((String) rs.getObject("estado"));
				registro.setObservacionesEstado((String) rs.getObject("observaciones"));
				registro.setFechaParaCuando((Date) rs.getObject("para_cuando"));

				listado.add(registro);
			}

		} catch (Exception e) {
			throw new Exception(e);

		} finally {

			if (rs != null) {
				rs.close();
			}
			conexion.cerrarConexion();

		}
		return listado;

	}

	public static List<Cronograma> getCronograma(Cronograma aCronograma) throws Exception {
		List<Cronograma> listado = new ArrayList<Cronograma>();
		List<Object> prametros = new ArrayList<Object>();
		Cronograma registro = null;
		Conexion conexion = new Conexion();
		ResultSet rs = null;
		try {

			StringBuilder sql = new StringBuilder();
			sql.append("  SELECT d.*, p.tarea, p.posicion, p.requisito");
			sql.append("  FROM cronograma d");
			sql.append("  INNER JOIN tareas_proyecto p ON p.id = d.id_tarea");
			sql.append("  WHERE 1=1 ");

			if (aCronograma != null && aCronograma.getProyectoCliente() != null && aCronograma.getProyectoCliente().getId() != null) {
				sql.append("  AND d.id_proyecto_cliente = ?");
				prametros.add(aCronograma.getProyectoCliente().getId());
			}

			sql.append("  ORDER BY posicion");

			rs = conexion.consultarBD(sql.toString(), prametros);

			while (rs.next()) {

				registro = new Cronograma();
				registro.setId((Integer) rs.getObject("id"));
				registro.getProyectoCliente().setId((Integer) rs.getObject("id_proyecto_cliente"));
				registro.getTareaProyecto().setPosicion((Integer) rs.getObject("posicion"));
				registro.getTareaProyecto().setTarea((String) rs.getObject("tarea"));
				registro.getTareaProyecto().setId((Integer) rs.getObject("id_tarea"));
				registro.getTareaProyecto().setRequisito((String) rs.getObject("requisito"));

				registro.setRecomendaciones((String) rs.getObject("recomendaciones"));
				registro.setMetaDocumentacion((BigDecimal) rs.getObject("meta_documentacion"));
				registro.setMetaSocializacion((BigDecimal) rs.getObject("meta_socializacion"));
				registro.setMetaImplementacion((BigDecimal) rs.getObject("meta_implementacion"));
				registro.setMetaAuditoria((BigDecimal) rs.getObject("meta_auditoria"));

				registro.setFechaInicioDocumentacion((Date) rs.getObject("fecha_inicio_documentacion"));
				registro.setFechaInicioSocializacion((Date) rs.getObject("fecha_inicio_socializacion"));
				registro.setFechaInicioImplementacion((Date) rs.getObject("fecha_inicio_implementacion"));
				registro.setFechaInicioAuditoria((Date) rs.getObject("fecha_inicio_auditoria"));

				registro.setFechaFinDocumentacion((Date) rs.getObject("fecha_fin_documentacion"));
				registro.setFechaFinSocializacion((Date) rs.getObject("fecha_fin_socializacion"));
				registro.setFechaFinImplementacion((Date) rs.getObject("fecha_fin_implementacion"));
				registro.setFechaFinAuditoria((Date) rs.getObject("fecha_fin_auditoria"));

				registro.setFechaSeguimientoDocumentacion((Date) rs.getObject("fecha_seguimiento_documentacion"));
				registro.setFechaSeguimientoSocializacion((Date) rs.getObject("fecha_seguimiento_socializacion"));
				registro.setFechaSeguimientoImplementacion((Date) rs.getObject("fecha_seguimiento_implementacion"));
				registro.setFechaSeguimientoAuditoria((Date) rs.getObject("fecha_seguimiento_auditoria"));

				registro.setResponsableDocumentacion((String) rs.getObject("responsable_documentacion"));
				registro.setResponsableSocializacion((String) rs.getObject("responsable_socializacion"));
				registro.setResponsableImplementacion((String) rs.getObject("responsable_implementacion"));
				registro.setResponsableAuditoria((String) rs.getObject("responsable_auditoria"));

				listado.add(registro);
			}

		} catch (Exception e) {
			throw new Exception(e);

		} finally {

			if (rs != null) {
				rs.close();
			}
			conexion.cerrarConexion();

		}
		return listado;

	}

	public static List<Documentacion> getDocumentacion(Documentacion aDocumentacion) throws Exception {
		List<Documentacion> listado = new ArrayList<Documentacion>();
		List<Object> prametros = new ArrayList<Object>();
		Documentacion registro = null;
		Conexion conexion = new Conexion();
		ResultSet rs = null;
		try {

			StringBuilder sql = new StringBuilder();
			sql.append("  SELECT d.*, p.tarea, p.posicion, p.requisito, p.explicacion_documentacion");
			sql.append("  FROM documentacion d");
			sql.append("  INNER JOIN tareas_proyecto p ON p.id = d.id_tarea");
			sql.append("  WHERE 1=1 ");

			if (aDocumentacion != null && aDocumentacion.getProyectoCliente() != null && aDocumentacion.getProyectoCliente().getId() != null) {
				sql.append("  AND d.id_proyecto_cliente = ?");
				prametros.add(aDocumentacion.getProyectoCliente().getId());
			}

			sql.append("  ORDER BY posicion");

			rs = conexion.consultarBD(sql.toString(), prametros);

			while (rs.next()) {

				registro = new Documentacion();
				registro.setId((Integer) rs.getObject("id"));
				registro.getProyectoCliente().setId((Integer) rs.getObject("id_proyecto_cliente"));
				registro.getTareaProyecto().setPosicion((Integer) rs.getObject("posicion"));
				registro.getTareaProyecto().setTarea((String) rs.getObject("tarea"));
				registro.getTareaProyecto().setId((Integer) rs.getObject("id_tarea"));
				registro.getTareaProyecto().setRequisito((String) rs.getObject("requisito"));
				registro.getTareaProyecto().setExplicacionDocumentacion((String) rs.getObject("explicacion_documentacion"));

				registro.setRecomendacionesCliente((String) rs.getObject("recomendaciones_cliente"));
				registro.setRecomendacionesConsultor((String) rs.getObject("recomendaciones_consultor"));
				registro.setExplicacionConsultor((String) rs.getObject("explicacion_consultor"));
				registro.setEstadoActualDocumentacion((String) rs.getObject("estado_actual_documentacion"));

				registro.setFechaRevision((Date) rs.getObject("fecha_revision"));
				registro.setFechaAprobacion((Date) rs.getObject("fecha_aprobacion"));

				listado.add(registro);
			}

		} catch (Exception e) {
			throw new Exception(e);

		} finally {

			if (rs != null) {
				rs.close();
			}
			conexion.cerrarConexion();

		}
		return listado;

	}

	/**
	 * Obtiene un listado de planificaciï¿½n segï¿½n objeto de entrada
	 * 
	 * @param aPlanificacion
	 * @return listadoPlanificacion
	 * @throws Exception
	 */
	public static List<Planificacion> getPlanificacion(Planificacion aPlanificacion) throws Exception {
		List<Planificacion> listadoPlanificacion = new ArrayList<Planificacion>();
		List<Object> prametros = new ArrayList<Object>();
		Planificacion registro = null;
		Conexion conexion = new Conexion();
		ResultSet rs = null;
		try {

			StringBuilder sql = new StringBuilder();
			sql.append("  SELECT d.*, p.tarea, p.posicion, p.requisito");
			sql.append("  FROM planificacion d");
			sql.append("  INNER JOIN tareas_proyecto p ON p.id = d.id_tarea_proyecto");
			sql.append("  WHERE 1=1 ");

			if (aPlanificacion != null && aPlanificacion.getProyectoCliente() != null && aPlanificacion.getProyectoCliente().getId() != null) {
				sql.append("  AND d.id_proyecto_cliente = ?");
				prametros.add(aPlanificacion.getProyectoCliente().getId());
			}

			sql.append("  ORDER BY posicion");

			rs = conexion.consultarBD(sql.toString(), prametros);

			while (rs.next()) {
				registro = new Planificacion();
				registro.setId((Integer) rs.getObject("id"));
				registro.getProyectoCliente().setId((Integer) rs.getObject("id_proyecto_cliente"));
				registro.getTareaProyecto().setPosicion((Integer) rs.getObject("posicion"));
				registro.getTareaProyecto().setTarea((String) rs.getObject("tarea"));
				registro.setObservaciones((String) rs.getObject("observaciones"));
				registro.getTareaProyecto().setId((Integer) rs.getObject("id_tarea_proyecto"));
				registro.getTareaProyecto().setRequisito((String) rs.getObject("requisito"));

				listadoPlanificacion.add(registro);
			}

		} catch (Exception e) {
			throw new Exception(e);

		} finally {

			if (rs != null) {
				rs.close();
			}
			conexion.cerrarConexion();

		}
		return listadoPlanificacion;

	}

	/**
	 * Obtiene la persona del diagnï¿½stico
	 * 
	 * @param aIdProyectoCliente
	 * @return
	 * @throws Exception
	 */
	public static PersonaDiagnostico getPersonaDiagnostico(Integer aIdProyectoCliente) throws Exception {

		List<Object> prametros = new ArrayList<Object>();
		PersonaDiagnostico persona = null;
		Conexion conexion = new Conexion();
		ResultSet rs = null;
		try {

			StringBuilder sql = new StringBuilder();
			sql.append("  SELECT d.*");
			sql.append("  FROM persona_diagnostico d");
			sql.append("  WHERE d.id_proyecto_cliente = ? ");
			prametros.add(aIdProyectoCliente);

			rs = conexion.consultarBD(sql.toString(), prametros);

			while (rs.next()) {
				persona = new PersonaDiagnostico();
				persona.getProyectoCliente().setId((Integer) rs.getObject("id_proyecto_cliente"));
				persona.setPersonaEntrevistada((String) rs.getObject("persona_entrevistada"));
				persona.setCargoEntrevistado((String) rs.getObject("cargo_entrevistado"));
				persona.setFirma((String) rs.getObject("firma"));
				persona.setRequiereFirma((String) rs.getObject("requiere_firma"));
				persona.setConclusiones((String) rs.getObject("conclusiones"));
				persona.setFecha((Date) rs.getObject("fecha"));

			}

		} catch (Exception e) {
			throw new Exception(e);

		} finally {

			if (rs != null) {
				rs.close();
			}
			conexion.cerrarConexion();

		}
		return persona;

	}

	/**
	 * Obtiene el listado de diagnostico realizado a un cliente
	 * 
	 * @param aDiagnostico
	 * @return listadoDiagnostico
	 * @throws Exception
	 */
	public static List<Diagnostico> getDiagnostico(Diagnostico aDiagnostico) throws Exception {
		List<Diagnostico> listadoDiagnostico = new ArrayList<Diagnostico>();
		List<Object> prametros = new ArrayList<Object>();
		Diagnostico registro = null;
		Conexion conexion = new Conexion();
		ResultSet rs = null;
		try {

			StringBuilder sql = new StringBuilder();
			sql.append("  SELECT d.*, p.pregunta, p.posible_evidencia, p.posicion");
			sql.append("  FROM diagnostico d");
			sql.append("  INNER JOIN preguntas_proyecto p ON p.id = d.id_pregunta_proyecto");
			sql.append("  WHERE 1=1 ");

			if (aDiagnostico != null && aDiagnostico.getProyectoCliente() != null && aDiagnostico.getProyectoCliente().getId() != null) {
				sql.append("  AND d.id_proyecto_cliente = ?");
				prametros.add(aDiagnostico.getProyectoCliente().getId());
			}

			sql.append("  ORDER BY posicion");

			rs = conexion.consultarBD(sql.toString(), prametros);

			while (rs.next()) {
				registro = new Diagnostico();
				registro.setId((Integer) rs.getObject("id"));
				registro.setEvidenciaEncontrada((String) rs.getObject("evidencia_encontrada"));

				registro.getProyectoCliente().setId((Integer) rs.getObject("id_proyecto_cliente"));

				registro.getPreguntaProyecto().setPosibleEvidencia((String) rs.getObject("posible_evidencia"));
				registro.getPreguntaProyecto().setPosicion((Integer) rs.getObject("posicion"));
				registro.getPreguntaProyecto().setPregunta((String) rs.getObject("pregunta"));
				registro.getPreguntaProyecto().setId((Integer) rs.getObject("id_pregunta_proyecto"));
				listadoDiagnostico.add(registro);
			}

		} catch (Exception e) {
			throw new Exception(e);

		} finally {

			if (rs != null) {
				rs.close();
			}
			conexion.cerrarConexion();

		}
		return listadoDiagnostico;

	}

	/**
	 * Obtiene los proyectos activos del cliente
	 * 
	 * @param aProyectoCliente
	 * @return proyectos
	 * @throws Exception
	 */
	public static List<ProyectoCliente> getProyectosClientesNormas(ProyectoCliente aProyectoCliente) throws Exception {
		List<ProyectoCliente> proyectos = new ArrayList<ProyectoCliente>();
		List<Object> prametros = new ArrayList<Object>();
		ProyectoCliente proyecto = null;
		Conexion conexion = new Conexion();
		ResultSet rs = null;
		try {

			StringBuilder sql = new StringBuilder();
			sql.append("  SELECT DISTINCT p.*, pr.*, c.*, co.*, 'A' AS estado");
			sql.append("  FROM proyectos_cliente p");
			sql.append("  INNER JOIN proyectos pr ON pr.id = p.id_proyecto");
			sql.append("  INNER JOIN clientes c ON c.id = p.id_cliente");
			sql.append("  INNER JOIN consultores co ON co.id = p.id_consultor");
			sql.append("  WHERE p.fecha_inicio <= CURRENT_DATE AND p.fecha_fin >= CURRENT_DATE");

			if (aProyectoCliente != null && aProyectoCliente.getCliente() != null && aProyectoCliente.getCliente().getId() != null) {
				sql.append("  AND p.id_cliente = ? ");
				prametros.add(aProyectoCliente.getCliente().getId());
			}

			if (aProyectoCliente != null && aProyectoCliente.getConsultor() != null && aProyectoCliente.getConsultor().getId() != null) {
				sql.append("  AND p.id_consultor = ? ");
				prametros.add(aProyectoCliente.getConsultor().getId());
			}

			sql.append("  UNION ALL");

			sql.append("  SELECT DISTINCT p.*, pr.*, c.*, co.*, 'I' AS estado");
			sql.append("  FROM  proyectos_cliente p");
			sql.append("  INNER JOIN proyectos pr ON pr.id = p.id_proyecto");
			sql.append("  INNER JOIN clientes c ON c.id = p.id_cliente");
			sql.append("  INNER JOIN consultores co ON co.id = p.id_consultor");
			sql.append("  WHERE NOT(p.fecha_inicio <= CURRENT_DATE AND p.fecha_fin >= CURRENT_DATE)");

			if (aProyectoCliente != null && aProyectoCliente.getCliente() != null && aProyectoCliente.getCliente().getId() != null) {
				sql.append("  AND p.id_cliente = ? ");
				prametros.add(aProyectoCliente.getCliente().getId());
			}

			if (aProyectoCliente != null && aProyectoCliente.getConsultor() != null && aProyectoCliente.getConsultor().getId() != null) {
				sql.append("  AND p.id_consultor = ? ");
				prametros.add(aProyectoCliente.getConsultor().getId());
			}

			sql.append("	ORDER BY estado, nombre");

			rs = conexion.consultarBD(sql.toString(), prametros);

			while (rs.next()) {
				proyecto = new ProyectoCliente();
				proyecto.setId((Integer) rs.getObject("id"));

				proyecto.getCliente().setId((Integer) rs.getObject("id_cliente"));
				proyecto.getCliente().setCliente((String) rs.getObject("cliente"));
				proyecto.getCliente().setNit((String) rs.getObject("nit"));

				proyecto.getConsultor().setId((Integer) rs.getObject("id_consultor"));
				proyecto.getConsultor().setNombres((String) rs.getObject("nombres"));
				proyecto.getConsultor().setApellidos((String) rs.getObject("apellidos"));

				proyecto.getProyecto().setId((Integer) rs.getObject("id_proyecto"));
				proyecto.getProyecto().setNombre((String) rs.getObject("nombre"));

				proyecto.setFechaInicio((Date) rs.getObject("fecha_inicio"));
				proyecto.setFechaFin((Date) rs.getObject("fecha_fin"));
				proyecto.setFechaCertificacion((Date) rs.getObject("fecha_certificacion"));

				proyecto.settEstado((String) rs.getObject("estado"));

				proyectos.add(proyecto);
			}

		} catch (Exception e) {
			throw new Exception(e);

		} finally {

			if (rs != null) {
				rs.close();
			}
			conexion.cerrarConexion();

		}
		return proyectos;

	}

	/**
	 * Obtiene los clientes cuyas normas sean activas o inactivas
	 * 
	 * @param aProyectoCliente
	 * @return clientes
	 * @throws Exception
	 */
	public static List<Cliente> getClientesNormas(ProyectoCliente aProyectoCliente) throws Exception {
		List<Cliente> clientes = new ArrayList<Cliente>();
		List<Object> prametros = new ArrayList<Object>();
		Cliente cliente = null;
		Conexion conexion = new Conexion();
		ResultSet rs = null;
		try {

			StringBuilder sql = new StringBuilder();
			sql.append("  SELECT DISTINCT c.*,'A' AS estado");
			sql.append("  FROM clientes c");
			sql.append("  INNER JOIN proyectos_cliente p ON p.id_cliente = c.id");
			sql.append("  WHERE p.fecha_inicio <= CURRENT_DATE AND p.fecha_fin >= CURRENT_DATE");

			if (aProyectoCliente != null && aProyectoCliente.getConsultor() != null && aProyectoCliente.getConsultor().getId() != null) {
				sql.append("  AND p.id_consultor = ? ");
				prametros.add(aProyectoCliente.getConsultor().getId());
			}

			sql.append("  UNION ALL");

			sql.append("  SELECT DISTINCT c.*,'I' AS estado");
			sql.append("  FROM clientes c");
			sql.append("  INNER JOIN proyectos_cliente p ON p.id_cliente = c.id");
			sql.append("  WHERE NOT(p.fecha_inicio <= CURRENT_DATE AND p.fecha_fin >= CURRENT_DATE)");

			if (aProyectoCliente != null && aProyectoCliente.getConsultor() != null && aProyectoCliente.getConsultor().getId() != null) {
				sql.append("  AND p.id_consultor = ? ");
				prametros.add(aProyectoCliente.getConsultor().getId());
			}

			sql.append("  AND c.id NOT IN(");
			sql.append("  								SELECT c.id");
			sql.append("  								FROM clientes c");
			sql.append("  								INNER JOIN proyectos_cliente p ON p.id_cliente = c.id");
			sql.append("  								WHERE p.fecha_inicio <= CURRENT_DATE AND p.fecha_fin >= CURRENT_DATE");
			sql.append("  							 )");
			sql.append("	ORDER BY estado, cliente");

			rs = conexion.consultarBD(sql.toString(), prametros);

			while (rs.next()) {
				cliente = new Cliente();
				cliente.setId((Integer) rs.getObject("id"));
				cliente.setCliente((String) rs.getObject("cliente"));
				cliente.setNit((String) rs.getObject("nit"));
				cliente.setRepresentante((String) rs.getObject("representante"));
				cliente.setCorreoElectronico((String) rs.getObject("correo_electronico"));
				cliente.setClave((String) rs.getObject("clave"));
				cliente.setTelefono((String) rs.getObject("telefono"));
				cliente.settEstado((String) rs.getObject("estado"));
				clientes.add(cliente);
			}

		} catch (Exception e) {
			throw new Exception(e);

		} finally {

			if (rs != null) {
				rs.close();
			}
			conexion.cerrarConexion();

		}
		return clientes;

	}

	/**
	 * Obtiene un listadod de tareas
	 * 
	 * @param conexion
	 * @param aTarea
	 * @return tareas
	 * @throws Exception
	 */
	public static List<TareaProyecto> getTareas(Conexion conexion, TareaProyecto aTarea) throws Exception {
		List<TareaProyecto> tareas = new ArrayList<TareaProyecto>();
		List<Object> parametros = new ArrayList<Object>();
		TareaProyecto tarea = null;

		ResultSet rs = null;
		try {

			StringBuilder sql = new StringBuilder();
			sql.append("  SELECT *");
			sql.append("  FROM tareas_proyecto p ");
			sql.append("  WHERE 1=1");

			if (aTarea != null && aTarea.getProyecto() != null && aTarea.getProyecto().getId() != null) {

				sql.append("  AND p.id_proyecto = ? ");
				parametros.add(aTarea.getProyecto().getId());
			}

			sql.append("  ORDER BY p.posicion");

			rs = conexion.consultarBD(sql.toString(), parametros);

			while (rs.next()) {

				tarea = new TareaProyecto();

				tarea.setId((Integer) rs.getObject("id"));
				tarea.setTarea(rs.getString("tarea"));
				tarea.setEstado((String) rs.getObject("estado"));
				tarea.setFechaEstado((Date) rs.getObject("fecha_estado"));
				tarea.setPosicion((Integer) rs.getObject("posicion"));
				tarea.setResponsable((String) rs.getObject("responsable"));
				tarea.setProducto((String) rs.getObject("producto"));
				tarea.setRequisito((String) rs.getObject("requisito"));
				tarea.setNumeroEtapa((Integer) rs.getObject("numero_etapa"));

				tarea.setExplicacionDocumentacion((String) rs.getObject("explicacion_documentacion"));

				// proyecto
				tarea.getProyecto().setId((Integer) rs.getObject("id_proyecto"));

				tareas.add(tarea);
			}

		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			if (rs != null) {
				rs.close();
			}

		}
		return tareas;

	}

	/**
	 * Obtiene las preguntas sobre una transacciï¿½n atï¿½mica
	 * 
	 * @param aPregunta
	 * @return preguntas
	 * @throws Exception
	 */
	public static List<PreguntaProyecto> getPreguntas(Conexion conexion, PreguntaProyecto aPregunta) throws Exception {
		List<PreguntaProyecto> preguntas = new ArrayList<PreguntaProyecto>();
		List<Object> parametros = new ArrayList<Object>();
		PreguntaProyecto pregunta = null;

		ResultSet rs = null;
		try {

			StringBuilder sql = new StringBuilder();
			sql.append("  SELECT *");
			sql.append("  FROM preguntas_proyecto p ");
			sql.append("  WHERE 1=1");

			if (aPregunta != null && aPregunta.getProyecto() != null && aPregunta.getProyecto().getId() != null) {

				sql.append("  AND p.id_proyecto = ? ");
				parametros.add(aPregunta.getProyecto().getId());
			}

			sql.append("  ORDER BY p.posicion");

			rs = conexion.consultarBD(sql.toString(), parametros);

			while (rs.next()) {

				// preguntas
				pregunta = new PreguntaProyecto();

				pregunta.setId((Integer) rs.getObject("id"));
				pregunta.setPregunta(rs.getString("pregunta"));
				pregunta.setPosibleEvidencia(rs.getString("posible_evidencia"));
				pregunta.setEstado((String) rs.getObject("estado"));
				pregunta.setFechaEstado((Date) rs.getObject("fecha_estado"));
				pregunta.setPosicion((Integer) rs.getObject("posicion"));
				// proyecto
				pregunta.getProyecto().setId((Integer) rs.getObject("id_proyecto"));

				preguntas.add(pregunta);
			}

		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			if (rs != null) {
				rs.close();
			}

		}
		return preguntas;

	}

	/**
	 * Obtiene un listado de las tareas posibles
	 * 
	 * @param aTarea
	 * @return tareas
	 * @throws Exception
	 */
	public static List<TareaProyecto> getTareas(TareaProyecto aTarea) throws Exception {
		List<TareaProyecto> tareas = new ArrayList<TareaProyecto>();
		List<Object> parametros = new ArrayList<Object>();
		TareaProyecto tarea = null;
		Conexion conexion = new Conexion();
		ResultSet rs = null;
		try {

			StringBuilder sql = new StringBuilder();
			sql.append("  SELECT *");
			sql.append("  FROM tareas_proyecto p ");
			sql.append("  WHERE 1=1");

			if (aTarea != null && aTarea.getId() != null) {

				sql.append("  AND p.id = ? ");
				parametros.add(aTarea.getId());
			}

			if (aTarea != null && aTarea.getProyecto() != null && aTarea.getProyecto().getId() != null) {

				sql.append("  AND p.id_proyecto = ? ");
				parametros.add(aTarea.getProyecto().getId());
			}

			if (aTarea != null && aTarea.getEstado() != null && aTarea.getEstado() != null) {

				sql.append("  AND p.estado = ? ");
				parametros.add(aTarea.getEstado());
			}

			sql.append("  ORDER BY p.id");

			rs = conexion.consultarBD(sql.toString(), parametros);

			while (rs.next()) {

				tarea = new TareaProyecto();

				tarea.setId((Integer) rs.getObject("id"));
				tarea.setTarea(rs.getString("tarea"));
				tarea.setEstado((String) rs.getObject("estado"));
				tarea.setFechaEstado((Date) rs.getObject("fecha_estado"));
				tarea.setPosicion((Integer) rs.getObject("posicion"));
				tarea.setResponsable((String) rs.getObject("responsable"));
				tarea.setProducto((String) rs.getObject("producto"));
				tarea.setRequisito((String) rs.getObject("requisito"));
				tarea.setNumeroEtapa((Integer) rs.getObject("numero_etapa"));
				tarea.setExplicacionDocumentacion((String) rs.getObject("explicacion_documentacion"));
				// proyecto
				tarea.getProyecto().setId((Integer) rs.getObject("id_proyecto"));

				tareas.add(tarea);
			}

		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			if (rs != null) {
				rs.close();
			}

			conexion.cerrarConexion();

		}
		return tareas;

	}

	/**
	 * Obtiene las preguntas de un proyecto. La Excepciï¿½n la muestra la lï¿½gica
	 * de negocio
	 * 
	 * @param aPregunta
	 * @return preguntas
	 * @throws Exception
	 */
	public static List<PreguntaProyecto> getPreguntas(PreguntaProyecto aPregunta) throws Exception {
		List<PreguntaProyecto> preguntas = new ArrayList<PreguntaProyecto>();
		List<Object> parametros = new ArrayList<Object>();
		PreguntaProyecto pregunta = null;
		Conexion conexion = new Conexion();
		ResultSet rs = null;
		try {

			StringBuilder sql = new StringBuilder();
			sql.append("  SELECT *");
			sql.append("  FROM preguntas_proyecto p ");
			sql.append("  WHERE 1=1");

			if (aPregunta != null && aPregunta.getProyecto() != null && aPregunta.getProyecto().getId() != null) {

				sql.append("  AND p.id_proyecto = ? ");
				parametros.add(aPregunta.getProyecto().getId());
			}

			if (aPregunta != null && aPregunta.getEstado() != null && aPregunta.getEstado() != null) {

				sql.append("  AND p.estado = ? ");
				parametros.add(aPregunta.getEstado());
			}

			sql.append("  ORDER BY p.posicion");

			rs = conexion.consultarBD(sql.toString(), parametros);

			while (rs.next()) {

				// preguntas
				pregunta = new PreguntaProyecto();

				pregunta.setId((Integer) rs.getObject("id"));
				pregunta.setPregunta(rs.getString("pregunta"));
				pregunta.setPosibleEvidencia(rs.getString("posible_evidencia"));
				pregunta.setEstado((String) rs.getObject("estado"));
				pregunta.setFechaEstado((Date) rs.getObject("fecha_estado"));
				pregunta.setPosicion((Integer) rs.getObject("posicion"));

				// proyecto
				pregunta.getProyecto().setId((Integer) rs.getObject("id_proyecto"));

				preguntas.add(pregunta);
			}

		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			if (rs != null) {
				rs.close();
			}

			conexion.cerrarConexion();

		}
		return preguntas;

	}

	/**
	 * Obtiene un listado de proyectos
	 * 
	 * @param aProyecto
	 * @return proyectos
	 * @throws Exception
	 */
	public static List<Proyecto> getProyectos(Proyecto aProyecto) throws Exception {

		List<Proyecto> proyectos = new ArrayList<Proyecto>();
		List<Object> parametros = new ArrayList<Object>();
		Proyecto proyecto = null;
		Conexion conexion = new Conexion();
		ResultSet rs = null;
		try {

			StringBuilder sql = new StringBuilder();
			sql.append("  SELECT a.*");
			sql.append("  FROM proyectos a ");
			sql.append("  WHERE 1=1");

			if (aProyecto != null && aProyecto.getId() != null) {
				sql.append("  AND a.id = ?");
				parametros.add(aProyecto.getId());
			}

			if (aProyecto != null && aProyecto.getIndicativoVigencia() != null && !aProyecto.getIndicativoVigencia().trim().equals("")) {
				sql.append("  AND a.indicativo_vigencia = ?");
				parametros.add(aProyecto.getIndicativoVigencia().trim());
			}

			sql.append("  ORDER BY a.id ");

			rs = conexion.consultarBD(sql.toString(), parametros);

			while (rs.next()) {

				proyecto = new Proyecto();

				proyecto.setId((Integer) rs.getObject("id"));
				proyecto.setNombre((String) rs.getObject("nombre"));
				proyecto.setIndicativoVigencia((String) rs.getObject("indicativo_vigencia"));

				proyectos.add(proyecto);

			}

		} catch (Exception e) {
			throw new Exception(e);

		} finally {

			if (rs != null) {
				rs.close();
			}
			conexion.cerrarConexion();

		}

		return proyectos;

	}

	/**
	 * Obtiene los parï¿½metrois de auditorï¿½a
	 * 
	 * @return parametroAuditoria
	 * @throws Exception
	 */
	public static ParametroAuditoria getParametroAuditoria() throws Exception {

		ParametroAuditoria parametroAuditoria = null;
		Conexion conexion = new Conexion();
		ResultSet rs = null;
		try {

			StringBuilder sql = new StringBuilder();
			sql.append("  SELECT a.*");
			sql.append("  FROM parametros_auditoria a ");
			sql.append("  WHERE id = 1");

			rs = conexion.consultarBD(sql.toString(), null);

			if (rs.next()) {

				parametroAuditoria = new ParametroAuditoria();

				parametroAuditoria.setId((Integer) rs.getObject("id"));
				parametroAuditoria.setObjetivos((String) rs.getObject("objetivos"));
				parametroAuditoria.setAlcance((String) rs.getObject("alcance"));
				parametroAuditoria.setDocumentosReferencia((String) rs.getObject("documentos_referencia"));

			}

		} catch (Exception e) {
			throw new Exception(e);

		} finally {

			if (rs != null) {
				rs.close();
			}
			conexion.cerrarConexion();

		}

		return parametroAuditoria;

	}

	/**
	 * Obtiene un listado de estados
	 * 
	 * @param aEstado
	 * @return estados
	 * @throws Exception
	 */
	public static List<Estado> getEstados(Estado aEstado) throws Exception {

		List<Estado> estados = new ArrayList<Estado>();
		List<Object> parametros = new ArrayList<Object>();
		Estado estado = null;
		Conexion conexion = new Conexion();
		ResultSet rs = null;
		try {

			StringBuilder sql = new StringBuilder();
			sql.append("  SELECT a.*");
			sql.append("  FROM estados a ");
			sql.append("  WHERE 1=1");

			if (aEstado != null && aEstado.getId() != null) {
				sql.append("  AND a.id = ?");
				parametros.add(aEstado.getId());
			}

			if (aEstado != null && aEstado.getIndicativoVigencia() != null && !aEstado.getIndicativoVigencia().trim().equals("")) {
				sql.append("  AND a.indicativo_vigencia = ?");
				parametros.add(aEstado.getIndicativoVigencia().trim());
			}

			sql.append("  ORDER BY a.id ");

			rs = conexion.consultarBD(sql.toString(), parametros);

			while (rs.next()) {

				estado = new Estado();

				estado.setId((Integer) rs.getObject("id"));
				estado.setNombre((String) rs.getObject("nombre"));
				estado.setIndicativoVigencia((String) rs.getObject("indicativo_vigencia"));

				estados.add(estado);

			}

		} catch (Exception e) {
			throw new Exception(e);

		} finally {

			if (rs != null) {
				rs.close();
			}
			conexion.cerrarConexion();

		}

		return estados;

	}

	/**
	 * Obtiene un cliente por el nombre
	 * 
	 * @param aNombreCliente
	 * @return cliente
	 * @throws Exception
	 */
	public static Cliente getClientesPorNombre(String aNombreCliente) throws Exception {

		List<Object> prametros = new ArrayList<Object>();
		Cliente cliente = null;
		Conexion conexion = new Conexion();
		ResultSet rs = null;
		try {

			StringBuilder sql = new StringBuilder();
			sql.append("  SELECT *");
			sql.append("  FROM clientes p");
			sql.append("  WHERE 1=1 ");
			sql.append("  AND UPPER(p.cliente)=  ? ");

			prametros.add(aNombreCliente.trim().toUpperCase());

			rs = conexion.consultarBD(sql.toString(), prametros);

			if (rs.next()) {
				cliente = new Cliente();
				cliente.setId((Integer) rs.getObject("id"));
				cliente.setCliente((String) rs.getObject("cliente"));
				cliente.setNit((String) rs.getObject("nit"));
				cliente.setRepresentante((String) rs.getObject("representante"));
				cliente.setCorreoElectronico((String) rs.getObject("correo_electronico"));
				cliente.setClave((String) rs.getObject("clave"));
				cliente.setTelefono((String) rs.getObject("telefono"));

			}

		} catch (Exception e) {
			throw new Exception(e);

		} finally {

			if (rs != null) {
				rs.close();
			}
			conexion.cerrarConexion();

		}
		return cliente;

	}

	/**
	 * Obtiene el listado de clientes
	 * 
	 * @param aCliente
	 * @return clientes
	 * @throws Exception
	 */
	public static List<Cliente> getClientes(Cliente aCliente) throws Exception {
		List<Cliente> clientes = new ArrayList<Cliente>();
		List<Object> prametros = new ArrayList<Object>();
		Cliente cliente = null;
		Conexion conexion = new Conexion();
		ResultSet rs = null;
		try {

			StringBuilder sql = new StringBuilder();
			sql.append("  SELECT *");
			sql.append("  FROM clientes p");
			sql.append("  WHERE 1=1 ");

			if (aCliente != null && aCliente.getNit() != null && !aCliente.getNit().trim().equals("")) {
				sql.append("  AND UPPER(p.nit) LIKE  ? ");
				prametros.add("%" + aCliente.getNit().trim().toUpperCase() + "%");
			}

			if (aCliente != null && aCliente.getCliente() != null && !aCliente.getCliente().trim().equals("")) {
				sql.append("  AND UPPER(p.cliente) LIKE  ? ");
				prametros.add("%" + aCliente.getCliente().trim().toUpperCase() + "%");
			}

			if (aCliente != null && aCliente.getCorreoElectronico() != null && !aCliente.getCorreoElectronico().trim().equals("")) {
				sql.append("  AND UPPER(p.correo_electronico) = ?");
				prametros.add(aCliente.getCorreoElectronico().trim().toUpperCase());
			}

			if (aCliente != null && aCliente.getClave() != null && !aCliente.getClave().trim().equals("")) {
				sql.append("  AND p.clave = ?");
				prametros.add(aCliente.getClave().trim());
			}

			sql.append("  ORDER BY cliente");

			rs = conexion.consultarBD(sql.toString(), prametros);

			while (rs.next()) {
				cliente = new Cliente();
				cliente.setId((Integer) rs.getObject("id"));
				cliente.setCliente((String) rs.getObject("cliente"));
				cliente.setNit((String) rs.getObject("nit"));
				cliente.setRepresentante((String) rs.getObject("representante"));
				cliente.setCorreoElectronico((String) rs.getObject("correo_electronico"));
				cliente.setClave((String) rs.getObject("clave"));
				cliente.setTelefono((String) rs.getObject("telefono"));

				clientes.add(cliente);
			}

		} catch (Exception e) {
			throw new Exception(e);

		} finally {

			if (rs != null) {
				rs.close();
			}
			conexion.cerrarConexion();

		}
		return clientes;

	}

	/**
	 * Valida si un cliente existe o no de acuerdo a su nit
	 * 
	 * @param aParteA
	 * @param aParteB
	 * @return existente
	 * @throws Exception
	 */
	public static boolean getClienteExistente(String aParteA) throws Exception {
		boolean existente = false;
		List<Object> prametros = new ArrayList<Object>();
		Conexion conexion = new Conexion();
		ResultSet rs = null;
		try {

			StringBuilder sql = new StringBuilder();
			sql.append("  SELECT *");
			sql.append("  FROM clientes p");
			sql.append("  WHERE 1=1 ");

			sql.append("  AND ( UPPER(p.nit) = ?");
			sql.append("  		  OR UPPER(p.nit) LIKE ? ");
			sql.append("  		) ");

			prametros.add(aParteA.toUpperCase().trim());
			prametros.add(aParteA.toUpperCase().trim() + "-%");

			rs = conexion.consultarBD(sql.toString(), prametros);

			if (rs.next()) {
				existente = true;
			}

		} catch (Exception e) {
			throw new Exception(e);

		} finally {

			if (rs != null) {
				rs.close();
			}
			conexion.cerrarConexion();

		}
		return existente;

	}

	/**
	 * Obtiene un listado de proyectos cliente
	 * 
	 * @param aProyectoCliente
	 * @return proyectos
	 * @throws Exception
	 */
	public static List<ProyectoCliente> getProyectosCliente(ProyectoCliente aProyectoCliente) throws Exception {
		List<ProyectoCliente> proyectos = new ArrayList<ProyectoCliente>();
		List<Object> prametros = new ArrayList<Object>();
		ProyectoCliente proyecto = null;
		Conexion conexion = new Conexion();
		ResultSet rs = null;
		try {

			StringBuilder sql = new StringBuilder();
			sql.append("  SELECT p.*, c.cliente, c.nit, cr.nombres, cr.apellidos, pr.nombre, c.correo_electronico, cr.correo_electronico correo_consultor, c.representante");
			sql.append("  FROM proyectos_cliente p");
			sql.append("  INNER JOIN clientes c ON c.id = p.id_cliente");
			sql.append("  INNER JOIN proyectos pr ON pr.id = p.id_proyecto");
			sql.append("  INNER JOIN consultores cr ON cr.id = p.id_consultor");
			sql.append("  WHERE 1=1 ");

			// nuevo filtro verificar influencias en combos de otros casos de uso
			if (aProyectoCliente != null && aProyectoCliente.getId() != null) {
				sql.append("  AND p.id = ?");
				prametros.add(aProyectoCliente.getId());
			}

			if (aProyectoCliente != null && aProyectoCliente.getCliente() != null && aProyectoCliente.getCliente().getId() != null) {
				sql.append("  AND p.id_cliente = ?");
				prametros.add(aProyectoCliente.getCliente().getId());
			}

			if (aProyectoCliente != null && aProyectoCliente.getConsultor() != null && aProyectoCliente.getConsultor().getId() != null) {
				sql.append("  AND p.id_consultor = ?");
				prametros.add(aProyectoCliente.getConsultor().getId());
			}

			if (aProyectoCliente != null && aProyectoCliente.getProyecto() != null && aProyectoCliente.getProyecto().getId() != null) {
				sql.append("  AND p.id_proyecto = ?");
				prametros.add(aProyectoCliente.getProyecto().getId());
			}

			if (aProyectoCliente != null && aProyectoCliente.getFechaInicio() != null) {
				sql.append("  AND p.fecha_inicio >= ?");
				prametros.add(aProyectoCliente.getFechaInicio());
			}

			if (aProyectoCliente != null && aProyectoCliente.getFechaFin() != null) {
				sql.append("  AND p.fecha_fin <= ?");
				prametros.add(aProyectoCliente.getFechaFin());
			}

			if (aProyectoCliente != null && aProyectoCliente.gettFechaInicioCertificacion() != null) {
				sql.append("  AND p.fecha_certificacion >= ?");
				prametros.add(aProyectoCliente.getFechaInicio());
			}

			if (aProyectoCliente != null && aProyectoCliente.gettFechaFinCertificacion() != null) {
				sql.append("  AND p.fecha_certificacion <= ?");
				prametros.add(aProyectoCliente.getFechaFin());
			}

			if (aProyectoCliente != null && aProyectoCliente.gettEstadoCosnultoria() != null && aProyectoCliente.gettEstadoCosnultoria().equals(IConstantes.ACTIVO)) {
				sql.append("  AND p.fecha_inicio <= CURRENT_DATE AND  p.fecha_fin >= CURRENT_DATE");
			}

			if (aProyectoCliente != null && aProyectoCliente.gettEstadoCosnultoria() != null && aProyectoCliente.gettEstadoCosnultoria().equals(IConstantes.INACTIVO)) {
				sql.append("  AND NOT (p.fecha_inicio <= CURRENT_DATE AND  p.fecha_fin >= CURRENT_DATE)");
			}

			if (aProyectoCliente != null && aProyectoCliente.gettEstadoCertificacion() != null && aProyectoCliente.gettEstadoCertificacion().equals(IConstantes.AFIRMACION)) {
				sql.append("  AND p.fecha_certificacion IS NOT NULL");
			}
			if (aProyectoCliente != null && aProyectoCliente.gettEstadoCertificacion() != null && aProyectoCliente.gettEstadoCertificacion().equals(IConstantes.NEGACION)) {
				sql.append("  AND p.fecha_certificacion IS NULL");
			}

			sql.append("  ORDER BY p.fecha_inicio, p.fecha_fin, c.cliente");

			rs = conexion.consultarBD(sql.toString(), prametros);

			while (rs.next()) {
				proyecto = new ProyectoCliente();
				proyecto.setId((Integer) rs.getObject("id"));

				proyecto.getCliente().setId((Integer) rs.getObject("id_cliente"));
				proyecto.getCliente().setCliente((String) rs.getObject("cliente"));
				proyecto.getCliente().setCorreoElectronico((String) rs.getObject("correo_electronico"));
				proyecto.getCliente().setNit((String) rs.getObject("nit"));
				proyecto.getCliente().setRepresentante((String) rs.getObject("representante"));

				proyecto.getConsultor().setId((Integer) rs.getObject("id_consultor"));
				proyecto.getConsultor().setNombres((String) rs.getObject("nombres"));
				proyecto.getConsultor().setApellidos((String) rs.getObject("apellidos"));
				proyecto.getConsultor().setCorreoElectronico((String) rs.getObject("correo_consultor"));

				proyecto.getProyecto().setId((Integer) rs.getObject("id_proyecto"));
				proyecto.getProyecto().setNombre((String) rs.getObject("nombre"));

				proyecto.setFechaInicio((Date) rs.getObject("fecha_inicio"));
				proyecto.setFechaFin((Date) rs.getObject("fecha_fin"));
				proyecto.setFechaCertificacion((Date) rs.getObject("fecha_certificacion"));

				proyectos.add(proyecto);
			}

		} catch (Exception e) {
			throw new Exception(e);

		} finally {

			if (rs != null) {
				rs.close();
			}
			conexion.cerrarConexion();

		}
		return proyectos;

	}

	/**
	 * Obtiene los consultores por distintos criterios
	 * 
	 * @param aConsultor
	 * @return consultores
	 * @throws Exception
	 */
	public static List<Consultor> getConsultores(Consultor aConsultor) throws Exception {
		List<Consultor> consultores = new ArrayList<Consultor>();
		List<Object> prametros = new ArrayList<Object>();
		Consultor consultor = null;
		Conexion conexion = new Conexion();
		ResultSet rs = null;
		try {

			StringBuilder sql = new StringBuilder();
			sql.append("  SELECT p.*, encode(archivo::bytea, 'base64') foto_decodificada ");
			sql.append("  FROM consultores p");
			sql.append("  WHERE 1=1 ");

			if (aConsultor != null && aConsultor.getCorreoElectronico() != null && !aConsultor.getCorreoElectronico().trim().equals("")) {
				sql.append("  AND UPPER(p.correo_electronico) = ?");
				prametros.add(aConsultor.getCorreoElectronico().trim().toUpperCase());
			}

			if (aConsultor != null && aConsultor.getClave() != null && !aConsultor.getClave().trim().equals("")) {
				sql.append("  AND p.clave = ?");
				prametros.add(aConsultor.getClave().trim());
			}

			if (aConsultor != null && aConsultor.getEstadoVigencia() != null && !aConsultor.getEstadoVigencia().trim().equals("")) {
				sql.append("  AND p.estado_vigencia = ?");
				prametros.add(aConsultor.getEstadoVigencia().trim());
			}

			if (aConsultor != null && aConsultor.getFechaInicio() != null) {
				sql.append("  AND p.fecha_inicio >= ?");
				prametros.add(aConsultor.getFechaInicio());
			}

			if (aConsultor != null && aConsultor.getFechaFin() != null) {
				sql.append("  AND p.fecha_fin <= ?");
				prametros.add(aConsultor.getFechaFin());
			}

			sql.append("  ORDER BY nombres, apellidos");

			rs = conexion.consultarBD(sql.toString(), prametros);

			while (rs.next()) {
				consultor = new Consultor();
				consultor.setId((Integer) rs.getObject("id"));
				consultor.setNombres((String) rs.getObject("nombres"));
				consultor.setApellidos((String) rs.getObject("apellidos"));
				consultor.setCorreoElectronico((String) rs.getObject("correo_electronico"));
				consultor.setClave((String) rs.getObject("clave"));
				consultor.setEstadoVigencia((String) rs.getObject("estado_vigencia"));
				consultor.setTelefono((String) rs.getObject("telefono"));
				consultor.setPerfil((String) rs.getObject("perfil"));
				consultor.setFechaInicio((Date) rs.getObject("fecha_inicio"));
				consultor.setFechaFin((Date) rs.getObject("fecha_fin"));
				consultor.settFotoDecodificada((String) rs.getObject("foto_decodificada"));
				consultor.setArchivo(rs.getBytes("archivo"));

				consultores.add(consultor);
			}

		} catch (Exception e) {
			throw new Exception(e);

		} finally {

			if (rs != null) {
				rs.close();
			}
			conexion.cerrarConexion();

		}
		return consultores;

	}

	/**
	 * Obtiene un listado de administradores del software
	 * 
	 * @param aPersonal
	 * @return administradores
	 * @throws Exception
	 */
	public static List<Personal> getAdministradores(Personal aPersonal) throws Exception {
		List<Personal> administradores = new ArrayList<Personal>();
		List<Object> prametros = new ArrayList<Object>();
		Personal administrador = null;
		Conexion conexion = new Conexion();
		ResultSet rs = null;
		try {

			StringBuilder sql = new StringBuilder();
			sql.append("  SELECT *");
			sql.append("  FROM personal p");
			sql.append("  WHERE 1=1 ");

			if (aPersonal != null && aPersonal.getCorreoElectronico() != null && !aPersonal.getCorreoElectronico().trim().equals("")) {
				sql.append("  AND UPPER(p.correo_electronico) = ?");
				prametros.add(aPersonal.getCorreoElectronico().trim().toUpperCase());
			}

			if (aPersonal != null && aPersonal.getClave() != null && !aPersonal.getClave().trim().equals("")) {
				sql.append("  AND p.clave = ?");
				prametros.add(aPersonal.getClave().trim());
			}

			if (aPersonal != null && aPersonal.getEstadoVigencia() != null && !aPersonal.getEstadoVigencia().trim().equals("")) {
				sql.append("  AND p.estado_vigencia = ?");
				prametros.add(aPersonal.getEstadoVigencia().trim());
			}

			sql.append("  ORDER BY nombres, apellidos");

			rs = conexion.consultarBD(sql.toString(), prametros);

			while (rs.next()) {
				administrador = new Personal();
				administrador.setId((Integer) rs.getObject("id"));
				administrador.setNombres((String) rs.getObject("nombres"));
				administrador.setApellidos((String) rs.getObject("apellidos"));
				administrador.setCorreoElectronico((String) rs.getObject("correo_electronico"));
				administrador.setClave((String) rs.getObject("clave"));
				administrador.setEstadoVigencia((String) rs.getObject("estado_vigencia"));

				administradores.add(administrador);
			}

		} catch (Exception e) {
			throw new Exception(e);

		} finally {

			if (rs != null) {
				rs.close();
			}
			conexion.cerrarConexion();

		}
		return administradores;

	}

}
