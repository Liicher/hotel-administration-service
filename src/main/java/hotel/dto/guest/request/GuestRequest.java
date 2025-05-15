package hotel.dto.guest.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GuestRequest {
	@NotNull(message = "Guest name cannot be null")
	private String name;

	@NotNull(message = "Guest last name cannot be null")
	private String lastName;
}
