package hotel.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import hotel.dto.user.RegisterRequest;
import hotel.entities.security.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "role", ignore = true)
	User toEntity(RegisterRequest registerRequest);
}
