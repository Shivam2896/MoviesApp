package com.example.dell.moviesapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AccountInfo extends AppCompatActivity {

    public static final String user_name = "USER_NAME";
    public static final String user_email = "USER_EMAIL";
    public static final String user_photo = "USER_PHOTO";

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.avatar)
    ImageView display_avatar;
    @Bind(R.id.name)
    TextView display_name;
    @Bind(R.id.email)
    TextView display_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_info);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        String name = bundle.getString(user_name);
        String email = bundle.getString(user_email);
        String photo = bundle.getString(user_photo);

        display_name.setText(name);
        display_email.setText(email);

        Picasso.with(this)
                .load(photo)
                .into(display_avatar);
    }
}
