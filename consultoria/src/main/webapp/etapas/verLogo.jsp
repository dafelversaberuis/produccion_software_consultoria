<%@ page contentType="text/html; charset=iso-8859-1" language="java"
	import="java.sql.*" import="java.util.*,java.text.SimpleDateFormat"
	session="false"%>
	
	<jsp:useBean id="bAdministrarEtapa"
	class="etapas.AdministrarEtapa" scope="page" />

<%
	response.setHeader("Cache-Control", "no-store");
	response.setHeader("Pragma", "no-cache");
	response.setDateHeader("Expires", 0);
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html>
<head>
<title>iConsulting</title>
<meta name="Description"
	content="Innovación en Gestión Empresarial">


<? header("Cache-Control: no-cache, must-revalidate");?>
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="pragma" content="no-cache" />
<meta name="resource-type" content="document" />

<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">

<link href="estilos/estilos.css" rel="stylesheet" type="text/css">
<style type="text/css">
<!--
body {
	background-image: url();
--background-color: #AFDBF6;
}
-->
</style>
<style type="text/css">
<!--
.Estilo5 {
	font-size: 12px;
	font-weight: bold;
	font-family: Arial, Helvetica, sans-serif;
}

.Estilo6 {
	font-size: 12px;
	font-weight: bold;
}
-->
</style>


</head>
<body>
<%
//String tu = request.getParameter("tu");
//String us = request.getParameter("us");
String id = request.getParameter("id");




//lo crea x sia ca no está
String directorio_ruta = application.getRealPath("archivosTemporales")+"/";
DocumentoActividad a = new DocumentoActividad();
a.setId(Integer.parseInt(id));
DocumentoActividad temp = IConsultasDAO.getAdjuntoDocumento(a);
String nombre = "isoluciones" + temp.getId().intValue() * temp.getId().intValue() + ".pdf";   
	



%>

<div id="contiene-imagenes" style="width:500px; height: 500px">
<%if(sw==0){ %>
<img src="imagenes/logosFinanciadores/logo_financiador_<%=id %>-<%=n%>.jpg" alt="CLIENTE" title="CLIENTE"/>

<%}else{ %>
<img src="/i-web/ver_foto_adjunta.jsp?id=<%=financiador[0] %>&n=<%=n%>" alt="CLIENTE" title="CLIENTE"/>
<% }%>
</div>

</body>
</html>
