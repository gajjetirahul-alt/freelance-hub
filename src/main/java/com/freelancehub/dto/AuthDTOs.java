package com.freelancehub.dto;

import com.freelancehub.model.User;

public class AuthDTOs {

    public static class RegisterRequest {
        private String name;
        private String email;
        private String password;
        private User.Role role;

        public String getName()      { return name; }
        public String getEmail()     { return email; }
        public String getPassword()  { return password; }
        public User.Role getRole()   { return role; }
        public void setName(String name)         { this.name = name; }
        public void setEmail(String email)       { this.email = email; }
        public void setPassword(String password) { this.password = password; }
        public void setRole(User.Role role)      { this.role = role; }
    }

    public static class LoginRequest {
        private String email;
        private String password;

        public String getEmail()    { return email; }
        public String getPassword() { return password; }
        public void setEmail(String email)       { this.email = email; }
        public void setPassword(String password) { this.password = password; }
    }

    public static class AuthResponse {
        private String token;
        private String type = "Bearer";
        private Long userId;
        private String name;
        private String email;
        private User.Role role;

        public AuthResponse() {}
        public AuthResponse(String token, Long userId, String name, String email, User.Role role) {
            this.token = token;
            this.userId = userId;
            this.name = name;
            this.email = email;
            this.role = role;
        }

        public String getToken()    { return token; }
        public String getType()     { return type; }
        public Long getUserId()     { return userId; }
        public String getName()     { return name; }
        public String getEmail()    { return email; }
        public User.Role getRole()  { return role; }
    }
}
