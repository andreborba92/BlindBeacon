package borba.com.br.blindbeacon;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Locale;

import borba.com.br.blindbeacon.datamodels.PredioDataModel;
import borba.com.br.blindbeacon.models.PredioModel;

/**
 * Created by André Borba on 20/09/2016.
 */
public class SelecionarPredioActivity extends Activity {

    private ListView lvPredios;
    ArrayList<PredioModel> listPredios;
    Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.selecionar_predio);

        ctx = this;
        lvPredios = (ListView)findViewById(R.id.lvPredios);
        lvPredios.setLongClickable(true);

        PredioDataModel dataModel = new PredioDataModel(this);
        listPredios = dataModel.getAll();

        lvPredios.setAdapter(new PredioAdapter(ctx, R.layout.list_item_predio, listPredios));

        // ListView Item Click Listener
        lvPredios.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition = position;

                // ListView Clicked item value
                PredioModel itemValue = (PredioModel) lvPredios.getItemAtPosition(position);

                TTSManager.Speak("Você clicou em: " + itemValue.getNome());
            }

        });

        lvPredios.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {

                // ListView Clicked item index
                int itemPosition = position;

                // ListView Clicked item value
                PredioModel itemValue = (PredioModel) lvPredios.getItemAtPosition(position);

                Gson myGson = new Gson();

                Intent in = new Intent(ctx, SelecionarDestinoActivity.class);
                in.putExtra("PredioSelecionado", myGson.toJson(itemValue));

                startActivity(in);

                return true;
            }
        });

    }
}
