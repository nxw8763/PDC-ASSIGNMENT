package dto;

public class UserDTO {

    private final int userId;
    private final String username;
    private final String name;
    private final String email;
    private final String role;

    public UserDTO(
            int userId,
            String username,
            String name,
            String email,
            String role) {

        this.userId = userId;
        this.username = username;
        this.name = name;
        this.email = email;
        this.role = role;
    }

    public int getUserId() {
        return userId;
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

    public String getRole() {
        return role;
    }
}