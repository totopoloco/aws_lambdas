package at.mavila.aws.recognition.impls;

import at.mavila.aws.recognition.interfaces.Detector;
import at.mavila.aws.recognition.pojos.FaceDetection;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.*;

import java.util.List;

public class FaceDetectionEngine implements Detector<FaceDetection, List<FaceDetail>> {


    @Override
    public List<FaceDetail> detect(FaceDetection faceDetection) {

        AmazonRekognition defaultClient = AmazonRekognitionClientBuilder.defaultClient();

        final S3Object s3Object = new S3Object().withName(faceDetection.getPhoto()).withBucket(faceDetection.getBucket());
        final Image image = new Image().withS3Object(s3Object);
        DetectFacesRequest request = new DetectFacesRequest().withImage(image).withAttributes(faceDetection.getAttribute());
        // Replace Attribute.ALL with Attribute.DEFAULT to get default values.

        return defaultClient.detectFaces(request).getFaceDetails();

    }

}
