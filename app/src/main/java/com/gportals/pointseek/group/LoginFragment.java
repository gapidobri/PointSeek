package com.gportals.pointseek.group;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.gportals.pointseek.R;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    private static final String DEBUG = "Login";

    EditText etName, etPassword;
    Button btnLogin;
    TextView tvMessage;

    SharedPreferences storage;

    List<DocumentSnapshot> groups;

    private CollectionReference mGroups = FirebaseFirestore.getInstance().collection("groups");

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_login, container, false);

        etName = view.findViewById(R.id.etName);
        etPassword = view.findViewById(R.id.etPassword);
        btnLogin = view.findViewById(R.id.btnLogin);
        tvMessage = view.findViewById(R.id.tvMessage);

        storage = getActivity().getSharedPreferences("groupLogin", Context.MODE_PRIVATE);

        if (storage.getString("group_id", null) != null) {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new GroupFragment()).commit();
        } else {

            etName.setText(storage.getString("group_name", null));
            etName.setText(storage.getString("group_password", null));

            mGroups.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    groups = queryDocumentSnapshots.getDocuments();
                }
            });

            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    tvMessage.setText(null);

                    boolean found = false;

                    for (DocumentSnapshot group : groups) {

                        if (etName.getText().toString().equals(group.getString("name"))) {
                            found = true;

                            if (etPassword.getText().toString().equals(group.getString("password"))) {
                                //Successful login

                                //Stores name and password to device
                                storage.edit().putString("group_id", group.getId()).apply();
                                storage.edit().putString("group_name", group.getString("name")).apply();
                                storage.edit().putString("group_password", group.getString("password")).apply();

                                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                        new GroupFragment()).commit();

                            } else {
                                //Wrong password
                                tvMessage.setTextColor(Color.RED);
                                tvMessage.setText("Wrong password");
                            }

                        }

                    }

                    if (!found) {
                        //Group not found
                        tvMessage.setTextColor(Color.RED);
                        tvMessage.setText("Group not found");
                    }
                }
            });

        }

        // Inflate the layout for this fragment
        return view;
    }

}
