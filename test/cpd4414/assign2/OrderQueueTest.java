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

import cpd4414.assign2.OrderQueue;
import cpd4414.assign2.Purchase;
import cpd4414.assign2.Order;
import java.util.Date;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Len Payne <len.payne@lambtoncollege.ca>, Amanda Cohoon <mandiecohoon@gmail.com>
 */
public class OrderQueueTest {
    
    public OrderQueueTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testWhenCustomerExistsAndPurchasesExistThenTimeReceivedIsNow() throws Exception {
        OrderQueue orderQueue = new OrderQueue();
        Order order = new Order("CUST00001", "ABC Construction");
        order.addPurchase(new Purchase(0004, 450));
        order.addPurchase(new Purchase(0006, 250));
        orderQueue.add(order);
        
        long expResult = new Date().getTime();
        long result = order.getTimeReceived().getTime();
        assertTrue(Math.abs(result - expResult) < 1000);
    }
    
    @Test
    public void testWhenCustomerDoesNotExistAndCustomerIDDoesNotExistThenThrowException() {
       OrderQueue orderQueue = new OrderQueue();
       Order order = new Order(null, null);
       boolean flag = false;
       
       try {
            orderQueue.add(order);
       } catch (Exception e) {
           flag = true;
       }
       assertTrue(flag);
    }
    
    @Test
    public void testWhenListOfPurchasesDoesNotExistThenThrowException() throws Exception {
        OrderQueue orderQueue = new OrderQueue();
        Order order = new Order("CUST00001", "ABC Construction");
        boolean flag = false;
        
        try {
           orderQueue.add(order);
        } catch (Exception e) {
            flag = true;
        }
        assertTrue(flag);
    }
    
    @Test
    public void testWhenNoOrdersThenReturnEarliestOrder() throws Exception {
        OrderQueue orderQueue = new OrderQueue();
        Order order = new Order("CUST00001", "ABC Construction");
        order.addPurchase(new Purchase(0004, 450));
        order.addPurchase(new Purchase(0006, 250));
        orderQueue.add(order);
        
        Order orderNew = new Order("CUST00002", "ABCD Construction");
        orderNew.addPurchase(new Purchase(0004, 450));
        orderNew.addPurchase(new Purchase(0006, 250));
        orderQueue.add(orderNew);
        
        Order expResult = order;
        Order result = orderQueue.next();
        
        assertEquals(expResult, result);
    }
    
    @Test
    public void testWhenNoOrdersThenReturnNull() {
        OrderQueue orderQueue = new OrderQueue();
        
        Order expResult = null;
        String result = "";
        
        try {
            orderQueue.next();
        } catch (Exception e) {
            result = null;
        }
        
        assertEquals(expResult, result);
    }
    
   
    @Test
    public void testWhenOrderHasTimeRecievedAndOrderIsInStockThenSetTimeToNow() throws Exception {
        OrderQueue orderQueue = new OrderQueue();
        Order order = new Order("CUST00001", "ABC Construction");
        order.addPurchase(new Purchase(0004, 450));
        order.addPurchase(new Purchase(0006, 250));
        orderQueue.add(order);
        
        orderQueue.process(order);
        
        Date expResult = new Date();
        Date result = order.getTimeProcessed();
        
        assertEquals(expResult, result);
    }
    
    @Test
    public void testWhenOrderDoesNotHaveTimeRecievedThenThrowException() throws Exception {
        OrderQueue orderQueue = new OrderQueue();
        Order order = new Order("CUST00001", "ABC Construction");
        order.addPurchase(new Purchase(0004, 450));
        order.addPurchase(new Purchase(0006, 250));
        boolean flag = false;
        
        try {
            orderQueue.process(order);
        } catch (Exception e) {
            flag = true;
        }
        
        assertTrue(flag);
    }
    
    
    @Test
    public void testWhenOrderFulfilledThenSetTimeFulfilledToNow() throws Exception {
        OrderQueue orderQueue = new OrderQueue();
        Order order = new Order("CUST00001", "ABC Construction");
        order.addPurchase(new Purchase(0004, 450));
        order.addPurchase(new Purchase(0006, 250));
        orderQueue.add(order);
        
        orderQueue.process(order);
        orderQueue.fulfill(order);
        
        Date expResult = new Date();
        Date result = order.getTimeFulfilled();
        
        assertEquals(expResult, result);
    }
    
    @Test
    public void testWhenOrderRequestToBeFulfilledAndOrderDoesNotHaveATimeProcessedThenThrowException() throws Exception {
        OrderQueue orderQueue = new OrderQueue();
        Order order = new Order("CUST00001", "ABC Construction");
        order.addPurchase(new Purchase(0004, 450));
        order.addPurchase(new Purchase(0006, 250));
        orderQueue.add(order);
        boolean flag = false;
        
        try {
            orderQueue.fulfill(order);
        } catch (Exception e) {
            flag = true;
        }
        
        assertTrue(flag);
    }
    
    @Test
    public void testWhenOrderRequestToBeFulfilledAndOrderDoesNotHaveATimeRecievedThenThrowException() throws Exception {
        OrderQueue orderQueue = new OrderQueue();
        Order order = new Order("CUST00001", "ABC Construction");
        order.addPurchase(new Purchase(0004, 450));
        order.addPurchase(new Purchase(0006, 250));
        boolean flag = false;
        
        try {
            orderQueue.fulfill(order);
        } catch (Exception e) {
            flag = true;
        }
        
        assertTrue(flag);
    }
    
    @Test
    public void testWhenResquestForReportAndThereAreNoOrdersThenReturnEmptyString() throws Exception {
        OrderQueue orderQueue = new OrderQueue();
        Order order = new Order("CUST00001", "ABC Construction");
        
        String expResult = "";
        String result = orderQueue.report(order, "orderQueue");
        
        assertEquals(expResult, result);
    }
    
    @Test
    public void testWhenRequestForReportAndThereAreOrdersThenReturnJsonObject() throws Exception {
        OrderQueue orderQueue = new OrderQueue();
        Order order = new Order("11", "aaa");
        order.addPurchase(new Purchase(0004, 2));
        order.addPurchase(new Purchase(0005, 3));
        orderQueue.add(order);
        
        Order orderNew = new Order("22", "bbb");
        orderNew.addPurchase(new Purchase(0006, 6));
        orderNew.addPurchase(new Purchase(0007, 98));
        orderQueue.add(orderNew);
        
        String result = orderQueue.report(order, "orderQueue");
        Date timeNow = new Date();
        
        String expResult = "{\"orders\":[{\"customerId\":\"11\",\"customerName\":\"aaa\","
                + "\"timeReceived\":"+ timeNow + ",\"timeProcessed\":null,"
                + "\"timeFulfilled\":null,\"purchases\":[{\"productId\":4,\"quantity\":2},"
                + "{\"productId\":5,\"quantity\":3}],\"notes\":null},{\"customerId\":\"22\","
                + "\"customerName\":\"bbb\",\"timeReceived\":"+ timeNow + ","
                + "\"timeProcessed\":null,\"timeFulfilled\":null,\"purchases\":[{\"productId\":6,\"quantity\":6},"
                + "{\"productId\":7,\"quantity\":98}],\"notes\":null}]}";
        
        assertEquals(expResult, result);
    }
}
