package comjschmulandandroidgroupproject.httpsgithub.androidgroupproject.models;


import java.util.Date;

public class ExerciseRecords {
    private String exerciseName, date;
    private double duration, calories;
    private int id;

    public ExerciseRecords() {

    }

    public ExerciseRecords(String exerciseName, double duration, String date, double calories) {
        this.exerciseName = exerciseName;
        this.duration = duration;
        this.date = date;
        this.calories = calories;
    }

    public ExerciseRecords(int id, String exerciseName, double duration, String date, double calories) {
        this(exerciseName, duration, date, calories);
        this.id = id;

    }

    public double getCalories() {
        return calories;
    }

    public void setCalories(double calories) {
        this.calories = calories;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public double getDuration() {
        return duration;
    }

    public String getDate() {
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

    public void setDate(String date) {
        this.date = date;
    }

    public void setId(int id) {
        this.id = id;
    }


}
