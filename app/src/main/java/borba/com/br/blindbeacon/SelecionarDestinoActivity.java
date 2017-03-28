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
import borba.com.br.blindbeacon.models.PredioModel;

/**
 * Created by André Borba on 21/02/2017.
 */
public class SelecionarDestinoActivity extends Activity {

    private ListView lvDestinos;
    private PredioModel predioSelecionado;
    ArrayList<DestinoModel> listaDestinos;
    Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_destinos);

        ctx = this;
        lvDestinos = (ListView)findViewById(R.id.lvDestinos);
        listaDestinos = new ArrayList<DestinoModel>();

        Intent intent = getIntent();
        Gson myGson = new Gson();

        String serializedPredio = intent.getStringExtra("PredioSelecionado");

        predioSelecionado = myGson.fromJson(serializedPredio, PredioModel.class);

        //Montagem da Lista
        DestinoDataModel dataModel = new DestinoDataModel(this);
        listaDestinos = dataModel.getAll_ApenasDestinos(predioSelecionado.getId());

        //ToDo: Do banco de dados virão destinos da categoria obstáculo. Eles não devem ser exibidos,
        // apenas notificados quando estão próximos.
        //ToDo: No momento da rota, ter uma lista dos destinos "exibíveis" e dos para controle interno

        lvDestinos.setAdapter(new DestinoAdapter(ctx, R.layout.list_item_destino, listaDestinos));

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

                TTSManager.Speak(textoTTS);
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
                in.putExtra("PredioSelecionado", myGson.toJson(predioSelecionado));
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
