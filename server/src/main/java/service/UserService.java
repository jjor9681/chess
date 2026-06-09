package service;

import java.util.UUID;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;

public class UserService {

    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public UserService(UserDAO userDataAccessObject, AuthDAO authDataAccessObject) {
        this.userDAO = userDataAccessObject;
        this.authDAO = authDataAccessObject;
    }

    public AuthData register(UserData userData) throws AlreadyTakenException,BadRequestException, DataAccessException {
        // Successful registration should return an authToken and a username, so I've chosen AuthData for the return type here.

        // Check for a bad request:
        if (userData.username() == null || userData.email() == null || userData.password() == null){
            throw new BadRequestException("Error: bad request");
        }

        // Check if a username is already taken, which I can take advantage of the fact that I used a hashmap.
        UserData otherUser = userDAO.getUser(userData.username());
        if (otherUser != null){
            throw new AlreadyTakenException("Error: already taken");
        }

        // Username passed security. (The username is available.)
        userDAO.createUser(userData);

        // Time to return an authToken and username. This was in the phase instructions.
        String authToken = UUID.randomUUID().toString();
        AuthData authData = new AuthData(authToken, userData.username());
        authDAO.createAuth(authData);
        return authData;
    }

    public AuthData login(UserData userData) throws UnauthorizedException, BadRequestException, DataAccessException {
        // Same checks as earlier on the username.
        if (userData.username() == null || userData.password() == null) {
            throw new BadRequestException("Error: bad request");
        }
        // Basically a copy and paste from register, except we want the name to be taken for login purposes.
        UserData otherUser = userDAO.getUser(userData.username());
        if (otherUser == null){
            throw new UnauthorizedException("Error: unauthorized");
        }
        // See if passwords match
        if (otherUser.password().equals(userData.password())){

            // No need to create another user. That is done in register. This authData will only have the authToken that is new.

            String authToken = UUID.randomUUID().toString();
            AuthData authData = new AuthData(authToken, userData.username());
            authDAO.createAuth(authData);
            return authData;

        } // Passwords didn't match, --> unauthorized.
        throw new UnauthorizedException("Error: unauthorized");

    }

    public void logout(String authToken) throws UnauthorizedException, DataAccessException {

        // If they're not logged in in the first place they should not be able to log out.
        AuthData authData = authDAO.getAuth(authToken);
        if (authData == null){
            throw new UnauthorizedException("Error: unauthorized");
        }

        // Delete authtoken
        authDAO.deleteAuth(authToken);

    }
}