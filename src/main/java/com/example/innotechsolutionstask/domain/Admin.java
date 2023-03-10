package com.example.innotechsolutionstask.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Data
@Entity
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "admin", uniqueConstraints = {
        @UniqueConstraint(columnNames = "username")
})
public class Admin extends User {
    public Admin(String username, String password, boolean active, Role role) {
        super(username, password, active, role);
    }

    @Override
    public boolean isAdmin() {
        return true;
    }
}
