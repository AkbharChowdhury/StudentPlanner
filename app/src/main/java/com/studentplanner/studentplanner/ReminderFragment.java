package com.studentplanner.studentplanner;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_reminder, container, false);
        context = getContext();

        TextView f = (TextView) view.findViewById(R.id.textapple);
        f.setText("Hello world");
        // Inflate the layout for this fragment
        return view;
    }
}