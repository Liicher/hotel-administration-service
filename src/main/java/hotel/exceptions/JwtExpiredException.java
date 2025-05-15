package hotel.exceptions;

public class JwtExpiredException extends RuntimeException {
	public JwtExpiredException(String message) {
		super(message);
	}
}
