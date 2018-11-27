package org.izv.aad.ttspspaad;

import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity  implements TextToSpeech.OnInitListener {


    private static final String TAG = "TextToSpeechDemo";
    private TextToSpeech mTts;
    private Button mAgainButton, btRecord;
    private EditText et;
    private static final int CTE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    public void init(){
        // Initialize text-to-speech. This is an asynchronous operation.
        // The OnInitListener (second argument) is called after initialization completes.
        et=findViewById(R.id.editText);
        btRecord=findViewById(R.id.btGrabar);
        btRecord.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "es-ES");
                i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Habla ahora");
                i.putExtra(RecognizerIntent. EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS, 3000);
                startActivityForResult(i, CTE);
            }
        });
        mTts = new TextToSpeech(this,
                this  // TextToSpeech.OnInitListener
        );
        mAgainButton = (Button) findViewById(R.id.btSpeach);
        mAgainButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sayHello();
            }
        });
    }

   /* private static final Random RANDOM = new Random();
    private static final String[] HELLOS = {
            "Hello",
            "Salutations",
            "Greetings",
            "Howdy",
            "What's crack-a-lackin?",
            "That explains the stench!"
    };*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CTE && resultCode == RESULT_OK){
            ArrayList<String> textos = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            Log.v(TAG, textos.size() + " textos");
            if (textos.size()>0){
                et.setText(textos.get(0));
            }
        }
    }

    private void sayHello(String s) {
        mTts.speak(s,
                TextToSpeech.QUEUE_FLUSH,  // Drop all pending entries in the playback queue.
                null);
    }
    private void sayHello() {
        sayHello(et.getText().toString());
    }

    @Override
    public void onDestroy() {
        // Don't forget to shutdown!
        if (mTts != null) {
            mTts.stop();
            mTts.shutdown();
        }
        super.onDestroy();
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = mTts.setLanguage(Locale.US);
            if (result == TextToSpeech.LANG_MISSING_DATA ||
                    result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e(TAG, "Language is not available.");
            } else {
                mAgainButton.setEnabled(true);
                sayHello();
            }
        } else {
            Log.e(TAG, "Could not initialize TextToSpeech.");
        }
    }
}
