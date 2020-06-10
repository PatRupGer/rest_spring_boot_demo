package de.pat.rup.a.bk.service;

import com.sun.istack.NotNull;
import de.pat.rup.a.bk.models.TrendType;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CalculatorService {

    /**
     * Calculates the average of a List
     *
     * @param elements a list of Floats
     * @return the Average Value, or 0 if empty list
     */
    public float calcAverage(@NotNull List<Float> elements) {
        if (elements.isEmpty()) {
            return 0;
        }
        float sum = 0;
        for (float i : elements) {
            sum += i;
        }
        return sum / elements.size();
    }

    /**
     * Evaluated if a List of elements has a Trend
     *
     * @param elements a list of Elements
     * @return a TrendType for the List
     */
    public TrendType evaluateTrend(@NotNull List<Float> elements) {
        if (elements.isEmpty())
            return TrendType.undefined;
        if (isConstant(elements))
            return TrendType.constant;
        if (reversedListIsAscending(elements))
            return TrendType.ascending;
        if (reversedListIsDescending(elements))
            return TrendType.descending;
        return TrendType.undefined;
    }

    /**
     * Check if Elements are ascending
     *
     * @param reversedOrder elements have to be in reversed Order
     * @return true if Elements are ascending
     */
    public boolean reversedListIsAscending(@NotNull List<Float> reversedOrder) {
        if (reversedOrder.isEmpty())
            return false;
        for (int i = reversedOrder.size() - 1; i > 1; i--) {
            if ((reversedOrder.get(i) > reversedOrder.get(i - 1))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check if Elements ar descending
     *
     * @param reversedOrder elements have to be in reversed Order
     * @return true if Elements are Descending
     */
    public boolean reversedListIsDescending(@NotNull List<Float> reversedOrder) {
        if (reversedOrder.isEmpty())
            return false;
        for (int i = reversedOrder.size() - 1; i > 1; i--) {
            if ((reversedOrder.get(i) < reversedOrder.get(i - 1))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check if all elements in List have the same Value
     *
     * @param elements a list of Elements
     * @return true, if all Elements in List dont change
     */
    public boolean isConstant(@NotNull List<Float> elements) {
        if (elements.isEmpty())
            return false;
        for (float e : elements) {
            if (e != elements.get(0))
                return false;
        }
        return true;
    }
}
