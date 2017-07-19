package com.comp3617.finalproject.database;

import com.comp3617.finalproject.model.Workout;

import java.util.ArrayList;

import io.realm.Realm;

/**
 * Created by andrewchen on 2017-07-13.
 */

public class Database {
    private Realm realm;

    public Database(Realm realm){
        this.realm = realm;
    }

    public ArrayList<Workout> getAllWorkout() {
        ArrayList<Workout> workouts = new ArrayList<>();
        for (Workout workout: realm.where(Workout.class).findAll()) {
            if(workout.getFileName() !=null){
                workouts.add(workout);
            }
        }
        return workouts;
    }

    public Workout getWorkout(final String fileName) {

        final Workout searchTask = realm.where(Workout.class).equalTo("fileName",fileName).findFirst();
        return searchTask;
    }

    public void addNewWorkout(final Workout workout) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Workout newWorkout = realm.createObject(Workout.class);
                newWorkout.setFileName(workout.getFileName());
                newWorkout.setName(workout.getName());
                newWorkout.setDate(workout.getDate());
                newWorkout.setDistance(workout.getDistance());
                newWorkout.setDuration(workout.getDuration());
            }
        });
    }

    public void deleteWorkout(final String fileName) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(Workout.class).equalTo("fileName",fileName).findFirst().deleteFromRealm();
            }
        });
    }
}
