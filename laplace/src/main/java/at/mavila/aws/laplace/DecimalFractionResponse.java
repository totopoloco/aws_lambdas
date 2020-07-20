package at.mavila.aws.laplace;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class DecimalFractionResponse {
    private final String decimal;
    private final String fraction;

    public DecimalFractionResponse(String decimal, String fraction) {
        this.decimal = decimal;
        this.fraction = fraction;
    }

    public String getDecimal() {
        return this.decimal;
    }

    public String getFraction() {
        return this.fraction;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("decimal", this.decimal)
                .append("fraction", this.fraction).toString();
    }
}

