package domain;

import java.time.LocalTime;

public class Excursion extends Entity<Long>{
    private String company;
    private float price;
    private LocalTime time;
    private Long seats;

    public Excursion(String company, float price, LocalTime time, Long seats) {
        this.company = company;
        this.price = price;
        this.time = time;
        this.seats = seats;
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
}
