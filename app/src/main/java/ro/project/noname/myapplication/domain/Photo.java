package ro.project.noname.myapplication.domain;

/**
 * Created by ovidiumihota on 13/01/16.
 */
public class Photo {
    private String id;
    private String url;
    private int totalLikes;

    public Photo(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getTotalLikes() {
        return totalLikes;
    }

    public void setTotalLikes(int totalLikes) {
        this.totalLikes = totalLikes;
    }
}
