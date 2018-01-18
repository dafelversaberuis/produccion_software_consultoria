package consultoria.modulos.tablasSoporte;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.ReorderEvent;
import org.primefaces.event.SelectEvent;

import com.csvreader.CsvReader;

import consultoria.Conexion;
import consultoria.beans.Cliente;
import consultoria.beans.Consultor;
import consultoria.beans.Diagnostico;
import consultoria.beans.DocumentoActividad;
import consultoria.beans.Estado;
import consultoria.beans.EstadoDiagnostico;
import consultoria.beans.EstadoProyectoCliente;
import consultoria.beans.Iva;
import consultoria.beans.ParametroAuditoria;
import consultoria.beans.Plan;
import consultoria.beans.PreguntaProyecto;
import consultoria.beans.Proyecto;
import consultoria.beans.ProyectoCliente;
import consultoria.beans.TareaProyecto;
import consultoria.generales.ConsultarFuncionesAPI;
import consultoria.generales.Estadistica;
import consultoria.generales.GraficoEstadistica;
import consultoria.generales.IConstantes;
import consultoria.modulos.IConsultasDAO;
import consultoria.modulos.personal.AdministrarSesionCliente;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@ManagedBean
@ViewScoped
public class HacerMantenimiento extends ConsultarFuncionesAPI implements Serializable {

	/**
	 * 
	 */
	private static final long					serialVersionUID	= 5700817004191801685L;

	// inyecta el bean de sesion
	@ManagedProperty(value = "#{administrarSesionCliente}")
	private AdministrarSesionCliente	administrarSesionCliente;

	private Plan											arbolito;
	private Plan											arbolitoTransaccion;

	private String										vistaActual;

	private Estado										estado;
	private Estado										estadoTransaccion;

	private ParametroAuditoria				parametroAuditoria;

	private Proyecto									proyecto;
	private Proyecto									proyectoTransaccion;
	private PreguntaProyecto					preguntaProyecto;
	private PreguntaProyecto					preguntaProyectoTransaccion;
	private TareaProyecto							tareaProyecto;
	private TareaProyecto							tareaProyectoTransaccion;

	private DocumentoActividad				documentoActividad;
	private DocumentoActividad				documentoActividadTransaccion;

	private ProyectoCliente						proyectoClienteConsulta;
	private ProyectoCliente						proyectoCliente;
	private ProyectoCliente						proyectoClienteTransaccion;

	private Iva												iva;
	private Iva												ivaTransaccion;

	private List<Estado>							estados;
	private List<Proyecto>						proyectos;
	private List<ProyectoCliente>			proyectosCliente;
	private List<PreguntaProyecto>		preguntas;
	private List<TareaProyecto>				tareas;
	private List<DocumentoActividad>	documentos;
	private List<Iva>									ivas;
	private List<SelectItem>					itemsIva;
	private List<Plan>								arbolitos;
	private List<TareaProyecto>				tareasArchivo;

	private List<SelectItem>					itemsProyectos;
	private List<SelectItem>					itemsConsultores;
	private List<SelectItem>					itemsProyectosEdicion;
	private List<SelectItem>					itemsConsultoresEdicion;

	private List<Estadistica>					radicadosAuditoria;

	// privados

	/**
	 * Valida un administrador
	 *
	 * @param aTransaccion
	 * @return ok
	 */
	private boolean isValidoArbolito(String aTransaccion) {
		boolean ok = true;

		if (aTransaccion.equals("C")) {

			if (this.isVacio(this.arbolito.getNombre())) {
				ok = false;
				this.mostrarMensajeGlobalPersonalizado("ESCRIBA UN NOMBRE DEL PLAN", "advertencia");
			}

		} else {

			if (this.isVacio(this.arbolitoTransaccion.getNombre())) {
				ok = false;
				this.mostrarMensajeGlobalPersonalizado("ESCRIBA UN NOMBRE DEL PLAN", "advertencia");
			}

		}

		return ok;
	}

	/**
	 * Calcula los precios con iva para mostrarlos en pantalla
	 * 
	 * @param aTransaccion
	 */
	public void calcularPrecios(String aTransaccion) {

		try {
			List<Iva> ivas = null;
			if (aTransaccion.equals("C")) {

				if (this.arbolito != null && this.arbolito.getIva() != null && this.arbolito.getIva().getId() != null) {

					ivas = IConsultasDAO.getIvas(this.arbolito.getIva());
					this.arbolito.getIva().setValorIva(ivas.get(0).getValorIva());

					if (this.arbolito.getPrecioVentaPesos() != null) {

						this.arbolito.setIvaPesos(this.getValorRedondeado(this.arbolito.getPrecioVentaPesos().multiply(this.arbolito.getIva().getValorIva()).divide(new BigDecimal(100)), IConstantes.DECIMALES_REDONDEAR));
						this.arbolito.setPrecioVentaPesosConIva(this.getValorRedondeado(this.arbolito.getPrecioVentaPesos().add(this.arbolito.getIvaPesos()), IConstantes.DECIMALES_REDONDEAR));

					} else {
						this.arbolito.setIvaPesos(null);
						this.arbolito.setPrecioVentaPesosConIva(null);

					}

				} else {

					this.arbolito.setIvaPesos(null);
					this.arbolito.setPrecioVentaPesosConIva(null);

				}

			} else {

				if (this.arbolitoTransaccion != null && this.arbolitoTransaccion.getIva() != null && this.arbolitoTransaccion.getIva().getId() != null) {

					ivas = IConsultasDAO.getIvas(this.arbolitoTransaccion.getIva());
					this.arbolitoTransaccion.getIva().setValorIva(ivas.get(0).getValorIva());

					if (this.arbolitoTransaccion.getPrecioVentaPesos() != null) {

						this.arbolitoTransaccion.setIvaPesos(this.getValorRedondeado(this.arbolitoTransaccion.getPrecioVentaPesos().multiply(this.arbolitoTransaccion.getIva().getValorIva()).divide(new BigDecimal(100)), IConstantes.DECIMALES_REDONDEAR));
						this.arbolitoTransaccion.setPrecioVentaPesosConIva(this.getValorRedondeado(this.arbolitoTransaccion.getPrecioVentaPesos().add(this.arbolitoTransaccion.getIvaPesos()), IConstantes.DECIMALES_REDONDEAR));

					} else {
						this.arbolitoTransaccion.setIvaPesos(null);
						this.arbolitoTransaccion.setPrecioVentaPesosConIva(null);

					}

				} else {

					this.arbolitoTransaccion.setIvaPesos(null);
					this.arbolitoTransaccion.setPrecioVentaPesosConIva(null);

				}
			}

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}

	}

	/**
	 * Crea un nuevo administrador del software
	 */
	public void crearArbolito() {
		Conexion conexion = new Conexion();

		try {
			if (isValidoArbolito("C")) {

				List<Iva> ivas = IConsultasDAO.getIvas(this.arbolito.getIva());
				if (ivas != null && ivas.size() > 0) {

					conexion.setAutoCommitBD(false);

					this.arbolito.setNombre(this.getSinEspacios(this.arbolito.getNombre()));

					// Cï¿½LCULOS CON IVA INCLUIDO

					this.arbolito.getIva().setValorIva(ivas.get(0).getValorIva());

					this.arbolito.setIvaPesos(this.getValorRedondeado(this.arbolito.getPrecioVentaPesos().multiply(this.arbolito.getIva().getValorIva()).divide(new BigDecimal(100)), IConstantes.DECIMALES_REDONDEAR));
					this.arbolito.setPrecioVentaPesosConIva(this.getValorRedondeado(this.arbolito.getPrecioVentaPesos().add(this.arbolito.getIvaPesos()), IConstantes.DECIMALES_REDONDEAR));

					this.arbolito.getCamposBD();

					conexion.insertarBD(this.arbolito.getEstructuraTabla().getTabla(), this.arbolito.getEstructuraTabla().getPersistencia());
					conexion.commitBD();

					this.mostrarMensajeGlobal("creacionExitosa", "exito");

					// reseteo de variables
					this.arbolito = null;
					this.getArbolito();
					this.arbolitos = null;
					this.getArbolitos();

				} else {
					this.mostrarMensajeGlobal("transaccionFallida", "error");

				}
			}

		} catch (Exception e) {
			conexion.rollbackBD();
			this.mostrarMensajeGlobal("transaccionFallida", "error");
		} finally {
			conexion.cerrarConexion();
		}

	}

	/**
	 * Edita un registro de arbolito
	 */
	public void editarArbolito() {
		Conexion conexion = new Conexion();

		try {
			if (isValidoArbolito("E")) {

				List<Iva> ivas = IConsultasDAO.getIvas(this.arbolitoTransaccion.getIva());
				if (ivas != null && ivas.size() > 0) {
					conexion.setAutoCommitBD(false);

					this.arbolitoTransaccion.setNombre(this.getSinEspacios(this.arbolitoTransaccion.getNombre()));

					this.arbolitoTransaccion.getIva().setValorIva(ivas.get(0).getValorIva());

					this.arbolitoTransaccion.setIvaPesos(this.getValorRedondeado(this.arbolitoTransaccion.getPrecioVentaPesos().multiply(this.arbolitoTransaccion.getIva().getValorIva()).divide(new BigDecimal(100)), IConstantes.DECIMALES_REDONDEAR));
					this.arbolitoTransaccion.setPrecioVentaPesosConIva(this.getValorRedondeado(this.arbolitoTransaccion.getPrecioVentaPesos().add(this.arbolitoTransaccion.getIvaPesos()), IConstantes.DECIMALES_REDONDEAR));

					this.arbolitoTransaccion.getCamposBD();

					conexion.actualizarBD(this.arbolitoTransaccion.getEstructuraTabla().getTabla(), this.arbolitoTransaccion.getEstructuraTabla().getPersistencia(), this.arbolitoTransaccion.getEstructuraTabla().getLlavePrimaria(), null);
					conexion.commitBD();
					this.mostrarMensajeGlobal("edicionExitosa", "exito");
					this.cerrarModal("panelEdicionArbolito");

					// reseteo de variables
					this.arbolitoTransaccion = null;
					this.getArbolitoTransaccion();
					this.arbolitos = null;
					this.getArbolitos();

				} else {
					this.mostrarMensajeGlobal("transaccionFallida", "error");
				}
			}

		} catch (Exception e) {
			conexion.rollbackBD();
			this.mostrarMensajeGlobal("transaccionFallida", "error");
		} finally {
			conexion.cerrarConexion();
		}

	}

	/**
	 * Elimina un registro de arbolito
	 */
	public void eliminarArbolito() {

		Conexion conexion = new Conexion();
		try {

			conexion.setAutoCommitBD(false);
			this.arbolitoTransaccion.getCamposBD();
			conexion.eliminarBD(this.arbolitoTransaccion.getEstructuraTabla().getTabla(), this.arbolitoTransaccion.getEstructuraTabla().getLlavePrimaria());
			conexion.commitBD();
			this.mostrarMensajeGlobal("eliminacionExitosa", "exito");

		} catch (Exception e) {
			conexion.rollbackBD();
			this.mostrarMensajeGlobal("transaccionFallida", "error");
			this.mostrarMensajeGlobal("eliminacionFallida", "error");

		} finally {
			conexion.cerrarConexion();
		}

		// reseteo de variables
		this.arbolitoTransaccion = null;
		this.arbolitos = null;
		this.getArbolitos();

	}

	/**
	 * Este mï¿½todo borra el formulario de creaciï¿½n de un arbolito
	 */
	public void cancelarArbolito() {

		try {
			this.arbolito = new Plan();

			this.arbolitos = null;
			this.getArbolitos();
		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}

	}

	/**
	 * Este mï¿½todo borra el formulario de ediciï¿½n de un arbolito en
	 * transacciï¿½n
	 */
	public void cancelarArbolitoTransaccion(String aVista) {
		try {

			this.arbolitoTransaccion = new Plan();
			this.arbolitos = null;

			this.getArbolitos();

			if (aVista != null && aVista.equals("MODAL_EDITAR_ARBOLITO")) {
				this.cerrarModal("panelEdicionArbolito");

			} else if (aVista != null && aVista.equals("MODAL_ELIMINAR_ARBOLITO")) {
				this.cerrarModal("panelEliminacionArbolito");

			} else {
				this.vistaActual = null;

			}

		} catch (Exception e) {

			IConstantes.log.error(e, e);

		}

	}

	/**
	 * Asigna un arbolito para realizar una acciï¿½n
	 * 
	 * @param aArbolito
	 * @param aVista
	 */
	public void asignarArbolito(Plan aArbolito, String aVista) {

		try {

			this.arbolitoTransaccion = aArbolito;

			if (aVista != null && aVista.equals("VISTA_FOTOS")) {
				this.vistaActual = aVista;

			} else if (aVista != null && aVista.equals("MODAL_EDITAR_ARBOLITO")) {
				this.abrirModal("panelEdicionArbolito");

			} else {

				this.abrirModal("panelEliminacionArbolito");

			}

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}

	}

	/**
	 * Obtiene un listado de arbolitos
	 * 
	 * @return arbolitos
	 */
	public List<Plan> getArbolitos() {
		try {
			if (this.arbolitos == null) {

				this.arbolitos = IConsultasDAO.getArbolitos(null);

			}
		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
		return arbolitos;
	}

	/**
	 * Establece un listado de arbolitos
	 * 
	 * @param arbolitos
	 */
	public void setArbolitos(List<Plan> arbolitos) {
		this.arbolitos = arbolitos;
	}

	public Plan getArbolito() {
		try {
			if (this.arbolito == null) {
				this.arbolito = new Plan();
			}
		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
		return arbolito;
	}

	public void setArbolito(Plan arbolito) {
		this.arbolito = arbolito;
	}

	public Plan getArbolitoTransaccion() {
		try {
			if (this.arbolitoTransaccion == null) {
				this.arbolitoTransaccion = new Plan();
			}
		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
		return arbolitoTransaccion;
	}

	public void setArbolitoTransaccion(Plan arbolitoTransaccion) {
		this.arbolitoTransaccion = arbolitoTransaccion;
	}

	/**
	 * Validaciones de iva
	 * 
	 * @param aTransaccion
	 * @return ok
	 */
	private boolean isValidoIva(String aTransaccion) {
		boolean ok = true;

		if (aTransaccion.equals("C")) {
			if (this.ivas != null && this.ivas.size() > 0 && this.ivas.stream().anyMatch(i -> i.getValorIva().equals(this.iva.getValorIva()))) {
				ok = false;
				this.mostrarMensajeGlobalPersonalizado("YA EXISTE EL VALOR DEL IVA", "advertencia");
			}
			if (this.isVacio(this.iva.getNombre())) {
				ok = false;
				this.mostrarMensajeGlobalPersonalizado("NOMBRE VACIO", "advertencia");
			}
		} else {
			if (this.ivas != null && this.ivas.size() > 0 && this.ivas.stream().anyMatch(i -> i.getId() != this.ivaTransaccion.getId() && i.getValorIva().equals(this.ivaTransaccion.getValorIva()))) {
				ok = false;
				this.mostrarMensajeGlobal("YA EXISTE EL VALOR DEL IVA", "advertencia");
			}
			if (this.isVacio(this.ivaTransaccion.getNombre())) {
				ok = false;
				this.mostrarMensajeGlobal("NOMBRE VACIO", "advertencia");
			}
		}

		return ok;
	}

	/**
	 * Crea un nuevo registro de iva
	 */
	public void crearIva() {
		Conexion conexion = new Conexion();

		try {
			if (isValidoIva("C")) {
				conexion.setAutoCommitBD(false);

				this.iva.setNombre(this.getSinEspacios(this.iva.getNombre()));

				this.iva.getCamposBD();
				conexion.insertarBD(this.iva.getEstructuraTabla().getTabla(), this.iva.getEstructuraTabla().getPersistencia());
				conexion.commitBD();
				this.mostrarMensajeGlobal("creacionExitosa", "exito");

				// reseteo de variables
				this.iva = null;
				this.getIva();
				this.ivas = null;
				this.getIvas();
			}

		} catch (Exception e) {
			conexion.rollbackBD();
			this.mostrarMensajeGlobal("transaccionFallida", "error");
		} finally {
			conexion.cerrarConexion();
		}

	}

	/**
	 * Edita un registro de iva
	 */
	public void editarIva() {
		Conexion conexion = new Conexion();
		Plan arbolito = null;

		List<Plan> arbolitos = null;

		try {
			if (isValidoIva("E")) {
				conexion.setAutoCommitBD(false);
				this.ivaTransaccion.setNombre(this.getSinEspacios(this.ivaTransaccion.getNombre()));

				this.ivaTransaccion.getCamposBD();
				conexion.actualizarBD(this.ivaTransaccion.getEstructuraTabla().getTabla(), this.ivaTransaccion.getEstructuraTabla().getPersistencia(), this.ivaTransaccion.getEstructuraTabla().getLlavePrimaria(), null);

				// actualiza los precios de los planes existentes, pues
				arbolito = new Plan();
				arbolito.setIva(this.ivaTransaccion);
				arbolitos = IConsultasDAO.getArbolitos(arbolito);

				if (arbolitos != null && arbolitos.size() > 0) {
					for (Plan a : arbolitos) {

						// puede ser que se cambiï¿½ el valor del iva
						a.getIva().setValorIva(this.ivaTransaccion.getValorIva());

						a.setIvaPesos(this.getValorRedondeado(a.getPrecioVentaPesos().multiply(a.getIva().getValorIva()).divide(new BigDecimal(100)), IConstantes.DECIMALES_REDONDEAR));
						a.setPrecioVentaPesosConIva(this.getValorRedondeado(a.getPrecioVentaPesos().add(a.getIvaPesos()), IConstantes.DECIMALES_REDONDEAR));

						Map<String, Object> camposActualizar = new HashMap<String, Object>();
						camposActualizar.put("valor_iva_cop", a.getIvaPesos());
						camposActualizar.put("precio_cop_con_iva", a.getPrecioVentaPesosConIva());
						a.getCamposBD();
						conexion.actualizarBD(a.getEstructuraTabla().getTabla(), camposActualizar, a.getEstructuraTabla().getLlavePrimaria(), null);

					}
				}

				conexion.commitBD();
				this.mostrarMensajeGlobal("edicionExitosa", "exito");
				this.cerrarModal("panelEdicionIva");

				// reseteo de variables
				this.ivaTransaccion = null;
				this.getIvaTransaccion();
				this.ivas = null;
				this.getIvas();
			}

		} catch (Exception e) {
			conexion.rollbackBD();
			this.mostrarMensajeGlobal("transaccionFallida", "error");
		} finally {
			conexion.cerrarConexion();
		}

	}

	/**
	 * Elimina un registro de iva
	 */
	public void eliminarIva() {

		Conexion conexion = new Conexion();
		try {

			conexion.setAutoCommitBD(false);
			this.ivaTransaccion.getCamposBD();
			conexion.eliminarBD(this.ivaTransaccion.getEstructuraTabla().getTabla(), this.ivaTransaccion.getEstructuraTabla().getLlavePrimaria());
			conexion.commitBD();
			this.mostrarMensajeGlobal("eliminacionExitosa", "exito");

		} catch (Exception e) {
			conexion.rollbackBD();
			this.mostrarMensajeGlobal("transaccionFallida", "error");
			this.mostrarMensajeGlobal("eliminacionFallida", "error");

		} finally {
			conexion.cerrarConexion();
		}

		// reseteo de variables
		this.ivaTransaccion = null;
		this.ivas = null;
		this.getIvas();

	}

	/**
	 * Este mï¿½todo borra el formulario de creaciï¿½n de un iva
	 */
	public void cancelarIva() {

		try {
			this.iva = new Iva();

			this.ivas = null;
			this.getIvas();
		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}

	}

	/**
	 * Este mï¿½todo borra el formulario de ediciï¿½n de un iva en transacciï¿½n
	 */
	public void cancelarIvaTransaccion(String aVista) {
		try {

			this.ivaTransaccion = new Iva();
			this.ivas = null;
			this.getIvas();

			if (aVista != null && aVista.equals("MODAL_EDITAR_IVA")) {
				this.cerrarModal("panelEdicionIva");

			} else if (aVista != null && aVista.equals("MODAL_ELIMINAR_IVA")) {
				this.cerrarModal("panelEliminacionIva");

			}

		} catch (Exception e) {

			IConstantes.log.error(e, e);

		}

	}

	/**
	 * Asigna un iva para realizar una acciï¿½n
	 * 
	 * @param aAgrupador
	 * @param aVista
	 */
	public void asignarIva(Iva aIva, String aVista) {

		try {

			this.ivaTransaccion = aIva;

			if (aVista != null && aVista.equals("MODAL_EDITAR_IVA")) {
				this.abrirModal("panelEdicionIva");

			} else {

				this.abrirModal("panelEliminacionIva");

			}

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}

	}

	/**
	 * Obtiene un listado de ivas creados
	 * 
	 * @return ivas
	 */
	public List<Iva> getIvas() {
		try {
			if (this.ivas == null) {

				this.ivas = IConsultasDAO.getIvas(null);

			}
		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
		return ivas;
	}

	/**
	 * Establece los ivas creados
	 * 
	 * @param ivas
	 */
	public void setIvas(List<Iva> ivas) {
		this.ivas = ivas;
	}

	/**
	 * Obtiene items de iva
	 * 
	 * @return itemsIva
	 */
	public List<SelectItem> getItemsIva() {
		try {

			this.itemsIva = new ArrayList<SelectItem>();
			this.itemsIva.add(new SelectItem("", "Seleccione.."));

			this.ivas = null;
			this.getIvas();
			if (this.ivas != null && this.ivas.size() > 0) {
				this.ivas.forEach(p -> this.itemsIva.add(new SelectItem(p.getId(), p.getNombre() + "(" + p.getValorIva() + "%)")));
			}

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
		return itemsIva;
	}

	/**
	 * Establece item de iva
	 * 
	 * @param itemsIva
	 */
	public void setItemsIva(List<SelectItem> itemsIva) {
		this.itemsIva = itemsIva;
	}

	public Iva getIva() {
		try {
			if (this.iva == null) {
				this.iva = new Iva();
			}
		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
		return iva;
	}

	public void setIva(Iva iva) {
		this.iva = iva;
	}

	public Iva getIvaTransaccion() {
		return ivaTransaccion;
	}

	public void setIvaTransaccion(Iva ivaTransaccion) {
		this.ivaTransaccion = ivaTransaccion;
	}

	/**
	 * Descarga el archivo requerido
	 * 
	 * @param aArchivo
	 * @param aExtension
	 * @param aContentType
	 */
	private void descargarAdjunto(byte[] aArchivo, String aExtension, String aContentType) {

		try {

			FacesContext context = FacesContext.getCurrentInstance();
			ExternalContext ext = context.getExternalContext();
			SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");

			HttpServletResponse response = (HttpServletResponse) ext.getResponse();
			response.setContentType(aContentType);
			response.setHeader("Content-Disposition", "attachment; filename=" + formato.format(new Date()) + "." + aExtension.toLowerCase());
			response.setContentLength(aArchivo.length);
			ServletOutputStream servletOutputStream = response.getOutputStream();

			servletOutputStream.write(aArchivo, 0, aArchivo.length);

			servletOutputStream.flush();
			servletOutputStream.close();
			FacesContext.getCurrentInstance().responseComplete();

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
	}

	/**
	 * Valida si un proyecto-clinente-consultor cumple con los requisitos
	 * 
	 * @param aTransaccion
	 * @return ok
	 */
	private boolean isOkProyectoCliente(String aTransaccion) {
		boolean ok = true;
		try {

			SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");

			if (aTransaccion.equals("C")) {
				if (formato.format(this.proyectoCliente.getFechaInicio()).compareTo(formato.format(this.proyectoCliente.getFechaFin())) > 0) {
					ok = false;
					this.mostrarMensajeGlobal("fechaInicioConsultoria", "advertencia");
				} else {

					ProyectoCliente proyectoCliente = new ProyectoCliente();
					proyectoCliente.settEstadoCosnultoria(IConstantes.ACTIVO);
					proyectoCliente.getCliente().setId(this.proyectoCliente.getCliente().getId());
					proyectoCliente.getProyecto().setId(this.proyectoCliente.getProyecto().getId());
					List<ProyectoCliente> proyectos = IConsultasDAO.getProyectosCliente(proyectoCliente);
					if (proyectos != null && proyectos.size() > 0) {
						ok = false;
						this.mostrarMensajeGlobal("existeConsultoriaVigente", "advertencia");
					}

				}

			} else {

				if (this.proyectoClienteTransaccion.getFechaCertificacion() != null && formato.format(this.proyectoClienteTransaccion.getFechaInicio()).compareTo(formato.format(this.proyectoClienteTransaccion.getFechaCertificacion())) > 0) {
					ok = false;
					this.mostrarMensajeGlobal("fechaCertificacionInvalida", "advertencia");
				}

				if (formato.format(this.proyectoClienteTransaccion.getFechaInicio()).compareTo(formato.format(this.proyectoClienteTransaccion.getFechaFin())) > 0) {
					ok = false;
					this.mostrarMensajeGlobal("fechaInicioConsultoria", "advertencia");
				} else {

					ProyectoCliente proyectoCliente = new ProyectoCliente();
					proyectoCliente.settEstadoCosnultoria(IConstantes.ACTIVO);
					proyectoCliente.getCliente().setId(this.proyectoClienteTransaccion.getCliente().getId());
					proyectoCliente.getProyecto().setId(this.proyectoClienteTransaccion.getProyecto().getId());
					List<ProyectoCliente> proyectos = IConsultasDAO.getProyectosCliente(proyectoCliente);
					if (proyectos != null && proyectos.size() > 0 && proyectos.stream().anyMatch(p -> p.getId().intValue() != this.proyectoClienteTransaccion.getId().intValue())) {
						ok = false;
						this.mostrarMensajeGlobal("existeConsultoriaVigente", "advertencia");
					}

				}

			}

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}

		return ok;

	}

	/**
	 * Valida si un proyecto cumple con los requisitos
	 * 
	 * @param aTransaccion
	 * @return
	 */
	private boolean isOkProyecto(String aTransaccion) {
		boolean ok = true;

		if (aTransaccion.equals("C")) {
			if (this.isVacio(this.proyecto.getNombre())) {
				ok = false;
				this.mostrarMensajeGlobal("campoEstaVacio", "advertencia");
			} else {
				this.proyecto.setNombre(this.proyecto.getNombre().trim());
			}

		} else {
			if (this.isVacio(this.proyectoTransaccion.getNombre())) {
				ok = false;
				this.mostrarMensajeGlobal("campoEstaVacio", "advertencia");
			} else {
				this.proyectoTransaccion.setNombre(this.proyectoTransaccion.getNombre().trim());
			}

		}

		return ok;

	}

	/**
	 * Valida parï¿½metros de auditorï¿½a
	 * 
	 * @return ok
	 */
	private boolean isOkParametroAuditoria() {
		boolean ok = true;

		if (this.isVacio(this.parametroAuditoria.getObjetivos())) {
			ok = false;
			this.mostrarMensajeGlobal("campoEstaVacio", "advertencia");
		} else {
			this.parametroAuditoria.setObjetivos(this.parametroAuditoria.getObjetivos().trim());
		}

		if (this.isVacio(this.parametroAuditoria.getAlcance())) {
			ok = false;
			this.mostrarMensajeGlobal("campoEstaVacio", "advertencia");
		} else {
			this.parametroAuditoria.setAlcance(this.parametroAuditoria.getAlcance().trim());
		}

		if (this.isVacio(this.parametroAuditoria.getDocumentosReferencia())) {
			ok = false;
			this.mostrarMensajeGlobal("campoEstaVacio", "advertencia");
		} else {
			this.parametroAuditoria.setDocumentosReferencia(this.parametroAuditoria.getDocumentosReferencia().trim());
		}

		if (this.isVacio(this.parametroAuditoria.getObservaciones())) {
			ok = false;
			this.mostrarMensajeGlobal("campoEstaVacio", "advertencia");
		} else {
			this.parametroAuditoria.setObservaciones(this.parametroAuditoria.getObservaciones().trim());
		}

		if (!ok) {
			this.mostrarMensajeGlobal("campoEstaVacio", "advertencia");

		}

		return ok;

	}

	/**
	 * Valida si cumple las condiciones de estado para ser guardado o editado
	 * 
	 * @param aTransaccion
	 * @return ok
	 */
	private boolean isOkEstado(String aTransaccion) {
		boolean ok = true;

		if (aTransaccion.equals("C")) {
			if (this.isVacio(this.estado.getNombre())) {
				ok = false;
				this.mostrarMensajeGlobal("campoEstaVacio", "advertencia");
			} else {
				this.estado.setNombre(this.estado.getNombre().trim());
			}

		} else {
			if (this.isVacio(this.estadoTransaccion.getNombre())) {
				ok = false;
				this.mostrarMensajeGlobal("campoEstaVacio", "advertencia");
			} else {
				this.estadoTransaccion.setNombre(this.estadoTransaccion.getNombre().trim());
			}

		}

		return ok;

	}

	/**
	 * Valida que la pregunta sea correcta
	 * 
	 * @param aTransaccion
	 * @return ok
	 */
	private boolean isPreguntaOK(String aTransaccion) {
		boolean ok = true;

		if (aTransaccion.equals("C")) {
			if (this.isVacio(this.preguntaProyecto.getPregunta())) {
				ok = false;

			} else {
				this.preguntaProyecto.setPregunta(this.preguntaProyecto.getPregunta().trim());
			}

			if (this.isVacio(this.preguntaProyecto.getPosibleEvidencia())) {
				ok = false;

			} else {
				this.preguntaProyecto.setPosibleEvidencia(this.preguntaProyecto.getPosibleEvidencia().trim());
			}

			if (!this.isVacio(this.preguntaProyecto.getNumeral())) {
				// solo quita espacios
				this.preguntaProyecto.setNumeral(this.preguntaProyecto.getNumeral().trim());
			}

			if (!ok) {

				this.mostrarMensajeGlobal("campoEstaVacio", "advertencia");
			}

		} else {

			if (this.isVacio(this.preguntaProyectoTransaccion.getPregunta())) {
				ok = false;

			} else {
				this.preguntaProyectoTransaccion.setPregunta(this.getMayusculaSinEspacios(this.preguntaProyectoTransaccion.getPregunta()));
			}

			if (this.isVacio(this.preguntaProyectoTransaccion.getPosibleEvidencia())) {
				ok = false;

			} else {
				this.preguntaProyectoTransaccion.setPosibleEvidencia(this.getMayusculaSinEspacios(this.preguntaProyectoTransaccion.getPosibleEvidencia()));
			}

		}

		return ok;

	}

	/**
	 * Valida si una tarea estï¿½ bien
	 * 
	 * @param aTransaccion
	 * @return ok
	 */
	private boolean isTareaOK(String aTransaccion) {
		boolean ok = true;

		if (aTransaccion.equals("C")) {
			if (this.isVacio(this.tareaProyecto.getTarea())) {
				ok = false;

			} else {
				this.tareaProyecto.setTarea(this.tareaProyecto.getTarea().trim());
			}

			if (!ok) {

				this.mostrarMensajeGlobal("campoEstaVacio", "advertencia");
			}

		} else {

			if (this.isVacio(this.tareaProyectoTransaccion.getTarea())) {
				ok = false;

			} else {
				this.tareaProyectoTransaccion.setTarea(this.getSinEspacios(this.tareaProyectoTransaccion.getTarea()));
			}

		}

		return ok;

	}

	/**
	 * Valida la creaciï¿½n de un documento
	 * 
	 * @return ok
	 */
	private boolean isValidoDocumento() {
		boolean ok = true;

		if (this.isVacio(this.documentoActividad.getNombre())) {
			ok = false;
			this.mostrarMensajeGlobal("campoEstaVacio", "advertencia");
		} else {
			this.documentoActividad.setNombre(this.documentoActividad.getNombre().trim());
		}

		if (this.documentoActividad.getArchivo() == null) {
			ok = false;
			this.mostrarMensajeGlobal("archivoAdjuntoRequerido", "advertencia");
		}

		return ok;
	}

	// publicos

	/**
	 * Genear un pdf en disco
	 * 
	 * @param aArchivo
	 */
	public void crearPdf(byte[] aArchivo, DocumentoActividad aDocumentoActividad) {

		try {

			String nombre = "isoluciones" + aDocumentoActividad.getId().intValue() * aDocumentoActividad.getId().intValue();

			// File file = new File(this.getPath("/archivosTemporales/") + "/" +
			// nombre + ".pdf");

			// if (file == null || (file != null && file.exists())) {
			// aDocumentoActividad.settNombre(nombre + ".pdf");
			// } else {
			//
			// }

			OutputStream out = new FileOutputStream(this.getPath("/archivosTemporales/") + "/" + nombre + ".pdf");
			out.write(aArchivo);
			out.close();
			aDocumentoActividad.settNombre(nombre + ".pdf");

			this.abrirModal("panelVistaPreviaDocumento");

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}

	}

	/**
	 * Cancela la eliminacion de un docuemnto en transacciï¿½n
	 * 
	 * @param aVista
	 */
	public void cancelarDocumentoTransaccion(String aVista) {
		try {

			this.documentoActividadTransaccion = null;
			this.getDocumentoActividadTransaccion();
			this.documentos = null;
			this.getDocumentos();

			if (aVista != null && aVista.equals("MODAL_ELIMINAR_DOCUMENTO")) {
				this.cerrarModal("panelEliminacionDocumento");

			} else {

				this.cerrarModal("panelEdicionDocumento");

			}
		} catch (Exception e) {

			IConstantes.log.error(e, e);

		}

	}

	/**
	 * Edita el documento elegido
	 */
	public void editarDocumento() {

		Conexion conexion = new Conexion();
		try {

			conexion.setAutoCommitBD(false);
			Map<String, Object> actualizar = new HashMap<String, Object>();

			this.documentoActividadTransaccion.getCamposBD();
			actualizar.put("nombre", documentoActividadTransaccion.getNombre());
			actualizar.put("descargable", documentoActividadTransaccion.getDescargable());

			conexion.actualizarBD(this.documentoActividadTransaccion.getEstructuraTabla().getTabla(), actualizar, this.documentoActividadTransaccion.getEstructuraTabla().getLlavePrimaria(), null);
			conexion.commitBD();
			this.mostrarMensajeGlobal("edicionExitosa", "exito");

			this.cerrarModal("panelEdicionDocumento");

		} catch (Exception e) {
			conexion.rollbackBD();
			this.mostrarMensajeGlobal("transaccionFallida", "error");

		} finally {
			conexion.cerrarConexion();
		}

		// reseteo de variables
		this.documentoActividadTransaccion = null;
		this.getDocumentoActividadTransaccion();
		this.documentos = null;
		this.getDocumentos();

	}

	/**
	 * Elimina el documento elegido
	 */
	public void eliminarDocumento() {

		Conexion conexion = new Conexion();
		try {

			conexion.setAutoCommitBD(false);
			this.documentoActividadTransaccion.getCamposBD();
			conexion.eliminarBD(this.documentoActividadTransaccion.getEstructuraTabla().getTabla(), this.documentoActividadTransaccion.getEstructuraTabla().getLlavePrimaria());
			conexion.commitBD();
			this.mostrarMensajeGlobal("eliminacionExitosa", "exito");

			this.cerrarModal("panelEliminacionDocumento");

		} catch (Exception e) {
			conexion.rollbackBD();
			this.mostrarMensajeGlobal("transaccionFallida", "error");
			this.mostrarMensajeGlobal("eliminacionFallida", "error");

		} finally {
			conexion.cerrarConexion();
		}

		// reseteo de variables
		this.documentoActividadTransaccion = null;
		this.getDocumentoActividadTransaccion();
		this.documentos = null;
		this.getDocumentos();

	}

	/**
	 * Determina si estï¿½ habilitada la dcoumentaciï¿½n
	 * 
	 * @param aTareaProyecto
	 * @return ok
	 */
	public boolean isHabilitadaDocumentacion(TareaProyecto aTareaProyecto) {
		boolean ok = true;
		try {
			// if (!(aTareaProyecto != null &&
			// aTareaProyecto.getExplicacionDocumentacion() != null &&
			// !aTareaProyecto.getExplicacionDocumentacion().trim().equals(""))) {
			// ok = false;
			// }
			if (aTareaProyecto != null && aTareaProyecto.getId() != null) {
				DocumentoActividad doc = new DocumentoActividad();
				doc.getTareaProyecto().setId(aTareaProyecto.getId());
				List<DocumentoActividad> docs = IConsultasDAO.getDocumentos(doc);
				if (!(docs != null && docs.size() > 0)) {
					ok = false;
				}
			} else {
				ok = false;

			}

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
		return ok;

	}

	/**
	 * Descarga el adjunto solicitado
	 */
	public void descargarDocumento(DocumentoActividad aDocumentoEquipo) {
		try {
			this.documentoActividadTransaccion = aDocumentoEquipo;
			DocumentoActividad temp = IConsultasDAO.getAdjuntoDocumento(aDocumentoEquipo);
			if (temp != null) {
				crearPdf(temp.getArchivo(), aDocumentoEquipo);
				// descargarAdjunto(temp.getArchivo(), "pdf", "application/pdf");
			}

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}

	}

	/**
	 * Asigna un documento para realizarle operaciones
	 * 
	 * @param aDocumentoEquipo
	 * @param aVista
	 */
	public void asignarDocumento(DocumentoActividad aDocumentoEquipo, String aVista) {

		try {

			this.documentoActividadTransaccion = aDocumentoEquipo;
			if (aVista != null && aVista.equals("MODAL_ELIMINAR_DOCUMENTO")) {
				this.abrirModal("panelEliminacionDocumento");
			} else {
				this.abrirModal("panelEdicionDocumento");
			}

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}

	}

	/**
	 * Cancela la creaciï¿½n de un documento
	 */
	public void cancelarDocumento() {

		try {
			this.documentoActividad = null;
			this.getDocumentoActividad();
			this.documentoActividadTransaccion = null;
			this.getDocumentoActividadTransaccion();
			this.documentos = null;
			this.getDocumentos();
			this.tareaProyectoTransaccion = null;
			this.getTareaProyectoTransaccion();

			this.cerrarModal("panelDocumento");

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}

	}

	/**
	 * Permite crear un documento
	 */
	public void crearDocumento() {
		Conexion conexion = new Conexion();

		try {
			if (isValidoDocumento()) {
				conexion.setAutoCommitBD(false);

				this.documentoActividad.setFecha(new Date());

				this.documentoActividad.getCamposBD();
				conexion.insertarBD(this.documentoActividad.getEstructuraTabla().getTabla(), this.documentoActividad.getEstructuraTabla().getPersistencia());
				// recupera el id generado
				this.documentoActividad.setId(conexion.getUltimoSerialTransaccion(this.documentoActividad.getEstructuraTabla().getTabla()));

				// guarda el archivo en capeta por si acaso sirve sin subir a servidor
				String nombre = "isoluciones" + this.documentoActividad.getId().intValue() * this.documentoActividad.getId().intValue();
				OutputStream out = new FileOutputStream(this.getPath("/archivosTemporales/") + "/" + nombre + ".pdf");
				out.write(this.documentoActividad.getArchivo());
				out.close();

				conexion.commitBD();
				this.mostrarMensajeGlobal("creacionExitosa", "exito");

				// reseteo de variables
				this.documentoActividad = null;
				this.getDocumentoActividad();
				this.documentos = null;
				this.getDocumentos();
			}

		} catch (Exception e) {
			conexion.rollbackBD();
			this.mostrarMensajeGlobal("transaccionFallida", "error");
		} finally {
			conexion.cerrarConexion();
		}

	}

	/**
	 * Limpia el archivo cargado para volver a ingresar otro
	 */
	public void limpiarArchivoCargado() {
		this.documentoActividad.settFile(null);
		this.documentoActividad.setArchivo(null);
	}

	public void recibirArchivoCSV(FileUploadEvent event) {

		try {
			this.tareaProyecto.settFile(event.getFile());
			this.tareaProyecto.setArchivo(event.getFile().getContents());

			this.mostrarMensajeGlobal("archivoRecibido", "advertencia");
		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}

	}

	/**
	 * Limpia el archivo cargado para volver a ingresar otro
	 */
	public void limpiarArchivoCargadoCSV() {
		this.tareaProyecto.settFile(null);
		this.tareaProyecto.setArchivo(null);
	}

	/**
	 * Recibir el archivo y lo asigna al objeto
	 * 
	 * @param event
	 */
	public void recibirArchivo(FileUploadEvent event) {

		try {
			this.documentoActividad.settFile(event.getFile());
			this.documentoActividad.setArchivo(event.getFile().getContents());

			this.mostrarMensajeGlobalPersonalizado("Archivo recibido, guï¿½rdelo.", "advertencia");
		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}

	}

	/**
	 * Reordena las filas de las tareas
	 * 
	 * @param event
	 */
	public void onRowReorderTareas(ReorderEvent event) {
		Conexion conexion = new Conexion();
		conexion.setAutoCommitBD(false);

		try {
			int i = 0;
			if (event.getFromIndex() != event.getToIndex() && this.tareas != null && this.tareas.size() > 0) {

				for (TareaProyecto p : this.tareas) {
					i++;
					p.setPosicion(i);
					p.getCamposBD();
					conexion.actualizarBD(p.getEstructuraTabla().getTabla(), p.getEstructuraTabla().getPersistencia(), p.getEstructuraTabla().getLlavePrimaria(), null);
					conexion.commitBD();
				}

				this.mostrarMensajeGlobalPersonalizado(this.getMensaje("tareaMovida", "" + (event.getFromIndex() + 1), "" + (event.getToIndex() + 1)), "exito");
			}

			this.tareas = null;
			this.getTareas();

		} catch (Exception e) {
			conexion.rollbackBD();

			this.mostrarMensajeGlobal("transaccionFallida", "error");
		} finally {

			conexion.cerrarConexion();
		}

	}

	/**
	 * Reordena las filas de las preguntas
	 * 
	 * @param event
	 */
	public void onRowReorder(ReorderEvent event) {
		Conexion conexion = new Conexion();
		conexion.setAutoCommitBD(false);

		try {
			int i = 0;
			if (event.getFromIndex() != event.getToIndex() && this.preguntas != null && this.preguntas.size() > 0) {

				for (PreguntaProyecto p : this.preguntas) {
					i++;
					p.setPosicion(i);
					p.getCamposBD();
					conexion.actualizarBD(p.getEstructuraTabla().getTabla(), p.getEstructuraTabla().getPersistencia(), p.getEstructuraTabla().getLlavePrimaria(), null);
					conexion.commitBD();
				}

				this.mostrarMensajeGlobalPersonalizado(this.getMensaje("preguntaMovida", "" + (event.getFromIndex() + 1), "" + (event.getToIndex() + 1)), "exito");
			}

			this.preguntas = null;
			this.getPreguntas();

		} catch (Exception e) {
			conexion.rollbackBD();

			this.mostrarMensajeGlobal("transaccionFallida", "error");
		} finally {

			conexion.cerrarConexion();
		}

	}

	public String getHost() {
		return IConstantes.HOST;
	}

	/**
	 * Crea un registro de tarea de un proyecto
	 */
	public void crearTareaProyecto2() {
		Conexion conexion = new Conexion();

		try {

			conexion.setAutoCommitBD(false);
			int pos = this.tareas.size();
			int sw = 0;
			for (TareaProyecto t : this.tareasArchivo) {
				if (t.istApto()) {
					sw = 1;
					break;

				}

			}

			if (sw == 1) {
				for (TareaProyecto t : this.tareasArchivo) {
					if (t.istApto()) {
						pos++;
						t.setProyecto(this.tareaProyecto.getProyecto());
						t.setEstado(IConstantes.ACTIVO);
						t.setFechaEstado(new Date());
						t.setNumeroEtapa(2); // todo el ciclo del proyecto
						t.setPosicion(pos);
						t.getCamposBD();
						conexion.insertarBD(t.getEstructuraTabla().getTabla(), t.getEstructuraTabla().getPersistencia());
					}

				}
				conexion.commitBD();
				this.mostrarMensajeGlobal("creacionExitosa", "exito");

				// reseteo de variables
				this.tareasArchivo = null;
				this.tareasArchivo = new ArrayList<TareaProyecto>();
				this.tareaProyecto = null;
				this.getTareaProyecto();
				this.tareas = null;
				this.getTareas();
				this.cerrarModal("panelVerArchivo");

			} else {
				this.mostrarMensajeGlobalPersonalizado("NO CHEQUEO NINGUNA ACTIVIDAD, DEBE SELECCIONAR AL MENOS UNA", "exito");
			}

		} catch (Exception e) {
			conexion.rollbackBD();

			this.mostrarMensajeGlobal("transaccionFallida", "error");
		} finally {

			conexion.cerrarConexion();
		}
	}

	/**
	 * Crea un registro de tarea de un proyecto
	 */
	public void crearTareaProyecto() {
		Conexion conexion = new Conexion();
		InputStream input = null;
		CsvReader archivoLeido = null;

		Integer cuentaFilas = 0;
		TareaProyecto tareaArchivo = null;
		String valorCelda = null;

		try {
			this.tareasArchivo = new ArrayList<TareaProyecto>();
			if (this.tareaProyecto.gettFormaAgregar() == null || this.tareaProyecto.gettFormaAgregar().trim().equals("I")) {

				if (isTareaOK("C")) {

					conexion.setAutoCommitBD(false);

					this.tareaProyecto.setEstado(IConstantes.ACTIVO);
					this.tareaProyecto.setFechaEstado(new Date());
					this.tareaProyecto.setNumeroEtapa(2); // todo el ciclo del proyecto

					if (this.tareas != null && this.tareas.size() > 0) {
						this.tareaProyecto.setPosicion(this.tareas.size() + 1);
					} else {

						this.tareaProyecto.setPosicion(1);
					}

					this.tareaProyecto.getCamposBD();
					conexion.insertarBD(this.tareaProyecto.getEstructuraTabla().getTabla(), this.tareaProyecto.getEstructuraTabla().getPersistencia());

					conexion.commitBD();
					this.mostrarMensajeGlobal("creacionExitosa", "exito");

					// reseteo de variables
					this.tareaProyecto = null;
					this.getTareaProyecto();
					this.tareas = null;
					this.getTareas();

				}

			} else {

				if (this.tareaProyecto.getArchivo() == null) {
					// ok = false;
					this.mostrarMensajeGlobalPersonalizado("ARCHIVO PLANO REQUERIDO", "advertencia");
				} else {

					// aquí va la revisión y armado de csv

					input = new ByteArrayInputStream(this.tareaProyecto.getArchivo());
					Charset inputCharset = Charset.forName("ISO-8859-1");
					archivoLeido = new CsvReader(new InputStreamReader(input, inputCharset), ';');

					archivoLeido.setDelimiter(';');

					while (archivoLeido.readRecord()) {
						cuentaFilas++;
						if (cuentaFilas.intValue() >= IConstantes.FILA_INICIO_PERSONA_CSV.intValue()) {
							tareaArchivo = new TareaProyecto();

							valorCelda = archivoLeido.get(0);// actividad
							if (valorCelda != null && !valorCelda.trim().equals("")) {

								tareaArchivo.setTarea(valorCelda.trim());

							}

							valorCelda = archivoLeido.get(1);// entregables
							if (valorCelda != null && !valorCelda.trim().equals("")) {
								tareaArchivo.setProducto(valorCelda.trim());
							}

							valorCelda = archivoLeido.get(2);// responsables
							if (valorCelda != null && !valorCelda.trim().equals("")) {
								tareaArchivo.setResponsable(valorCelda.trim());
							}

							valorCelda = archivoLeido.get(3);// requisitos
							if (valorCelda != null && !valorCelda.trim().equals("")) {
								tareaArchivo.setRequisito(valorCelda.trim());
							}

							tareaArchivo.settConcepto("");
							tareaArchivo.settApto(true);

							this.tareasArchivo.add(tareaArchivo);

						}

					}

					if (this.tareasArchivo != null && this.tareasArchivo.size() > 0) {

						// analizamos la información
						for (TareaProyecto p : this.tareasArchivo) {

							// analizamos si falta información
							if (!(p.getTarea() != null && !p.getTarea().trim().equals("")) || !(p.getProducto() != null && !p.getProducto().trim().equals("")) || !(p.getResponsable() != null && !p.getResponsable().trim().equals(""))) {
								if (p.gettConcepto() != null && !p.gettConcepto().trim().equals("")) {
									p.settConcepto(p.gettConcepto() + "; " + "INFORMACION INCOMPLETA REVISE CAMPOS (*) REQUERIDOS");
								} else {
									p.settConcepto("" + "INFORMACION INCOMPLETA REVISE CAMPOS (*) REQUERIDOS");
								}

								p.settApto(false);

							}

							// revisa q haya escrito lo que debe
							if (p.getResponsable() != null && !p.getResponsable().trim().equals("")) {
								if (p.getResponsable().trim().toUpperCase().equals("AMBOS")) {
									p.setResponsable("A");
								} else if (p.getResponsable().trim().toUpperCase().equals("ISOLUCIONES")) {
									p.setResponsable("CO");
								} else if (p.getResponsable().trim().toUpperCase().equals("CLIENTE")) {
									p.setResponsable("CL");
								} else {
									p.settApto(false);
									if (p.gettConcepto() != null && !p.gettConcepto().trim().equals("")) {
										p.settConcepto(p.gettConcepto() + "; " + "LAS UNICAS PALABRAS PERMITIDAS PARA RESPONSABLES SON: ISOLUCIONES, CLIENTE, AMBOS");
									} else {
										p.settConcepto("" + "LAS UNICAS PALABRAS PERMITIDAS PARA RESPONSABLES SON: ISOLUCIONES, CLIENTE, AMBOS");
									}
								}
							}

						}

						for (TareaProyecto p : this.tareasArchivo) {
							if (p.istApto()) {

								p.settConcepto("OK");

							}

						}

						this.abrirModal("panelVerArchivo");
						this.mostrarMensajeGlobalPersonalizado("RESUMEN DE ACTIVIDADES DEL ARCHIVO. REVISE LAS QUE DESEE Y TENGASLAS CHEQUEADAS", "advertencia");

					} else {

						this.mostrarMensajeGlobalPersonalizado("EL ARCHIVO NO POSEE ACTIVIDADES O TAREAS", "advertencia");
					}

				}
			}

		} catch (Exception e) {
			conexion.rollbackBD();

			this.mostrarMensajeGlobal("transaccionFallida", "error");
		} finally {

			conexion.cerrarConexion();
		}
	}

	/**
	 * Crea un registro de pregunta de un proyecto
	 */
	public void crearPreguntaProyecto() {
		Conexion conexion = new Conexion();

		try {

			if (isPreguntaOK("C")) {

				conexion.setAutoCommitBD(false);

				this.preguntaProyecto.setEstado(IConstantes.ACTIVO);
				this.preguntaProyecto.setFechaEstado(new Date());

				if (this.preguntas != null && this.preguntas.size() > 0) {
					this.preguntaProyecto.setPosicion(this.preguntas.size() + 1);
				} else {

					this.preguntaProyecto.setPosicion(1);
				}

				this.preguntaProyecto.getCamposBD();
				conexion.insertarBD(this.preguntaProyecto.getEstructuraTabla().getTabla(), this.preguntaProyecto.getEstructuraTabla().getPersistencia());

				conexion.commitBD();
				this.mostrarMensajeGlobal("creacionExitosa", "exito");

				// reseteo de variables
				this.preguntaProyecto = null;
				this.getPreguntaProyecto();
				this.preguntas = null;
				this.getPreguntas();

			}

		} catch (Exception e) {
			conexion.rollbackBD();

			this.mostrarMensajeGlobal("transaccionFallida", "error");
		} finally {

			conexion.cerrarConexion();
		}
	}

	/**
	 * Determina si la vigencia de un rango de fechas
	 * 
	 * @param aProyectoCliente
	 * @return vigente
	 */
	public boolean isProyectoClienteVigente(ProyectoCliente aProyectoCliente) {
		boolean vigente = false;
		SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");

		if (formato.format(aProyectoCliente.getFechaInicio()).compareTo(formato.format(new Date())) <= 0 && formato.format(aProyectoCliente.getFechaFin()).compareTo(formato.format(new Date())) >= 0) {
			vigente = true;
		} else {
			vigente = false;
		}

		return vigente;
	}

	/**
	 * Obtiene el texto que se va a mostrar en la grilla del proyecto cliente
	 * 
	 * @param aProyectoCliente
	 * @return texto
	 */
	public String getTextoVigenciaProyecto(ProyectoCliente aProyectoCliente) {
		String texto = "";
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
		texto = formato.format(aProyectoCliente.getFechaInicio()) + " a " + formato.format(aProyectoCliente.getFechaFin());

		return texto;
	}

	/**
	 * Obtiene el autocompletar de consulta del client
	 * 
	 * @param aTexto
	 * @return clientes
	 */
	public List<String> usarAutocompletarConsultarCliente(String aTexto) {
		final List<String> clientes = new ArrayList<String>();
		try {

			if (aTexto != null && !aTexto.equals("")) {
				Cliente cliente = new Cliente();
				cliente.setCliente(aTexto.trim().toUpperCase());
				List<Cliente> listadoClientes = IConsultasDAO.getClientes(cliente);

				if (listadoClientes != null && listadoClientes.size() > 0) {

					listadoClientes.forEach(p -> clientes.add(p.getCliente()));
				}

			}
		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
		return clientes;
	}

	/**
	 * Autocompleta el texto del cliente
	 * 
	 * @param aTexto
	 * @return clientes
	 */
	public List<String> usarAutocompletarEditarCliente(String aTexto) {
		final List<String> clientes = new ArrayList<String>();
		try {

			if (aTexto != null && !aTexto.equals("")) {
				Cliente cliente = new Cliente();
				cliente.setCliente(aTexto.trim().toUpperCase());
				List<Cliente> listadoClientes = IConsultasDAO.getClientes(cliente);

				if (listadoClientes != null && listadoClientes.size() > 0) {

					listadoClientes.forEach(p -> clientes.add(p.getCliente()));
				}

			}
		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
		return clientes;
	}

	/**
	 * Obtiene un mï¿½todo de autocompletar para el nombre del cliente
	 * 
	 * @param aTexto
	 * @return clientes
	 */
	public List<String> usarAutocompletarCrearCliente(String aTexto) {
		final List<String> clientes = new ArrayList<String>();
		try {

			if (aTexto != null && !aTexto.equals("")) {
				Cliente cliente = new Cliente();
				cliente.setCliente(aTexto.trim().toUpperCase());
				List<Cliente> listadoClientes = IConsultasDAO.getClientes(cliente);

				if (listadoClientes != null && listadoClientes.size() > 0) {

					listadoClientes.forEach(p -> clientes.add(p.getCliente()));
				}

			}
		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
		return clientes;
	}

	/**
	 * Mï¿½todo que me selecciona el nombre del cliente, lo busca y llena del
	 * objeto el id para consulta
	 * 
	 * @param event
	 */
	public void onItemSelectConsultarCliente(SelectEvent event) {

		try {
			if (event != null && event.getObject() != null && !event.getObject().toString().trim().equals("") && this.proyectoClienteConsulta != null) {
				Cliente cliente = IConsultasDAO.getClientesPorNombre(event.getObject().toString().trim());
				if (cliente != null && cliente.getId() != null) {
					this.proyectoClienteConsulta.getCliente().setId(cliente.getId());
					this.proyectoClienteConsulta.getCliente().setCliente(cliente.getCliente());

				}

			}

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}

	}

	/**
	 * Obtiene el cliente al seleccionarlo al editar
	 * 
	 * @param event
	 */
	public void onItemSelectEditarCliente(SelectEvent event) {

		try {
			if (event != null && event.getObject() != null && !event.getObject().toString().trim().equals("") && this.proyectoClienteTransaccion != null) {
				Cliente cliente = IConsultasDAO.getClientesPorNombre(event.getObject().toString().trim());
				if (cliente != null && cliente.getId() != null) {
					this.proyectoClienteTransaccion.getCliente().setId(cliente.getId());
					this.proyectoClienteTransaccion.getCliente().setCliente(cliente.getCliente());

				}

			}

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}

	}

	/**
	 * Mï¿½todo que me selecciona el nombre del cliente, lo busca y llena del
	 * objeto el id
	 * 
	 * @param event
	 */
	public void onItemSelectCrearCliente(SelectEvent event) {

		try {
			if (event != null && event.getObject() != null && !event.getObject().toString().trim().equals("") && this.proyectoCliente != null) {
				Cliente cliente = IConsultasDAO.getClientesPorNombre(event.getObject().toString().trim());
				if (cliente != null && cliente.getId() != null) {
					this.proyectoCliente.getCliente().setId(cliente.getId());
					this.proyectoCliente.getCliente().setCliente(cliente.getCliente());

				}

			}

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}

	}

	/**
	 * Cancela y resetea criterios de consulta y resultados antes cargados
	 */
	public void cancelarProyectoClienteConsulta() {
		this.proyectoClienteConsulta = null;
		this.getProyectoClienteConsulta();

		this.proyectosCliente = null;
	}

	/**
	 * Consulta los proyectos de los clientes
	 */
	public void consultarProyectosClientes() {
		Conexion conexion = new Conexion();
		ResultSet rs = null;
		Estadistica estadistica = null;
		List<Object> parametros = null;
		String codigosDiagnostico = null;
		try {

			this.radicadosAuditoria = null;

			if (!(this.proyectoClienteConsulta != null && this.proyectoClienteConsulta.getCliente() != null && this.proyectoClienteConsulta.getCliente().getCliente() != null && !this.proyectoClienteConsulta.getCliente().getCliente().trim().equals(""))) {
				this.proyectoClienteConsulta.getCliente().setId(null);
				this.proyectoClienteConsulta.getCliente().setCliente(null);
				this.proyectoClienteConsulta.getCliente().setNit(null);
			}

			this.proyectosCliente = IConsultasDAO.getProyectosCliente(this.proyectoClienteConsulta);

			// ai selecciona una norma que haga los siguientes calculos
			if (this.proyectosCliente != null && this.proyectosCliente.size() > 0 && this.proyectoClienteConsulta != null && this.proyectoClienteConsulta.getProyecto() != null && this.proyectoClienteConsulta.getProyecto().getId() != null) {
				this.radicadosAuditoria = new ArrayList<Estadistica>();

				try {
					String proyectosClienteLista = "";
					int i = 0;
					for (ProyectoCliente pc : this.proyectosCliente) {
						i++;
						if (i == 1) {
							proyectosClienteLista += "" + pc.getId();
						} else {
							proyectosClienteLista += "," + pc.getId();
						}
					}

					StringBuilder sql = new StringBuilder();
					sql.append("  SELECT distinct pp.numeral FROM diagnostico d");
					sql.append("  INNER JOIN preguntas_proyecto pp ON d.id_pregunta_proyecto = pp.id");
					sql.append("  WHERE d.id_proyecto_cliente IN (" + proyectosClienteLista + ")");
					sql.append("  AND pp.numeral IS NOT NULL AND TRIM(pp.numeral) <> ''");

					rs = conexion.consultarBD(sql.toString(), null);

					while (rs.next()) {
						estadistica = new Estadistica();
						estadistica.setNumeral(rs.getString(1));
						estadistica.setNoConformidad(0);
						estadistica.setFortaleza(0);
						estadistica.setRecomendacion(0);

						radicadosAuditoria.add(estadistica);
					}

					if (radicadosAuditoria != null && radicadosAuditoria.size() > 0) {

						// para cada numeral
						for (Estadistica e : radicadosAuditoria) {
							// para cada cliente
							for (ProyectoCliente pc : this.proyectosCliente) {

								parametros = new ArrayList<Object>();
								codigosDiagnostico = "";
								sql = new StringBuilder();
								sql.append("  SELECT d.id diagnostico FROM diagnostico d");
								sql.append("  INNER JOIN preguntas_proyecto pp ON d.id_pregunta_proyecto = pp.id");
								sql.append("  WHERE pp.numeral IS NOT NULL AND TRIM(pp.numeral) <> ''");
								sql.append("  AND pp.numeral = ?");
								sql.append("  AND d.id_proyecto_cliente = ?");

								parametros.add(e.getNumeral());
								parametros.add(pc.getId());

								rs = conexion.consultarBD(sql.toString(), parametros);
								int j = 0;
								while (rs.next()) {
									j++;
									if (j == 1) {
										codigosDiagnostico += "" + rs.getInt(1);
									} else {
										codigosDiagnostico += "," + rs.getInt(1);
									}

								}

								if (j > 0) {

									sql = new StringBuilder();
									// fortaleza
									sql.append("  SELECT COUNT(*) FROM estados_diagnostico WHERE id_diagnostico IN (" + codigosDiagnostico + ") AND id_estado = 7");
									rs = conexion.consultarBD(sql.toString(), null);
									if (rs.next()) {
										if (rs.getInt(1) > 0) {
											e.setFortaleza(e.getFortaleza().intValue() + 1);
										}
									}

									// recomendacion
									sql = new StringBuilder();
									sql.append("  SELECT COUNT(*) FROM estados_diagnostico WHERE id_diagnostico IN (" + codigosDiagnostico + ") AND id_estado = 8");
									rs = conexion.consultarBD(sql.toString(), null);
									if (rs.next()) {
										if (rs.getInt(1) > 0) {
											e.setRecomendacion(e.getRecomendacion().intValue() + 1);
										}
									}

									// nc conformidad
									sql = new StringBuilder();
									sql.append("  SELECT COUNT(*) FROM estados_diagnostico WHERE id_diagnostico IN (" + codigosDiagnostico + ") AND id_estado = 9");
									rs = conexion.consultarBD(sql.toString(), null);
									if (rs.next()) {
										if (rs.getInt(1) > 0) {
											e.setNoConformidad(e.getNoConformidad().intValue() + 1);
										}
									}

								}

							}

						}

						GraficoEstadistica grafico = null;
						for (Estadistica e : radicadosAuditoria) {

							e.setValoresGrafico(new ArrayList<GraficoEstadistica>());

							grafico = new GraficoEstadistica();
							grafico.setHallazgo("FORTALEZA");
							grafico.setValor(e.getFortaleza());
							e.getValoresGrafico().add(grafico);

							grafico = new GraficoEstadistica();
							grafico.setHallazgo("RECOMENDACION");
							grafico.setValor(e.getRecomendacion());
							e.getValoresGrafico().add(grafico);

							grafico = new GraficoEstadistica();
							grafico.setHallazgo("NO CONFORMIDAD");
							grafico.setValor(e.getNoConformidad());
							e.getValoresGrafico().add(grafico);

						}

					}

				} catch (Exception e) {
					throw new Exception(e);

				}

			}

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		} finally {
			conexion.cerrarConexion();
		}

	}
	
	
	
	/**
	 * Imprime radicado
	 */
	public void imprimirRadicado() {
		try {

			String reporte = "";
			SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
			

			Map<String, Object> parametros = new HashMap<String, Object>();
			

			parametros.put("SUBREPORT_DIR", this.getPath(IConstantes.PAQUETE_MODULO_REPORTES) + "/");
			parametros.put("rutaFirmas", this.getPath(IConstantes.PAQUETE_IMAGENES) + "/fotosFirmas/");
			parametros.put("pProyectoCliente", this.proyectosCliente.get(0));
			parametros.put("pParametroAuditoria", IConsultasDAO.getParametroAuditoria());
			parametros.put("pRutaLogo", this.getPath(IConstantes.PAQUETE_IMAGENES + IConstantes.LOGO1));

			this.generarListado(new JRBeanCollectionDataSource(this.radicadosAuditoria), "imprimirInformeRadicado.jasper", "INFORME-" + formatoFecha.format(new Date()), null, parametros);

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
	}

	/**
	 * Asigna un proyecto cliente
	 * 
	 * @param aProyectoCliente
	 * @param aVista
	 */
	public void asignarProyectoCliente(ProyectoCliente aProyectoCliente, String aVista) {

		try {

			this.proyectoClienteTransaccion = aProyectoCliente;

			if (aVista != null && aVista.equals("MODAL_EDITAR")) {
				this.abrirModal("panelEdicion");

			} else {

				this.abrirModal("panelEliminacion");

			}

			this.itemsConsultoresEdicion = null;
			this.itemsProyectosEdicion = null;

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}

	}

	/**
	 * Asigna un proyecto para realizarle transacciones
	 * 
	 * @param aProyecto
	 * @param aVista
	 */
	public void asignarProyecto(Proyecto aProyecto, String aVista) {

		try {

			this.proyectoTransaccion = aProyecto;
			this.vistaActual = null;

			if (aVista != null && aVista.equals("VISTA_PREGUNTAS")) {
				this.vistaActual = aVista;
				this.preguntas = null;
				this.getPreguntas();

				this.preguntaProyecto = null;
				this.getPreguntaProyecto();

			} else if (aVista != null && aVista.equals("VISTA_TAREAS")) {
				this.vistaActual = aVista;
				this.tareas = null;
				this.getTareas();

				this.tareaProyecto = null;
				this.getTareaProyecto();

			} else if (aVista != null && aVista.equals("MODAL_EDITAR")) {
				this.abrirModal("panelEdicion");

			} else {

				this.abrirModal("panelEliminacion");

			}

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}

	}

	/**
	 * Este mï¿½todo asigna un objeto del listado de estados para relizarle
	 * operaciones distintas
	 * 
	 * @param aEstado
	 * @param aVista
	 */
	public void asignarEstado(Estado aEstado, String aVista) {

		try {

			this.estadoTransaccion = aEstado;

			if (aVista != null && aVista.equals("MODAL_EDITAR")) {
				this.abrirModal("panelEdicion");

			} else {

				this.abrirModal("panelEliminacion");

			}

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}

	}

	/**
	 * Cancela un proyecto cliente transacciï¿½n
	 * 
	 * @param aVista
	 */
	public void cancelarProyectoClienteTransaccion(String aVista) {
		try {

			this.proyectoClienteTransaccion = null;
			this.getProyectoClienteTransaccion();
			this.proyectosCliente = null;
			this.consultarProyectosClientes();

			if (aVista != null && aVista.equals("MODAL_EDITAR")) {
				this.cerrarModal("panelEdicion");

			} else if (aVista != null && aVista.equals("MODAL_ELIMINAR")) {
				this.cerrarModal("panelEliminacion");

			}

		} catch (Exception e) {

			IConstantes.log.error(e, e);

		}

	}

	/**
	 * Cancela la ediciï¿½n, eliminaciï¿½n de un proyecto o regresar a vista
	 * 
	 * @param aVista
	 */
	public void cancelarProyectoTransaccion(String aVista) {
		try {

			this.proyectoTransaccion = null;
			this.getProyectoTransaccion();
			this.proyectosCliente = null;
			this.getProyectosCliente();
			this.preguntas = null;
			this.tareas = null;

			if (aVista != null && aVista.equals("MODAL_EDITAR")) {
				this.cerrarModal("panelEdicion");

			} else if (aVista != null && aVista.equals("MODAL_ELIMINAR")) {
				this.cerrarModal("panelEliminacion");

			} else {

				this.vistaActual = null;

			}

		} catch (Exception e) {

			IConstantes.log.error(e, e);

		}

	}

	/**
	 * Cancela la ediciï¿½n o eliminaciï¿½n de un estado y la vuelve su estado
	 * original
	 * 
	 * @param aVista
	 */
	public void cancelarEstadoTransaccion(String aVista) {
		try {

			this.estadoTransaccion = null;
			this.getEstadoTransaccion();
			this.estados = null;
			this.getEstados();

			if (aVista != null && aVista.equals("MODAL_EDITAR")) {
				this.cerrarModal("panelEdicion");

			} else if (aVista != null && aVista.equals("MODAL_ELIMINAR")) {
				this.cerrarModal("panelEliminacion");

			}

		} catch (Exception e) {

			IConstantes.log.error(e, e);

		}

	}

	/**
	 * Elimina un proyecto de un cliente
	 */
	public void eliminarProyectoCliente() {

		Conexion conexion = new Conexion();

		try {

			conexion.setAutoCommitBD(false);
			this.proyectoClienteTransaccion.getCamposBD();
			conexion.eliminarBD(this.proyectoClienteTransaccion.getEstructuraTabla().getTabla(), this.proyectoClienteTransaccion.getEstructuraTabla().getLlavePrimaria());

			conexion.commitBD();
			this.mostrarMensajeGlobal("eliminacionExitosa", "exito");

		} catch (Exception e) {
			conexion.rollbackBD();
			this.mostrarMensajeGlobal("transaccionFallida", "error");
			this.mostrarMensajeGlobal("eliminacionFallida", "error");

		} finally {
			conexion.cerrarConexion();
		}

		// reseteo de variables
		this.proyectoClienteTransaccion = null;
		this.getProyectoClienteTransaccion();
		this.proyectosCliente = null;
		this.consultarProyectosClientes();

	}

	/**
	 * Elimina un proyecto
	 */
	public void eliminarProyecto() {

		Conexion conexion = new Conexion();
		try {

			conexion.setAutoCommitBD(false);
			this.proyectoTransaccion.getCamposBD();
			conexion.eliminarBD(this.proyectoTransaccion.getEstructuraTabla().getTabla(), this.proyectoTransaccion.getEstructuraTabla().getLlavePrimaria());
			conexion.commitBD();
			this.mostrarMensajeGlobal("eliminacionExitosa", "exito");

		} catch (Exception e) {
			conexion.rollbackBD();
			this.mostrarMensajeGlobal("transaccionFallida", "error");
			this.mostrarMensajeGlobal("eliminacionFallida", "error");

		} finally {
			conexion.cerrarConexion();
		}

		// reseteo de variables
		this.proyectoTransaccion = null;
		this.getProyectoTransaccion();
		this.proyectos = null;
		this.getProyectos();

	}

	/**
	 * Elimina un registro de estado
	 */
	public void eliminarEstado() {

		Conexion conexion = new Conexion();
		try {

			conexion.setAutoCommitBD(false);
			this.estadoTransaccion.getCamposBD();
			conexion.eliminarBD(this.estadoTransaccion.getEstructuraTabla().getTabla(), this.estadoTransaccion.getEstructuraTabla().getLlavePrimaria());
			conexion.commitBD();
			this.mostrarMensajeGlobal("eliminacionExitosa", "exito");

		} catch (Exception e) {
			conexion.rollbackBD();
			this.mostrarMensajeGlobal("transaccionFallida", "error");
			this.mostrarMensajeGlobal("eliminacionFallida", "error");

		} finally {
			conexion.cerrarConexion();
		}

		// reseteo de variables
		this.estadoTransaccion = null;
		this.getEstadoTransaccion();
		this.estados = null;
		this.getEstados();

	}

	/**
	 * Edita un proyecto cliente
	 */
	public void editarProyectoCliente() {
		Conexion conexion = new Conexion();

		try {
			if (isOkProyectoCliente("E")) {

				conexion.setAutoCommitBD(false);
				this.proyectoClienteTransaccion.getCamposBD();
				conexion.actualizarBD(this.proyectoClienteTransaccion.getEstructuraTabla().getTabla(), this.proyectoClienteTransaccion.getEstructuraTabla().getPersistencia(), this.proyectoClienteTransaccion.getEstructuraTabla().getLlavePrimaria(), null);
				conexion.commitBD();

				this.mostrarMensajeGlobal("edicionExitosa", "exito");
				this.cerrarModal("panelEdicion");

				// reseteo de variables
				this.proyectosCliente = null;
				this.consultarProyectosClientes();
			}

		} catch (Exception e) {
			conexion.rollbackBD();
			this.mostrarMensajeGlobal("transaccionFallida", "error");
		} finally {
			conexion.cerrarConexion();
		}

	}

	/**
	 * Asigna la tarea a editar o eliminar
	 * 
	 * @param aPregunta
	 * @param aVista
	 */
	public void asignarTareaProyecto(TareaProyecto aTareaProyecto, String aVista) {

		try {

			this.tareaProyectoTransaccion = aTareaProyecto;

			if (aVista != null && aVista.equals("MODAL_EDICION_TAREA")) {
				this.abrirModal("panelEdicionHijoTarea");
			} else if (aVista != null && aVista.equals("MODAL_ELIMINACION_TAREA")) {
				this.abrirModal("panelEliminacionHijoTarea");
			} else if (aVista != null && aVista.equals("MODAL_VER_TAREA")) {
				this.abrirModal("panelVerHijoTarea");
			} else if (aVista != null && aVista.equals("MODAL_VER_SUBTAREAS")) {

				this.abrirModal("panelVerSubtarea");

			} else if (aVista != null && aVista.equals("MODAL_VER_DOCUMENTOS")) {

				this.documentoActividad = null;
				this.getDocumentoActividad();
				this.documentoActividadTransaccion = null;
				this.getDocumentoActividadTransaccion();
				this.documentos = null;
				this.getDocumentos();

				this.abrirModal("panelDocumento");

			}

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}

	}

	/**
	 * Asigna la preguna a editar o eliminar
	 * 
	 * @param aPregunta
	 * @param aVista
	 */
	public void asignarPreguntaProyecto(PreguntaProyecto aPregunta, String aVista) {

		try {

			this.preguntaProyectoTransaccion = aPregunta;

			if (aVista != null && aVista.equals("MODAL_EDICION_PREGUNTA")) {
				this.abrirModal("panelEdicionHijo");
			} else if (aVista != null && aVista.equals("MODAL_ELIMINACION_PREGUNTA")) {
				this.abrirModal("panelEliminacionHijo");
			} else if (aVista != null && aVista.equals("MODAL_VER_PREGUNTA")) {
				this.abrirModal("panelVerHijo");
			}

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}

	}

	/**
	 * Elimina una tarea
	 */
	public void eliminarTareaProyecto() {
		Conexion conexion = new Conexion();
		List<TareaProyecto> tareasTransaccion = null;
		int i = 0;

		try {

			conexion.setAutoCommitBD(false);
			this.tareaProyectoTransaccion.getCamposBD();
			conexion.eliminarBD(this.tareaProyectoTransaccion.getEstructuraTabla().getTabla(), this.tareaProyectoTransaccion.getEstructuraTabla().getLlavePrimaria());

			TareaProyecto tarea = new TareaProyecto();
			tarea.setProyecto(this.proyectoTransaccion);
			tareasTransaccion = IConsultasDAO.getTareas(conexion, tarea);
			if (tareasTransaccion != null && tareasTransaccion.size() > 0) {
				for (TareaProyecto p : tareasTransaccion) {
					i++;
					p.setPosicion(i);
					p.getCamposBD();
					conexion.actualizarBD(p.getEstructuraTabla().getTabla(), p.getEstructuraTabla().getPersistencia(), p.getEstructuraTabla().getLlavePrimaria(), null);

				}
			}

			conexion.commitBD();

			this.mostrarMensajeGlobal("eliminacionExitosa", "exito");
			// this.mostrarMensajeGlobal("tambienPosiciones", "exito");
			this.cerrarModal("panelEliminacionHijoTarea");

		} catch (Exception e) {
			conexion.rollbackBD();
			this.mostrarMensajeGlobal("transaccionFallida", "error");
			this.mostrarMensajeGlobal("eliminacionFallida", "error");
		} finally {
			conexion.cerrarConexion();
		}

		// reseteo de variables
		this.tareaProyectoTransaccion = null;
		this.getTareaProyectoTransaccion();
		this.tareas = null;
		this.getTareas();

	}

	/**
	 * Elimina la pregunta de un proyecto
	 */
	public void eliminarPreguntaProyecto() {
		Conexion conexion = new Conexion();
		List<PreguntaProyecto> preguntasTransaccion = null;
		int i = 0;

		try {

			conexion.setAutoCommitBD(false);
			this.preguntaProyectoTransaccion.getCamposBD();
			conexion.eliminarBD(this.preguntaProyectoTransaccion.getEstructuraTabla().getTabla(), this.preguntaProyectoTransaccion.getEstructuraTabla().getLlavePrimaria());

			PreguntaProyecto pregunta = new PreguntaProyecto();
			pregunta.setProyecto(this.proyectoTransaccion);
			preguntasTransaccion = IConsultasDAO.getPreguntas(conexion, pregunta);
			if (preguntasTransaccion != null && preguntasTransaccion.size() > 0) {
				for (PreguntaProyecto p : preguntasTransaccion) {
					i++;
					p.setPosicion(i);
					p.getCamposBD();
					conexion.actualizarBD(p.getEstructuraTabla().getTabla(), p.getEstructuraTabla().getPersistencia(), p.getEstructuraTabla().getLlavePrimaria(), null);

				}
			}

			conexion.commitBD();

			this.mostrarMensajeGlobal("eliminacionExitosa", "exito");
			this.mostrarMensajeGlobal("tambienPosiciones", "exito");
			this.cerrarModal("panelEliminacionHijo");

		} catch (Exception e) {
			conexion.rollbackBD();
			this.mostrarMensajeGlobal("transaccionFallida", "error");
			this.mostrarMensajeGlobal("eliminacionFallida", "error");
		} finally {
			conexion.cerrarConexion();
		}

		// reseteo de variables
		this.preguntaProyectoTransaccion = null;
		this.getPreguntaProyectoTransaccion();
		this.preguntas = null;
		this.getPreguntas();

	}

	/**
	 * Edita la explicaciï¿½n en la actividad
	 */
	public void editarExplicacion() {
		Conexion conexion = new Conexion();

		try {
			if (!this.isVacio(this.tareaProyectoTransaccion.getExplicacionDocumentacion())) {

				conexion.setAutoCommitBD(false);

				Map<String, Object> actualizar = new HashMap<String, Object>();
				actualizar.put("explicacion_documentacion", this.tareaProyectoTransaccion.getExplicacionDocumentacion().trim());
				this.tareaProyectoTransaccion.getCamposBD();

				conexion.actualizarBD(this.tareaProyectoTransaccion.getEstructuraTabla().getTabla(), actualizar, this.tareaProyectoTransaccion.getEstructuraTabla().getLlavePrimaria(), null);
				conexion.commitBD();

				this.mostrarMensajeGlobalPersonalizado("Explicaciï¿½n actualizada con ï¿½xito", "exito");

				// reseteo de variables
				this.tareas = null;
				this.getTareas();

				if (this.tareas != null && this.tareas.size() > 0) {
					this.tareas.stream().filter(p -> p.getId().intValue() == this.tareaProyectoTransaccion.getId().intValue()).forEach(p -> this.tareaProyectoTransaccion = p);
				}

			} else {

				this.mostrarMensajeGlobalPersonalizado("La explicaciï¿½n se encuentra vacï¿½a", "advertencia");

			}

		} catch (Exception e) {
			conexion.rollbackBD();
			this.mostrarMensajeGlobal("transaccionFallida", "error");
		} finally {
			conexion.cerrarConexion();
		}

	}

	/**
	 * Edita una tarea de un proyecto
	 */
	public void editarTareaProyecto() {
		Conexion conexion = new Conexion();

		try {
			if (isTareaOK("E")) {

				conexion.setAutoCommitBD(false);
				this.tareaProyectoTransaccion.setFechaEstado(new Date());
				this.tareaProyectoTransaccion.setNumeroEtapa(2);
				this.tareaProyectoTransaccion.getCamposBD();
				conexion.actualizarBD(this.tareaProyectoTransaccion.getEstructuraTabla().getTabla(), this.tareaProyectoTransaccion.getEstructuraTabla().getPersistencia(), this.tareaProyectoTransaccion.getEstructuraTabla().getLlavePrimaria(), null);
				conexion.commitBD();

				this.mostrarMensajeGlobal("edicionExitosa", "exito");
				this.cerrarModal("panelEdicionHijoTarea");

				// reseteo de variables
				this.tareaProyectoTransaccion = null;
				this.getTareaProyectoTransaccion();
				this.tareas = null;
				this.getTareas();
			}

		} catch (Exception e) {
			conexion.rollbackBD();
			this.mostrarMensajeGlobal("transaccionFallida", "error");
		} finally {
			conexion.cerrarConexion();
		}

	}

	/**
	 * Edita la pregunta de un proyecto
	 */
	public void editarPreguntaProyecto() {
		Conexion conexion = new Conexion();

		try {
			if (isPreguntaOK("E")) {

				conexion.setAutoCommitBD(false);
				this.preguntaProyectoTransaccion.setFechaEstado(new Date());
				this.preguntaProyectoTransaccion.getCamposBD();
				conexion.actualizarBD(this.preguntaProyectoTransaccion.getEstructuraTabla().getTabla(), this.preguntaProyectoTransaccion.getEstructuraTabla().getPersistencia(), this.preguntaProyectoTransaccion.getEstructuraTabla().getLlavePrimaria(), null);
				conexion.commitBD();

				this.mostrarMensajeGlobal("edicionExitosa", "exito");
				this.cerrarModal("panelEdicionHijo");

				// reseteo de variables
				this.preguntaProyectoTransaccion = null;
				this.getPreguntaProyectoTransaccion();
				this.preguntas = null;
				this.getPreguntas();
			}

		} catch (Exception e) {
			conexion.rollbackBD();
			this.mostrarMensajeGlobal("transaccionFallida", "error");
		} finally {
			conexion.cerrarConexion();
		}

	}

	/**
	 * Este mï¿½todo cancela la transacciï¿½n de una tarea
	 */
	public void cancelarTareaTransaccion(String aVista) {

		try {

			this.tareaProyectoTransaccion = null;
			this.getTareaProyectoTransaccion();
			this.tareas = null;
			this.getTareas();
			if (aVista != null && aVista.equals("MODAL_EDITAR_TAREA")) {

				this.cerrarModal("panelEdicionHijoTarea");

			} else if (aVista != null && aVista.equals("MODAL_VER_TAREA")) {

				this.cerrarModal("panelVerHijoTarea");

			} else {

				this.cerrarModal("panelEliminacionHijoTarea");
			}

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}

	}

	/**
	 * Este mï¿½todo cancela la transacciï¿½n de una pregunta
	 */
	public void cancelarPreguntaTransaccion(String aVista) {

		try {

			this.preguntaProyectoTransaccion = null;
			this.getPreguntaProyectoTransaccion();
			this.preguntas = null;
			this.getPreguntas();
			if (aVista != null && aVista.equals("MODAL_EDITAR_PREGUNTA")) {

				this.cerrarModal("panelEdicionHijo");

			} else if (aVista != null && aVista.equals("MODAL_VER_PREGUNTA")) {

				this.cerrarModal("panelVerHijo");

			} else {

				this.cerrarModal("panelEliminacionHijo");
			}

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}

	}

	/**
	 * Edita un registro de proyecto
	 */
	public void editarProyecto() {
		Conexion conexion = new Conexion();

		try {
			if (isOkProyecto("E")) {

				conexion.setAutoCommitBD(false);
				this.proyectoTransaccion.getCamposBD();
				conexion.actualizarBD(this.proyectoTransaccion.getEstructuraTabla().getTabla(), this.proyectoTransaccion.getEstructuraTabla().getPersistencia(), this.proyectoTransaccion.getEstructuraTabla().getLlavePrimaria(), null);
				conexion.commitBD();

				this.mostrarMensajeGlobal("edicionExitosa", "exito");
				this.cerrarModal("panelEdicion");

				// reseteo de variables
				this.proyectos = null;
				this.getProyectos();
			}

		} catch (Exception e) {
			conexion.rollbackBD();
			this.mostrarMensajeGlobal("transaccionFallida", "error");
		} finally {
			conexion.cerrarConexion();
		}

	}

	/**
	 * Edita un registro de parametro auditoria
	 */
	public void editarParametroAuditoria() {
		Conexion conexion = new Conexion();

		try {
			if (isOkParametroAuditoria()) {

				conexion.setAutoCommitBD(false);
				this.parametroAuditoria.getCamposBD();
				conexion.actualizarBD(this.parametroAuditoria.getEstructuraTabla().getTabla(), this.parametroAuditoria.getEstructuraTabla().getPersistencia(), this.parametroAuditoria.getEstructuraTabla().getLlavePrimaria(), null);
				conexion.commitBD();

				this.mostrarMensajeGlobal("edicionExitosa", "exito");

				// reseteo de variables
				this.parametroAuditoria = null;
				this.getParametroAuditoria();
			}

		} catch (Exception e) {
			conexion.rollbackBD();
			this.mostrarMensajeGlobal("transaccionFallida", "error");
		} finally {
			conexion.cerrarConexion();
		}

	}

	/**
	 * Edita un registro de bahia
	 */
	public void editarEstado() {
		Conexion conexion = new Conexion();

		try {
			if (isOkEstado("E")) {

				conexion.setAutoCommitBD(false);
				this.estadoTransaccion.getCamposBD();
				conexion.actualizarBD(this.estadoTransaccion.getEstructuraTabla().getTabla(), this.estadoTransaccion.getEstructuraTabla().getPersistencia(), this.estadoTransaccion.getEstructuraTabla().getLlavePrimaria(), null);
				conexion.commitBD();

				this.mostrarMensajeGlobal("edicionExitosa", "exito");
				this.cerrarModal("panelEdicion");

				// reseteo de variables
				this.estados = null;
				this.getEstados();
			}

		} catch (Exception e) {
			conexion.rollbackBD();
			this.mostrarMensajeGlobal("transaccionFallida", "error");
		} finally {
			conexion.cerrarConexion();
		}

	}

	/**
	 * Crea un proyecto cliente
	 */
	public void crearProyectoCliente() {
		Conexion conexion = new Conexion();

		try {
			if (isOkProyectoCliente("C")) {
				conexion.setAutoCommitBD(false);

				this.proyectoCliente.getCamposBD();
				conexion.insertarBD(this.proyectoCliente.getEstructuraTabla().getTabla(), this.proyectoCliente.getEstructuraTabla().getPersistencia());
				conexion.commitBD();

				this.mostrarMensajeGlobal("creacionExitosa", "exito");

				// reseteo de variables

				this.proyectoClienteConsulta = null;
				this.getProyectoClienteConsulta();
				this.proyectoClienteConsulta.getCliente().setId(this.proyectoCliente.getCliente().getId());

				this.proyectoCliente = null;
				this.getProyectoCliente();
				this.proyectosCliente = null;
				this.proyectosCliente = IConsultasDAO.getProyectosCliente(this.proyectoClienteConsulta);

			}

		} catch (Exception e) {
			conexion.rollbackBD();
			this.mostrarMensajeGlobal("transaccionFallida", "error");
		} finally {
			conexion.cerrarConexion();
		}

	}

	/**
	 * Crea un proyecto
	 */
	public void crearProyecto() {
		Conexion conexion = new Conexion();

		try {
			if (isOkProyecto("C")) {
				conexion.setAutoCommitBD(false);

				this.proyecto.setIndicativoVigencia(IConstantes.ACTIVO);

				this.proyecto.getCamposBD();
				conexion.insertarBD(this.proyecto.getEstructuraTabla().getTabla(), this.proyecto.getEstructuraTabla().getPersistencia());
				conexion.commitBD();

				this.mostrarMensajeGlobal("creacionExitosa", "exito");

				// reseteo de variables
				this.proyecto = null;
				this.getProyecto();
				this.proyectos = null;
				this.getProyectos();

			}

		} catch (Exception e) {
			conexion.rollbackBD();
			this.mostrarMensajeGlobal("transaccionFallida", "error");
		} finally {
			conexion.cerrarConexion();
		}

	}

	/**
	 * Crea un nuevo registro de estado
	 */
	public void crearEstado() {
		Conexion conexion = new Conexion();

		try {
			if (isOkEstado("C")) {
				conexion.setAutoCommitBD(false);

				this.estado.setIndicativoVigencia(IConstantes.ACTIVO);

				this.estado.getCamposBD();
				conexion.insertarBD(this.estado.getEstructuraTabla().getTabla(), this.estado.getEstructuraTabla().getPersistencia());
				conexion.commitBD();

				this.mostrarMensajeGlobal("creacionExitosa", "exito");

				// reseteo de variables
				this.estado = null;
				this.getEstado();
				this.estados = null;
				this.getEstados();

			}

		} catch (Exception e) {
			conexion.rollbackBD();
			this.mostrarMensajeGlobal("transaccionFallida", "error");
		} finally {
			conexion.cerrarConexion();
		}

	}

	/**
	 * Cancela un proyecto cliente a crear
	 */
	public void cancelarProyectoCliente() {

		try {
			this.proyectoCliente = null;
			this.getProyectoCliente();

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}

	}

	/**
	 * Cancela un proyecto a crear
	 */
	public void cancelarProyecto() {

		try {
			this.proyecto = null;
			this.getProyecto();
			this.proyectos = null;
			this.getProyectos();

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}

	}

	/**
	 * Este mï¿½todo borra el formulario de creaciï¿½n de un estado
	 */
	public void cancelarEstado() {

		try {
			this.estado = null;
			this.getEstado();
			this.estados = null;
			this.getEstados();

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}

	}

	// listados y etructuras

	// gets/sets

	/**
	 * Obtiene el estado de creaciï¿½n
	 * 
	 * @return estado
	 */
	public Estado getEstado() {
		try {
			if (this.estado == null) {
				this.estado = new Estado();
			}
		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
		return estado;
	}

	/**
	 * Establece el estado de creaciï¿½n
	 * 
	 * @param estado
	 */
	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	/**
	 * Obtiene el estado para realziar transacciones
	 * 
	 * @return estadoTransaccion
	 */
	public Estado getEstadoTransaccion() {
		return estadoTransaccion;
	}

	/**
	 * Establece el estado para realziar transacciones
	 * 
	 * @param estadoTransaccion
	 */
	public void setEstadoTransaccion(Estado estadoTransaccion) {
		this.estadoTransaccion = estadoTransaccion;
	}

	/**
	 * Obtiene los datos de un proyecto
	 * 
	 * @return proyecto
	 */
	public Proyecto getProyecto() {
		try {
			if (this.proyecto == null) {
				this.proyecto = new Proyecto();
			}
		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
		return proyecto;
	}

	/**
	 * Establece los datos de un proyecto
	 * 
	 * @param proyecto
	 */
	public void setProyecto(Proyecto proyecto) {
		this.proyecto = proyecto;
	}

	/**
	 * Obtiene los datos de un proyecto en transacciï¿½n
	 * 
	 * @return proyectoTransaccion
	 */
	public Proyecto getProyectoTransaccion() {
		try {
			if (this.proyectoTransaccion == null) {
				this.proyectoTransaccion = new Proyecto();
			}
		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
		return proyectoTransaccion;
	}

	/**
	 * Establece los datos de un proyecto en transacciï¿½n
	 * 
	 * @param proyectoTransaccion
	 */
	public void setProyectoTransaccion(Proyecto proyectoTransaccion) {
		this.proyectoTransaccion = proyectoTransaccion;
	}

	/**
	 * Obtiene el listado de estados
	 * 
	 * @return estados
	 */
	public List<Estado> getEstados() {
		try {
			this.estados = IConsultasDAO.getEstados(null);

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}

		return estados;
	}

	/**
	 * Establece el listado de estados
	 * 
	 * @param estados
	 */
	public void setEstados(List<Estado> estados) {
		this.estados = estados;
	}

	/**
	 * Obtiene un listado de proyectos
	 * 
	 * @return proyectos
	 */
	public List<Proyecto> getProyectos() {
		try {
			this.proyectos = IConsultasDAO.getProyectos(null);

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
		return proyectos;
	}

	/**
	 * Establece un listado de proyectos
	 * 
	 * @param proyectos
	 */
	public void setProyectos(List<Proyecto> proyectos) {
		this.proyectos = proyectos;
	}

	/**
	 * Obtiene un proyecto cliente a crear
	 * 
	 * @return proyectoCliente
	 */
	public ProyectoCliente getProyectoCliente() {
		try {
			if (this.proyectoCliente == null) {
				this.proyectoCliente = new ProyectoCliente();
			}
		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
		return proyectoCliente;
	}

	/**
	 * Establece un proyecto cliente a crear
	 * 
	 * @param proyectoCliente
	 */
	public void setProyectoCliente(ProyectoCliente proyectoCliente) {
		this.proyectoCliente = proyectoCliente;
	}

	/**
	 * Obtiene un proyecto cliente en transacciï¿½n
	 * 
	 * @return proyectoClienteTransaccion
	 */
	public ProyectoCliente getProyectoClienteTransaccion() {
		try {
			if (this.proyectoClienteTransaccion == null) {
				this.proyectoClienteTransaccion = new ProyectoCliente();
			}
		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
		return proyectoClienteTransaccion;
	}

	/**
	 * Obtiene el objeto de consulta
	 * 
	 * @return proyectoClienteConsulta
	 */
	public ProyectoCliente getProyectoClienteConsulta() {
		try {
			if (this.proyectoClienteConsulta == null) {
				this.proyectoClienteConsulta = new ProyectoCliente();
			}
		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
		return proyectoClienteConsulta;
	}

	/**
	 * Establece el objeto de consulta
	 * 
	 * @param proyectoClienteConsulta
	 */
	public void setProyectoClienteConsulta(ProyectoCliente proyectoClienteConsulta) {
		this.proyectoClienteConsulta = proyectoClienteConsulta;
	}

	/**
	 * Establece un proyecto cliente transacciï¿½n
	 * 
	 * @param proyectoClienteTransaccion
	 */
	public void setProyectoClienteTransaccion(ProyectoCliente proyectoClienteTransaccion) {
		this.proyectoClienteTransaccion = proyectoClienteTransaccion;
	}

	/**
	 * Obtiene un listado de los proyectos del cliente
	 * 
	 * @return proyectosCliente
	 */
	public List<ProyectoCliente> getProyectosCliente() {
		return proyectosCliente;
	}

	/**
	 * Establece un listado de los proyectos del cliente
	 * 
	 * @param proyectosCliente
	 */
	public void setProyectosCliente(List<ProyectoCliente> proyectosCliente) {
		this.proyectosCliente = proyectosCliente;
	}

	/**
	 * Obtiene un listado de items de proyectos
	 * 
	 * @return itemsProyectos
	 */
	public List<SelectItem> getItemsProyectos() {

		try {
			if (this.itemsProyectos == null) {
				this.itemsProyectos = new ArrayList<SelectItem>();
				Proyecto proyecto = new Proyecto();
				proyecto.setIndicativoVigencia(IConstantes.ACTIVO);
				List<Proyecto> proyectos = IConsultasDAO.getProyectos(proyecto);
				this.itemsProyectos.add(new SelectItem("", this.getMensaje("comboVacio")));
				if (proyectos != null && proyectos.size() > 0) {
					proyectos.forEach(p -> this.itemsProyectos.add(new SelectItem(p.getId(), p.getNombre())));
				}

			}
		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
		return itemsProyectos;
	}

	/**
	 * Establece un list de items de proyectos
	 * 
	 * @param itemsProyectos
	 */
	public void setItemsProyectos(List<SelectItem> itemsProyectos) {
		this.itemsProyectos = itemsProyectos;
	}

	/**
	 * Establece un list de items de consultores de proyectos
	 * 
	 * @return itemsConsultores
	 */
	public List<SelectItem> getItemsConsultores() {
		try {
			SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
			if (this.itemsConsultores == null) {
				this.itemsConsultores = new ArrayList<SelectItem>();
				List<Consultor> consultores = IConsultasDAO.getConsultores(null);

				if (this.administrarSesionCliente != null && this.administrarSesionCliente.getPersonalSesion() != null && this.administrarSesionCliente.getPersonalSesion().getId() != null && this.administrarSesionCliente.getPersonalSesion().getTipoUsuario().equals("A")) {

					this.itemsConsultores.add(new SelectItem("", this.getMensaje("comboVacio")));
				}

				if (consultores != null && consultores.size() > 0) {
					for (Consultor c : consultores) {

						if (this.administrarSesionCliente != null && this.administrarSesionCliente.getPersonalSesion() != null && this.administrarSesionCliente.getPersonalSesion().getId() != null && this.administrarSesionCliente.getPersonalSesion().getTipoUsuario().equals("CO") && this.administrarSesionCliente.getPersonalSesion().getId().intValue() == c.getId().intValue()) {
							if (c.getEstadoVigencia() != null && c.getEstadoVigencia().equals(IConstantes.ACTIVO)) {
								this.itemsConsultores.add(new SelectItem(c.getId(), c.getNombres() + " " + c.getApellidos()));
							} else if (c.getFechaInicio() != null && c.getFechaFin() != null && formato.format(c.getFechaInicio()).compareTo(formato.format(new Date())) <= 0 && formato.format(c.getFechaFin()).compareTo(formato.format(new Date())) >= 0) {
								this.itemsConsultores.add(new SelectItem(c.getId(), c.getNombres() + " " + c.getApellidos()));
							}
						} else if (this.administrarSesionCliente != null && this.administrarSesionCliente.getPersonalSesion() != null && this.administrarSesionCliente.getPersonalSesion().getId() != null && this.administrarSesionCliente.getPersonalSesion().getTipoUsuario().equals("A")) {

							if (c.getEstadoVigencia() != null && c.getEstadoVigencia().equals(IConstantes.ACTIVO)) {
								this.itemsConsultores.add(new SelectItem(c.getId(), c.getNombres() + " " + c.getApellidos()));
							} else if (c.getFechaInicio() != null && c.getFechaFin() != null && formato.format(c.getFechaInicio()).compareTo(formato.format(new Date())) <= 0 && formato.format(c.getFechaFin()).compareTo(formato.format(new Date())) >= 0) {
								this.itemsConsultores.add(new SelectItem(c.getId(), c.getNombres() + " " + c.getApellidos()));
							}
						}

					}
				}

			}
		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
		return itemsConsultores;
	}

	/**
	 * Establece un list de items de consultores
	 * 
	 * @param itemsConsultores
	 */
	public void setItemsConsultores(List<SelectItem> itemsConsultores) {
		this.itemsConsultores = itemsConsultores;
	}

	/**
	 * Obtiene los items de un proyecto cliente en ediciï¿½n. Los activo y si el
	 * analizado estï¿½ inactivo lo adiciona.
	 * 
	 * @return itemsProyectosEdicion
	 */
	public List<SelectItem> getItemsProyectosEdicion() {
		try {
			if (this.itemsProyectosEdicion == null) {
				this.itemsProyectosEdicion = new ArrayList<SelectItem>();
				Proyecto proyecto = new Proyecto();
				proyecto.setIndicativoVigencia(IConstantes.ACTIVO);
				List<Proyecto> proyectos = IConsultasDAO.getProyectos(proyecto);
				this.itemsProyectosEdicion.add(new SelectItem("", this.getMensaje("comboVacio")));
				if (proyectos != null && proyectos.size() > 0) {
					proyectos.forEach(p -> this.itemsProyectosEdicion.add(new SelectItem(p.getId(), p.getNombre())));

				}

				if (this.proyectoClienteTransaccion != null && this.proyectoClienteTransaccion.getId() != null && !this.itemsProyectosEdicion.stream().anyMatch(p -> p.getValue() != null && !p.getValue().equals("") && Integer.parseInt(p.getValue().toString()) == this.proyectoClienteTransaccion.getProyecto().getId().intValue())) {
					this.itemsProyectosEdicion.add(new SelectItem(this.proyectoClienteTransaccion.getProyecto().getId(), this.proyectoClienteTransaccion.getProyecto().getNombre()));
				}

			}

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
		return itemsProyectosEdicion;
	}

	/**
	 * Obtiene los items de un proyecto cliente en ediciï¿½n. Los activa y si el
	 * analizado estï¿½ inactivo lo adiciona.
	 */

	public void setItemsProyectosEdicion(List<SelectItem> itemsProyectosEdicion) {
		this.itemsProyectosEdicion = itemsProyectosEdicion;
	}

	/**
	 * Obtiene los consultores en ediciï¿½n
	 * 
	 * @return itemsConsultoresEdicion
	 */
	public List<SelectItem> getItemsConsultoresEdicion() {
		try {
			SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
			if (this.itemsConsultoresEdicion == null) {
				this.itemsConsultoresEdicion = new ArrayList<SelectItem>();
				List<Consultor> consultores = IConsultasDAO.getConsultores(null);
				this.itemsConsultoresEdicion.add(new SelectItem("", this.getMensaje("comboVacio")));
				if (consultores != null && consultores.size() > 0) {
					for (Consultor c : consultores) {

						if (this.administrarSesionCliente != null && this.administrarSesionCliente.getPersonalSesion() != null && this.administrarSesionCliente.getPersonalSesion().getId() != null && this.administrarSesionCliente.getPersonalSesion().getTipoUsuario().equals("CO") && this.administrarSesionCliente.getPersonalSesion().getId().intValue() == c.getId().intValue()) {

							if (c.getEstadoVigencia() != null && c.getEstadoVigencia().equals(IConstantes.ACTIVO)) {
								this.itemsConsultoresEdicion.add(new SelectItem(c.getId(), c.getNombres() + " " + c.getApellidos()));
							} else if (c.getFechaInicio() != null && c.getFechaFin() != null && formato.format(c.getFechaInicio()).compareTo(formato.format(new Date())) <= 0 && formato.format(c.getFechaFin()).compareTo(formato.format(new Date())) >= 0) {
								this.itemsConsultoresEdicion.add(new SelectItem(c.getId(), c.getNombres() + " " + c.getApellidos()));
							} else if (this.proyectoClienteTransaccion != null && this.proyectoClienteTransaccion.getId() != null && !this.itemsConsultoresEdicion.stream().anyMatch(p -> p.getValue() != null && !p.getValue().equals("") && Integer.parseInt(p.getValue().toString()) == this.proyectoClienteTransaccion.getConsultor().getId().intValue())) {
								this.itemsConsultoresEdicion.add(new SelectItem(this.proyectoClienteTransaccion.getConsultor().getId(), this.proyectoClienteTransaccion.getConsultor().getNombres() + " " + this.proyectoClienteTransaccion.getConsultor().getApellidos()));
							}

						} else if (this.administrarSesionCliente != null && this.administrarSesionCliente.getPersonalSesion() != null && this.administrarSesionCliente.getPersonalSesion().getId() != null && this.administrarSesionCliente.getPersonalSesion().getTipoUsuario().equals("A")) {
							if (c.getEstadoVigencia() != null && c.getEstadoVigencia().equals(IConstantes.ACTIVO)) {
								this.itemsConsultoresEdicion.add(new SelectItem(c.getId(), c.getNombres() + " " + c.getApellidos()));
							} else if (c.getFechaInicio() != null && c.getFechaFin() != null && formato.format(c.getFechaInicio()).compareTo(formato.format(new Date())) <= 0 && formato.format(c.getFechaFin()).compareTo(formato.format(new Date())) >= 0) {
								this.itemsConsultoresEdicion.add(new SelectItem(c.getId(), c.getNombres() + " " + c.getApellidos()));
							} else if (this.proyectoClienteTransaccion != null && this.proyectoClienteTransaccion.getId() != null && !this.itemsConsultoresEdicion.stream().anyMatch(p -> p.getValue() != null && !p.getValue().equals("") && Integer.parseInt(p.getValue().toString()) == this.proyectoClienteTransaccion.getConsultor().getId().intValue())) {
								this.itemsConsultoresEdicion.add(new SelectItem(this.proyectoClienteTransaccion.getConsultor().getId(), this.proyectoClienteTransaccion.getConsultor().getNombres() + " " + this.proyectoClienteTransaccion.getConsultor().getApellidos()));
							}
						}
					}
				}

			}
		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
		return itemsConsultoresEdicion;
	}

	/**
	 * Establece los consultores en ediciï¿½n
	 * 
	 * @param itemsConsultoresEdicion
	 */
	public void setItemsConsultoresEdicion(List<SelectItem> itemsConsultoresEdicion) {
		this.itemsConsultoresEdicion = itemsConsultoresEdicion;
	}

	/**
	 * 
	 * @return vistaActual
	 */
	public String getVistaActual() {
		return vistaActual;
	}

	/**
	 * 
	 * @param vistaActual
	 */
	public void setVistaActual(String vistaActual) {
		this.vistaActual = vistaActual;
	}

	/**
	 * Obtiene un documento de actividad
	 * 
	 * @return documentoActividad
	 */
	public DocumentoActividad getDocumentoActividad() {
		try {
			if (this.documentoActividad == null) {
				this.documentoActividad = new DocumentoActividad();
				this.documentoActividad.getTareaProyecto().setId(this.tareaProyectoTransaccion.getId());
			}
		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
		return documentoActividad;
	}

	/**
	 * 
	 * @param documentoActividad
	 */
	public void setDocumentoActividad(DocumentoActividad documentoActividad) {
		this.documentoActividad = documentoActividad;
	}

	/**
	 * 
	 * @return
	 */
	public DocumentoActividad getDocumentoActividadTransaccion() {
		try {
			if (this.documentoActividadTransaccion == null) {
				this.documentoActividadTransaccion = new DocumentoActividad();
				this.documentoActividadTransaccion.getTareaProyecto().setId(this.tareaProyectoTransaccion.getId());
			}
		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
		return documentoActividadTransaccion;
	}

	public void setDocumentoActividadTransaccion(DocumentoActividad documentoActividadTransaccion) {
		this.documentoActividadTransaccion = documentoActividadTransaccion;
	}

	/**
	 * Obtiene la pregunat a crear
	 * 
	 * @return preguntaProyecto
	 */
	public PreguntaProyecto getPreguntaProyecto() {
		if (this.preguntaProyecto == null) {
			this.preguntaProyecto = new PreguntaProyecto();
			this.preguntaProyecto.setProyecto(this.proyectoTransaccion);
		}
		return preguntaProyecto;
	}

	/**
	 * Establece la pregunta a crear
	 * 
	 * @param preguntaProyecto
	 */
	public void setPreguntaProyecto(PreguntaProyecto preguntaProyecto) {
		this.preguntaProyecto = preguntaProyecto;
	}

	/**
	 * Obtiene la pregunata a realizarle una transacciï¿½n
	 * 
	 * @return preguntaProyectoTransaccion
	 */
	public PreguntaProyecto getPreguntaProyectoTransaccion() {
		if (this.preguntaProyectoTransaccion == null) {
			this.preguntaProyectoTransaccion = new PreguntaProyecto();
		}
		return preguntaProyectoTransaccion;
	}

	/**
	 * Establece la pregunta a realizarle una transacciï¿½n
	 * 
	 * @param preguntaProyectoTransaccion
	 */
	public void setPreguntaProyectoTransaccion(PreguntaProyecto preguntaProyectoTransaccion) {
		this.preguntaProyectoTransaccion = preguntaProyectoTransaccion;
	}

	/**
	 * Obtiene la tarea a crear
	 * 
	 * @return tareaProyecto
	 */
	public TareaProyecto getTareaProyecto() {
		if (this.tareaProyecto == null) {
			this.tareaProyecto = new TareaProyecto();
			this.tareaProyecto.setProyecto(this.proyectoTransaccion);
		}
		return tareaProyecto;
	}

	/**
	 * Establece la tarea a crear
	 * 
	 * @param tareaProyecto
	 */
	public void setTareaProyecto(TareaProyecto tareaProyecto) {
		this.tareaProyecto = tareaProyecto;
	}

	/**
	 * Establece los parï¿½metros de auditorï¿½a
	 * 
	 * @return parametroAuditoria
	 */
	public ParametroAuditoria getParametroAuditoria() {
		try {
			if (parametroAuditoria == null) {
				this.parametroAuditoria = new ParametroAuditoria();
				this.parametroAuditoria = IConsultasDAO.getParametroAuditoria();
				this.parametroAuditoria.setId(IConstantes.ID_PARAMETRO_AUDITORIA);
			}
		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
		return parametroAuditoria;
	}

	public void setParametroAuditoria(ParametroAuditoria parametroAuditoria) {
		this.parametroAuditoria = parametroAuditoria;
	}

	/**
	 * Obtiene la tarea a realizar tarnsacciï¿½n
	 * 
	 * @return tareaProyectoTransaccion
	 */
	public TareaProyecto getTareaProyectoTransaccion() {
		if (this.tareaProyectoTransaccion == null) {
			this.tareaProyectoTransaccion = new TareaProyecto();
			this.tareaProyectoTransaccion.getProyecto().setId(this.proyectoTransaccion.getId());
		}
		return tareaProyectoTransaccion;
	}

	/**
	 * Establece las tareas de un proyecto en transacciï¿½n
	 * 
	 * @param tareaProyectoTransaccion
	 */
	public void setTareaProyectoTransaccion(TareaProyecto tareaProyectoTransaccion) {
		this.tareaProyectoTransaccion = tareaProyectoTransaccion;
	}

	/**
	 * Obtiene un listado de pregunta a realizarle transacciones
	 * 
	 * @return preguntas
	 */
	public List<PreguntaProyecto> getPreguntas() {
		try {
			if (this.preguntas == null) {

				PreguntaProyecto pregunta = new PreguntaProyecto();
				pregunta.setProyecto(this.proyectoTransaccion);

				this.preguntas = IConsultasDAO.getPreguntas(pregunta);

			}
		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
		return preguntas;
	}

	/**
	 * Establece un listado de preguntas a relizarle transacciones
	 * 
	 * @param preguntas
	 */
	public void setPreguntas(List<PreguntaProyecto> preguntas) {
		this.preguntas = preguntas;
	}

	/**
	 * Obtiene la etapa aplicada
	 * 
	 * @return etapa
	 */
	public String getEtapaAplicada(TareaProyecto aTarea) {
		String etapa = "";
		if (aTarea != null && aTarea.getNumeroEtapa() != null) {
			if (aTarea.getNumeroEtapa().intValue() == 1) {
				etapa = "Diagnï¿½stico";
			}
			if (aTarea.getNumeroEtapa().intValue() == 2) {
				etapa = "Aplica en todo el ciclo del proyecto";
			}
			if (aTarea.getNumeroEtapa().intValue() == 3) {
				etapa = "Documentaciï¿½n";
			}
		}

		return etapa;
	}

	/**
	 * Obtiene un listado de tareas
	 * 
	 * @return tareas
	 */
	public List<TareaProyecto> getTareas() {
		try {
			if (this.tareas == null) {

				TareaProyecto tarea = new TareaProyecto();
				tarea.setProyecto(this.proyectoTransaccion);

				this.tareas = IConsultasDAO.getTareas(tarea);

			}
		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
		return tareas;
	}

	/**
	 * Establece las tareas
	 * 
	 * @param tareas
	 */
	public void setTareas(List<TareaProyecto> tareas) {
		this.tareas = tareas;
	}

	/**
	 * Obtiene los documentos de una actividad
	 * 
	 * @return documentos
	 */
	public List<DocumentoActividad> getDocumentos() {
		try {
			SimpleDateFormat formatoFecha2 = new SimpleDateFormat("dd/MM/yyyy");
			if (this.documentos == null && this.tareaProyectoTransaccion != null && this.tareaProyectoTransaccion.getId() != null) {
				DocumentoActividad doc = new DocumentoActividad();
				doc.getTareaProyecto().setId(this.tareaProyectoTransaccion.getId());
				this.documentos = IConsultasDAO.getDocumentos(doc);

				if (this.documentos != null && this.documentos.size() > 0) {
					for (DocumentoActividad da : this.documentos) {
						if (da.getFecha() != null) {
							da.setNombre(da.getNombre() + " (" + formatoFecha2.format(da.getFecha()) + ")");
						}
					}
				}

			}
		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
		return documentos;
	}

	/**
	 * Establece los documentos
	 * 
	 * @param documentos
	 */
	public void setDocumentos(List<DocumentoActividad> documentos) {
		this.documentos = documentos;
	}

	public AdministrarSesionCliente getAdministrarSesionCliente() {
		return administrarSesionCliente;
	}

	public void setAdministrarSesionCliente(AdministrarSesionCliente administrarSesionCliente) {
		this.administrarSesionCliente = administrarSesionCliente;
	}

	public List<TareaProyecto> getTareasArchivo() {
		return tareasArchivo;
	}

	public void setTareasArchivo(List<TareaProyecto> tareasArchivo) {
		this.tareasArchivo = tareasArchivo;
	}

	public List<Estadistica> getRadicadosAuditoria() {
		return radicadosAuditoria;
	}

	public void setRadicadosAuditoria(List<Estadistica> radicadosAuditoria) {
		this.radicadosAuditoria = radicadosAuditoria;
	}

}
