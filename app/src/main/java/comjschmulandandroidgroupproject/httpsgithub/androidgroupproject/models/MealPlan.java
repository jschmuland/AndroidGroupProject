package comjschmulandandroidgroupproject.httpsgithub.androidgroupproject.models;

import java.util.ArrayList;
import java.util.HashMap;


public class MealPlan extends ID{
    private String planName;
    private HashMap<String, Meal> meals;


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

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getPlanName() {
        return planName;
    }
}
