package com.example.svmc_habit_tracker.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.svmc_habit_tracker.MainActivity;
import com.example.svmc_habit_tracker.R;
import com.example.svmc_habit_tracker.adapter.DlogIconsRVAdapter;
import com.example.svmc_habit_tracker.data.Database;
import com.example.svmc_habit_tracker.data.model.Habit;
import com.example.svmc_habit_tracker.exception.NotEmptyException;
import com.example.svmc_habit_tracker.fragment.home.FragHome;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;

public class AddActivity extends AppCompatActivity implements View.OnClickListener {
    private MainActivity mainActivity;
    private Database database;
    private ImageView btniBack, addIcon;
    private EditText edTen, edGoal, edUnit, edDes, edReminder;
    private TextView tvTitle, edStart, edEnd;
    private Button btnComplete;

    ArrayList<Habit> arrayList = new ArrayList<>();

    private final String TAG = "addActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        // anh xa
        initView();

        edTen.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0){
                    tvTitle.setText(s);
                }
                if (start == 0){
                    tvTitle.setText("New Habit");
                }
//                Toast.makeText(AddActivity.this, "start: "+start+"\n"+"before: "
//                        + before+"\ncount: "+count, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btniBack.setOnClickListener(this);
        btnComplete.setOnClickListener(this);
        edStart.setOnClickListener(this);
        edEnd.setOnClickListener(this);
        addIcon.setOnClickListener(this);
    }

    private void initView() {
        database = new Database(this);
        btniBack = findViewById(R.id.add_btni_back);
        addIcon = findViewById(R.id.addIcon);
        edTen = findViewById(R.id.addTen);
        edDes = findViewById(R.id.addDes);
        edStart = findViewById(R.id.addStart);
        edEnd = findViewById(R.id.addEnd);
        edGoal = findViewById(R.id.addGoal);
        edReminder = findViewById(R.id.addReminder);
        edUnit = findViewById(R.id.addUnit);
        tvTitle = findViewById(R.id.addTitle);
        btnComplete = findViewById(R.id.addBtnComplete);

        Calendar calendar = Calendar.getInstance();
        String today = calendar.get(Calendar.DAY_OF_MONTH)+"/"+(calendar.get(Calendar.MONTH)+1)+"/"+calendar.get(Calendar.YEAR);
        edStart.setText(today);

//        edGoal.setText(10+"");
        edUnit.setText("count");




    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.add_btni_back:
                addBtnBack();
                break;
            case R.id.addBtnComplete:
                addHabitComplete();
                break;
            case R.id.addStart:
                datePicker(edStart);
                break;
            case R.id.addEnd:
                datePicker(edEnd);
                break;
            case R.id.addIcon:
                ShowIconsDialog();
                break;
        }
    }

    private void addBtnBack() {
        Intent intent = new Intent();
        intent.putExtra("ArrayListAddFromAddActivity", arrayList);
        setResult(2222, intent);
        finish();
    }

    // show icon dialog
    private void ShowIconsDialog() {
        final int[] iconSelected = {0};

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_icons_box, null);
        dialogBuilder.setView(dialogView);

        RecyclerView dialogIconRecyclerView = dialogView.findViewById(R.id.dlogI_rv);
        Button dialogIcon_btnSelect = dialogView.findViewById(R.id.dlogI_btnSelect);
        DlogIconsRVAdapter dlogIconsRVAdapter = new DlogIconsRVAdapter(this);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 7);
        dialogIconRecyclerView.setLayoutManager(gridLayoutManager);
        dialogIconRecyclerView.setAdapter(dlogIconsRVAdapter);

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        // bat su kien tich button select
        dialogIcon_btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (iconSelected[0] != 0)
                    addIcon.setImageResource(iconSelected[0]);
                alertDialog.dismiss();
            }
        });

        // bat su kien click item
        dlogIconsRVAdapter.setOnItemDialogIconClickListener(new DlogIconsRVAdapter.OnItemDialogIconClickListener() {
            @Override
            public void OnItemClick(int position) {
               // Toast.makeText(AddActivity.this, "click icon", Toast.LENGTH_SHORT).show();
                iconSelected[0] = dlogIconsRVAdapter.getIcon(position);
            }
        });
    }

    // chon ngay
    public void datePicker(TextView eddate){
        Calendar newCalendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int yy, int mm, int dd) {
                eddate.setText(dd+"/"+(mm+1)+"/"+yy);
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    public void addHabitComplete(){
        Calendar calendar = Calendar.getInstance();
        String today = calendar.get(Calendar.DAY_OF_MONTH)+"/"+(calendar.get(Calendar.MONTH)+1)+"/"+calendar.get(Calendar.YEAR);

        String ten = "", start = "", end ="", unit="", des="", reminder="";
        long goal = 0;

        try {

        ten = edTen.getText().toString();
        start = edStart.getText().toString() + "";
        end = edEnd.getText().toString() + "";
        goal = Long.parseLong(edGoal.getText().toString());
        unit = edUnit.getText().toString() + "";
        des = edDes.getText().toString() + "";
        reminder = edReminder.getText().toString()+"";
//                ImageView imageView = addIcon;
//        int icon = addIcon.getDrawable();

        //Exception
            arrayList = database.insertHabitStartToEndReturnArrayList(ten, ImageView_to_Byte(addIcon), start, end, des, unit, goal, reminder, today);
            // xu ly UI
            // tra data arraylist ve cho FragHome, MainActivity
            Intent intent = new Intent();
            intent.putExtra("ArrayListAddFromAddActivity", arrayList);
            setResult(2222, intent);


//                    database.insertHabit(ten, ImageView_to_Byte(addIcon));
//            Toast.makeText(AddActivity.this, "today: "+today
//                    +"\nstart: "+start
//                    +"\nend: "+end, Toast.LENGTH_SHORT).show();

//            database.insertHabit(ten, ImageView_to_Byte(addIcon), start, end, des, unit, goal, reminder, today);

//            database.insertHabitStartToEnd(ten, ImageView_to_Byte(addIcon), start, end, des, unit, goal, reminder, today);



            Toast.makeText(AddActivity.this, "Add Habit successful", Toast.LENGTH_SHORT).show();

            finish();

            //
            database.insertHabitStartToEnd(ten, ImageView_to_Byte(addIcon), start, end, des, unit, goal, reminder, today);

//            database.insertHabit(ten, ImageView_to_Byte(addIcon), start, end, des, unit, goal, reminder, today);
        } catch (NumberFormatException e){
            Toast.makeText(this, "Goals is a number", Toast.LENGTH_SHORT).show();
            edGoal.requestFocus();

        } catch (Exception e){
            if (ten.isEmpty()){
                Toast.makeText(AddActivity.this, "Please input a habit name", Toast.LENGTH_SHORT).show();
                edTen.requestFocus();
            }else if (unit.isEmpty()){
                Toast.makeText(AddActivity.this, "Please input a unit", Toast.LENGTH_SHORT).show();
                edUnit.requestFocus();
            }
        }

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
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart: ");
    }
}