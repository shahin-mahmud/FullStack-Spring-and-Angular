package shop.online.services.auth;

import shop.online.dto.SignupRequest;
import shop.online.dto.UserDto;

public interface AuthService {

	UserDto createUser(SignupRequest signupRequest);
	boolean hasUserWithEmail(String email);
}
