package sp.com.login_signup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EmailSent extends AppCompatActivity {
    TextView CountDown,ForgotpassEmailError,Message,BackBtn;
    FirebaseAuth mAuth;


    Intent intent,to_forgetPass;
    DatabaseReference reference;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_sent);

        Message = findViewById(R.id.message);
        CountDown = findViewById(R.id.countdown);
        mAuth = FirebaseAuth.getInstance();
        ForgotpassEmailError = findViewById(R.id.forgotpassEmailError);
        BackBtn = findViewById(R.id.backBtn);

        to_forgetPass = new Intent(EmailSent.this,forgetPassword.class);

        reference = FirebaseDatabase.getInstance("https://fintrack-cca93-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("ForgotPass");
        reference.child("email").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()) {
                        DataSnapshot dataSnapshot = task.getResult();
                        String email_to_sent = String.valueOf(dataSnapshot.getValue());
                        Message.setText("We've sent the password reset link to "+email_to_sent+" . Please check");

                    }
                }else {

                }

            }
        });
        BackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(to_forgetPass);
                finish();
            }
        });

        new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                CountDown.setText("Didn't get it? Send again in " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                SpannableString ss = new SpannableString("Send again");
                ClickableSpan login = new ClickableSpan() {
                    @Override
                    public void onClick(@NonNull View widget) {
                        reference = FirebaseDatabase.getInstance("https://fintrack-cca93-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("ForgotPass");
                        reference.child("email").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                if (task.isSuccessful()) {
                                    if (task.getResult().exists()) {
                                        DataSnapshot dataSnapshot = task.getResult();
                                        String email_to_sent = String.valueOf(dataSnapshot.getValue());
                                        SendPassResetLink(email_to_sent);

                                    }
                                }else {

                                }

                            }
                        });

                    }

                    @Override
                    public void updateDrawState(@NonNull TextPaint ds) {
                        super.updateDrawState(ds);
                        ds.setColor(Color.parseColor("#0F67B1"));
                        ds.setUnderlineText(false);
                    }
                };
                ss.setSpan(login,0,10, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                CountDown.setText(ss);
                CountDown.setMovementMethod(LinkMovementMethod.getInstance());
            }
        }.start();
    }
    public void SendPassResetLink(String email_input){

        mAuth.sendPasswordResetEmail(email_input).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                new CountDownTimer(60000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        CountDown.setText("Didn't get it? Send again in " + millisUntilFinished / 1000);
                    }

                    public void onFinish() {
                        SpannableString ss = new SpannableString("Send again");
                        ClickableSpan login = new ClickableSpan() {
                            @Override
                            public void onClick(@NonNull View widget) {
                                reference = FirebaseDatabase.getInstance("https://fintrack-cca93-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("ForgotPass");
                                reference.child("email").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            if (task.getResult().exists()) {
                                                DataSnapshot dataSnapshot = task.getResult();
                                                String email_to_sent = String.valueOf(dataSnapshot.getValue());
                                                SendPassResetLink(email_to_sent);

                                            }
                                        }else {

                                        }

                                    }
                                });

                            }

                            @Override
                            public void updateDrawState(@NonNull TextPaint ds) {
                                super.updateDrawState(ds);
                                ds.setColor(Color.parseColor("#0F67B1"));
                                ds.setUnderlineText(false);
                            }
                        };
                        ss.setSpan(login,0,10, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                        CountDown.setText(ss);
                        CountDown.setMovementMethod(LinkMovementMethod.getInstance());
                    }
                }.start();
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                ForgotpassEmailError.setText("Failed to send Email. Try again later.");
            }
        });

    }
    @Override
    public boolean onNavigateUp() {
        super.onBackPressed();
        return super.onNavigateUp();
    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        startActivity(to_forgetPass);
        finish();
    }
}