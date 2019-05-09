package ru.lod_misis.ithappened.ui.utils;

import java.math.BigDecimal;
import java.math.MathContext;

import ru.lod_misis.ithappened.domain.statistics.facts.StringParse;

public class WorkWithDecimal {

    public static String makeDecimalWithMantisa(Double decimal) {

        Long par = 1L;
        while (decimal > 10) {
            par=par*10;
            decimal=decimal/10;
        }
        return StringParse.parseDouble(BigDecimal.valueOf(decimal).round(new MathContext(3)).doubleValue()*par);
    }
}
