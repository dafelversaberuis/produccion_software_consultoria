package consultoria.modulos.etapas;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import consultoria.beans.DocumentoActividad;
import consultoria.modulos.IConsultasDAO;

@WebServlet("/ver_archivo_adjunto.jsp")
public class VerArchivoAdjunto extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3132177421172054188L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		try {

			String directorio_ruta = request.getRealPath("archivosTemporales") + "/";

			String id = request.getParameter("id");

			if (id != null) {

				DocumentoActividad a = new DocumentoActividad();
				a.setId(Integer.parseInt(id));
				DocumentoActividad temp = IConsultasDAO.getAdjuntoDocumento(a);

				String nombre = "isoluciones" + temp.getId().intValue() * temp.getId().intValue() + ".pdf";

				// lo cea en carpeta
				new AdministrarEtapa().guardarArchivoDisco(directorio_ruta + nombre, (byte[]) temp.getArchivo());

				byte[] bytes = (byte[]) temp.getArchivo();

				// lo leee

				/* Indicamos que la respuesta va a ser en formato PDF */

				response.setContentType("application/pdf"); // siemrpe
																																	// guardamos
																																	// jpg
				response.setContentLength(bytes.length);
				ServletOutputStream ouputStream = response.getOutputStream();
				ouputStream.write(bytes, 0, bytes.length);

				ouputStream.flush();
				ouputStream.close();

			}

		} catch (Exception e) {
			// out.println("NO SE PUEDE MOSTRAR EL CERTIFICADO");
		}

	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doPost(req, resp);
	}
}
