package model;

import com.sun.istack.Nullable;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * @author Michael König
 */
@Entity
@Table(name = "rental")
@SequenceGenerator(name = "rental_seq", sequenceName = "rental_id_seq")
public class Rental {
    @Id
    @GeneratedValue(generator = "rental_seq")
    @Column(name = "rental_id")
    private Integer id;

    @Column(name = "rental_km")
    private Integer km;

    @Column(name = "rental_rentalDate")
    private LocalDate rentalDate;

    @Column(name = "rental_returnDate")
    private LocalDate returnDate;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "rental_rental_station_id", nullable = false)
    private Station rentalStation;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "rental_return_station_id")
    private Station returnStation;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "rental_registration_nr", nullable = false)
    private Car car;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "rental_customer_number")
    private Customer driver;

    public Rental() {

    }

    public Rental(Integer km, LocalDate rentalDate, LocalDate returnDate, Station rentalStation, Station returnStation, Car car, Customer driver) {
        this.km = km;
        this.rentalDate = rentalDate;
        this.returnDate = returnDate;
        this.rentalStation = rentalStation;
        this.returnStation = returnStation;
        setCar(car);
        this.driver = driver;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getKm() {
        return km;
    }

    public void setKm(Integer km) {
        car.setMilage(car.getMilage() + km);
        this.km = km;
    }

    public LocalDate getRentalDate() {
        return rentalDate;
    }

    public void setRentalDate(LocalDate rentalDate) {
        this.rentalDate = rentalDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        if (returnDate.isBefore(rentalDate))
            throw new IllegalArgumentException("returnDate must be after rentalDate");
        this.returnDate = returnDate;
    }

    public Station getRentalStation() {
        return rentalStation;
    }

    public void setRentalStation(Station rentalStation) {
        if (this.rentalStation != null)
            throw new IllegalArgumentException("rentalStation cannot not be changed");
        this.rentalStation = rentalStation;
    }

    public Station getReturnStation() {
        return returnStation;
    }

    public void setReturnStation(Station returnStation) {
        this.returnStation = returnStation;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        if (this.car != null)
            throw new IllegalArgumentException("Car cannot be changed");
        if (car == null)
            throw new IllegalArgumentException("Car must not be null");
        if (car.getLocation() != null)
            throw new IllegalArgumentException("Car is currently at a station");
        this.car = car;
    }

    public Customer getDriver() {
        return driver;
    }

    public void setDriver(Customer driver) {
        if (this.driver != null)
            throw new IllegalArgumentException("driver cannot not be changed");
        this.driver = driver;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Rental rental = (Rental) o;

        return getId() != null ? getId().equals(rental.getId()) : rental.getId() == null;
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }

    @Override
    public String
    toString() {
        return "Rental{" +
                "id=" + id +
                ", km=" + km +
                ", rentalDate=" + rentalDate +
                ", returnDate=" + returnDate +
                ", rentalStation=" + rentalStation +
                ", returnStation=" + returnStation +
                ", car=" + car +
                ", driver=" + driver +
                '}';
    }
}
