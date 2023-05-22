package com.studentplanner.studentplanner.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.studentplanner.studentplanner.DatabaseHelper;
import com.studentplanner.studentplanner.R;
import com.studentplanner.studentplanner.adapters.ModuleAdapter;
import com.studentplanner.studentplanner.addActivities.AddModuleActivity;
import com.studentplanner.studentplanner.databinding.FragmentModuleBinding;
import com.studentplanner.studentplanner.models.Module;
import com.studentplanner.studentplanner.utils.Helper;

import java.util.ArrayList;
import java.util.List;


public class ModuleFragment extends Fragment {
    private Context context;
    private Activity activity;
    private RecyclerView recyclerView;
    private ModuleAdapter adapter;
    private List<Module> list;
    private FragmentModuleBinding binding;

    public ModuleFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initFragment(container);

        FloatingActionButton floatingActionButton = binding.fabAddModule;
        floatingActionButton.setOnClickListener(v -> Helper.longToastMessage(getContext(), "Hello"));
        DatabaseHelper db = DatabaseHelper.getInstance(context);
        Helper.getIntentMessage(context, activity.getIntent().getExtras());

        list = db.getModules();
        recyclerView = binding.moduleRecyclerView;
        buildRecyclerView();


        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.fabAddModule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Helper.longToastMessage(getActivity(), "Hello");
            }
        });


//        FragmentModuleBinding binding1  = FragmentModuleBinding.inflate(getLayoutInflater());
//        getActivity().setContentView(binding.getRoot());
//        binding1.fabAddModule.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Helper.goToActivity(getActivity(), AddModuleActivity.class);
//            }
//        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void initFragment(ViewGroup container) {
        binding = FragmentModuleBinding.inflate(getLayoutInflater(), container, false);
        context = getContext();
        activity = getActivity();
        getActivity().setTitle(context.getString(R.string.my_modules));
        setHasOptionsMenu(true);

    }


    private void buildRecyclerView() {
        if (list.size() > 0) {
            adapter = new ModuleAdapter(list, context, getActivity());
            LinearLayoutManager manager = new LinearLayoutManager(context);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(manager);
            recyclerView.setAdapter(adapter);
            return;
        }

        binding.emptyModuleImage.setVisibility(View.VISIBLE);
        binding.emptyModuleText.setVisibility(View.VISIBLE);


    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {


        if (list.size() > 0) {
            getActivity().getMenuInflater().inflate(R.menu.search_menu, menu);

            MenuItem searchItem = menu.findItem(R.id.actionSearch);
            SearchView searchView = (SearchView) searchItem.getActionView();

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    filter(newText);
                    return false;
                }
            });

        }
    }


    private void filter(String text) {
        List<Module> filteredList = new ArrayList<>();

        for (Module module : list) {
            String name = module.getModuleName().toLowerCase();
            String code = module.getModuleName().toLowerCase();
            if (name.contains(text.toLowerCase()) | code.contains(text.toLowerCase())) {
                filteredList.add(module);
            }
        }
        if (filteredList.isEmpty()) {
            Toast.makeText(context, "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {
            adapter.filterList(filteredList);
        }
    }


}

