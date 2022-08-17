package com.example.svmc_habit_tracker.notification;

import android.app.Service;
import android.os.Binder;

public class MyBinder extends Binder {
    Service mService;

    public MyBinder(Service service){
        this.mService = service;
    }


    public Service getmService() {
        return mService;
    }
}
