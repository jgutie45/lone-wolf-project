package com.example.lonewolf;

/*
    Account Recovery activity. When the user forgets their password, an email will be sent to
    the email they entered fo change it to one they do remember
 */

import android.os.Bundle;
import android.util.Patterns;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;

public class AcctRecovery extends AppCompatActivity {

    private EditText e;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgotpassword);
        e = findViewById(R.id.email_recover);

        e.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN
                    && keyCode == KeyEvent.KEYCODE_ENTER) {
                sendPasswordReset();
            }
            return false;
        });

        Button sendResetEmail = findViewById(R.id.button);

        sendResetEmail.setOnClickListener(v -> sendPasswordReset());
    }

    //function to tell if the password reset email was sent correctly or not, and to send it
    private void sendPasswordReset() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String emailAddy = e.getText().toString();

        //if the address is a valid address,
        if (validateEmailAddress()) {
            auth.sendPasswordResetEmail(emailAddy)
                    .addOnCompleteListener((OnCompleteListener<Void>) task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(AcctRecovery.this,
                                    "Email Sent Successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(AcctRecovery.this,
                                    "Faield to Send Email", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(this, "Invalid Email Address", Toast.LENGTH_SHORT).show();
        }
    }

    // function to test if email is valid or not
    private boolean validateEmailAddress()
    {
        String emailInput = e.getText().toString();
        // CASE 1:  If email is empty
        if(emailInput.isEmpty()){
            e.setError("Field is empty");
            return false;
        }
        // CASE 2: If email does not match a proper email
        else if(!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()){
            e.setError("Invalid Email Address");
            //Toast.makeText(this, "Invalid Email Address", Toast.LENGTH_SHORT).show();
            return false;
        }
        // CASE 3: If email is a proper one and is not empty
        else if(!emailInput.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            Toast.makeText(this, "Email Address Validated Successfully", Toast.LENGTH_SHORT).show();
            return true;
        }
        else
            return false;
    }


}
