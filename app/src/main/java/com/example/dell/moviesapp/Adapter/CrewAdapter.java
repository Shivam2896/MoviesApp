package com.example.dell.moviesapp.Adapter;

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

/**
 * Created by DELL on 06-Mar-17.
 */

public class CrewAdapter extends RecyclerView.Adapter<CrewAdapter.ViewHolder> {

    Context mContext;

    int length;
    JSONObject crewdata;

    String BASE_POSTER_URL = "http://image.tmdb.org/t/p/w342/";

    String [] crew_photo;
    String [] crew_name;
    String [] crew_role;

    public CrewAdapter (String crewJson, Context context) {

        mContext = context;

        if (crewJson != null) {
            try {
                crewdata = new JSONObject(crewJson);
                JSONArray results = crewdata.getJSONArray("crew");

                length = results.length();

                crew_photo = new String[length];
                crew_name = new String[length];
                crew_role = new String[length];

                for (int i = 0; i < length; i++) {
                    JSONObject curResult = results.getJSONObject(i);

                    String path = curResult.getString("profile_path");
                    if (path.equals("null")) {
                        crew_photo[i] = "null";
                    } else {
                        crew_photo[i] = BASE_POSTER_URL + curResult.getString("profile_path");
                    }
                    crew_name[i] = curResult.getString("name");
                    crew_role[i] = curResult.getString("department");
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

        if (crew_photo[position].equals("null")) {
            holder.image.setImageResource(R.drawable.no_image);
        } else {
            Picasso.with(mContext)
                    .load(crew_photo[position])
                    .into(holder.image);
        }

        holder.crewname.setText(crew_name[position]);
        holder.role.setText(crew_role[position]);
    }

    @Override
    public int getItemCount() {
        return length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView image;
        public TextView crewname;
        public TextView role;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.cast_image);
            crewname = (TextView) itemView.findViewById(R.id.cast_name);
            role = (TextView) itemView.findViewById(R.id.cast_role);
        }
    }
}
