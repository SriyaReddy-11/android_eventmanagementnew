package com.eventmanagement.Entities;

public class BookEntity {
    String key;

    Event event;
    String customer;
    String customerName;
    String status;

    public BookEntity() {
    }

    public BookEntity(Event event, String customer, String customerName, String status) {

        this.event = event;
        this.customer = customer;
        this.customerName = customerName;
        this.status = status;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

