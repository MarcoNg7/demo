package com.example.demo;


import com.example.dto.placeOrder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.maps.model.LatLng;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class DemoController {

    private static final ObjectMapper json = new ObjectMapper();


    @Autowired
    private DemoDao demodao;

    @Autowired
    private DemoService demoService;

    public DemoController(DemoService demoService){
        this.demoService = demoService;
    }

    @PostMapping(value = "/orders")
    public String go(@RequestBody String requestBody){

        /*
        orderList order = new orderList();
        Optional<orderList> a = demodao.findById(1L);
        System.out.println(a.map(orderList::getStatus).orElse(null));

        order.setStatus("test1");
        demoService.update(order);
        */

        try {

            orderList orderDao = new orderList();
            placeOrder order = json.readValue(requestBody, placeOrder.class);

            List<String> origin = order.getOrigin();
            List<String> destination = order.getDestination();

            double originValueStart = Double.parseDouble(origin.get(0));
            double originValueEnd = Double.parseDouble(origin.get(1));

            double destinationValueStart = Double.parseDouble(destination.get(0));
            double destinationValueEnd = Double.parseDouble(destination.get(1));

            String start = demoService.getDestination(originValueStart,originValueEnd);
            String end = demoService.getDestination(destinationValueStart,destinationValueEnd);
            System.out.println("Start: " + start + " End: " + end);

            LatLng startD = new LatLng(originValueStart, originValueEnd);
            LatLng endD = new LatLng(destinationValueStart, destinationValueEnd);

            String distance = demoService.calculateDistance(startD, endD);
            System.out.println("distance: " + distance);

            // update to db
            orderDao.setDistance(distance);
            orderDao.setStatus("UNASSIGNED");
            demoService.update(orderDao);

            //Json response
            return json.writeValueAsString(orderDao);


        } catch (Exception e) {
            //throw new RuntimeException(e);
            return ("error:" + e);
        }

    }

    @PatchMapping(path = "/orders" ,consumes="application/json")
    public String takeOrder(@RequestParam("id") Long id, @RequestBody String requestBody)  {

        orderList orderDao = new orderList();

        try {
            JsonNode jsonNode = json.readTree(requestBody);
            if(jsonNode.get("status").asText().equals("TAKEN")){


                Optional<orderList> list = demodao.findById(id);
                if(list.map(orderList::getDistance).equals("SUCCESS")){
                    orderDao.setDistance(list.map(orderList::getDistance).orElse(null));
                    orderDao.setStatus("SUCCESS");
                    orderDao.setId(id);
                    demoService.update(orderDao);

                    Map<String, Object> responseMap = new HashMap<>();
                    responseMap.put("status", orderDao.getStatus());
                    return json.writeValueAsString(responseMap);

                }else {
                    return ("error:" + " The Order has been taken.");
                }
            }

        } catch (Exception e) {
            return ("error:" + e);
        }
        return "";
    }


    @GetMapping("/orders")
    public String getOrdersByPage(@RequestParam("page") int page, @RequestParam("limit") int limit) {

        try {
            Pageable pages = PageRequest.of(page, limit);
            Page<orderList> result = demodao.findAll(pages);
            List<orderList> orders = result.getContent();
            System.out.println(orders);
            return json.writeValueAsString(orders);
        } catch (Exception e){
            return ("error:" + e);
        }
    }
}
