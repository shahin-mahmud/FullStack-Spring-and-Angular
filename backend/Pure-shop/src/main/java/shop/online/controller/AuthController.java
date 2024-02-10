package shop.online.controller;

import java.io.IOException;
import java.util.Optional;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import shop.online.dto.AuthenticationRequest;
import shop.online.dto.SignupRequest;
import shop.online.dto.UserDto;
import shop.online.model.User;
import shop.online.repository.UserRepository;
import shop.online.services.auth.AuthService;
import shop.online.util.JWTutils;

@RestController
@RequiredArgsConstructor
public class AuthController {

	public static final String HEADAR_STRING = "Bearer ";
	public static final String TOKEN_PREFIX = "Authorization";
	@Autowired
	private final AuthenticationManager authenticationManager;
	private final UserDetailsService userDetailsService;
	private final UserRepository userRepository;
	private final JWTutils jwTutils;
	private final AuthService authService;
	

	public AuthController(AuthenticationManager authenticationManager, UserDetailsService userDetailsService,
			UserRepository userRepository, JWTutils jwTutils,AuthService authService) {
		super();
		this.authenticationManager = authenticationManager;
		this.userDetailsService = userDetailsService;
		this.userRepository = userRepository;
		this.jwTutils = jwTutils;
		this.authService = authService;
	}

	@PostMapping("/authenticate")
	public void  createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest,HttpServletResponse response) throws IOException, JSONException {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
		}
		catch (BadCredentialsException e){
			new BadCredentialsException("Please Input Correct Username and Password");
		}
		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
		Optional<User> optionalUser = userRepository.findFirstByEmail(userDetails.getUsername());
		final String jwt = jwTutils.generateToken(userDetails.getUsername());
		if(optionalUser.isPresent()) {
			response.getWriter().write(new JSONObject()
					.put("userId",optionalUser.get().getId())
					.put("role",optionalUser.get().getRole())
					.toString()
					);
		//	response.addHeader("Access-Control-Expose-Headers", "Authorization");
		//	response.addHeader("Access-Control-Allow-Origin", "*");
			response.addHeader("Access-Control-Allow-Headers", "Authorization, X-PINGOTHER, Origin, "+
			"X-Requested-With,Content-Type,Accept,X-Custom-header");
			response.addHeader(HEADAR_STRING,TOKEN_PREFIX+jwt);
			
		}
	}
	@PostMapping("/sign-up")
	public ResponseEntity<?> signupUser(@RequestBody SignupRequest signupRequest){
		if(authService.hasUserWithEmail(signupRequest.getEmail())) {
			return new ResponseEntity<>("User already exists",HttpStatus.NOT_ACCEPTABLE);
		}
		UserDto userDto = authService.createUser(signupRequest);
		return new ResponseEntity<>(userDto,HttpStatus.OK);
	}
}
