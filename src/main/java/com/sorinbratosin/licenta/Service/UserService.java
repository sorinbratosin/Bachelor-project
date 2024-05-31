package com.sorinbratosin.licenta.Service;

import com.sorinbratosin.licenta.Database.UserDAO;
import com.sorinbratosin.licenta.POJO.User;
import com.sorinbratosin.licenta.Security.CredentialsAreNotValidException;
import com.sorinbratosin.licenta.Security.EmailAlreadyTakenException;
import com.sorinbratosin.licenta.Security.PasswordLengthException;
import com.sorinbratosin.licenta.Security.PasswordsDontMatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void save(String email, String nume, String prenume, String password1, String password2)
            throws IllegalArgumentException, EmailAlreadyTakenException, PasswordLengthException, PasswordsDontMatchException {

        if (email == null || nume == null || prenume == null || password1 == null || password2 == null) {
            throw new IllegalArgumentException("All parameters must be non-null");
        }

        if (userDAO.findByEmail(email).isPresent()) {
            throw new EmailAlreadyTakenException("This email is already associated with an account");
        }

        if (!password1.equals(password2)) {
            throw new PasswordsDontMatchException("Passwords don't match!");
        }

        if (password1.length() < 8) {
            throw new PasswordLengthException("Password should have more than 7 characters");
        }

        String encodedPassword = passwordEncoder.encode(password1);
        User user = new User();
        user.setEmail(email);
        user.setNume(nume);
        user.setPrenume(prenume);
        user.setPassword(encodedPassword);
        userDAO.save(user);
    }


    public void login(String email, String password) throws CredentialsAreNotValidException {
        User user = userDAO.findByEmail(email)
                .orElseThrow(() -> new CredentialsAreNotValidException("Invalid credentials"));
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new CredentialsAreNotValidException("Invalid credentials");
        }
        // logare cu succes, implementeaza sesiunea
    }

}
