package comjschmulandandroidgroupproject.httpsgithub.androidgroupproject.models;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Sleep {
    private int id;
    private Date date;
    private int duration;

    public Sleep(){
        this.id = 0;
        this.date = new Date();
        this.duration = 0;
    }

    public Sleep(Date date, int duration){
        this.date = date;
        this.duration = duration;
    }

    public Sleep(int id, Date date, int duration){
        this(date, duration);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String toString(){
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        return df.format(date) + " " + duration + "Hours slept";
    }

}
