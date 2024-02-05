package com.example.demo;

import jakarta.persistence.*;

@Entity
@Table(name = "orderlist")
public class orderList {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column
    private Long id;
    @Column
    private String status;

    @Column
    private String distance;

    public orderList() {}

    public Long getId(){
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus(){
        return status;
    }

    public void setStatus(String status){
        this.status = status;
    }

    public String getDistance(){
        return distance;
    }

    public void setDistance(String distance){
        this.distance = distance;
    }


}
