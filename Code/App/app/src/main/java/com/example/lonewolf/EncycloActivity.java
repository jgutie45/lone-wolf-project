package com.example.lonewolf;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EncycloActivity extends AppCompatActivity {
    EditText t;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.encyclo_search);

        Button b = findViewById(R.id.button3);
        t = findViewById(R.id.daSearch);
        b.setOnClickListener(searchForThis);
    }

    public View.OnClickListener searchForThis = v -> searchDis();

    private void searchDis() {
        Log.i("starting search", "searching for input text now");
        String searchFor =  "https://www.wikipedia.org/wiki/" + t.getText().toString();
        Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(searchFor));
        startActivity(intent);
    }
}
