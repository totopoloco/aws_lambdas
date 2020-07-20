package at.mavila.aws.recognition.interfaces;

@FunctionalInterface
public interface Detector<I, O> {

    /**
     * Detect and produce a result
     *
     * @param i
     * @return
     */
    O detect(final I i);

}
