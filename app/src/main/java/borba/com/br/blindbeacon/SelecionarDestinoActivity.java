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

/**
 * Created by André Borba on 21/02/2017.
 */
public class SelecionarDestinoActivity extends Activity {

    private ListView lvDestinos;
    private String predioSelecionado;
    ArrayList<Destino> listaDestinos;
    public TextToSpeech tts1;
    Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_destinos);

        ctx = this;
        lvDestinos = (ListView)findViewById(R.id.lvDestinos);
        listaDestinos = new ArrayList<>();

        Intent intent = getIntent();
        predioSelecionado = intent.getStringExtra("predio_selecionado");

        //Montagem da Lista
        listaDestinos.add(new Destino("Sala 1","Sala de Matematica","Sala de Aula",1));
        listaDestinos.add(new Destino("Sala 2","Sala de Física","Sala de Aula",5));
        listaDestinos.add(new Destino("Banheiro Masculino","Banheiro","Banheiro",120));
        listaDestinos.add(new Destino("Auditório","Eventos","Auditório",215));
        listaDestinos.add(new Destino("Xerox","Aberto 24h","Utilidade",25));
        listaDestinos.add(new Destino("Sala 4","Sala de História","Sala de Aula",42));

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
                Destino itemValue = (Destino) lvDestinos.getItemAtPosition(position);

                String textoTTS = "Você selecionou o destino: " + itemValue.getNome() + ". Este destino é da categoria: " +
                        itemValue.getCategoria() + ". Sua descrição é: " + itemValue.getDescricao() +
                        ". A distância até o destino é de: " + itemValue.getDistancia() + " metros";

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
                Destino itemValue = (Destino) lvDestinos.getItemAtPosition(position);

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
