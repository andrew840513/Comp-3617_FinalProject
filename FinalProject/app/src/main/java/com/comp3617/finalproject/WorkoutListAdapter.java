package com.comp3617.finalproject;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.comp3617.finalproject.model.Workout;
import com.daimajia.swipe.SwipeLayout;

import java.util.List;
import java.util.Locale;

/**
 * Created by andrewchen on 2017-07-14.
 */

public class WorkoutListAdapter extends ArrayAdapter<Workout> {
    private final Context ctx;
    private List<Workout> workoutList;

    public WorkoutListAdapter(Context ctx, List<Workout> workoutList){
        super(ctx,0,workoutList);
        this.ctx = ctx;
        this.workoutList = workoutList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final View rowView;
        final Workout workout = workoutList.get(position);
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.list_prototype_layout,parent,false);
        }else{
            rowView = convertView;
        }
        TextView workoutName = (TextView) rowView.findViewById(R.id.list_name);
        TextView durationText = (TextView) rowView.findViewById(R.id.list_duration);
        TextView distanceText = (TextView) rowView.findViewById(R.id.list_distance);
        TextView dateText = (TextView) rowView.findViewById(R.id.list_date);
        SwipeLayout list = (SwipeLayout) rowView.findViewById(R.id.list_swipeLayout);

        list.addDrag(SwipeLayout.DragEdge.Left,rowView.findViewById(R.id.bottom_wrapper));

        workoutName.setText(workout.getName());
        durationText.setText(convertTime(workout.getDuration()));
        distanceText.setText(String.format(Locale.getDefault(),"%.2fkm",workout.getDistance()));
        dateText.setText(workout.getDate().toString());

        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),RecordActivity.class);
                intent.putExtra("fileName",workout.getFileName());
            }
        });
        return rowView;
    }

    private String convertTime(Long time){
        int hours = time.intValue() / 3600;
        int minutes = time.intValue() / 60 % 60;
        int seconds = time.intValue() % 60;
        return String.format(Locale.getDefault(),"%02d:%02d:%02d", hours, minutes, seconds);
    }
}
