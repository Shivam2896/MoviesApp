package com.example.dell.moviesapp.Adapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.moviesapp.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by DELL on 10-Mar-17.
 */

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.ViewHolder> {

    Context mContext;

    int length;
    JSONObject videosData;

    String BASE_THUMBNAIL_URL = "http://img.youtube.com/vi/";

    String [] video_name;
    String [] video_thumbnail;
    String [] video_id;

    public VideosAdapter(String videosJson, Context context) {

        mContext = context;

        if (videosJson != null) {
            try {
                videosData = new JSONObject(videosJson);
                JSONArray results = videosData.getJSONArray("results");

                length = results.length();

                video_name = new String[length];
                video_thumbnail = new String[length];
                video_id = new String[length];

                for (int i = 0; i < length; i++) {
                    JSONObject curResult = results.getJSONObject(i);

                    String key = curResult.getString("key");
                    video_name [i] = curResult.getString("name");
                    video_thumbnail[i] = BASE_THUMBNAIL_URL + key + "/maxresdefault.jpg";
                    video_id[i] = key;
                }
            } catch (JSONException e) {
                Log.e("JSON", "Can't get data");
            }
        }
    }

    @Override
    public VideosAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VideosAdapter.ViewHolder holder, final int position) {

        Picasso.with(mContext)
                .load(video_thumbnail[position])
                .into(holder.thumbnail);

        holder.video_name.setText(video_name[position]);

        holder.play_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                watchVideo(video_id[position]);
                Toast.makeText(mContext, "Clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void watchVideo (String videoId) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + videoId));
        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + videoId));

        try {
            mContext.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            mContext.startActivity(webIntent);
        }
    }
    @Override
    public int getItemCount() {
        return length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView thumbnail;
        public TextView video_name;
        public ImageButton play_button;

        public ViewHolder(View itemView) {
            super(itemView);
            thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
            video_name = (TextView) itemView.findViewById(R.id.video_name);
            play_button = (ImageButton) itemView.findViewById(R.id.play_button);
        }
    }
}
