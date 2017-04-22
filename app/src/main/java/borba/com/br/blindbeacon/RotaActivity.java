package borba.com.br.blindbeacon;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
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

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import borba.com.br.blindbeacon.datamodels.DestinoDataModel;
import borba.com.br.blindbeacon.datamodels.RotaDataModel;
import borba.com.br.blindbeacon.enums.TipoDestinoEnum;
import borba.com.br.blindbeacon.models.DestinoModel;
import borba.com.br.blindbeacon.models.RotaModel;
import borba.com.br.blindbeacon.utils.BeaconDestinoComparator;
import borba.com.br.blindbeacon.utils.BeaconNativeComparator;
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
        beaconManager.setForegroundBetweenScanPeriod(1000L); //12000L
        beaconManager.setForegroundScanPeriod(3000L);

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

private int contador = 0;
private Boolean firstSearch = true;
private Beacon _closerBeaconInIteration, _closerBeaconResult;
private ArrayList<Beacon> _listBeaconsTesteRange = new ArrayList<>();
private ArrayList<Beacon> _listBeaconsProximosRange = new ArrayList<>();

    @Override
    public void onBeaconServiceConnect() {

        beaconManager.setRangeNotifier(new RangeNotifier() {

            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                MyBeacons = new ArrayList<BeaconDestinoViewModel>();
                RotasExibicao = new ArrayList<RotaDestinoViewModel>();

                if (beacons.size() > 0) {

                    if(firstSearch){
                        firstSearch = false;
                        TTSManager.Speak("Por favor, aguarde enquanto verifico sua localização");
                    }

                    _listBeaconsTesteRange = new ArrayList<Beacon>();

                    for(Beacon b:beacons){
                        //Adiciona na lista apenas beacon mapeados
                        DestinoModel vm = destinoDataModel.getByBeacon(listDestinosExistentesNaRota, String.valueOf(b.getId1()),
                                String.valueOf(b.getId2()), String.valueOf(b.getId3()));

                        if(vm == null)
                            continue;

                        _listBeaconsTesteRange.add(b);
                    }

                    Collections.sort(_listBeaconsTesteRange, new BeaconNativeComparator());
                    _closerBeaconInIteration = _listBeaconsTesteRange.get(0);

                    _listBeaconsProximosRange.add(_closerBeaconInIteration);

                    contador++;
                    Log.w("TAG_FLUXO", "DEU SCAN: " + contador);

                    if(contador < 4)
                        return;

                    if(contador == 4){

                        //Caso não encontrou nenhum nas 4 iterações, avisa e reinicia
                        if(_listBeaconsProximosRange.size() == 0){
                            ManipularVisibilidadeGIF(true);
                            TTSManager.Speak("Não foi possível determinar sua localização, favor caminhar mais um pouco");
                            contador = 0;
                            return;
                        }

                        _closerBeaconResult = getCloserFromList(_listBeaconsProximosRange);

                        DestinoModel vm = destinoDataModel.getByBeacon(listDestinosExistentesNaRota, String.valueOf(_closerBeaconResult.getId1()),
                                String.valueOf(_closerBeaconResult.getId2()), String.valueOf(_closerBeaconResult.getId3()));

                        beaconMaisProximo = new BeaconDestinoViewModel(_closerBeaconResult, vm);

                        _listBeaconsProximosRange = new ArrayList<Beacon>();
                        contador = 0;
                    }

                    ManipularVisibilidadeGIF(false);

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
                    //Log.w("TAG_FLUXO", "Nao localizou nada. Audio");

//                    ManipularVisibilidadeGIF(true);
//                    TTSManager.Speak("Não foi possível determinar sua localização, favor caminhar mais um pouco");
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
                    //wView.setVisibility(View.VISIBLE);
                    lvRotas.setVisibility(View.INVISIBLE);
                }
                else{
                    //wView.setVisibility(View.VISIBLE);
                    lvRotas.setVisibility(View.VISIBLE);
                }
            }
        });



    }

    //Este método irá receber a lista do "melhor de 4" pesquisas de beacon próximos. Irá retornar o Beacon mais próximo pelos seguintes critérios:
    //Retorna o que apareceu mais vezes na lista. Caso seja 2/2, retorna o com a distancia mais próxima.
    //A lista sempre tera 4 posíções
    private Beacon getCloserFromList(ArrayList<Beacon> listaPesquisada){
        //Validar pelo unique, major e minor. Distance é só para desempate
        ArrayList<Beacon> _col1 = new ArrayList<>();
        ArrayList<Beacon> _col2 = new ArrayList<>();
        ArrayList<Beacon> _col3 = new ArrayList<>();
        ArrayList<Beacon> _col4 = new ArrayList<>();

        ArrayList<ArrayList<Beacon>> _colecoes = new ArrayList<>();
        _colecoes.add(_col1);_colecoes.add(_col2);_colecoes.add(_col3);_colecoes.add(_col4);

        int indexBeaconCorrente = 0;
        ArrayList<Integer> indexJaInserdos = new ArrayList<>();

        for(ArrayList<Beacon> colecaoCorrente:_colecoes){
            indexBeaconCorrente = 0;

            for(Beacon beaconCorrente:listaPesquisada){

                Boolean beaconJaInserido = false;
                for(int i = 0; i < indexJaInserdos.size(); i++){
                    if(indexBeaconCorrente == indexJaInserdos.get(i))
                        beaconJaInserido = true;
                }

                if(beaconJaInserido) {
                    indexBeaconCorrente++;
                    continue;
                }

                if(colecaoCorrente.size() == 0){
                    //Caso não tenha registro, insere
                    colecaoCorrente.add(beaconCorrente);

                    indexJaInserdos.add(indexBeaconCorrente);
                    indexBeaconCorrente++;
                    continue;
                }

                //Caso a coleção já tenha registros, só insere se o Beacon é igual
                if(beaconCorrente.getId1().equals(colecaoCorrente.get(0).getId1()) &&
                        beaconCorrente.getId2().equals(colecaoCorrente.get(0).getId2()) &&
                        beaconCorrente.getId3().equals(colecaoCorrente.get(0).getId3())) {

                    //Caso o beaconCorrente seja igual ao existente na listaCorrente, adiciona
                    colecaoCorrente.add(beaconCorrente);
                    indexJaInserdos.add(indexBeaconCorrente);
                    indexBeaconCorrente++;

                    continue;
                }

                indexBeaconCorrente++;
            }
        }


        int maxSize = getMaxSize(_colecoes);

        ArrayList<ArrayList<Beacon>> listsEquivalentes = new ArrayList<>();
        if(_col1.size() == maxSize)
            listsEquivalentes.add(_col1);
        if(_col2.size() == maxSize)
            listsEquivalentes.add(_col2);
        if(_col3.size() == maxSize)
            listsEquivalentes.add(_col3);
        if(_col4.size() == maxSize)
            listsEquivalentes.add(_col4);

        if(listsEquivalentes.size() == 1)
            return listsEquivalentes.get(0).get(0);

        Beacon _beaconMaisProximoCompare = null;

        for(ArrayList<Beacon> b:listsEquivalentes){
            if(_beaconMaisProximoCompare == null) {
                _beaconMaisProximoCompare = b.get(0);
                continue;
            }

            if(b.get(0).getDistance() < _beaconMaisProximoCompare.getDistance())
                _beaconMaisProximoCompare = b.get(0);
        }

        return _beaconMaisProximoCompare;
    }

    private int getMaxSize(ArrayList<ArrayList<Beacon>> list){
        int maxSize = 0;

        for(int i = 0; i < list.size(); i++){
            if(list.get(i).size() > maxSize)
                maxSize = list.get(i).size();
        }

        return maxSize;
    }
}
