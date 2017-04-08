package borba.com.br.blindbeacon;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.google.gson.Gson;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import borba.com.br.blindbeacon.datamodels.DestinoDataModel;
import borba.com.br.blindbeacon.datamodels.RotaDataModel;
import borba.com.br.blindbeacon.enums.TipoDestinoEnum;
import borba.com.br.blindbeacon.models.DestinoModel;
import borba.com.br.blindbeacon.models.RotaModel;
import borba.com.br.blindbeacon.utils.BeaconDestinoComparator;
import borba.com.br.blindbeacon.viewmodels.BeaconDestinoViewModel;
import borba.com.br.blindbeacon.viewmodels.RotaDestinoViewModel;

/**
 * Created by andre on 08/04/2017.
 */
//ToDo: Rever toda a lógica desta tela....
//ToDo: Lista interna com os beacons encontrados. Lista "visível" com a ordem dos pontos da rota.
public class RotaActivity extends Activity implements BeaconConsumer {

    private BeaconManager beaconManager;
    private Region beaconScanRegion;
    Context ctx;

    //private ListView lvBeaconsLocalizados;
    private ListView lvRotas;
    ArrayList<BeaconDestinoViewModel> MyBeacons;
    ArrayList<RotaDestinoViewModel> MyRotas;
    ArrayList<RotaDestinoViewModel> RotasExibicao;
    BeaconDestinoViewModel beaconMaisProximo;

    private DestinoDataModel destinoDataModel;
    private RotaDataModel rotaDataModel;

    private BeaconDestinoViewModel beaconDestinoViewModel;
    //private RotaDestinoViewModel rotaDestinoViewModel;
    private DecimalFormat df = new DecimalFormat("#.##");

    private ArrayList<DestinoModel> listDestinosExistentesNaRota = new ArrayList<>();

    DestinoModel destinoFinal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rota);

        destinoDataModel = new DestinoDataModel(this);
        rotaDataModel = new RotaDataModel(this);

        beaconManager = BeaconManager.getInstanceForApplication(this);

        //ToDo: Notificar via áudio a mudança de distância a cada X pulsos ou a cada X distancia alterada.
        //Apenas do mais próximo através da prop Orientação (F/T)

        //Setting tempos de duração dos scans. 8 segundos entre scan
        beaconManager.setForegroundBetweenScanPeriod(5000L);
        beaconManager.setForegroundScanPeriod(8000L);

        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
        beaconManager.bind(this);
        ctx = this;

        lvRotas = (ListView)findViewById(R.id.lvPontosRota);
        MyBeacons = new ArrayList<>();
        MyRotas = new ArrayList<>();
        beaconScanRegion = new Region("myRangingUniqueIdaa", null, null, null);

        CarregarListaRotas();
    }

    private void CarregarListaRotas(){
        //Com base no destino selecionado, carrega a listagem de rotas
        Intent in = getIntent();
        String serializedDestino = in.getStringExtra("DestinoSelecionado");

        Gson myGson = new Gson();
        DestinoModel destino = myGson.fromJson(serializedDestino, DestinoModel.class);

        ArrayList<RotaModel> _rotas = rotaDataModel.getAllByPredioId(destino.getIdPredio());
        Log.w("TAG_FLUXO", "Quantidade de rotas localizados: " + _rotas.size());

        for(int i = 0; i < _rotas.size(); i++){
            destinoFinal = destinoDataModel.getByIdDestino(_rotas.get(i).getIdDestino());

            listDestinosExistentesNaRota.add(destinoFinal);
            MyRotas.add(new RotaDestinoViewModel(_rotas.get(i), destinoFinal));
        }

        Log.w("TAG_FLUXO", "Quantidade de itens na listaDestinos: " + listDestinosExistentesNaRota.size());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        beaconManager.unbind(this);
    }

    @Override
    public void onBeaconServiceConnect() {
        beaconManager.setRangeNotifier(new RangeNotifier() {

            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                MyBeacons = new ArrayList<BeaconDestinoViewModel>();

                if (beacons.size() > 0) {

                    Log.w("TAG_FLUXO", "Quantidade beacons localizados: " + beacons.size());

                    for(Beacon beaconLocalizado:beacons) {
                        DestinoModel vm = destinoDataModel.getByBeacon(listDestinosExistentesNaRota, String.valueOf(beaconLocalizado.getId1()),
                                String.valueOf(beaconLocalizado.getId2()), String.valueOf(beaconLocalizado.getId3()));

                        if(vm == null)
                            continue;

                        beaconDestinoViewModel = new BeaconDestinoViewModel(beaconLocalizado, vm);

                        //O código abaixo evita que adicione duas vezes o mesmo beacon na coleção
                        int indexOF = VerificaBeaconExistente(MyBeacons, beaconDestinoViewModel);

                        if (indexOF == -1) {
                            MyBeacons.add(beaconDestinoViewModel);
                        } else {
                            MyBeacons.remove(indexOF);
                            MyBeacons.add(beaconDestinoViewModel);
                        }
                    }

                    Collections.sort(MyBeacons, new BeaconDestinoComparator());
                    beaconMaisProximo = MyBeacons.get(0);

                    Log.w("TAG_FLUXO", "Beacon mais próximo: " + beaconMaisProximo.getDestinoModel().getNome());

                    //Aqui verifica em que ponto da rota o usuário está
                    int posicaoPontoAtual = 0;
                    int posicaoDestinoFinal = 0;

                    for(RotaDestinoViewModel _rotaCorrente:MyRotas){
                        if(_rotaCorrente.getDestinoModel().getIdDestino() == destinoFinal.getIdDestino())
                            posicaoDestinoFinal = _rotaCorrente.getRotaModel().getOrdem();

                        if(_rotaCorrente.getDestinoModel().getIdDestino() == beaconMaisProximo.getDestinoModel().getIdDestino())
                            posicaoPontoAtual = _rotaCorrente.getRotaModel().getOrdem();
                    }

                    Log.w("TAG_FLUXO", "Posição Atual: " + posicaoPontoAtual + "; Posição Final: " + posicaoDestinoFinal);

                    //A coleção abaixo só irá receber os pontos entre o local atual e o destino final
                    RotasExibicao = RotaDestinoViewModel.getPontosEntreOrigemDestino(MyRotas, posicaoPontoAtual,
                            posicaoDestinoFinal, beaconMaisProximo.getBeacon().getDistance());

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                        Log.w("TAG_FLUXO","Status do lvRotas: " + lvRotas);
                        lvRotas.setAdapter(new RotaAdapter(ctx, R.layout.list_item_rota, RotasExibicao));
                        }
                    });

                    RotaDestinoViewModel proximoPonto = RotasExibicao.size() > 1 ? RotasExibicao.get(1) : null;
                    String nomeProxDestino = proximoPonto == null ? "" : proximoPonto.getDestinoModel().getNome();
                    String distanciaProxDestino = proximoPonto == null ? "" : " em " + proximoPonto.getDistanciaFormatada();

                    //Orientação frente
                    if(posicaoPontoAtual < posicaoDestinoFinal){
                        String textoLocal = "Você está perto do ponto: " + beaconMaisProximo.getDestinoModel().getNome() + ". ";

                        String orientacao = RotasExibicao.get(0).getRotaModel().getOrientacaoFrente().replace("{NOME}",
                                nomeProxDestino + distanciaProxDestino);

                        Log.w("TAG_FLUXO", textoLocal + orientacao);
                        TTSManager.Speak(textoLocal + orientacao);
                    }
                    else{
                        String textoLocal = "Você está perto do ponto: " + beaconMaisProximo.getDestinoModel().getNome() + ". ";

                        String orientacao = RotasExibicao.get(0).getRotaModel().getOrientacaoTras().replace("{NOME}",
                                nomeProxDestino + distanciaProxDestino);

                        Log.w("TAG_FLUXO", textoLocal + orientacao);
                        TTSManager.Speak(textoLocal + orientacao);
                    }

                    //ToDo: Reproduzir TTS com o destino mais próximo e orientações (F/T)
                    // Enviar para o adapter a distancia do ponto inicial pra somar com a do final

                    /*
                    for(Beacon beaconLocalizado:beacons){

                        Log.w("TAG_BEACON_ADD", "Beacon pulse localizado: " + String.valueOf(beaconLocalizado.getId1()) +
                                "; Minor: " + String.valueOf(beaconLocalizado.getId3()) +
                                " Distancia: " + beaconLocalizado.getDistance());

                 !!       DestinoModel vm = destinoDataModel.getByBeacon(listDestinosExistentesNaRota, String.valueOf(beaconLocalizado.getId1()),
                                String.valueOf(beaconLocalizado.getId2()), String.valueOf(beaconLocalizado.getId3()));

                        if(vm == null)
                            continue;

                        Log.w("TAG_BEACON_ADD", "Destino: " + vm.getNome());

                 !!       if(vm.getIdTipoDestino() == TipoDestinoEnum.OBSTACULO.getValue()){
                            Log.w("TAG_BEACON_ADD", "Obstáculo localizado: " + vm.getNome() +
                                    "Distancia: " + beaconLocalizado.getDistance());

                            String distanciaFormatada = df.format(beaconLocalizado.getDistance()) + " metros";

                            //ToDo: Melhorar forma de reproduzir audio para nao ser uma metralhadora de notificações
                            //                        TTSManager.Speak("Obstáculo localizado, " + vm.getNome() + " a " + distanciaFormatada +
                            //                        " de distância.");
                            continue;
                        }

                        //Inserção de info para identificação
                 !!       beaconDestinoViewModel = new BeaconDestinoViewModel(beaconLocalizado, vm);

                        int indexOF = VerificaBeaconExistente(MyBeacons, beaconDestinoViewModel);

                        if (indexOF == -1) {
                            MyBeacons.add(beaconDestinoViewModel);
                        } else {
                            MyBeacons.remove(indexOF);
                            MyBeacons.add(beaconDestinoViewModel);
                        }

                    }

                    Collections.sort(MyBeacons, new BeaconDestinoComparator());

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            lvRotas.setAdapter(new RotaAdapter(ctx, R.layout.list_item_rota, RotasExibicao));
                        }
                    });
                    */

                }
                else{
                    //ToDo: Avisar que não localizou pontos... andar um pouco
                    TTSManager.Speak("Não foi possível determinar sua localização, favor caminhe mais um pouco");
                }

            }

        });

        try {
            beaconManager.startRangingBeaconsInRegion(beaconScanRegion);


        } catch (RemoteException e) {    }
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();

    }

//    public void onClickPararScan(View v) throws RemoteException {
//        beaconManager.stopRangingBeaconsInRegion(beaconScanRegion);
//
//    }


    //Verificação dentro da listagem de beacons, se um único item existe
    private int VerificaBeaconExistente(ArrayList<BeaconDestinoViewModel> list, BeaconDestinoViewModel object) {
        int s = list.size() -1;

        if (object != null) {
            for (int i = 0; i < s; i++) {
                Log.w("ID DESTINO", String.valueOf(list.get(i).getDestinoModel().getIdDestino()));

                if (object.getDestinoModel().getIdDestino() == list.get(i).getDestinoModel().getIdDestino()){
                    return i;
                }
            }
        }

        return -1;
    }
}
