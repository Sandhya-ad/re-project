package com.example.coffee2_app.Entrant_ui.signedup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.coffee2_app.R;
import com.example.coffee2_app.databinding.FragmentSignedUpBinding;

public class SignedUpFragment extends Fragment {

    private FragmentSignedUpBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Attach the layout to this fragment
        View root = inflater.inflate(R.layout.fragment_signed_up, container, false);

        // Find the Back Button from the layout
        ImageButton backButton = root.findViewById(R.id.back_button);

        // Handle Back Button click
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the back button action
                getActivity().onBackPressed();  // Navigate back when clicked
            }
        });
        return root;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}