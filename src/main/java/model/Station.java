package model;

import javax.persistence.*;

/**
 * @author Michael König
 */
@Entity
@Table(name = "station")
@SequenceGenerator(name = "station_seq", sequenceName = "station_id_seq")
public class Station {
    @Id
    @GeneratedValue(generator = "station_seq")
    @Column(name = "station_id")
    private Integer id;

    @Column(name = "station_city")
    private String city;

    public Station() {
    }

    public Station(String city) {
        this.city = city;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Station station = (Station) o;

        return getId() != null ? getId().equals(station.getId()) : station.getId() == null;
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Station{" +
                "id=" + id +
                ", city='" + city + '\'' +
                '}';
    }
}
