package borba.com.br.blindbeacon;

import android.content.Context;
import android.speech.tts.TextToSpeech;

import java.util.Locale;

/**
 * Created by andre on 27/03/2017.
 */

public class TTSManager {
    private static TextToSpeech _tts;

    public static void Initialize(Context ctx){
        _tts = new TextToSpeech(ctx, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    _tts.setLanguage(Locale.getDefault());
                }
            }
        });
    }

    public static TextToSpeech getTTS(Context ctx){
        if(_tts != null)
            return _tts;

        _tts = new TextToSpeech(ctx, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    _tts.setLanguage(Locale.getDefault());
                }
            }
        });

        return _tts;
    }

    public static void Speak(String text){
        _tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

    public static void Pause(){
        if(_tts !=null){
            _tts.stop();
            //_tts.shutdown();
        }
    }
}
