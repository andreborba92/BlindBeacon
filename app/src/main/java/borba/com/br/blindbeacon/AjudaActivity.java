package borba.com.br.blindbeacon;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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

    public Button btnTTS, btnAjudaFuncionaldades, btnAjudaNavegacao, btnAjudaObstaculos;
    public TextToSpeech tts1;
    public String textoParaTTS;
    Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ajuda);

        ctx = this;

        this.btnTTS = (Button) findViewById(R.id.btnTTS);
        this.btnAjudaFuncionaldades = (Button) findViewById(R.id.btnAjudaFuncionaldades);
        this.btnAjudaNavegacao = (Button) findViewById(R.id.btnAjudaNavegacao);
        this.btnAjudaObstaculos = (Button) findViewById(R.id.btnAjudaObstaculos);

        this.textoParaTTS = "Olá, bem vindo ao aplicativo. "+
         "Ao clicar uma vez em algum botão, você irá ouvir a sua descrição. "+
         "Para executar o botão, você deve segurar o botão em um clique longo. "+
         "Abaixo existem algumas opções de ajuda: sobre funcionalidades, sobre navegação e sobre obstáculos";

        tts1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    tts1.setLanguage(Locale.getDefault());
                }
            }
        });

        //Criação de um Handlar para enviar o TTS após X segundos da inicialização
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                // Actions to do after 4 seconds
                tts1.speak(textoParaTTS, TextToSpeech.QUEUE_FLUSH, null);
            }
        }, 4000);


        SetClickListeners();
    }

    public void SetClickListeners(){

        btnAjudaFuncionaldades.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v)
            {
                tts1.speak("Este botão é sobre ajuda de funcionalidades. Pressione para ouvir", TextToSpeech.QUEUE_FLUSH, null);
            }
        });

        btnAjudaFuncionaldades.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String textoAjuda = "Este aplicativo possui as seguintes funcionalidades:";
                tts1.speak(textoAjuda, TextToSpeech.QUEUE_FLUSH, null);
                return true;
            }
        });

        btnAjudaNavegacao.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v)
            {
                tts1.speak("Este botão é sobre ajuda de navegação. Pressione para ouvir", TextToSpeech.QUEUE_FLUSH, null);
            }
        });

        btnAjudaNavegacao.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String textoAjuda = "A navegação funciona da seguinte forma:";
                tts1.speak(textoAjuda, TextToSpeech.QUEUE_FLUSH, null);
                return true;
            }
        });

        btnAjudaObstaculos.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v)
            {
                tts1.speak("Este botão é sobre ajuda de obstáculos. Pressione para ouvir", TextToSpeech.QUEUE_FLUSH, null);
            }
        });

        btnAjudaObstaculos.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String textoAjuda = "Alguns obstáculos nos setores disponíveis são conhecidos, como portas e escadas. " +
                        "O aplicativo tentará te avisar da proximidade de um obstáculo sempre que possível, mas mesmo assim, "+
                        "você deve caminhar com cuidado.";
                tts1.speak(textoAjuda, TextToSpeech.QUEUE_FLUSH, null);
                return true;
            }
        });

    };

    public void onClickBtnTTS(View v){
        String toSpeak = textoParaTTS;
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
