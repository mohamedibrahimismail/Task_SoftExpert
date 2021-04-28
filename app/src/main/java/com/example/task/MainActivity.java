package com.example.task;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.task.Data.GetDataService;
import com.example.task.Data.Models.CarsListModel;
import com.example.task.Data.Models.Datum;
import com.example.task.Data.RetrofitClientInstance;
import com.google.gson.JsonObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    int page_list = 1;
    @BindView(R.id.progressbar)
    ProgressBar progressbar;
    @BindView(R.id.home_recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.SwipeRefreshLayout)
    androidx.swiperefreshlayout.widget.SwipeRefreshLayout SwipeRefreshLayout;
    @BindView(R.id.nodatatoshow)
    View nodatatoshow;

    EndlessRecyclerViewScrollListener endlessRecyclerViewScrollListener;
    RecyclerViewAdapter recyclerViewAdapter;

    private androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener refreshListener = new androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener() {

        @Override
        public void onRefresh() {
            SwipeRefreshLayout.setRefreshing(true);
            page_list=1;
            endlessRecyclerViewScrollListener.resetState();
            getCarList();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setupSwipeRefresher();
        setup_RecyclerView();
        getCarList();
    }

    public void setupSwipeRefresher(){
        SwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimaryDark));
        SwipeRefreshLayout.setRefreshing(true);
        SwipeRefreshLayout.setOnRefreshListener(refreshListener);
    }

    public void setup_RecyclerView(){
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);

        endlessRecyclerViewScrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                Log.e("onLoadMore", "endlessRecyclerViewScrollListener: "+page );
                progressbar.setVisibility(View.VISIBLE);
                page_list++;
                getCarList();
                recyclerViewAdapter.notifyDataSetChanged();
                //recyclerView.scrollToPosition(recyclerViewAdapter.getItemCount() - 1);

            }
        };

        recyclerView.addOnScrollListener(endlessRecyclerViewScrollListener);
    }


    public void getCarList(){
        if (checkNetworkConnection()) {
            GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<CarsListModel> call = service.getCarList(page_list);
            call.enqueue(new Callback<CarsListModel>() {
                @Override
                public void onResponse(Call<CarsListModel> call, Response<CarsListModel> response) {
                    if (response.code() == 200) {
                        if(response.body().getData()!=null) {
                            if (page_list == 1) {
                                setData(response.body().getData());
                            } else {
                                addDataToList(response.body().getData());
                            }
                        }else {
                            SwipeRefreshLayout.setRefreshing(false);
                            progressbar.setVisibility(View.GONE);
                        }
                    } else {
                        SwipeRefreshLayout.setRefreshing(false);
                        progressbar.setVisibility(View.GONE);
                     //   Utilities.ShowToast(context, "----------------------");
                    }
                }

                @Override
                public void onFailure(Call<CarsListModel> call, Throwable t) {
//                    SwipeRefreshLayout.setRefreshing(false);
//                    Utilities.ShowToast(context, "----------------------");
                }
            });
        } else {

        }
    }

    public void setData(List<Datum> data){

        recyclerViewAdapter = new RecyclerViewAdapter(this,data);
        recyclerView.setAdapter(recyclerViewAdapter);
        SwipeRefreshLayout.setRefreshing(false);
        progressbar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }


    public void addDataToList(List<Datum> data){
        recyclerViewAdapter.addDataToList(data);
        progressbar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    public boolean checkNetworkConnection() {
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        } else {
            SwipeRefreshLayout.setRefreshing(false);
            Toast.makeText(this, getResources().getString(R.string.pleasechecknetworkconnection), Toast.LENGTH_SHORT).show();
            connected = false;

        }

        return connected;
    }


}