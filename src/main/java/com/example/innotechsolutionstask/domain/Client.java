package com.example.innotechsolutionstask.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.*;

@Data
@Entity
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "client", uniqueConstraints = {
        @UniqueConstraint(columnNames = "username")
})
public class Client extends User {
    @Column(name = "balance")
    private Integer balance;

    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinTable(
            name = "client_train",
            joinColumns = @JoinColumn(name = "client_id"),
            inverseJoinColumns = @JoinColumn(name = "train_id")
    )
    private List<Train> trains = new ArrayList<>();

    @Override
    public boolean isAdmin() {
        return false;
    }
}