package com.yvo.genrateurdattestationdplacement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

public class person extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        SharedPreferences sharedPref = getSharedPreferences(ApplicationConstants.PREFERENCES, Context.MODE_PRIVATE);

        EditText e = findViewById(R.id.ePrenom);
        e.setText(sharedPref.getString("Prenom", ""));

        e = findViewById(R.id.eNom);
        e.setText(sharedPref.getString("Nom", ""));

        e = findViewById(R.id.eDateDeN);
        e.setText(sharedPref.getString("Date", ""));

        e = findViewById(R.id.eLieuDeN);
        e.setText(sharedPref.getString("Lieu", ""));

        e = findViewById(R.id.eAdresse);
        e.setText(sharedPref.getString("Adresse", ""));

        e = findViewById(R.id.eVille);
        e.setText(sharedPref.getString("Ville", ""));

        e = findViewById(R.id.eCP);
        e.setText(sharedPref.getString("CP", ""));
        e.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    onSaveClick(v);
                }
                return false;
            }
        });
    }


    public void onSaveClick(View view)
    {
        SharedPreferences sharedPref = getSharedPreferences(ApplicationConstants.PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        EditText e = findViewById(R.id.ePrenom);
        editor.putString("Prenom", e.getText().toString());

        e = findViewById(R.id.eNom);
        editor.putString("Nom", e.getText().toString());

        e = findViewById(R.id.eDateDeN);
        editor.putString("Date", e.getText().toString());

        e = findViewById(R.id.eLieuDeN);
        editor.putString("Lieu", e.getText().toString());

        e = findViewById(R.id.eAdresse);
        editor.putString("Adresse", e.getText().toString());

        e = findViewById(R.id.eVille);
        editor.putString("Ville", e.getText().toString());

        e = findViewById(R.id.eCP);
        editor.putString("CP", e.getText().toString());

        editor.apply();

        finish();
    }
}