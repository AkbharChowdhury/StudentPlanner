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

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.studentplanner.studentplanner.DatabaseHelper;
import com.studentplanner.studentplanner.R;
import com.studentplanner.studentplanner.adapters.ModuleTeacherAdapter;
import com.studentplanner.studentplanner.addActivities.AddModuleTeacherActivity;
import com.studentplanner.studentplanner.databinding.FragmentModuleTeacherBinding;
import com.studentplanner.studentplanner.models.ModuleTeacher;
import com.studentplanner.studentplanner.utils.Helper;

import java.util.ArrayList;
import java.util.List;


public class ModuleTeacherFragment extends Fragment {
    private Context context;
    private Activity activity;

    private RecyclerView recyclerView;
    private ModuleTeacherAdapter adapter;
    private List<ModuleTeacher> list;
    private FragmentModuleTeacherBinding binding;
    private DatabaseHelper db;


    public ModuleTeacherFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initFragment();
        binding = FragmentModuleTeacherBinding.inflate(inflater, container, false);
        binding.fabAdd.setOnClickListener((v -> Helper.goToActivity(activity, AddModuleTeacherActivity.class)));
        recyclerView = binding.recyclerView;


        db = DatabaseHelper.getInstance(context);
        Helper.getIntentMessage(context, activity.getIntent().getExtras());

        getModuleTeacher();
        return binding.getRoot();
    }
    private void getModuleTeacher(){
        list = db.getModuleTeachers();
        buildRecyclerView();
    }


    @Override
    public void onResume() {
        super.onResume();
        if (Helper.isUpdated()){
            getModuleTeacher();
            Helper.setUpdatedStatus(false);
        }

    }

    private void initFragment() {
        context = getContext();
        activity = getActivity();
        activity.setTitle(context.getString(R.string.my_teachers_modules));
        setHasOptionsMenu(true);

    }


    private void buildRecyclerView() {
        if (list.size() > 0) {
            adapter = new ModuleTeacherAdapter(list, context, activity);
            LinearLayoutManager manager = new LinearLayoutManager(context);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(manager);
            recyclerView.setAdapter(adapter);
            return;
        }

        binding.emptyImage.setVisibility(View.VISIBLE);
        binding.emptyText.setVisibility(View.VISIBLE);


    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {


        if (list.size() > 0) {
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
        List<ModuleTeacher> filteredList = new ArrayList<>();

//        for (M module : list) {
//            String moduleFullName = module.getModuleDetails().toLowerCase().trim();
//            String t = text.toLowerCase().trim();
//            if (moduleFullName.contains(t)) {
//                filteredList.add(module);
//            }
//        }
//
//        if (filteredList.isEmpty()) {
//            Helper.shortToastMessage(context, context.getString(R.string.no_data_found));
//        } else {
//            adapter.filterList(filteredList);
//        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}