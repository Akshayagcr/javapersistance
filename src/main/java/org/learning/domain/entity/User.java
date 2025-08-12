package org.learning.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "app_user")
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Version
    private Long version;

    @Column(name = "username", updatable = false, nullable = false)
    private String userName;

    @Column(name = "email_id")
    private String emailId;

    @Column(name = "country")
    private String country;

}
