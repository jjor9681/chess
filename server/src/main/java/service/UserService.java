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

        // Username passed security. It can go to the database.
        userDAO.createUser(userData);

        // Time to return an authToken. This was in the phase instructions.
        String authToken = UUID.randomUUID().toString();
        AuthData authData = new AuthData(authToken, userData.username());
        authDAO.createAuth(authData);
        return authData;
    }

    public void login() {

    }

    public void logout() {

    }
}