package hotel.exceptions;

public class RoomCapacityOverflowException extends RuntimeException {
    public RoomCapacityOverflowException(String message) {
        super(message);
    }
}
