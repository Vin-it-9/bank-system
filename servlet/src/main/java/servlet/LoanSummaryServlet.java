package servlet;

import java.io.*;
import java.math.BigDecimal;
import java.sql.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/LoanSummaryServlet")
public class LoanSummaryServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new ServletException("MySQL JDBC Driver not found.", e);
        }
        String accountIdStr = request.getParameter("account_id");
        String password = request.getParameter("password");

        int accountId;
        try {
            accountId = Integer.parseInt(accountIdStr);
        } catch (NumberFormatException e) {
            response.getWriter().println("Invalid account ID format.");
            return;
        }
        String jdbcUrl = "jdbc:mysql://localhost:3306/bankdb";
        String dbUsername = "root";
        String dbPassword = "root";

        try (Connection conn = DriverManager.getConnection(jdbcUrl, dbUsername, dbPassword)) {
            String validateSql = "SELECT * FROM accounts WHERE account_id = ? AND password = ?";
            try (PreparedStatement validateStmt = conn.prepareStatement(validateSql)) {
                validateStmt.setInt(1, accountId);
                validateStmt.setString(2, password);

                try (ResultSet rs = validateStmt.executeQuery()) {
                    if (!rs.next()) {
                        response.getWriter().println("Invalid account ID or password.");
                        return;
                    }
                }
            }
            String loanSumSql = "SELECT COALESCE(SUM(l.loan_amount), 0) AS total_loan_amount, "
                    + "COALESCE(SUM(lu.updated_loan_amount), 0) AS total_updated_loan_amount "
                    + "FROM loans l "
                    + "LEFT JOIN loan_updates lu ON l.loan_id = lu.loan_id "
                    + "WHERE l.account_id = ?";
            try (PreparedStatement loanSumStmt = conn.prepareStatement(loanSumSql)) {
                loanSumStmt.setInt(1, accountId);

                try (ResultSet rs = loanSumStmt.executeQuery()) {
                    BigDecimal totalLoanAmount = BigDecimal.ZERO;
                    BigDecimal totalUpdatedLoanAmount = BigDecimal.ZERO;

                    if (rs.next()) {
                        totalLoanAmount = rs.getBigDecimal("total_loan_amount");
                        totalUpdatedLoanAmount = rs.getBigDecimal("total_updated_loan_amount");
                    }
                    request.setAttribute("totalLoanAmount", totalLoanAmount);
                    request.setAttribute("totalUpdatedLoanAmount", totalUpdatedLoanAmount);
                    request.getRequestDispatcher("/loan_summary_result.jsp").forward(request, response);
                }
            }

        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }
}
