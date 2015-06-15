package com.example.ratslab.home;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.AdapterView;

/**
 * Created by ratslab on 02/06/2015.
 */
public class AppClickListener implements AdapterView.OnItemClickListener{

    Context c;
    MainActivity.Pac[] pacs;
    PackageManager pm;
    public AppClickListener(Context c, MainActivity.Pac[] pacs,PackageManager pm)
    {
        this.c=c;
        this.pacs=pacs;
        this.pm=pm;
        MainActivity.appLaunchable=true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(MainActivity.appLaunchable) {
            Intent launchIntent = new Intent(Intent.ACTION_MAIN);
            launchIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            ComponentName cn= new ComponentName(pacs[position].packageName,pacs[position].name);
            launchIntent.setComponent(cn);
            c.startActivity(launchIntent);
        }
    }

}
