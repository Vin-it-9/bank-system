package servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import util.DatabaseUtils;

@WebServlet("/withdrawMoney")
public class WithdrawMoneyServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("username") == null) {
            response.sendRedirect("login.html");
            return;
        }

        String username = (String) session.getAttribute("username");
        int accountId = Integer.parseInt(request.getParameter("account_id"));
        double amount = Double.parseDouble(request.getParameter("amount"));

        if (amount <= 0) {
            response.sendRedirect("dashboard.jsp?error=invalidAmount");
            return;
        }

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseUtils.getConnection();
            String checkOwnershipQuery = "SELECT user_id, account_balance FROM accounts WHERE account_id = ?";
            stmt = conn.prepareStatement(checkOwnershipQuery);
            stmt.setInt(1, accountId);
            rs = stmt.executeQuery();
            if (rs.next()) {
                int userId = rs.getInt("user_id");
                double currentBalance = rs.getDouble("account_balance");
                if (isUserAuthorized(userId, username, conn)) {
                    if (currentBalance >= amount) {
                        // Check if the withdrawal amount exceeds the current balance
                        double newBalance = currentBalance - amount;
                        if (newBalance >= 0) {
                            String updateBalanceQuery = "UPDATE accounts SET account_balance = ? WHERE account_id = ?";
                            stmt = conn.prepareStatement(updateBalanceQuery);
                            stmt.setDouble(1, newBalance);
                            stmt.setInt(2, accountId);
                            int rowsUpdated = stmt.executeUpdate();
                            if (rowsUpdated > 0) {
                                response.sendRedirect("dashboard.jsp?message=withdrawalSuccess");
                            } else {
                                response.sendRedirect("dashboard.jsp?error=withdrawalFailed");
                            }
                        } else {
                            response.sendRedirect("dashboard.jsp?error=insufficientFunds");
                        }
                    } else {
                        response.sendRedirect("dashboard.jsp?error=insufficientFunds");
                    }
                } else {
                    response.sendRedirect("dashboard.jsp?error=unauthorizedAccess");
                }
            } else {
                response.sendRedirect("dashboard.jsp?error=accountNotFound");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            response.sendRedirect("dashboard.jsp?error=databaseError");
        } finally {
            DatabaseUtils.closeResources(rs, stmt, conn);
        }
    }

    private boolean isUserAuthorized(int userId, String username, Connection conn) throws SQLException {
        String checkUserQuery = "SELECT id FROM users WHERE username = ? AND id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(checkUserQuery)) {
            stmt.setString(1, username);
            stmt.setInt(2, userId);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        }
    }
}
