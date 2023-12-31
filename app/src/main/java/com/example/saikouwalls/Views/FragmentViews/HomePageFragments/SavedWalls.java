package com.example.saikouwalls.Views.FragmentViews.HomePageFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.saikouwalls.Adapters.SavedWallpaperRVAdapter;
import com.example.saikouwalls.Models.SavedWallpaperRVModel;
import com.example.saikouwalls.R;
import com.example.saikouwalls.Services.DatabaseKeys;
import com.example.saikouwalls.Views.FragmentViews.HomePage;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SavedWalls#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SavedWalls extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SavedWalls() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SavedWalls.
     */
    // TODO: Rename and change types and number of parameters
    public static SavedWalls newInstance(String param1, String param2) {
        SavedWalls fragment = new SavedWalls();
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
    private String ID ;
    private TextView txt ;
    private RecyclerView savedWallpaperRV ;
    private ProgressBar loadingBar ;
    private SavedWallpaperRVAdapter wallpaperRVAdapter ;
    private ArrayList<SavedWallpaperRVModel> wallpaperArrayList ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        parentViewHolder = inflater.inflate(R.layout.fragment_saved_walls, container, false);
        init();
        sync();
        swipeRefreshLayout = (SwipeRefreshLayout) parentViewHolder.findViewById(R.id.SyncSavedWalls) ;
        swipeRefreshLayout.setOnRefreshListener(this) ;
        return parentViewHolder ;
    }
    private void init(){
        ID = ((HomePage)getActivity()).UniqueID ;
        txt = parentViewHolder.findViewById(R.id.NoSavedTxt) ;
        savedWallpaperRV = parentViewHolder.findViewById(R.id.idRVSavedWallpaper) ;
        loadingBar = parentViewHolder.findViewById(R.id.idRVSavedProgressBar) ;
        wallpaperArrayList = new ArrayList<>() ;
    }
    private void sync(){
        GridLayoutManager layoutManager = new GridLayoutManager(getContext() , 2) ;
        wallpaperRVAdapter = new SavedWallpaperRVAdapter(wallpaperArrayList , getContext()) ;

        savedWallpaperRV.setLayoutManager(layoutManager);
        savedWallpaperRV.setAdapter(wallpaperRVAdapter);
        getWallpapers() ;
    }
    private void getWallpapers(){
        DatabaseReference mRealtime = FirebaseDatabase.getInstance().getReference() ;
        mRealtime.child(DatabaseKeys.users).child(ID).child(DatabaseKeys.savedImg).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    for (DataSnapshot imgId : snapshot.getChildren()) {
                        String imgUrl = imgId.child(DatabaseKeys.imgURL).getValue(String.class);
                        String imgWidth = imgId.child(DatabaseKeys.imgWIDTH).getValue(String.class) ;
                        String imgHeight = imgId.child(DatabaseKeys.imgHEIGHT).getValue(String.class) ;
                        String imgAlt = imgId.child(DatabaseKeys.imgALT).getValue(String.class) ;
                        String imgPhotographer = imgId.child(DatabaseKeys.imgPHOTOGRAPHER).getValue(String.class) ;
                        String urlPhotographer = imgId.child(DatabaseKeys.urlPHOTOGRAPHER).getValue(String.class) ;
                        String imgColor = imgId.child(DatabaseKeys.imgCOLOR).getValue(String.class) ;
                        wallpaperArrayList.add(new SavedWallpaperRVModel(ID, imgUrl, imgAlt, imgPhotographer, imgWidth, imgHeight, urlPhotographer, imgColor));
                        loadingBar.setVisibility(View.GONE);
                    }
                    wallpaperRVAdapter.notifyDataSetChanged();
                }
                else {
                    loadingBar.setVisibility(View.GONE) ;
                    txt.setVisibility(View.VISIBLE) ;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                throw error.toException() ;
            }
        });
    }

    @Override
    public void onRefresh() {
        init();
        sync();
        swipeRefreshLayout.setRefreshing(false);
    }
}