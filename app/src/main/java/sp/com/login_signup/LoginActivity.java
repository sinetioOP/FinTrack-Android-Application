package sp.com.login_signup;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
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

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import sp.com.login_signup.R;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private TextInputEditText Email;

    private TextInputEditText Password;
    private Button LoginBtn;
    private TextView Signupclick;
    private TextView forgetPass;
    private Intent intent;
    private TextView Error;
    private TextInputLayout PassLayout;
    String email;
    String mail;
    FirebaseDatabase db;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Email = findViewById(R.id.email);
        Password = findViewById(R.id.password);
        LoginBtn = findViewById(R.id.loginBtn);
        forgetPass = findViewById(R.id.forgotPassword);
        Signupclick = findViewById(R.id.signupclick);
        Error = findViewById(R.id.error);
        PassLayout = findViewById(R.id.passLayout);
        mAuth = FirebaseAuth.getInstance();

        login2signup("New to FinTrack? Sign up",17,24);
        forgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email_input = Email.getText().toString().trim();
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
                intent = new Intent(LoginActivity.this, forgetPassword.class);
                startActivity(intent);
                mail = email;
                finish();
            }
        });

        LoginBtn.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                email = Email.getText().toString().trim();
                String password = Password.getText().toString().trim();
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Email.setError("Invalid Email");
                    Email.setFocusable(true);

                } else if (password.length() < 6) {
                    Password.setText("");
                    login2signup("Incorrect Email or Password. Try again or Create new.",41,52);
                }else {
                    LogUserIn(email, password);
                }
            }



        });


    }
    public void LogUserIn(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Error.setText(" ");
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            reference = FirebaseDatabase.getInstance("https://fintrack-cca93-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("ByID");
                            reference.child(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        if (task.getResult().exists()) {
                                            DataSnapshot dataSnapshot = task.getResult();
                                            String profileUser = String.valueOf(dataSnapshot.child("user").getValue());
                                        }
                                    }
                                }
                            });
                            intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Password.setText("");
                            login2signup("Incorrect Email or Password. Try again or Create new.",41,52);


                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {


                    }
                });

    }
    public void login2signup(String part,int start,int end) {
        SpannableString ss = new SpannableString(part);
        ClickableSpan login = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                intent = new Intent(LoginActivity.this, signup.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.parseColor("#0F67B1"));
                ds.setUnderlineText(false);
            }
        };
        ss.setSpan(login, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        if (part == "Incorrect Email or Password. Try again or Create new.") {
            Error.setText(ss);
            Error.setMovementMethod(LinkMovementMethod.getInstance());
        } else {
            Signupclick.setText(ss);
            Signupclick.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        return super.onNavigateUp();
    }
}