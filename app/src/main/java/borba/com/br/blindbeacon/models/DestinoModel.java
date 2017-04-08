package borba.com.br.blindbeacon.models;

import org.altbeacon.beacon.Beacon;

import java.util.ArrayList;

/**
 * Created by andre on 22/03/2017.
 */

public class DestinoModel{
    private int IdDestino, IdPredio, IdTipoDestino, IdCategoria;
    private String Nome, Descricao;
    private int DistanciaAproximada;

    //Dados dos Beacons
    private String UniqueId, MinorId, MajorId;

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

    public String getUniqueId() {
        return UniqueId;
    }

    public void setUniqueId(String uniqueId) {
        UniqueId = uniqueId;
    }

    public String getMinorId() {
        return MinorId;
    }

    public void setMinorId(String minorId) {
        MinorId = minorId;
    }

    public String getMajorId() {
        return MajorId;
    }

    public void setMajorId(String majorId) {
        MajorId = majorId;
    }

    public DestinoModel(){

    }

    public DestinoModel(int IdDestino, int IdPredio, int IdTipoDestino, int IdCategoria, String Nome,
                         String UniqueId, String MajorId, String MinorId, String Descricao){
        this.IdDestino = IdDestino;
        this.IdPredio= IdPredio;
        this.IdTipoDestino= IdTipoDestino;
        this.IdCategoria= IdCategoria;
        this.Nome= Nome;
        this.UniqueId= UniqueId;
        this.MajorId= MajorId;
        this.MinorId= MinorId;
        this.Descricao= Descricao;
    }
}
