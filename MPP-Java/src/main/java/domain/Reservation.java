package domain;

public class Reservation extends Entity<Long>{
    private String name;
    private Long phoneNumber;
    private Long ticketsNumber;
    private Long excursionID;

    public Reservation(String name, Long phoneNumber, Long ticketsNumber, Long excursionID) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.ticketsNumber = ticketsNumber;
        this.excursionID = excursionID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(Long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Long getTicketsNumber() {
        return ticketsNumber;
    }


    public void setTicketsNumber(Long ticketsNumber) {
        this.ticketsNumber = ticketsNumber;
    }

    public Long getExcursionID() {
        return excursionID;
    }

    public void setExcursionID(Long excursionID) {
        this.excursionID = excursionID;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "name='" + name + '\'' +
                ", phoneNumber=" + phoneNumber +
                ", ticketsNumber=" + ticketsNumber +
                ", excursionID=" + excursionID +
                '}';
    }


}
