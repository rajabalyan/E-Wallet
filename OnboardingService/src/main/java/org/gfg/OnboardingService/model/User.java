package org.gfg.OnboardingService.model;

import commons.models.UserIdentifier;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @NotNull
    String name;

    @Column(unique = true)
    String email;

    @Column(unique = true)
    String mobileNo;

    @NotNull
    String dob;

    @NotNull
    String password;

    @NotNull
    @Enumerated(EnumType.STRING)
    UserIdentifier userIdentifier;

    @Column(unique = true)
    String userIdentifierValue;

    @NotNull
    @Enumerated(EnumType.STRING)
    UserStatus userStatus;

    @CreationTimestamp
    Date createdOn;

    @UpdateTimestamp
    Date updatedOn;
}
