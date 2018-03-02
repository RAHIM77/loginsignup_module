package com.example.stjais.carpoolkarle;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.ResultCodes;
import com.google.firebase.auth.FirebaseAuth;
import java.util.Arrays;
/**
 * Created by AnupTechTips on 13/7/18.
 */
public class PhoneNoAuth extends AppCompatActivity {
    private static final int RC_SIGN_IN = 123;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
// already signed in
            startActivity(new Intent(PhoneNoAuth.this, MainActivity.class));
            finish();
        } else {
// not signed in
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(
                                    Arrays.asList(
                                            new AuthUI.IdpConfig.Builder(AuthUI.PHONE_VERIFICATION_PROVIDER).build()
                                    ))
                            .build(),
                    RC_SIGN_IN);
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // RC_SIGN_IN is the request code
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
// Successfully signed in
            if (resultCode == ResultCodes.OK) {
                startActivity(new Intent(PhoneNoAuth.this,MainActivity.class));
                finish();
                return;
            }
            // Sign in failed
            else {
                // User pressed back button
                if (response == null) {
                    ShowAlertBox("Login canceled by User");
                    return;
                }
                //No internet connection on the device
                if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
                    ShowAlertBox("No Internet Connection");
                    return;
                }
                //other errors
                if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    ShowAlertBox("Unknown Error");
                    return;
                }
            }
            ShowAlertBox("Unknown sign in response");
        }
    }
    //Creating a custom alert box
    protected void ShowAlertBox(String msg){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(PhoneNoAuth.this);
        builder1.setMessage(msg);
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}