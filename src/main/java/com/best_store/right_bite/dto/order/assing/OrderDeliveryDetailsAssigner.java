package com.best_store.right_bite.dto.order.assing;

import com.best_store.right_bite.constant.order.DeliveryMethod;

public interface OrderDeliveryDetailsAssigner {

    String firstname();

    String lastname();

    String phoneNumber();

    String houseNumber();

    String streetName();

    String city();

    String country();

    String zipCode();

    String comment();

    DeliveryMethod deliveryMethod();
}
