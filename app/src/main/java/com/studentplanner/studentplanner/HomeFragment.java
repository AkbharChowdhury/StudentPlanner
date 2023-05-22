package com.studentplanner.studentplanner;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.studentplanner.studentplanner.adapters.ModuleAdapter;
import com.studentplanner.studentplanner.addActivities.AddModuleActivity;
import com.studentplanner.studentplanner.databinding.ActivityAddModuleBinding;
import com.studentplanner.studentplanner.databinding.FragmentHomeBinding;
import com.studentplanner.studentplanner.databinding.FragmentModuleBinding;
import com.studentplanner.studentplanner.models.Module;
import com.studentplanner.studentplanner.utils.Helper;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private Context context;
    private Activity activity;
    private RecyclerView recyclerView;
    private ModuleAdapter adapter;
    private List<Module> list;




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

    public HomeFragment() {
        // Required empty public constructor
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        context = getContext();
        binding = FragmentHomeBinding.inflate(getLayoutInflater(), container, false);
        getActivity().setContentView(binding.getRoot());
        getActivity().setTitle(context.getString(R.string.my_modules));
        setHasOptionsMenu(true);

        binding.fabAddModule.setOnClickListener(v -> Helper.goToActivity(getActivity(), AddModuleActivity.class));


//
            DatabaseHelper db = DatabaseHelper.getInstance(getContext());
            Helper.getIntentMessage(getContext(), getActivity().getIntent().getExtras());
//
            list = db.getModules();
            recyclerView = binding.moduleRecyclerView;
            buildRecyclerView();




        return view;
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
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}