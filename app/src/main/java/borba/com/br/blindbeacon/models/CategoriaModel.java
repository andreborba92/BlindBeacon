package borba.com.br.blindbeacon.models;

/**
 * Created by andre on 22/03/2017.
 */

public class CategoriaModel {
    private int Id;
    private String Nome; //sala de aula, banheiro, xerox, auditório, café, secretaria

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getNome() {
        return Nome;
    }

    public void setNome(String nome) {
        Nome = nome;
    }

    public CategoriaModel(){
    }
}
