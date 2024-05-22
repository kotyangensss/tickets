package org.example;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Ticket {
    @JsonProperty("origin")
    private String origin;
    @JsonProperty("origin_name")
    private String originName;
    @JsonProperty("destination")
    private String destination;
    @JsonProperty("destination_name")
    private String destinationName;
    @JsonProperty("departure_date")
    private String departureDate;
    @JsonProperty("departure_time")
    private String departureTime;
    @JsonProperty("arrival_date")
    private String arrivalDate;
    @JsonProperty("arrival_time")
    private String arrivalTime;
    @JsonProperty("carrier")
    private String carrier;
    @JsonProperty("stops")
    private int stops;
    @JsonProperty("price")
    private int price;

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");

    public String getOrigin() { return origin; }
    public String getOriginName() { return originName; }
    public String getDestination() { return destination; }
    public String getDestinationName() { return destinationName; }
    public String getDepartureDate() { return departureDate; }
    public String getDepartureTime() { return departureTime; }
    public String getArrivalDate() { return arrivalDate; }
    public String getArrivalTime() { return arrivalTime; }
    public String getCarrier() { return carrier; }
    public int getPrice() { return price; }

    public LocalDateTime getDepartureDateTime() {
        if (departureTime.length() < 5)
            return LocalDateTime.parse(departureDate + " 0" + departureTime, DATE_TIME_FORMATTER);
        else
            return LocalDateTime.parse(departureDate + " " + departureTime, DATE_TIME_FORMATTER);
    }

    public LocalDateTime getArrivalDateTime() {
        if (arrivalTime.length() < 5)
            return LocalDateTime.parse(arrivalDate + " 0" + arrivalTime, DATE_TIME_FORMATTER);
        else
            return LocalDateTime.parse(arrivalDate + " " + arrivalTime, DATE_TIME_FORMATTER);
    }

    public Duration getFlightDuration() {
        return Duration.between(getDepartureDateTime(), getArrivalDateTime());
    }
}