package at.mavila.aws.recognition;

import at.mavila.aws.recognition.impls.FaceDetectionEngine;
import at.mavila.aws.recognition.interfaces.Detector;
import at.mavila.aws.recognition.pojos.FaceDetection;
import com.amazonaws.services.rekognition.model.AgeRange;
import com.amazonaws.services.rekognition.model.Attribute;
import com.amazonaws.services.rekognition.model.Emotion;
import com.amazonaws.services.rekognition.model.FaceDetail;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class App {

    private static final Detector<FaceDetection, List<FaceDetail>> FACE_DETECTOR = new FaceDetectionEngine();

    public static void main(String[] args) {

        try {

            FaceDetection faceDetection = new FaceDetection("two.jpg", "marcot-avilac-exam-000", Attribute.ALL);
            final List<FaceDetail> facesDetected = FACE_DETECTOR.detect(faceDetection);

            facesDetected.forEach(faceDetail -> {

                AgeRange ageRange = faceDetail.getAgeRange();

                System.out.println(
                        "The detected face is estimated to be between " + ageRange.getLow().toString() + " and "
                                + ageRange.getHigh().toString() + " years old.");

                faceDetail.getEmotions().forEach(emotion -> System.out.println(emotion.toString()));

                //attributesDump(faceDetail);

            });
        } catch (Exception e) {

            System.err.println(e.getMessage());
            e.printStackTrace();

        }

    }

    private static void attributesDump(final FaceDetail faceDetail) {
        System.out.println("Here's the complete set of attributes:");

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(faceDetail));
        } catch (JsonProcessingException e) {
            System.err.println("JsonProcessingException caught: " + e.getMessage());
        }
    }
}