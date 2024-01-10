package dev.erikmota.desafiounikamain;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class test {

    static LocalDate data = LocalDate.of(1920,1,2);


    public static void main(String[] args) {
        System.out.println(getData());
    }

    private static LocalDate getData() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String date = data.format(formatter);
        LocalDate dataFoda = LocalDate.parse(date, formatter);
        System.out.println(dataFoda);
        return dataFoda;
    }
}
