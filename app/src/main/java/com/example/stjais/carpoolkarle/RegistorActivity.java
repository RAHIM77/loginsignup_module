package com.example.stjais.carpoolkarle;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.collect.Range;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class RegistorActivity extends AppCompatActivity {
    private static final String TAG ="dcd" ;
    EditText fullname, email, age, Contactno,uname, password;
    Button Registorbtn;
    private AwesomeValidation  awesomeValidation;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mauthlistener;
    DatabaseReference DR;


    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registor);
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        fullname = (EditText) findViewById(R.id.et_fullname);
        awesomeValidation.addValidation(this, R.id.et_fullname, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.nameerror);

        email = (EditText) findViewById(R.id.et_email);
        awesomeValidation.addValidation(this, R.id.et_email, Patterns.EMAIL_ADDRESS, R.string.emailerror);

        age = (EditText) findViewById(R.id.et_age);
        awesomeValidation.addValidation(this, R.id.et_age, Range.closed(13, 60), R.string.ageerror);

        Contactno = (EditText) findViewById(R.id.et_cno);
        //  awesomeValidation.addValidation(this, R.id.et_cno, "^[2-9]{2}[0-9]{8}$", R.string.mobileerror);

        uname = (EditText) findViewById(R.id.et_username);

        password = (EditText) findViewById(R.id.et_password);
        Registorbtn = (Button) findViewById(R.id.btnSignup);

        mAuth = FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);
        DR= FirebaseDatabase.getInstance().getReference().child("Users");


        Registorbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                StartSingnup();

            }
        });

    }


    private void StartSingnup() {
        final String fname, mail, ag, cno, usname, pass;
        final int finalage, finalcno;

        fname = fullname.getText().toString();
        mail = email.getText().toString();
        ag = age.getText().toString();
        finalage = Integer.parseInt(ag);
        cno = Contactno.getText().toString();

        finalcno = Integer.parseInt(cno);

        usname = uname.getText().toString();
        pass = password.getText().toString();


        progressDialog.setMessage("Registoring User\n Please Wait...");
        progressDialog.show();
        //registoring user
        if (awesomeValidation.validate() && !TextUtils.isEmpty(usname) && !TextUtils.isEmpty(pass)) {
            mAuth.createUserWithEmailAndPassword(mail, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        sendVerificationEmail();
                        Toast.makeText(RegistorActivity.this, "Registored Successfully", Toast.LENGTH_SHORT).show();
                        String userid = mAuth.getCurrentUser().getUid();
                        DatabaseReference currentuser = DR.child(userid);
                        currentuser.child("fullname").setValue(fname);
                        currentuser.child("email").setValue(mail);
                        currentuser.child("age").setValue(finalage);
                        currentuser.child("contactno").setValue(finalcno);
                        currentuser.child("username").setValue(usname);
                        currentuser.child("password").setValue(pass);

                        // go to login page
                        Intent intent = new Intent(RegistorActivity.this, LoginActivity.class);
                        startActivity(intent);

                    }

                }
            });
        }
        else
        {
            Toast.makeText(this, "enter proper details..", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendVerificationEmail() {
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            user.sendEmailVerification()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(RegistorActivity.this, "Signup successful Verification email sent", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }


}
