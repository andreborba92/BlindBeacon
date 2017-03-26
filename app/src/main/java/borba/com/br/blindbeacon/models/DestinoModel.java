package borba.com.br.blindbeacon.models;

/**
 * Created by andre on 22/03/2017.
 */

public class DestinoModel {
    private int IdDestino, IdPredio, IdTipoDestino, IdCategoria;
    private String Nome, Descricao;
    private int DistanciaAproximada;

    public int getIdDestino() {
        return IdDestino;
    }

    public void setIdDestino(int idDestino) {
        IdDestino = idDestino;
    }

    public int getIdPredio() {
        return IdPredio;
    }

    public void setIdPredio(int idPredio) {
        IdPredio = idPredio;
    }

    public int getIdTipoDestino() {
        return IdTipoDestino;
    }

    public void setIdTipoDestino(int idTipoDestino) {
        IdTipoDestino = idTipoDestino;
    }

    public int getIdCategoria() {
        return IdCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        IdCategoria = idCategoria;
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

    public int getDistanciaAproximada() {
        return DistanciaAproximada;
    }

    public void setDistanciaAproximada(int distanciaAproximada) {
        DistanciaAproximada = distanciaAproximada;
    }

    public DestinoModel(){

    }

    public DestinoModel( int IdPredio, int IdTipoDestino, int IdCategoria, String Nome, String Descricao ){
        //this.IdDestino = IdDestino;
        this.IdPredio= IdPredio;
        this.IdTipoDestino= IdTipoDestino;
        this.IdCategoria= IdCategoria;
        this.Nome= Nome;
        this.Descricao= Descricao;
    }
}
