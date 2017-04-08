package borba.com.br.blindbeacon.models;

/**
 * Created by andre on 07/04/2017.
 */

public class RotaModel {

    private int IdRota, IdPredio, IdDestino, Ordem;
    private String OrientacaoFrente, OrientacaoTras;
    private double Metragem;

    public RotaModel(){}

    public RotaModel(int IdPredio, int IdDestino, int Ordem, String OrientacaoF, String OrientacaoT, double Metragem){
        this.IdPredio = IdPredio;
        this.IdDestino = IdDestino;
        this.Ordem = Ordem;
        this.OrientacaoFrente = OrientacaoF;
        this.OrientacaoTras = OrientacaoT;
        this.Metragem = Metragem;
    }

    public int getIdRota() {
        return IdRota;
    }

    public void setIdRota(int idRota) {
        IdRota = idRota;
    }

    public int getIdPredio() {
        return IdPredio;
    }

    public void setIdPredio(int idPredio) {
        IdPredio = idPredio;
    }

    public int getIdDestino() {
        return IdDestino;
    }

    public void setIdDestino(int idDestino) {
        IdDestino = idDestino;
    }

    public int getOrdem() {
        return Ordem;
    }

    public void setOrdem(int ordem) {
        Ordem = ordem;
    }

    public String getOrientacaoFrente() {
        return OrientacaoFrente;
    }

    public void setOrientacaoFrente(String orientacaoFrente) {
        OrientacaoFrente = orientacaoFrente;
    }

    public String getOrientacaoTras() {
        return OrientacaoTras;
    }

    public void setOrientacaoTras(String orientacaoTras) {
        OrientacaoTras = orientacaoTras;
    }

    public double getMetragem() {
        return Metragem;
    }

    public void setMetragem(double metragem) {
        Metragem = metragem;
    }
}
