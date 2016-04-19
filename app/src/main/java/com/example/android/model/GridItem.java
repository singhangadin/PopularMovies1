package com.example.android.model;

import org.json.JSONException;
import org.json.JSONObject;

/**<p>
 * Created by Angad on 08/04/2016.
 * </p>
 */
public class GridItem
{   private String imgUrl,title,id;
    private String releasedate,voteavg,summary;

    public GridItem() {
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReleasedate() {
        return releasedate;
    }

    public void setReleasedate(String releasedate) {
        this.releasedate = releasedate;
    }

    public String getVoteavg() {
        return voteavg;
    }

    public void setVoteavg(String voteavg) {
        this.voteavg = voteavg;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String toJSONString()
    {   JSONObject obj=new JSONObject();
        try
        {   obj.put("id",id);
            obj.put("title",title);
            obj.put("banner",imgUrl);
            obj.put("reldate",releasedate);
            obj.put("summary",summary);
            obj.put("avg",voteavg);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return obj.toString();
    }
}