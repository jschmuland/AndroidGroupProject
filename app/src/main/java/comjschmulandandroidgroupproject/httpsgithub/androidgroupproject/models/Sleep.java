package comjschmulandandroidgroupproject.httpsgithub.androidgroupproject.models;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Sleep is a model that represents a nights sleep with a date and a duration for time slept
 */

public class Sleep extends ID {
    //Date for date and int for duration
    private Date date;
    private int duration;

    //Constructors
    public Sleep(){
        this(new Date(), 0);
    }

    public Sleep(Date date, int duration){
        this(0, date, duration);
    }

    public Sleep(int id, Date date, int duration){
        this.date = date;
        this.duration = duration;
        setId(id);
    }

    //getters and setters
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

    //toString() returns a String of the date and hours slept
    @Override
    public String toString(){
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        return df.format(date) + " " + duration + "Hours slept";
    }

}

