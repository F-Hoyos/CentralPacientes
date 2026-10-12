
package persistencia;

import modelo.CentralPacientes;
import modelo.Paciente;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ArchivoPacientes {

    private final Path ruta;

    public ArchivoPacientes() {
        this.ruta = localizarRutaProyecto()
                .resolve("data")
                .resolve("pacientes.txt");
    }

    private Path localizarRutaProyecto() {
        Path directorio = Paths.get("").toAbsolutePath().normalize();

        while (directorio != null) {
            if (Files.exists(directorio.resolve("pom.xml"))) {
                return directorio;
            }

            directorio = directorio.getParent();
        }

        Path usuario = Paths.get(System.getProperty("user.home"));
        Path documentos = usuario.resolve("OneDrive")
                .resolve("Documentos")
                .resolve("NetBeansProjects")
                .resolve("CentralPacientes");

        if (Files.exists(documentos.resolve("pom.xml"))) {
            return documentos;
        }

        throw new IllegalStateException(
            "No se encontró la carpeta raíz del proyecto."
        );
    }

    public void guardar(CentralPacientes central) throws IOException {
        Files.createDirectories(ruta.getParent());

        try (BufferedWriter escritor = Files.newBufferedWriter(
                ruta, StandardCharsets.UTF_8)) {

            Paciente actual = central.getPrimero();

            while (actual != null) {
                escritor.write(
                    actual.getCodigo() + "|"
                    + limpiar(actual.getNombre()) + "|"
                    + limpiar(actual.getClinica()) + "|"
                    + limpiar(actual.getInformacionMedica()) + "|"
                    + actual.getSexo()
                );

                escritor.newLine();
                actual = actual.getSiguiente();
            }
        }

        System.out.println("Pacientes guardados en: " + ruta);
    }

    public CentralPacientes cargar() throws IOException {
        CentralPacientes central = new CentralPacientes();

        Files.createDirectories(ruta.getParent());

        if (!Files.exists(ruta)) {
            Files.createFile(ruta);
            System.out.println("Archivo creado en: " + ruta);
            return central;
        }

        for (String linea : Files.readAllLines(
                ruta, StandardCharsets.UTF_8)) {

            if (linea.trim().isEmpty()) {
                continue;
            }

            String[] datos = linea.split("\\|", -1);

            if (datos.length != 5) {
                continue;
            }

            try {
                int codigo = Integer.parseInt(datos[0]);
                String nombre = datos[1];
                String clinica = datos[2];
                String informacionMedica = datos[3];
                int sexo = Integer.parseInt(datos[4]);

                if (central.localizar(codigo) == null) {
                    central.agregarPacienteAlFinal(
                        new Paciente(
                            codigo, nombre, clinica,
                            informacionMedica, sexo
                        )
                    );
                }

            } catch (NumberFormatException e) {
                System.out.println("Registro inválido: " + linea);
            }
        }

        System.out.println("Pacientes recuperados: "
                + central.getNumPacientes());
        System.out.println("Archivo leído desde: " + ruta);

        return central;
    }

    private String limpiar(String texto) {
        if (texto == null) {
            return "";
        }

        return texto.replace("|", "/")
                    .replace("\r", " ")
                    .replace("\n", " ");
    }
}
