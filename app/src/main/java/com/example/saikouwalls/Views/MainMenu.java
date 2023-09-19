package com.example.saikouwalls.Views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import com.example.saikouwalls.Adapters.CategoryClickInterface;
import com.example.saikouwalls.Adapters.CategoryRVAdapter;
import com.example.saikouwalls.Adapters.WallpaperRVAdapter;
import com.example.saikouwalls.Models.CategoryRVModel;
import com.example.saikouwalls.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainMenu extends AppCompatActivity implements CategoryClickInterface {

    private RecyclerView categoryRV , wallpaperRV ;
    private EditText searchEdt ;
    private ImageView searchIV ;
    private ProgressBar loadingPB ;
    private CategoryRVAdapter categoryRVAdapter ;
    private WallpaperRVAdapter wallpaperRVAdapter ;
    private ArrayList<CategoryRVModel> categoryRVModelArrayList ;
    private ArrayList<String> wallpaperArrayList ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        init() ;
        sync() ;
    }

    private void init(){
        categoryRV = findViewById(R.id.idRVCategories) ;
        wallpaperRV = findViewById(R.id.idRVWallpapers) ;
        searchEdt = findViewById(R.id.idEdtSearch) ;
        searchIV = findViewById(R.id.idIVSearch) ;
        loadingPB = findViewById(R.id.idPBLoading) ;
        wallpaperArrayList = new ArrayList<>() ;
        categoryRVModelArrayList = new ArrayList<>() ;
    }
    private void sync(){
        LinearLayoutManager manager = new LinearLayoutManager(MainMenu.this , LinearLayoutManager.HORIZONTAL , false) ;
        wallpaperRVAdapter = new WallpaperRVAdapter(wallpaperArrayList , this) ;
        categoryRVAdapter = new CategoryRVAdapter(categoryRVModelArrayList , this , this) ;

        categoryRV.setLayoutManager(manager) ;
        categoryRV.setAdapter(categoryRVAdapter) ;

        GridLayoutManager layoutManager = new GridLayoutManager(this , 2) ;

        wallpaperRV.setLayoutManager(layoutManager);
        wallpaperRV.setAdapter(wallpaperRVAdapter);

        getCategories() ;
        getWallpapers() ;
        searchIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchStr = searchEdt.getText().toString() ;
                if(searchStr.isEmpty())
                    showCustomToast("No context");
                else
                    getWallpapersByCategory(searchStr) ;
            }
        });
    }

    private void getWallpapersByCategory(String category){
        wallpaperArrayList.clear();
        //on below line we are making visibility of our progress bar as gone.
        loadingPB.setVisibility(View.VISIBLE);
        //on below line we are creating a string variable for our url and adding url to it.
        String url = "https://api.pexels.com/v1/search?query=" + category + "&per_page=30&page=1";
        //on below line we are creating a new variable for our request queue.
        RequestQueue queue = Volley.newRequestQueue(MainMenu.this);
        //on below line we are making a json object request to get the data from url .
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //on below line we are extracting the data from our response and passing it to our array list.
                try {
                    loadingPB.setVisibility(View.GONE);
                    //on below line we are extracting json data.
                    JSONArray photos = response.getJSONArray("photos");
                    for (int i = 0; i < photos.length(); i++) {
                        JSONObject photoObj = photos.getJSONObject(i);
                        String imgUrl = photoObj.getJSONObject("src").getString("portrait");
                        //on below line we are passing data to our array list
                        wallpaperArrayList.add(imgUrl);
                    }
                    //here we are notifying adapter that data has changed in our list.
                    wallpaperRVAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    //handling json exception on below line.
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //displaying a simple toast message on error response.
                Toast.makeText(MainMenu.this, "Fail to get data..", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                //in this method passing headers as key along with value as API keys.
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", "nLsr7rbmLLxM7jwCeToZ1ilcPNBRwquikyJB7SfmMJ1gUwOTkDLEBx4B");
                //at last returning headers.
                return headers;
            }
        };
        queue.add(jsonObjectRequest);
    }
    private void getCategories(){
        categoryRVModelArrayList.add(new CategoryRVModel("4K", "https://images.unsplash.com/photo-1617468264185-e6535390e9a4?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=2070&q=80"));
        categoryRVModelArrayList.add(new CategoryRVModel("Landscape", "https://images.unsplash.com/34/BA1yLjNnQCI1yisIZGEi_2013-07-16_1922_IMG_9873.jpg?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=2071&q=80"));
        categoryRVModelArrayList.add(new CategoryRVModel("Travel", "https://images.pexels.com/photos/672358/pexels-photo-672358.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500"));
        categoryRVModelArrayList.add(new CategoryRVModel("Architecture", "https://images.pexels.com/photos/256150/pexels-photo-256150.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500"));
        categoryRVModelArrayList.add(new CategoryRVModel("Arts", "https://images.pexels.com/photos/1194420/pexels-photo-1194420.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500"));
        categoryRVModelArrayList.add(new CategoryRVModel("Music", "https://images.pexels.com/photos/4348093/pexels-photo-4348093.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500"));
        categoryRVModelArrayList.add(new CategoryRVModel("Abstract", "https://images.pexels.com/photos/2110951/pexels-photo-2110951.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500"));
        categoryRVModelArrayList.add(new CategoryRVModel("Cars", "https://images.pexels.com/photos/3802510/pexels-photo-3802510.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500"));
        categoryRVModelArrayList.add(new CategoryRVModel("Flowers", "https://images.pexels.com/photos/1086178/pexels-photo-1086178.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500"));
        categoryRVModelArrayList.add(new CategoryRVModel("Games", "https://images.unsplash.com/photo-1577741314755-048d8525d31e?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=2070&q=80"));
        categoryRVModelArrayList.add(new CategoryRVModel("Programming", "https://images.unsplash.com/photo-1542831371-29b0f74f9713?ixid=MnwxMjA3fDB8MHxzZWFyY2h8MXx8cHJvZ3JhbW1pbmd8ZW58MHx8MHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60"));
        categoryRVModelArrayList.add(new CategoryRVModel("Technology", "https://images.unsplash.com/photo-1526374965328-7f61d4dc18c5?ixid=MnwxMjA3fDB8MHxzZWFyY2h8MTJ8fHRlY2hub2xvZ3l8ZW58MHx8MHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60"));
    }
    private void getWallpapers(){
        wallpaperArrayList.clear();
        loadingPB.setVisibility(View.VISIBLE);
        //creating a variable for our url.
        String url = "https://api.pexels.com/v1/curated?per_page=30&page=1";
        //on below line we are creating a new variable for our request queue.
        RequestQueue queue = Volley.newRequestQueue(MainMenu.this);
        //on below line we are making a json object request to get the data from url .
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //on below line we are extracting the data from our response and passing it to our array list.
                loadingPB.setVisibility(View.GONE);
                try {
                    //on below line we are extracting json data.
                    JSONArray photos = response.getJSONArray("photos");
                    for (int i = 0; i < photos.length(); i++) {
                        JSONObject photoObj = photos.getJSONObject(i);
                        String imgUrl = photoObj.getJSONObject("src").getString("portrait");
                        //on below line we are passing data to our array list
                        wallpaperArrayList.add(imgUrl);
                    }
                    wallpaperRVAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    //handling json exception on below line.
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showCustomToast("failed to fetch data");
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                //in this method passing headers as key along with value as API keys.
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", "nLsr7rbmLLxM7jwCeToZ1ilcPNBRwquikyJB7SfmMJ1gUwOTkDLEBx4B");
                //at last returning headers.
                return headers;
            }
        };
        queue.add(jsonObjectRequest);
    }

    @Override
    public void onCategoryClick(int position) {
        String category = categoryRVModelArrayList.get(position).getCategory() ;
        getWallpapersByCategory(category) ;
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

}