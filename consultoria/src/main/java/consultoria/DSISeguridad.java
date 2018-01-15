package consultoria;



import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/zonaSegura")
public class DSISeguridad extends HttpServlet {



	/**
	 * 
	 */
	private static final long serialVersionUID = 5925355637269612006L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		//doGet(req,res);
		String usuario = req.getParameter("usuario");
		String rol   = req.getParameter("rol");
		
		System.out.println(usuario);
		
		res.sendRedirect("home.jsf");
		
	}

}
