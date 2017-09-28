package consultoria.generales;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TimeZone;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.primefaces.context.RequestContext;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;

@ManagedBean
public class ConsultarFuncionesAPI implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -765747957311162349L;

	// PRIVADOS

	// Fecha actual colombia
	public Date getFechaActualGmtColombia() {
		Date fecha = null;
		try {
			SimpleDateFormat dateFormatGmt = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
			dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT-5"));

			// Local time zone
			SimpleDateFormat dateFormatLocal = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");

			fecha = dateFormatLocal.parse(dateFormatGmt.format(new Date()));
		} catch (Exception e) {

		}
		// Time in GMT
		return fecha;
	}

	/**
	 * Obtiene la fecha con dias sumados o restados
	 * 
	 * @param aFecha
	 * @param aDias
	 * @return calendar.getTime();
	 */
	public Date getFechaDiasSumados(Date aFecha, int aDias) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(aFecha);
		// calendar.add(Calendar.DAY_OF_YEAR, aDias);
		calendar.add(Calendar.DATE, aDias);
		return calendar.getTime();

	}

	/**
	 * Obtiene la fecha y sumarle el máximo de dias el mes de su MES
	 * 
	 * @param aFecha
	 * @param aDias
	 * @return calendar.getTime();
	 */
	public Date getFechaDiasSumados(Date aFecha) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(aFecha);
		calendar.add(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		return calendar.getTime();

	}

	/**
	 * Obtiene la ruta de un listado
	 * 
	 * @param aPosibleRuta
	 * @return path
	 */
	public String getPath(String aPosibleRuta) {
		String path = "";
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
		path = request.getRealPath(aPosibleRuta);
		return path;
	}

	// PUBLICOS

	/**
	 * Guarda la imagen en disco para luego ser leída
	 * 
	 * @param aNombre
	 * @param aSartaBase64
	 */
	public void guardarImagenEnDisco(Integer aIdFoto, String aSartaBase64, String aSubcarpeta) {

		try {
			File outputfile = new File(this.getPath(IConstantes.PAQUETE_IMAGENES) + "/" + aSubcarpeta + "/foto" + aIdFoto + ".png");
			if (!outputfile.exists()) {
				byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(aSartaBase64);
				BufferedImage img = ImageIO.read(new ByteArrayInputStream(imageBytes));
				ImageIO.write(img, "png", outputfile);
			}

		} catch (Exception e) {
			IConstantes.log.error(e, e);

		}

	}

	/**
	 * Obtiene el reporte en pdf y/o excel segun se quiera
	 * 
	 * @param aDataSource
	 * @param aReporte
	 * @param aNombreReporte
	 * @param aTipoReporte
	 */
	public void generarListado(JRBeanCollectionDataSource aDataSource, String aReporte, String aNombreReporte, String aTipoReporte, Map<String, Object> aParametros) {

		try {

			byte[] bytes = null;
			JasperPrint print = null;

			if (aReporte != null && !aReporte.trim().equals("")) {

				if (!(aNombreReporte != null && !aNombreReporte.trim().equals(""))) {
					aNombreReporte = "REPORTE";
				}

				if (aParametros == null) {

					aParametros = new HashMap<String, Object>();

				}

				FacesContext context = FacesContext.getCurrentInstance();
				ExternalContext ext = context.getExternalContext();

				if (aDataSource != null) {

					if (aTipoReporte == null || (aTipoReporte != null && aTipoReporte.equals("pdf"))) {

						bytes = JasperRunManager.runReportToPdf(this.getPath(IConstantes.PAQUETE_MODULO_REPORTES + aReporte), aParametros, aDataSource);

					} else {

						print = JasperFillManager.fillReport(this.getPath(IConstantes.PAQUETE_MODULO_REPORTES + aReporte), aParametros, aDataSource);

					}

				} else {

					if (aTipoReporte == null || (aTipoReporte != null && aTipoReporte.equals("pdf"))) {

						bytes = JasperRunManager.runReportToPdf(this.getPath(IConstantes.PAQUETE_MODULO_REPORTES + aReporte), aParametros, new JREmptyDataSource());

					} else {

						print = JasperFillManager.fillReport(this.getPath(IConstantes.PAQUETE_MODULO_REPORTES + aReporte), aParametros, new JREmptyDataSource());

					}
				}

				if (aTipoReporte == null || (aTipoReporte != null && aTipoReporte.equals("pdf"))) {

					HttpServletResponse response = (HttpServletResponse) ext.getResponse();
					response.setContentType("application/pdf");
					response.setHeader("Content-Disposition", "attachment; filename=" + aNombreReporte.trim().toUpperCase() + ".pdf");
					response.setContentLength(bytes.length);
					ServletOutputStream servletOutputStream = response.getOutputStream();

					servletOutputStream.write(bytes, 0, bytes.length);

					servletOutputStream.flush();
					servletOutputStream.close();
					FacesContext.getCurrentInstance().responseComplete();

				} else {

					ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
					JRXlsExporter exporterXLS = new JRXlsExporter();

					exporterXLS.setParameter(JRXlsExporterParameter.JASPER_PRINT, print);
					exporterXLS.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, arrayOutputStream);
					exporterXLS.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
					exporterXLS.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
					exporterXLS.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
					exporterXLS.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);

					exporterXLS.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS, Boolean.TRUE);

					exporterXLS.exportReport();

					HttpServletResponse response = (HttpServletResponse) ext.getResponse();
					response.setContentType("application/vnd.ms-excel");
					response.setHeader("Content-Disposition", "attachment; filename=" + aNombreReporte.trim().toUpperCase() + ".xls");
					response.setContentLength(arrayOutputStream.toByteArray().length);
					ServletOutputStream servletOutputStream = response.getOutputStream();

					servletOutputStream.write(arrayOutputStream.toByteArray(), 0, arrayOutputStream.toByteArray().length);

					servletOutputStream.flush();
					servletOutputStream.close();
					FacesContext.getCurrentInstance().responseComplete();

				}

			} else {

				// this.statusMessages.addToControl("mensajes", Severity.WARN,
				// FormatoWeb.get("modRepImposibleReporte", false));
			}

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}

	}

	/**
	 * Obtiene el mes y el año
	 * 
	 * @param aFecha
	 * @return aFecha
	 */
	public String getMesAnoF2(Date aFecha) {
		SimpleDateFormat formato = new SimpleDateFormat("MMM yyyy");
		return aFecha != null ? formato.format(aFecha) : "";

	}

	/**
	 * Obtiene el mes y el año
	 * 
	 * @param aFecha
	 * @return aFecha
	 */
	public String getMesAno(Date aFecha) {
		SimpleDateFormat formato = new SimpleDateFormat("MMMM 'de' yyyy");
		return aFecha != null ? formato.format(aFecha) : "";

	}

	/**
	 * Obtiene la fecha en formato Colombia
	 * 
	 * @param aFecha
	 * @return aFecha
	 */
	public String getFechaColombia(Date aFecha) {
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
		return aFecha != null ? formato.format(aFecha) : "";

	}

	/**
	 * Obtiene la fecha y la hora formateada
	 * 
	 * @return aFechaHora
	 */
	public String getFechaHoraColombia(Date aFechaHora) {
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
		return aFechaHora != null ? formato.format(aFechaHora) : "";

	}

	/**
	 * Abre un diálogo modal
	 * 
	 * @param aVariableWidgetDelModal
	 */
	public void abrirModal(String aVariableWidgetDelModal) {
		try {

			RequestContext.getCurrentInstance().execute("PF('" + aVariableWidgetDelModal + "').show()");

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
	}

	/**
	 * Cierra un diálogo modal
	 * 
	 * @param aVariableWidgetDelModal
	 */
	public void cerrarModal(String aVariableWidgetDelModal) {
		try {

			RequestContext.getCurrentInstance().execute("PF('" + aVariableWidgetDelModal + "').hide()");

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
	}

	/**
	 * Obtiene un mensaje del properties para poderlo mostrar en capa de
	 * presentacion
	 * 
	 * @param aId
	 * @return mensaje
	 */
	public String getMensaje(String aId) {
		String mensaje = "[[" + aId + "]]";
		try {
			FacesContext context = FacesContext.getCurrentInstance();
			ResourceBundle bundle = context.getApplication().getResourceBundle(context, "mensaje");
			if (bundle != null) {
				mensaje = bundle.getString(aId);
			}

		} catch (Exception e) {

		}

		return mensaje;
	}

	/**
	 * Obtiene un mensaje del properties para poderlo mostrar en capa de
	 * presentacion con muchos argumentos como parámetros
	 * 
	 * @param aId
	 * @return mensaje
	 */
	public String getMensaje(String aId, String... aArgumentos) {
		String mensaje = aId;
		try {
			FacesContext context = FacesContext.getCurrentInstance();
			ResourceBundle bundle = context.getApplication().getResourceBundle(context, "mensaje");
			if (bundle != null) {
				mensaje = bundle.getString(aId);
				mensaje = MessageFormat.format(mensaje, aArgumentos);
			}

		} catch (Exception e) {

		}

		return mensaje;
	}

	/**
	 * Muestra un mensaje personalizado en pantalla
	 * 
	 * @param aSarta
	 * @param aTipo
	 */
	public void mostrarMensajeGlobalPersonalizado(String aSarta, String aTipo) {
		FacesContext context = FacesContext.getCurrentInstance();
		if (aTipo == null || (aTipo != null && aTipo.equals("exito"))) {

			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, this.getMensaje("INFORMACION"), aSarta));

		} else if (aTipo == null || (aTipo != null && aTipo.equals("advertencia"))) {

			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, this.getMensaje("INFORMACION"), aSarta));

		} else {

			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, this.getMensaje("INFORMACION"), aSarta));
		}
	}

	/**
	 * Muestra un mensaje en pantalla
	 * 
	 * @param aIdProperties
	 * @param aTipo
	 */
	public void mostrarMensajeGlobal(String aIdProperties, String aTipo) {
		FacesContext context = FacesContext.getCurrentInstance();
		if (aTipo == null || (aTipo != null && aTipo.equals("exito"))) {

			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, this.getMensaje("INFORMACION"), this.getMensaje(aIdProperties)));

		} else if (aTipo == null || (aTipo != null && aTipo.equals("advertencia"))) {

			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, this.getMensaje("INFORMACION"), this.getMensaje(aIdProperties)));

		} else {

			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, this.getMensaje("INFORMACION"), this.getMensaje(aIdProperties)));
		}
	}

	/**
	 * Convierte una fecha a java.util
	 * 
	 * @param aFecha
	 * @return fechaCasteada
	 */
	public Date getFechaJavaUtil(Object aFecha) {
		Date fechaCasteada = null;
		try {
			SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");

			if (aFecha != null) {

				fechaCasteada = formato.parse(((String) aFecha));

			}
		} catch (Exception e) {

		}
		return fechaCasteada;
	}

	/**
	 * Obtiene una moneda con formato
	 * 
	 * @param aMoneda
	 * @param aValor
	 * @return textoSalida
	 */
	public String getMoneda(BigDecimal aValor) {
		String textoSalida = "";
		DecimalFormat formato = new DecimalFormat("###,###.00");
		if (aValor != null) {
			textoSalida = formato.format(aValor);
		}
		return "$ " + textoSalida;

	}

	/**
	 * Determina si un valor es menor o igual a cero
	 * 
	 * @param aValor
	 * @return ok
	 */
	public boolean isMenorIgualCero(BigDecimal aValor) {
		boolean ok = false;
		if (aValor != null && aValor.compareTo(new BigDecimal(0)) <= 0) {
			ok = true;
		}
		return ok;
	}

	/**
	 * Determina si es menor a cero
	 * 
	 * @param aValor
	 * @return ok
	 */
	public boolean isMenorCero(BigDecimal aValor) {
		boolean ok = false;
		if (aValor != null && aValor.compareTo(new BigDecimal(0)) < 0) {
			ok = true;
		}
		return ok;
	}

	/**
	 * Redondea un valor a n decimales
	 * 
	 * @param aValor
	 * @param aNumeroDecimales
	 * @return valor
	 */
	public BigDecimal getValorRedondeado(BigDecimal aValor, Integer aNumeroDecimales) {

		BigDecimal valor = new BigDecimal(0);
		try {

			valor = aValor.setScale(aNumeroDecimales, RoundingMode.HALF_UP);

		} catch (Exception e) {
			valor = new BigDecimal(0);
		}

		return valor;
	}

	/**
	 * Valida si una sarta esta vacía o no
	 * 
	 * @param aSarta
	 * @return vacio
	 */
	public boolean isVacio(String aSarta) {
		boolean vacio = true;
		if (aSarta != null && !aSarta.trim().equals("")) {
			vacio = false;
		}
		return vacio;
	}

	/**
	 * Retorna la sarta en mayúscula y sin espacios.
	 * 
	 * @param aSarta
	 * @return sarta
	 */
	public String getMayusculaSinEspacios(String aSarta) {
		String sarta = null;
		if (aSarta != null) {
			sarta = aSarta.toUpperCase().trim();
		}
		return sarta;

	}

	/**
	 * Retorna la sarta sin espacios.
	 * 
	 * @param aSarta
	 * @return sarta
	 */
	public String getSinEspacios(String aSarta) {
		String sarta = null;
		if (aSarta != null) {
			sarta = aSarta.trim();
		}
		return sarta;

	}

	/**
	 * Obtiene fselecitems de estados
	 * 
	 * @return itemsEstados
	 */
	public List<SelectItem> getItemsEstados() {
		List<SelectItem> itemsEstados = new ArrayList<SelectItem>();
		itemsEstados.add(new SelectItem("", this.getMensaje("comboVacio")));
		itemsEstados.add(new SelectItem(IConstantes.ACTIVO, this.getMensaje("ESTADO_ACTIVO")));
		itemsEstados.add(new SelectItem(IConstantes.INACTIVO, this.getMensaje("ESTADO_INACTIVO")));

		return itemsEstados;
	}

}
