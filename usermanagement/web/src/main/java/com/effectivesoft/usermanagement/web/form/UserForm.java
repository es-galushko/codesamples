package com.effectivesoft.usermanagement.web.form;

public class UserForm {
    private Integer id;
    private String username;
    private String password;
    private String description;
    private String email;
    private String locked;
    private String admin;
    private Integer[] roleIds;
    private String passwordChanged;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLocked() {
        return locked;
    }

    public void setLocked(String locked) {
        this.locked = locked;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public Integer[] getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(Integer[] roleIds) {
        this.roleIds = roleIds;
    }

    public String getPasswordChanged() {
        return passwordChanged;
    }

    public void setPasswordChanged(String passwordChanged) {
        this.passwordChanged = passwordChanged;
    }
}
