package com.studentplanner.studentplanner.fragments;

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

import com.studentplanner.studentplanner.DatabaseHelper;
import com.studentplanner.studentplanner.R;
import com.studentplanner.studentplanner.adapters.CourseworkAdapter;
import com.studentplanner.studentplanner.models.Coursework;
import com.studentplanner.studentplanner.utils.Helper;

import org.apache.commons.text.WordUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class ReminderFragment extends Fragment {
    private View view;
    private Context context;

    private RecyclerView recyclerView;
    private CourseworkAdapter adapter;
    private List<Coursework> list;


    public ReminderFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        initFragment(inflater, container);

        DatabaseHelper db = DatabaseHelper.getInstance(context);
        Helper.getIntentMessage(context, getActivity().getIntent().getExtras());

        list = db.getUpComingCourseworkByMonth();
        recyclerView = view.findViewById(R.id.reminderRecyclerView);
        buildRecyclerView();

        return view;
    }

    private void initFragment(LayoutInflater inflater, ViewGroup container) {
        view = inflater.inflate(R.layout.fragment_reminder, container, false);
        context = getContext();

        getActivity().setTitle(Helper.getReminderTitle());
        setHasOptionsMenu(true);

    }


    private void buildRecyclerView() {
        if (list.size() > 0) {
            adapter = new CourseworkAdapter(list, context, getActivity());
            LinearLayoutManager manager = new LinearLayoutManager(context);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(manager);
            recyclerView.setAdapter(adapter);
            return;
        }

        view.findViewById(R.id.emptyCourseworkReminderImage).setVisibility(View.VISIBLE);
        view.findViewById(R.id.emptyCourseworkReminderText).setVisibility(View.VISIBLE);


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
        List<Coursework> filteredList = new ArrayList<>();

        for (Coursework coursework : list) {
            String name = coursework.getTitle().toLowerCase();
            if (name.contains(text.toLowerCase())) {
                filteredList.add(coursework);
            }
        }
        if (filteredList.isEmpty()) {
            Toast.makeText(context, "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {
            adapter.filterList(filteredList);
        }
    }

}