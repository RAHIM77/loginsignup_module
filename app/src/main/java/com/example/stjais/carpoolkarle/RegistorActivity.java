package com.example.stjais.carpoolkarle;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mauthlistener;
    DatabaseReference DR;
    TextInputLayout ifname,iemail,iage,icon,iusname,ipass;


    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registor);


        fullname = (EditText) findViewById(R.id.et_fullname);
        email = (EditText) findViewById(R.id.et_email);
        age = (EditText) findViewById(R.id.et_age);
        Contactno = (EditText) findViewById(R.id.et_cno);
        uname = (EditText) findViewById(R.id.et_username);
        password = (EditText) findViewById(R.id.et_password);

        ifname=(TextInputLayout)findViewById(R.id.input_layout_name);
        iemail=(TextInputLayout)findViewById(R.id.input_email_);
        iage=(TextInputLayout)findViewById(R.id.input_layout_age);
        icon=(TextInputLayout)findViewById(R.id.input_layout_contact);
        iusname=(TextInputLayout)findViewById(R.id.input_layout_username);
        ipass=(TextInputLayout)findViewById(R.id.input_layout_password);

        Registorbtn = (Button) findViewById(R.id.btnSignup);

        mAuth = FirebaseAuth.getInstance();
        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user=mAuth.getInstance().getCurrentUser();
                if (user != null ) {
                    Log.e(TAG, user.isEmailVerified() ? "User is signed in and email is verified" : "Email is not verified");
                } else {
                    Log.e(TAG, "onAuthStateChanged:signed_out");
                }
            }
        });
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

        if (!validateName()) {
            return;
        }

        if (!validateEmail()) {
            return;
        }
        if(!validateage()){
            return;
        }
        if(!validatcno()){
            return;
        }
        if(!validateuname()){
            return;
        }
        if(!validatepass()){
            return;
        }
            
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
    private boolean validateName() {
        if(fullname.getText().toString().trim().isEmpty()){
            ifname.setError("Enter your fullname");
            requestFocus(fullname);
            return  false;
        }
        else {
            ifname.setErrorEnabled(false);

        }
        return  true;
    }

    private boolean validateEmail() {
        String em=email.getText().toString().trim();
        if(em.isEmpty() || !isValidEmail(em)){
            iemail.setError("Enter your email Id");
            requestFocus(email);
            return  false;
        }
        else {
            iemail.setErrorEnabled(false);
        }
        return  true;
    }

    private boolean validateage() {
        if(age.getText().toString().trim().isEmpty()){
            iage.setError("Enter your age");
            requestFocus(age);
            return  false;
        }
        else {
            iage.setErrorEnabled(false);
        }
        return  true;
    }

    private boolean validatcno() {
        if(Contactno.getText().toString().trim().isEmpty()){
            icon.setError("Enter your contact no");
            requestFocus(Contactno);
            return  false;
        }
        else {
            icon.setErrorEnabled(false);
        }
        return  true;
    }

    private boolean validateuname() {
        if(uname.getText().toString().trim().isEmpty()){
            iusname.setError("Enter Username");
            requestFocus(uname);
            return  false;
        }
        else {
            iusname.setErrorEnabled(false);
        }
        return  true;
    }

    private boolean validatepass() {
        if(password.getText().toString().trim().isEmpty()){
            ipass.setError("Enter password");
            requestFocus(password);
            return  false;
        }
        else {
            ipass.setErrorEnabled(false);
        }
        return true;
    }

    private boolean isValidEmail(String em) {
        return !TextUtils.isEmpty(em) && android.util.Patterns.EMAIL_ADDRESS.matcher(em).matches();
    }


    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.et_fullname:
                    validateName();
                    break;
                case R.id.et_email:
                    validateEmail();
                    break;
                case R.id.et_age:
                    validateage();
                    break;
                case R.id.et_cno:
                    validatcno();
                    break;
                case R.id.et_username:
                    validateuname();
                    break;
                case R.id.et_password:
                    validatepass();
                    break;


            }
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

