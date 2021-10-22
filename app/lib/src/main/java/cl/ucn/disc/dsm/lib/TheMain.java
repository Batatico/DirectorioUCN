package cl.ucn.disc.dsm.lib;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.jsoup.nodes.Document;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TheMain {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static void main(String[] args) throws IOException, InterruptedException {

        //Definir Tipo
        Type type = new TypeToken<List<Funcionario>>(){
        }.getType();

        //Cargar archivo
        String data = FileUtils.readFileToString(new File("funcionarios.json"), StandardCharsets.UTF_8);


        // Lista De Funcionarios
        List<Funcionario> funcionarios = GSON.fromJson(data, type);


        // Ultimo Ingresado
        int start = funcionarios.get(funcionarios.size() - 1).getId();
        int end = 30000;

        Random r = new Random();
        log.debug("Starting the Scrapping from {} to {} ....", start, end);
        for (int id = start; id <= end; id++ ){
            //Wait for..
            Thread.sleep(50 +r.nextInt(350));

            log.debug("Retriving Funcionario id: {}.", id);
            //Conexion Al Sitio
            Document doc = Jsoup.connect("http://admision01.ucn.cl/directoriotelefonicoemail/fichaGenerica/?cod=" + id).get();

            // Scrapping
            String nombre = doc.getElementById("lblCargo").text();
            String cargo = doc.getElementById("lblCargo").text();
            String unidad = doc.getElementById("lblUnidad").text();
            String email = doc.getElementById("lblEmail").text();
            String telefono = doc.getElementById("lblTelefono").text();
            String oficina = doc.getElementById("lblOficina").text();
            String direccion = doc.getElementById("lblDireccion").text();

            // Si No Hay Dato, No Se Carga Nada
            if (nombre.length() <= 1){
                log.warn("No data found for id: {}.", id);
                continue;
            }

            log.info("Funcionario id: {} founded: {}.", id, nombre);


            //Constructor de Funcionario
            Funcionario f = Funcionario.builder()
                    .id(id)
                    .nombre(nombre)
                    .cargo(cargo)
                    .unidad(unidad)
                    .mail(email)
                    .telefono(telefono)
                    .oficina(oficina)
                    .direccion(direccion)
                    .build();



            // Agregar Funcionario A La Lista
            funcionarios.add(f);

            //save by 25
            if (funcionarios.size() % 3 == 0){

                log.debug("Writing {} Funcionarios to file .... ", funcionarios.size());
                //Write the list of Funcionario in JSON format
                FileUtils.writeStringToFile(new File("funcionarios.json"),
                        GSON.toJson(funcionarios),
                        StandardCharsets.UTF_8);
            }

        }

        log.debug("Done.");

    }


}






}