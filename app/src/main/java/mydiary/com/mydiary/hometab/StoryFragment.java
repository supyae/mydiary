package mydiary.com.mydiary.hometab;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import mydiary.com.mydiary.AppController;
import mydiary.com.mydiary.CartListAdapter;
import mydiary.com.mydiary.Item;
import mydiary.com.mydiary.R;
import mydiary.com.mydiary.story.StoryActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class StoryFragment extends Fragment implements View.OnClickListener {

    //private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    private View v;
    private CartListAdapter adapter;
    private ArrayList<Item> cartList;
    private RecyclerView.ViewHolder holder;

    private ImageButton addButton;

    private static final String URL = "http://192.168.1.144/my_diary_app/story.json";


    public StoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_story, container, false);

        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        //coordinatorLayout = findViewById(R.id.coordinator_layout);
        cartList = new ArrayList<>();
        adapter = new CartListAdapter(this, cartList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);


//        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
//            @Override
//            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
//
//                View child = rv.findChildViewUnder(e.getX(),e.getY());
//
//                int position = recyclerView.getChildAdapterPosition(child);
//
//                Log.d("position", String.valueOf(position));
//
//                StoryDetailFragment storyDetailFragment = new mydiary.com.mydiary.hometab.StoryDetailFragment();
//
//                // Insert the fragment by replacing any existing fragment
//                android.support.v4.app.FragmentManager fmg = getActivity().getSupportFragmentManager();
//                FragmentTransaction fgT = fmg.beginTransaction();
//                fgT.replace(R.id.story, storyDetailFragment);
//                fgT.commit();
//
//                return true;
//            }
//
//            @Override
//            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
//
//            }
//
//            @Override
//            public void onRequestDisallowInterceptTouchEvent(boolean b) {
//
//            }
//        });

        prepareData();

        //Add Story Button Click
        ImageButton addButton = (ImageButton)v.findViewById(R.id.addButton);
        addButton.setOnClickListener(this);

        return v;
    }

    /**
     * Fetch the data from api or something ....;
     */
    private void prepareData() {

        JsonArrayRequest request = new JsonArrayRequest(URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (response == null) {
                            Toast.makeText(getActivity(), "Couldn't fetch the menu! Pleas try again.", Toast.LENGTH_LONG).show();
                            return;
                        }

                        List<Item> items = new Gson().fromJson(response.toString(), new TypeToken<List<Item>>() {
                        }.getType());

                        // adding items to cart list
                        cartList.clear();
                        cartList.addAll(items);

                        // refreshing recycler view
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error in getting json
                // Log.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getActivity(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        AppController.getInstance().addToRequestQueue(request);
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(getActivity(), "Add Story button was clicked", Toast.LENGTH_SHORT).show();
        Intent storyIntent = new Intent(getActivity(), StoryActivity.class);
        startActivity(storyIntent);
    }
}
