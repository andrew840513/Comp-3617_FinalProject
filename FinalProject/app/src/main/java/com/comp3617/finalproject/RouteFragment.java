package com.comp3617.finalproject;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.comp3617.finalproject.database.Database;

import io.realm.Realm;

public class RouteFragment extends Fragment {
	private Realm realm = Realm.getDefaultInstance();
	private Database database = new Database(realm);
	public RouteFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View fragmentView = inflater.inflate(R.layout.fragment_route, container, false);

		ListView workoutListView = (ListView) fragmentView.findViewById(R.id.route_list_view);
		WorkoutListAdapter adapter = new WorkoutListAdapter(getActivity().getApplicationContext(), database.getAllWorkout());
		workoutListView.setAdapter(adapter);
		return fragmentView;
	}
}