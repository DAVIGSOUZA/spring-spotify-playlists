package ada.spotify.backend.model.user;

public record UserDTO(String username, String email, String role) {

    public UserDTO(User userModel) {
        this(userModel.getUsername() , userModel.getEmail(), userModel.getRole().toString());
    }

}
