package com.gportals.pointseek.group;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.vision.text.Line;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.gportals.pointseek.R;

import java.lang.ref.Reference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GroupFragment extends Fragment {

    private static final String TAG = "GroupFragment";

    private ArrayList<String> mPointNames = new ArrayList<>();
    private ArrayList<String> mPointDescriptions = new ArrayList<>();

    private TextView tvDisplayName;
    private RecyclerView rvReachedPoints;
    private Button btnLogout;

    private SharedPreferences storage;

    public GroupFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: started.");
        
        View view = inflater.inflate(R.layout.group_fragment, container, false);

        tvDisplayName = view.findViewById(R.id.tvDisplayName);
        rvReachedPoints = view.findViewById(R.id.rvReachedPoints);
        btnLogout = view.findViewById(R.id.btnLogout);

        storage = getActivity().getSharedPreferences("groupLogin", Context.MODE_PRIVATE);
        DocumentReference mGroup = FirebaseFirestore.getInstance().collection("groups").document(storage.getString("group_id", "testgroup"));

        tvDisplayName.setText(null);

        mGroup.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                tvDisplayName.setText(documentSnapshot.getString("display"));
                initPoints(documentSnapshot);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storage.edit().remove("group_id").apply();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new LoginFragment()).commit();
            }
        });


        return view;
    }

    private void initPoints(DocumentSnapshot documentSnapshot) {
        List<DocumentReference> points = (List<DocumentReference>) documentSnapshot.get("locations");
        Log.d(TAG, "initPoints: Size of points is " + points.size());
        for (DocumentReference reference : points) {
            reference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    Log.d(TAG, "onSuccess: got the document");
                    mPointNames.add(documentSnapshot.getString("name"));
                    mPointDescriptions.add(documentSnapshot.getString("description"));

                    initRecyclerView();
                }
            });
        }


    }

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: init recyclerview.");
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(mPointNames, mPointDescriptions, getActivity());
        rvReachedPoints.setAdapter(adapter);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvReachedPoints.setLayoutManager(layoutManager);
    }

    public void setmPointNames(ArrayList<String> mPointNames) {
        this.mPointNames = mPointNames;
    }

    public void setmPointDescriptions(ArrayList<String> mPointDescriptions) {
        this.mPointDescriptions = mPointDescriptions;
    }
}
