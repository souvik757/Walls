package com.example.saikouwalls.Views.FragmentViews.HomePageFragments.CategoryWalls;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.saikouwalls.Adapters.CategoryRVAdapter;
import com.example.saikouwalls.Models.CategoryRVModel;
import com.example.saikouwalls.R;
import com.example.saikouwalls.Views.FragmentViews.HomePage;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CategoryWalls#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CategoryWalls extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CategoryWalls() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CategoryWalls.
     */
    // TODO: Rename and change types and number of parameters
    public static CategoryWalls newInstance(String param1, String param2) {
        CategoryWalls fragment = new CategoryWalls();
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
    private ProgressBar pBar ;
    private RecyclerView categoryRV ;
    private CategoryRVAdapter adapter ;
    private ArrayList<CategoryRVModel> categoryRVLargeModels ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        parentViewHolder = inflater.inflate(R.layout.fragment_category_walls, container, false);
        init() ;
        sync() ;
        swipeRefreshLayout = (SwipeRefreshLayout) parentViewHolder.findViewById(R.id.SyncCatWalls) ;
        swipeRefreshLayout.setOnRefreshListener(this) ;
        return parentViewHolder ;
    }
    private void init() {
        ID = ((HomePage)getActivity()).UniqueID ;
        pBar = parentViewHolder.findViewById(R.id.idPB) ;
        categoryRV = parentViewHolder.findViewById(R.id.wallpaperCategories) ;
        categoryRVLargeModels = new ArrayList<>() ;

    }
    private void sync() {
        GridLayoutManager layoutManager = new GridLayoutManager(getContext() , 2) ;
        adapter = new CategoryRVAdapter(categoryRVLargeModels , getContext()) ;

        categoryRV.setLayoutManager(layoutManager);
        categoryRV.setAdapter(adapter);
        getCategories() ;
    }
    private void getCategories() {
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
        mRef.child("users").child("categories").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    // write code here..
                    for (DataSnapshot categories : snapshot.getChildren()){
                        String name = categories.child("name").getValue(String.class) ;
                        String url  = categories.child("url").getValue(String.class) ;
                        categoryRVLargeModels.add(new CategoryRVModel(ID , name , url)) ;
                        pBar.setVisibility(View.GONE);
                        adapter.notifyDataSetChanged();
                    }
                }
                else {
                    Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                    pBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                pBar.setVisibility(View.GONE);
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