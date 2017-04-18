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
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

import borba.com.br.blindbeacon.database.DataBaseHandler;
import borba.com.br.blindbeacon.datamodels.PredioDataModel;
import borba.com.br.blindbeacon.models.PredioModel;

public class MainActivity extends Activity {

    Context ctx;
    public DataBaseHandler dbHandler;
    Boolean exibir = true;
    private Button btnSetores, btnAjuda, btnOuvirNovamente;
    private String textoParaTTS = "Olá, bem vindo ao aplicativo. "+
            "Ao clicar uma vez em algum botão, você irá ouvir a sua descrição. "+
            "Para executar o botão, você deve segurar o botão em um clique longo. "+
            "Abaixo existem os seguintes botões: seleção de setores, ajuda, e ouvir novamente." +
            " Se for sua primeira vez no aplicativo, acesse a tela de ajuda para entender melhor como é, o funcionamento. Para isso," +
            " basta clicar no botão abaixo." ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

        this.ctx = this;

        this.btnSetores = (Button)this.findViewById(R.id.buttonSelecionarPredio);
        this.btnAjuda = (Button)this.findViewById(R.id.buttonAjuda);
        this.btnOuvirNovamente = (Button)this.findViewById(R.id.buttonOuvirNovamente);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onStart() {
        super.onStart();
        TTSManager.Initialize(ctx);

        //Criação de um Handlar para enviar o TTS após X segundos da inicialização
        Handler handlerTTS = new Handler();
        handlerTTS.postDelayed(new Runnable() {
            public void run() {
                // Actions to do after 500 miliseconds
                TTSManager.Speak(textoParaTTS);
            }
        }, 500);

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

        SetListeners();
     }

    @Override
    public void onStop() {
        super.onStop();
    }

    private void SetListeners(){

        btnSetores.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v)
            {
                TTSManager.Speak("Este é o botão de seleção de setores. Pressione para acessar esta funcionalidade.");
            }
        });

        btnSetores.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent in = new Intent(ctx, SelecionarPredioActivity.class);
                startActivity(in);

                return true;
            }
        });

        btnAjuda.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v)
            {
                TTSManager.Speak("Este é o botão de ajuda. Pressione para acessar esta tela.");
            }
        });

        btnAjuda.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent in = new Intent(ctx, AjudaActivity.class);
                startActivity(in);
                return true;
            }
        });

        btnOuvirNovamente.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v)
            {
                TTSManager.Speak("Este é o botão ouvir novamente. Pressione para ouvir.");
            }
        });

        btnOuvirNovamente.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                TTSManager.Speak(textoParaTTS);
                return true;
            }
        });

    }

}
