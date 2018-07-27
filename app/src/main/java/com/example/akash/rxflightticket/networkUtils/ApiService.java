package com.example.akash.rxflightticket.networkUtils;

import com.example.akash.rxflightticket.pojo.Price;
import com.example.akash.rxflightticket.pojo.Ticket;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    @GET("airline-tickets.php")
    public Single<List<Ticket>> getAirlineTickets(@Query("from") String from, @Query("to") String to);

    @GET("airline-tickets-price.php")
    public Single<Price> getAirlineTicketPrice(@Query("flight_number") String flight_number, @Query("from") String from, @Query("to") String to);
}
