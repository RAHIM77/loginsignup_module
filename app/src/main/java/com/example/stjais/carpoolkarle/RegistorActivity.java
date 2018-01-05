package com.example.stjais.carpoolkarle;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistorActivity extends AppCompatActivity {
    EditText fullname, email, age, Contactno, password;
    Button Registorbtn;
    FirebaseAuth mAuth;
 DatabaseReference DR;
 ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registor);
        fullname = (EditText) findViewById(R.id.et_fullname);
        email = (EditText) findViewById(R.id.et_email);
        age = (EditText) findViewById(R.id.et_age);
        Contactno = (EditText) findViewById(R.id.et_cno);
        password = (EditText) findViewById(R.id.et_password);
        Registorbtn = (Button) findViewById(R.id.btnSignup);
        mAuth = FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);
        DR= FirebaseDatabase.getInstance().getReference();

        Registorbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartSingnup();
            }
        });

    }

    private void StartSingnup() {
        final String fname, mail, ag, cno, pass,gender="";
        final int finalage, finalcno;

        fname = fullname.getText().toString();
        if(TextUtils.isEmpty(fname)){
            Toast.makeText(this, "Fullname field is emty", Toast.LENGTH_SHORT).show();
        }

        mail = email.getText().toString();
        if(TextUtils.isEmpty(mail)){
            Toast.makeText(this, "Email field is emty", Toast.LENGTH_SHORT).show();
        }

        ag = age.getText().toString();
        if(TextUtils.isEmpty(ag)){
            Toast.makeText(this, "age field is emty", Toast.LENGTH_SHORT).show();
        }

        finalage = Integer.parseInt(ag);
        cno = Contactno.getText().toString();
        if(TextUtils.isEmpty(cno)){
            Toast.makeText(this, "Contact no field is empty", Toast.LENGTH_SHORT).show();
        }
        finalcno = Integer.parseInt(cno);


        pass = password.getText().toString();
        if(TextUtils.isEmpty(pass)){
            Toast.makeText(this, "password field is emty", Toast.LENGTH_SHORT).show();
        }


        progressDialog.setMessage("Registoring User\n Please Wait...");
        progressDialog.show();
        //registoring user
        mAuth.createUserWithEmailAndPassword(mail, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(RegistorActivity.this, "Registored Successfully", Toast.LENGTH_SHORT).show();
                            String userid=mAuth.getCurrentUser().getUid();
                            DatabaseReference currentuser=DR.child(userid);
                            currentuser.child("fullname").setValue(fname);
                            currentuser.child("email").setValue(mail);
                            currentuser.child("age").setValue(finalage);
                            currentuser.child("contactno").setValue(finalcno);
                            currentuser.child("password").setValue(pass);

                            // go to login page
                            Intent intent=new Intent(RegistorActivity.this,LoginActivity.class);
                            startActivity(intent);
                            progressDialog.dismiss();
                        }
                        if(!task.isSuccessful()){
                            FirebaseAuthException e = (FirebaseAuthException )task.getException();
                            Toast.makeText(RegistorActivity.this, "Failed Registration: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                            progressDialog.hide();
                            return;
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegistorActivity.this, "error while signing up.. try again", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
