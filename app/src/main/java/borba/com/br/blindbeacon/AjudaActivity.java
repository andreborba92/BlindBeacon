package borba.com.br.blindbeacon;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

/**
 * Created by André Borba on 20/09/2016.
 */
public class AjudaActivity extends Activity {

    public Button btnTTS, buttonTeste;
    public TextToSpeech tts1;
    public String textoParaTTS;
    Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ajuda);

        ctx = this;

        this.btnTTS = (Button) findViewById(R.id.btnTTS);
        this.buttonTeste = (Button) findViewById(R.id.btnTeste);
        this.textoParaTTS = "Olá, bem vindo ao aplicativo. Para ouvir uma ação, clique uma vez. Para executar uma ação, clique duas vezes.";

        tts1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    tts1.setLanguage(Locale.getDefault());
                }
            }
        });

        SetClickListeners();
    }

    public void SetClickListeners(){

        buttonTeste.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v)
            {
                Toast.makeText(ctx, "One click", Toast.LENGTH_SHORT).show();
            }
        });

        buttonTeste.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                Toast.makeText(ctx, "Long Press", Toast.LENGTH_SHORT).show();

                return true;
            }
        });

    };

    public void onClickBtnTTS(View v){
        String toSpeak = textoParaTTS;
        Toast.makeText(getApplicationContext(), toSpeak, Toast.LENGTH_SHORT).show();
        tts1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
    }

    public void onPause(){
        if(tts1 !=null){
            tts1.stop();
            tts1.shutdown();
        }
        super.onPause();
    }
}
