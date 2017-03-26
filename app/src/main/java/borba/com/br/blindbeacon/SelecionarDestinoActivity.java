package borba.com.br.blindbeacon;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Locale;

import borba.com.br.blindbeacon.datamodels.DestinoDataModel;
import borba.com.br.blindbeacon.enums.CategoriaEnum;
import borba.com.br.blindbeacon.models.DestinoModel;

/**
 * Created by André Borba on 21/02/2017.
 */
public class SelecionarDestinoActivity extends Activity {

    private ListView lvDestinos;
    private String predioSelecionado;
    ArrayList<DestinoModel> listaDestinos;
    public TextToSpeech tts1;
    Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_destinos);

        ctx = this;
        lvDestinos = (ListView)findViewById(R.id.lvDestinos);
        listaDestinos = new ArrayList<DestinoModel>();

        Intent intent = getIntent();
        predioSelecionado = intent.getStringExtra("predio_selecionado");

        //Montagem da Lista
        DestinoDataModel dataModel = new DestinoDataModel(this);
        listaDestinos = dataModel.getAll_ApenasDestinos(1);

        //ToDo: Do banco de dados virão destinos da categoria obstáculo. Eles não devem ser exibidos,
        // apenas notificados quando estão próximos.
        //ToDo: No momento da rota, ter uma lista dos destinos "exibíveis" e dos para controle interno

        lvDestinos.setAdapter(new DestinoAdapter(ctx, R.layout.list_item_destino, listaDestinos));

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
        lvDestinos.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition = position;

                // ListView Clicked item value
                DestinoModel itemValue = (DestinoModel) lvDestinos.getItemAtPosition(position);

                String textoTTS = "Você selecionou o destino: " + itemValue.getNome() + ". Este destino é da categoria: " +
                        CategoriaEnum.getCategoriaById(itemValue.getIdCategoria()).toString() +
                        ". A distância até o destino é de: " + itemValue.getDistanciaAproximada() + " metros";

                tts1.speak(textoTTS, TextToSpeech.QUEUE_FLUSH, null);
            }

        });

        lvDestinos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {

                // ListView Clicked item index
                int itemPosition = position;

                // ListView Clicked item value
                DestinoModel itemValue = (DestinoModel) lvDestinos.getItemAtPosition(position);

                Gson myGson = new Gson();

                Intent in = new Intent(ctx, DetalhesDestinoActivity.class);
                in.putExtra("PredioSelecionado", predioSelecionado);
                in.putExtra("DestinoSelecionado", myGson.toJson(itemValue));

                //Toast.makeText(ctx, myGson.toJson(itemValue), Toast.LENGTH_SHORT).show();

                startActivity(in);

                return true;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
