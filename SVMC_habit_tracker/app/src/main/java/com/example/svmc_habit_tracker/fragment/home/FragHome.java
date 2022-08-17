package com.example.svmc_habit_tracker.fragment.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.svmc_habit_tracker.MainActivity;
import com.example.svmc_habit_tracker.R;
import com.example.svmc_habit_tracker.adapter.DlogIconsRVAdapter;
import com.example.svmc_habit_tracker.adapter.FhomeRecycleViewAdapter;
import com.example.svmc_habit_tracker.data.Database;
import com.example.svmc_habit_tracker.data.model.Habit;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class FragHome extends Fragment implements FhomeRecycleViewAdapter.OnFhomeRecycleViewItemClickListener {

    final public String TAG = "FRAGHOME";
    private TextView textDay;
    private TabLayout tabLayout;
    private RecyclerView recyclerViewHabitList;
    private FhomeRecycleViewAdapter recycleViewAdapter;
    private ArrayList<Habit> habitArrayList = new ArrayList<>();

    private Database database;

    private boolean isPassOnAttach = false;

    private FragmentChangeListener mFragmentChangeListener;

    public void setFragmentChangeListener(FragmentChangeListener fragmentChangeListener) {
        mFragmentChangeListener = fragmentChangeListener;
    }

    private String strtext ="";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_home, container, false);

        BottomNavigationView navBar = getActivity().findViewById(R.id.mNaviBottomBar);
        navBar.setVisibility(View.VISIBLE);

        Log.i(TAG, "onCreateView: home");

        // NHAN data tu fragItemHome sau khi Deleted
        getParentFragmentManager().setFragmentResultListener("fromFragItemDel", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                habitArrayList.clear();
                habitArrayList = (ArrayList<Habit>) result.getSerializable("listAfterDeleted");
            }
        });

//        try {
//            strtext = getArguments().getString("edttext");
//        }catch (Exception e){
//            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
//        }


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        database.insertListNameAndStartTimeGroupByNameHabitOnGoing();
        initView(view);

//        Toast.makeText(getActivity(), strtext, Toast.LENGTH_SHORT).show();
        }

    private void initView(View itemview) {






        recyclerViewHabitList = itemview.findViewById(R.id.fhoRv);
        recycleViewAdapter = new FhomeRecycleViewAdapter(itemview.getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(itemview.getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerViewHabitList.setLayoutManager(linearLayoutManager);
        // get list
        if (habitArrayList != null){
//            habitArrayList.clear();
//            habitArrayList.addAll(database.getListHabit());

//            recycleViewAdapter.setHabitArrayList(database.getListHabitToday());

//            recycleViewAdapter.setHabitArrayList(database.getListAllHabit());
            recycleViewAdapter.setHabitArrayList(habitArrayList);

//            Toast.makeText(itemview.getContext().getApplicationContext(), habitArrayList.size(), Toast.LENGTH_SHORT).show();
        }


        recyclerViewHabitList.setAdapter(recycleViewAdapter);
        recycleViewAdapter.setOnFhomeRecycleViewItemClickListener(this);
    }

    //imageview to byte
    public byte[] ImageView_to_Byte(ImageView img){
        BitmapDrawable drawable = (BitmapDrawable) img.getDrawable();
        Bitmap bmp = drawable.getBitmap();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    @Override
    public void onItemClick(int position){
//        Toast.makeText(getActivity().getApplicationContext(), "clicked: " + position, Toast.LENGTH_SHORT).show();
//        Toast.makeText(getActivity().getApplicationContext(), "clicked: " + position, Toast.LENGTH_SHORT).show();
        Habit habit = recycleViewAdapter.getHabit(position);

        // Gui DATA sang fragItemHome
        Bundle bundle = new Bundle();
        bundle.putSerializable("fh1", habit);
        bundle.putSerializable("habitList", habitArrayList);
        getParentFragmentManager().setFragmentResult("dataFrom1", bundle);
//        getParentFragmentManager().setFragmentResult("daataFrom1,");

        FragItemHome fih=new FragItemHome();
        mFragmentChangeListener.replaceFragment(fih);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: fraghome");
//        if (database.getListHabitToday() != null){
//            recycleViewAdapter.setHabitArrayList(database.getListHabitToday());
//        }

        MainActivity activity = (MainActivity) getActivity();
        ArrayList<Habit> arrayList = activity.getData();
        // Neu khong load tu DB thi moi lay du lieu cua main

//            habitArrayList.clear();
//            habitArrayList.addAll(arrayList);


//            isPassOnAttach = false;
//
            habitArrayList.clear();
            habitArrayList.addAll(database.getListHabitToday());



        if (habitArrayList != null){
            recycleViewAdapter.setHabitArrayList(habitArrayList);
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i(TAG, "onDestroyView: ");
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.i(TAG, "onAttach: ");
        database = new Database(context);
        /*
        // tai du lieu tu database
        database = new Database(context);
        habitArrayList = database.getListAllHabit();
        isPassOnAttach = true;
         */
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "onStart: ");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG, "onStop: ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: ");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate: ");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i(TAG, "onDetach: ");
    }

    public interface FragmentChangeListener {
        public void replaceFragment(Fragment fragment);
    }
}
