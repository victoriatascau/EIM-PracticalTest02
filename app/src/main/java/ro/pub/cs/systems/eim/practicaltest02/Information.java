package ro.pub.cs.systems.eim.practicaltest02;

public class Information {

    private String data;

    public Information() {
        this.data = new String();
    }

    public Information(String data) {
        this.data = data;
    }

    public String getData() {return data;}

    public void setData(String data) {this.data = data;}

}
