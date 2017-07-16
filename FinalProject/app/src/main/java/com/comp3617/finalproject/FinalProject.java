package com.comp3617.finalproject;

import android.app.Application;

import com.comp3617.finalproject.model.Workout;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by andrewchen on 2017-07-13.
 */

public class FinalProject extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration realmConfig = new RealmConfiguration.Builder()
                .initialData(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.createObject(Workout.class);
                    }})
                .build();
        //TODO remove deleteReam
        //Realm.deleteRealm(realmConfig);
        Realm.setDefaultConfiguration(realmConfig);
    }
}