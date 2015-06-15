package com.example.ratslab.home;

import android.app.usage.UsageEvents;
import android.os.Debug;
import android.util.EventLog;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.widget.RelativeLayout;

/**
 * Created by ratslab on 02/06/2015.
 */
public class AppTouchListener implements View.OnTouchListener {

    int iconSize;
    public AppTouchListener(int width)
    {
        iconSize=width;
    }
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(MainActivity.isEditable) {

            int xTouch = (int) event.getRawX();
            int yTouch = (int) event.getRawY();
            int x = (int) MainActivity.removeApp.getX();
            int y = (int) MainActivity.removeApp.getY();
            int width = MainActivity.removeApp.getWidth();
            int height = MainActivity.removeApp.getHeight();

            int xLowerlimit = -(iconSize / 4 - x - width / 2);
            int xUupperLimit = iconSize / 4 + x + width / 2;
            int yLowerLimit = iconSize / 4 - y - height / 2;
            int yUpperLimit = iconSize / 4 + y + height / 2;
            int homeLowerX =(int)MainActivity.launcherHomeView.getX();
            int homeLowerY =(int)MainActivity.launcherHomeView.getY();
            int homeUpperX =v.getWidth()-(homeLowerX + MainActivity.launcherHomeView.getWidth());
            int homeUpperY= v.getHeight()-(homeLowerY + MainActivity.launcherHomeView.getHeight());
            switch (event.getAction()) {
                case MotionEvent.ACTION_MOVE:

                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(iconSize, iconSize);
                    lp.leftMargin = xTouch - iconSize / 2;
                    lp.topMargin = yTouch - iconSize / 2;
                    if (((xLowerlimit < xTouch) && (xTouch < xUupperLimit)) && ((yLowerLimit < yTouch) && (yTouch < yUpperLimit))) {
                        v.setBackgroundResource(android.R.color.holo_red_dark);
                    } else {
                        v.setBackgroundResource(0);
                    }

                    if(lp.leftMargin<homeLowerX)lp.leftMargin=2;
                    v.setLayoutParams(lp);
                    return true;

                case MotionEvent.ACTION_UP:


                    if (((xLowerlimit < xTouch) && (xTouch < xUupperLimit)) && ((yLowerLimit < yTouch) && (yTouch < yUpperLimit))) {
                        Log.e("On Cancle", "====================================================================================================");
                        Log.e("Cordinates", "Touch(" + xTouch + "," + yTouch + ") LowerLimit (" + xLowerlimit + "," + yLowerLimit + ") UpperLimit (" + xUupperLimit + "," + yUpperLimit + ") ");
                        Log.e("x", "" + x);
                        MainActivity.launcherHomeView.removeView(v);


                    }
                    MainActivity.isEditable=false;
                    MainActivity.removeApp.setVisibility(View.INVISIBLE);
                    break;


            }
        return true;
        }
        else
        return false;
    }
}
