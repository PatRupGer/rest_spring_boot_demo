package de.pat.rup.a.bk.repo;

import de.pat.rup.a.bk.models.ExchangeRateSummary;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface ExchangeRepoitory extends CrudRepository<ExchangeRateSummary, Integer> {
    @Query(value = "select a from ExchangeRateSummary a where a.date like :date")
    List<ExchangeRateSummary> daily(@Param("date") Date date);

    @Query(value = "select a from ExchangeRateSummary a where a.date >= :begin and a.date < :end")
    List<ExchangeRateSummary> monthly(@Param("begin") Date begin, @Param("end") Date end);
}
