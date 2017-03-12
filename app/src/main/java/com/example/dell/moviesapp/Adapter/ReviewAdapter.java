package com.example.dell.moviesapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.dell.moviesapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by DELL on 10-Mar-17.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    Context mContext;

    int length;
    JSONObject videosData;

    String [] name;
    String [] text;
    String [] url;

    public ReviewAdapter(String videosJson, Context context) {

        mContext = context;

        if (videosJson != null) {
            try {
                videosData = new JSONObject(videosJson);
                JSONArray results = videosData.getJSONArray("results");

                length = results.length();

                name = new String[length];
                text = new String[length];
                url = new String[length];

                for (int i = 0; i < length; i++) {
                    JSONObject curResult = results.getJSONObject(i);

                    name[i] = curResult.getString("author");
                    text[i] = curResult.getString("content");
                    url[i] = curResult.getString("url");
                }
            } catch (JSONException e) {
                Log.e("JSON", "Can't get data");
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        if (length != 0) {
            holder.review_name.setText(name[position]);
            holder.review_text.setText(text[position]);
            holder.read_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url[position]));
                    mContext.startActivity(intent);
                }
            });
        } else {
            holder.review_name.setVisibility(View.GONE);
            holder.review_text.setVisibility(View.GONE);
            holder.read_more.setVisibility(View.GONE);
            holder.empty.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        if (length == 0) {
            return 1;
        }
        return length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.review_name)
        public TextView review_name;
        @Bind(R.id.review_text)
        public TextView review_text;
        @Bind(R.id.empty)
        public TextView empty;
        @Bind(R.id.read_more)
        public Button read_more;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
