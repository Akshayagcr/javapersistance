package org.learning.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.learning.domain.enums.UserLifeCycleStatus;

import java.time.LocalDate;

@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Version
    private Long version;

    @Column(name = "userName", updatable = false, nullable = false)
    private String userName;

    private String emailId;

    private LocalDate dateOfBirth;

    private String country;

    @Enumerated(EnumType.STRING)
    private UserLifeCycleStatus userLifeCycleStatus;
}
