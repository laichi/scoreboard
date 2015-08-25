package com.yahoo.scoreboard.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yahoo.scoreboard.R;
import com.yahoo.scoreboard.dao.ProfileJobData;

import java.util.List;

/**
 * Created by jhkao on 7/31/15.
 */
public class ProfileJobAdapter extends ArrayAdapter<ProfileJobData> {
    // What data do we need from the activity
    // Context, Data Sosurce
    public ProfileJobAdapter(Context context, List<ProfileJobData> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
    }

    // What our item looks like
    // Use the template to display each photo

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        ProfileJobData pdata = getItem(position);
        // Check if we are using a recycled view, if not we need to inflate
        if (convertView == null) {
            // create a new view from template
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_profile_job, parent, false);
        }
        // Lookup the views for populating the data (image, caption)
        TextView profileTxtJobName = (TextView) convertView.findViewById(R.id.profileTxtJobName);
        TextView profileTxtBuildTime = (TextView) convertView.findViewById(R.id.profileTxtBuildTime);
        ImageView imgStatus = (ImageView) convertView.findViewById(R.id.profileImgStatus);

        profileTxtJobName.setText(pdata.jobName);
        profileTxtBuildTime.setText(pdata.time);



        switch (pdata.status) {
            case "SUCCESS":
                Log.i("legochen", "S");
                imgStatus.setImageResource(R.drawable.success);
                break;
            case "FAILURE":
                Log.i("legochen", "F");
                imgStatus.setImageResource(R.drawable.failure);
                break;
            case "UNSTABLE":
                Log.i("legochen", "U");
                imgStatus.setBackgroundColor(Color.rgb(0, 128, 128));
                break;
            default:
                Log.i("legochen", pdata.status);
                break;
        }

        // Return the created item as a view
        return convertView;
    }
}
