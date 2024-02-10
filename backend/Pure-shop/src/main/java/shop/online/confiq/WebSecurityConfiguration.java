package shop.online.confiq;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import shop.online.filter.JwtRequestFilter;
@Data
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfiguration {

	private final JwtRequestFilter authFilter;
	
	
	public WebSecurityConfiguration(JwtRequestFilter authFilter) {
		
		this.authFilter = authFilter;
	}
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	return	http.csrf((csrf) ->csrf.disable())
			.authorizeHttpRequests((authorize) ->authorize.requestMatchers("/authenticate","/sign-up","/order/**").permitAll())
			.authorizeHttpRequests((authorize) ->authorize.requestMatchers("/api/**").authenticated())
			.formLogin((formLogin) ->
				formLogin
					.usernameParameter("username")
					.passwordParameter("password")
					.loginPage("/authenticate")
					.failureUrl("/authentication/login?failed")
					.loginProcessingUrl("/authentication/login/process"))
			.addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class).build();
	}
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}
}
