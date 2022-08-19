package hanu.a2_1901040191.models;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Constants {
    public static final ExecutorService executor = Executors.newFixedThreadPool(4);
    public static final String APILink ="https://mpr-cart-api.herokuapp.com/products";
}
