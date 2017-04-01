package comjschmulandandroidgroupproject.httpsgithub.androidgroupproject.models;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by james on 2017-03-28.
 */

public class FoodEaten extends Food {
    private Date date;

    public FoodEaten(Food food, Date date){
        super(food);
        this.date = date;
    }

    public FoodEaten(String foodName, int calories, Date date){
        super(foodName, calories);
        this.date = date;
    }

    public FoodEaten(int id, String foodName, int calories, Date date){
        super(id, foodName, calories);
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd hh:mm");
        return df.format(date) +" " + super.toString();
    }
}
