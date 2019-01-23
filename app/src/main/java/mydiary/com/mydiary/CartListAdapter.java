package mydiary.com.mydiary;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import mydiary.com.mydiary.hometab.StoryDetailFragment;
import mydiary.com.mydiary.hometab.StoryFragment;

public class CartListAdapter extends RecyclerView.Adapter<CartListAdapter.MyViewHolder> {

    private StoryFragment context;
    private ArrayList<Item> cartList;

    public CartListAdapter(StoryFragment context, ArrayList<Item> cartList) {
        this.context = context;
        this.cartList = cartList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView title, description, date;
        public ImageView thumbnail;
        public RelativeLayout viewBackground, viewForeground;


        public MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            description = view.findViewById(R.id.description);
            date = view.findViewById(R.id.date);
            thumbnail = view.findViewById(R.id.thumbnail);
            //viewBackground = view.findViewById(R.id.view_background);
            viewForeground = view.findViewById(R.id.view_foreground);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            Item item = cartList.get(getPosition());
            int position = getPosition();
            Log.d("Title", item.getTitle());
            Log.d("Pos", String.valueOf(getPosition()));
//
            Toast.makeText(v.getContext(), item.getTitle(), Toast.LENGTH_SHORT).show();

            FragmentManager fmg = context.getActivity().getSupportFragmentManager();
            FragmentTransaction fgT = fmg.beginTransaction();

            //pass selected data;
            Bundle bundle = new Bundle();
            bundle.putSerializable("story", item);

            StoryDetailFragment storyDetailFragment = new mydiary.com.mydiary.hometab.StoryDetailFragment();
            storyDetailFragment.setArguments(bundle);

            fgT.replace(R.id.story, storyDetailFragment);
            //fgT.addToBackStack(null);

            fgT.commit();
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.story_item_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Item item = cartList.get(position);
        holder.title.setText(item.getTitle());
        holder.description.setText(item.getDescription());
        holder.date.setText(item.getDate());

        Glide.with(context)
                .load(item.getThumbnail())
                .into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public void removeItem(int position) {
        cartList.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public void restoreItem(Item item, int position) {
        cartList.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
    }

}
