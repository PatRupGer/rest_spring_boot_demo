package de.pat.rup.a.bk;

import de.pat.rup.a.bk.models.ExchangeRateSummary;
import de.pat.rup.a.bk.models.TrendType;
import de.pat.rup.a.bk.repo.ExchangeRepoitory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ExchangeRepositoryIntegrationTest {

    @Autowired
    ExchangeRepoitory exchangeRepoitory;

    @Test
    public void testDaily_oneIsPresent() {
        addSomeOthers();
        ExchangeRateSummary expected = new ExchangeRateSummary((float) 1.2725, (float) 1.2713801,
                TrendType.undefined, Date.valueOf("2012-11-12"));
        // add some entrys so we make sure no others are inside
        ExchangeRateSummary dayBeforFalse = new ExchangeRateSummary((float) 1.2725, (float) 1.2713801,
                TrendType.undefined, Date.valueOf("2012-11-13"));
        ExchangeRateSummary dayAfterFalse = new ExchangeRateSummary((float) 1.2725, (float) 1.2713801,
                TrendType.undefined, Date.valueOf("2012-12-14"));
        exchangeRepoitory.save(expected);
        exchangeRepoitory.save(dayAfterFalse);
        exchangeRepoitory.save(dayBeforFalse);
        // when
        List<ExchangeRateSummary> results = exchangeRepoitory.daily(Date.valueOf("2012-11-12"));
        ExchangeRateSummary result = results.get(0);
        // then
        assertThat(results.size() == 1);
        isValid(expected, result);
    }

    @Test
    public void testDaily_empty() {
        addSomeOthers();
        // add some entrys so we make sure no others are inside
        ExchangeRateSummary dayBeforFalse = new ExchangeRateSummary((float) 1.2725, (float) 1.2713801,
                TrendType.undefined, Date.valueOf("2012-11-13"));
        ExchangeRateSummary dayAfterFalse = new ExchangeRateSummary((float) 1.2725, (float) 1.2713801,
                TrendType.undefined, Date.valueOf("2012-12-14"));
        exchangeRepoitory.save(dayAfterFalse);
        exchangeRepoitory.save(dayBeforFalse);
        // when
        List<ExchangeRateSummary> results = exchangeRepoitory.daily(Date.valueOf("2012-11-12"));
        // then
        assertThat(results.isEmpty());
    }

    @Test
    public void testDaily_threeArePresent() {
        addSomeOthers();
        ExchangeRateSummary expected = new ExchangeRateSummary((float) 1.2725, (float) 1.2713801,
                TrendType.undefined, Date.valueOf("2012-11-12"));
        ExchangeRateSummary expected1 = new ExchangeRateSummary((float) 1.2, (float) 1.2713801,
                TrendType.undefined, Date.valueOf("2012-11-12"));
        ExchangeRateSummary expected2 = new ExchangeRateSummary((float) 1.3, (float) 1.2713801,
                TrendType.undefined, Date.valueOf("2012-11-12"));
        // add some entrys so we make sure no others are inside
        ExchangeRateSummary dayBeforFalse = new ExchangeRateSummary((float) 1.2725, (float) 1.2713801,
                TrendType.undefined, Date.valueOf("2012-11-13"));
        ExchangeRateSummary dayAfterFalse = new ExchangeRateSummary((float) 1.2725, (float) 1.2713801,
                TrendType.undefined, Date.valueOf("2012-12-14"));
        // when
        exchangeRepoitory.save(expected);
        exchangeRepoitory.save(expected1);
        exchangeRepoitory.save(expected2);
        exchangeRepoitory.save(dayAfterFalse);
        exchangeRepoitory.save(dayBeforFalse);
        List<ExchangeRateSummary> results = exchangeRepoitory.daily(Date.valueOf("2012-11-12"));
        assertThat(results.size() == 3);
        // dates should be 2012-11-12
        LocalDate result1 = results.get(0).getDate().toLocalDate();
        LocalDate result2 = results.get(1).getDate().toLocalDate();
        LocalDate result3 = results.get(2).getDate().toLocalDate();
        assertThat(result1.getYear() == 2020);
        assertThat(result1.getMonthValue() == 2);
        assertThat(result1.getDayOfMonth() == 12);
        assertThat(result2.getYear() == 2020);
        assertThat(result2.getMonthValue() == 2);
        assertThat(result2.getDayOfMonth() == 12);
        assertThat(result3.getYear() == 2020);
        assertThat(result3.getMonthValue() == 2);
        assertThat(result3.getDayOfMonth() == 12);
    }

    @Test
    public void testMontly_testBorderCaseFebruary() {
        addSomeOthers();
        // february 2020 has 29 days -  add 2 February
        ExchangeRateSummary february2020_29 = new ExchangeRateSummary((float) 1.2725, (float) 1.2713801,
                TrendType.undefined, Date.valueOf("2020-02-29"));
        ExchangeRateSummary february2020_01 = new ExchangeRateSummary((float) 1.2725, (float) 1.2713801,
                TrendType.undefined, Date.valueOf("2020-02-01"));
        // wrong
        ExchangeRateSummary march2020_01_false = new ExchangeRateSummary((float) 1.2725, (float) 1.2713801,
                TrendType.undefined, Date.valueOf("2020-03-01"));
        ExchangeRateSummary january2020_31_false = new ExchangeRateSummary((float) 1.2725, (float) 1.2713801,
                TrendType.undefined, Date.valueOf("2020-01-31"));
        exchangeRepoitory.save(february2020_01);
        exchangeRepoitory.save(february2020_29);
        exchangeRepoitory.save(march2020_01_false);
        exchangeRepoitory.save(january2020_31_false);

        List<ExchangeRateSummary> results = exchangeRepoitory.monthly(Date.valueOf("2020-02-01"), Date.valueOf("2020-03-01"));

        // list should contain 2
        assertThat(results.size() == 2);
        // dates should be inside February 2020
        LocalDate result1 = results.get(0).getDate().toLocalDate();
        LocalDate result2 = results.get(0).getDate().toLocalDate();
        assertThat(result1.getYear() == 2020);
        assertThat(result1.getMonthValue() == 2);
        assertThat(result2.getYear() == 2020);
        assertThat(result2.getMonthValue() == 2);
    }

    @Test
    public void testMontly_empty() {
        addSomeOthers();
        // wrong
        ExchangeRateSummary march2020_01_false = new ExchangeRateSummary((float) 1.2725, (float) 1.2713801,
                TrendType.undefined, Date.valueOf("2020-03-01"));
        ExchangeRateSummary january2020_31_false = new ExchangeRateSummary((float) 1.2725, (float) 1.2713801,
                TrendType.undefined, Date.valueOf("2020-01-31"));
        List<ExchangeRateSummary> results = exchangeRepoitory.monthly(Date.valueOf("2020-02-01"), Date.valueOf("2020-03-01"));

        // list should contain 0
        assertThat(results.isEmpty());
    }

    private void addSomeOthers() {
        // add some entrys so we make sure no others are inside
        ExchangeRateSummary january2012 = new ExchangeRateSummary((float) 1.2725, (float) 1.2713801,
                TrendType.undefined, Date.valueOf("2012-01-13"));
        ExchangeRateSummary february2012 = new ExchangeRateSummary((float) 1.2725, (float) 1.2713801,
                TrendType.undefined, Date.valueOf("2012-02-14"));
        ExchangeRateSummary february2013 = new ExchangeRateSummary((float) 1.2725, (float) 1.2713801,
                TrendType.undefined, Date.valueOf("2013-02-14"));
        exchangeRepoitory.save(january2012);
        exchangeRepoitory.save(february2012);
        exchangeRepoitory.save(february2013);
    }

    private void isValid(ExchangeRateSummary expected, ExchangeRateSummary result) {
        assertThat(expected.getExRate() == result.getExRate());
        assertThat(expected.getAverageExRate() == result.getAverageExRate());
        assertThat(expected.getTrendType() == result.getTrendType());
        assertThat(expected.getDate() == result.getDate());
    }
}
