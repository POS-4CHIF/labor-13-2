package model;

import javax.persistence.*;
import java.util.List;

/**
 * @author Michael König
 */
@Entity
@Table(name = "car")
public class Car {
    @Id
    @Column(name = "car_registration_nr")
    private String registrationNr;

    @Column(name = "car_constructionYear")
    private Integer constructionYear;

    @Column(name = "car_milage", nullable = false)
    private Integer milage;

    @Column(name = "car_model")
    private String model;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "car_station_id")
    private Station location;

    @OneToMany(mappedBy = "car", cascade = CascadeType.MERGE)
    private List<Rental> rentals;

    public Car() {
    }

    public Car(String registrationNr, Integer constructionYear, Integer milage, String model, Station location, List<Rental> rentals) {
        this.registrationNr = registrationNr;
        this.constructionYear = constructionYear;
        this.milage = milage;
        this.model = model;
        this.location = location;
        this.rentals = rentals;
    }

    public String getRegistrationNr() {
        return registrationNr;
    }

    public void setRegistrationNr(String registrationNr) {
        this.registrationNr = registrationNr;
    }

    public Integer getConstructionYear() {
        return constructionYear;
    }

    public void setConstructionYear(Integer constructionYear) {
        this.constructionYear = constructionYear;
    }

    public Integer getMilage() {
        return milage;
    }

    public void setMilage(Integer milage) {
        this.milage = milage;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Station getLocation() {
        return location;
    }

    public void setLocation(Station location) {
        for (Rental rental : getRentals()) {
            if (rental.getReturnDate() == null)
                throw new IllegalArgumentException("Can't set location, car is currently rented");
        }
        this.location = location;
    }

    public List<Rental> getRentals() {
        return rentals;
    }

    public void setRentals(List<Rental> rentals) {
        this.rentals = rentals;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Car car = (Car) o;

        return getRegistrationNr() != null ? getRegistrationNr().equals(car.getRegistrationNr()) : car.getRegistrationNr() == null;
    }

    @Override
    public int hashCode() {
        return getRegistrationNr() != null ? getRegistrationNr().hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Car{" +
                "registrationNr='" + registrationNr + '\'' +
                ", constructionYear=" + constructionYear +
                ", milage=" + milage +
                ", model='" + model + '\'' +
                ", location=" + location +
                ", rental=" + rentals.size() +
                '}';
    }
}
