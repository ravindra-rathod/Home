package com.example.ratslab.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by ratslab on 01/06/2015.
 */
public class AppsListAdapter extends BaseAdapter {
    Context c;
    MainActivity.Pac pacs[];

    public AppsListAdapter(Context c,MainActivity.Pac pacs[]) {
        this.c=c;
        this.pacs=pacs;
    }

    @Override
    public int getCount() {
        return pacs.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        /*
        * ViewHolder class holds icon and label for application
        * layout inflater able to assign layoyt file to a view i this case convertView
        * ViewHolder get access to layout's icon and label
        * and the sets it to desired application

         */
        ViewHolder vh;
        LayoutInflater li = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView==null)
        {
            convertView=li.inflate(R.layout.app_view,null);

            vh = new ViewHolder();
            vh.icon=(ImageView)convertView.findViewById(R.id.app_icon);
            vh.name=(TextView)convertView.findViewById(R.id.app_name);
            convertView.setTag(vh);
        }
        else
        {
            vh=(ViewHolder)convertView.getTag();
        }

        vh.name.setText(pacs[position].label);
        vh.icon.setImageDrawable(pacs[position].icon);

        return convertView;
    }
    class ViewHolder
    {
        TextView name;
        ImageView icon;
    }
}
