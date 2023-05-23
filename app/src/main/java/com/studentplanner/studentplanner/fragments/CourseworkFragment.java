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
import com.studentplanner.studentplanner.adapters.CourseworkAdapter;
import com.studentplanner.studentplanner.addActivities.AddCourseworkActivity;
import com.studentplanner.studentplanner.databinding.FragmentCourseworkBinding;
import com.studentplanner.studentplanner.models.Coursework;
import com.studentplanner.studentplanner.utils.Helper;

import java.util.ArrayList;
import java.util.List;


public class CourseworkFragment extends Fragment {
    private Context context;
    private Activity activity;

    private RecyclerView recyclerView;
    private CourseworkAdapter adapter;
    private List<Coursework> list;
    private FragmentCourseworkBinding binding;


    public CourseworkFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initFragment();
        binding = FragmentCourseworkBinding.inflate(inflater, container, false);
        binding.fabAdd.setOnClickListener((v -> Helper.goToActivity(activity, AddCourseworkActivity.class)));


        DatabaseHelper db = DatabaseHelper.getInstance(context);
        Helper.getIntentMessage(context, activity.getIntent().getExtras());

        list = db.getCoursework();
        recyclerView = binding.recyclerView;
        buildRecyclerView();
        return binding.getRoot();
    }

    private void initFragment() {
        context = getContext();
        activity = getActivity();
        activity.setTitle(context.getString(R.string.my_coursework));
        setHasOptionsMenu(true);

    }


    private void buildRecyclerView() {
        if (list.size() > 0) {
            adapter = new CourseworkAdapter(list, context, activity);
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
        List<Coursework> filteredList = new ArrayList<>();

        for (Coursework coursework : list) {
            String name = coursework.getTitle().toLowerCase();
            if (name.contains(text.toLowerCase())) {
                filteredList.add(coursework);
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