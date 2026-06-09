package service;

import dataaccess.AuthDAO;
import dataaccess.UserDAO;

public class UserService {

    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public UserService(UserDAO userDataAccessObject, AuthDAO authDataAccessObject) {
        this.userDAO = userDataAccessObject;
        this.authDAO = authDataAccessObject;
    }
}