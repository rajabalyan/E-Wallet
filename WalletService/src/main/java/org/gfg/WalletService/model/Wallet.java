package org.gfg.WalletService.model;

import commons.models.UserIdentifier;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "wallet")
@Builder
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;
    @Column(unique = true)
    public int userId;

    public String name;
    @Column(unique = true)
    public String mobileNo;

    @Column(unique = true)
    public String userIdentifierValue;

    @Enumerated(EnumType.STRING)
    public UserIdentifier userIdentifier;

    @Enumerated(EnumType.STRING)
    public WalletStatus walletStatus;

    double balance;

    @CreationTimestamp
    Date createdOn;
    @UpdateTimestamp
    Date updatedOn;
}
