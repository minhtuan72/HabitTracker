package com.example.svmc_habit_tracker.fragment.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import com.example.svmc_habit_tracker.MainActivity;
import com.example.svmc_habit_tracker.R;
import com.example.svmc_habit_tracker.activity.UpdateActivity;
import com.example.svmc_habit_tracker.bottomsheet.AddTimeBottomSheetDialog;
import com.example.svmc_habit_tracker.data.Database;
import com.example.svmc_habit_tracker.data.model.Habit;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.util.ArrayList;
import java.util.Locale;

public class FragItemHome extends Fragment implements FragmentResultListener {
  private static final String ADD_NOTE_REQUEST="ADD_NOTE";
  private static final String ADD_TIME_REQUEST="ADD_TIME";
  public static int Check = 0;

  private long START_TIME_IN_MILLIS = 10000;
  private long mTimeLeftInMillis = START_TIME_IN_MILLIS;

  private CountDownTimer mCountDownTimer;
  private TextView Display;
  private Button mButtonStartPause;
  private ImageButton mButtonReset;
  private Button mButtonSwitch;
  private ImageButton mButtonBTAddTime;
  private Button mButtonBTAddNote;

  private ImageView img_back;
  private ImageView img_menu;
  private boolean mTimerRunning;
  private boolean mTimerIsCountdown=true;

  private CircularProgressBar circularProgressBar;
  private int i=0;

  private FragHome.FragmentChangeListener mFragmentChangeListener;

  private long done = 0;


  public static Habit receivedHabit; // habit duoc truyen tu fragHome
  private Database database;
  private ArrayList<Habit> habitListFromHome = new ArrayList<>(); // habit list duoc truyen tu fraghome

  private final String TAG = "FragItemHome";
  BottomNavigationView navBar;


  public void setFragmentChangeListener(FragHome.FragmentChangeListener fragmentChangeListener) {
    mFragmentChangeListener = fragmentChangeListener;
  }
  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view=inflater.inflate(R.layout.frag_itemhome, container, false);
    view.setBackgroundColor(Color.WHITE);

    navBar = getActivity().findViewById(R.id.mNaviBottomBar);
    navBar.setVisibility(View.GONE);

    database = new Database(getActivity());


    Display =view.findViewById(R.id.progress_text);
    mButtonStartPause=view.findViewById(R.id.btn_start_pause);
    mButtonReset=view.findViewById(R.id.btn_reset);
    mButtonBTAddTime=view.findViewById(R.id.btn_bottomsheet_addTime);
    img_back=view.findViewById(R.id.img_back);
    img_menu=view.findViewById(R.id.img_menu);


    //circle progress bar
    circularProgressBar = view.findViewById(R.id.circularProgressBar);
    circularProgressBar.setProgress(i);
    circularProgressBar.setRoundBorder(true);
    circularProgressBar.setBackgroundProgressBarColor(Color.parseColor("#FFDCC7DF"));
    circularProgressBar.setProgressBarColorStart(Color.parseColor("#6e5fa1"));
    circularProgressBar.setProgressBarColorEnd(Color.parseColor("#7ac292"));

    // NHAN data tu fragHome
    Log.i(TAG, "onCreateView: nhan dataFrom1");
    getParentFragmentManager().setFragmentResultListener("dataFrom1", this, new FragmentResultListener() {
      @Override
      public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
        receivedHabit = (Habit) result.getSerializable("fh1");
        habitListFromHome = (ArrayList<Habit>) result.getSerializable("habitList");
     //   Toast.makeText(view.getContext(), receivedHabit.getNameHabit().toString(), Toast.LENGTH_SHORT).show();




        //Khoi tao giao dien neu don vi khong phai la phut
        if(receivedHabit != null && !receivedHabit.getUnit().equals("m")) { // dang bi null o day
          circularProgressBar.setProgressMax(receivedHabit.getGoal());
//          Display.setText("0");
          Display.setText(receivedHabit.getDone()+"");
          circularProgressBar.setProgress(receivedHabit.getDone());
//      mButtonStartPause.getCompoundDrawables();
          mButtonStartPause.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_done,0,0,0);
          mButtonStartPause.setText("Done");
          mButtonBTAddTime.setImageResource(R.drawable.ic_add_disable);
          mButtonBTAddTime.setEnabled(false);
        } else {
          START_TIME_IN_MILLIS = receivedHabit.getGoal()*60*1000;
          mTimeLeftInMillis = START_TIME_IN_MILLIS;
          circularProgressBar.setProgressMax(100);
          updateCountDownText();
        }

      }
    });
    return view;
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);








    img_back.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        navBar.setVisibility(View.VISIBLE);
        getFragmentManager().popBackStack();
      }
    });
    img_menu.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        // Initializing the popup menu and giving the reference as current context
        PopupMenu popupMenu = new PopupMenu(getActivity(), img_menu);

        // Inflating popup menu from popup_menu.xml file
        popupMenu.getMenuInflater().inflate(R.menu.menu_itemhome_frag, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
          @Override
          public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()){
              // Btn DELETE
              case R.id.menuDelete:
//                navBar.setVisibility(View.VISIBLE);
//                Toast.makeText(getContext(), "menu delete", Toast.LENGTH_SHORT).show();
                try {
                  if (receivedHabit == null){
                    Toast.makeText(getContext(), "receivedHabit is null", Toast.LENGTH_SHORT).show();
                  }
                  ///≡≡-- xu ly UI
                  int idHabitDelete = receivedHabit.getIdHabit();
                  String nameHabit = receivedHabit.getNameHabit();

                  // xoa habits trong habitlist
                  ArrayList<Habit> toRemoveList = new ArrayList<>();
                  for (Habit i:habitListFromHome){
                    if (i.getNameHabit().equals(nameHabit)){
                      toRemoveList.add(i);
                    }
                  }
                  habitListFromHome.removeAll(toRemoveList);

                  // truyen data sang fragHome
                  Bundle bundle = new Bundle();
                  bundle.putSerializable("listAfterDeleted", habitListFromHome);
                  getParentFragmentManager().setFragmentResult("fromFragItemDel", bundle);

                  MainActivity activity = (MainActivity) getActivity();
                  activity.setData(habitListFromHome);

                  /// xu ly DATABASE
                  database.deleteHabit(receivedHabit.getIdHabit());
                  Toast.makeText(getContext(), "Delete Successful", Toast.LENGTH_SHORT).show();



                  // tro ve fragMainHome
                  getParentFragmentManager().popBackStack();
                }catch (Exception e){
                  Toast.makeText(getContext(), "delete exception:"+ e.toString(), Toast.LENGTH_SHORT).show();
                }
                break;
              //btn EDIT
              case R.id.menuEdit:
//                Toast.makeText(getContext(), "menu edit", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), UpdateActivity.class);
                intent.putExtra("updateHabit", receivedHabit);
                startActivityForResult(intent, 1);
                break;
            }
            return true;
          }
        });
        // Showing the popup menu
        popupMenu.show();
      }
    });
    registerForContextMenu(img_menu);
    mButtonStartPause.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (!receivedHabit.getUnit().toString().equals("m")) {
          // Neu don vi khong phai phut thi done va save data tai day
          circularProgressBar.setProgress(receivedHabit.getGoal());
          Display.setText(receivedHabit.getGoal()+"");
          done = receivedHabit.getGoal();
       //   Toast.makeText(getActivity(), "Done and save data hear!", Toast.LENGTH_SHORT).show();
          database.updateHabitTestDone(new Habit(receivedHabit.getIdHabit(), receivedHabit.getNameHabit(), receivedHabit.getImageHabit(), receivedHabit.getTimeStart(), receivedHabit.getTimeEnd(), receivedHabit.getDescription(), receivedHabit.getUnit(), receivedHabit.getGoal(), done, receivedHabit.getReminder(), receivedHabit.getDate()));

        } else {
          //Neu don vi la phut thi xu li binh thuong
          if (mButtonStartPause.getText().toString().equals("Save data")) {
          //  Toast.makeText(getContext(), "save data", Toast.LENGTH_SHORT).show();
            //thuc hien save data
            mButtonStartPause.setEnabled(false);
            mButtonStartPause.setText("start");
            mButtonStartPause.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_play, 0, 0, 0);
          } else {
            if (mTimerRunning) {
              pauseTimer();
            } else {
              startTimer();
            }
          }
        }
      }


    });

    mButtonReset.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        resetTimer();
        done = 0;
        database.updateHabitTestDone(new Habit(receivedHabit.getIdHabit(), receivedHabit.getNameHabit(), receivedHabit.getImageHabit(), receivedHabit.getTimeStart(), receivedHabit.getTimeEnd(), receivedHabit.getDescription(), receivedHabit.getUnit(), receivedHabit.getGoal(), done, receivedHabit.getReminder(), receivedHabit.getDate()));
      }
    });


    mButtonBTAddTime.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        BottomSheetDialogFragment addTime = new AddTimeBottomSheetDialog();
        addTime.show(getFragmentManager(), addTime.getTag());
      }
    });



//    updateCountDownText();
    getParentFragmentManager().setFragmentResultListener(ADD_TIME_REQUEST, getViewLifecycleOwner(), this);
//    getParentFragmentManager().setFragmentResultListener(ADD_NOTE_REQUEST, getViewLifecycleOwner(), this);
  }

  private void startTimer() {
    mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
      @Override
      public void onTick(long l) {
        mTimeLeftInMillis = l;
        i++;
        circularProgressBar.setProgressWithAnimation((int) i * 100 / ((int) START_TIME_IN_MILLIS / 1000), 1000L);
        updateCountDownText();
      }

      @Override
      public void onFinish() {
        i++;
        circularProgressBar.setProgressWithAnimation(100, 500L);
        mTimerRunning = false;
//        mButtonStartPause.setEnabled(false);
        mButtonBTAddTime.setEnabled(false);
        mButtonStartPause.setText("Save data");
        Check = 1;
      }
    };
    mCountDownTimer.start();
    mTimerRunning = true;
    mButtonStartPause.setText("Pause");
    mButtonStartPause.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_pause, 0,0,0);

  }
  private void pauseTimer() {
    i--;
    mCountDownTimer.cancel();
    mTimerRunning=false;
    mButtonStartPause.setText("Start");
    mButtonStartPause.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_play, 0,0,0);

  }
  private void resetTimer() {
    if (!receivedHabit.getUnit().equals("m")) {
      //Neu don vi khong phai la phut
      Display.setText("0");
      circularProgressBar.setProgress(0);
    } else {
      //Neu don vi la phut thi xu li reset thoi gian binh thuong
      if(!mTimerRunning) {
        mButtonStartPause.setText("start");
        mButtonStartPause.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_play, 0, 0, 0);
        mButtonBTAddTime.setEnabled(true);
        mButtonStartPause.setEnabled(true);
        i = 0;
        circularProgressBar.setProgressWithAnimation(0, 1000L); // =1s
        mTimeLeftInMillis = START_TIME_IN_MILLIS;
        updateCountDownText();
      }
    }
  }
  private void updateCountDownText() {
    int minutes = (int) (mTimeLeftInMillis/1000)/60;
    int seconds = (int) (mTimeLeftInMillis/1000)%60;
    String timerLeftFormatted= String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
    Display.setText(timerLeftFormatted);
  }

  @Override
  public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
    if(requestKey.equals(ADD_TIME_REQUEST)) {
      int timeAdd = result.getInt("time");
      mTimeLeftInMillis+=timeAdd*1000;
      START_TIME_IN_MILLIS+=timeAdd*1000;
      //i+=timeAdd;
      //Toast.makeText(getContext(), ""+mTimeLeftInMillis, Toast.LENGTH_SHORT).show();
      circularProgressBar.setProgressWithAnimation((int) i * 100 / ((int) START_TIME_IN_MILLIS / 1000), 1000L);
      updateCountDownText();
    }
//    if(requestKey.equals(ADD_NOTE_REQUEST)) {
//      String note = result.getString("note");
//      Toast.makeText(getContext(), note, Toast.LENGTH_SHORT).show();
//    }
  }

  @Override
  public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
    if(v.getId()==R.id.img_menu) {
      getActivity().getMenuInflater().inflate(R.menu.menu_itemhome_frag,menu);
    }
    super.onCreateContextMenu(menu, v, menuInfo);
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
  public void onDestroy() {
    super.onDestroy();
    Log.i(TAG, "onDestroy: ");
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
  public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
  //  Toast.makeText(getActivity(), "ok", Toast.LENGTH_SHORT).show();
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == 1) {
    //  Toast.makeText(getActivity(), "ok 1", Toast.LENGTH_SHORT).show();
      if(resultCode == Activity.RESULT_OK){
      //  Toast.makeText(getActivity(), "ok 2", Toast.LENGTH_SHORT).show();
        receivedHabit = (Habit) data.getExtras().getSerializable("result");
        START_TIME_IN_MILLIS = receivedHabit.getGoal()*60*1000;
        mTimeLeftInMillis = START_TIME_IN_MILLIS;
        i=0;
        circularProgressBar.setProgressWithAnimation(0,1000L);
        updateCountDownText();
      }
      if (resultCode == Activity.RESULT_CANCELED) {
        // Write your code if there's no result
      }
    }
  }
}
