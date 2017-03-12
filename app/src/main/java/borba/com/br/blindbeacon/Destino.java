package borba.com.br.blindbeacon;

/**
 * Created by André Borba on 21/02/2017.
 */
public class Destino {
    private String nome;
    //private String descricao;
    private String categoria;
    private int distancia;

    public Destino(String nome,  String categoria, int distancia){
        this.nome = nome;
        //this.descricao = descricao;
        this.categoria = categoria;
        this.distancia = distancia;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

//    public String getDescricao() {
//        return descricao;
//    }
//
//    public void setDescricao(String descricao) {
//        this.descricao = descricao;
//    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public int getDistancia() {
        return distancia;
    }

    public void setDistancia(int distancia) {
        this.distancia = distancia;
    }
}

