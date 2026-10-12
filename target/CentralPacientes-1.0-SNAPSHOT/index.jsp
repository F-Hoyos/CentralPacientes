
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Central de Pacientes</title>
</head>
<body>
    <%
        response.sendRedirect(
            request.getContextPath() + "/pacientes"
        );
    %>
</body>
</html>
