package comjschmulandandroidgroupproject.httpsgithub.androidgroupproject.models;

import java.util.ArrayList;

/**
 * Meal is the model for meals, a meal has a name and can consist of many foods
 */

public class Meal extends ID{
    //String for name of the meal and ArrayList<Food> for the list of foods
    private String mealName;
    private ArrayList<Food> foods;

    //Constructors
    public Meal(){
        this("");
    }

    public Meal(String mealName) {
        this(mealName, new ArrayList<Food>() );
    }

    public Meal(String mealName, ArrayList<Food> foods){
        this(0, mealName, foods);
    }

    public Meal(int id, String mealName, ArrayList<Food> foods){
        this.mealName = mealName;
        this.foods = foods;
        setId(id);
    }

    //addFood(Food food) takes a food object and adds it to the list of foods
    public void addFood(Food food){
        foods.add(food);
    }

    //removeFood(Food food) takes a food object and if that object is in the list it removes it
    public void removeFood(Food food) {
        if(foods.contains(food)){
            foods.remove(food);
        }
    }

    //getters and setters
    public Food getFood(int index){
        return foods.get(index);
    }

    public ArrayList<Food> getFoods() {
        return foods;
    }

    public void setFoods(ArrayList<Food> foods) {
        this.foods = foods;
    }

    public String getMealName() {
        return mealName;
    }

    public void setMealName(String mealName) {
        this.mealName = mealName;
    }
}
