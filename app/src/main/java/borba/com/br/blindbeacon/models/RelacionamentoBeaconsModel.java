package borba.com.br.blindbeacon.models;

/**
 * Created by andre on 22/03/2017.
 */

public class RelacionamentoBeaconsModel {
    private int IdRelacionamentoBeacon;
    private String UniqueId, UniqueIdRelacionamento, Orientacao;
    private Double Distancia;

    public int getIdRelacionamentoBeacon() {
        return IdRelacionamentoBeacon;
    }

    public void setIdRelacionamentoBeacon(int idRelacionamentoBeacon) {
        IdRelacionamentoBeacon = idRelacionamentoBeacon;
    }

    public String getUniqueId() {
        return UniqueId;
    }

    public void setUniqueId(String uniqueId) {
        UniqueId = uniqueId;
    }

    public String getUniqueIdRelacionamento() {
        return UniqueIdRelacionamento;
    }

    public void setUniqueIdRelacionamento(String uniqueIdRelacionamento) {
        UniqueIdRelacionamento = uniqueIdRelacionamento;
    }

    public String getOrientacao() {
        return Orientacao;
    }

    public void setOrientacao(String orientacao) {
        Orientacao = orientacao;
    }

    public Double getDistancia() {
        return Distancia;
    }

    public void setDistancia(Double distancia) {
        Distancia = distancia;
    }

    public RelacionamentoBeaconsModel(){

    }
}
