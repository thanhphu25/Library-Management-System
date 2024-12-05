package Interface;

/**
 * Interface representing an observer in the observer design pattern.
 */
public interface Observer {
    /**
     * Updates the observer with a new message.
     *
     * @param message The message to update the observer with.
     */
    void update(String message);
}
