package security;

import com.sun.net.httpserver.*;
import dao.UserDao;
import exception.UserNotExistsException;
import model.User;
import util.Database;
import util.HashUtil;
import util.Role;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Base64;

public class BasicAuth extends BasicAuthenticator {
    public BasicAuth(String realm) {
        super(realm);
    }

    @Override
    public Result authenticate(HttpExchange t) {
        Headers rmap = t.getRequestHeaders();

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
        String phone = userpass.substring (0, colon);
        String pass = userpass.substring (colon+1);

        User user;

        try (Connection connection = Database.getConnection()){
            UserDao userDao = new UserDao(connection);
            user = userDao.getUserByPhone(phone);
        }
        catch (UserNotExistsException e) {
            return new Authenticator.Failure (401);
        }
        catch (SQLException e) {
            return new Authenticator.Failure (500);
        }

        if (checkCredentials (phone, pass)) {
            if ("GET".equalsIgnoreCase(t.getRequestMethod())) {
                return new Authenticator.Success(
                        new HttpPrincipal(
                                phone, realm
                        )
                );
            }
            if (("POST".equalsIgnoreCase(t.getRequestMethod()) || "PUT".equalsIgnoreCase(t.getRequestMethod()))
                    && (t.getRequestURI().getPath().substring(1).split("/")[0].equalsIgnoreCase("users")
                    || t.getRequestURI().getPath().substring(1).split("/")[0].equalsIgnoreCase("items"))
                    && user.getRole().equals(Role.ROLE_ADMIN.getRole())) {
                return new Authenticator.Success(
                        new HttpPrincipal(
                                phone, realm
                        )
                );
            }
            if ("POST".equalsIgnoreCase(t.getRequestMethod())
                    && t.getRequestURI().getPath().substring(1).split("/")[0].equalsIgnoreCase("orders")
                    && user.getRole().equals(Role.ROLE_USER.getRole())) {
                return new Authenticator.Success(
                        new HttpPrincipal(
                                phone, realm
                        )
                );
            }
            else {
                return new Authenticator.Failure(403);
            }
        }
        else {
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
        try (Connection connection = Database.getConnection()){
            UserDao userDao = new UserDao(connection);
            User user = userDao.getUserByPhone(phone);

            return HashUtil.isValid(password, user.getPassword());
        }
        catch (UserNotExistsException | NoSuchAlgorithmException | SQLException e) {
            return false;
        }
    }
}
