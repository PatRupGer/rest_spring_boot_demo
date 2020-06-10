package de.pat.rup.a.bk.util;

import java.time.LocalDate;

public interface FINALS {
    LocalDate FIRSTDATE = LocalDate.of(2000, 01, 01);
    LocalDate LASTDATE = LocalDate.now().minusDays(1);
    String BASE_URI = "https://api.exchangeratesapi.io/";

}
