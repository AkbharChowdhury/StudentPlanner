package com.studentplanner.studentplanner;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.studentplanner.studentplanner.databinding.FragmentViewModuleBinding;

public class ViewModuleFragment extends Fragment {
    private FragmentViewModuleBinding binding;




    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {

        getActivity().getMenuInflater().inflate(R.menu.search_menu, menu);

    }

    public ViewModuleFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_home, container, false);
//
//        binding = FragmentHomeBinding.inflate(getLayoutInflater());
//        getActivity().setContentView(binding.getRoot());
//
//
//
//        Button b = binding.btn1;
//        b.setOnClickListener(v -> Toast.makeText(getContext(), "Hello", Toast.LENGTH_SHORT).show());
//        return view;
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_module, container, false);
        getActivity().setTitle(getContext().getString(R.string.my_modules));
        setHasOptionsMenu(true);

        binding = FragmentViewModuleBinding.inflate(getLayoutInflater());
        getActivity().setContentView(binding.getRoot());



//        Button b = binding.btnSubmit;
//        b.setOnClickListener(v -> Toast.makeText(getContext(), "Hello", Toast.LENGTH_SHORT).show());
        return view;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}