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

@WebServlet("/checkBalance")
public class CheckBalanceServlet extends HttpServlet {
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
        String password = request.getParameter("password");

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseUtils.getConnection();
            String checkAccountQuery = "SELECT a.account_id, a.account_name, a.account_balance, u.username " +
                    "FROM accounts a INNER JOIN users u ON a.user_id = u.id " +
                    "WHERE a.account_id = ? AND u.username = ? AND a.password = ?";
            stmt = conn.prepareStatement(checkAccountQuery);
            stmt.setInt(1, accountId);
            stmt.setString(2, username);
            stmt.setString(3, password);
            rs = stmt.executeQuery();

            if (rs.next()) {
                int fetchedAccountId = rs.getInt("account_id");
                String accountName = rs.getString("account_name");
                double accountBalance = rs.getDouble("account_balance");
                request.setAttribute("account_id", fetchedAccountId);
                request.setAttribute("account_name", accountName);
                request.setAttribute("account_balance", accountBalance);
                request.getRequestDispatcher("balance.jsp").forward(request, response);
            } else {
                response.sendRedirect("dashboard.jsp?error=invalidCredentials");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            response.sendRedirect("dashboard.jsp?error=databaseError");
        } finally {
            DatabaseUtils.closeResources(rs, stmt, conn);
        }
    }
}
