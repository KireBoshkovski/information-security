package mk.ukim.finki.ib.authentication.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mk.ukim.finki.ib.authentication.model.enumerations.Role;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "app_users")
public class User {
    @Id
    private String username;
    private String email;
    private String password;
    private boolean isConfirmed;
    @Enumerated(value = EnumType.STRING)
    private Role role;

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.isConfirmed = false;
        this.role = Role.ROLE_USER;
    }
}

