package at.mavila.aws.laplace;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.Objects;

import static java.util.Objects.isNull;

public class LaplaceController implements RequestHandler<Map<String, Object>, APIGatewayProxyResponseEvent> {

    private static final String SUCCESS = "SUCCESS";
    private static final String FAILURE = "FAILURE";
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private LambdaLogger logger;

    @Override
    public APIGatewayProxyResponseEvent handleRequest(final Map<String,Object> input, final Context context) {

        this.logger = context.getLogger();

        this.logger.log("Env variables: " + GSON.toJson(System.getenv()));
        this.logger.log("Context: " + GSON.toJson(context));
        this.logger.log("Event: " + GSON.toJson(input));
        this.logger.log("Event class: " + GSON.toJson(input.getClass().getName()));



       input.forEach((key, value) -> {
            this.logger.log("Key in the map: " + GSON.toJson(key));
            this.logger.log("Type of value: "+GSON.toJson(isNull(value) ? "null" : value.getClass().getName()));
            this.logger.log("Value in the map: " + GSON.toJson(value));
        });

        return handleRequest(input);

        /*try {
            final SnsClient snsClient = getSnsClient(logger);
            return SUCCESS;
        } catch (Exception exception) {
            logger.log("Exception caught: " + exception.getMessage());
            return FAILURE;
        }*/
    }

    private APIGatewayProxyResponseEvent handleRequest(Map<String, Object> input) {
        String dices = null;
        String positiveCases= null;

        try {
            dices = getElement(input, "dices", "Expected dicesObj to be a String object but got: ");

            final long dicesL = Long.parseLong(dices);
            final long possibleCases = this.possibleCases(dicesL);

            positiveCases = getElement(input, "positive_cases",
                    "Expected positiveCasesObj to be a String object but got: ");
            final long positiveCasesL = Long.parseLong(positiveCases);

            if (positiveCasesL > possibleCases) {
                return createResponse(FAILURE, 500);
            }

            return getCalculationResult(possibleCases, positiveCasesL);
        } catch (Exception e){
            final boolean aNull = isNull(positiveCases);
            final String s = aNull ? dices : positiveCases;



            APIGatewayProxyResponseEvent apiGatewayProxyResponseEvent = new APIGatewayProxyResponseEvent();
            apiGatewayProxyResponseEvent.setBody(s + " - " + e.getMessage());
            apiGatewayProxyResponseEvent.setStatusCode(500);

            return apiGatewayProxyResponseEvent;
        }
    }

    private String getElement(Map<String, Object> input, String element, String errorMessage) {
        final Object dicesObj = input.get(element);

        if(dicesObj instanceof String){
            return (String) dicesObj;
        }

        final Object queryStringParameters = input.get("queryStringParameters");
        if(queryStringParameters instanceof Map<?,?>){
            Map<?,?> keysAndValues = (Map<?,?>)queryStringParameters;
            return (String) keysAndValues.get(element);
        }

        throw new IllegalStateException(errorMessage + (Objects.isNull(dicesObj) ? "null" : dicesObj.getClass().getName()));
    }

    private APIGatewayProxyResponseEvent createResponse(String text, Integer statusCode) {


        APIGatewayProxyResponseEvent apiGatewayProxyResponseEvent = new APIGatewayProxyResponseEvent();
        apiGatewayProxyResponseEvent.setBody(text);
        apiGatewayProxyResponseEvent.setStatusCode(statusCode);


        return apiGatewayProxyResponseEvent;
    }

    private APIGatewayProxyResponseEvent getCalculationResult(long possibleCases, long positiveCasesL) {
        BigDecimal bdPositiveCases = BigDecimal.valueOf(positiveCasesL);
        BigDecimal bdPossibleCases = BigDecimal.valueOf(possibleCases);

        BigDecimal divide = bdPositiveCases.divide(bdPossibleCases, 12, RoundingMode.HALF_UP);

        final DecimalFractionResponse src = new DecimalFractionResponse(divide.toString(),
                Rational.getFraction(divide));

        logger.log("Result of the operation: "+src);

        return createResponse(src.getDecimal() + "|" + src.getFraction(), 200);
    }

   /* private SnsClient getSnsClient(LambdaLogger logger) throws Exception {
        //sns client
        logger.log("Trying to get a SnsClientBuilder");
        final SnsClientBuilder builder = SnsClient.builder();
        logger.log("Trying to get a SnsClientBuilder with the region");

        DefaultSdkHttpClientBuilder defaultSdkHttpClientBuilder = new DefaultSdkHttpClientBuilder();
        AttributeMap attributeMap = AttributeMap.builder().build();
        defaultSdkHttpClientBuilder.buildWithDefaults(attributeMap);

        final SnsClientBuilder region = builder.region(Region.US_EAST_1).httpClientBuilder(defaultSdkHttpClientBuilder);
        logger.log("Trying to get a SnsClient");
        SnsClient snsClient = region.build();


        logger.log("I have a SNS Client: " + GSON.toJson(snsClient.getClass().getName()));
        return snsClient;
    }*/

    private long possibleCases(long dices) {
        long result = 1;
        while (dices > 0) {

            result = Constants.DICE_FACES * result;

            --dices;
        }
        return result;
    }

}