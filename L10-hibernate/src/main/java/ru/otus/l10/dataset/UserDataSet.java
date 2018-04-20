package ru.otus.l10.dataset;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class UserDataSet extends DataSet {

    @Column(name = "name")
    private String name;

    @OneToOne(cascade = CascadeType.ALL)
    private AddressDataSet address;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PhoneDataSet> phones = new ArrayList<>();

    public UserDataSet() {
    }

    public UserDataSet(String name, PhoneDataSet phone, AddressDataSet address) {
        this.setId(-1);
        this.name = name;
        this.address = address;
        phones.add(phone);
        phone.setUser(this);
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getName() {
        return name;
    }


    public PhoneDataSet getPhone() {
        return phones.get(0);
    }

    public void addPhone(PhoneDataSet phone) {
        phones.add(phone);
        phone.setUser(this);
    }

    public void removePhone(PhoneDataSet phone) {
        phones.remove(phone);
        phone.setUser(null);
    }

    public AddressDataSet getAddress() {
        return address;
    }

    public void setAddress(AddressDataSet address) {
        this.address = address;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("User: \n");
        sb.append("  Id: ").append(getId()).append("\n");
        sb.append("  Name: ").append(name).append("\n");
        sb.append("  Address: ").append(address != null ? address.getStreet(): "").append("\n");
        for (PhoneDataSet pds : phones) {
            sb.append("  Phone: ").append(pds.getNumber()).append("\n");
        }
        return sb.toString();
    }
}
