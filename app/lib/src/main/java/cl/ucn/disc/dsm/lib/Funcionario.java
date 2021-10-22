package cl.ucn.disc.dsm.lib;
import lombok.Builder;
import lombok.Getter;


@Builder
public class Funcionario {
    @Getter
    private int id;
    @Getter
    private String nombre;
    @Getter
    private String  cargo;
    @Getter
    private String unidad;
    @Getter
    private String mail;
    @Getter
    private String telefono;
    @Getter
    private String oficina;
    @Getter
    private String direccion;

}
