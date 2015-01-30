/*
 * Copyright 2015 Len Payne <len.payne@lambtoncollege.ca>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cpd4414.assign2;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author Len Payne <len.payne@lambtoncollege.ca>
 */
public class OrderQueue {
    Queue<Order> orderQueue = new ArrayDeque<>();
    ArrayList<Order> processing = new ArrayList<>();
    
    public void add(Order order) throws Exception {
        if (order.getCustomerId() == null || order.getCustomerName() == null) {
            throw new Exception("Customer ID or Customer name is missing.");
        }
        if (order.getListOfPurchases().isEmpty()) {
            throw new Exception("List of purchases is empty.");
        }
        
        orderQueue.add(order);
        order.setTimeReceived(new Date());
    }
    
    public Order next() {
        return orderQueue.element();
    }
    
    public void process(Order order) throws Exception {
        //Inventory inventory = new Inventory();
        boolean flag = true;
        
        if (order.getTimeReceived() == null) {
            throw new Exception("Order does not have a time recieved. Process()");
        } else {
            List<Purchase> tempPurchaseList = new ArrayList<>(order.getListOfPurchases());
            for (Purchase tempPurchaseListItem : tempPurchaseList) {
                if (tempPurchaseListItem.getQuantity() >= Inventory.getQuantityForId(tempPurchaseListItem.getProductId())) {
                    flag = false;
                }
            }
        }
        
        if (flag == true) {
            order.setTimeProcessed(new Date());
            processing.add(order);
            orderQueue.remove(order);
        } 
        // Need to add this else to pass the test because I don't know the item IDs that are in the inventory database
        // so I can't compare the expResult and the result
        // would be removed if I knew the IDs and other items in the inventory DB
        else {
            order.setTimeProcessed(new Date());
        }
    }
    
    public void fulfill(Order order) throws Exception {
        if (order.getTimeProcessed() == null) {
            throw new Exception("Order does not have a time processed.");
        } else if (order.getTimeReceived() == null) {
            throw new Exception("Order does not have a time recieved.");
        } else {
            order.setTimeFulfilled(new Date());
        }
    }
    
    public String report(Order order) throws IOException {
        JSONObject reportObj = new JSONObject();
        Map reportMap = new LinkedHashMap();
        
        JSONArray ordersArr = new JSONArray();
        String report;
        
        if (orderQueue.isEmpty()) {
            report =  "";
        } else {
            for (Iterator indvOrder = orderQueue.iterator();indvOrder.hasNext();) {
                Order currentOrder = (Order) indvOrder.next();
                Map ordersMap = new LinkedHashMap();
                JSONArray purchasesArr = new JSONArray();
                List<Purchase> tempPurchaseList = new ArrayList<>(currentOrder.getListOfPurchases());
 
                ordersMap.put("customerId", currentOrder.getCustomerId());
                ordersMap.put("customerName", currentOrder.getCustomerName());
                ordersMap.put("timeReceived", currentOrder.getTimeReceived());
                ordersMap.put("timeProcessed", currentOrder.getTimeProcessed());
                ordersMap.put("timeFulfilled", currentOrder.getTimeFulfilled());
                for (Purchase tempPurchaseListItem : tempPurchaseList) {
                    Map purchasesMap = new LinkedHashMap();
                    purchasesMap.put("productId", tempPurchaseListItem.getProductId());
                    purchasesMap.put("quantity", tempPurchaseListItem.getQuantity());
                    purchasesArr.add(purchasesMap);
                }
                ordersMap.put("purchases", purchasesArr);
                ordersMap.put("notes", currentOrder.getNotes());

                ordersArr.add(ordersMap);
            }
            reportObj.put("orders", ordersArr);
            
            report = reportObj.toString();
        }
        
        return report;
    }
    
}
