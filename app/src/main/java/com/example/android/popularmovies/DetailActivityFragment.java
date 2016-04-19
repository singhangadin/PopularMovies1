package com.example.android.popularmovies;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {
    private String data;
    private Context context;

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View V=inflater.inflate(R.layout.fragment_detail, container, false);
        data=getActivity().getIntent().getStringExtra("data");
        ImageView img=(ImageView)V.findViewById(R.id.imageView);
        TextView rate=(TextView)V.findViewById(R.id.avg);
        TextView reldate=(TextView)V.findViewById(R.id.rel_date);
        TextView summary=(TextView)V.findViewById(R.id.summary);
        try {
            JSONObject obj=new JSONObject(data);
            rate.setText("Rated:\n"+obj.optString("avg")+"/10");
            reldate.setText("Released On:\n"+obj.optString("reldate"));
            summary.setText("Summary:\n"+obj.optString("summary"));
            Picasso.with(context).load("http://image.tmdb.org/t/p/w185/"+obj.optString("banner")).into(img);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return V;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
    }
}
