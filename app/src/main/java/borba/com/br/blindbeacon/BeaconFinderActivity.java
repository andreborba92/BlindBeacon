package borba.com.br.blindbeacon;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.beacon_finder);
        beaconManager = BeaconManager.getInstanceForApplication(this);
        // To detect proprietary beacons, you must add a line like below corresponding to your beacon
        // type.  Do a web search for "setBeaconLayout" to get the proper expression.
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
                    Beacon teste = beacons.iterator().next();

                    if (!MyBeacons.contains(teste)) {
                        MyBeacons.add(teste);
                    } else {
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
