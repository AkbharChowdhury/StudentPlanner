package com.studentplanner.studentplanner.fragments;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.studentplanner.studentplanner.DatabaseHelper;
import com.studentplanner.studentplanner.R;
import com.studentplanner.studentplanner.adapters.ModuleAdapter;
import com.studentplanner.studentplanner.addActivities.AddModuleActivity;
import com.studentplanner.studentplanner.databinding.FragmentModuleBinding;

import com.studentplanner.studentplanner.models.Module;
import com.studentplanner.studentplanner.models.Search;
import com.studentplanner.studentplanner.utils.EmptyData;
import com.studentplanner.studentplanner.utils.Helper;

import java.util.List;


public class ModuleFragment extends Fragment {
    private Context context;
    private Activity activity;

    private RecyclerView recyclerView;
    private ModuleAdapter adapter;
    private List<Module> list;
    private FragmentModuleBinding binding;
    private DatabaseHelper db;
    private List<Module> ALL_MODULES;
    private EmptyData emptyData;

    private void activityResult(ActivityResult result){
        if (result.getResultCode() == RESULT_OK){
            getModule();
        }
    }

    private final ActivityResultLauncher<Intent> startForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), this::activityResult);


    public ModuleFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initFragment();
        binding = FragmentModuleBinding.inflate(inflater, container, false);
        binding.fabAdd.setOnClickListener(v -> startForResult.launch(new Intent(getActivity(), AddModuleActivity.class)));
        recyclerView = binding.recyclerView;
        db = DatabaseHelper.getInstance(context);
        ALL_MODULES = db.getModules();

        emptyData = new EmptyData(binding.emptyImage, binding.emptyText);
        Helper.getIntentMessage(context, activity.getIntent().getExtras());
        getModule();

        return binding.getRoot();
    }


    private void getModule() {
        list = db.getModules();
        buildRecyclerView();
    }

    private void initFragment() {
        context = getContext();
        activity = getActivity();
        activity.setTitle(context.getString(R.string.my_modules));
        setHasOptionsMenu(true);

    }


    private void buildRecyclerView() {
        if (!list.isEmpty()) {
            adapter = new ModuleAdapter(list, context, startForResult);
            LinearLayoutManager manager = new LinearLayoutManager(context);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(manager);
            recyclerView.setAdapter(adapter);
            return;
        }

        emptyData.emptyResultStatus(true);


    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {


        if (!list.isEmpty()) {
            activity.getMenuInflater().inflate(R.menu.search_menu, menu);

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

        List<Module> filteredList = (List<Module>) Search.textSearch(ALL_MODULES, text);

        if (filteredList.isEmpty()) {
            adapter.filterList(filteredList);
            Helper.shortToastMessage(context, context.getString(R.string.no_data_found));
            emptyData.emptyResultStatus(true);
            return;
        }
        adapter.filterList(filteredList);
        emptyData.emptyResultStatus(false);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}