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
import com.studentplanner.studentplanner.adapters.SemesterAdapter;
import com.studentplanner.studentplanner.addActivities.AddSemesterActivity;
import com.studentplanner.studentplanner.databinding.FragmentSemesterBinding;
import com.studentplanner.studentplanner.models.Semester;
import com.studentplanner.studentplanner.utils.Helper;

import java.util.ArrayList;
import java.util.List;


public class SemesterFragment extends Fragment {
    private Context context;
    private Activity activity;

    private RecyclerView recyclerView;
    private SemesterAdapter adapter;
    private List<Semester> list;
    private FragmentSemesterBinding binding;
    private DatabaseHelper db;


    public SemesterFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initFragment();
        binding = FragmentSemesterBinding.inflate(inflater, container, false);
        recyclerView = binding.recyclerView;
        binding.fabAdd.setOnClickListener((v -> Helper.goToActivity(activity, AddSemesterActivity.class)));

        db = DatabaseHelper.getInstance(context);
        Helper.getIntentMessage(context, activity.getIntent().getExtras());

        getSemester();;

        return binding.getRoot();
    }

    private void initFragment() {
        context = getContext();
        activity = getActivity();
        activity.setTitle(context.getString(R.string.my_semesters));
        setHasOptionsMenu(true);

    }
    private void getSemester(){
        list = db.getSemester();
        buildRecyclerView();
    }


    @Override
    public void onResume() {
        super.onResume();
        if (Helper.isUpdated()){
            getSemester();
            Helper.setUpdatedStatus(false);
        }

    }


    private void buildRecyclerView() {
        if (list.size() > 0) {
            adapter = new SemesterAdapter(list, context, activity);
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
        List<Semester> filteredList = new ArrayList<>();

        for (Semester semester : list) {
            String name = semester.getName().toLowerCase();
            if (name.contains(text.toLowerCase())) {
                filteredList.add(semester);
            }
        }

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