package comjschmulandandroidgroupproject.httpsgithub.androidgroupproject.models;

import java.util.ArrayList;

public class Meal {
    private int id;
    private String mealName;
    private ArrayList<Food> foods;

    public Meal(){
        this.mealName = "";
        this.foods = new ArrayList<>();
        this.id = 0;
    }

    public Meal(String mealName) {
        this();
        this.mealName = mealName;
    }

    public Meal(String mealName, ArrayList<Food> foods){
        this(mealName);
        this.foods = foods;
    }

    public Meal(int id, String mealName, ArrayList<Food> foods){
        this(mealName, foods);
        this.id = id;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
