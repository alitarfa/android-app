package com.add.smarthome.ui.dialog;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.add.smarthome.R;
import com.add.smarthome.ui.home.MainActivity;

public class Settings extends DialogFragment {

    public interface OnChangeAction {
        public void onHandelSW(boolean status);
    }

    private OnChangeAction onChangeAction;

    public Settings(MainActivity mainActivity) {
        onChangeAction = mainActivity;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_filter_layout, container, false);
        Switch aSwitch = view.findViewById(R.id.notificationSw);
        SharedPreferences prefs = getContext().getSharedPreferences("com.example.app", Context.MODE_PRIVATE);
        aSwitch.setChecked(prefs.getBoolean("notification",true));
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    onChangeAction.onHandelSW(true);
                }else {
                    onChangeAction.onHandelSW(false);
                }
            }
        });
        return  view;
    }
}
