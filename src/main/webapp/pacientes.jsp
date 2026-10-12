
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="modelo.Paciente"%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Central de Pacientes</title>

    <style>
        * { box-sizing: border-box; }

        body {
            margin: 0;
            padding: 25px;
            font-family: Arial, sans-serif;
            background: #f1f5f9;
            color: #1e293b;
        }

        .contenedor {
            max-width: 1200px;
            margin: auto;
        }

        h1, h2 { color: #17365d; }

        .panel {
            background: white;
            padding: 22px;
            margin-bottom: 22px;
            border-radius: 10px;
            box-shadow: 0 3px 12px rgba(0,0,0,.07);
        }

        .formulario {
            display: grid;
            grid-template-columns: repeat(2, minmax(0, 1fr));
            gap: 14px;
        }

        .campo {
            display: flex;
            flex-direction: column;
            gap: 6px;
        }

        .completo { grid-column: 1 / -1; }

        input, select, textarea {
            width: 100%;
            padding: 10px;
            border: 1px solid #cbd5e1;
            border-radius: 5px;
            font-size: 15px;
        }

        button {
            padding: 10px 15px;
            border: none;
            border-radius: 5px;
            background: #1769aa;
            color: white;
            font-weight: bold;
            cursor: pointer;
        }

        button:hover { opacity: .85; }
        .rojo { background: #c62828; }
        .verde { background: #287d46; }
        .morado { background: #6b46a1; }

        .tabla-contenedor { overflow-x: auto; }

        table {
            width: 100%;
            min-width: 800px;
            border-collapse: collapse;
        }

        th, td {
            padding: 11px;
            border-bottom: 1px solid #e2e8f0;
            text-align: left;
            vertical-align: top;
        }

        th {
            background: #17365d;
            color: white;
        }

        .mensaje {
            padding: 12px;
            margin-bottom: 15px;
            border-radius: 5px;
            background: #dbeafe;
            color: #1e40af;
        }

        .error {
            background: #fee2e2;
            color: #991b1b;
        }

        .fila-busqueda {
            display: grid;
            grid-template-columns: 1fr auto;
            gap: 10px;
        }

        @media (max-width: 650px) {
            body { padding: 10px; }
            .formulario { grid-template-columns: 1fr; }
            .completo { grid-column: auto; }
        }
    </style>
</head>

<body>
<div class="contenedor">

    <h1>Central de Pacientes</h1>

    <% if (request.getAttribute("error") != null) { %>
        <div class="mensaje error">
            <%= request.getAttribute("error") %>
        </div>
    <% } %>

    <section class="panel">
        <h2>Registrar paciente al final</h2>

        <form action="<%= request.getContextPath() %>/pacientes"
              method="post" class="formulario">

            <input type="hidden" name="accion" value="final">

            <div class="campo">
                <label for="codigo">Código</label>
                <input type="number" id="codigo" name="codigo"
                       min="1" required>
            </div>

            <div class="campo">
                <label for="nombre">Nombre completo</label>
                <input type="text" id="nombre" name="nombre"
                       maxlength="150" required>
            </div>

            <div class="campo">
                <label for="clinica">Clínica</label>
                <input type="text" id="clinica" name="clinica"
                       maxlength="150" required>
            </div>

            <div class="campo">
                <label for="sexo">Sexo</label>
                <select id="sexo" name="sexo" required>
                    <option value="">Selecciona</option>
                    <option value="1">Masculino</option>
                    <option value="2">Femenino</option>
                </select>
            </div>

            <div class="campo completo">
                <label for="informacionMedica">Información médica</label>
                <textarea id="informacionMedica"
                          name="informacionMedica"
                          rows="3" required></textarea>
            </div>

            <div class="campo completo">
                <button type="submit">Registrar al final</button>
            </div>
        </form>
    </section>

    <section class="panel">
        <h2>Buscar paciente por código</h2>

        <form action="<%= request.getContextPath() %>/pacientes"
              method="post" class="fila-busqueda">

            <input type="hidden" name="accion" value="buscar">

            <input type="number" name="codigoBusqueda"
                   min="1" placeholder="Ingresa el código"
                   required>

            <button type="submit">Buscar</button>
        </form>

        <% if (request.getAttribute("pacienteBuscado") != null) {
            Paciente buscado =
                (Paciente) request.getAttribute("pacienteBuscado");
        %>
            <div class="mensaje">
                <strong>Paciente encontrado</strong><br>
                Código: <%= buscado.getCodigo() %><br>
                Nombre: <%= buscado.getNombre() %><br>
                Clínica: <%= buscado.getClinica() %><br>
                Información médica:
                <%= buscado.getInformacionMedica() %><br>
                Sexo: <%= buscado.getSexo() == 1
                    ? "Masculino" : "Femenino" %>
            </div>
        <% } %>

        <% if (request.getAttribute("errorBusqueda") != null) { %>
            <div class="mensaje error">
                <%= request.getAttribute("errorBusqueda") %>
            </div>
        <% } %>
    </section>

    <section class="panel">
        <h2>Insertar paciente al comienzo</h2>

        <form action="<%= request.getContextPath() %>/pacientes"
              method="post" class="formulario">

            <input type="hidden" name="accion" value="inicio">

            <div class="campo">
                <label>Código</label>
                <input type="number" name="codigo" min="1" required>
            </div>

            <div class="campo">
                <label>Nombre completo</label>
                <input type="text" name="nombre" required>
            </div>

            <div class="campo">
                <label>Clínica</label>
                <input type="text" name="clinica" required>
            </div>

            <div class="campo">
                <label>Sexo</label>
                <select name="sexo" required>
                    <option value="">Selecciona</option>
                    <option value="1">Masculino</option>
                    <option value="2">Femenino</option>
                </select>
            </div>

            <div class="campo completo">
                <label>Información médica</label>
                <textarea name="informacionMedica"
                          rows="2" required></textarea>
            </div>

            <div class="campo completo">
                <button type="submit" class="verde">
                    Insertar al comienzo
                </button>
            </div>
        </form>
    </section>

    <section class="panel">
        <h2>Insertar después de otro paciente</h2>

        <form action="<%= request.getContextPath() %>/pacientes"
              method="post" class="formulario">

            <input type="hidden" name="accion" value="despues">

            <div class="campo completo">
                <label>Código del paciente de referencia</label>
                <input type="number" name="codigoReferencia"
                       min="1" required>
            </div>

            <div class="campo">
                <label>Código del nuevo paciente</label>
                <input type="number" name="codigo" min="1" required>
            </div>

            <div class="campo">
                <label>Nombre completo</label>
                <input type="text" name="nombre" required>
            </div>

            <div class="campo">
                <label>Clínica</label>
                <input type="text" name="clinica" required>
            </div>

            <div class="campo">
                <label>Sexo</label>
                <select name="sexo" required>
                    <option value="">Selecciona</option>
                    <option value="1">Masculino</option>
                    <option value="2">Femenino</option>
                </select>
            </div>

            <div class="campo completo">
                <label>Información médica</label>
                <textarea name="informacionMedica"
                          rows="2" required></textarea>
            </div>

            <div class="campo completo">
                <button type="submit" class="morado">
                    Insertar después del código indicado
                </button>
            </div>
        </form>
    </section>

    <section class="panel">
        <h2>Listado de pacientes</h2>

        <p>
            Total de pacientes:
            <strong><%= request.getAttribute("numPacientes") %></strong>
        </p>

        <div class="tabla-contenedor">
            <table>
                <thead>
                    <tr>
                        <th>Código</th>
                        <th>Nombre</th>
                        <th>Clínica</th>
                        <th>Información médica</th>
                        <th>Sexo</th>
                        <th>Acciones</th>
                    </tr>
                </thead>

                <tbody>
                    <%
                        Paciente actual =
                            (Paciente) request.getAttribute("primero");

                        if (actual == null) {
                    %>
                        <tr>
                            <td colspan="6">
                                No hay pacientes registrados.
                            </td>
                        </tr>
                    <%
                        } else {
                            while (actual != null) {
                    %>
                        <tr>
                            <td><%= actual.getCodigo() %></td>
                            <td><%= actual.getNombre() %></td>
                            <td><%= actual.getClinica() %></td>
                            <td><%= actual.getInformacionMedica() %></td>
                            <td><%= actual.getSexo() == 1
                                ? "Masculino" : "Femenino" %></td>
                            <td>
                                <form
                                    action="<%= request.getContextPath() %>/pacientes"
                                    method="post"
                                    onsubmit="return confirm('¿Deseas eliminar este paciente?');">

                                    <input type="hidden" name="accion"
                                           value="eliminar">

                                    <input type="hidden" name="codigo"
                                           value="<%= actual.getCodigo() %>">

                                    <button type="submit" class="rojo">
                                        Eliminar
                                    </button>
                                </form>
                            </td>
                        </tr>
                    <%
                                actual = actual.getSiguiente();
                            }
                        }
                    %>
                </tbody>
            </table>
        </div>
    </section>

</div>
</body>
</html>
