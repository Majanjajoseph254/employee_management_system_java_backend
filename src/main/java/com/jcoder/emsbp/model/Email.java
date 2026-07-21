package com.jcoder.emsbp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "email_details")
public class Email {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private String recipient;
    @Column(nullable = false)
    private String subject;
    @Column(length = 1000, nullable = false)
    private String message;
    @Column(nullable = false)
    private LocalDateTime sentAt;
    @Column(nullable = false)
    private String status;
}
