package com.example.demo.server;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
public class Customer implements java.io.Serializable {

    @Id
    private long id;

    @NonNull
    private String name;

    @NonNull
    private  Double amount;

}
