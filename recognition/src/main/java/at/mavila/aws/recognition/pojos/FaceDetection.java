package at.mavila.aws.recognition.pojos;

import com.amazonaws.services.rekognition.model.Attribute;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class FaceDetection {
    private final String photo;
    private final String bucket;
    private final Attribute attribute;

    public FaceDetection(String photo, String bucket, final Attribute attribute) {
        this.photo = photo;
        this.bucket = bucket;
        this.attribute = attribute;
    }

    public Attribute getAttribute() {
        return attribute;
    }

    public String getPhoto() {
        return photo;
    }

    public String getBucket() {
        return bucket;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("photo", photo).append("bucket", bucket).append("attribute", attribute)
                .toString();
    }
}
