package com.example.lonewolf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    // Declare Spannable String can underline to click link to register
    Button loginButton;
    TextView t1;
    SpannableString ss;
    EditText emailEdit;
    EditText passwordEdit;

    private boolean emailRes;
    private boolean passRes;
    private FirebaseAuth auth;

    // Pattern To Check if password is valid
   private static final Pattern PASSWORD_PATTERN=
            Pattern.compile("^" +
                    //"(?=.*[0-9])" +  // at least 1 digit
                    //"(?=.*[a-z])" +  // at least 1 lower
                    // "(?=.*[A-Z])" +
                    "(?=.*[a-zA-Z])" +
                    "(?=\\S+$)" +
                    ".{4,}" +
                    "$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton  = findViewById(R.id.button2);

        t1 = findViewById(R.id.new_account);
        emailEdit = findViewById(R.id.emailEdit);
        passwordEdit = findViewById(R.id.passwordEdit);
        auth = FirebaseAuth.getInstance();

        //when clicked, the user will be redirected to a new activity to enter their email and
        //reset their password if they forgot it.
        TextView forgotPsswrd = findViewById(R.id.forget_pw);
        forgotPsswrd.setOnClickListener(v -> {
            Intent passwordIntent = new Intent(LoginActivity.this, AcctRecovery.class);
            LoginActivity.this.startActivity(passwordIntent);
        });

        // This highlights specific text to indicate which text should be click on
        ss = new SpannableString(getString(R.string.register_link));

        // Click on "Create Account here!" to go to Register page
        ClickableSpan clickableSpan1 = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                Toast.makeText(LoginActivity.this, "Register", Toast.LENGTH_SHORT).show();
                Log.i("LoginActivity", "switching");
                Intent intent2 = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(intent2);
            }
        };
        // "New user? Create Account here!"
        // Can set the bounds based on the lines position in the string
        ss.setSpan(clickableSpan1, 10, 29, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Update underline to text
        t1.setText(ss);
        t1.setMovementMethod(LinkMovementMethod.getInstance());


        loginButton.setOnClickListener(view -> {

            emailRes = validateEmailAddress(emailEdit);
            passRes = validatePassword(passwordEdit);
            if(emailRes && passRes) {
                signIntoApp(emailEdit.getText().toString(),
                        passwordEdit.getText().toString());
            }
        });
    }

    // With all parts verified and authenticated, we can now go to the main even
    private void goToMain() {
        // do something when the button is clicked
        Log.i("LoginActivity", "switching");
        Toast.makeText(this, "Now logging in...", Toast.LENGTH_SHORT).show();
        Intent intent1 = new Intent(LoginActivity.this, MainActivity.class);
        LoginActivity.this.startActivity(intent1);
    }

    // function to test if email is valid or not
    private boolean validateEmailAddress(EditText emailEdit)
    {
        String emailInput = emailEdit.getText().toString();
        // CASE 1:  If email is empty
        if(emailInput.isEmpty()){
            emailEdit.setError("Field is empty");
            return false;
        }
        // CASE 2: If email does not match a proper email
        else if(!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()){
            emailEdit.setError("Invalid Email Address");
            Toast.makeText(this, "Invalid Email Address", Toast.LENGTH_SHORT).show();
            return false;
        }
        // CASE 3: If email is a proper one and is not empty
        else if(!emailInput.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            //Toast.makeText(this, "Email Address Validated Successfully", Toast.LENGTH_SHORT).show();
            return true;
        }
        else
            return false;
    }

    // function to test if email is valid or not
    private boolean validatePassword(EditText passwordEdit) {
        String passwordInput = passwordEdit.getText().toString();
        Log.i("LoginActivity", "Password is " + passwordInput);
        // CASE 1: If password is empty
        if(passwordInput.isEmpty()) {
            passwordEdit.setError("Field is empty");
            return false;
        }
        // CASE 2: The password is incorrect for the specified email address
        else if(!PASSWORD_PATTERN.matcher(passwordInput).matches()){
            passwordEdit.setError("Password is incorrect");
            //Toast.makeText(this, "Invalid Email Address", Toast.LENGTH_SHORT).show();
            return false;
        }
        // CASE 3: The password is correct for the specified email address
        else{
            passwordEdit.setError(null);
            return true;
        }
    }

    // authenticate user if they have an account already.
    private void signIntoApp(String email, String password) {

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d("SIGNIN", "Successful sign in");
                        emailEdit.setText("");
                        passwordEdit.setText("");
                        goToMain();
                    } else {
                        Log.w("SIGNIN_FAIL", "Failed to Sign in User");
                        Toast.makeText(LoginActivity.this, "Authentication failed",
                                Toast.LENGTH_SHORT).show();
                    }
                });

    }
}