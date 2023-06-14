package com.studentplanner.studentplanner.fragments;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.studentplanner.studentplanner.DatabaseHelper;
import com.studentplanner.studentplanner.R;
import com.studentplanner.studentplanner.adapters.TeacherAdapter;
import com.studentplanner.studentplanner.addActivities.AddTeacherActivity;
import com.studentplanner.studentplanner.databinding.FragmentTeacherBinding;
import com.studentplanner.studentplanner.models.Search;
import com.studentplanner.studentplanner.models.Teacher;
import com.studentplanner.studentplanner.utils.Helper;

import java.util.Collections;
import java.util.List;


public class TeacherFragment extends Fragment {
    private Context context;
    private Activity activity;

    private RecyclerView recyclerView;
    private TeacherAdapter adapter;
    private List<Teacher> list;
    private FragmentTeacherBinding binding;
    private DatabaseHelper db;
    private List<Teacher> ALL_TEACHERS;


    private final ActivityResultLauncher<Intent> startForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {

        if (result.getResultCode() == RESULT_OK) {
            getTeachers();


        }

    });


    public TeacherFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initFragment();
        binding = FragmentTeacherBinding.inflate(inflater, container, false);
        binding.fabAdd.setOnClickListener(v -> startForResult.launch(new Intent(getActivity(), AddTeacherActivity.class)));
        recyclerView = binding.recyclerView;

        db = DatabaseHelper.getInstance(context);
        ALL_TEACHERS = Collections.unmodifiableList(db.getTeachers());
        Helper.getIntentMessage(context, activity.getIntent().getExtras());
        getTeachers();

        return binding.getRoot();
    }

    private void getTeachers() {
        list = db.getTeachers();
        buildRecyclerView();
    }

    private void initFragment() {
        context = getContext();
        activity = getActivity();
        activity.setTitle(context.getString(R.string.my_teachers));
        setHasOptionsMenu(true);

    }


    private void buildRecyclerView() {
        if (!list.isEmpty()) {
            adapter = new TeacherAdapter(list, getContext(), startForResult);
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
        List<Teacher> filteredList = (List<Teacher>) Search.textSearch(ALL_TEACHERS, text);
        if (filteredList.isEmpty()) {
            Helper.shortToastMessage(context, context.getString(R.string.no_data_found));
        } else {
            adapter.filterList(filteredList);
        }

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}