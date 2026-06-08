package dto;

public class UserFormDTO {

    private final String username;
    private final String name;
    private final String email;
    private final String password;
    private final String role;

    public UserFormDTO(
            String username,
            String name,
            String email,
            String password,
            String role) {

        this.username = username;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }
}