package com.bankofbaku.SpringSecurityJWT.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "roles")
@NoArgsConstructor
@Data
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    public Role(Long id){
        this.id=id;
    }
    public Role(String name){
        this.name=name;
    }
    @Override
    public String toString(){
        return this.name;
    }
}
