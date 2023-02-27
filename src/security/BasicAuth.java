package security;

import com.sun.net.httpserver.*;
import dao.UserDao;
import exception.UserNotExistsException;
import model.User;
import util.Database;
import util.HashUtil;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Base64;

public class BasicAuth extends BasicAuthenticator {
    private static final String dbUrl = "jdbc:postgresql://localhost:5432/code21";
    private static final String dbUser = "root";
    private static final String dbPassword = "Qwerty1";
    public BasicAuth(String realm) {
        super(realm);
    }

    @Override
    public Result authenticate(HttpExchange t) {
        Headers rmap = t.getRequestHeaders();
        /*
         * look for auth token
         */
        String auth = rmap.getFirst ("Authorization");
        if (auth == null) {
            setAuthHeader(t);
            return new Authenticator.Retry (401);
        }
        int sp = auth.indexOf (' ');
        if (sp == -1 || !auth.substring(0, sp).equals ("Basic")) {
            return new Authenticator.Failure (401);
        }
        byte[] b = Base64.getDecoder().decode(auth.substring(sp+1));
        String userpass = new String (b, StandardCharsets.UTF_8);
        int colon = userpass.indexOf (':');
        String uname = userpass.substring (0, colon);
        String pass = userpass.substring (colon+1);

        if (checkCredentials (uname, pass)) {
            return new Authenticator.Success (
                    new HttpPrincipal(
                            uname, realm
                    )
            );
        } else {
            /* reject the request again with 401 */
            setAuthHeader(t);
            return new Authenticator.Failure(401);
        }
    }

    private void setAuthHeader(HttpExchange t) {
        Headers map = t.getResponseHeaders();
        var authString = "Basic realm=" + "\"" + realm + "\"" + ", charset=\"UTF-8\"";
        map.set ("WWW-Authenticate", authString);
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
