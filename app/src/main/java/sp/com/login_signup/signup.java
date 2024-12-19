package sp.com.login_signup;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Map;

public class signup extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private TextInputEditText Email;
    private TextInputEditText Username;
    private TextInputEditText Password;
    private Button SignupBtn;
    private TextView Loginclick,Error;
    private Intent intent;
    private TextInputLayout passLayout;
    String user;

    FirebaseDatabase db;
    DatabaseReference reference,ByUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        Username = findViewById(R.id.username);
        Email = findViewById(R.id.email);
        Password = findViewById(R.id.password);
        SignupBtn = findViewById(R.id.signupBtn);
        Loginclick = findViewById(R.id.loginclick);
        passLayout = findViewById(R.id.textInputLayout3);
        Error = findViewById(R.id.Error);


        signup2login("Already have an account? Log in",-6,0,"Loginclick");

        SignupBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String email = Email.getText().toString().toLowerCase().trim();
                String password = Password.getText().toString().trim();
                String username = Username.getText().toString().trim();
                if(!(username.isEmpty()||username.length()<2)) {
                    db = FirebaseDatabase.getInstance("https://fintrack-cca93-default-rtdb.asia-southeast1.firebasedatabase.app");
                    reference = db.getReference("ByUsername");
                    reference.child(username).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (task.isSuccessful()) {
                                if (task.getResult().exists()) {
                                    Username.setError("Username must be unique.");
                                    Username.setFocusable(true);
                                } else {
                                    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                                        Email.setError("Invalid Email");
                                        Email.setFocusable(true);

                                    } else if (password.length() < 6) {
                                        passLayout.setEndIconVisible(false);
                                        Password.setError("Password must be at least 6 letters.");
                                        Password.setFocusable(true);

                                        Password.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Password.setError(null);
                                                passLayout.setEndIconVisible(true);
                                            }
                                        });
                                    } else {
                                        register_user(email, password, username);
                                    }
                                }
                            } else {

                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });

                }else {
                    Username.setError("Username must be at least 2 letters.");
                    Username.setFocusable(true);
                }

            }


        });


    }



    public void register_user(String email, String password,String username){


                            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override

                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (task.isSuccessful()) {
                                        SaveUser(username,email);
                                    }

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                    signup2login("This Email is already connected to an account. Try again or log in",-6,0,"Error");
                                }
                            });


    }

    public void signup2login(String part,int start,int end,String type){
        int reversed_start = part.length()+start;
        int reversed_end=part.length()+end;
            SpannableString ss = new SpannableString(part);
            ClickableSpan login = new ClickableSpan() {
                @Override
                public void onClick(@NonNull View widget) {
                    intent = new Intent(signup.this, LoginActivity.class);
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
            ss.setSpan(login,reversed_start,reversed_end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            if(type.equals("Loginclick")) {
                Loginclick.setText(ss);
                Loginclick.setMovementMethod(LinkMovementMethod.getInstance());
            }else if(type.equals("Error")){
            Error.setText(ss);
            Error.setMovementMethod(LinkMovementMethod.getInstance());
        }

    }

    public void SaveUser(String user,String email){
        String UserId = FirebaseAuth.getInstance().getCurrentUser().getUid().toString().trim();
        User users = new User(user,email);
        db = FirebaseDatabase.getInstance("https://fintrack-cca93-default-rtdb.asia-southeast1.firebasedatabase.app");
        reference = db.getReference("ByID");
        reference.child(UserId).setValue(users);
        ByUser = db.getReference("ByUsername");
        ByUser.child(user).setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Username.setText("");
                Toast.makeText(signup.this,"User added", Toast.LENGTH_SHORT).show();
                intent = new Intent(signup.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(signup.this,"failed", Toast.LENGTH_SHORT).show();

            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        return super.onNavigateUp();
    }



}


