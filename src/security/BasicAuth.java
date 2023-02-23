package security;

import com.sun.net.httpserver.BasicAuthenticator;
import dao.UserDao;
import exception.UserNotExistsException;
import model.User;
import util.Database;
import util.HashUtil;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;

public class BasicAuth extends BasicAuthenticator {
    private static final String dbUrl = "jdbc:postgresql://localhost:5432/code21";
    private static final String dbUser = "root";
    private static final String dbPassword = "Qwerty1";
    public BasicAuth(String realm) {
        super(realm);
    }

    @Override
    public boolean checkCredentials(String phone, String password) {
        try (Connection connection = Database.getDB(dbUrl, dbUser, dbPassword).getConnection()){
            UserDao userDao = new UserDao(connection);
            User user = userDao.getUserByPhone(phone);

            return HashUtil.isValid(password, user.getPassword());
        }
        catch (UserNotExistsException | NoSuchAlgorithmException | SQLException e) {
            return false;
        }
    }
}
