package borba.com.br.blindbeacon.models;

/**
 * Created by andre on 20/03/2017.
 */

public class PredioModel {
    private int Id;
    private String Nome, Descricao;

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

    public String getDescricao() {
        return Descricao;
    }

    public void setDescricao(String descricao) {
        Descricao = descricao;
    }

    public PredioModel(){
    }
}
