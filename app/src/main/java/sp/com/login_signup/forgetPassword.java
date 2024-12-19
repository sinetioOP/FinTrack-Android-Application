package sp.com.login_signup;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.CountDownTimer;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class forgetPassword extends AppCompatActivity {
    TextView BackBtn;
    Button ConfirmBtn;
    Intent intent,to_login;
    TextInputEditText Email;
    FirebaseAuth mAuth;
    DatabaseReference reference,ForgotPass;
    TextView ForgotpassEmailError,CountDown;

    FirebaseDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_password);

        CountDown= findViewById(R.id.countdown);
        Email = findViewById(R.id.email);
        BackBtn = findViewById(R.id.backBtn);
        ConfirmBtn = findViewById(R.id.confirmBtn);
        ForgotpassEmailError = findViewById(R.id.forgotpassEmailError);
        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance("https://fintrack-cca93-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("User");

        ForgotPass = FirebaseDatabase.getInstance("https://fintrack-cca93-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("ForgotPass");
        ForgotPass.child("email").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()) {
                        DataSnapshot dataSnapshot = task.getResult();
                        String email = String.valueOf(dataSnapshot.getValue());
                        Email.setText(email);

                    }
                }else {

                }

            }
        });

        ConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email_input = Email.getText().toString().toLowerCase().trim();
                db = FirebaseDatabase.getInstance("https://fintrack-cca93-default-rtdb.asia-southeast1.firebasedatabase.app");
                reference = db.getReference("ForgotPass");
                reference.child("email").setValue(email_input).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
                 if ((email_input.isEmpty())||(!Patterns.EMAIL_ADDRESS.matcher(email_input).matches()) ){
                    Email.setError("Invalid Email Address.");
                }  else {
                    checkEmailExists(email_input);
                }
            }
        });

        BackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                to_login = new Intent(forgetPassword.this,LoginActivity.class);
                startActivity(to_login);
                finish();
            }
        });
    }

    public void SendPassResetLink(String email_input){

        mAuth.sendPasswordResetEmail(email_input).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(forgetPassword.this, "Email Sent!", Toast.LENGTH_SHORT).show();
                intent = new Intent(forgetPassword.this, EmailSent.class);
                startActivity(intent);
                finish();
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                ForgotpassEmailError.setText("Failed to send Email. Try again later.");
            }
        });

    }
    public void checkEmailExists(final String Checkemail) {
        mAuth.createUserWithEmailAndPassword(Checkemail, "dummyPassword")
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Email.setError("The email is not registered.");
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            user.delete()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d(TAG, "User account deleted.");
                                            }
                                        }
                                    });
                        } else {
                            // If sign in fails, display a message to the user.
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                // This email is already registered
                                SendPassResetLink(Checkemail);
                            } else {
                                // Handle other errors
                                Log.d(TAG, "Error: " + task.getException().getMessage());
                            }
                        }
                    }
                });

    }


    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        return super.onNavigateUp();
    }
    public void onBackPressed(){
        super.onBackPressed();
        to_login = new Intent(forgetPassword.this,LoginActivity.class);
        startActivity(to_login);
        finish();
    }
}