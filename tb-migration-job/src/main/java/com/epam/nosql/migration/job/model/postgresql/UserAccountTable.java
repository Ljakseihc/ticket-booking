package com.epam.nosql.migration.job.model.postgresql;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "user_accounts")
@Getter
@Setter
public class UserAccountTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private UserTable user;

    @Column(name = "money", nullable = false)
    private BigDecimal money;

    public UserAccountTable() {
    }

    public UserAccountTable(UserTable user, BigDecimal money) {
        this.user = user;
        this.money = money;
    }

    public UserAccountTable(Long id, UserTable user, BigDecimal money) {
        this.id = id;
        this.user = user;
        this.money = money;
    }

    @Override
    public String toString() {
        return "UserAccount{" +
                "id=" + id +
                ", user=" + user +
                ", money=" + money +
                '}';
    }
}
