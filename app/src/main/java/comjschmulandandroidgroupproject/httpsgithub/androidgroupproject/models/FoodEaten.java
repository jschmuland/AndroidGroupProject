package comjschmulandandroidgroupproject.httpsgithub.androidgroupproject.models;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class FoodEaten extends Food {
    private Date date;

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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        return df.format(date) +" " + super.toString();
    }
}
