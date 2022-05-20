package com.example.lonewolf;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth auth; //the authenticator for accounts
    private EditText emailAcct, passwrd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth = FirebaseAuth.getInstance();

        emailAcct = findViewById(R.id.email_edit);
        passwrd = findViewById(R.id.password_edit);

        Button button = findViewById(R.id.register_button);
        button.setOnClickListener(registerButtonListener);
    }

    public View.OnClickListener registerButtonListener = v -> makeAcct(emailAcct.getText().toString(),
            passwrd.getText().toString());

    public void signUp() {
        // do something when the button is clicked
        Log.i("RegisterActivity", "switching");
        Toast.makeText(this, "Logging in...", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        RegisterActivity.this.startActivity(intent);
    }

    private void makeAcct(String email, String password) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d("SIGN UP", "Successfully added user with email");
                        signUp();
                    }
                });
        {}
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser curUser = auth.getCurrentUser();
        if (curUser != null) {
            reload();
        }
    }


    private void reload() {
        Objects.requireNonNull(auth.getCurrentUser()).reload().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Intent intent = new Intent(RegisterActivity.this,
                        MainActivity.class);
                RegisterActivity.this.startActivity(intent);
            } else {
                Toast.makeText(RegisterActivity.this,
                        "Faield to reload", Toast.LENGTH_SHORT).show();
            }
        });
    }
}