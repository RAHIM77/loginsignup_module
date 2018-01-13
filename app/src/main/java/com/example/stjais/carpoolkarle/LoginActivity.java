package com.example.stjais.carpoolkarle;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG ="dfnjn" ;
    TextView gotoregistor,resetlink;
EditText email,password;
Button loginbtn;
FirebaseAuth mAuth;

ProgressDialog pd;
private AwesomeValidation awesomeValidation2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        awesomeValidation2 = new AwesomeValidation(ValidationStyle.BASIC);
        email=(EditText)findViewById(R.id.et_email);
        awesomeValidation2.addValidation(this, R.id.et_email, Patterns.EMAIL_ADDRESS, R.string.emailerror);
        password=(EditText)findViewById(R.id.et_password);
        loginbtn=(Button)findViewById(R.id.btnlogin);
        mAuth=FirebaseAuth.getInstance();
        pd=new ProgressDialog(this);

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    startlogin();

            }
        });

        gotoregistor=(TextView)findViewById(R.id.txt_reglink);

        gotoregistor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this,RegistorActivity.class);
                startActivity(intent);
            }
        });
        resetlink=(TextView)findViewById(R.id.txt_resetlink);


        resetlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this,ResetPasswordActivity.class);
                startActivity(intent);
            }
        });
    }

    private void startlogin() {
        String mail, pass;
        mail = email.getText().toString();

        pass = password.getText().toString();


        pd.setMessage("Signing in\nPlease Wait...");
        pd.show();
        if (awesomeValidation2.validate()) {
            mAuth.signInWithEmailAndPassword(mail, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                    if (!task.isSuccessful()) {
                        pd.dismiss();
                        FirebaseAuthException e = (FirebaseAuthException) task.getException();
                        Toast.makeText(LoginActivity.this, "Failed to login: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else
                    {
                        checkIfEmailVerified();
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(LoginActivity.this, "u need to registor first!!", Toast.LENGTH_SHORT).show();
                }
            });

        }


    }
    private void checkIfEmailVerified()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user.isEmailVerified())
        {
            // user is verified, so you can finish this activity or send user to activity which you want.
            finish();
            Toast.makeText(LoginActivity.this, "Successfully logged in", Toast.LENGTH_SHORT).show();
        }
        else
        {
            // email is not verified, so just prompt the message to the user and restart this activity.
            // NOTE: don't forget to log out the user.
            FirebaseAuth.getInstance().signOut();

            //restart this activity

        }
    }




}
