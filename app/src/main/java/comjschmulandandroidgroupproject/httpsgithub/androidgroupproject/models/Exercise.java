package comjschmulandandroidgroupproject.httpsgithub.androidgroupproject.models;


import java.util.Date;

public class Exercise {
    private String exerciseName;
    private double duration;
    private Date date;
    private int id;

    public Exercise(){

    }

    public Exercise(String exerciseName, double duration, Date date){
        this.exerciseName = exerciseName;
        this.duration = duration;
        this.date = date;
    }

    public Exercise(int id, String exerciseName, double duration, Date date){
        this(exerciseName, duration, date);
        this.id = id;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public double getDuration() {
        return duration;
    }

    public Date getDate() {
        return date;
    }

    public int getId() {
        return id;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setId(int id) {
        this.id = id;
    }
}
