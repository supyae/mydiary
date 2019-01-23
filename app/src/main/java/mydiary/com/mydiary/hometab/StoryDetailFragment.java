package mydiary.com.mydiary.hometab;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import mydiary.com.mydiary.Item;
import mydiary.com.mydiary.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class StoryDetailFragment extends Fragment {

    private View v;

    private Item item;
    private TextView title, description;
    private ImageView img;


    public StoryDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_story_detail, container, false);

        //get the passed data;
        item = (Item) getArguments().getSerializable("story");

        Log.d("obj_selected ", item.getTitle());

        //setting layout
        title = v.findViewById(R.id.title);
        title.setText(item.getTitle());

        description = v.findViewById(R.id.description);
        description.setText(item.getDescription());

        img = v.findViewById(R.id.img1);

        Glide.with(getContext())
                .load(item.getThumbnail())
                .into(img);

        // Inflate the layout for this fragment
        return v;
    }

}
