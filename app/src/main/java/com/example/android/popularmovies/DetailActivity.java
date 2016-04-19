package com.example.android.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import org.json.JSONException;
import org.json.JSONObject;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        try {
            JSONObject data=new JSONObject(getIntent().getStringExtra("data"));
            toolbar.setTitle(data.optString("title"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(savedInstanceState==null)
        {   getSupportFragmentManager().beginTransaction().add(R.id.fragment,new DetailActivityFragment()).commit();
        }
    }

}
