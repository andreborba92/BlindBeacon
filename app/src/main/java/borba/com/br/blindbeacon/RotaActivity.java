package borba.com.br.blindbeacon;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
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

public class RotaActivity extends Activity implements BeaconConsumer {

    private BeaconManager beaconManager;
    private Region beaconScanRegion;
    Context ctx;

    private ListView lvRotas;
    private WebView wView;

    ArrayList<BeaconDestinoViewModel> MyBeacons;
    ArrayList<RotaDestinoViewModel> MyRotas;
    ArrayList<RotaDestinoViewModel> RotasExibicao;
    BeaconDestinoViewModel beaconMaisProximo;

    private DestinoDataModel destinoDataModel;
    private RotaDataModel rotaDataModel;

    private BeaconDestinoViewModel beaconDestinoViewModel;
    private DecimalFormat df = new DecimalFormat("#.##");

    private ArrayList<DestinoModel> listDestinosExistentesNaRota = new ArrayList<>();

    Boolean primeiraTransmissao = true;
    DestinoModel destinoFinal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rota);

        destinoDataModel = new DestinoDataModel(this);
        rotaDataModel = new RotaDataModel(this);

        beaconManager = BeaconManager.getInstanceForApplication(this);

        //Setting tempos de duração dos scans. 8 segundos entre scan
        beaconManager.setForegroundBetweenScanPeriod(12000L);
        beaconManager.setForegroundScanPeriod(4000L);

        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
        beaconManager.bind(this);
        ctx = this;

        lvRotas = (ListView)findViewById(R.id.lvPontosRota);
        wView = (WebView) this.findViewById(R.id.webViewGif);
        wView.loadUrl("file:///android_asset/bluetooth.gif");

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
        destinoFinal = myGson.fromJson(serializedDestino, DestinoModel.class);

        ArrayList<RotaModel> _rotas = rotaDataModel.getAllByPredioId(destinoFinal.getIdPredio());
        //Log.w("TAG_FLUXO", "Quantidade de rotas localizados: " + _rotas.size());

        for(int i = 0; i < _rotas.size(); i++){
            DestinoModel _destinoCorrente = destinoDataModel.getByIdDestino(_rotas.get(i).getIdDestino());

            listDestinosExistentesNaRota.add(_destinoCorrente);
            MyRotas.add(new RotaDestinoViewModel(_rotas.get(i), _destinoCorrente));
        }

        //Log.w("TAG_FLUXO", "Quantidade de itens na listaDestinos: " + listDestinosExistentesNaRota.size());
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
                RotasExibicao = new ArrayList<RotaDestinoViewModel>();

                Log.w("TAG_FLUXO", "onBeaconServiceConnect. QTD Localizados: " + beacons.size());

                if (beacons.size() > 0) {

                    ManipularVisibilidadeGIF(false);
                    //Log.w("TAG_FLUXO", "Quantidade beacons localizados: " + beacons.size());

                    for(Beacon beaconLocalizado:beacons) {
//                        Log.w("TAG_FLUXO", "beacon localizado: " + beaconLocalizado.getId1() + "; " +
//                        beaconLocalizado.getId2() + "; " + beaconLocalizado.getId3());

                        DestinoModel vm = destinoDataModel.getByBeacon(listDestinosExistentesNaRota, String.valueOf(beaconLocalizado.getId1()),
                                String.valueOf(beaconLocalizado.getId2()), String.valueOf(beaconLocalizado.getId3()));

                        if(vm == null)
                            continue;

                        beaconDestinoViewModel = new BeaconDestinoViewModel(beaconLocalizado, vm);

                        //O código abaixo evita que adicione duas vezes o mesmo beacon na coleção
//                        int indexOF = VerificaBeaconExistente(MyBeacons, beaconDestinoViewModel);
//
//                        if (indexOF == -1) {
//                            MyBeacons.add(beaconDestinoViewModel);
//                        } else {
//                            MyBeacons.remove(indexOF);
//                            MyBeacons.add(beaconDestinoViewModel);
//                        }
                        MyBeacons.add(beaconDestinoViewModel);
                    }

                    if(MyBeacons.size() == 0){
                        ManipularVisibilidadeGIF(true);
                        TTSManager.Speak("Não foi possível determinar sua localização, favor caminhar mais um pouco");
                        return;
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
                            lvRotas.setAdapter(new RotaAdapter(ctx, R.layout.list_item_rota, RotasExibicao));
                        }
                    });

                    //Executa a transmissão do áudio
                    TransmissaoAudio(posicaoPontoAtual, posicaoDestinoFinal);

                }
                else{
                    Log.w("TAG_FLUXO", "Nao localizou nada. Audio");

                    ManipularVisibilidadeGIF(true);
                    TTSManager.Speak("Não foi possível determinar sua localização, favor caminhar mais um pouco");
                }
            }
        });

        //Inicia requisições
        StartScan();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    public void StartScan(){
        try {
            Log.w("TAG_FLUXO", "Start Scan");
            beaconManager.startRangingBeaconsInRegion(beaconScanRegion);
        } catch (RemoteException e) {  Log.w("TAG_FLUXO", "EXCEPTION Start Scan");  }
    }

    public void StopScan(){
        try {
            Log.w("TAG_FLUXO", "Stop Scan");
            beaconManager.stopRangingBeaconsInRegion(beaconScanRegion);
        } catch (RemoteException e) {  Log.w("TAG_FLUXO", "EXCEPTION Stop Scan");   }
    }

    private void TransmissaoAudio(int posicaoPontoAtual, int posicaoDestinoFinal){

        RotaDestinoViewModel proximoPonto = RotasExibicao.size() > 1 ? RotasExibicao.get(1) : null;
        String nomeProxDestino = proximoPonto == null ? "" : proximoPonto.getDestinoModel().getNome();
        String distanciaProxDestino = proximoPonto == null ? "" : " em " + proximoPonto.getDistanciaFormatada();

        if(primeiraTransmissao){
            primeiraTransmissao = false;
            Double distanciaEntreOrigemDestino = RotaDestinoViewModel.getDistanciaTotalEntreOrigemDestino(RotasExibicao,
                    posicaoPontoAtual, posicaoDestinoFinal, beaconMaisProximo.getBeacon().getDistance());
            String texto = "O destino selecionado foi: " + destinoFinal.getNome() + ". Você está a aproximadamente " +
                    df.format(distanciaEntreOrigemDestino) + " metros de distância. Continue andando que irei te guiar até chegar lá";

            TTSManager.Speak(texto);
            return;
        }


        if(beaconMaisProximo.getDestinoModel().getIdTipoDestino() == TipoDestinoEnum.OBSTACULO.getValue()){
            //Avisa usuário de obstáculo próximo
            //ToDo: Revisar para nao dizer que o próximo ponto é obstáculo, fica redundante.

            String texto = "Cuidado, tem um obstáculo próximo de você. " + beaconMaisProximo.getDestinoModel().getNome() +
                    " em " + df.format(beaconMaisProximo.getBeacon().getDistance()) + " metros. ";
            texto += "Depois você passará pelo local: " + nomeProxDestino + distanciaProxDestino;

            TTSManager.Speak(texto);
            return;
        }

        if(beaconMaisProximo.getDestinoModel().getIdDestino() == destinoFinal.getIdDestino()){
            String textoChegouDestino;

            if(beaconMaisProximo.getBeacon().getDistance() <= 2.0) {
                textoChegouDestino = "Você chegou ao seu destino. " + destinoFinal.getNome();
                //beaconManager.stopRangingBeaconsInRegion(beaconScanRegion);
            }
            else{
                textoChegouDestino = "Você está próximo do seu destino. " + destinoFinal.getNome() + " em " +
                        df.format(beaconMaisProximo.getBeacon().getDistance()) + " metros";
            }

            TTSManager.Speak(textoChegouDestino);
            return;
        }

        if(RotasExibicao.get(0) == null)
            return;

        //Orientação frente
        if(posicaoPontoAtual < posicaoDestinoFinal){
            String textoLocal = "Você está perto do local: " + beaconMaisProximo.getDestinoModel().getNome() + ". ";

            String orientacao = RotasExibicao.get(0).getRotaModel().getOrientacaoFrente().replace("{NOME}",
                    nomeProxDestino + distanciaProxDestino);

            Log.w("TAG_FLUXO", textoLocal + orientacao);
            TTSManager.Speak(textoLocal + orientacao);
        }
        else{
            String textoLocal = "Você está perto do local: " + beaconMaisProximo.getDestinoModel().getNome() + ". ";

            String orientacao = RotasExibicao.get(0).getRotaModel().getOrientacaoTras().replace("{NOME}",
                    nomeProxDestino + distanciaProxDestino);

            Log.w("TAG_FLUXO", textoLocal + orientacao);
            TTSManager.Speak(textoLocal + orientacao);
        }

    }

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

    private void ManipularVisibilidadeGIF(Boolean exibir){

        final Boolean _exibir = exibir;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(_exibir){
                    wView.setVisibility(View.VISIBLE);
                    lvRotas.setVisibility(View.INVISIBLE);
                }
                else{
                    wView.setVisibility(View.INVISIBLE);
                    lvRotas.setVisibility(View.VISIBLE);
                }
            }
        });



    }
}
