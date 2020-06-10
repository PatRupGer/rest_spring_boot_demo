package de.pat.rup.a.bk;

import de.pat.rup.a.bk.models.TrendType;
import de.pat.rup.a.bk.service.CalculatorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = CalculatorService.class)
public class CalculaterServiceTest {

    @Autowired
    CalculatorService serviceHelper;

    @Test
    void testCalcAverage_result_3() {
        List<Float> exchangeRates = List.of(1f, 2f, 3f, 4f, 5f);
        float result = serviceHelper.calcAverage(exchangeRates);
        assertEquals(3, result);
        assertNotEquals(5, result, 0.0);
    }

    @Test
    void testCalcAverage_emptyList() {
        List<Float> exchangeRates = List.of();
        float result = serviceHelper.calcAverage(exchangeRates);
        assertEquals(0, result);
        assertNotEquals(5, result, 0.0);
    }

    @Test
    void testReversedListIsDescending() {
        List<Float> a = List.of(1f, 2f, 3f, 4f, 5f);
        assertTrue(serviceHelper.reversedListIsDescending(a));
        List<Float> aReversed = List.of(5f, 4f, 3f, 2f, 1f);
        assertFalse(serviceHelper.reversedListIsDescending(aReversed));
        List<Float> empty = List.of();
        assertFalse(serviceHelper.reversedListIsDescending(empty));
    }

    @Test
    void testReversedListIsAscending() {
        List<Float> a = List.of(1f, 2f, 3f, 4f, 5f);
        assertFalse(serviceHelper.reversedListIsAscending(a));
        List<Float> aReversed = List.of(5f, 4f, 3f, 2f, 1f);
        assertTrue(serviceHelper.reversedListIsAscending(aReversed));
        List<Float> empty = List.of();
        assertFalse(serviceHelper.reversedListIsAscending(empty));
    }

    @Test
    void testIsConstant() {
        List<Float> a = List.of(1f, 2f, 3f, 4f, 5f);
        assertFalse(serviceHelper.isConstant(a));
        List<Float> aReversed = List.of(5f, 5f, 5f, 5f, 5f);
        assertTrue(serviceHelper.isConstant(aReversed));
        List<Float> empty = List.of();
        assertFalse(serviceHelper.isConstant(empty));
    }

    @Test
    void testEvaluateTrend() {
        List<Float> a = List.of(1f, 2f, 3f, 4f, 5f);
        assertEquals(TrendType.descending, serviceHelper.evaluateTrend(a));
        List<Float> aReversed = List.of(5f, 4f, 3f, 2f, 1f);
        assertEquals(TrendType.ascending, serviceHelper.evaluateTrend(aReversed));
        List<Float> aConstant = List.of(5f, 5f, 5f, 5f, 5f);
        assertEquals(TrendType.constant, serviceHelper.evaluateTrend(aConstant));
        List<Float> b = List.of(5f, 3f, 5f, 4f, 5f);
        assertEquals(TrendType.undefined, serviceHelper.evaluateTrend(b));
        List<Float> empty = List.of();
        assertEquals(TrendType.undefined, serviceHelper.evaluateTrend(empty));
    }
}
