package com.eat_it.root.lets_eat.common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.eat_it.root.lets_eat.model.User;

public class common {
    public static User currentuser;
    public static final String DELETE="Delete";
    public static final String USER_KEY="User";
    public static final String PASS_KEY="Password";
    public static final String Phone_key="Phone";

    public static boolean Isconnectedtointernet(Context context){

        ConnectivityManager manager=(ConnectivityManager)context.getSystemService(context.CONNECTIVITY_SERVICE);
        if(manager !=null){
            NetworkInfo[] networkInfo=manager.getAllNetworkInfo();
            if(networkInfo !=null){
                for (int i=0;i<networkInfo.length;i++){
                    if (networkInfo[i].getState()==NetworkInfo.State.CONNECTED){
                        return true;
                    }

                }
            }
        }
        return false;
    }

}
