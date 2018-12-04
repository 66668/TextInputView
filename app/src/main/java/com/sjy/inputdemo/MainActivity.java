package com.sjy.inputdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.sjy.inputdemo.embed.TextInputView;
import com.sjy.inputdemo.system_wdget.TextInputView2;

public class MainActivity extends AppCompatActivity {
    TextInputView textInputView;
    TextInputView2 textInputView2;
    EditText et_input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textInputView = findViewById(R.id.textInputView1);
        textInputView2 = findViewById(R.id.textInputView2);
        et_input = findViewById(R.id.et_input);
        //
        textInputView.setOnInputFinishListener(new TextInputView.OnInputFinishListener() {
            @Override
            public void onComplete(String content) {

            }
        });
        textInputView2.setOnInputFinishListener(new TextInputView2.OnInputFinishListener() {
            @Override
            public void onComplete(String content) {

            }
        });
        et_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                textInputView.setText(s.toString());
            }
        });
    }
}
