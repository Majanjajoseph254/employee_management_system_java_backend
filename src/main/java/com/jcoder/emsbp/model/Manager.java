package com.jcoder.emsbp.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Setter
@Getter
@Entity
@Table(name = "manager_table")
public class Manager {
    @Id
    @Column(name ="manager_id")
    private Long id;
    @Column(name = "manager_name",nullable = false)
    private String name;
    @Column(name = "manager_email",nullable = false,unique = true)
    private String email;
    @Column(name = "manager_password",nullable = false)
    private String password;
    @Column(name = "manager_department",nullable = false)
    private String department;
    @Column(name = "manager_contact",nullable = false,unique = true)
    private String contact;

    @OneToMany(mappedBy = "manager",cascade = CascadeType.ALL)
    private List<Employee> employees;
    @OneToMany(mappedBy = "assignedByManager",cascade = CascadeType.ALL)
    private List<Duty> dutiesAssigned;


    @Override
    public String toString() {
        return "Manager{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", department='" + department + '\'' +
                ", contact='" + contact + '\'' +
                ", employees=" + employees +
                ", dutiesAssigned=" + dutiesAssigned +
                '}';
    }

    public String getUsername() {
        return this.email;
    }



    public Long getId() {
        return id;
    }
}
