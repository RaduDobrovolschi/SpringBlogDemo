package com.blog.blog.service.dto;

import javax.validation.constraints.Size;

public class UserDto {
    @Size(min = 1, max = 50)
    private String username;
    @Size(min = 1, max = 60)
    private String password;
    @Size(min = 1, max = 50)
    private String lastName;
    @Size(min = 1, max = 50)
    private String firstName;
    @Size(min = 1, max = 50)
    private String email;
    private String role;

    public boolean noEmptyFields(){
        return (username != null && password != null && lastName != null && firstName != null && email != null && role != null);
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                '}';
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
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

    public void setFirstName(String fistName) {
        this.firstName = fistName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
