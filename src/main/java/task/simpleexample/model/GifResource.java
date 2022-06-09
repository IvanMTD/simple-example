package task.simpleexample.model;

import java.util.List;

public class GifResource {
    private List<Data> data;

    public GifResource(){

    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public Data getRandomData(){
        int value = Math.round((float)Math.random() * (float)(data.size() - 1));
        return data.get(value);
    }
}
