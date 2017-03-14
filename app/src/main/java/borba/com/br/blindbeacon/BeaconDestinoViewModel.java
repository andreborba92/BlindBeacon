package borba.com.br.blindbeacon;

import org.altbeacon.beacon.Beacon;

/**
 * Created by andre on 13/03/2017.
 */

public class BeaconDestinoViewModel {
    private String UniqueID, MinorID, MajorID, Descricao, Tipo, Categoria, Nome;

    //Beacons: UniqueId, MinorId, MajorId, Descricao, IdLocal, “Lat, Long”
    //Locais: Id, IdSetor, Tipo (destino, conexão), IdCategoria, Nome, Descricao

    public String getUniqueID() {
        return UniqueID;
    }

    public void setUniqueID(String uniqueID) {
        UniqueID = uniqueID;
    }

    public String getMinorID() {
        return MinorID;
    }

    public void setMinorID(String minorID) {
        MinorID = minorID;
    }

    public String getMajorID() {
        return MajorID;
    }

    public void setMajorID(String majorID) {
        MajorID = majorID;
    }

    public String getDescricao() {
        return Descricao;
    }

    public void setDescricao(String descricao) {
        Descricao = descricao;
    }

    public String getTipo() {
        return Tipo;
    }

    public void setTipo(String tipo) {
        Tipo = tipo;
    }

    public String getCategoria() {
        return Categoria;
    }

    public void setCategoria(String categoria) {
        Categoria = categoria;
    }

    public String getNome() {
        return Nome;
    }

    public void setNome(String nome) {
        Nome = nome;
    }

    public BeaconDestinoViewModel(String uniqueId, String minorID, String majorID, String descricao,
                                  String tipo, String categoria, String nome){
        this.UniqueID = uniqueId;
        this.MinorID = minorID;
        this.MajorID = majorID;
        this.Descricao = descricao;
        this.Tipo = tipo;
        this.Categoria = categoria;
        this.Nome = nome;
    }

}
