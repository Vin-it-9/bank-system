package servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // JDBC URL, username, and password of MySQL server
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/bankdb";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "root";

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            // Load the JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish a connection to MySQL database
            conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);

            // SQL query to check if username and password match
            String query = "SELECT * FROM users WHERE username = ? AND password = ?";
            stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);

            rs = stmt.executeQuery();

            if (rs.next()) {
                // Retrieve additional user information
                String email = rs.getString("email");
                Timestamp memberSince = rs.getTimestamp("created_at");

                // Authentication successful, create a session
                HttpSession session = request.getSession();
                session.setAttribute("username", username);
                session.setAttribute("email", email);
                session.setAttribute("memberSince", memberSince);

                // Redirect to dashboard or success page
                response.sendRedirect("dashboard.jsp");
            } else {
                // Authentication failed, redirect back to login page with error message
                response.sendRedirect("login.html?error=invalid");
            }
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
            response.sendRedirect("error.html");
        } finally {
            // Close resources
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
