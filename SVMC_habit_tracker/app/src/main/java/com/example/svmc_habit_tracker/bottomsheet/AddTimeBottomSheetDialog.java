package com.example.svmc_habit_tracker.bottomsheet;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.svmc_habit_tracker.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class AddTimeBottomSheetDialog extends BottomSheetDialogFragment implements View.OnKeyListener {
    private static final String ADD_TIME_REQUEST="ADD_TIME";
    private EditText edAddTime;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.addtime_bottomsheet, container, false);
//        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        edAddTime=view.findViewById(R.id.edTimeAdd);
        edAddTime.setOnKeyListener(this);
        return view;
    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        if (i==KeyEvent.KEYCODE_ENTER) { //Whenever you got user click enter. Get text in
            //Toast.makeText(getContext(), edAddTime.getText(), Toast.LENGTH_SHORT).show();
            try {
                int m = Integer.parseInt(edAddTime.getText().toString());
                Bundle bd = new Bundle();
                bd.putInt("time", m);
                requireActivity().getSupportFragmentManager().setFragmentResult(ADD_TIME_REQUEST, bd);
                dismiss();
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Input Invalid", Toast.LENGTH_SHORT).show();
            }
        }
        return false;
    }
}
