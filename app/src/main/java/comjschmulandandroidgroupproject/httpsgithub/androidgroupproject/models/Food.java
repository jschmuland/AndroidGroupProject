package comjschmulandandroidgroupproject.httpsgithub.androidgroupproject.models;

import java.util.Date;

/**
 * Created by James Thibaudeau on 2017-03-24.
 */

public class Food {

    private String foodName;
    private int calories;
    private int id;

    public Food(){
        this.foodName = "";
        this.calories = 0;
        this.id = 0;
    }

    public Food(String foodName, int calories){
        this();
        this.foodName = foodName;
        this.calories = calories;
    }

    public Food(int id, String foodName, int calories){
        this(foodName, calories);
        this.id = id;
    }

    public Food(Food food) {
        this(food.getId(), food.getFoodName(), food.getCalories());
    }

    public void setFoodName(String foodName){
        this.foodName = foodName;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFoodName() {
        return foodName;
    }

    public int getCalories(){
        return  calories;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return foodName + " " + calories;
    }
}
