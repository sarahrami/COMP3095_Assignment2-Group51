package ca.gbc.userservice.services;

import ca.gbc.userservice.dto.UserRequest;
import ca.gbc.userservice.dto.UserResponse;
import ca.gbc.userservice.model.User;
import java.util.List;

public interface UserService {
    List<UserResponse> findAll();
    UserResponse findById(Long id); // Changed the type from Long to String
    User save(UserRequest user);
    User update(Long id, User user); // Changed the type from Long to String
    boolean delete(Long id); // Changed the type from Long to String
}

