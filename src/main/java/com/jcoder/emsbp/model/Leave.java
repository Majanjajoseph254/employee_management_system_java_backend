package com.jcoder.emsbp.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
@Setter
@Getter
@Entity
@Table(name = "leave_table")
public class Leave {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private LocalDate startDate;
    @Column(nullable = false)
    private LocalDate endDate;
    @Column(nullable = false)
    private String reason;
    @Column(nullable = false)
    private String status;

    @ManyToOne
    @JoinColumn(name = "emp_id")
    private Employee employee;


    @Override
    public String toString() {
        return "Leave{" +
                "id=" + id +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", reason='" + reason + '\'' +
                ", status='" + status + '\'' +
                ", employee=" + employee +
                '}';
    }

    public void setManager(Manager manager) {

    }
}
