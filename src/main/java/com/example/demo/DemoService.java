package com.example.demo;

import com.google.maps.model.LatLng;
import org.springframework.stereotype.Service;

public interface DemoService {

    void update(orderList order);

    void get(Long id);


    String getDestination(double latitude, double longitude);
    String calculateDistance(LatLng origin, LatLng destination);
}
