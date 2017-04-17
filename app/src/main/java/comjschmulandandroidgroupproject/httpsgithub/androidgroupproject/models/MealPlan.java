package comjschmulandandroidgroupproject.httpsgithub.androidgroupproject.models;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * MealPlan is the model that represents a meal plan, a meal plan
 * contains meals organized in a HashMap for easy lookup by the name of the meal
 */

public class MealPlan extends ID{
    //String for meal plan name, HashMap<String, Meal> for storing meals
    private String planName;
    private HashMap<String, Meal> meals;

    //Constructors
    public MealPlan(){
        this("", new HashMap<String, Meal>());
    }

    public MealPlan(String planName){
        this(1, planName);
    }
    public MealPlan(int id, String planName){
        this(id, planName, new HashMap<String, Meal>());
    }


    public MealPlan(String planName, HashMap<String, Meal> meals){
        this(1, planName, meals);
    }

    public MealPlan(int id, String planName, HashMap<String, Meal> meals){
        setId(id);
        this.planName = planName;
        this.meals = meals;
    }

    public MealPlan(MealPlan plan){
        this(plan.getId(), plan.getPlanName(), plan.getMeals());
    }

    //addMeal(String mealName, Meal meal)
    public void addMeal(String mealName, Meal meal){
        meals.put(mealName, meal);
    }

    //removeMeal(String mealName) checks if a meal by that name exists and removes it
    public void removeMeal(String mealName) {
        Meal temp = meals.get(mealName);
        if(temp != null) {
            meals.remove(mealName);
        }
    }

    //getters and setters
    public Meal getMeal(String mealName) {
        return meals.get(mealName);
    }

    public HashMap<String, Meal> getMeals() {
        return meals;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getPlanName() {
        return planName;
    }
}
