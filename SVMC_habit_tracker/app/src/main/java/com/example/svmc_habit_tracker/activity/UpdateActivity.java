package com.example.svmc_habit_tracker.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.svmc_habit_tracker.R;
import com.example.svmc_habit_tracker.adapter.DlogIconsRVAdapter;
import com.example.svmc_habit_tracker.data.Database;
import com.example.svmc_habit_tracker.data.model.Habit;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;

public class UpdateActivity extends AppCompatActivity implements View.OnClickListener{
    private Database database;
    private ImageView btniBack, addIcon;
    private EditText edTen, edStart, edEnd, edGoal, edUnit, edDes, edReminder, edDone, edDate;
    private TextView tvTitle;
    private Button btnComplete;
    private Habit receivedHabit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        // anh xa
        initView();

        Intent  intent = getIntent();
        receivedHabit = (Habit) intent.getSerializableExtra("updateHabit");

        tvTitle.setText(receivedHabit.getNameHabit());
        edTen.setText(receivedHabit.getNameHabit());
        Bitmap bitmap = BitmapFactory.decodeByteArray(receivedHabit.getImageHabit(),0, receivedHabit.getImageHabit().length);
        addIcon.setImageBitmap(bitmap);
        edGoal.setText(receivedHabit.getGoal()+"");
        edUnit.setText(receivedHabit.getUnit());
        edStart.setText(receivedHabit.getTimeStart());
        edEnd.setText(receivedHabit.getTimeEnd());
        edDes.setText(receivedHabit.getDescription());
        edReminder.setText(receivedHabit.getReminder());
        edDone.setText(receivedHabit.getDone()+"");
        edDate.setText(receivedHabit.getDate());

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
        btniBack = findViewById(R.id.update_btni_back);
        addIcon = findViewById(R.id.updateIcon);
        edTen = findViewById(R.id.updateTen);
        edDes = findViewById(R.id.updateDes);
        edStart = findViewById(R.id.updateStart);
        edEnd = findViewById(R.id.updateEnd);
        edGoal = findViewById(R.id.updateGoal);
        edReminder = findViewById(R.id.updateReminder);
        edUnit = findViewById(R.id.updateUnit);
        edDone = findViewById(R.id.updateDone);
        edDate = findViewById(R.id.updateDate);
        tvTitle = findViewById(R.id.updateTitle);
        btnComplete = findViewById(R.id.updateBtnComplete);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.update_btni_back:
                finish();
                break;
            case R.id.updateBtnComplete:
                addHabitComplete();
                break;
            case R.id.updateStart:
                datePicker(edStart);
                break;
            case R.id.updateEnd:
                datePicker(edEnd);
                break;
            case R.id.updateIcon:
                ShowIconsDialog();
                break;

        }
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
                //Toast.makeText(getApplicationContext(), "click icon", Toast.LENGTH_SHORT).show();
                iconSelected[0] = dlogIconsRVAdapter.getIcon(position);
            }
        });
    }

    // chon ngay
    public void datePicker(EditText eddate){
        Calendar newCalendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int yy, int mm, int dd) {
                eddate.setText(dd+"/"+mm+"/"+yy);
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    public void addHabitComplete(){
        Calendar calendar = Calendar.getInstance();
        String today = calendar.get(Calendar.DAY_OF_MONTH)+"/"+(calendar.get(Calendar.MONTH)+1)+"/"+calendar.get(Calendar.YEAR);

        String ten = edTen.getText().toString();
        String start = edStart.getText().toString() + "";
        String end = edEnd.getText().toString() + "";
        long goal = Integer.parseInt(edGoal.getText().toString());
        String unit = edUnit.getText().toString() + "";
        String des = edDes.getText().toString() + "";
        String reminder = edReminder.getText().toString()+"";
        String date = edDate.getText().toString()+"";
        long done = Long.parseLong(edDone.getText().toString());
        try {
            // int idHabit, String nameHabit, byte[] imageHabit, String timeStart, String timeEnd, String description, String unit, long goal
            // MAIN
//            database.updateHabit(new Habit(receivedHabit.getIdHabit(), ten, ImageView_to_Byte(addIcon), start, end, des, unit, goal, reminder));

            // TEST DONE
//            constructor public Habit(int idHabit, String nameHabit, byte[] imageHabit, String timeStart, String timeEnd, String description, String unit, long goal, long done, String reminder, String date) {
// chua tao constructor

//            Habit(int idHabit, String nameHabit, byte[] imageHabit, String timeStart, String timeEnd, String description, String unit, long goal, long done, String reminder, String date)
           // Toast.makeText(this, receivedHabit.getIdHabit()+" "+ receivedHabit.getDone(), Toast.LENGTH_SHORT).show();
            Habit _resulthb=new Habit(receivedHabit.getIdHabit(), ten, ImageView_to_Byte(addIcon), start, end, des, unit, goal, done, reminder, date);
            database.updateHabitTestDone(_resulthb);

            Intent returnIntent = new Intent();
            returnIntent.putExtra("result",_resulthb);
            setResult(Activity.RESULT_OK,returnIntent);
            finish();
//            database.updateHabitAll(new Habit(receivedHabit.getIdHabit(), ten, ImageView_to_Byte(addIcon), start, end, des, unit, goal, done, reminder, date));

            Toast.makeText(this, "Update Successful", Toast.LENGTH_SHORT).show();

        } catch (Exception e){
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
        finish();
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
}