package com.gigd.daret.models;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class DateUtils {

    public static Date calculerDateEnvoiPrevu(Date dateDemarrage, int tourRole, String periodicite) {
        // Convertir la date de démarrage en LocalDate
        LocalDate localDateDemarrage = dateDemarrage.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        // Vérifiez la périodicité et ajustez la logique de calcul en conséquence
        switch (periodicite.toLowerCase()) {
            case "15j":
                return Date.from(localDateDemarrage.plusDays((tourRole - 1) * 15).atStartOfDay(ZoneId.systemDefault()).toInstant());
            case "par semaine":
                return Date.from(localDateDemarrage.plusWeeks(tourRole - 1).atStartOfDay(ZoneId.systemDefault()).toInstant());
            case "mensuel":
                return Date.from(localDateDemarrage.plusMonths(tourRole - 1).atStartOfDay(ZoneId.systemDefault()).toInstant());
            default:
                throw new IllegalArgumentException("Périodicité non prise en charge : " + periodicite);
        }
    }
}
