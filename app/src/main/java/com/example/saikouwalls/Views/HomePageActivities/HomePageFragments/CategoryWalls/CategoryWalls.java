package com.example.saikouwalls.Views.HomePageActivities.HomePageFragments.CategoryWalls;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.saikouwalls.Adapters.CategoryRVAdapter;
import com.example.saikouwalls.Models.CategoryRVModel;
import com.example.saikouwalls.R;
import com.example.saikouwalls.Views.HomePageActivities.HomePage;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CategoryWalls#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CategoryWalls extends Fragment {

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
    private String ID ;
    private RecyclerView categoryRV ;
    private CategoryRVAdapter adapter ;
    private ArrayList<CategoryRVModel> categoryRVLargeModels ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        parentViewHolder = inflater.inflate(R.layout.fragment_category_walls, container, false);
        init() ;
        sync() ;
        return parentViewHolder ;
    }

    private void init() {
        ID = ((HomePage)getActivity()).UniqueID ;
        categoryRV = parentViewHolder.findViewById(R.id.wallpaperCategories) ;
        categoryRVLargeModels = new ArrayList<>() ;
    }

    private void sync() {
        LinearLayoutManager manager = new LinearLayoutManager(getContext() , LinearLayoutManager.VERTICAL , false) ;
        adapter = new CategoryRVAdapter(categoryRVLargeModels , getContext()) ;
        categoryRV.setLayoutManager(manager) ;
        categoryRV.setAdapter(adapter) ;

        getCategories() ;
    }

    private void getCategories() {
        categoryRVLargeModels.add(new CategoryRVModel(ID ,"4K", "https://images.unsplash.com/photo-1650802203688-eac9694fe798?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1931&q=80"));
        categoryRVLargeModels.add(new CategoryRVModel(ID ,"Landscape", "https://images.unsplash.com/34/BA1yLjNnQCI1yisIZGEi_2013-07-16_1922_IMG_9873.jpg?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=2071&q=80"));
        categoryRVLargeModels.add(new CategoryRVModel(ID ,"Travel", "https://images.pexels.com/photos/672358/pexels-photo-672358.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500"));
        categoryRVLargeModels.add(new CategoryRVModel(ID ,"Architecture", "https://plus.unsplash.com/premium_photo-1679496470437-a09743179634?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=2070&q=80"));
        categoryRVLargeModels.add(new CategoryRVModel(ID ,"Arts", "https://images.unsplash.com/photo-1456086272160-b28b0645b729?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1932&q=80"));
        categoryRVLargeModels.add(new CategoryRVModel(ID ,"Music", "https://images.pexels.com/photos/4348093/pexels-photo-4348093.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500"));
        categoryRVLargeModels.add(new CategoryRVModel(ID ,"Abstract", "https://images.pexels.com/photos/2110951/pexels-photo-2110951.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500"));
        categoryRVLargeModels.add(new CategoryRVModel(ID ,"Cars", "https://images.unsplash.com/photo-1631526090968-6979b72f2ce2?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=2070&q=80"));
        categoryRVLargeModels.add(new CategoryRVModel(ID ,"Flowers", "https://images.unsplash.com/photo-1455659817273-f96807779a8a?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=2070&q=80"));
        categoryRVLargeModels.add(new CategoryRVModel(ID ,"Games", "https://images.unsplash.com/photo-1577741314755-048d8525d31e?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=2070&q=80"));
        categoryRVLargeModels.add(new CategoryRVModel(ID ,"Programming", "https://images.unsplash.com/photo-1542831371-29b0f74f9713?ixid=MnwxMjA3fDB8MHxzZWFyY2h8MXx8cHJvZ3JhbW1pbmd8ZW58MHx8MHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60"));
        categoryRVLargeModels.add(new CategoryRVModel(ID ,"Technology", "https://images.unsplash.com/photo-1526374965328-7f61d4dc18c5?ixid=MnwxMjA3fDB8MHxzZWFyY2h8MTJ8fHRlY2hub2xvZ3l8ZW58MHx8MHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60"));
    }
}