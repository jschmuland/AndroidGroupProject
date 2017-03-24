package comjschmulandandroidgroupproject.httpsgithub.androidgroupproject.models;

/**
 * Created by James Thibaudeau on 2017-03-24.
 */

public class FoodEaten {

    private String foodName;
    private String date;
    private int calories;
    private int id;

    public FoodEaten(){
    }

    public FoodEaten(String foodName, String date, int calories){
        this.foodName = foodName;
        this.date = date;
        this.calories = calories;
    }

    public FoodEaten(String foodName, String date, int calories, int id){
        this.id = id;
        this.foodName = foodName;
        this.date = date;
        this.calories = calories;
    }

    public void setFoodName(String foodName){
        this.foodName = foodName;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFoodName() {
        return foodName;
    }

    public String getDate() {
        return date;
    }

    public int getCalories(){
        return  calories;
    }

    public int getId() {
        return id;
    }
}
