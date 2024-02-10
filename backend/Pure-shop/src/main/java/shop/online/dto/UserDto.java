package shop.online.dto;

import shop.online.enums.UserRole;

public class UserDto {

	private long id;
	private String email;
	private String name;
	private UserRole userRole;
	public UserDto(long id, String email, String name, UserRole userRole) {
		super();
		this.id = id;
		this.email = email;
		this.name = name;
		this.userRole = userRole;
	}
	public UserDto() {
		super();
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public UserRole getUserRole() {
		return userRole;
	}
	public void setUserRole(UserRole userRole) {
		this.userRole = userRole;
	}
	
}
