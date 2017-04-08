package borba.com.br.blindbeacon.viewmodels;

import org.altbeacon.beacon.Beacon;

import java.text.DecimalFormat;
import java.util.ArrayList;

import borba.com.br.blindbeacon.models.DestinoModel;
import borba.com.br.blindbeacon.models.RotaModel;

/**
 * Created by andre on 08/04/2017.
 */

public class RotaDestinoViewModel implements Comparable<RotaDestinoViewModel> {
    private RotaModel _rotaModel;
    private DestinoModel _destinoModel;
    private Double _distanciaDoPontoOrigem;
    //ToDo: Ver se terá uma prop para a metragem "origem" para este ponto da rota

    public RotaDestinoViewModel(RotaModel rota, DestinoModel destino){
        this._rotaModel = rota;
        this._destinoModel = destino;
    }

    public RotaModel getRotaModel() {
        return _rotaModel;
    }

    public DestinoModel getDestinoModel() {
        return _destinoModel;
    }

    public Double getDistanciaDoPontoOrigem() {
        return _distanciaDoPontoOrigem;
    }

    public void setDistanciaDoPontoOrigem(Double _distanciaDoPontoOrigem) {
        this._distanciaDoPontoOrigem = _distanciaDoPontoOrigem;
    }

    public String getDistanciaFormatada(){
        DecimalFormat df = new DecimalFormat("#.##");
        Double distancia = this.getDistanciaDoPontoOrigem();
        return df.format(distancia) + " metros";
    }

    public static ArrayList<RotaDestinoViewModel> getPontosEntreOrigemDestino(ArrayList<RotaDestinoViewModel> rotas,
                                                           int posicaoOrigem, int posicaoDestino, double distanciaPontoOrigem){
        ArrayList<RotaDestinoViewModel> _rotasRetorno = new ArrayList<>();

        for(RotaDestinoViewModel vm:rotas){
            //Caso esteja avançando no trajeto
            if(posicaoOrigem < posicaoDestino){
                if(vm.getRotaModel().getOrdem() >= posicaoOrigem && vm.getRotaModel().getOrdem() <= posicaoDestino)
                    _rotasRetorno.add(vm);
            }

            //Caso esteja voltando no trajeto
            if(posicaoOrigem > posicaoDestino){
                if(vm.getRotaModel().getOrdem() <= posicaoOrigem && vm.getRotaModel().getOrdem() >= posicaoDestino)
                    _rotasRetorno.add(vm);
            }
        }

        Boolean primeiroRegistro = true;
        Double distanciaAcumulada = distanciaPontoOrigem;

        for(RotaDestinoViewModel vm:_rotasRetorno) {

            if(primeiroRegistro)
                vm.setDistanciaDoPontoOrigem(distanciaPontoOrigem);

            distanciaAcumulada += vm.getRotaModel().getMetragem();
            vm.setDistanciaDoPontoOrigem(distanciaAcumulada);

            primeiroRegistro = false;
        }

        return _rotasRetorno;
    }

    @Override
    public int compareTo(RotaDestinoViewModel another) {
        if(this.getDestinoModel().getIdDestino() < another.getDestinoModel().getIdDestino())
            return -1;
        else if(this.getDestinoModel().getIdDestino() > another.getDestinoModel().getIdDestino())
            return 1;
        else
            return 0;
    }
}
