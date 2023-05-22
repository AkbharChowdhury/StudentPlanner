package com.studentplanner.studentplanner;

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
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.studentplanner.studentplanner.adapters.ModuleAdapter;
import com.studentplanner.studentplanner.models.Module;
import com.studentplanner.studentplanner.utils.Helper;

import java.util.ArrayList;
import java.util.List;


public class ModuleFragment extends Fragment {
    private View view;
    private Context context;

    private RecyclerView recyclerView;
    private ModuleAdapter adapter;
    private List<Module> list;


    public ModuleFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        initFragment(inflater,container);

        DatabaseHelper db = DatabaseHelper.getInstance(context);
        Helper.getIntentMessage(context, getActivity().getIntent().getExtras());

        list = db.getModules();
        recyclerView = view.findViewById(R.id.moduleRecyclerView);
        buildRecyclerView();
        FloatingActionButton button = (FloatingActionButton) view.findViewById(R.id.fab_add_module);
        button.setOnClickListener(v -> Helper.longToastMessage(context, "Hello"));

        return view;
    }

    private void initFragment(LayoutInflater inflater, ViewGroup container) {
        view = inflater.inflate(R.layout.fragment_module, container, false);
        context = getContext();
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

        view.findViewById(R.id.emptyModuleImage).setVisibility(View.VISIBLE);
        view.findViewById(R.id.emptyModuleText).setVisibility(View.VISIBLE);


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
            String code =  module.getModuleName().toLowerCase();
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