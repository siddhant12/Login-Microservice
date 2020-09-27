package com.siddhant.users.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CreateUserRequestDTO {

    @NotNull
    @Size(min=2,message = "First name must not be less than 2 characters")
    public String firstName;

    @NotNull
    @Size(min = 2,message = "Last name must not be less than 2 characters")
    public String lastName;

    @NotNull
    @Email
    public String email;

    @NotNull
    @Size(min=4,max = 10,message = "Password size must be between 4 and 10")
    public String password;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
