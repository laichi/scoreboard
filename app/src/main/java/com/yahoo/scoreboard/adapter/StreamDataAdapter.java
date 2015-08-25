package com.yahoo.scoreboard.adapter;

/**
 * Created by legochen on 7/31/15.
 */
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.yahoo.scoreboard.R;
import com.yahoo.scoreboard.StreamData;

import java.util.List;

public class StreamDataAdapter extends ArrayAdapter<StreamData> {
    // What data do we need from the activity
    // Context, Data Sosurce
    public StreamDataAdapter(Context context, List<StreamData> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
    }

    // What our item looks like
    // Use the template to display each photo

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        StreamData sdata = getItem(position);
        // Check if we are using a recycled view, if not we need to inflate
        if (convertView == null) {
            // create a new view from template
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_stream_data, parent, false);
        }
        // Lookup the views for populating the data (image, caption)
        ImageView ivAuthor = (ImageView) convertView.findViewById(R.id.ivAuthor);
        TextView txtJobName = (TextView) convertView.findViewById(R.id.txtJobName);
        TextView txtAuthor = (TextView) convertView.findViewById(R.id.txtAuthor);
        TextView txtBuildTime = (TextView) convertView.findViewById(R.id.txtBuildTime);
        ImageView imgStatus = (ImageView) convertView.findViewById(R.id.imgStatus);

        ivAuthor.setImageResource(0);
        String byProfileImgUrl = getContext().getString(R.string.by_profile_img_url);

        com.squareup.picasso.Transformation transformation = new RoundedTransformationBuilder()
                .borderColor(Color.WHITE)
                .borderWidthDp(1)
                .cornerRadiusDp(40)
                .oval(false)
                .build();

        if(sdata.byid.equals("by-tortuga")) {
            Picasso.with(getContext()).load(R.drawable.tor).resize(120, 120).transform(transformation).into(ivAuthor);
        }
        else{
            Picasso.with(getContext()).load(String.format(byProfileImgUrl, sdata.byid)).resize(120,120).transform(transformation).into(ivAuthor);
        }

        txtJobName.setText(sdata.jobName);
        txtBuildTime.setText(sdata.time);
        txtAuthor.setText(sdata.byid);

        switch (sdata.status) {
            case "SUCCESS":
                Log.i("legochen", "S");
                // imgStatus.setBackgroundColor(Color.rgb(149,234,0));
                imgStatus.setImageResource(R.drawable.success);
                break;
            case "FAILURE":
                Log.i("legochen", "F");
                // imgStatus.setBackgroundColor(Color.rgb(255, 0, 0));
                imgStatus.setImageResource(R.drawable.failure);
                break;
            case "UNSTABLE":
                Log.i("legochen", "U");
                imgStatus.setBackgroundColor(Color.rgb(0, 128, 128));
                break;
            default:
                Log.i("legochen", sdata.status);
                break;
        }

        // Return the created item as a view
        return convertView;
    }
}