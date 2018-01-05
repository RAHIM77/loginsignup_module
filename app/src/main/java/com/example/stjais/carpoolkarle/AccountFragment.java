package com.example.stjais.carpoolkarle;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;


public class AccountFragment extends Fragment {

    TextView fn,em,ps;
    Button lg;
    ProgressDialog p;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fn=(EditText)getView().findViewById(R.id.fname);
        em=(EditText)getView().findViewById(R.id.email);
        ps=(EditText)getView().findViewById(R.id.pss);
        lg=(Button)getView().findViewById(R.id.btnlogout);
        lg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutUser();
            }
        });

    }

    private void logoutUser() {
        FirebaseAuth.getInstance().signOut();
        Intent intent=new Intent(getActivity(),LoginActivity.class);
        getActivity().startActivity(intent);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account, container, false);

    }






}
