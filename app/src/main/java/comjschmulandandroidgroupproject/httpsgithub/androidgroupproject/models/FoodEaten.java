package comjschmulandandroidgroupproject.httpsgithub.androidgroupproject.models;

import java.util.Date;

/**
 * Created by james on 2017-03-28.
 */

public class FoodEaten extends Food {
    Date date;

    public FoodEaten(Food food, Date date){
        super(food);
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
}
