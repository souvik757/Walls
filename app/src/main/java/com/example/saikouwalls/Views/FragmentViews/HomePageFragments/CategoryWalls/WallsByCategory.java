package com.example.saikouwalls.Views.FragmentViews.HomePageFragments.CategoryWalls;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.saikouwalls.Adapters.WallpaperRVAdapter;
import com.example.saikouwalls.Models.WallpaperRVModel;
import com.example.saikouwalls.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WallsByCategory extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{
    // widgets
    private SwipeRefreshLayout swipeRefreshLayout ;
    private ArrayList<WallpaperRVModel> wallpaperArrayList ;
    private RecyclerView categorizedWallpaperRV ;
    private WallpaperRVAdapter adapter ;
    private ProgressBar loadBar ;
    private TextView categoryTextView ;
    private String category ;
    private String ID ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walls_by_category);

        init();
        sync();
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.SyncWallsByCat) ;
        swipeRefreshLayout.setOnRefreshListener(this);
    }
    private void init(){
        ID = getIntent().getStringExtra("userID") ;
        category = getIntent().getStringExtra("SelectedCategory") ;
        loadBar = findViewById(R.id.idPBLoading) ;
        categorizedWallpaperRV = findViewById(R.id.idRVWallpapers) ;
        categoryTextView = findViewById(R.id.idTextCategory) ;
        wallpaperArrayList = new ArrayList<>() ;
    }
    private void sync(){
        setResources();
        GridLayoutManager layoutManager = new GridLayoutManager(this , 2) ;
        adapter = new WallpaperRVAdapter(wallpaperArrayList , this) ;

        categorizedWallpaperRV.setLayoutManager(layoutManager) ;
        categorizedWallpaperRV.setAdapter(adapter) ;

        getWallpapers() ;
    }
    private void setResources(){
        categoryTextView.setText(category) ;
    }
    private void getWallpapers() {
        loadBar.setVisibility(View.VISIBLE);
        String url = "https://api.pexels.com/v1/search?query=" + category + "&per_page=200&page=1";
        RequestQueue queue = Volley.newRequestQueue(WallsByCategory.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    loadBar.setVisibility(View.GONE);
                    JSONArray photos = response.getJSONArray("photos");
                    for (int i = 0; i < photos.length(); i++) {
                        JSONObject photoObj = photos.getJSONObject(i);
                        String imgUrl = photoObj.getJSONObject("src").getString("portrait");
                        String imgDes = photoObj.getString("alt") ;
                        String width = photoObj.getString("width") ;
                        String height = photoObj.getString("height") ;
                        String photographer = photoObj.getString("photographer") ;
                        String photographerURL = photoObj.getString("photographer_url") ;
                        String photoColor = photoObj.getString("avg_color") ;
                        wallpaperArrayList.add(new WallpaperRVModel(ID ,imgUrl,imgDes,photographer,width,height,photographerURL,photoColor)) ;
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showCustomToast("Fail to get data..") ;
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", "nLsr7rbmLLxM7jwCeToZ1ilcPNBRwquikyJB7SfmMJ1gUwOTkDLEBx4B");

                return headers;
            }
        };
        queue.add(jsonObjectRequest);
    }
    private void showCustomToast(String message){
        LayoutInflater inflater = getLayoutInflater() ;
        View layout = inflater.inflate(R.layout.custom_toast_layout , (ViewGroup) findViewById(R.id.containerToast)) ;
        ImageView img = layout.findViewById(R.id.imageViewToast) ;
        img.setImageResource(R.drawable.baseline_notifications_active_24);
        TextView txt = layout.findViewById(R.id.textViewToast) ;
        txt.setText(message);
        Toast toast = new Toast(getApplicationContext()) ;
        toast.setDuration(Toast.LENGTH_SHORT) ;
        toast.setView(layout);
        toast.show() ;
    }
    @Override
    public void onRefresh() {
        init();
        sync();
        swipeRefreshLayout.setRefreshing(false);
    }
}