package com.example.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class DemoServiceImpl implements DemoService {

    private final String apiKey = "AIzaSyCVPWVMUHJle_OomXzgN1Vvz1bChcdX-UM";
    private final GeoApiContext geoApiContext;
    private final ObjectMapper json = new ObjectMapper();

    @Autowired
    private DemoDao demodao;

    public DemoServiceImpl () {
        this.geoApiContext = new GeoApiContext.Builder()
                .apiKey(apiKey)
                .connectTimeout(1, TimeUnit.SECONDS)
                .build();
    }


    @Override
    public void update(orderList order){
        demodao.save(order);
    }

    @Override
    public void get(Long id){
        demodao.findById(id);
    }


    @Override
    public  String getDestination(double latitude, double longitude){
        try {
            GeocodingResult[] results = GeocodingApi.reverseGeocode(geoApiContext, new LatLng(latitude, longitude)).await();
            System.out.println("Get GeoCode completed");
            if (results.length > 0) {
                return results[0].formattedAddress;
            }
        } catch (Exception e) {
            return e.toString();
        }
        return null;
    }

    @Override
    public String calculateDistance(LatLng origin, LatLng destination){
        try {
            DirectionsResult directionsResult = DirectionsApi.newRequest(geoApiContext)
                    .origin(origin)
                    .destination(destination)
                    .await();

            if (directionsResult.routes.length > 0 && directionsResult.routes[0].legs.length > 0) {
                return Double.toString(directionsResult.routes[0].legs[0].distance.inMeters);
            }

        } catch (Exception ex) {
            return ex.toString();
        }
        return "";
    }

}
