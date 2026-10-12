
package servlet;

import modelo.CentralPacientes;
import modelo.Paciente;
import persistencia.ArchivoPacientes;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/pacientes")
public class PacienteServlet extends HttpServlet {

    private CentralPacientes central;
    private ArchivoPacientes archivo;

    @Override
    public void init() throws ServletException {
        archivo = new ArchivoPacientes();

        try {
            central = archivo.cargar();
        } catch (IOException e) {
            throw new ServletException(
                "No se pudieron cargar los pacientes.", e
            );
        }
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("primero", central.getPrimero());
        request.setAttribute("numPacientes", central.getNumPacientes());

        request.getRequestDispatcher("/pacientes.jsp")
               .forward(request, response);
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String accion = request.getParameter("accion");

        try {
            if ("buscar".equals(accion)) {
                buscarPaciente(request, response);
                return;
            }

            if ("eliminar".equals(accion)) {
                String codigoTexto = request.getParameter("codigo");
                int codigo = Integer.parseInt(codigoTexto);

                if (central.eliminarPaciente(codigo)) {
                    archivo.guardar(central);
                    redirigir(response, request);
                } else {
                    mostrarError(request, response,
                        "No se encontró el paciente que deseas eliminar.");
                }
                return;
            }

            String codigoTexto = request.getParameter("codigo");
            String nombre = request.getParameter("nombre");
            String clinica = request.getParameter("clinica");
            String informacionMedica =
                request.getParameter("informacionMedica");
            String sexoTexto = request.getParameter("sexo");

            if (codigoTexto == null || nombre == null
                    || clinica == null || informacionMedica == null
                    || sexoTexto == null
                    || nombre.trim().isEmpty()
                    || clinica.trim().isEmpty()
                    || informacionMedica.trim().isEmpty()) {

                mostrarError(request, response,
                    "Todos los campos son obligatorios.");
                return;
            }

            int codigo = Integer.parseInt(codigoTexto);
            int sexo = Integer.parseInt(sexoTexto);

            if (codigo <= 0) {
                mostrarError(request, response,
                    "El código debe ser mayor que cero.");
                return;
            }

            if (sexo != 1 && sexo != 2) {
                mostrarError(request, response,
                    "Selecciona un sexo válido.");
                return;
            }

            if (central.localizar(codigo) != null) {
                mostrarError(request, response,
                    "Ya existe un paciente con ese código.");
                return;
            }

            Paciente nuevo = new Paciente(
                codigo, nombre.trim(), clinica.trim(),
                informacionMedica.trim(), sexo
            );

            if ("inicio".equals(accion)) {
                central.agregarPacienteAlComienzo(nuevo);

            } else if ("despues".equals(accion)) {
                String referenciaTexto =
                    request.getParameter("codigoReferencia");

                if (referenciaTexto == null
                        || referenciaTexto.trim().isEmpty()) {
                    mostrarError(request, response,
                        "Ingresa el código de referencia.");
                    return;
                }

                int codigoReferencia =
                    Integer.parseInt(referenciaTexto);

                if (central.localizar(codigoReferencia) == null) {
                    mostrarError(request, response,
                        "No existe el paciente de referencia.");
                    return;
                }

                central.agregarPacienteDespuesDe(
                    codigoReferencia, nuevo
                );

            } else {
                central.agregarPacienteAlFinal(nuevo);
            }

            archivo.guardar(central);
            redirigir(response, request);

        } catch (NumberFormatException e) {
            mostrarError(request, response,
                "Los códigos y el sexo deben ser números válidos.");
        }
    }

    private void buscarPaciente(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String texto = request.getParameter("codigoBusqueda");

            if (texto == null || texto.trim().isEmpty()) {
                mostrarError(request, response,
                    "Ingresa el código que deseas buscar.");
                return;
            }

            int codigo = Integer.parseInt(texto);
            Paciente encontrado = central.localizar(codigo);

            request.setAttribute("pacienteBuscado", encontrado);
            request.setAttribute("codigoBuscado", codigo);

            if (encontrado == null) {
                request.setAttribute("errorBusqueda",
                    "No se encontró un paciente con ese código.");
            }

            doGet(request, response);

        } catch (NumberFormatException e) {
            mostrarError(request, response,
                "El código de búsqueda debe ser numérico.");
        }
    }

    private void mostrarError(
            HttpServletRequest request,
            HttpServletResponse response,
            String mensaje)
            throws ServletException, IOException {

        request.setAttribute("error", mensaje);
        doGet(request, response);
    }

    private void redirigir(
            HttpServletResponse response,
            HttpServletRequest request)
            throws IOException {

        response.sendRedirect(
            request.getContextPath() + "/pacientes"
        );
    }
}
