package com.studentplanner.studentplanner;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.studentplanner.studentplanner.addActivities.AddModuleActivity;
import com.studentplanner.studentplanner.databinding.FragmentModuleBinding;
import com.studentplanner.studentplanner.databinding.FragmentModuleTestBinding;
import com.studentplanner.studentplanner.utils.Helper;


public class ModuleTestFragment extends Fragment {

    private Button btnPressMe;



    public ModuleTestFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_module_test, container, false);
        btnPressMe = view.findViewById(R.id.button);
        btnPressMe.setText("Hello Akbhar");
        getActivity().setTitle(getContext().getString(R.string.my_modules));
        btnPressMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddModuleActivity.class));


            }
        });

//        FragmentModuleTestBinding binding  = FragmentModuleTestBinding.inflate(getLayoutInflater(), container, false);
////        getActivity().setContentView(binding.getRoot());
//
//        Button button = binding.button;
//        button.setText("Changed");
////        button.setOnClickListener(v -> Helper.goToActivity(getActivity(), AddModuleActivity.class));
//        binding.button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Helper.goToActivity(getActivity(), AddModuleActivity.class);
//            }
//        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        getActivity().setTitle(getContext().getString(R.string.my_modules));
//        Button button =  view.findViewById(R.id.button);
//        button.setOnClickListener(v -> Helper.goToActivity(getActivity(), AddModuleActivity.class));
//        button.setText("Hello World!");
//
//        getActivity().findViewById(R.id.button).setOnClickListener(this);



    }

}