package consultoria.modulos.personal;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import consultoria.beans.Cliente;
import consultoria.beans.Consultor;
import consultoria.beans.ParametroAuditoria;
import consultoria.beans.Personal;
import consultoria.generales.ConsultarFuncionesAPI;
import consultoria.generales.IConstantes;
import consultoria.modulos.IConsultasDAO;

@ManagedBean
@SessionScoped
public class AdministrarSesionCliente extends ConsultarFuncionesAPI implements Serializable {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -8153708434415072107L;
	private Personal					personalSesion;
	private Personal					personal;
	
	private String generico;

	public void init() {

	}

	// privados

	/**
	 * Obtiene validaci�n de rol no admitido
	 * 
	 * @return pagina
	 */
	public String getNoRolAdmitido(String aInterfaz) {
		String pagina = null;
		int sw = 2;

		if (this.personalSesion != null && this.personalSesion.getId() != null) {
			sw = 1;
			if (this.personalSesion.getTipoUsuario().equals(IConstantes.ROL_ADMINISTRADOR)) {
				sw = 0;
			} else if (this.personalSesion.getTipoUsuario().equals(IConstantes.ROL_CONSULTOR)) {

				if (aInterfaz != null && aInterfaz.equals("DIAGNOSTICO")) {
					sw = 0;
				} else if (aInterfaz != null && aInterfaz.equals("PARAMETROS_AUDITORIA")) {
					sw = 0;
				} else if (aInterfaz != null && aInterfaz.equals("INFORMACION_CLIENTE")) {
					sw = 0;
				} else if (aInterfaz != null && aInterfaz.equals("NORMAS")) {
					sw = 0;
				} else if (aInterfaz != null && aInterfaz.equals("PROYECTOS_CLIENTE")) {
					sw = 0;
				}else if (aInterfaz != null && aInterfaz.equals("PLANIFICACION")) {
					sw = 0;
				}else if (aInterfaz != null && aInterfaz.equals("INDICADORES")) {
					sw = 0;
				}else if (aInterfaz != null && aInterfaz.equals("DOCUMENTACION")) {
					sw = 0;
				}

			} else if (this.personalSesion.getTipoUsuario().equals(IConstantes.ROL_CLIENTE)) {

				if (aInterfaz != null && aInterfaz.equals("DIAGNOSTICO")) {
					sw = 0;
				
				}else if (aInterfaz != null && aInterfaz.equals("PLANIFICACION")) {
					sw = 0;
				}else if (aInterfaz != null && aInterfaz.equals("INDICADORES")) {
					sw = 0;
				}else if (aInterfaz != null && aInterfaz.equals("DOCUMENTACION")) {
					sw = 0;
				}

			}

		}

		if (sw == 1) {
			pagina = IConstantes.PAGINA_HOME;

			this.mostrarMensajeGlobal("noPoseePrivilegiosSobreInterfaz", "advertencia");
			// Guarda el mensaje antes de redireccionar y lo muestra
			FacesContext context = FacesContext.getCurrentInstance();
			context.getExternalContext().getFlash().setKeepMessages(true);

		} else if (sw == 2) {
			pagina = IConstantes.PAGINA_NO_LOGUEO;

			this.mostrarMensajeGlobal("noPoseePrivilegiosSobreInterfaz", "advertencia");
			// Guarda el mensaje antes de redireccionar y lo muestra
			FacesContext context = FacesContext.getCurrentInstance();
			context.getExternalContext().getFlash().setKeepMessages(true);

		}

		return pagina;
	}

	/**
	 * Determina si un personal esta en sesi�n y lo deja o no acceder
	 * 
	 * @return pagina
	 */
	public String getNologueoPersonal() {
		String pagina = null;
		if (!(this.personalSesion != null && this.personalSesion.getId() != null)) {

			pagina = IConstantes.PAGINA_NO_LOGUEO;

			this.mostrarMensajeGlobal("noPoseePrivilegios", "advertencia");
			// Guarda el mensaje antes de redireccionar y lo muestra
			FacesContext context = FacesContext.getCurrentInstance();
			context.getExternalContext().getFlash().setKeepMessages(true);

		}

		return pagina;
	}

	// publicos

	/**
	 * Cierra la sesi�n del administrador
	 */
	public String getCerrarSesion() {
		String pagina = IConstantes.PAGINA_NO_LOGUEO;
		this.personalSesion = null;

		// this.vistaLogueado = 0;

		this.mostrarMensajeGlobal("cierreSesionCorrecto", "exito");

		// Guarda el mensaje antes de redireccionar y lo muestra
		FacesContext context = FacesContext.getCurrentInstance();
		context.getExternalContext().getFlash().setKeepMessages(true);

		return pagina;

	}
	
	


	/**
	 * Permite el acceso del personal
	 */
	public String accederPersonal() {
		String pagina = null;

		List<Personal> administradores = null;
		List<Consultor> consultores = null;
		List<Cliente> clientes = null;
		try {

			if (this.personal != null && this.personal.getCorreoElectronico() != null && !this.personal.getCorreoElectronico().trim().equals("") && this.personal.getClave() != null && !this.personal.getClave().trim().equals("")) {

				if (this.personal.getTipoUsuario() != null && this.personal.getTipoUsuario().equals(IConstantes.ROL_ADMINISTRADOR)) {

					Personal admin = new Personal();
					admin.setClave(this.personal.getClave());
					admin.setCorreoElectronico(this.personal.getCorreoElectronico().trim());
					admin.setEstadoVigencia(IConstantes.ACTIVO);
					administradores = IConsultasDAO.getAdministradores(admin);

					if (administradores != null && administradores.size() > 0 && administradores.get(0) != null && administradores.get(0).getId() != null) {

						this.personalSesion = new Personal();
						this.personalSesion.setCorreoElectronico(administradores.get(0).getCorreoElectronico().trim());
						this.personalSesion.setNombreCompleto(administradores.get(0).getNombres().trim()+" "+administradores.get(0).getApellidos().trim());
						this.personalSesion.setTipoUsuario(this.personal.getTipoUsuario());
						this.personalSesion.setId(administradores.get(0).getId());

						this.mostrarMensajeGlobal("ingresoCorrecto", "exito");
						this.personal = null;

						pagina = IConstantes.PAGINA_HOME;

						// Guarda el mensaje antes de redireccionar y lo muestra
						FacesContext context = FacesContext.getCurrentInstance();
						context.getExternalContext().getFlash().setKeepMessages(true);

					} else {
						this.mostrarMensajeGlobal("noCoincideCredenciales", "advertencia");
					}

				} else if (this.personal.getTipoUsuario() != null && this.personal.getTipoUsuario().equals(IConstantes.ROL_CONSULTOR)) {

					Consultor tecnico = new Consultor();
					tecnico.setClave(this.personal.getClave());
					tecnico.setCorreoElectronico(this.personal.getCorreoElectronico().trim());
					tecnico.setEstadoVigencia(IConstantes.ACTIVO);

					consultores = IConsultasDAO.getConsultores(tecnico);

					if (consultores != null && consultores.size() > 0 && consultores.get(0) != null && consultores.get(0).getId() != null) {

						this.personalSesion = new Personal();
						this.personalSesion.setCorreoElectronico(consultores.get(0).getCorreoElectronico().trim());
						this.personalSesion.setNombreCompleto(consultores.get(0).getNombres().trim()+" "+consultores.get(0).getApellidos().trim());
						this.personalSesion.setTipoUsuario(this.personal.getTipoUsuario());
						this.personalSesion.setId(consultores.get(0).getId());

						this.mostrarMensajeGlobal("ingresoCorrecto", "exito");
						this.personal = null;

						pagina = IConstantes.PAGINA_HOME;

						// Guarda el mensaje antes de redireccionar y lo muestra
						FacesContext context = FacesContext.getCurrentInstance();
						context.getExternalContext().getFlash().setKeepMessages(true);

					} else {
						this.mostrarMensajeGlobal("noCoincideCredenciales", "advertencia");
					}

				} else if (this.personal.getTipoUsuario() != null && this.personal.getTipoUsuario().equals(IConstantes.ROL_CLIENTE)) {

					// falata garegar
					
					Cliente cliente = new Cliente();
					cliente.setClave(this.personal.getClave());
					cliente.setCorreoElectronico(this.personal.getCorreoElectronico().trim());
					
					clientes = IConsultasDAO.getClientes(cliente);
					
					
					
					if (clientes != null && clientes.size() > 0 && clientes.get(0) != null && clientes.get(0).getId() != null) {

						this.personalSesion = new Personal();
						this.personalSesion.setCorreoElectronico(clientes.get(0).getCorreoElectronico().trim());
						this.personalSesion.setNombreCompleto(clientes.get(0).getCliente().trim());
						this.personalSesion.setTipoUsuario(this.personal.getTipoUsuario());
						this.personalSesion.setId(clientes.get(0).getId());

						this.mostrarMensajeGlobal("ingresoCorrecto", "exito");
						this.personal = null;

						pagina = IConstantes.PAGINA_HOME;

						// Guarda el mensaje antes de redireccionar y lo muestra
						FacesContext context = FacesContext.getCurrentInstance();
						context.getExternalContext().getFlash().setKeepMessages(true);

					} else {
						this.mostrarMensajeGlobal("noCoincideCredenciales", "advertencia");
					}
					
					

				} else {

					this.mostrarMensajeGlobal("noCoincideCredenciales", "advertencia");

				}

			} else {

				this.mostrarMensajeGlobal("noCoincideCredenciales", "advertencia");

			}

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}

		return pagina;
	}

	/**
	 * Obtiene los datos de sesion de un personal
	 * 
	 * @return personalSesion
	 */
	public Personal getPersonalSesion() {
		try {
			if (this.personalSesion == null) {
				this.personalSesion = new Personal();
			}
		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
		return personalSesion;
	}

	/**
	 * Establece los datos de sesi�n de un personal
	 * 
	 * @param personalSesion
	 */
	public void setPersonalSesion(Personal personalSesion) {
		this.personalSesion = personalSesion;
	}

	/**
	 * Obtiene el personal que se logeua
	 * 
	 * @return personal
	 */
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

	/**
	 * Establece el personal que se loguea
	 * 
	 * @param personal
	 */
	public void setPersonal(Personal personal) {
		this.personal = personal;
	}

	public String getGenerico() {
		try {
			if (this.generico == null) {
				ParametroAuditoria pa = IConsultasDAO.getParametroAuditoria();
				if (pa != null && pa.getGenerico() != null && pa.getGenerico().equals("S")) {
					generico = "S";
				}
			}
		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
		return generico;
	}

	public void setGenerico(String generico) {
		
		this.generico = generico;
	}
	
	

}
