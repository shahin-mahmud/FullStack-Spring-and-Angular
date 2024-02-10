package shop.online.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {
	@GetMapping("/index")
public String home() {
	return "Hello from Pure Shop";
}
}
