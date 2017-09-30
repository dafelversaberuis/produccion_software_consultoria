package consultoria.modulos.personal;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;

import org.primefaces.event.FileUploadEvent;

import consultoria.Conexion;
import consultoria.beans.Cliente;
import consultoria.beans.Consultor;
import consultoria.beans.Personal;
import consultoria.beans.Plan;
import consultoria.beans.PlanCliente;
import consultoria.generales.ConsultarFuncionesAPI;
import consultoria.generales.IConstantes;
import consultoria.generales.IEmail;
import consultoria.modulos.IConsultasDAO;

@ManagedBean
@ViewScoped
public class AdministrarPersonal extends ConsultarFuncionesAPI implements Serializable {

	/**
	 * 
	 */
	private static final long		serialVersionUID	= -2221958861238708985L;

	private Personal						personal;
	private Personal						personalTransaccion;
	private Consultor						consultor;
	private Consultor						consultorTransaccion;
	private Cliente							clienteConsulta;
	private Cliente							cliente;
	private Cliente							clienteTransaccion;
	private String							vistaActual;

	private int									minutosGastados;

	private PlanCliente					preguntaProyecto;
	private PlanCliente					preguntaProyectoTransaccion;
	private Map<String, Object>	totales;

	private List<Personal>			administradores;
	private List<Consultor>			consultores;
	private List<Cliente>				clientes;
	private List<PlanCliente>		preguntas;
	private List<SelectItem>		itemsArbolitosDisponibles;

	// nuevos
	
	public void contarMinutos(){
		minutosGastados++;
	}
	
	public int getMinutosGastados() {
		return minutosGastados;
	}

	public void setMinutosGastados(int minutosGastados) {
		this.minutosGastados = minutosGastados;
	}
	
	
	

	public BigDecimal gePrecioMinutosGastadosB(Integer aMinutosGastados, Integer aMinutosPlan, BigDecimal aValorPlan) {
		BigDecimal precio = new BigDecimal(0);
		try {
			if (aMinutosPlan != null && aValorPlan != null && aMinutosGastados != null) {
				precio = (this.getValorRedondeado((aValorPlan.divide(new BigDecimal(aMinutosPlan), 10, RoundingMode.HALF_UP)).multiply(new BigDecimal(aMinutosGastados)), IConstantes.DECIMALES_REDONDEAR));
			}
		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}

		return precio;
	}

	public String gePrecioMinutosGastados(Integer aMinutosGastados, Integer aMinutosPlan, BigDecimal aValorPlan) {
		String precio = this.getMoneda(new BigDecimal(0));
		try {
			if (aMinutosPlan != null && aValorPlan != null && aMinutosGastados != null) {
				precio = this.getMoneda(this.getValorRedondeado((aValorPlan.divide(new BigDecimal(aMinutosPlan), 10, RoundingMode.HALF_UP)).multiply(new BigDecimal(aMinutosGastados)), IConstantes.DECIMALES_REDONDEAR));
			}
		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}

		return precio;
	}

	public String gePrecioMinuto(Integer aMinutos, BigDecimal aValorPlan) {
		String precio = "";
		try {
			if (aMinutos != null && aValorPlan != null) {
				precio += this.getMoneda(this.getValorRedondeado(aValorPlan.divide(new BigDecimal(aMinutos), 10, RoundingMode.HALF_UP), IConstantes.DECIMALES_REDONDEAR)) + "/ min";
			}
		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}

		return precio;
	}

	/**
	 * Crea un registro de pregunta de un proyecto
	 */
	public void crearPreguntaProyecto() {
		Conexion conexion = new Conexion();
		List<Plan> planes = null;
		try {

			conexion.setAutoCommitBD(false);

			this.preguntaProyecto.setFechaCompra(this.getFechaActualGmtColombia());
			this.preguntaProyecto.setMinutosConCosto(0);
			this.preguntaProyecto.setMinutosSinCosto(0);
			this.preguntaProyecto.setMinutosGastados(0);

			Plan temp = new Plan();
			temp.setId(this.preguntaProyecto.getPlan().getId());
			planes = IConsultasDAO.getArbolitos(temp);
			if (planes != null && planes.size() > 0) {
				this.preguntaProyecto.setPlan(planes.get(0));
			}
			this.preguntaProyecto.setMinutosComprados(this.preguntaProyecto.getPlan().getMinutos());
			this.preguntaProyecto.setPrecioVentaPesos(this.preguntaProyecto.getPlan().getPrecioVentaPesos());
			this.preguntaProyecto.setPrecioVentaPesosConIva(this.preguntaProyecto.getPlan().getPrecioVentaPesosConIva());
			this.preguntaProyecto.setIvaPesos(this.preguntaProyecto.getPlan().getIvaPesos());

			this.preguntaProyecto.getCamposBD();
			conexion.insertarBD(this.preguntaProyecto.getEstructuraTabla().getTabla(), this.preguntaProyecto.getEstructuraTabla().getPersistencia());

			conexion.commitBD();
			this.mostrarMensajeGlobal("creacionExitosa", "exito");

			// reseteo de variables
			this.preguntaProyecto = null;
			this.getPreguntaProyecto();
			this.preguntas = null;
			this.getPreguntas();

		} catch (Exception e) {
			conexion.rollbackBD();

			this.mostrarMensajeGlobal("transaccionFallida", "error");
		} finally {

			conexion.cerrarConexion();
		}
	}

	/**
	 * Asigna la preguna a editar o eliminar
	 * 
	 * @param aPregunta
	 * @param aVista
	 */
	public void asignarPreguntaProyecto(PlanCliente aPregunta, String aVista) {

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
	 * Elimina la pregunta de un proyecto
	 */
	public void eliminarPreguntaProyecto() {
		Conexion conexion = new Conexion();

		try {

			conexion.setAutoCommitBD(false);
			this.preguntaProyectoTransaccion.getCamposBD();
			conexion.eliminarBD(this.preguntaProyectoTransaccion.getEstructuraTabla().getTabla(), this.preguntaProyectoTransaccion.getEstructuraTabla().getLlavePrimaria());

			conexion.commitBD();

			this.mostrarMensajeGlobal("eliminacionExitosa", "exito");

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
	 * Este m�todo cancela la transacci�n de una pregunta
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
	 * Obtiene la pregunat a crear
	 * 
	 * @return preguntaProyecto
	 */
	public PlanCliente getPreguntaProyecto() {
		if (this.preguntaProyecto == null) {
			this.preguntaProyecto = new PlanCliente();
			this.preguntaProyecto.setCliente(this.clienteTransaccion);
		}
		return preguntaProyecto;
	}

	/**
	 * Establece la pregunta a crear
	 * 
	 * @param preguntaProyecto
	 */
	public void setPreguntaProyecto(PlanCliente preguntaProyecto) {
		this.preguntaProyecto = preguntaProyecto;
	}

	/**
	 * Obtiene la pregunata a realizarle una transacci�n
	 * 
	 * @return preguntaProyectoTransaccion
	 */
	public PlanCliente getPreguntaProyectoTransaccion() {
		if (this.preguntaProyectoTransaccion == null) {
			this.preguntaProyectoTransaccion = new PlanCliente();
		}
		return preguntaProyectoTransaccion;
	}

	/**
	 * Establece la pregunta a realizarle una transacci�n
	 * 
	 * @param preguntaProyectoTransaccion
	 */
	public void setPreguntaProyectoTransaccion(PlanCliente preguntaProyectoTransaccion) {
		this.preguntaProyectoTransaccion = preguntaProyectoTransaccion;
	}

	/**
	 * Obtiene un listado de pregunta a realizarle transacciones
	 * 
	 * @return preguntas
	 */
	public List<PlanCliente> getPreguntas() {
		try {
			if (this.preguntas == null) {

				PlanCliente pregunta = new PlanCliente();
				pregunta.setCliente(this.clienteTransaccion);

				this.preguntas = IConsultasDAO.getPlanesCliente(pregunta);

				this.totales = new HashMap<String, Object>();

				this.totales.put("precioPlan", new BigDecimal(0));
				this.totales.put("minutosAdquiridos", new Integer(0));
				this.totales.put("minutosCostoGastado", new Integer(0));
				this.totales.put("minutosCostoDisponible", new Integer(0));
				this.totales.put("minutosGratisGastado", new Integer(0));
				this.totales.put("costoGastado", new BigDecimal(0));
				this.totales.put("costoAhorrado", new BigDecimal(0));

				if (this.preguntas != null && this.preguntas.size() > 0) {
					for (PlanCliente a : this.preguntas) {

						this.totales.put("precioPlan", ((BigDecimal) this.totales.get("precioPlan")).add(a.getPrecioVentaPesosConIva()));
						this.totales.put("minutosAdquiridos", ((Integer) this.totales.get("minutosAdquiridos")) + a.getMinutosComprados().intValue());
						this.totales.put("minutosCostoGastado", ((Integer) this.totales.get("minutosCostoGastado")) + a.getMinutosConCosto().intValue());

						if ((a.getMinutosComprados().intValue() - a.getMinutosConCosto().intValue()) > 0) {
							this.totales.put("minutosCostoDisponible", ((Integer) this.totales.get("minutosCostoDisponible")) + (a.getMinutosComprados().intValue() - a.getMinutosConCosto().intValue()));

						}

						this.totales.put("minutosGratisGastado", ((Integer) this.totales.get("minutosGratisGastado")) + a.getMinutosSinCosto().intValue());

						this.totales.put("costoGastado", ((BigDecimal) this.totales.get("costoGastado")).add(gePrecioMinutosGastadosB(a.getMinutosConCosto(), a.getMinutosComprados(), a.getPrecioVentaPesosConIva())));
						this.totales.put("costoAhorrado", ((BigDecimal) this.totales.get("costoAhorrado")).add(gePrecioMinutosGastadosB(a.getMinutosSinCosto(), a.getMinutosComprados(), a.getPrecioVentaPesosConIva())));

					}
				}

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
	public void setPreguntas(List<PlanCliente> preguntas) {
		this.preguntas = preguntas;
	}

	// privados

	/**
	 * Obtiene una clave aleatoria num�rica de n d�gitos
	 * 
	 * @return clave
	 */
	public String getClaveAleatoria() {
		String clave = "";
		int n = 0;
		try {
			for (int i = 1; i <= IConstantes.NUMERO_DIGITOS_CLAVE_ALEATORIA.intValue(); i++) {
				n = (int) (10.0 * Math.random()) + 0;
				clave = clave + String.valueOf(n);

			}
		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
		return clave;

	}

	/**
	 * Valida un administrador
	 * 
	 * @param aTransaccion
	 * @return ok
	 */
	private boolean isValidoAdministrador(String aTransaccion) {
		boolean ok = true;

		if (aTransaccion.equals("C")) {
			if (this.administradores != null && this.administradores.size() > 0 && this.administradores.stream().anyMatch(i -> i.getCorreoElectronico().trim().toLowerCase().equals(this.personal.getCorreoElectronico().trim().toLowerCase()))) {
				ok = false;
				this.mostrarMensajeGlobal("correoExistenteAdministrador", "advertencia");
			}
			if (this.isVacio(this.personal.getNombres())) {
				ok = false;
				this.mostrarMensajeGlobal("nombresVacios", "advertencia");
			}

			if (this.isVacio(this.personal.getApellidos())) {
				ok = false;
				this.mostrarMensajeGlobal("apellidosVacios", "advertencia");
			}

			if (this.isVacio(this.personal.getCorreoElectronico())) {
				ok = false;
				this.mostrarMensajeGlobal("correoVacio", "advertencia");
			}

		} else {

			if (this.administradores != null && this.administradores.size() > 0 && this.administradores.stream().anyMatch(i -> i.getId() != this.personalTransaccion.getId() && i.getCorreoElectronico().trim().toLowerCase().equals(this.personalTransaccion.getCorreoElectronico().trim().toLowerCase()))) {
				ok = false;
				this.mostrarMensajeGlobal("correoExistenteAdministrador", "advertencia");
			}
			if (this.isVacio(this.personalTransaccion.getNombres())) {
				ok = false;
				this.mostrarMensajeGlobal("nombresVacios", "advertencia");
			}

			if (this.isVacio(this.personalTransaccion.getApellidos())) {
				ok = false;
				this.mostrarMensajeGlobal("apellidosVacios", "advertencia");
			}

			if (this.isVacio(this.getPersonalTransaccion().getCorreoElectronico())) {
				ok = false;
				this.mostrarMensajeGlobal("correoVacio", "advertencia");
			}

		}

		return ok;
	}

	/**
	 * Detremina si un cliente es v�lido
	 * 
	 * @param aTransaccion
	 * @return ok
	 */
	private boolean isValidoCliente(String aTransaccion) {
		boolean ok = true;

		try {

			boolean clienteExistente = false;
			String parteA = null;
			String parteB = null;
			List<Cliente> clientes = null;

			if (aTransaccion.equals("C")) {

				if (this.isVacio(this.cliente.getCorreoElectronico())) {
					ok = false;
					this.mostrarMensajeGlobal("correoVacio", "advertencia");
				} else {
					Cliente clienteTemp = new Cliente();
					clienteTemp.setCorreoElectronico(this.cliente.getCorreoElectronico().trim());
					clientes = IConsultasDAO.getClientes(clienteTemp);

					if (clientes != null && clientes.size() > 0 && clientes.stream().anyMatch(i -> i.getCorreoElectronico().trim().toLowerCase().equals(this.cliente.getCorreoElectronico().trim().toLowerCase()))) {
						ok = false;
						this.mostrarMensajeGlobal("correoExistenteCliente", "advertencia");
					}

				}

				if (this.isVacio(this.cliente.getCliente())) {
					ok = false;
					this.mostrarMensajeGlobal("clienteVacio", "advertencia");
				}

				if (this.isVacio(this.cliente.getNit())) {
					ok = false;
					this.mostrarMensajeGlobal("nitVacio", "advertencia");
				} else {
					this.cliente.setNit(this.cliente.getNit().trim());
				}

				if (this.isVacio(this.cliente.getRepresentante())) {
					ok = false;
					this.mostrarMensajeGlobal("representanteVacio", "advertencia");
				}

				if (!this.isVacio(this.cliente.getTelefono())) {
					this.cliente.setTelefono(this.cliente.getTelefono().trim());
					// ok = false;
					// this.mostrarMensajeGlobal("telefonoVacio", "advertencia");
				}

				// analiza nit
				if (!this.isVacio(this.cliente.getNit())) {
					String[] partes = this.cliente.getNit().split("-");

					if (partes != null && partes.length == 1) {
						parteA = this.cliente.getNit().toUpperCase().trim();
						this.cliente.setNit(parteA);

					} else {
						parteA = partes[0].toUpperCase().trim();
						parteB = partes[0].toUpperCase().trim() + "-" + partes[1].toUpperCase().trim();

						this.cliente.setNit(parteB);
					}

					clienteExistente = IConsultasDAO.getClienteExistente(parteA);
					if (clienteExistente) {
						ok = false;
						this.mostrarMensajeGlobal("clienteExistente", "advertencia");

					}

				}

			} else {

				if (this.isVacio(this.clienteTransaccion.getCorreoElectronico())) {
					ok = false;
					this.mostrarMensajeGlobal("correoVacio", "advertencia");
				} else {

					Cliente clienteTemp = new Cliente();
					clienteTemp.setCorreoElectronico(this.clienteTransaccion.getCorreoElectronico().trim());
					clientes = IConsultasDAO.getClientes(clienteTemp);

					if (clientes != null && clientes.size() > 0 && clientes.stream().anyMatch(i -> i.getId().intValue() != this.clienteTransaccion.getId().intValue() && i.getCorreoElectronico().trim().toLowerCase().equals(this.clienteTransaccion.getCorreoElectronico().trim().toLowerCase()))) {
						ok = false;
						this.mostrarMensajeGlobal("correoExistenteCliente", "advertencia");
					}

				}

				if (this.isVacio(this.clienteTransaccion.getCliente())) {
					ok = false;
					this.mostrarMensajeGlobal("clienteVacio", "advertencia");
				}

				if (this.isVacio(this.clienteTransaccion.getNit())) {
					ok = false;
					this.mostrarMensajeGlobal("nitVacio", "advertencia");
				}

				if (this.isVacio(this.clienteTransaccion.getRepresentante())) {
					ok = false;
					this.mostrarMensajeGlobal("representanteVacio", "advertencia");
				}

				if (!this.isVacio(this.clienteTransaccion.getTelefono())) {
					this.clienteTransaccion.setTelefono(this.clienteTransaccion.getTelefono().trim());
					// ok = false;
					// this.mostrarMensajeGlobal("telefonoVacio", "advertencia");
				}

				// analiza nit
				if (!this.isVacio(this.clienteTransaccion.getNit())) {
					String[] partes = this.clienteTransaccion.getNit().split("-");

					if (partes != null && partes.length == 1) {
						parteA = this.clienteTransaccion.getNit().toUpperCase().trim();
						this.clienteTransaccion.setNit(parteA);

					} else {
						parteA = partes[0].toUpperCase().trim();
						parteB = partes[0].toUpperCase().trim() + "-" + partes[1].toUpperCase().trim();

						this.clienteTransaccion.setNit(parteB);
					}

					if (!this.clienteTransaccion.gettCopiaNit().trim().toUpperCase().equals(parteA)) {
						clienteExistente = IConsultasDAO.getClienteExistente(parteA);
						if (clienteExistente) {
							ok = false;
							this.mostrarMensajeGlobal("clienteExistente", "advertencia");

						}
					}

				}

			}

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}

		return ok;
	}

	/**
	 * Limpia la foto cargada para cargar otra
	 */
	public void limpiarFotoCargada() {
		this.consultorTransaccion.settFile(null);
		this.consultorTransaccion.setArchivo(null);
	}

	/**
	 * Recibir foto y asignara aobjeto
	 * 
	 * @param event
	 */
	public void recibirFoto(FileUploadEvent event) {

		try {
			this.consultorTransaccion.settFile(event.getFile());
			this.consultorTransaccion.setArchivo(event.getFile().getContents());

			this.mostrarMensajeGlobal("archivoRecibido", "advertencia");
		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}

	}

	/**
	 * Valida un consultor
	 * 
	 * @param aTransaccion
	 * @return ok
	 */
	private boolean isValidoConsultor(String aTransaccion) {
		boolean ok = true;
		SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");

		if (aTransaccion.equals("C")) {
			if (this.consultores != null && this.consultores.size() > 0 && this.consultores.stream().anyMatch(i -> i.getCorreoElectronico().trim().toLowerCase().equals(this.consultor.getCorreoElectronico().trim().toLowerCase()))) {
				ok = false;
				this.mostrarMensajeGlobal("correoExistenteConsultor", "advertencia");
			}
			if (this.isVacio(this.consultor.getNombres())) {
				ok = false;
				this.mostrarMensajeGlobal("nombresVacios", "advertencia");
			}

			if (this.isVacio(this.consultor.getApellidos())) {
				ok = false;
				this.mostrarMensajeGlobal("apellidosVacios", "advertencia");
			}

			if (this.isVacio(this.consultor.getCorreoElectronico())) {
				ok = false;
				this.mostrarMensajeGlobal("correoVacio", "advertencia");
			}

			if (this.isVacio(this.consultor.getTelefono())) {
				ok = false;
				this.mostrarMensajeGlobal("telefonoVacio", "advertencia");
			}

			if (this.consultor.getPerfil() != null) {
				if (!this.isVacio(this.consultor.getPerfil())) {
					this.consultor.setPerfil(this.consultor.getPerfil().trim());
				}
			}

			if (this.consultor.getFechaInicio() != null && this.consultor.getFechaFin() != null && formato.format(this.consultor.getFechaFin()).compareTo(formato.format(this.consultor.getFechaInicio())) < 0) {
				ok = false;
				this.mostrarMensajeGlobal("fechaFinInvalida", "advertencia");
			}

		} else {

			if (this.consultores != null && this.consultores.size() > 0 && this.consultores.stream().anyMatch(i -> i.getId() != this.consultorTransaccion.getId() && i.getCorreoElectronico().trim().toLowerCase().equals(this.consultorTransaccion.getCorreoElectronico().trim().toLowerCase()))) {
				ok = false;
				this.mostrarMensajeGlobal("correoExistenteConsultor", "advertencia");
			}
			if (this.isVacio(this.consultorTransaccion.getNombres())) {
				ok = false;
				this.mostrarMensajeGlobal("nombresVacios", "advertencia");
			}

			if (this.isVacio(this.consultorTransaccion.getApellidos())) {
				ok = false;
				this.mostrarMensajeGlobal("apellidosVacios", "advertencia");
			}

			if (this.isVacio(this.consultorTransaccion.getCorreoElectronico())) {
				ok = false;
				this.mostrarMensajeGlobal("correoVacio", "advertencia");
			}

			if (this.isVacio(this.consultorTransaccion.getTelefono())) {
				ok = false;
				this.mostrarMensajeGlobal("telefonoVacio", "advertencia");
			}

			if (this.consultorTransaccion.getPerfil() != null) {
				if (!this.isVacio(this.consultorTransaccion.getPerfil())) {
					this.consultorTransaccion.setPerfil(this.consultorTransaccion.getPerfil().trim());
				}
			}

			if (this.consultorTransaccion.getFechaInicio() != null && this.consultorTransaccion.getFechaFin() != null && formato.format(this.consultorTransaccion.getFechaFin()).compareTo(formato.format(this.consultorTransaccion.getFechaInicio())) < 0) {
				ok = false;
				this.mostrarMensajeGlobal("fechaFinInvalida", "advertencia");
			}

		}

		return ok;
	}

	// publicos

	/**
	 * Consulta los clientes
	 */
	public void consultarClientes() {
		Conexion conexion = new Conexion();
		try {

			this.clientes = IConsultasDAO.getClientes(this.clienteConsulta);

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		} finally {
			conexion.cerrarConexion();
		}

	}

	/**
	 * Obtiene el texto que se va a mostrar en la grilla del consultor
	 * 
	 * @param aConsultor
	 * @return texto
	 */
	public String getTextoVigenciaConsultor(Consultor aConsultor) {
		String texto = "";
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
		if (aConsultor != null && aConsultor.getEstadoVigencia() != null) {
			if (aConsultor.getEstadoVigencia().equals(IConstantes.ACTIVO)) {
				texto = IConstantes.LABEL_ACTIVO;
			} else {
				texto = IConstantes.LABEL_INACTIVO;
			}

		} else {
			texto = formato.format(aConsultor.getFechaInicio()) + " a " + formato.format(aConsultor.getFechaFin());

		}

		return texto;
	}

	/**
	 * Determina si un consultor est� vigente
	 * 
	 * @param aConsultor
	 * @return vigente
	 */
	public boolean isConsultorVigente(Consultor aConsultor) {
		boolean vigente = false;
		SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
		if (aConsultor != null && aConsultor.getEstadoVigencia() != null) {
			if (aConsultor.getEstadoVigencia().equals(IConstantes.ACTIVO)) {
				vigente = true;
			} else {
				vigente = false;
			}

		} else {

			if (formato.format(aConsultor.getFechaInicio()).compareTo(formato.format(new Date())) <= 0 && formato.format(aConsultor.getFechaFin()).compareTo(formato.format(new Date())) >= 0) {
				vigente = true;
			} else {
				vigente = false;
			}

		}

		return vigente;
	}

	/**
	 * Permite crear un cliente
	 */
	public void crearCliente() {
		Conexion conexion = new Conexion();

		try {
			if (isValidoCliente("C")) {
				conexion.setAutoCommitBD(false);

				this.cliente.setCliente(this.getSinEspacios(this.cliente.getCliente()));
				this.cliente.setNit(this.getSinEspacios(this.cliente.getNit()));
				this.cliente.setRepresentante(this.getSinEspacios(this.cliente.getRepresentante()));
				this.cliente.setCorreoElectronico(this.cliente.getCorreoElectronico().trim());
				this.cliente.setTelefono(this.cliente.getTelefono().trim());
				this.cliente.setClave(this.getClaveAleatoria());

				this.cliente.getCamposBD();

				conexion.insertarBD(this.cliente.getEstructuraTabla().getTabla(), this.cliente.getEstructuraTabla().getPersistencia());
				conexion.commitBD();

				this.mostrarMensajeGlobal("creacionExitosa", "exito");
				this.mostrarMensajeGlobalPersonalizado(this.getMensaje("claveAleatoria", this.cliente.getClave()), "exito");

				IEmail.enviarCorreo(this.getMensaje("mensajeClaveAleatoria", this.cliente.getCliente(), this.cliente.getClave()), this.getMensaje("asuntoClaveAleatoria"), this.cliente.getCorreoElectronico());
				this.mostrarMensajeGlobalPersonalizado(this.getMensaje("claveCorreoExitoso", this.cliente.getCorreoElectronico()), "exito");

				// reseteo de variables

				this.clienteConsulta = null;
				this.getClienteConsulta();
				this.clienteConsulta.setNit(this.cliente.getNit());

				// reseteo de variables
				this.cliente = null;
				this.getCliente();
				this.clientes = null;
				this.consultarClientes();
			}

		} catch (Exception e) {
			conexion.rollbackBD();
			this.mostrarMensajeGlobal("transaccionFallida", "error");
		} finally {
			conexion.cerrarConexion();
		}

	}

	/**
	 * Cancela y resetea criterios de consulta y resultados antes cargados
	 */
	public void cancelarConsulta() {
		this.clienteConsulta = null;
		this.getClienteConsulta();

		this.clientes = null;
	}

	/**
	 * Permite crear un consultor
	 */
	public void crearConsultor() {
		Conexion conexion = new Conexion();

		try {
			if (isValidoConsultor("C")) {
				conexion.setAutoCommitBD(false);

				if (this.consultor.gettTipoEstado() != null && this.consultor.gettTipoEstado().equals(IConstantes.TIPO_VIGENCIA_MANUAL)) {
					this.consultor.setFechaInicio(null);
					this.consultor.setFechaFin(null);

				} else {

					this.consultor.setEstadoVigencia(null);

				}

				this.consultor.setNombres(this.getSinEspacios(this.consultor.getNombres()));
				this.consultor.setApellidos(this.getSinEspacios(this.consultor.getApellidos()));
				this.consultor.setCorreoElectronico(this.consultor.getCorreoElectronico().trim());
				this.consultor.setTelefono(this.consultor.getTelefono().trim());
				this.consultor.setClave(this.getClaveAleatoria());

				this.consultor.getCamposBD();

				conexion.insertarBD(this.consultor.getEstructuraTabla().getTabla(), this.consultor.getEstructuraTabla().getPersistencia());
				conexion.commitBD();

				this.mostrarMensajeGlobal("creacionExitosa", "exito");
				this.mostrarMensajeGlobalPersonalizado(this.getMensaje("claveAleatoria", this.consultor.getClave()), "exito");

				IEmail.enviarCorreo(this.getMensaje("mensajeClaveAleatoria", this.consultor.getNombres() + " " + this.consultor.getApellidos(), this.consultor.getClave()), this.getMensaje("asuntoClaveAleatoria"), this.consultor.getCorreoElectronico());
				this.mostrarMensajeGlobalPersonalizado(this.getMensaje("claveCorreoExitoso", this.consultor.getCorreoElectronico()), "exito");

				// reseteo de variables
				this.consultor = null;
				this.getConsultor();
				this.consultores = null;
				this.getConsultores();
			}

		} catch (Exception e) {
			conexion.rollbackBD();
			this.mostrarMensajeGlobal("transaccionFallida", "error");
		} finally {
			conexion.cerrarConexion();
		}

	}

	/**
	 * Crea un nuevo administrador del software
	 */
	public void crearAdministrador() {
		Conexion conexion = new Conexion();

		try {
			if (isValidoAdministrador("C")) {
				conexion.setAutoCommitBD(false);

				this.personal.setEstadoVigencia(IConstantes.ACTIVO);
				this.personal.setNombres(this.getSinEspacios(this.personal.getNombres()));
				this.personal.setApellidos(this.getSinEspacios(this.personal.getApellidos()));
				this.personal.setCorreoElectronico(this.personal.getCorreoElectronico().trim());
				this.personal.setClave(this.getClaveAleatoria());

				this.personal.getCamposBD();

				conexion.insertarBD(this.personal.getEstructuraTabla().getTabla(), this.personal.getEstructuraTabla().getPersistencia());
				conexion.commitBD();

				this.mostrarMensajeGlobal("creacionExitosa", "exito");
				this.mostrarMensajeGlobalPersonalizado(this.getMensaje("claveAleatoria", this.getPersonal().getClave()), "exito");

				IEmail.enviarCorreo(this.getMensaje("mensajeClaveAleatoria", this.personal.getNombres() + " " + this.personal.getApellidos(), this.personal.getClave()), this.getMensaje("asuntoClaveAleatoria"), this.personal.getCorreoElectronico());
				this.mostrarMensajeGlobalPersonalizado(this.getMensaje("claveCorreoExitoso", this.getPersonal().getCorreoElectronico()), "exito");

				// reseteo de variables
				this.personal = null;
				this.getPersonal();
				this.administradores = null;
				this.getAdministradores();
			}

		} catch (Exception e) {
			conexion.rollbackBD();
			this.mostrarMensajeGlobal("transaccionFallida", "error");
		} finally {
			conexion.cerrarConexion();
		}

	}

	/**
	 * Genera una nueva clave aleatoria para el administrador
	 */
	public void generarClaveAdministrador() {

		Conexion conexion = new Conexion();

		try {
			boolean ok = true;
			if (this.personalTransaccion != null && this.personalTransaccion.gettTipoClave() != null && this.personalTransaccion.gettTipoClave().equals("A")) {

				this.personalTransaccion.setClave(this.getClaveAleatoria());

			} else {

				if (this.isVacio(this.personalTransaccion.getClave())) {
					ok = false;
				}

			}
			if (ok) {
				conexion.setAutoCommitBD(false);

				this.personalTransaccion.getCamposBD();

				conexion.actualizarBD(this.personalTransaccion.getEstructuraTabla().getTabla(), this.personalTransaccion.getEstructuraTabla().getPersistencia(), this.personalTransaccion.getEstructuraTabla().getLlavePrimaria(), null);
				conexion.commitBD();

				this.mostrarMensajeGlobalPersonalizado(this.getMensaje("claveAleatoria", this.personalTransaccion.getClave()), "exito");

				IEmail.enviarCorreo(this.getMensaje("mensajeClaveAleatoria", this.personalTransaccion.getNombres() + " " + this.personalTransaccion.getApellidos(), this.personalTransaccion.getClave()), this.getMensaje("asuntoClaveAleatoria"), this.personalTransaccion.getCorreoElectronico());
				this.mostrarMensajeGlobalPersonalizado(this.getMensaje("claveCorreoExitoso", this.personalTransaccion.getCorreoElectronico()), "exito");

				this.cerrarModal("panelClaveAdministrador");

				// reseteo de variables
				this.personalTransaccion = null;
				this.getPersonalTransaccion();
				this.administradores = null;
				this.getAdministradores();

			} else {

				this.mostrarMensajeGlobal("claveEnBlanco", "error");
			}

		} catch (Exception e) {
			conexion.rollbackBD();
			this.mostrarMensajeGlobal("transaccionFallida", "error");
		} finally {
			conexion.cerrarConexion();
		}

	}

	/**
	 * Genera la clave del cliente
	 */
	public void generarClaveCliente() {

		Conexion conexion = new Conexion();

		try {
			boolean ok = true;
			if (this.clienteTransaccion != null && this.clienteTransaccion.gettTipoClave() != null && this.clienteTransaccion.gettTipoClave().equals("A")) {

				this.clienteTransaccion.setClave(this.getClaveAleatoria());

			} else {

				if (this.isVacio(this.clienteTransaccion.getClave())) {
					ok = false;
				}

			}
			if (ok) {
				conexion.setAutoCommitBD(false);

				this.clienteTransaccion.getCamposBD();

				conexion.actualizarBD(this.clienteTransaccion.getEstructuraTabla().getTabla(), this.clienteTransaccion.getEstructuraTabla().getPersistencia(), this.clienteTransaccion.getEstructuraTabla().getLlavePrimaria(), null);
				conexion.commitBD();

				this.mostrarMensajeGlobalPersonalizado(this.getMensaje("claveAleatoria", this.clienteTransaccion.getClave()), "exito");

				IEmail.enviarCorreo(this.getMensaje("mensajeClaveAleatoria", this.clienteTransaccion.getCliente(), this.clienteTransaccion.getClave()), this.getMensaje("asuntoClaveAleatoria"), this.clienteTransaccion.getCorreoElectronico());
				this.mostrarMensajeGlobalPersonalizado(this.getMensaje("claveCorreoExitoso", this.clienteTransaccion.getCorreoElectronico()), "exito");

				this.cerrarModal("panelClaveCliente");

				// reseteo de variables
				this.clienteTransaccion = null;
				this.getClienteTransaccion();
				this.clientes = null;
				consultarClientes();

			} else {

				this.mostrarMensajeGlobal("claveEnBlanco", "error");
			}

		} catch (Exception e) {
			conexion.rollbackBD();
			this.mostrarMensajeGlobal("transaccionFallida", "error");
		} finally {
			conexion.cerrarConexion();
		}

	}

	/**
	 * Genera una nueva clave para el consultor
	 */
	public void generarClaveConsultor() {

		Conexion conexion = new Conexion();

		try {
			boolean ok = true;
			if (this.consultorTransaccion != null && this.consultorTransaccion.gettTipoClave() != null && this.consultorTransaccion.gettTipoClave().equals("A")) {

				this.consultorTransaccion.setClave(this.getClaveAleatoria());

			} else {

				if (this.isVacio(this.consultorTransaccion.getClave())) {
					ok = false;
				}

			}
			if (ok) {
				conexion.setAutoCommitBD(false);

				this.consultorTransaccion.getCamposBD();

				conexion.actualizarBD(this.consultorTransaccion.getEstructuraTabla().getTabla(), this.consultorTransaccion.getEstructuraTabla().getPersistencia(), this.consultorTransaccion.getEstructuraTabla().getLlavePrimaria(), null);
				conexion.commitBD();

				this.mostrarMensajeGlobalPersonalizado(this.getMensaje("claveAleatoria", this.consultorTransaccion.getClave()), "exito");

				IEmail.enviarCorreo(this.getMensaje("mensajeClaveAleatoria", this.consultorTransaccion.getNombres() + " " + this.consultorTransaccion.getApellidos(), this.consultorTransaccion.getClave()), this.getMensaje("asuntoClaveAleatoria"), this.consultorTransaccion.getCorreoElectronico());
				this.mostrarMensajeGlobalPersonalizado(this.getMensaje("claveCorreoExitoso", this.consultorTransaccion.getCorreoElectronico()), "exito");

				this.cerrarModal("panelClaveConsultor");

				// reseteo de variables
				this.consultorTransaccion = null;
				this.getConsultorTransaccion();
				this.consultores = null;
				this.getConsultores();

			} else {

				this.mostrarMensajeGlobal("claveEnBlanco", "error");
			}

		} catch (Exception e) {
			conexion.rollbackBD();
			this.mostrarMensajeGlobal("transaccionFallida", "error");
		} finally {
			conexion.cerrarConexion();
		}

	}

	/**
	 * Edita un registro de administrador de software
	 */
	public void editarAdministrador() {
		Conexion conexion = new Conexion();

		try {
			if (isValidoAdministrador("E")) {
				conexion.setAutoCommitBD(false);

				this.personalTransaccion.setNombres(this.getSinEspacios(this.personalTransaccion.getNombres()));
				this.personalTransaccion.setApellidos(this.getSinEspacios(this.personalTransaccion.getApellidos()));

				this.personalTransaccion.getCamposBD();
				conexion.actualizarBD(this.personalTransaccion.getEstructuraTabla().getTabla(), this.personalTransaccion.getEstructuraTabla().getPersistencia(), this.personalTransaccion.getEstructuraTabla().getLlavePrimaria(), null);
				conexion.commitBD();
				this.mostrarMensajeGlobal("edicionExitosa", "exito");
				this.mostrarMensajeGlobal("algunosCambios", "advertencia");
				this.cerrarModal("panelEdicionAdministrador");

				// reseteo de variables
				this.personalTransaccion = null;
				this.getPersonalTransaccion();
				this.administradores = null;
				this.getAdministradores();
			}

		} catch (Exception e) {
			conexion.rollbackBD();
			this.mostrarMensajeGlobal("transaccionFallida", "error");
		} finally {
			conexion.cerrarConexion();
		}

	}

	/**
	 * Edita los datos de un cliente
	 */
	public void editarCliente() {
		Conexion conexion = new Conexion();

		try {
			if (isValidoCliente("E")) {
				conexion.setAutoCommitBD(false);

				this.clienteTransaccion.setCliente(this.getSinEspacios(this.clienteTransaccion.getCliente()));
				this.clienteTransaccion.setNit(this.getSinEspacios(this.clienteTransaccion.getNit()));
				this.clienteTransaccion.setRepresentante(this.getSinEspacios(this.clienteTransaccion.getRepresentante()));
				this.clienteTransaccion.setCorreoElectronico(this.getSinEspacios(this.clienteTransaccion.getCorreoElectronico()));
				this.clienteTransaccion.setTelefono(this.getSinEspacios(this.clienteTransaccion.getTelefono()));

				this.clienteTransaccion.getCamposBD();
				conexion.actualizarBD(this.clienteTransaccion.getEstructuraTabla().getTabla(), this.clienteTransaccion.getEstructuraTabla().getPersistencia(), this.clienteTransaccion.getEstructuraTabla().getLlavePrimaria(), null);
				conexion.commitBD();
				this.mostrarMensajeGlobal("edicionExitosa", "exito");
				this.mostrarMensajeGlobal("algunosCambios", "advertencia");
				this.cerrarModal("panelEdicionCliente");

				// reseteo de variables
				this.clienteTransaccion = null;
				this.getClienteTransaccion();
				this.clientes = null;
				this.consultarClientes();
			}

		} catch (Exception e) {
			conexion.rollbackBD();
			this.mostrarMensajeGlobal("transaccionFallida", "error");
		} finally {
			conexion.cerrarConexion();
		}

	}

	/**
	 * Edita los datos de un consultor
	 */
	public void editarConsultor() {
		Conexion conexion = new Conexion();

		try {
			if (isValidoConsultor("E")) {
				conexion.setAutoCommitBD(false);

				this.consultorTransaccion.setNombres(this.getSinEspacios(this.consultorTransaccion.getNombres()));
				this.consultorTransaccion.setApellidos(this.getSinEspacios(this.consultorTransaccion.getApellidos()));
				this.consultorTransaccion.setTelefono(this.getSinEspacios(this.consultorTransaccion.getTelefono()));

				if (this.consultorTransaccion.gettTipoEstado() != null && this.consultorTransaccion.gettTipoEstado().equals(IConstantes.TIPO_VIGENCIA_MANUAL)) {
					this.consultorTransaccion.setFechaInicio(null);
					this.consultorTransaccion.setFechaFin(null);

				} else {

					this.consultorTransaccion.setEstadoVigencia(null);

				}

				this.consultorTransaccion.getCamposBD();
				conexion.actualizarBD(this.consultorTransaccion.getEstructuraTabla().getTabla(), this.consultorTransaccion.getEstructuraTabla().getPersistencia(), this.consultorTransaccion.getEstructuraTabla().getLlavePrimaria(), null);
				conexion.commitBD();
				this.mostrarMensajeGlobal("edicionExitosa", "exito");
				this.mostrarMensajeGlobal("algunosCambios", "advertencia");
				this.cerrarModal("panelEdicionConsultor");

				// reseteo de variables
				this.consultorTransaccion = null;
				this.getConsultorTransaccion();
				this.consultores = null;
				this.getConsultores();
			}

		} catch (Exception e) {
			conexion.rollbackBD();
			this.mostrarMensajeGlobal("transaccionFallida", "error");
		} finally {
			conexion.cerrarConexion();
		}

	}

	/**
	 * Elimina un cliente
	 */
	public void eliminarCliente() {

		Conexion conexion = new Conexion();
		try {

			conexion.setAutoCommitBD(false);
			this.clienteTransaccion.getCamposBD();
			conexion.eliminarBD(this.clienteTransaccion.getEstructuraTabla().getTabla(), this.clienteTransaccion.getEstructuraTabla().getLlavePrimaria());
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
		this.clienteTransaccion = null;
		this.getClienteTransaccion();
		this.clientes = null;
		this.consultarClientes();

	}

	/**
	 * Elimina el consultor
	 */
	public void eliminarConsultor() {

		Conexion conexion = new Conexion();
		try {

			conexion.setAutoCommitBD(false);
			this.consultorTransaccion.getCamposBD();
			conexion.eliminarBD(this.consultorTransaccion.getEstructuraTabla().getTabla(), this.consultorTransaccion.getEstructuraTabla().getLlavePrimaria());
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
		this.consultorTransaccion = null;
		this.consultores = null;
		this.getConsultores();

	}

	/**
	 * Elimina un registro de adminiistador
	 */
	public void eliminarAdministrador() {

		Conexion conexion = new Conexion();
		try {

			conexion.setAutoCommitBD(false);
			this.personalTransaccion.getCamposBD();
			conexion.eliminarBD(this.personalTransaccion.getEstructuraTabla().getTabla(), this.personalTransaccion.getEstructuraTabla().getLlavePrimaria());
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
		this.personalTransaccion = null;
		this.administradores = null;
		this.getAdministradores();

	}

	/**
	 * Este m�todo borra el formulario de creaci�n de un administrador
	 */
	public void cancelarAdministrador() {

		try {
			this.personal = new Personal();

			this.administradores = null;
			this.getAdministradores();
		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}

	}

	/**
	 * Cancela la creaci�n de un cliente
	 */
	public void cancelarCliente() {

		try {
			this.cliente = null;
			this.getCliente();
		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}

	}

	/**
	 * Este m�todo cancela la informaci�n de un consultor
	 */
	public void cancelarConsultor() {

		try {
			this.consultor = new Consultor();

			this.consultores = null;
			this.getConsultores();
		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}

	}

	/**
	 * Cancela un cliente en transacci�n
	 * 
	 * @param aVista
	 */
	public void cancelarClienteTransaccion(String aVista) {
		try {

			this.clienteTransaccion = new Cliente();
			this.clientes = null;
			this.preguntas = null;
			this.consultarClientes();

			if (aVista != null && aVista.equals("MODAL_EDITAR_CLIENTE")) {
				this.cerrarModal("panelEdicionCliente");

			} else if (aVista != null && aVista.equals("MODAL_CLAVE_CLIENTE")) {
				this.cerrarModal("panelClaveCliente");

			} else if (aVista != null && aVista.equals("MODAL_ELIMINAR_CLIENTE")) {
				this.cerrarModal("panelEliminacionCliente");

			} else {
				this.vistaActual = null;

			}

		} catch (Exception e) {

			IConstantes.log.error(e, e);

		}

	}

	/**
	 * Cancela la edici�n de un consultor
	 * 
	 * @param aVista
	 */
	public void cancelarConsultorTransaccion(String aVista) {
		try {

			this.consultorTransaccion = new Consultor();
			this.consultores = null;
			this.getConsultores();

			if (aVista != null && aVista.equals("MODAL_EDITAR_CONSULTOR")) {
				this.cerrarModal("panelEdicionConsultor");

			} else if (aVista != null && aVista.equals("MODAL_CLAVE_CONSULTOR")) {
				this.cerrarModal("panelClaveConsultor");

			} else if (aVista != null && aVista.equals("MODAL_ELIMINAR_CONSULTOR")) {
				this.cerrarModal("panelEliminacionConsultor");

			} else if (aVista != null && aVista.equals("MODAL_VER_CONSULTOR")) {
				this.cerrarModal("panelVerConsultor");

			}

		} catch (Exception e) {

			IConstantes.log.error(e, e);

		}

	}

	/**
	 * Este m�todo borra el formulario de edici�n de un administrador en
	 * transacci�n
	 */
	public void cancelarAdministradorTransaccion(String aVista) {
		try {

			this.personalTransaccion = new Personal();
			this.administradores = null;
			this.getAdministradores();

			if (aVista != null && aVista.equals("MODAL_EDITAR_ADMINISTRADOR")) {
				this.cerrarModal("panelEdicionAdministrador");

			} else if (aVista != null && aVista.equals("MODAL_CLAVE_ADMINISTRADOR")) {
				this.cerrarModal("panelClaveAdministrador");

			} else if (aVista != null && aVista.equals("MODAL_ELIMINAR_ADMINISTRADOR")) {
				this.cerrarModal("panelEliminacionAdministrador");

			}

		} catch (Exception e) {

			IConstantes.log.error(e, e);

		}

	}

	/**
	 * Asigna un cliente para realizar una transacci�n
	 * 
	 * @param aCliente
	 * @param aVista
	 */
	public void asignarCliente(Cliente aCliente, String aVista) {

		try {

			this.clienteTransaccion = aCliente;
			this.vistaActual = null;
			this.minutosGastados = 0;

			if (aVista != null && aVista.equals("VISTA_PREGUNTAS")) {
				this.vistaActual = aVista;
				this.preguntas = null;
				this.getPreguntas();

				this.preguntaProyecto = null;
				this.getPreguntaProyecto();

			} else if (aVista != null && aVista.equals("MODAL_EDITAR_CLIENTE")) {

				String[] partes = this.clienteTransaccion.getNit().split("-");

				if (partes != null && partes.length == 1) {

					this.clienteTransaccion.settCopiaNit(this.clienteTransaccion.getNit().toUpperCase().trim());

				} else {
					this.clienteTransaccion.settCopiaNit(partes[0].toUpperCase().trim());

				}

				this.abrirModal("panelEdicionCliente");
			} else if (aVista != null && aVista.equals("MODAL_CLAVE_CLIENTE")) {
				this.abrirModal("panelClaveCliente");
			} else {
				this.abrirModal("panelEliminacionCliente");
			}

		} catch (

		Exception e) {
			IConstantes.log.error(e, e);
		}

	}

	/**
	 * Asigna un consultor para realizar alguna operaci�n sobre el mismo
	 * 
	 * @param aConsultor
	 * @param aVista
	 */
	public void asignarConsultor(Consultor aConsultor, String aVista) {

		try {

			this.consultorTransaccion = aConsultor;

			if (aVista != null && aVista.equals("MODAL_EDITAR_CONSULTOR")) {
				if (this.consultorTransaccion.getEstadoVigencia() != null) {
					this.consultorTransaccion.settTipoEstado(IConstantes.TIPO_VIGENCIA_MANUAL);
				} else {
					this.consultorTransaccion.settTipoEstado(IConstantes.TIPO_VIGENCIA_FECHAS);
				}
				this.abrirModal("panelEdicionConsultor");

			} else if (aVista != null && aVista.equals("MODAL_CLAVE_CONSULTOR")) {
				this.abrirModal("panelClaveConsultor");

			} else if (aVista != null && aVista.equals("MODAL_VER_CONSULTOR")) {

				if (this.consultorTransaccion.getEstadoVigencia() != null) {
					this.consultorTransaccion.settTipoEstado(IConstantes.TIPO_VIGENCIA_MANUAL);
				} else {
					this.consultorTransaccion.settTipoEstado(IConstantes.TIPO_VIGENCIA_FECHAS);
				}

				if (this.consultorTransaccion.getArchivo() != null) {
					this.guardarImagenEnDisco(this.consultorTransaccion.getId(), this.consultorTransaccion.gettFotoDecodificada(), "fotosConsultores");
					this.consultorTransaccion.settFotoDecodificada("foto" + this.consultorTransaccion.getId() + ".png");
				}
				this.abrirModal("panelVerConsultor");

			} else {

				if (this.consultorTransaccion.getEstadoVigencia() != null) {
					this.consultorTransaccion.settTipoEstado(IConstantes.TIPO_VIGENCIA_MANUAL);
				} else {
					this.consultorTransaccion.settTipoEstado(IConstantes.TIPO_VIGENCIA_FECHAS);
				}

				this.abrirModal("panelEliminacionConsultor");

			}

		} catch (

		Exception e) {
			IConstantes.log.error(e, e);
		}

	}

	/**
	 * Asigna un administrador para realizar una acci�n
	 * 
	 * @param aAgrupador
	 * @param aVista
	 */
	public void asignarAdministrador(Personal aPersonal, String aVista) {

		try {

			this.personalTransaccion = aPersonal;

			if (aVista != null && aVista.equals("MODAL_EDITAR_ADMINISTRADOR")) {
				this.abrirModal("panelEdicionAdministrador");

			} else if (aVista != null && aVista.equals("MODAL_CLAVE_ADMINISTRADOR")) {
				this.abrirModal("panelClaveAdministrador");

			} else {

				this.abrirModal("panelEliminacionAdministrador");

			}

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}

	}

	/**
	 * Obtiene un listado de administradores
	 * 
	 * @return administradores
	 */
	public List<Personal> getAdministradores() {
		try {
			if (this.administradores == null) {

				this.administradores = IConsultasDAO.getAdministradores(null);

			}
		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
		return administradores;
	}

	/**
	 * Establece los administradores del software
	 * 
	 * @param administradores
	 */
	public void setAdministradores(List<Personal> administradores) {
		this.administradores = administradores;
	}

	/**
	 * Obtiene los consultores de acuerdo a los criterios especificados
	 * 
	 * @return consultores
	 */
	public List<Consultor> getConsultores() {
		try {
			if (this.consultores == null) {

				this.consultores = IConsultasDAO.getConsultores(null);

			}
		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
		return consultores;
	}

	/**
	 * Establece los consultores del software
	 * 
	 * @param consultores
	 */
	public void setConsultores(List<Consultor> consultores) {
		this.consultores = consultores;
	}

	/**
	 * Obtiene los clientes
	 * 
	 * @return clientes
	 */
	public List<Cliente> getClientes() {
		return clientes;
	}

	/**
	 * Establece los clientes
	 * 
	 * @param clientes
	 */
	public void setClientes(List<Cliente> clientes) {
		this.clientes = clientes;
	}

	// get/sets

	public Cliente getCliente() {
		try {
			if (this.cliente == null) {
				this.cliente = new Cliente();

			}
		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Cliente getClienteTransaccion() {
		try {
			if (this.clienteTransaccion == null) {
				this.clienteTransaccion = new Cliente();

			}
		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
		return clienteTransaccion;
	}

	public void setClienteTransaccion(Cliente clienteTransaccion) {
		this.clienteTransaccion = clienteTransaccion;
	}

	public Personal getPersonal() {
		try {
			if (this.personal == null) {
				this.personal = new Personal();

			}
		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
		return personal;
	}

	public void setPersonal(Personal personal) {
		this.personal = personal;
	}

	public Personal getPersonalTransaccion() {

		return personalTransaccion;
	}

	public void setPersonalTransaccion(Personal personalTransaccion) {
		this.personalTransaccion = personalTransaccion;
	}

	public Consultor getConsultor() {
		try {
			if (this.consultor == null) {
				this.consultor = new Consultor();

			}
		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
		return consultor;
	}

	public void setConsultor(Consultor consultor) {
		this.consultor = consultor;
	}

	public Consultor getConsultorTransaccion() {
		return consultorTransaccion;
	}

	public void setConsultorTransaccion(Consultor consultorTransaccion) {
		this.consultorTransaccion = consultorTransaccion;
	}

	public Cliente getClienteConsulta() {
		try {
			if (this.clienteConsulta == null) {
				this.clienteConsulta = new Cliente();
			}
		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
		return clienteConsulta;
	}

	public void setClienteConsulta(Cliente clienteConsulta) {
		this.clienteConsulta = clienteConsulta;
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
	 * Obtiene los items de los arbolitos disponibles
	 * 
	 * @return itemsArbolitosDisponibles
	 */
	public List<SelectItem> getItemsArbolitosDisponibles() {
		try {

			this.itemsArbolitosDisponibles = new ArrayList<SelectItem>();
			this.itemsArbolitosDisponibles.add(new SelectItem("", this.getMensaje("comboVacio")));

			Plan arbol = new Plan();
			arbol.setEstadoVigencia(IConstantes.ACTIVO);
			List<Plan> arbolitosActivos = IConsultasDAO.getArbolitos(arbol);

			if (arbolitosActivos != null && arbolitosActivos.size() > 0) {
				arbolitosActivos.forEach(p -> this.itemsArbolitosDisponibles.add(new SelectItem(p.getId(), p.getMinutos() + " minutos " + this.getHoras(p.getMinutos()) + ", PRECIO PLAN: " + this.getMoneda(p.getPrecioVentaPesosConIva()) + ", NOMBRE PLAN:" + p.getNombre())));
			}

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
		return itemsArbolitosDisponibles;
	}

	/**
	 * Establece los items de los arbolitos disponibles
	 * 
	 * @param itemsArbolitosDisponibles
	 */
	public void setItemsArbolitosDisponibles(List<SelectItem> itemsArbolitosDisponibles) {
		this.itemsArbolitosDisponibles = itemsArbolitosDisponibles;
	}

	public Map<String, Object> getTotales() {
		return totales;
	}

	public void setTotales(Map<String, Object> totales) {
		this.totales = totales;
	}
	
	

	

}
