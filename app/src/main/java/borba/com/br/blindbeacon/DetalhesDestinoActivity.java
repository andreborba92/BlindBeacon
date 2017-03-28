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

import borba.com.br.blindbeacon.enums.CategoriaEnum;
import borba.com.br.blindbeacon.models.DestinoModel;
import borba.com.br.blindbeacon.models.PredioModel;

/**
 * Created by André Borba on 22/02/2017.
 */
public class DetalhesDestinoActivity extends Activity {

    private DestinoModel destino;
    private PredioModel predio;
    Context ctx;
    TextView tvnomeDestino, tvnomePredio, tvcategoriaDestino, tvdescricaoDestino, tvdistanciaDestino;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detalhes_destino);

        ctx = this;

        Intent i = getIntent();
        String serializedDestino = i.getStringExtra("DestinoSelecionado");
        String serializedPredio = i.getStringExtra("PredioSelecionado");

        Gson myGson = new Gson();
        destino = myGson.fromJson(serializedDestino, DestinoModel.class);
        predio = myGson.fromJson(serializedPredio, PredioModel.class);

        //Atribuição dos Text View
        tvnomeDestino = (TextView) this.findViewById(R.id.tvNomeDestino);
        tvnomePredio = (TextView) this.findViewById(R.id.tvNomePredio);
        tvcategoriaDestino = (TextView) this.findViewById(R.id.tvCategoriaDestino);
        //tvdescricaoDestino = (TextView) this.findViewById(R.id.tvDescricaoDestino);
        tvdistanciaDestino = (TextView) this.findViewById(R.id.tvDistanciaDestino);

        //Set Text
        tvnomeDestino.setText(destino.getNome());
        tvnomePredio.setText(predio.getNome());
        tvcategoriaDestino.setText(CategoriaEnum.getCategoriaById(destino.getIdCategoria()).toString());
        //tvdescricaoDestino.setText(destino.getDescricao());
        tvdistanciaDestino.setText(String.valueOf(destino.getDistanciaAproximada()) + " m");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void onClickNavegarDestino(View v){
        //Intent in = new Intent(this, BeaconFinderActivity.class);
        //startActivity(in);

        TTSManager.Speak("Este é o botão: navegar. Pressione para começar a navegação.");
    }

}
