package domain;

import org.json.JSONObject;

import java.sql.Time;
import java.time.LocalTime;
import java.util.Map;
import java.util.Objects;

public class Excursion extends Entity{


    private String objective;
    private String company;
    private float price;
    private LocalTime time;
    private Long seats;
    private String ormTime;

    public String getOrmTime() {
        return ormTime;
    }

    public void setOrmTime(String ormTime) {
        this.ormTime = ormTime;
        this.time = LocalTime.parse(ormTime);
    }

    public Excursion(){}

    public Excursion(String objective, String company, float price, LocalTime time, Long seats) {
        this.objective = objective;
        this.company = company;
        this.price = price;
        this.time = time;
        this.seats = seats;
    }

    public String getObjective() {
        return objective;
    }

    public void setObjective(String objective) {
        this.objective = objective;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public Long getSeats() {
        return seats;
    }

    public void setSeats(Long seats) {
        this.seats = seats;
    }

    @Override
    public String toString() {
        return "Excursion{" +
                "objective='" + objective + '\'' +
                ", company='" + company + '\'' +
                ", price=" + price +
                ", time=" + time +
                ", seats=" + seats +
                '}';
    }

    public JSONObject toJSON() {
        return new JSONObject(Map.ofEntries(
                Map.entry("id", getId()),
                Map.entry("objective", objective),
                Map.entry("company", company),
                Map.entry("price", price),
                Map.entry("time", time.toString()),
                Map.entry("seats", seats)
        ));
    }
    public Excursion(JSONObject obj) {
        this.setId(obj.getLong("id"));
        this.objective = obj.getString("objective");
        this.company = obj.getString("company");
        this.time = LocalTime.parse(obj.getString("time"));
        this.price = obj.getFloat("price");
        this.seats = obj.getLong("seats");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Excursion excursion = (Excursion) o;
        return Objects.equals(getId(), excursion.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getId());
    }


}
