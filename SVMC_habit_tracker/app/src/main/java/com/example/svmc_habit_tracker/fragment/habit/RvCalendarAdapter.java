package com.example.svmc_habit_tracker.fragment.habit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.svmc_habit_tracker.R;
import com.example.svmc_habit_tracker.data.model.Habit;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class RvCalendarAdapter extends RecyclerView.Adapter<RvCalendarAdapter.CalendarViewholder> {

    // đổ dữ liệu vào rvCalendar. Cần danh sách Habit
    private ArrayList<Habit> specificArrayList = new ArrayList<>();

    private ArrayList<String> daysOfMonth;
    private OnItemCalendarClickListener onItemCalendarClickListener;

    Context context;

    public void setOnItemCalendarClickListener(OnItemCalendarClickListener onItemCalendarClickListener) {
        this.onItemCalendarClickListener = onItemCalendarClickListener;
    }

    public RvCalendarAdapter(Context context) {
        this.context = context;
        daysOfMonth = new ArrayList<>();
        specificArrayList = new ArrayList<>();
    }

    public void setDaysOfMonthList(ArrayList<String> list){
        daysOfMonth.clear();
        daysOfMonth.addAll(list);
        notifyDataSetChanged();
    }

    public void setSpecificArrayList(ArrayList<Habit> list){
        specificArrayList.clear();
        specificArrayList.addAll(list);
//        Toast.makeText(context, "setspecific"+
//                "\nitem: "+list.get(0).getDone(), Toast.LENGTH_SHORT).show();

        notifyDataSetChanged();
    }

    public String getItem(int position){
        return daysOfMonth.get(position);
    }

    @NonNull
    @Override
    public CalendarViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.calendar_cell_habit, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = (int) (parent.getHeight() * 0.166666666);
        return new CalendarViewholder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull CalendarViewholder holder, int position) {
        Calendar calendar = Calendar.getInstance();

        String standardDate = daysOfMonth.get(position);


//        Toast.makeText(context, standardDate, Toast.LENGTH_SHORT).show();
        holder.tvDayofMonth.setText(standardDatetoDay(standardDate));



        if (standardDate.equalsIgnoreCase(setStandardDateFormat(calendar.getTime()))){
            //        if (daysOfMonth.get(position).equalsIgnoreCase(String.valueOf(calendar.get(Calendar.DATE))))
            holder.tvDayofMonth.setBackgroundResource(R.drawable.today_bg_red);
            holder.progressBar.setProgress(0);
            holder.progressBar.setMax(100);
//            Toast.makeText(context, standardDate, Toast.LENGTH_SHORT).show();
        }
        else{
            holder.tvDayofMonth.setBackgroundResource(R.drawable.today_bg);
        }

//        Toast.makeText(context, "specificArrayList: "+ specificArrayList.size(), Toast.LENGTH_SHORT).show();

        if (specificArrayList != null && specificArrayList.size() != 0) {
            for (Habit i : specificArrayList) {
              //  Toast.makeText(context, "in loop, size: "+specificArrayList.size(), Toast.LENGTH_SHORT).show();
//                Toast.makeText(context, formatDatefromddMYYYYtoYYYYMdd(i.getDate()).toString()
//                        +"\n"+standardDate, Toast.LENGTH_SHORT).show();

                if (formatDatefromddMYYYYtoYYYYMdd(i.getDate()).equalsIgnoreCase(standardDate)) { // i.getDate = 23/7/2022
                 //   Toast.makeText(context, "in loop calendaradapter:", Toast.LENGTH_SHORT).show();
                                    holder.progressBar.setProgress((int) i.getDone());
                    holder.progressBar.setProgress((int) i.getDone());
                    holder.progressBar.setMax((int) i.getGoal());
                    break;
                }
            }
        }

/*
        Database database = new Database(context.getApplicationContext());

        if (database.hasMonaninDay(standardDate)){
            holder.tvDayofMonth.setBackgroundResource(R.drawable.today_bg_yellow);
            if (standardDate.equalsIgnoreCase(setStandardDateFormat(calendar.getTime()))){
                holder.tvDayofMonth.setBackgroundResource(R.drawable.today_bg_orange);
            }
        }

 */
    }

    public String setStandardDateFormat(Date date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-M-d");
        return simpleDateFormat.format(date);
    }

    public String formatDatefromddMYYYYtoYYYYMdd(String date){
        String[] array = date.split("/");
        String day = array[0];
        String month = array[1];
        String year = array[2];
        return year+"-"+month+"-"+day;
    }

    public String standardDatetoDay(String standardDate){
        String[] array = standardDate.split("-");
        String day = array[2];
        String month = array[1];
        String year = array[0];
        return day;
    }

    public String standardDateYYYYMddToddMYYYY(String standardDate){
        String[] array = standardDate.split("-");
        String day = array[2];
        String month = array[1];
        String year = array[0];
        return day+"/"+month+"/"+year;
    }

    @Override
    public int getItemCount() {
        return daysOfMonth.size();
    }

    public class CalendarViewholder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ProgressBar progressBar;
        TextView tvDayofMonth;
        public CalendarViewholder(@NonNull View itemView) {
            super(itemView);
            tvDayofMonth = itemView.findViewById(R.id.ir_habit_cell);
            progressBar = itemView.findViewById(R.id.ir_habit_progressbar);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (onItemCalendarClickListener != null)
                onItemCalendarClickListener.onItemClick(getAdapterPosition(), (String) tvDayofMonth.getText(), getItem(getAdapterPosition()));
        }
    }

    public interface OnItemCalendarClickListener{
        public void onItemClick(int position, String daytext, String dayStandard);
    }
}


