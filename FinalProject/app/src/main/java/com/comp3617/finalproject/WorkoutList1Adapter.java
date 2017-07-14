package com.comp3617.finalproject;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.comp3617.finalproject.database.Database;
import com.comp3617.finalproject.model.Workout;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.ArraySwipeAdapter;

import java.util.List;
import java.util.Locale;

import io.realm.Realm;

/**
 * Created by andrewchen on 2017-07-14.
 */

public class WorkoutList1Adapter extends ArraySwipeAdapter<Workout> {
    private Context ctx;
    private List<Workout> workoutList;

    WorkoutList1Adapter(Context context, List<Workout> workouts){
        super(context,0,workouts);
        this.ctx = context;
        this.workoutList = workouts;
    }


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

        RelativeLayout main = (RelativeLayout) rowView.findViewById(R.id.list_mainLayout);
        LinearLayout delete = (LinearLayout) rowView.findViewById(R.id.bottom_wrapper);
        list.addDrag(SwipeLayout.DragEdge.Left,delete);
        list.addSwipeListener(onSwipe(list, main,delete,workout,position));


        main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),RecordActivity.class);
                intent.putExtra("fileName",workout.getFileName());
                ctx.startActivity(intent);
            }
        });
        
        workoutName.setText(workout.getName());

        durationText.setText(convertTime(workout.getDuration()));
        distanceText.setText(String.format(Locale.getDefault(),"%.2fkm",workout.getDistance()));
        dateText.setText(workout.getDate().toString());
        return rowView;
    }

    private String convertTime(Long time){
        int hours = time.intValue() / 3600;
        int minutes = time.intValue() / 60 % 60;
        int seconds = time.intValue() % 60;
        return String.format(Locale.getDefault(),"%02d:%02d:%02d", hours, minutes, seconds);
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.list_swipeLayout;
    }

    SwipeLayout.SwipeListener onSwipe(final SwipeLayout list, final RelativeLayout main, final LinearLayout delete, final Workout workout, final int position){
        return new SwipeLayout.SwipeListener() {
            @Override
            public void onStartOpen(SwipeLayout layout) {

            }

            @Override
            public void onOpen(SwipeLayout layout) {
                main.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        list.close(true);
                    }
                });

                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Database database = new Database(Realm.getDefaultInstance());
                        database.deleteWorkout(workout.getFileName());
                        workoutList.remove(position);
                        notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void onStartClose(SwipeLayout layout) {

            }

            @Override
            public void onClose(SwipeLayout layout) {
                main.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(view.getContext(),RecordActivity.class);
                        intent.putExtra("fileName",workout.getFileName());
                        ctx.startActivity(intent);
                    }
                });
            }

            @Override
            public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {

            }

            @Override
            public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {

            }
        };
    }
}
