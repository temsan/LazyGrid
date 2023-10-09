package com.example.application.data.entity;

import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Entity;
import jakarta.persistence.Column;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
@Table(name="users")
public class Users {

  @Id
  @GeneratedValue(strategy=GenerationType.AUTO)
  @Column(name="username", length=250, nullable=false, unique=true)
  private String username;

  @Column(name="password")
  private String password;

  @Column(name="enabled", columnDefinition = "boolean default true")
  private Boolean enabled;

  /////////////////////////
  public String getUsername() {
    return username;
  }
  public void setUsername(String username) {
    this.username = username;
  }
  /////////////////////////

  /////////////////////////
  public String getPassword() {
    return password;
  }
  public void setPassword(String password) {
    this.password = password;
  }
  /////////////////////////

  /////////////////////////
  public Boolean getEnabled() {
    return enabled;
  }
  public void setEnabled(Boolean enabled) {
    this.enabled = enabled;
  }
  /////////////////////////
}