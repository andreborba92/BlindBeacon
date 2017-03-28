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
    //public TextToSpeech tts1;
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


        //Criação de um Handlar para enviar o TTS após X segundos da inicialização
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                // Actions to do after 500 miliseconds
                TTSManager.Speak(textoParaTTS);
            }
        }, 500);


        SetClickListeners();
    }

    public void SetClickListeners(){

        btnAjudaFuncionaldades.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v)
            {
                TTSManager.Speak("Este botão é sobre ajuda de funcionalidades. Pressione para ouvir");
            }
        });

        btnAjudaFuncionaldades.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String textoAjuda = "Este aplicativo possui as seguintes funcionalidades:";
                TTSManager.Speak(textoAjuda);
                return true;
            }
        });

        btnAjudaNavegacao.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v)
            {
                TTSManager.Speak("Este botão é sobre ajuda de navegação. Pressione para ouvir");
            }
        });

        btnAjudaNavegacao.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String textoAjuda = "A navegação funciona da seguinte forma:";
                TTSManager.Speak(textoAjuda);
                return true;
            }
        });

        btnAjudaObstaculos.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v)
            {
                TTSManager.Speak("Este botão é sobre ajuda de obstáculos. Pressione para ouvir");
            }
        });

        btnAjudaObstaculos.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String textoAjuda = "Alguns obstáculos nos setores disponíveis são conhecidos, como portas e escadas. " +
                        "O aplicativo tentará te avisar da proximidade de um obstáculo sempre que possível, mas mesmo assim, "+
                        "você deve caminhar com cuidado.";
                TTSManager.Speak(textoAjuda);
                return true;
            }
        });

    };

    public void onClickBtnTTS(View v){
        String toSpeak = textoParaTTS;
        TTSManager.Speak(toSpeak);
    }

    public void onPause(){
        TTSManager.Pause();
        super.onPause();
    }
}
