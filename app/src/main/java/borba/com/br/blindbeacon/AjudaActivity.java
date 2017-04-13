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

        this.textoParaTTS = "Olá, esta é a tela de ajuda. "+
                "Abaixo existem algumas opções de ajuda: sobre funcionalidades, sobre navegação, sobre obstáculos e ouvir novamente." +
                " Clique nos botões abaixo para ouvir a ajuda de cada tópico.";


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

        btnTTS.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v)
            {
                TTSManager.Speak("Este botão é para ouvir novamente. Pressione para ouvir");
            }
        });

        btnTTS.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                TTSManager.Speak(textoParaTTS);
                return true;
            }
        });

        btnAjudaFuncionaldades.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v)
            {
                TTSManager.Speak("Este botão é sobre ajuda de funcionalidades. Pressione para ouvir");
            }
        });

        btnAjudaFuncionaldades.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String textoAjuda = "Após a tela inicial, você irá entrar na tela de seleção de Setores." +
                        " Nesta tela serão listados todos os setores disponíveis, que foram mapeados pelo aplicativo." +
                        " Ao selecionar um setor, você verá a listagem dos destinos mapeados, que são salas, banheiros, secretarias e outros." +
                        " Ao selecionar um destino, você irá para uma tela que irá descrever os seus detalhes, e um botão com a opção de navegar.";
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
                String textoAjuda = "Ao clicar no botão navegação, o aplicativo irá verificar onde você se encontra, e te guiar para o seu destino." +
                        " Será traçada uma rota, e conforme você vai se deslocando, o aplicativo irá avisar qual o próximo ponto conhecido da rota, " +
                        " bem como a distância aproximada até lá. Caso o aplicativo avise que não conseguiu localizar nenhum ponto conhecido, " +
                        " caminhe um pouco mais, pois terá algum ponto conhecido em algum lugar próximo.";
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
                String textoAjuda = "Alguns obstáculos nos setores disponíveis são conhecidos, como portas, escadas e degraus. " +
                        "O aplicativo tentará te avisar da proximidade de um obstáculo sempre que possível, mas mesmo assim, "+
                        "você deve caminhar devagar, e com cuidado.";
                TTSManager.Speak(textoAjuda);
                return true;
            }
        });

    };

    public void onPause(){
        TTSManager.Pause();
        super.onPause();
    }
}
