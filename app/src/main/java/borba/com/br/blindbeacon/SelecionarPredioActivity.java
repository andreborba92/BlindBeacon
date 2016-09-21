package borba.com.br.blindbeacon;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


import java.util.ArrayList;

/**
 * Created by André Borba on 20/09/2016.
 */
public class SelecionarPredioActivity extends Activity {

    private ListView lvPredios;
    ArrayList<String> listPredios;
    Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.selecionar_predio);

        ctx = this;
        lvPredios = (ListView)findViewById(R.id.lvPredios);

        String[] values = {"Predio Centro A","Predio Centro B","Predio Centro C","Predio Centro D"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values);


        // Assign adapter to ListView
        lvPredios.setAdapter(adapter);


        // ListView Item Click Listener
        lvPredios.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition = position;

                // ListView Clicked item value
                String  itemValue = (String) lvPredios.getItemAtPosition(position);

                // Show Alert
                Toast.makeText(getApplicationContext(),
                        "Position :" + itemPosition + "  ListItem : " + itemValue, Toast.LENGTH_LONG)
                        .show();

            }

        });

    }
}
