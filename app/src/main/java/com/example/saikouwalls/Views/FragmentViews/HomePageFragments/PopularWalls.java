package com.example.saikouwalls.Views.FragmentViews.HomePageFragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
import com.example.saikouwalls.Views.FragmentViews.HomePage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PopularWalls#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PopularWalls extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PopularWalls() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PopularWalls.
     */
    // TODO: Rename and change types and number of parameters
    public static PopularWalls newInstance(String param1, String param2) {
        PopularWalls fragment = new PopularWalls();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    // widgets & variables
    private View parentViewHolder ;
    private SwipeRefreshLayout swipeRefreshLayout ;
    private RelativeLayout RL ;
    private RecyclerView wallpaperRV ;
    private EditText searchEdt ;
    private ImageView searchIV ;
    private ProgressBar loadingPB ;
    private WallpaperRVAdapter wallpaperRVAdapter ;
    private ArrayList<WallpaperRVModel> wallpaperArrayList ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        parentViewHolder = inflater.inflate(R.layout.fragment_popular_walls, container, false);
        init();
        sync();
        swipeRefreshLayout = (SwipeRefreshLayout) parentViewHolder.findViewById(R.id.SyncPopularWalls) ;
        swipeRefreshLayout.setOnRefreshListener(this) ;
        return parentViewHolder ;
    }
    private void init(){
        RL = parentViewHolder.findViewById(R.id.idLL1) ;
        wallpaperRV = parentViewHolder.findViewById(R.id.idRVWallpapers) ;
        searchEdt = parentViewHolder.findViewById(R.id.idEdtSearch) ;
        searchIV = parentViewHolder.findViewById(R.id.idIVSearch) ;
        loadingPB = parentViewHolder.findViewById(R.id.idPBLoading) ;
        wallpaperArrayList = new ArrayList<>() ;
    }
    private void sync(){
        GridLayoutManager layoutManager = new GridLayoutManager(getContext() , 2) ;
        wallpaperRVAdapter = new WallpaperRVAdapter(wallpaperArrayList , getContext()) ;

        wallpaperRV.setHasFixedSize(true);
        wallpaperRV.setLayoutManager(layoutManager);
        wallpaperRV.setAdapter(wallpaperRVAdapter);

        getWallpapers() ;
        SetOnClickListener() ;
    }

    private void SetOnClickListener() {
        searchIV.setOnClickListener(view -> {
            String searchStr = searchEdt.getText().toString() ;
            if(searchStr.isEmpty())
                showCustomToast("No context");
            else
                getWallpapersByCategory(searchStr) ;
        });
        searchEdt.setOnKeyListener((view, i, keyEvent) -> {
            if(keyEvent.getAction() == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_ENTER) {
                String searchStr = searchEdt.getText().toString() ;
                if(!searchStr.isEmpty())
                    getWallpapersByCategory(searchStr);
                else
                    showCustomToast("No context");
            }
            return false;
        });
    }
    private void getWallpapersByCategory(String category){
        wallpaperArrayList.clear();
        loadingPB.setVisibility(View.VISIBLE);
        String url = "https://api.pexels.com/v1/search?query=" + category + "&per_page=200&page=1";
        RequestQueue queue = Volley.newRequestQueue(getContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    loadingPB.setVisibility(View.GONE);
                    JSONArray photos = response.getJSONArray("photos");
                    for (int i = 0; i < photos.length(); i++) {
                        JSONObject photoObj = photos.getJSONObject(i);
                        String imgUrl = photoObj.getJSONObject("src").getString("portrait");
                        String imgDes = photoObj.getString("alt") ;
                        String width = photoObj.getString("width") ;
                        String height = photoObj.getString("height") ;
                        String photographer = photoObj.getString("photographer") ;
                        String photographerURL = photoObj.getString("photographer_url") ;
                        String imgColor = photoObj.getString("avg_color") ;
                        String ID = ((HomePage)getActivity()).UniqueID ;
                        wallpaperArrayList.add(new WallpaperRVModel(ID ,imgUrl,imgDes,photographer,width,height,photographerURL,imgColor)) ;
                    }
                    wallpaperRVAdapter.notifyDataSetChanged();
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
    private void getWallpapers(){
        wallpaperArrayList.clear();
        loadingPB.setVisibility(View.VISIBLE);
        String url = "https://api.pexels.com/v1/curated?per_page=200&page=1" ;
        RequestQueue queue = Volley.newRequestQueue(getContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                loadingPB.setVisibility(View.GONE);
                try {

                    JSONArray photos = response.getJSONArray("photos");

                    for (int i = 0; i < photos.length(); i++) {
                        JSONObject photoObj = photos.getJSONObject(i);
                        String imgUrl = photoObj.getJSONObject("src").getString("portrait");
                        String imgDes = photoObj.getString("alt") ;
                        String width = photoObj.getString("width") ;
                        String height = photoObj.getString("height") ;
                        String photographer = photoObj.getString("photographer") ;
                        String photographerURL = photoObj.getString("photographer_url") ;
                        String imgColor = photoObj.getString("avg_color") ;
                        String ID = ((HomePage)getActivity()).UniqueID ;
                        wallpaperArrayList.add(new WallpaperRVModel(ID ,imgUrl,imgDes,photographer,width,height,photographerURL,imgColor)) ;
                    }
                    wallpaperRVAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
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
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", "nLsr7rbmLLxM7jwCeToZ1ilcPNBRwquikyJB7SfmMJ1gUwOTkDLEBx4B");

                return headers;
            }
        };
        queue.add(jsonObjectRequest);
    }
    private void showCustomToast(String message){
        LayoutInflater inflater = getLayoutInflater() ;
        View layout = inflater.inflate(R.layout.custom_toast_layout , parentViewHolder.findViewById(R.id.containerToast)) ;
        ImageView img = layout.findViewById(R.id.imageViewToast) ;
        img.setImageResource(R.drawable.baseline_notifications_active_24);
        TextView txt = layout.findViewById(R.id.textViewToast) ;
        txt.setText(message);
        Toast toast = new Toast(getContext()) ;
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