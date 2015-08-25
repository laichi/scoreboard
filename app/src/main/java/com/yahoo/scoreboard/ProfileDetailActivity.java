package com.yahoo.scoreboard;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.squareup.picasso.Picasso;
import com.yahoo.scoreboard.adapter.ProfileJobAdapter;
import com.yahoo.scoreboard.dao.ProfileJobData;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

public class ProfileDetailActivity extends Activity {

    View convertView;
    String ImageUrl = "https://backyard.yahoo.com/isweb-icons/staff/puritys_square.jpg";
    private ArrayList<ProfileJobData> pdatas;
    private ProfileJobAdapter aPdatas;
    private ListView lvProfile;
    private TextView txtHeroScore;
    private TextView txtBadScore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_detail);
        setTitle("Player profile");
        pdatas = new ArrayList<ProfileJobData>();
        aPdatas = new ProfileJobAdapter(this, pdatas);

        lvProfile = (ListView)findViewById(R.id.lvProfile);
        lvProfile.setAdapter(aPdatas);

        txtHeroScore = (TextView)findViewById(R.id.txtHeroScore);
        txtBadScore = (TextView)findViewById(R.id.txtBadScore);


        TextView tvPlayerName = (TextView)findViewById(R.id.playerName);
        tvPlayerName.setText("puritys");

        ImageView ivPhoto = (ImageView)findViewById(R.id.playerPhoto);
        Picasso.with(this)
                .load(ImageUrl)
                .into(ivPhoto);

        fetchProfileData();
    }

    public void fetchProfileData() {
        /*
            Popular : https://api.instagram.com/v1/media/popular?access_token=ACCESS-TOKEN
             - Response
        */

        String url = "http://scoreboard01.ac.corp.sg3.yahoo.com/v1/scoreboard/getuser?userid=puritys";
        Log.i("legochen", "legochen : " + url);

        // Create the network client
        AsyncHttpClient client = new AsyncHttpClient();
        // Trigger the GET request
        RequestHandle requestHandle = client.get(url, null, new JsonHttpResponseHandler() {
            // onSuccess (worked, 200)
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // Iterate each of photo items and decode the item into a java object
                Log.i("legochen", "onsuccess");
                JSONArray pdatasJSON = null;
                try {
                    pdatasJSON = response.getJSONObject("response_data").getJSONArray("list"); // array of posts
                    // iterate array of posts
                    ProfileJobData pdata = new ProfileJobData();

                    for (int i = 0; i < pdatasJSON.length(); i++) {
                        Log.i("legochen", Integer.toString(i));
                        // get the json object at that position
                        JSONObject pdataJSON = pdatasJSON.getJSONObject(i);
                        Log.i("legochen", pdataJSON.toString());

                        pdata.buildNumber = pdataJSON.getString("build_number");
                        pdata.jobName = pdataJSON.getString("job_name") + " #" + pdata.buildNumber;
                        pdata.status = pdataJSON.getString("last_status");
                        pdata.time = pdataJSON.getString("build_time");
                        pdatas.add(pdata);
                    }
                    txtHeroScore.setText(response.getJSONObject("response_data").getString("positive"));
                    txtBadScore.setText(response.getJSONObject("response_data").getString("negative"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // callback
                aPdatas.notifyDataSetChanged();
            }

            // onFailure (
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                // DO SOMETHING
                Log.i("legochen", "legochen : " + "fail");
            }
        });
    }
}