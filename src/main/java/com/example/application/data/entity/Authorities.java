package com.example.application.data.entity;

import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Entity;

import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.CascadeType;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
@Table(name = "authorities")
public class Authorities {

  @Id
  @GeneratedValue(strategy=GenerationType.AUTO)
  String authority;

  @Id
  @ManyToOne(cascade = CascadeType.ALL)
  @GeneratedValue(strategy=GenerationType.AUTO)
  @JoinColumn(name = "username", referencedColumnName = "username",
    foreignKey = @ForeignKey(name="fk_username", foreignKeyDefinition = "FOREIGN KEY (username) REFERENCES users(username) ON UPDATE CASCADE ON DELETE CASCADE")
  )
  Users username;
}