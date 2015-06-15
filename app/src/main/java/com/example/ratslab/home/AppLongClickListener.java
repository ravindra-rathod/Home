package com.example.ratslab.home;

import android.app.ActionBar;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.test.InstrumentationTestCase;
import android.test.TouchUtils;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.TouchDelegate;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.EventListener;
import java.util.MissingFormatArgumentException;

/**
 * Created by ratslab on 02/06/2015.
 */
public class AppLongClickListener implements AdapterView.OnItemLongClickListener{

    Context c;
    MainActivity.Pac[] pac;
    public AppLongClickListener(Context c,MainActivity.Pac[] pac)
    {
        this.c=c;
        this.pac=pac;
    }
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        MainActivity.appLaunchable=false;
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(view.getWidth(),view.getHeight());
        lp.leftMargin=(int) view.getX();
        lp.topMargin=(int)view.getY();
        LayoutInflater li = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout ll=(LinearLayout)li.inflate(R.layout.app_view,null);
        ((ImageView)ll.findViewById(R.id.app_icon)).setImageDrawable(((ImageView) view.findViewById(R.id.app_icon)).getDrawable());
        ((TextView)ll.findViewById(R.id.app_name)).setText(((TextView) view.findViewById(R.id.app_name)).getText());

        ll.setOnTouchListener(new AppTouchListener(view.getWidth()));
        ll.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                MainActivity.isEditable=true;
                MainActivity.removeApp.setVisibility(View.VISIBLE);

                return true;
            }
        });
        String data[] = new String[2];
        data[0]= pac[position].packageName;
        data[1]=pac[position].name;
        ll.setTag(data);
        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] data;
                data = (String[]) v.getTag();
                Intent launchIntent = new Intent(Intent.ACTION_MAIN);
                launchIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                ComponentName cn = new ComponentName(data[0], data[1]);
                launchIntent.setComponent(cn);
                c.startActivity(launchIntent);
            }
        });

        MainActivity.launcherHomeView.addView(ll, lp);

        MainActivity.menuClose();
        return true;
    }

}
