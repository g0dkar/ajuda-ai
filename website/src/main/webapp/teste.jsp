<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page session="false"%>
<html>
<head>
<title>Teste</title>
</head>
<body>
	<%
		String authServer = org.keycloak.common.util.UriUtils.getOrigin(request.getRequestURL().toString()) + "/auth";
		org.keycloak.admin.client.Keycloak keycloak = org.keycloak.admin.client.Keycloak.getInstance(authServer, "ajuda-ai", "rafael", "java", "security-admin-console");
		org.keycloak.admin.client.resource.ClientsResource clients = keycloak.realm("ajuda-ai").clients();
		out.println("<h1>Applications</h1>");
		out.println("<ul>");
		for (org.keycloak.representations.idm.ClientRepresentation client : clients.findAll()) {
			out.println("\t<li>");
			if (client.getBaseUrl() != null) {
				out.println("\t\t<a href=\"" + client.getBaseUrl() + "\">" + client.getClientId() + "</a>");
			} else {
				out.println("\t\t" + client.getClientId());
			}
			out.println("</li>");
		}
		out.println("</ul>");
	%>
</body>
</html>