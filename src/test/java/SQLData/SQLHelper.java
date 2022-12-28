package SQLData;

import data.DataHelper;
import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLHelper {
    private static QueryRunner runner = new QueryRunner();

    private SQLHelper(){
    }

    @SneakyThrows
    private static Connection getConn(){
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/app", "app","pass");
    }

    public static DataHelper.VerificationCode getVerificationCode() {
        var codeSQL = "SELECT * FROM auth_codes ORDER BY created DESC LIMIT 1";
        try (var conn = getConn()) {
            var result = runner.query(conn, codeSQL, new BeanHandler<>(DataHelper.AuthCode.class));
            return new DataHelper.VerificationCode(result.getCode());
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public static DataHelper.Status getUserStatus() {
        var codeSQL = "select status from users where login='vasya';";
        try (var conn = getConn()) {
            var result = runner.query(conn, codeSQL, new ScalarHandler<String>());
            return new DataHelper.Status(result);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    @SneakyThrows
    public static void cleanDatabase(){
        var connection = getConn();
        runner.execute(connection, "DELETE FROM auth_codes");
        runner.execute(connection, "DELETE FROM card_transactions");
        runner.execute(connection, "DELETE FROM cards");
        runner.execute(connection, "DELETE FROM users");
    }
}
