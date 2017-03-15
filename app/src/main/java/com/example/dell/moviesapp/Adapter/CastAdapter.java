package com.example.dell.moviesapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dell.moviesapp.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by DELL on 06-Mar-17.
 */

public class CastAdapter extends RecyclerView.Adapter<CastAdapter.ViewHolder> {

    Context mContext;

    int length;
    JSONObject castdata;

    String BASE_POSTER_URL = "http://image.tmdb.org/t/p/w342/";

    String [] cast_photo;
    String [] cast_name;
    String [] cast_role;

    public CastAdapter (String castJson, Context context) {

        mContext = context;

        if (castJson != null) {
            try {
                castdata = new JSONObject(castJson);
                JSONArray results = castdata.getJSONArray("cast");

                length = results.length();

                cast_photo = new String[length];
                cast_name = new String[length];
                cast_role = new String[length];

                for (int i = 0; i < length; i++) {
                    JSONObject curResult = results.getJSONObject(i);

                    String path = curResult.getString("profile_path");
                    if (path.equals("null")) {
                        cast_photo[i] = "null";
                    } else {
                        cast_photo[i] = BASE_POSTER_URL + curResult.getString("profile_path");
                    }
                    cast_name[i] = curResult.getString("name");
                    cast_role[i] = curResult.getString("character");
                }
            } catch (JSONException e) {
                Log.e("JSON", "Can't get data");
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cast_crew, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if (cast_photo[position].equals("null")) {
            holder.image.setImageResource(R.drawable.no_image);
        } else {
            Picasso.with(mContext)
                    .load(cast_photo[position])
                    .into(holder.image);
        }

        holder.castname.setText(cast_name[position]);
        holder.role.setText(cast_role[position]);
    }

    @Override
    public int getItemCount() {
        return length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.cast_image)
        public ImageView image;
        @Bind(R.id.cast_name)
        public TextView castname;
        @Bind(R.id.cast_role)
        public TextView role;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
