package com.example.svmc_habit_tracker.fragment.home;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.svmc_habit_tracker.R;

public class FragMainHome extends Fragment implements FragHome.FragmentChangeListener {
  private final String TAG = "fragMainHome";
  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view=inflater.inflate(R.layout.frag_mainhome, container, false);
    FragHome fh=new FragHome();
    fh.setFragmentChangeListener(this);
    getFragmentManager().beginTransaction().add(R.id.fl_container, fh, fh.getTag()).commit();
    return view;
  }


  @Override
  public void replaceFragment(Fragment fragment) {
    getFragmentManager().beginTransaction().replace(R.id.fl_container, fragment, fragment.getTag()).addToBackStack(null).commit();
  }

  @Override
  public void onAttach(@NonNull Context context) {
    super.onAttach(context);
    Log.i(TAG, "onAttach: ");
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Log.i(TAG, "onCreate: ");
  }

  @Override
  public void onStop() {
    super.onStop();
    Log.i(TAG, "onStop: ");
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    Log.i(TAG, "onDestroyView: ");
  }

  @Override
  public void onDetach() {
    super.onDetach();
    Log.i(TAG, "onDetach: ");
  }
}
