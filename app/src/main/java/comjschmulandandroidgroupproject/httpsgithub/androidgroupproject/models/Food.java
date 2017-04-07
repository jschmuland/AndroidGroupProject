package comjschmulandandroidgroupproject.httpsgithub.androidgroupproject.models;



public class Food extends ID{
    private String foodName;
    private int calories;

    public Food(){
        this("", 0);
    }

    public Food(String foodName, int calories){
        this(0, foodName, calories);
    }

    public Food(int id, String foodName, int calories){
        setId(id);
        this.foodName = foodName;
        this.calories = calories;
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

    public String getFoodName() {
        return foodName;
    }

    public int getCalories(){
        return  calories;
    }

    @Override
    public String toString() {
        return foodName + " " + calories;
    }
}
