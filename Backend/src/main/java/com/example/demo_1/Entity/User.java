package com.example.demo_1.Entity;

import jakarta.persistence.*;
import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.annotations.ColumnDefault;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users",uniqueConstraints = {
        @UniqueConstraint(columnNames = "email")
})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long uuid;
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    @Column(length = 32, columnDefinition = "varchar(32) default 'LOCAL'")
    @Enumerated(value = EnumType.STRING)
    AuthProvider provider = AuthProvider.LOCAL;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(  name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();
    @ColumnDefault("01558680835")
    private String emergencyContactNo;

    public User( String email, String password) {
        this.email=email;
        this.password=password;
    }
}
