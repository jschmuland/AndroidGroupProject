package comjschmulandandroidgroupproject.httpsgithub.androidgroupproject.models;

import java.util.ArrayList;

public class Meal extends ID{
    private String mealName;
    private ArrayList<Food> foods;

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

    public void addFood(Food food){
        foods.add(food);
    }

    public void removeFood(Food food) {
        if(foods.contains(food)){
            foods.remove(food);
        }
    }

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
