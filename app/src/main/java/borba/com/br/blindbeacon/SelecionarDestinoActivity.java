package borba.com.br.blindbeacon;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by André Borba on 21/02/2017.
 */
public class SelecionarDestinoActivity extends Activity {

    private ListView lvDestinos;
    private String predioSelecionado;
    ArrayList<Destino> listaDestinos;
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

        // ListView Item Click Listener
        lvDestinos.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition = position;

                // ListView Clicked item value
                Destino itemValue = (Destino) lvDestinos.getItemAtPosition(position);

                //ToDo Desconmentar
                Gson myGson = new Gson();

                Intent in = new Intent(ctx, DetalhesDestinoActivity.class);
                in.putExtra("PredioSelecionado", predioSelecionado);
                in.putExtra("DestinoSelecionado", myGson.toJson(itemValue));

                //Toast.makeText(ctx, myGson.toJson(itemValue), Toast.LENGTH_SHORT).show();

                startActivity(in);
            }

        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
