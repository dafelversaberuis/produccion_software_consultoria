package consultoria.generales;

import java.util.List;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public interface IEmail {

	/**
	 * Envía un correo electrónico de forma simple con texto html
	 * 
	 * @param Mensaje
	 * @param Asunto
	 * @param Correo
	 * @throws Exception
	 */
	public static void enviarCorreo(String Mensaje, String Asunto, String Correo) throws Exception {
		try {
			Properties props = new Properties();

			props.put("mail.transport.protocol", "smtps");
			props.put("mail.smtps.host", IConstantes.SMTP_HOST_NAME);
			props.put("mail.smtps.auth", "true");
			props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			props.put("mail.smtps.quitwait", "false");

			Session mailSession = Session.getDefaultInstance(props);

			// si se quiere que el programa muestre paso a paso el proceso de como se
			// envia, se descomentarea la sig linea
			mailSession.setDebug(false);
			Transport transport = mailSession.getTransport();

			MimeMessage message = new MimeMessage(mailSession);
			message.setSubject(Asunto, "UTF-8");

			// con texto html simple
			message.setContent(Mensaje, "text/html; charset=UTF-8");

			// SI QUEREMOS USAR A UN CORREO PROPIO PERO LO ENVIA ES EL SERVIDOR DE
			// GMAIL
			message.setFrom(new InternetAddress(IConstantes.SMTP_AUTH_USER, IConstantes.NOMBRE_SOFTWARE));

			// PARA MANDAR CORREO DE UNO EN UNO
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(Correo));

			transport.connect(IConstantes.SMTP_HOST_NAME, IConstantes.SMTP_HOST_PORT, IConstantes.SMTP_AUTH_USER, IConstantes.SMTP_AUTH_PWD);

			transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));

			transport.close();
		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
	}

	public static void enviarCorreoMasivo(String Mensaje, String Asunto, List<String> aCorreos) throws Exception {
		try {
			Properties props = new Properties();

			props.put("mail.transport.protocol", "smtps");
			props.put("mail.smtps.host", IConstantes.SMTP_HOST_NAME);
			props.put("mail.smtps.auth", "true");
			props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			props.put("mail.smtps.quitwait", "false");

			Session mailSession = Session.getDefaultInstance(props);

			// si se quiere que el programa muestre paso a paso el proceso de como se
			// envia, se descomentarea la sig linea
			mailSession.setDebug(false);
			Transport transport = mailSession.getTransport();

			MimeMessage message = new MimeMessage(mailSession);
			message.setSubject(Asunto, "UTF-8");

			// con texto html simple
			message.setContent(Mensaje, "text/html; charset=UTF-8");

			// SI QUEREMOS USAR A UN CORREO PROPIO PERO LO ENVIA ES EL SERVIDOR DE
			// GMAIL
			message.setFrom(new InternetAddress(IConstantes.SMTP_AUTH_USER, IConstantes.NOMBRE_SOFTWARE));

			// PARA MANDAR CORREO MASIVO
			Address[] direcciones = new Address[aCorreos.size()];
			int i = -1;
			for (String p : aCorreos) {
				i++;
				direcciones[i] = new InternetAddress(p);

			}

			message.addRecipients(Message.RecipientType.BCC, direcciones);

			transport.connect(IConstantes.SMTP_HOST_NAME, IConstantes.SMTP_HOST_PORT, IConstantes.SMTP_AUTH_USER, IConstantes.SMTP_AUTH_PWD);

			transport.sendMessage(message, message.getRecipients(Message.RecipientType.BCC));

			transport.close();
		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
	}
}