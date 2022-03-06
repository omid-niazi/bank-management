package ir.bootcamp.bank.model;

import ir.bootcamp.bank.dbutil.DatabaseConstants;

import javax.persistence.*;

@Table(name = DatabaseConstants.CUSTOMER_TABLE_NAME)
@Entity
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = DatabaseConstants.CUSTOMER_COLUMN_ID)
    private Integer id;
    @Column(name = DatabaseConstants.CUSTOMER_COLUMN_NAME)
    private String name;
    @Column(name = DatabaseConstants.CUSTOMER_COLUMN_NATIONAL_CODE)
    private String nationalCode;
    @Column(name = DatabaseConstants.CUSTOMER_COLUMN_PHONE)
    private String phone;

    public Customer() {
    }

    public Customer(Integer id, String name, String nationalCode, String phone) {
        this.id = id;
        this.name = name;
        this.nationalCode = nationalCode;
        this.phone = phone;
    }

    public Customer(String name, String nationalCode, String phone) {
        this.id = Integer.MIN_VALUE;
        this.name = name;
        this.nationalCode = nationalCode;
        this.phone = phone;
    }

    public Integer id() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String name() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String nationalCode() {
        return nationalCode;
    }

    public void setNationalCode(String nationalCode) {
        this.nationalCode = nationalCode;
    }

    public String phone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
