package com.blog.blog.domain;

import javax.management.relation.Role;
import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import com.blog.blog.domain.UserStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.context.annotation.Lazy;


@Entity
@Table(name = "\"user\"")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 1, max = 50)
    @Column(length = 50, unique = true, nullable = false, name = "username")
    private String username;

    @JsonIgnore
    @NotNull
    @Size(min = 1, max = 60)
    @Column(length = 50, nullable = false, name = "password")
    private String password;

    @Size(min = 1, max = 50)
    @Column(length = 50, nullable = false, name = "last_name")
    private String lastName;

    @Size(min = 1, max = 50)
    @Column(length = 50, nullable = false, name = "first_name")
    private String firstName;

    @NotNull
    @Size(min = 1, max = 50)
    @Column(length = 50, nullable = false, name = "email")
    private String email;

    /*@OneToMany(mappedBy = "user")
    private Set<Comment> comments;*/

    @OneToOne
    @JoinColumn(name = "status", referencedColumnName = "status")
    private UserStatus status;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "user_role",
            joinColumns = { @JoinColumn(name = "user_id", referencedColumnName = "id") },
            inverseJoinColumns = { @JoinColumn(name = "role_name", referencedColumnName = "role") }
    )
    @BatchSize(size = 20)
    private Set<UserRole> roles = new HashSet<>();

    public User() {
    }

    public User(Long id, @NotNull @Size(min = 1, max = 50) String username, @NotNull @Size(min = 1, max = 60) String password, @Size(min = 1, max = 50) String lastName, @Size(min = 1, max = 50) String firstName, @NotNull @Size(min = 1, max = 50) String email, UserStatus status, Set<UserRole> roles) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.lastName = lastName;
        this.firstName = firstName;
        this.email = email;
        this.status = status;
        this.roles = roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id.equals(user.id) && username.equals(user.username) && password.equals(user.password) && lastName.equals(user.lastName) && firstName.equals(user.firstName) && email.equals(user.email) && status.equals(user.status) && Objects.equals(roles, user.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, lastName, firstName, email, status, roles);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", email='" + email + '\'' +
                ", status=" + status +
                ", roles=" + roles +
                '}';
    }

    public void addRole(UserRole role){
        roles.add(role);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public Set<UserRole> getRoles() {
        return roles;
    }

    public void setRoles(Set<UserRole> roles) {
        this.roles = roles;
    }
}
