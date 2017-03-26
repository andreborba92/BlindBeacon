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
    public TextToSpeech tts1;
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

        //ToDo: PredioAdapter
        ArrayList<String> nomesPredio = new ArrayList<String>();

        for (int i = 0; i < listPredios.size(); i++){
            nomesPredio.add(listPredios.get(i).getNome());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, nomesPredio);


        // Assign adapter to ListView
        lvPredios.setAdapter(adapter);

        //Preparação do TTS
        tts1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    tts1.setLanguage(Locale.getDefault());
                }
            }
        });

        // ListView Item Click Listener
        lvPredios.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition = position;

                // ListView Clicked item value
                String  itemValue = (String) lvPredios.getItemAtPosition(position);

                tts1.speak("Você clicou em: " + itemValue, TextToSpeech.QUEUE_FLUSH, null);
            }

        });

        lvPredios.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {

                // ListView Clicked item index
                int itemPosition = position;

                // ListView Clicked item value
                String  itemValue = (String) lvPredios.getItemAtPosition(position);

                Intent in = new Intent(ctx, SelecionarDestinoActivity.class);
                in.putExtra("predio_selecionado", itemValue);
                startActivity(in);

                return true;
            }
        });

    }
}
