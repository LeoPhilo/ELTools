package com.projects.leophilo.eltools.tools;

import android.content.Context;
import android.content.SharedPreferences;

public class SPTool {

    public static SharedPreferences getAppSP(Context context) {
        return context.getSharedPreferences("ELTools", Context.MODE_PRIVATE);
    }

}
