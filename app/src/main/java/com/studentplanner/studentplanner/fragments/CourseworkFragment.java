package com.studentplanner.studentplanner.fragments;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.checkbox.MaterialCheckBox;
import com.studentplanner.studentplanner.DatabaseHelper;
import com.studentplanner.studentplanner.R;
import com.studentplanner.studentplanner.adapters.CourseworkAdapter;
import com.studentplanner.studentplanner.addActivities.AddCourseworkActivity;
import com.studentplanner.studentplanner.databinding.FragmentCourseworkBinding;
import com.studentplanner.studentplanner.models.Coursework;
import com.studentplanner.studentplanner.models.SearchCoursework;
import com.studentplanner.studentplanner.utils.Helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;


public class CourseworkFragment extends Fragment {
    private CheckBox checkBoxSort;

    private SearchCoursework search;
    private Spinner txtPriority;
    private Spinner txtCompletionStatus;


    private Context context;
    private Activity activity;

    private RecyclerView recyclerView;
    private CourseworkAdapter adapter;
    private List<Coursework> list;
    private DatabaseHelper db;
    private FragmentCourseworkBinding binding;
    private final ActivityResultLauncher<Intent> startForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {

        if (result.getResultCode() == RESULT_OK) {
            getCoursework();
        }

    });


    public CourseworkFragment() {

    }

    private void setCompletionDropdown() {
        txtCompletionStatus.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, context.getResources().getStringArray(R.array.completion_array_search)));

    }

    private void setPriorityDropdown() {
        Deque<String> deque = new LinkedList<>(Arrays.asList(context.getResources().getStringArray(R.array.priority_array)));
        deque.addFirst(context.getString(R.string.any_priority));
        List<String> priorityArray = new ArrayList<>(deque);

        txtPriority.setAdapter(new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_spinner_dropdown_item,
                priorityArray
        ));



    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initFragment();
        binding = FragmentCourseworkBinding.inflate(inflater, container, false);
        checkBoxSort = binding.chkSort;

        checkBoxSort.setOnCheckedChangeListener((buttonView, isChecked) -> {
            list.sort(isChecked ? Coursework.sortDeadlineDesc : Coursework.sortDeadlineAsc);
            adapter.notifyDataSetChanged();
        });


        txtPriority = binding.txtPriority;
        txtCompletionStatus = binding.txtCompletionStatus;

        setPriorityDropdown();
        setCompletionDropdown();

        txtPriority.setSelection(0, false);
        txtCompletionStatus.setSelection(0, false);


        txtPriority.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                search.setPriority(txtPriority.getAdapter().getItem(position).toString());
                List<Coursework> filteredList = search.filterResults(checkBoxSort.isChecked());
                checkEmptyResults(filteredList);
                adapter.filterList(filteredList);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        txtCompletionStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {
                    search.setDefaultStatus(true);
                    List<Coursework> filteredList = search.filterResults(checkBoxSort.isChecked());
                    checkEmptyResults(filteredList);
                    adapter.filterList(search.filterResults(checkBoxSort.isChecked()));
                    return;
                }
                search.setDefaultStatus(false);
                boolean isCompleted = txtCompletionStatus.getAdapter().getItem(position).toString().equalsIgnoreCase("Completed");
                search.setCompleted(isCompleted);
                List<Coursework> filteredList = search.filterResults(checkBoxSort.isChecked());
                checkEmptyResults(filteredList);
                adapter.filterList(filteredList);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        binding.fabAdd.setOnClickListener(v -> startForResult.launch(new Intent(getActivity(), AddCourseworkActivity.class)));
        recyclerView = binding.recyclerView;


        db = DatabaseHelper.getInstance(context);
        Helper.getIntentMessage(context, activity.getIntent().getExtras());
        getCoursework();

        return binding.getRoot();
    }

    private void getCoursework() {
        list = db.getCoursework();
//        list.sort(Coursework.sortDeadlineAsc);
        list.sort(Coursework.sortDeadlineAsc);


        search = new SearchCoursework(context, list);
        buildRecyclerView();
    }


    private void initFragment() {
        context = getContext();
        activity = getActivity();
        activity.setTitle(context.getString(R.string.my_coursework));
        setHasOptionsMenu(true);

    }

    private void buildRecyclerView() {
        if (!list.isEmpty()) {
            adapter = new CourseworkAdapter(list, context, startForResult);
            LinearLayoutManager manager = new LinearLayoutManager(context);

            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(manager);

            recyclerView.setAdapter(adapter);
            return;
        }

        showEmptyResults();


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
                    filterTitle(newText);
                    return false;
                }
            });

        }
    }


    private void filterTitle(String title) {
        search.setTitle(title);
        List<Coursework> filteredList = search.filterResults(checkBoxSort.isChecked());

        checkEmptyResults(filteredList);
        adapter.filterList(filteredList);


    }

    private void checkEmptyResults(List<Coursework> filteredList) {
        if (filteredList.isEmpty()) {
            Helper.shortToastMessage(context, context.getString(R.string.no_data_found));
            showEmptyResults();

        }


    }

    private void showEmptyResults() {
        binding.emptyImage.setVisibility(View.VISIBLE);
        binding.emptyText.setVisibility(View.VISIBLE);

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}