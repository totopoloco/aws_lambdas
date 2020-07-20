package at.mavila.aws.laplace;


import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.math.Fraction;
import org.apache.commons.math3.fraction.FractionFormat;

import java.math.BigDecimal;

public final class Rational {

    private static final ThreadLocal<FractionFormat> FRACTION_FORMAT_THREAD_LOCAL =
            ThreadLocal.withInitial(FractionFormat::getProperInstance);

    private Rational() {
    }


    public static String getFraction(final BigDecimal bigDecimal){
        Validate.notNull(bigDecimal, "Parameter decimal must not be null");
        return FRACTION_FORMAT_THREAD_LOCAL
                .get()
                .format(Fraction.getFraction(bigDecimal.doubleValue()))
                .replaceAll("\\s+","");
    }
}

