package com.example.lab3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class UpdatePhoneActivity extends AppCompatActivity {

    private EditText mEditTextProducer;
    private EditText mEditTextModel;
    private EditText mEditTextVersion;
    private EditText mEditTextWebsite;
    private Button mButtonWebsite;
    private Button mButtonCancel;
    private Button mButtonUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_phone);

        connectLayoutElementsWithFields();
    }

    private void connectLayoutElementsWithFields() {
        mEditTextProducer = findViewById(R.id.editTextUpdatePhoneProducer);
        mEditTextModel = findViewById(R.id.editTextUpdatePhoneModel);
        mEditTextVersion = findViewById(R.id.editTextUpdatePhoneVersion);
        mEditTextWebsite = findViewById(R.id.editTextUpdatePhoneWebsite);
        mButtonWebsite = findViewById(R.id.buttonUpdatePhoneWebsite);
        mButtonCancel = findViewById(R.id.buttonUpdatePhoneCancel);
        mButtonUpdate = findViewById(R.id.buttonUpdatePhoneUpdate);
    }
}