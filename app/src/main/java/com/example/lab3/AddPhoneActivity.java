package com.example.lab3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddPhoneActivity extends AppCompatActivity {
    public static final String PRODUCER_KEY = "producer";
    public static final String MODEL_KEY = "model";
    public static final String VERSION_KEY = "version";
    public static final String WEBSITE_KEY = "website";

    private EditText mEditTextAddPhoneProducer;
    private EditText mEditTextAddPhoneModel;
    private EditText mEditTextAddPhoneVersion;
    private EditText mEditTextAddPhoneWebsite;
    private Button mButtonWebsite;
    private Button mButtonCancel;
    private Button mButtonSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_phone);

        connectLayoutElementsWithFields();
        addListenersToLayoutElements();
    }

    private void connectLayoutElementsWithFields() {
        mEditTextAddPhoneProducer = findViewById(R.id.editTextAddPhoneProducer);
        mEditTextAddPhoneModel = findViewById(R.id.editTextAddPhoneModel);
        mEditTextAddPhoneVersion = findViewById(R.id.editTextAddPhoneVersion);
        mEditTextAddPhoneWebsite = findViewById(R.id.editTextAddPhoneWebsite);
        mButtonWebsite = findViewById(R.id.buttonWebsite);
        mButtonCancel = findViewById(R.id.buttonCancel);
        mButtonSave = findViewById(R.id.buttonSave);
    }

    private void addListenersToLayoutElements() {
        addNotNullListenerToTextField(mEditTextAddPhoneProducer);
        addNotNullListenerToTextField(mEditTextAddPhoneModel);
        mButtonSave.setOnClickListener(this::prepareForAddingNewPhone);
        mButtonCancel.setOnClickListener(this::abandonAddingNewPhone);
    }

    private void addNotNullListenerToTextField(EditText textField) {
        textField.setOnFocusChangeListener((view, hasFocus) -> {
            if (!hasFocus && !isTextFieldNotEmpty(textField)) {
                String errorText = getResources().getString(R.string.textIsEmptyAnnouncement);
                textField.setError(errorText);
                Toast.makeText(AddPhoneActivity.this, errorText, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isTextFieldNotEmpty(EditText textField) {
        return !TextUtils.isEmpty(textField.getText().toString());
    }

    private void prepareForAddingNewPhone(View view) {
        boolean phoneCanBeSaved = isTextFieldNotEmpty(mEditTextAddPhoneProducer)
                && isTextFieldNotEmpty(mEditTextAddPhoneModel);

        if (!phoneCanBeSaved) {
            Toast.makeText(AddPhoneActivity.this, getResources().getText(R.string.cannotSavePhoneMessage), Toast.LENGTH_LONG).show();
            return;
        }

        Intent intentWithPhoneData = preparePhoneData();
        setResult(RESULT_OK, intentWithPhoneData);
        finish();
    }

    private void abandonAddingNewPhone(View view) {
        Intent intent = new Intent(AddPhoneActivity.this, MainActivity.class);
        setResult(RESULT_CANCELED, intent);
        finish();
    }

    @NonNull
    private Intent preparePhoneData() {
        Intent intent = new Intent(AddPhoneActivity.this, MainActivity.class);
        intent.putExtra(PRODUCER_KEY, mEditTextAddPhoneProducer.getText().toString());
        intent.putExtra(MODEL_KEY, mEditTextAddPhoneModel.getText().toString());
        intent.putExtra(VERSION_KEY, mEditTextAddPhoneVersion.getText().toString());
        intent.putExtra(WEBSITE_KEY, mEditTextAddPhoneWebsite.getText().toString());
        return intent;
    }
}