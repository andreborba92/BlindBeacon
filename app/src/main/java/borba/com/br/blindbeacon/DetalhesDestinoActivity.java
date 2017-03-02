package borba.com.br.blindbeacon;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by André Borba on 22/02/2017.
 */
public class DetalhesDestinoActivity extends Activity {

    private Destino destino;
    private String predioSelecionado;
    Context ctx;
    TextView tvnomeDestino, tvnomePredio, tvcategoriaDestino, tvdescricaoDestino, tvdistanciaDestino;
    public TextToSpeech tts1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detalhes_destino);

        ctx = this;

        Intent i = getIntent();
        predioSelecionado = i.getStringExtra("PredioSelecionado");
        String serializedDestino = i.getStringExtra("DestinoSelecionado");

        Gson myGson = new Gson();
        destino = myGson.fromJson(serializedDestino, Destino.class);

        //Atribuição dos Text View
        tvnomeDestino = (TextView) this.findViewById(R.id.tvNomeDestino);
        tvnomePredio = (TextView) this.findViewById(R.id.tvNomePredio);
        tvcategoriaDestino = (TextView) this.findViewById(R.id.tvCategoriaDestino);
        tvdescricaoDestino = (TextView) this.findViewById(R.id.tvDescricaoDestino);
        tvdistanciaDestino = (TextView) this.findViewById(R.id.tvDistanciaDestino);

        //Set Text
        tvnomeDestino.setText(destino.getNome());
        tvnomePredio.setText(predioSelecionado);
        tvcategoriaDestino.setText(destino.getCategoria());
        tvdescricaoDestino.setText(destino.getDescricao());
        tvdistanciaDestino.setText(String.valueOf(destino.getDistancia()) + " m");

        tts1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    tts1.setLanguage(Locale.getDefault());
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void onClickNavegarDestino(View v){
        //Intent in = new Intent(this, BeaconFinderActivity.class);
        //startActivity(in);

        tts1.speak("Vamos navegar", TextToSpeech.QUEUE_FLUSH, null);
    }

}
