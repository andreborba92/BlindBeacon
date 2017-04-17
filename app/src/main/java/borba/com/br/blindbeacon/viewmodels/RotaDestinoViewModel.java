package borba.com.br.blindbeacon.viewmodels;

import org.altbeacon.beacon.Beacon;

import java.text.DecimalFormat;
import java.util.ArrayList;

import borba.com.br.blindbeacon.enums.CategoriaEnum;
import borba.com.br.blindbeacon.enums.TipoDestinoEnum;
import borba.com.br.blindbeacon.models.DestinoModel;
import borba.com.br.blindbeacon.models.RotaModel;

/**
 * Created by andre on 08/04/2017.
 */

public class RotaDestinoViewModel implements Comparable<RotaDestinoViewModel> {
    private RotaModel _rotaModel;
    private DestinoModel _destinoModel;
    private Double _distanciaDoPontoOrigem;
    
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

            if(posicaoOrigem == posicaoDestino) {
                if(vm.getRotaModel().getOrdem() == posicaoDestino)
                    _rotasRetorno.add(vm);
            }
        }

        Boolean primeiroRegistro = true;
        Double distanciaAcumulada = distanciaPontoOrigem;
        Double metragemInicial = 0.0;
        RotaDestinoViewModel pontoOrigem = _rotasRetorno.get(0);

        for(RotaDestinoViewModel vm:_rotasRetorno) {

            if(primeiroRegistro) {
                primeiroRegistro = false;

                metragemInicial = vm.getRotaModel().getMetragem();
                vm.setDistanciaDoPontoOrigem(distanciaPontoOrigem);

                //No primeiro registro este valor será negativo, pois ele está tirando toda a metragem inicial até o ponto de origem
                distanciaAcumulada += distanciaPontoOrigem - metragemInicial;
                continue;
            }

            distanciaAcumulada = vm.getRotaModel().getMetragem() - pontoOrigem.getRotaModel().getMetragem();
            vm.setDistanciaDoPontoOrigem(distanciaAcumulada);


//            if(vm.getDestinoModel().getIdTipoDestino() == TipoDestinoEnum.OBSTACULO.getValue())
//                vm.setDistanciaDoPontoOrigem(distanciaAcumulada);
//            else

        }

        return _rotasRetorno;
    }

    public static Double getDistanciaTotalEntreOrigemDestino(ArrayList<RotaDestinoViewModel> lista, int origem, int destino,
                                                             Double distanciaProximoPonto) {
        RotaDestinoViewModel rotaOrigem = null;
        RotaDestinoViewModel rotaDestino = null;

        for(RotaDestinoViewModel vm: lista){
            if(vm.getRotaModel().getOrdem() == origem)
                rotaOrigem = vm;

            if(vm.getRotaModel().getOrdem() == destino)
                rotaDestino = vm;
        }

        //ToDo: Comando comentado onde contabiliza a distancia do primeiro ponto
        //Double distancia = rotaDestino.getRotaModel().getMetragem() - rotaOrigem.getRotaModel().getMetragem() + distanciaProximoPonto;
        Double distancia = rotaDestino.getRotaModel().getMetragem() - rotaOrigem.getRotaModel().getMetragem();

        if(distancia < 0)
            distancia = distancia * -1;

        return distancia;
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
