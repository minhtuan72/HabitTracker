package com.example.svmc_habit_tracker.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.svmc_habit_tracker.R;
import com.example.svmc_habit_tracker.data.model.Habit;

import java.util.ArrayList;

public class FhomeRecycleViewAdapter extends RecyclerView.Adapter<FhomeRecycleViewAdapter.ViewHolder> {
    private Context context;

    // list, count, constructor, itemClickListener
    private ArrayList<Habit> habitArrayList = new ArrayList<>();

    // set onItemRecycleViewClickListener
    private OnFhomeRecycleViewItemClickListener onFhomeRecycleViewItemClickListener;

    public void setOnFhomeRecycleViewItemClickListener(OnFhomeRecycleViewItemClickListener onFhomeRecycleViewItemClickListener) {
        this.onFhomeRecycleViewItemClickListener = onFhomeRecycleViewItemClickListener;
    }



    public FhomeRecycleViewAdapter(Context context) {
        this.context = context;
        this.habitArrayList = habitArrayList;

    }

    public void addHabit(Habit habit){
        habitArrayList.add(habit);
        notifyDataSetChanged();
    }

    public void setHabitArrayList(ArrayList<Habit> list){
        habitArrayList.clear();
        habitArrayList.addAll(list);
        notifyDataSetChanged();
    }

    public Habit getHabit(int position){
        return habitArrayList.get(position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.irv_fhome_habit_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // gán giá trị ở đây
//        if (habitArrayList != null){?
            Habit habit = habitArrayList.get(position);
//            Toast.makeText(context, habit.getNameHabit(), Toast.LENGTH_SHORT).show();
            // CASE: anh bi null - de anh default
            if (habit.getImageHabit() == null){
//                Toast.makeText(context, "anh bi null" + habit.getImageHabit(), Toast.LENGTH_SHORT).show();
                //
                holder.iconHabit.setImageResource(R.drawable.img);
            }else{
                Bitmap bitmap = BitmapFactory.decodeByteArray(habit.getImageHabit(),0, habit.getImageHabit().length);
                holder.iconHabit.setImageBitmap(bitmap);
            }
            holder.nameHabit.setText(habit.getNameHabit());
            holder.doneHabit.setText(habit.getDone()+"/"+habit.getGoal());
            holder.unitHabit.setText(habit.getUnit());


    }

    @Override
    public int getItemCount() {
        if (habitArrayList != null)
            return habitArrayList.size();
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView iconHabit;
        TextView nameHabit, doneHabit, unitHabit;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            // ánh xạ components.
            iconHabit = itemView.findViewById(R.id.irv_Icon);
            nameHabit = itemView.findViewById(R.id.irv_Name);
            doneHabit = itemView.findViewById(R.id.irv_done);
            unitHabit = itemView.findViewById(R.id.irv_unit);
        }

        @Override
        public void onClick(View view) {
            if (onFhomeRecycleViewItemClickListener != null){
                onFhomeRecycleViewItemClickListener.onItemClick(getAdapterPosition());
            }
        }
    }

    public interface OnFhomeRecycleViewItemClickListener{
        public void onItemClick(int position);
    }
}
