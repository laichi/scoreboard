package com.yahoo.scoreboard;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestHandle;

import com.yahoo.scoreboard.adapter.StreamDataAdapter;

import org.apache.http.Header;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class StreamFragment extends Fragment  {

    private ListView lstData;
    private SwipeRefreshLayout laySwipe;
    private ArrayList<StreamData> sdatas;
    private StreamDataAdapter aSdatas;
    private static int itemCnt = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stream, container, false);
        sdatas = new ArrayList<StreamData>();
        aSdatas = new StreamDataAdapter(getActivity(), sdatas);
        return view;
    }

    public void onActivityCreated(Bundle savedState) {
        super.onActivityCreated(savedState);
        fetchScoreboardData();
        initView();
    }

    private void initView() {
        laySwipe = (SwipeRefreshLayout) getActivity().findViewById(R.id.laySwipe);
        laySwipe.setOnRefreshListener(onSwipeToRefresh);
        laySwipe.setColorSchemeResources(
                android.R.color.holo_red_light,
                android.R.color.holo_blue_light,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light);

        lstData = (ListView) getActivity().findViewById(R.id.lstData);
        lstData.setAdapter(aSdatas);
        lstData.setOnScrollListener(onListScroll);
    }

    public void fetchScoreboardData() {
        String url = "http://scoreboard01.ac.corp.sg3.yahoo.com/v1/scoreboard/getstream?offset=" + StreamFragment.itemCnt;

        // Create the network client
        AsyncHttpClient client = new AsyncHttpClient();
        // Trigger the GET request
        RequestHandle requestHandle = client.get(url, null, new JsonHttpResponseHandler() {
            // onSuccess (worked, 200)
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray sdatasJSON = null;
                try {
                    sdatasJSON = response.getJSONObject("response_data").getJSONArray("list"); // array of posts
                    // iterate array of posts
                    for (int i = 0; i < sdatasJSON.length(); i++) {
                        JSONObject sdataJSON = sdatasJSON.getJSONObject(i);

                        StreamData sdata = new StreamData();
                        sdata.buildNumber = sdataJSON.getString("build_number");
                        sdata.jobName = sdataJSON.getString("job_name") + " #" + sdata.buildNumber;
                        sdata.byid = sdataJSON.getString("by_id");
                        sdata.status = sdataJSON.getString("last_status");
                        sdata.time = sdataJSON.getString("build_time");

                        sdatas.add(sdata);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                itemCnt = itemCnt + sdatasJSON.length();
                // callback
                aSdatas.notifyDataSetChanged();
            }

            // onFailure (
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                // DO SOMETHING
                Log.i("legochen", "legochen : " + "fail");
            }
        });
    }

    private SwipeRefreshLayout.OnRefreshListener onSwipeToRefresh = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            // initial
            StreamFragment.itemCnt = 0;
            sdatas.clear();

            fetchScoreboardData();
            laySwipe.setRefreshing(true);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    laySwipe.setRefreshing(false);
                    Toast.makeText(getActivity(), "Refresh done!", Toast.LENGTH_SHORT).show();
                }
            }, 3000);
        }
    };

    private AbsListView.OnScrollListener onListScroll = new AbsListView.OnScrollListener() {

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (firstVisibleItem == 0) {
                laySwipe.setEnabled(true);
            } else{
                laySwipe.setEnabled(false);
            }

            if (firstVisibleItem + visibleItemCount == totalItemCount) {
                StreamFragment.itemCnt += 10;
                fetchScoreboardData();
            }
        }
    };

}
