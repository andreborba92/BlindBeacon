package borba.com.br.blindbeacon;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ListViewCompat;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MainActivity extends Activity implements BeaconConsumer {

    protected static final String TAG = "MonitoringActivity";
    private BeaconManager beaconManager;
private ListView lvMyBeacons;
    ArrayList<Beacon> MyBeacons;
    Context ctx;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        beaconManager = BeaconManager.getInstanceForApplication(this);
        // To detect proprietary beacons, you must add a line like below corresponding to your beacon
        // type.  Do a web search for "setBeaconLayout" to get the proper expression.
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
        beaconManager.bind(this);
        ctx = this;


        lvMyBeacons = (ListView)findViewById(R.id.lvMyBeacons);
        MyBeacons = new ArrayList<>();

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

                    if(!MyBeacons.contains(teste)) {
                        MyBeacons.add(teste);
                    }
                    else
                    {
                        int index = MyBeacons.indexOf(teste);
                        MyBeacons.remove(index);
                        MyBeacons.add(index,teste);
                    }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                lvMyBeacons.setAdapter( new BeaconsAdapter(ctx,R.layout.list_item_beacon, MyBeacons));
                            }
                        });



                }
            }

        });

        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));


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
}
