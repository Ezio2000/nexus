package org.nexus.mysql;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.*;

/**
 * @author Xieningjun
 */
@RestController
@RequestMapping("/jdbc")
public class JdbcController {

    @GetMapping("/insert")
    public void insert(@RequestParam String username, @RequestParam String password) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        String url = "jdbc:mysql://10.3.0.21:63306/ruqi_gateway?useUnicode=true&characterEncoding=utf-8&useSSL=false";
        String dbUsername = "gac_travel_test_new";
        String dbPassword = "NJElna9OCLisAi#5RfY8oi#M1Qp71ZX";
        Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword);
        String sql = "insert into nexus_user (username, password) values (?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, username);
        ps.setString(2, password);
        ps.executeUpdate();

        conn.close();
        ps.close();
    }

}
