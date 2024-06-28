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

@WebServlet("/changePassword")
public class ChangePassword extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/bankdb";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "root";

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String username = (String) session.getAttribute("username");

        String currentPassword = request.getParameter("currentPassword");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        if (newPassword == null || !newPassword.equals(confirmPassword)) {
            response.sendRedirect("dashboard.jsp?error=nomatch");
            return;
        }

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);

            String query = "SELECT password FROM users WHERE username = ?";
            stmt = conn.prepareStatement(query);
            stmt.setString(1, username);

            rs = stmt.executeQuery();

            if (rs.next()) {
                String currentPasswordInDB = rs.getString("password");
                if (!currentPassword.equals(currentPasswordInDB)) {
                    response.sendRedirect("dashboard.jsp?error=incorrect");
                    return;
                }

                query = "UPDATE users SET password = ? WHERE username = ?";
                stmt = conn.prepareStatement(query);
                stmt.setString(1, newPassword);
                stmt.setString(2, username);
                stmt.executeUpdate();

                response.sendRedirect("dashboard.jsp?message=success");
            } else {
                response.sendRedirect("dashboard.jsp?error=unknown");
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
