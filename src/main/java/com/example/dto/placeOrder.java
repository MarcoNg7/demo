package com.example.dto;

import java.util.List;

public class placeOrder {

    private List<String> origin;
    private List<String> destination;

    public placeOrder(){}

    public List<String> getOrigin(){
        return origin;
    }

    public List<String> getDestination(){
        return destination;
    }

}
