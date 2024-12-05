package Interface;

public interface Subject {

    /**
     * Attaches an observer to the subject.
     *
     * @param observer the observer to be attached
     */
    void attach(Observer observer);

    /**
     * Detaches an observer from the subject.
     *
     * @param observer the observer to be detached
     */
    void detach(Observer observer);

    /**
     * Notifies all attached observers with a message.
     *
     * @param message the message to be sent to observers
     */
    void notifyObservers(String message);
}
