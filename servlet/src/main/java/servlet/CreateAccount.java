package servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/createAccount")
public class CreateAccount extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/bankdb";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "root";

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("username") == null) {
            response.sendRedirect("login.html");
            return;
        }

        String username = (String) session.getAttribute("username");
        String accountName = request.getParameter("accountName");
        String password = request.getParameter("password");

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);

            // Get user ID from username
            String getUserQuery = "SELECT id FROM users WHERE username = ?";
            stmt = conn.prepareStatement(getUserQuery);
            stmt.setString(1, username);
            rs = stmt.executeQuery();

            if (rs.next()) {
                int userId = rs.getInt("id");

                // Insert new account
                String insertAccountQuery = "INSERT INTO accounts (user_id, account_name, account_balance, password) VALUES (?, ?, 0.00, ?)";
                stmt = conn.prepareStatement(insertAccountQuery);
                stmt.setInt(1, userId);
                stmt.setString(2, accountName);
                stmt.setString(3, password);

                stmt.executeUpdate();
                response.sendRedirect("dashboard.jsp?message=accountCreated");
            } else {
                response.sendRedirect("dashboard.jsp?error=userNotFound");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            response.sendRedirect("dashboard.jsp?error=failed");
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}
