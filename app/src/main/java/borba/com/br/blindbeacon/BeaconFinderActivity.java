package borba.com.br.blindbeacon;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Collection;

import borba.com.br.blindbeacon.datamodels.DestinoDataModel;
import borba.com.br.blindbeacon.enums.TipoDestinoEnum;
import borba.com.br.blindbeacon.models.DestinoModel;
import borba.com.br.blindbeacon.viewmodels.BeaconDestinoViewModel;

/**
 * Created by André Borba on 20/09/2016.
 */
public class BeaconFinderActivity extends Activity implements BeaconConsumer {

    protected static final String TAG = "MonitoringActivity";
    private BeaconManager beaconManager;
    private ListView lvMyBeacons;
    ArrayList<BeaconDestinoViewModel> MyBeacons;
    private Region beaconScanRegion;
    Context ctx;
    private DestinoDataModel destinoDataModel;
    private BeaconDestinoViewModel beaconDestinoViewModel;

    private ArrayList<DestinoModel> listDestinos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.beacon_finder);

        destinoDataModel = new DestinoDataModel(this);
        listDestinos = destinoDataModel.getAll(1); //Predio 1

        beaconManager = BeaconManager.getInstanceForApplication(this);
        // To detect proprietary beacons, you must add a line like below corresponding to your beacon
        // type.  Do a web search for "setBeaconLayout" to get the proper expression.

        //ToDo: Notificar via áudio a mudança de distância a cada X pulsos ou a cada X distancia alterada
        //Setting tempos de duração dos scans. 2 segundos entre scan
        //beaconManager.setForegroundBetweenScanPeriod(30L);
        beaconManager.setForegroundScanPeriod(1500L);

        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
        beaconManager.bind(this);
        ctx = this;


        lvMyBeacons = (ListView)findViewById(R.id.lvMyBeacons);
        MyBeacons = new ArrayList<>();
        beaconScanRegion = new Region("myRangingUniqueIdaa", null, null, null);

//        lvMyBeacons.setAdapter( new BeaconsAdapter(this,R.layout.list_item_beacon, MyBeacons));

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
                if (beacons.size() > 0) {
                    Beacon beaconLocalizado = beacons.iterator().next();

                    Log.w("TAG_BEACON_ADD", "Beacon pulse localizado: " + String.valueOf(beaconLocalizado.getId1()) +
                            "; Minor: " + String.valueOf(beaconLocalizado.getId3()) +
                            " Distancia: " + beaconLocalizado.getDistance());

                    DestinoModel vm = destinoDataModel.getByBeacon(listDestinos, String.valueOf(beaconLocalizado.getId1()),
                            String.valueOf(beaconLocalizado.getId2()), String.valueOf(beaconLocalizado.getId3()));

                    if(vm == null)
                        return;

                    Log.w("TAG_BEACON_ADD", "Destino: " + vm.getNome());

                    if(vm.getIdTipoDestino() == TipoDestinoEnum.OBSTACULO.getValue()){
                        Log.w("TAG_BEACON_ADD", "Obstáculo localizado: " + vm.getNome() +
                        "Distancia: " + beaconLocalizado.getDistance());
                        return;
                    }

                    //Inserção de info para identificação
                    beaconDestinoViewModel = new BeaconDestinoViewModel(beaconLocalizado, vm);

                    int indexOF = VerificaBeaconExistente(MyBeacons, beaconDestinoViewModel);

                    if (indexOF == -1) {
                        MyBeacons.add(beaconDestinoViewModel);
                    } else {
                        MyBeacons.remove(indexOF);
                        MyBeacons.add(beaconDestinoViewModel);
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            lvMyBeacons.setAdapter(new BeaconsAdapter(ctx, R.layout.list_item_beacon, MyBeacons));
                        }
                    });


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

    public void onClickPararScan(View v) throws RemoteException {
        beaconManager.stopRangingBeaconsInRegion(beaconScanRegion);

    }

    public void onClickReiniciarScan(View v){
        try {
//            int size = MyBeacons.size() -1;
//
//            for(int i = 0; i < size; i++){
//                MyBeacons.remove(i);
//            }

            beaconManager.startRangingBeaconsInRegion(beaconScanRegion);


        } catch (RemoteException e) {    }
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
}
