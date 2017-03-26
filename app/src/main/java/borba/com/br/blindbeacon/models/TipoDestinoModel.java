package borba.com.br.blindbeacon.models;

/**
 * Created by andre on 22/03/2017.
 */

public class TipoDestinoModel {
    private int Id;
    private String Nome; //destino, conexão, obstáculo

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

    public TipoDestinoModel(){
    }

    public TipoDestinoModel(int id, String nome){
        this.Id = id;
        this.Nome = nome;
    }
}
