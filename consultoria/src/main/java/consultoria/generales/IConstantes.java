package consultoria.generales;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

public interface IConstantes {

	// manejo de logs
	public static final Logger	log															= Logger.getLogger("consultoria");

	// validaciones anotaciones beans

	public static String				NOMBRE_SOFTWARE									= "Isoluciones - Consulting Software";
	public static String				VALIDACION_MAXIMA_LONGITUD			= "Máximo {max} caracteres";
	public static String				VALIDACION_FECHA_FUTURA					= "Debe ser mayor a hoy";
	public static String				VALIDACION_MINIMA_LONGITUD			= "Mínimo {min} caracteres";
	public static String				VALIDACION_MAXIMO_ENTERO				= "Máximo {value}";
	public static String				VALIDACION_MINIMO_ENTERO				= "Mínimo {value}";
	public static String				VALIDACION_VACIO								= "No se permite sólo vacío";

	public static String				VALIDACION_ACTIVO_INACTIVO			= "Se debe especificar si está activo o inactivo";
	public static String				VALIDACION_SI_NO								= "Se debe especificar SI ó NO";
	public static String				VALIDACION_MAXIMO_DECIMAL				= "Máximo un número de {integer} digitos enteros y {fraction} decimales";
	public static String				VALIDACION_EMAIL_INCORRECTO			= "El formato del correo electrónico es incorrecto";
	public static String				VALIDACION_MONEDA								= "Sólo pesos colombianos ó dólares";
	public static Integer				NUMERO_DIGITOS_CLAVE_ALEATORIA	= 6;

	public static Integer				FILA_INICIO_PERSONA_CSV					= 7;
	public static Integer				DECIMALES_REDONDEAR							= 2;
	public static String				AFIRMACION											= "S";
	public static String				NEGACION												= "N";
	public static String				ACTIVO													= "A";
	public static String				LABEL_ACTIVO										= "ACTIVO";
	public static String				LABEL_INACTIVO									= "INACTIVO";
	public static String				INACTIVO												= "I";
	public static String				PESO_COLOMBIANO									= "COP";
	public static String				DOLAR														= "US";
	public static String				IDIOMA_ESPANOL									= "ES";
	public static String				TRANSACCION_APROBADA						= "A";
	public static String				TRANSACCION_PENDIENTE						= "P";
	public static String				NO_APLICA												= "N";
	public static String				TIPO_VIGENCIA_MANUAL						= "M";
	public static String				TIPO_VIGENCIA_FECHAS						= "F";
	public static String				FORMA_COMPLETA									= "C";
	public static String				FORMA_INCOMPLETA								= "I";
	public static String				ABREVIATURA_CONSULTOR						= "CO";
	public static String				ABREVIATURA_CLIENTE							= "C";
	public static String				ABREVIATURA_ABIERTA							= "A";
	public static String				ABREVIATURA_CERRADA							= "C";

	public static String				ROL_CONSULTOR										= "CO";
	public static String				ROL_CLIENTE											= "CL";
	public static String				ROL_ADMINISTRADOR								= "A";

	public static Integer				MAXIMOS_REMITENTES_CORREO				= 100;
	public static Integer				ID_ESTADO_NO_APLICA							= 6;
	public static Integer				ID_ESTADO_CUMPLE								= 10;
	public static Integer				NUMERO_SEMANAS									= 5;

	public static Integer				ID_PARAMETRO_AUDITORIA					= 1;
	public static Integer				NUMERO_PERIODOS_INDICADORES			= 12;

	public static BigDecimal		META_INFERIOR_INICIAL						= new BigDecimal(20);
	public static BigDecimal		META_INTERMEDIA_INICIAL					= new BigDecimal(70);
	public static BigDecimal		META_SUPERIOR_INICIAL						= new BigDecimal(95);

	public static final String	SMTP_HOST_NAME									= "smtp.gmail.com";
	public static final int			SMTP_HOST_PORT									= 465;
	public static final String	SMTP_AUTH_USER									= "quimerapps@gmail.com";
	public static final String	SMTP_AUTH_PWD										= "mariacamila12";

	public static String				ID_USUARIO_SESION								= "dafelver";

	public static final String	PAQUETE_MODULO_REPORTES					= "/reportes/";

	public static final String	HOST														= "http://www.consultoriaisoluciones.com";
	public static final String	PAQUETE_IMAGENES								= "/imagenes/";
	public static final String	LOGO1														= "iconsulting_software.png";
	public static final String	LOGO2														= "calidad.png";

	public static String				PAGINA_NO_LOGUEO								= "/index.xhtml?faces-redirect=true";
	public static String				PAGINA_HOME											= "/home.xhtml?faces-redirect=true";

	public static final String	NOMBRE_REPORTE_DIAGNOSTICO			= "INFORME_DIAGNOSTICO";
	public static final String	REPORTE_DIAGNOSTICO							= "imprimirDiagnostico.jasper";

}
