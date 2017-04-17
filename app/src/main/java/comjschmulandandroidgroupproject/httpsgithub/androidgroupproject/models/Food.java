package comjschmulandandroidgroupproject.httpsgithub.androidgroupproject.models;

/**
 * Food class is the model for food used in meal plans
 */

public class Food extends ID{
    //String for food name, int for calories and String for food description
    private String foodName;
    private int calories;
    private String description;

    //Constructors
    public Food(){
        this("", 0, "");
    }

    public Food(String foodName, int calories, String description){
        this(0, foodName, calories, description);
    }

    public Food(int id, String foodName, int calories, String description){
        setId(id);
        this.foodName = foodName;
        this.calories = calories;
        this.description = description;
    }

    public Food(Food food) {
        this(food.getId(), food.getFoodName(), food.getCalories(), food.getDescription());
    }

    //Getters and setters
    public void setFoodName(String foodName){
        this.foodName = foodName;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public String getFoodName() {
        return foodName;
    }

    public int getCalories(){
        return  calories;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    //toString returns the food name and calories
    @Override
    public String toString() {
        return foodName + " " + calories;
    }
}
