package com.yahoo.scoreboard.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.yahoo.scoreboard.R;
import com.yahoo.scoreboard.dao.Player;

import java.util.List;

/**
 * Created by jhkao on 7/30/15.
 */
public class PlayerAdapter extends ArrayAdapter<Player> {

    public PlayerAdapter(Context context, List<Player> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Player player = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.billboard, parent, false);
        }

        ImageView ivProfile = (ImageView) convertView.findViewById(R.id.ivProfile);
        TextView tvByID = (TextView) convertView.findViewById(R.id.tvByID);
        TextView tvScore = (TextView) convertView.findViewById(R.id.tvScore);

        ivProfile.setImageResource(0);
        String byProfileImgUrl = getContext().getString(R.string.by_profile_img_url);

        com.squareup.picasso.Transformation transformation = new RoundedTransformationBuilder()
                .borderColor(Color.WHITE)
                .borderWidthDp(1)
                .cornerRadiusDp(40)
                .oval(false)
                .build();

        if(player.byid.equals("by-tortuga")) {
            Picasso.with(getContext()).load(R.drawable.tor).resize(120, 120).transform(transformation).into(ivProfile);
        }
        else{
            Picasso.with(getContext()).load(String.format(byProfileImgUrl, player.byid)).resize(120,120).transform(transformation).into(ivProfile);
        }

        tvByID.setText(player.byid);
        tvScore.setText(player.score);

        return convertView;
    }
}
