import Content.java;

public Class Post extends Content{
    private static float latitude;
    private static float longitude;
    private float lastKnownDist;
    private List<String> tags = new ArrayList<String>();

    public float getDistance(){
        return self.lastKnownDist;
    }

    public ArrayList<String> getTags(){
        return self.tags;
    }

    public float updateDistance(float lat, float lon){
        self.lastKnownDist =
        Math.acos(Math.sin(lat)*Math.sin(latitude)+Math.cos(lat)*Math.cos(latitude)*Math.cos(longitude-lon))*6371000;
        return self.lastKnownDist;
    }
}

public Class PostDistanceComparator implements Comparator<Post>{
    public int compare(Post post1, Post post2){
        return post1.getDistance() - post2.getDistance()
    }
}

