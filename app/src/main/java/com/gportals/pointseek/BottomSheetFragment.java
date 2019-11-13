package com.gportals.pointseek;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetFragment extends BottomSheetDialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_bottom_sheet, container, false);

        TextView tvName = v.findViewById(R.id.tvDisplayName);
        TextView tvDescription = v.findViewById(R.id.tvDescription);
        Button btnCheck = v.findViewById(R.id.btnCheck);

        final String name = getArguments().getString("name");
        String description = getArguments().getString("description");

        tvName.setText(name);
        tvDescription.setText(description);

        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getContext(), name, Toast.LENGTH_SHORT).show();

            }
        });



        return v;


    }

 

}
