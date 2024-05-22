package org.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        String resourceFileName = "tickets.json";
        List<Ticket> tickets = readTicketsFromFile(resourceFileName);

        List<Ticket> filteredTickets = tickets.stream()
                .filter(t -> t.getOrigin().equals("VVO") && t.getDestination().equals("TLV"))
                .collect(Collectors.toList());

        // Минимальное время полета для каждого авиаперевозчика
        Map<String, Duration> minFlightTimes = getMinFlightTimesByCarrier(filteredTickets);
        minFlightTimes.forEach((carrier, duration) ->
                System.out.printf("Минимальное время полета для %s: %s%n", carrier, formatDuration(duration)));

        // Разница между средней ценой и медианой
        double averagePrice = filteredTickets.stream().mapToInt(Ticket::getPrice).average().orElse(0);
        double medianPrice = getMedianPrice(filteredTickets);
        System.out.printf("Средняя цена: %.2f%n", averagePrice);
        System.out.printf("Медианная цена: %.2f%n", medianPrice);
        System.out.printf("Разница между средней ценой и медианной: %.2f%n", Math.abs(averagePrice - medianPrice));
    }

    private static List<Ticket> readTicketsFromFile(String resourceFileName) {
        ObjectMapper mapper = new ObjectMapper();
        try (InputStream inputStream = Main.class.getClassLoader().getResourceAsStream(resourceFileName)) {
            if (inputStream == null) {
                throw new IllegalArgumentException("Файл не найден: " + resourceFileName);
            }
            Map<String, List<Ticket>> map = mapper.readValue(inputStream, new TypeReference<Map<String, List<Ticket>>>() {});
            return map.get("tickets");
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    private static Map<String, Duration> getMinFlightTimesByCarrier(List<Ticket> tickets) {
        Map<String, Duration> minFlightTimes = new HashMap<>();
        for (Ticket ticket : tickets) {
            Duration flightDuration = ticket.getFlightDuration();
            minFlightTimes.merge(ticket.getCarrier(), flightDuration, (d1, d2) -> d1.compareTo(d2) < 0 ? d1 : d2);
        }
        return minFlightTimes;
    }

    private static double getMedianPrice(List<Ticket> tickets) {
        List<Integer> prices = tickets.stream().map(Ticket::getPrice).sorted().toList();
        int size = prices.size();
        if (size % 2 == 1) {
            return prices.get(size / 2);
        } else {
            return (prices.get(size / 2 - 1) + prices.get(size / 2)) / 2.0;
        }
    }

    private static String formatDuration(Duration duration) {
        long hours = duration.toHours();
        long minutes = duration.toMinutes() % 60;
        return String.format("%dч %dм", hours, minutes);
    }
}
