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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_reminder,container,false);
        context = getContext();
       TextView f = v.findViewById(R.id.emptyCourseworkReminderText); //Note this line
        f.setText("Looks like you have no coursework today");
        v.findViewById(R.id.fab_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "JS", Toast.LENGTH_SHORT).show();
            }
        });
        Toast.makeText(context, "JS", Toast.LENGTH_SHORT).show();



//other tasks you need to do
        return v;
//        view = inflater.inflate(R.layout.fragment_reminder, container, false);
//        context = getContext();
//        getActivity().setTitle("My Reminders");
//        view.findViewById(R.id.fab_add).setOnClickListener(v -> Toast.makeText(context, "Hello world", Toast.LENGTH_LONG).show());
//
////        TextView f = (TextView) view.findViewById(R.id.textapple);
////        f.setText("Hello world");
//        // Inflate the layout for this fragment
//        return view;
    }
}