package com.sjy.inputdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    TextInputView  textInputView;
    EditText et_input;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textInputView = findViewById(R.id.textInputView);
        et_input = findViewById(R.id.et_input);
        et_input = findViewById(R.id.et_input);
        //
        textInputView.setOnInputFinishListener(new TextInputView.OnInputFinishListener() {
            @Override
            public void onComplete(String content) {

            }
        });
        et_input.setText("123456");//同步一下
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
