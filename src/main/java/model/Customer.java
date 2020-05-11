package model;

import javax.persistence.*;

/**
 * @author Michael König
 */
@Entity
@Table(name = "customer")
public class Customer {
    @Id
    @Column(name = "customer_customer_number")
    private Integer customerNumber;

    @Column(name = "customer_first_name")
    private String firstName;

    @Column(name = "customer_last_name")
    private String lastName;

    public Customer() {
    }

    public Customer(Integer customerNumber, String firstName, String lastName) {
        setCustomerNumber(customerNumber);
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Integer getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(Integer customerNumber) {
        if (this.customerNumber != null)
            throw new IllegalArgumentException("customerNumber may not be changed");
        if (customerNumber < 111111 || customerNumber > 999999)
            throw new IllegalArgumentException("Invalid customerNumber");
        this.customerNumber = customerNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Customer customer = (Customer) o;

        return getCustomerNumber() != null ? getCustomerNumber().equals(customer.getCustomerNumber()) : customer.getCustomerNumber() == null;
    }

    @Override
    public int hashCode() {
        return getCustomerNumber() != null ? getCustomerNumber().hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "customerNumber=" + customerNumber +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
