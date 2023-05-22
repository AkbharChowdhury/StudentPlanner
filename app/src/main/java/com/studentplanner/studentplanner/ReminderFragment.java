package com.studentplanner.studentplanner;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class ReminderFragment extends Fragment {
    private View view;
    private Context context;



    public ReminderFragment() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    private void initFragment(LayoutInflater inflater, ViewGroup container) {
        view = inflater.inflate(R.layout.fragment_reminder, container, false);
        context = getContext();
        getActivity().setTitle(context.getString(R.string.my_reminders));
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initFragment(inflater, container);
        return view;



    }
}