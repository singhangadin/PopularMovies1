package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.android.customviews.MyRecyclerView;
import com.example.android.model.GridAdapter;
import com.example.android.model.GridItem;
import com.example.android.model.SpKeys;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MovieGridFragment extends Fragment
{   private Context context;
    private GridAdapter mGridAdapter;
    private MyRecyclerView movieGrid;
    private ArrayList<GridItem> gridItems;
    private GridLayoutManager mgridLayoutManager;

    public MovieGridFragment()
    {   setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View V=inflater.inflate(R.layout.fragment_main, container, false);
        movieGrid=(MyRecyclerView)V.findViewById(R.id.movie_grid);
        mgridLayoutManager=new GridLayoutManager(context,2);
        movieGrid.setLayoutManager(mgridLayoutManager);
        gridItems=new ArrayList<>();
        mGridAdapter=new GridAdapter(context,gridItems);
        movieGrid.setAdapter(mGridAdapter);
        movieGrid.addOnItemTouchListener(new MyRecyclerView.OnItemClickListener() {
            @Override
            public void onClick(View V, int position) {
                Intent I=new Intent(getActivity(),DetailActivity.class);
                I.putExtra("data",gridItems.get(position).toJSONString());
                startActivity(I);
            }
        });
        return V;
    }

    @Override
    public void onStart() {
        super.onStart();
        new FetchMoviesTask().execute();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_fragment,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {   case R.id.refresh:  new FetchMoviesTask().execute();
                                return true;

            default:return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if(newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE)
        {   mgridLayoutManager.setSpanCount(4);
        }
        else
        {   mgridLayoutManager.setSpanCount(2);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
    }

    public class FetchMoviesTask extends AsyncTask<String,Void,ArrayList<GridItem>>
    {  @Override
        protected ArrayList<GridItem> doInBackground(String... params) {
            try
            {   SharedPreferences preference= PreferenceManager.getDefaultSharedPreferences(context);
                String sort=preference.getString(SpKeys.SORT_KEY,getResources().getString(R.string.sort_default));
                switch(sort)
                {   case "0":   sort="popularity.desc";
                                break;

                    case "1":   sort="vote_average.desc";
                                break;
                }
                Uri.Builder reqUri=new Uri.Builder();
                reqUri.encodedPath("https://api.themoviedb.org/3/discover/movie");
                reqUri.appendQueryParameter("api_key", SpKeys.API_KEY);
                reqUri.appendQueryParameter("sort_by",sort);
                reqUri.appendQueryParameter("page","1");
                URL reqUrl=new URL(reqUri.toString());
                HttpURLConnection con=(HttpURLConnection) reqUrl.openConnection();
                InputStream in=con.getInputStream();
                BufferedReader buff=new BufferedReader(new InputStreamReader(in));
                StringBuilder jsonResp=new StringBuilder();
                String res;
                gridItems=null;
                while((res=buff.readLine())!=null)
                {   jsonResp.append(res);
                }
                if(jsonResp.length()>0)
                {   gridItems=new ArrayList<>();
                    JSONObject respone=new JSONObject(jsonResp.toString());
                    JSONArray result=respone.getJSONArray("results");
                    for(int i=0;i<result.length();i++)
                    {   JSONObject object=result.getJSONObject(i);
                        String title=object.optString("title");
                        String poster=object.optString("poster_path");
                        String id=object.optString("id");
                        String releasedate=object.optString("release_date");
                        String voteavg=object.optString("vote_average");
                        String overview=object.optString("overview");
                        GridItem gridItem=new GridItem();
                        gridItem.setTitle(title);
                        gridItem.setId(id);
                        gridItem.setImgUrl(poster);
                        gridItem.setReleasedate(releasedate);
                        gridItem.setSummary(overview);
                        gridItem.setVoteavg(voteavg);
                        gridItems.add(gridItem);
                    }
                }
                con.disconnect();
                return gridItems;
            }
            catch(Exception e)
            {   e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<GridItem> result) {
            super.onPostExecute(result);
            if(result!=null) {
                mGridAdapter = new GridAdapter(context, result);
                movieGrid.setAdapter(mGridAdapter);
            }
            else
            {   Toast.makeText(context,"Failed To Fetch Data",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
