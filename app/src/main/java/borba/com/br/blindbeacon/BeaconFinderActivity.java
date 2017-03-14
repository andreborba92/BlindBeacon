package borba.com.br.blindbeacon;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by André Borba on 20/09/2016.
 */
public class BeaconFinderActivity extends Activity implements BeaconConsumer {

    protected static final String TAG = "MonitoringActivity";
    private BeaconManager beaconManager;
    private ListView lvMyBeacons;
    ArrayList<Beacon> MyBeacons;
    private Region beaconScanRegion;
    Context ctx;
    public DestinosDB _destinosDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.beacon_finder);
        beaconManager = BeaconManager.getInstanceForApplication(this);
        // To detect proprietary beacons, you must add a line like below corresponding to your beacon
        // type.  Do a web search for "setBeaconLayout" to get the proper expression.

        //Setting tempos de duração dos scans. 2 segundos entre scan
        //beaconManager.setForegroundBetweenScanPeriod(30L);
        //ToDo: Notificar via áudio a mudança de distância a cada X pulsos ou a cada X distancia alterada
        beaconManager.setForegroundScanPeriod(4000L);

        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
        beaconManager.bind(this);
        ctx = this;


        lvMyBeacons = (ListView)findViewById(R.id.lvMyBeacons);
        MyBeacons = new ArrayList<>();
        beaconScanRegion = new Region("myRangingUniqueIdaa", null, null, null);

        _destinosDB = new DestinosDB();

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
                    Beacon teste = beacons.iterator().next();

                    //Log.w("TAG_BEACON_ADD", "vou buscar o UID: " + String.valueOf(teste.getId1()));

                    BeaconDestinoViewModel vm = _destinosDB.getPontoByUUID(String.valueOf(teste.getId1()));

                    if(vm == null)
                        return;

                    if(vm.getCategoria().equals("obstaculo")){
                        Log.w("TAG_BEACON_ADD", "Obstáculo localizado: " + String.valueOf(teste.getId1()) +
                        "Distancia: " + teste.getDistance());
                        return;
                    }

                    if (!MyBeacons.contains(teste)) {
                        MyBeacons.add(teste);
                    } else {
                        Log.w("TAG_BEACON_ADD","UUID: "+String.valueOf(teste.getId1())+"; ID2: "+ String.valueOf(teste.getId2()) +
                                ". Dist: " + teste.getDistance());

                        int index = MyBeacons.indexOf(teste);
                        MyBeacons.remove(index);
                        MyBeacons.add(index, teste);
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
            beaconManager.startRangingBeaconsInRegion(beaconScanRegion);


        } catch (RemoteException e) {    }
    }
}
