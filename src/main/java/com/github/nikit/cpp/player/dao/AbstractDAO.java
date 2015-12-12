package com.github.nikit.cpp.player.dao;

import android.content.Context;
import com.raizlabs.android.dbflow.config.FlowManager;

/**
 * Created by nik on 12.12.15.
 */
public class AbstractDAO {
    public static void init(Context context){
        FlowManager.init(context);
    }
}
