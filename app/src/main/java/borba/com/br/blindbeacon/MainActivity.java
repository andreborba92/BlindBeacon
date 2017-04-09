package borba.com.br.blindbeacon;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

import java.util.ArrayList;

import borba.com.br.blindbeacon.database.DataBaseHandler;
import borba.com.br.blindbeacon.datamodels.PredioDataModel;
import borba.com.br.blindbeacon.models.PredioModel;

public class MainActivity extends Activity {

    Context ctx;
    public DataBaseHandler dbHandler;
    Boolean exibir = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.ctx = this;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }



    @Override
    public void onStart() {
        super.onStart();
        TTSManager.Initialize(ctx);

        //Verifica permissão de localização
        Log.w("TAG_BEACON_ADD", "Vai verificar permissão localização");
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            Log.w("TAG_BEACON_ADD", "Não possui. Vai solicitar");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        }

        //Carrega dependências
          Log.w("Database", "Vai iniciar Database");
          this.dbHandler = new DataBaseHandler(this);
//          Log.w("Database", "Database - Start Fake Data");
//          this.dbHandler.LoadWithFakeData();
//          Log.w("Database", "Database - End Fake Data");

//          Log.w("Database", "Database - Start Data Test");
//          this.dbHandler.TestFakeData();
//          Log.w("Database", "Database - End Data Test");

        //Verifica GPS Ativo
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean GPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if(!GPSEnabled){

            Toast.makeText(ctx, "Voce deve ativar  o GPS", Toast.LENGTH_LONG).show();

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                }
            }, 2000);

        }

        //Verifica Bluetooth
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (mBluetoothAdapter != null && !mBluetoothAdapter.isEnabled()){
                // Bluetooth is not enable
                Toast.makeText(ctx, "Voce deve ativar  o Bluetooth", Toast.LENGTH_LONG).show();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        startActivity(new Intent(Settings.ACTION_BLUETOOTH_SETTINGS));
                    }
                }, 2000);
            }
     }

    @Override
    public void onStop() {
        super.onStop();
    }

    public void onClickBeaconFinder(View v){
        Intent in = new Intent(this, BeaconFinderActivity.class);
        startActivity(in);
    }

    public void onClickSelecionarPredio(View v){
        Intent in = new Intent(this, SelecionarPredioActivity.class);
        startActivity(in);
    }

    public void onClickBtnAjuda(View v){
        Intent in = new Intent(this, AjudaActivity.class);
        startActivity(in);
    }

//    private void TesteGIF(){
//
//        wView = (WebView) this.findViewById(R.id.webView);
//        wView.loadUrl("file:///android_asset/bluetooth.gif");
//        //setContentView(wView);
//    }
}
