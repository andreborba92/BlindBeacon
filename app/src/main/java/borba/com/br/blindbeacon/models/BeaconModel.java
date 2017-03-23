package borba.com.br.blindbeacon.models;

/**
 * Created by andre on 22/03/2017.
 */

public class BeaconModel {
    private String UniqueId, MinorId, MajorId, Descricao;
    private int IdDestino;

    public int getIdDestino() {
        return IdDestino;
    }

    public void setIdDestino(int idDestino) {
        IdDestino = idDestino;
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

    public String getDescricao() {
        return Descricao;
    }

    public void setDescricao(String descricao) {
        Descricao = descricao;
    }

    public BeaconModel() {

    }
}
