package com.example.svmc_habit_tracker;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;

import com.example.svmc_habit_tracker.data.model.Habit;
import com.example.svmc_habit_tracker.fragment.home.FragHome;
import com.example.svmc_habit_tracker.notification.MyBinder;
import com.example.svmc_habit_tracker.notification.MyService;
import com.example.svmc_habit_tracker.activity.AddActivity;
import com.example.svmc_habit_tracker.data.Database;
import com.example.svmc_habit_tracker.fragment.habit.FragHabit;
import com.example.svmc_habit_tracker.fragment.home.FragMainHome;
import com.example.svmc_habit_tracker.fragment.statistic.FragStatistic;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    SelectedBundle selectedBundle;

    public void setOnBundleSelected(SelectedBundle selectedBundle) {
        this.selectedBundle = selectedBundle;
    }

    public ArrayList<Habit> arrayListFromMain = new ArrayList<>();
    private String TAG = "MAINACTIVITY";
    final public int REQUEST_CODE_FOR_RESULT = 2222;



    private Database database = new Database(this);

    private BottomNavigationView mBottomNavi;
    private FrameLayout mframelayout;

    //Bat dau khai bao Notification:
    MyService mMyService;

    ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {

            MyBinder binder = (MyBinder) iBinder;
            mMyService = (MyService) binder.getmService();
            mMyService.makeLoad();

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.d(TAG, "DISCONNECT");
        }
    };
    //Het khai bao Notification

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);

        database.createTable();
//        arrayListFromMain = database.getListAllHabit();
        arrayListFromMain = database.getListHabitToday();

        setContentView(R.layout.activity_main);
        // function initView() - Ánh xạ components.
        initView();



    }

    private void naviHomeChecked() {

        // chuyen qua Add Intent
        Intent intent = new Intent(MainActivity.this, AddActivity.class);
        startActivityForResult(intent, REQUEST_CODE_FOR_RESULT);
//        mBottomNavi.setVisibility(View.GONE);
    }

    private void initView() {
//        database.deleteTable();
//        database.deleteDB();

//        database.deleteTable();

        // khởi tạo
        mBottomNavi = findViewById(R.id.mNaviBottomBar);

        mframelayout = findViewById(R.id.mFrameLayout);
        //
        mBottomNavi.getMenu().findItem(R.id.menuNaviHome).setChecked(true);
        getSupportFragmentManager().beginTransaction().replace(R.id.mFrameLayout, new FragMainHome()).commit();

        mBottomNavi.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.menuNaviHome:
                        if (mBottomNavi.getMenu().findItem(R.id.menuNaviHome).isChecked()){
//                            getSupportFragmentManager().beginTransaction().replace(R.id.mFrameLayout, new FragHabit()).commit();

                            naviHomeChecked();
                        }
                        else{
                            getSupportFragmentManager().beginTransaction().replace(R.id.mFrameLayout, new FragMainHome()).commit();
                            item.setChecked(true);
                        }
                        break;
                    case R.id.menuNaviHabit:
                        getSupportFragmentManager().beginTransaction().replace(R.id.mFrameLayout, new FragHabit()).commit();
                        mBottomNavi.getMenu().findItem(R.id.menuNaviHabit).setChecked(true);
                        mBottomNavi.getMenu().findItem(R.id.menuNaviStatistic).setChecked(false);
                        mBottomNavi.getMenu().findItem(R.id.menuNaviHome).setChecked(false);
                        break;
                    case R.id.menuNaviStatistic:
                        getSupportFragmentManager().beginTransaction().replace(R.id.mFrameLayout, new FragStatistic()).commit();
                        mBottomNavi.getMenu().findItem(R.id.menuNaviHabit).setChecked(false);
                        mBottomNavi.getMenu().findItem(R.id.menuNaviStatistic).setChecked(true);
                        mBottomNavi.getMenu().findItem(R.id.menuNaviHome).setChecked(false);
                        break;
                }
                return true;
            }
        });



    }

//// ≡≡≡≡≡≡≡≡ service
//    @Override
//    protected void onStart() {
//        super.onStart();
//        Intent intent = new Intent(this, MyService.class);
//        bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        unbindService(mServiceConnection);
//    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_FOR_RESULT){
            ArrayList<Habit> arrayList;
            if (null != data.getSerializableExtra("ArrayListAddFromAddActivity")){
                arrayList = (ArrayList<Habit>) data.getSerializableExtra("ArrayListAddFromAddActivity");
                arrayListFromMain.addAll(arrayList);
//                selectedBundle.onBundleSelect("bundle");
                // update ngay hom nay

                Bundle bundle = new Bundle();
                bundle.putString("edttext", "From Activity");
// set Fragmentclass Arguments
                FragHome fragobj = new FragHome();
                fragobj.setArguments(bundle);

              //  Toast.makeText(this, "received arraylist", Toast.LENGTH_SHORT).show();

            }
//            for (Habit i:arrayList){
//                Toast.makeText(this, arrayList.size()
//                        +"\nDate: "+i.getDate(), Toast.LENGTH_SHORT).show();
//            }
            /// Lam sao de fraghomelay duoc du lieu

        }else{
            Toast.makeText(this, "nothing", Toast.LENGTH_SHORT).show();
        }
    }

    public ArrayList<Habit> getData(){
        return arrayListFromMain;
    }

    public void setData(ArrayList<Habit> habitListFromHome) {
        arrayListFromMain.clear();
        arrayListFromMain.addAll(habitListFromHome);
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        Log.i(TAG, "onCreate: MainActivity");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart: MainActivity");
        Intent intent = new Intent(this, MyService.class);
        bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop: ");
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.i(TAG, "onDetachedFromWindow: ");
    }

    



    public interface SelectedBundle {
        void onBundleSelect(String bundle);
    }
}