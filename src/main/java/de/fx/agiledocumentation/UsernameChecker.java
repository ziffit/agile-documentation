package de.fx.agiledocumentation;

public class UsernameChecker {

    boolean check(String username) {
        if (username == null || username.isEmpty()) {
            return false;
        }
        // Check if the username contains only alphanumeric characters and underscores
        return username.matches("^[a-zA-Z0-9]{5,20}$");
    }
}
