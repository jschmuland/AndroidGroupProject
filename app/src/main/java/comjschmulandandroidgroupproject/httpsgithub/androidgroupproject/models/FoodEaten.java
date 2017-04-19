package comjschmulandandroidgroupproject.httpsgithub.androidgroupproject.models;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * FoodEaten is the model for the food a user eats, it extends food by adding a Date
 */

public class FoodEaten extends Food {
    //Date date for eating the food
    private Date date;

    //constructors
    public FoodEaten(){
        this("", 0, new Date(), "");
    }

    public FoodEaten(String foodName, int calories, Date date, String description){
        this(0, foodName, calories, date, description);
    }

    public FoodEaten(int id, String foodName, int calories, Date date, String description){
        super(id, foodName, calories, description);
        this.date = date;
    }

    public FoodEaten(Food food, Date date){
        super(food);
        this.date = date;
    }

    //getters and setters
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    //toString prints out the date and the food toString()
    @Override
    public String toString() {
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        return df.format(date) +" " + super.toString();
    }
}
