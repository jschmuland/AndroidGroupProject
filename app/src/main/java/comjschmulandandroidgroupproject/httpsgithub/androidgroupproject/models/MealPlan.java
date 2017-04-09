package comjschmulandandroidgroupproject.httpsgithub.androidgroupproject.models;

import java.util.ArrayList;
import java.util.HashMap;


public class MealPlan {
    private int id;
    private String planName;
    private HashMap<String, Meal> meals;

    public MealPlan(){
        this.id = 0;
        this.planName ="";
        this.meals = new HashMap<>();
    }
    public MealPlan(String planName){
        this.planName = planName;


    }
    public MealPlan(String planName, HashMap<String, Meal> meals){
        this.planName = planName;
        this.meals = meals;
        this.meals = new HashMap<>();
    }

    public MealPlan(int id, String planName, HashMap<String, Meal> meals){
        this(planName, meals);
        this.id = id;
    }

    public MealPlan(MealPlan plan){
        this(plan.getId(), plan.getPlanName(), plan.getMeals());
    }

    public void addMeal(String mealName, Meal meal){
        meals.put(mealName, meal);
    }

    public void removeMeal(String mealName) {
        Meal temp = meals.get(mealName);
        if(temp != null) {
            meals.remove(mealName);
        }
    }

    public Meal getMeal(String mealName) {
        return meals.get(mealName);
    }

    public HashMap<String, Meal> getMeals() {
        return meals;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getPlanName() {
        return planName;
    }
}
