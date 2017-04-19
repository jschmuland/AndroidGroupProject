package comjschmulandandroidgroupproject.httpsgithub.androidgroupproject.models;

/**
 * Created by carlo on 4/5/2017. Exercise Records Class for the Exercise Activity
 */
public class ExerciseRecords extends ID{
    private String exerciseName, date;
    private double duration, calories;

    public ExerciseRecords() {
        this("", 0, "", 0);
    }

    public ExerciseRecords(String exerciseName, double duration, String date, double calories) {
        this(0, exerciseName, duration, date, calories);
    }

    public ExerciseRecords(int id, String exerciseName, double duration, String date, double calories) {
        this.exerciseName = exerciseName;
        this.duration = duration;
        this.date = date;
        this.calories = calories;
        setId(id);

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

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public void setDate(String date) {
        this.date = date;
    }

}//end class
