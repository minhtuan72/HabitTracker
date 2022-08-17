package com.example.svmc_habit_tracker.fragment.habit;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.svmc_habit_tracker.R;
import com.example.svmc_habit_tracker.data.Database;
import com.example.svmc_habit_tracker.data.model.Habit;

import java.util.ArrayList;

public class HabitRvAdapter extends RecyclerView.Adapter<HabitRvAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Habit> habitArrayList = new ArrayList<>();
    private Database database;

    private OnHabitIconClickListener onHabitIconClickListener;

    public void setOnHabitIconClickListener(OnHabitIconClickListener onHabitIconClickListener) {
        this.onHabitIconClickListener = onHabitIconClickListener;
    }

    public HabitRvAdapter(Context context) {
        this.context = context;
        this.habitArrayList = new ArrayList<>();
        this.database = new Database(context);
    }

    public void setHabitList(ArrayList<Habit> list){
        habitArrayList.clear();
        habitArrayList.addAll(list);
        notifyDataSetChanged();
    }


    public String getNameHabit(int position){
        return habitArrayList.get(position).getNameHabit();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.irv_habit_image_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Habit habit = habitArrayList.get(position);
        Bitmap bitmap = BitmapFactory.decodeByteArray(habit.getImageHabit(),0, habit.getImageHabit().length);
        holder.iconHabit.setImageBitmap(bitmap);
    }

    @Override
    public int getItemCount() {
        return habitArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView iconHabit;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iconHabit = itemView.findViewById(R.id.irv_habit_img_item);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (onHabitIconClickListener != null){
                onHabitIconClickListener.onItemHabitClick(getAdapterPosition());
            }
        }
    }

    public interface OnHabitIconClickListener{
        void onItemHabitClick(int position);
    }

}
