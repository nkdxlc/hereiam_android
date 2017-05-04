package com.hereiam.app;

import android.app.Application;

import com.vuforia.samples.entity.User;

/**
 * Created by Administrator on 2017/5/2.
 */

public class MyApp extends Application {

    private User user;

    public User getUser(){
        return  user;
    }

    public  void  setUser(User user){
        this.user = user;
    }
}
