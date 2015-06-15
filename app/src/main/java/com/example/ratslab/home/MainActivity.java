package com.example.ratslab.home;

import android.app.Activity;
import android.app.WallpaperInfo;
import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.service.wallpaper.WallpaperService;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.io.IOException;
import java.util.List;


public class MainActivity extends Activity implements View.OnClickListener{

    static Button menuButton,menuButtonCollapse;
    static RelativeLayout menu,launcherHomeView;
    public static Animation bottomIn,bottomOut,rotate;
    static EditText searchApp;
    static LinearLayout searchBox;
    Pac pacs[];
    PackageManager pm;
    GridView appList;
    AppsListAdapter appsListAdapter;
    public static ImageView removeApp;
    public static boolean appLaunchable=true,isEditable=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        launcherHomeView=(RelativeLayout)findViewById(R.id.launcherHomeView);
        /*
        * Setting Background Wallpaper
        * function definition is given bellow
        * */
        setDefaultWallpaper();

        appList=(GridView)findViewById(R.id.appList);



        /* Setting animation check each anim to see what it does */
        bottomOut = AnimationUtils.loadAnimation(MainActivity.this,R.anim.abc_slide_out_bottom);
        bottomIn = AnimationUtils.loadAnimation(MainActivity.this,R.anim.abc_slide_in_bottom);
        rotate = AnimationUtils.loadAnimation(MainActivity.this,R.anim.clock_wise_half_rotation);
        /* Assigning Buttons and other core functions */
        removeApp=(ImageView)findViewById(R.id.remove_app);
        menuButton=(Button)findViewById(R.id.menuButton);
        menuButtonCollapse=(Button)findViewById(R.id.menuButtonCollapse);
        searchApp =(EditText)findViewById(R.id.search_app);
        searchBox=(LinearLayout)findViewById(R.id.searchBox);
        menu =(RelativeLayout)findViewById(R.id.menu);
        menu.setVisibility(View.INVISIBLE);
        searchBox.setVisibility(View.INVISIBLE);
        removeApp.setVisibility(View.INVISIBLE);

        menuButtonCollapse.setVisibility(View.INVISIBLE);
        menuButtonCollapse.setClickable(false);
        menuButton.setOnClickListener(this);
        menuButtonCollapse.setOnClickListener(this);


        setPacs();

        /*
        * Updating applist as application is installed or unInstalled
         */
        IntentFilter filter =new IntentFilter();
        filter.addAction(Intent.ACTION_PACKAGE_ADDED);
        filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        filter.addAction(Intent.ACTION_PACKAGE_CHANGED);
        filter.addDataScheme("package");
        registerReceiver(new PacReceiver(),filter);
    }

    private void setDefaultWallpaper() {
        getWindow().setBackgroundDrawable(getWallpaper());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClick(View v)
    {
    int id = v.getId();
        switch (id)
        {
            case R.id.menuButton:
                menuOpen();
                break;

            case R.id.menuButtonCollapse:
                menuClose();
                break;
        }
    }

public void setPacs(){
     /*
        * Code For Setting Application List to Grid View Start
        */

    pm = getPackageManager();
        /*
        * Setting application List , function body is below
        */

    /*
    * Getting Applist with its Main Activity and Launcher
    * ResolveInfo is used to get All apps  from package manager qualifying the category
    * Applist is transfered to Array of pacs which object of Class Pac
    * see Class Pac bellow for reference
    */
    final Intent mainIntent= new Intent(Intent.ACTION_MAIN,null);
    mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
    List<ResolveInfo> pacList= pm.queryIntentActivities(mainIntent,0);
    pacs =new Pac[pacList.size()];
    for(int i=0;i<pacList.size();i++)
    {
        pacs[i]= new Pac();
        pacs[i].label=pacList.get(i).activityInfo.loadLabel(pm).toString();
        pacs[i].packageName=pacList.get(i).activityInfo.packageName;
        pacs[i].name=pacList.get(i).activityInfo.name;
        pacs[i].icon=pacList.get(i).activityInfo.loadIcon(pm);
    }

     /* Embedding AppList into Grid view */
    appsListAdapter = new AppsListAdapter(MainActivity.this,pacs);
    appList.setAdapter(appsListAdapter);

        /* Open App on Click function Bellow ,See Custom AppClickListener for reference */
    appList.setOnItemClickListener(new AppClickListener(MainActivity.this, pacs, pm));
    appList.setOnItemLongClickListener(new AppLongClickListener(MainActivity.this,pacs));
        /*
        * Code For Setting Application List to Grid View End
         */

}

    public static void menuOpen(){

        appLaunchable=true;
        launcherHomeView.setVisibility(View.INVISIBLE);
        menuButtonCollapse.setVisibility(View.VISIBLE);
        menuButtonCollapse.startAnimation(rotate);
        menu.setVisibility(View.VISIBLE);
        menu.startAnimation(bottomIn);
        menuButton.setVisibility(View.INVISIBLE);
        searchBox.setVisibility(View.VISIBLE);
        searchBox.startAnimation(bottomIn);
        menuButton.setClickable(false);
        menuButtonCollapse.setClickable(true);


    }
    public static  void menuClose()
    {
        launcherHomeView.setVisibility(View.VISIBLE);
        menuButton.setVisibility(View.VISIBLE);
        menuButton.startAnimation(rotate);

        menu.startAnimation(bottomOut);
        menu.setVisibility(View.INVISIBLE);

        menuButtonCollapse.setVisibility(View.INVISIBLE);

        searchBox.startAnimation(bottomOut);
        searchBox.setVisibility(View.INVISIBLE);

        menuButton.setClickable(true);
        menuButtonCollapse.setClickable(false);

    }

class Pac{
    /*
    * Holds basic information of Application
    * Like Name , Label and Icon
     */
    Drawable icon;
    String label,name,packageName;
}
class PacReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        setPacs();
    }
}
}
