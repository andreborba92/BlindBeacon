package borba.com.br.blindbeacon;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }



    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();
    }

    public void onClickBeaconFinder(View v){
        Intent in = new Intent(this, BeaconFinderActivity.class);
        startActivity(in);
        //Toast.makeText(this, "Teste do toast", Toast.LENGTH_SHORT).show();
    }

    public void onClickSelecionarPredio(View v){
        Intent in = new Intent(this, SelecionarPredioActivity.class);
        startActivity(in);
    }

    public void onClickBtnAjuda(View v){
        Intent in = new Intent(this, AjudaActivity.class);
        startActivity(in);
    }
}
