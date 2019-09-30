package com.mao.sortletter;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.mao.sortletterlib.SortLetterView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView centerText = findViewById(R.id.centerText);
        SortLetterView letterView = findViewById(R.id.sort_view);
//        letterView.setLetters("sfsdfsfsdfsd");
        letterView.setLetterText(centerText);
        letterView.setOnLetterChangedListener(new SortLetterView.OnLetterChangedListener() {
            @Override
            public void onChanged(String letter, int position) {
                Log.e("tests", letter + "==" + position);
            }
        });
    }
}
