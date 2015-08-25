package com.yahoo.scoreboard;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.yahoo.scoreboard.adapter.PlayerAdapter;
import com.yahoo.scoreboard.dao.Player;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class BillboardFragment extends Fragment implements View.OnClickListener {
    private ListView lvPlayer;
    private ArrayList<Player> players;
    private PlayerAdapter adaPlayer;
    private SwipeRefreshLayout laySwipe;
    private static int itemCnt = 0;
    private String listType = "heros";
    Button btnHero, btnCulprit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_billboard,container,false);

        players = new ArrayList<>();
        adaPlayer = new PlayerAdapter(getActivity(), players);

        /*
        Button btnHero = (Button) getActivity().findViewById(R.id.btnHero);
        Button btnCulprit = (Button) getActivity().findViewById(R.id.btnCulprit);

        btnHero.setOnClickListener(this);
        btnCulprit.setOnClickListener(this);
        */
        /*
        btnHero.setOnClickListener(new Button.OnClickListener(){
            @Override
        public void onClick(View v) {
                if (!listType.equals("heros")) {
                    listType = "heros";
                    itemCnt = 0;
                    fetchBillboard();
                }
            }
        });
        btnCulprit.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (!listType.equals("culprits")) {
                    listType = "culprits";
                    itemCnt = 0;
                    fetchBillboard();
                }
            }
        });
        */

        return view;
    }

    public void onActivityCreated(Bundle savedState) {
        super.onActivityCreated(savedState);
        fetchBillboard();
        initView();
    }

    private void initView() {
        laySwipe = (SwipeRefreshLayout) getActivity().findViewById(R.id.laySwipeBillboard);
        laySwipe.setOnRefreshListener(onSwipeToRefresh);
        laySwipe.setColorSchemeResources(
                android.R.color.holo_red_light,
                android.R.color.holo_blue_light,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light);

        lvPlayer = (ListView) getActivity().findViewById(R.id.lvPlayer);
        lvPlayer.setAdapter(adaPlayer);
        lvPlayer.setOnScrollListener(onListScroll);
        lvPlayer.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ProfileDetailActivity.class);
                startActivity(intent);
            }
        });

        btnHero = (Button) getActivity().findViewById(R.id.btnHero);
        btnCulprit = (Button) getActivity().findViewById(R.id.btnCulprit);

        btnHero.setOnClickListener(this);
        btnCulprit.setOnClickListener(this);
    }

    private void fetchBillboard() {
        String url = "http://scoreboard01.ac.corp.sg3.yahoo.com/v1/scoreboard/get?type=" +listType + "&offset=" + BillboardFragment.itemCnt;
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, null, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // Log.i("DEBUG", response.toString());

                JSONArray billboardJSON = null;
                try {
                    billboardJSON = response.getJSONObject("response_data").getJSONArray("list");
                    for (int i = 0; i < billboardJSON.length(); i++) {
                        JSONObject record = billboardJSON.getJSONObject(i);
                        Player player = new Player();
                        player.byid = record.getString("by_id");
                        player.score = record.getString("score");

                        players.add(player);
                    }
                    itemCnt = itemCnt + billboardJSON.length();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                adaPlayer.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }
        });
    }

    private SwipeRefreshLayout.OnRefreshListener onSwipeToRefresh = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            // initial
            itemCnt = 0;
            players.clear();

            fetchBillboard();
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
            } else {
                laySwipe.setEnabled(false);
            }

            if (firstVisibleItem + visibleItemCount == totalItemCount) {
                itemCnt += 10;
                fetchBillboard();
            }
        }
    };

    @Override
    public void onClick(final View v) {
        switch(v.getId()){
            case R.id.btnHero:
                if (!listType.equals("heros")) {
                    listType = "heros";
                    itemCnt = 0;
                    players.clear();
                    fetchBillboard();
                }
                break;
            case R.id.btnCulprit:
                if (!listType.equals("culprits")) {
                    listType = "culprits";
                    itemCnt = 0;
                    players.clear();
                    fetchBillboard();
                }
                break;
        }
    }
}
