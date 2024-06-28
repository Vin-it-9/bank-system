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
import util.DatabaseUtils;

@WebServlet("/repayLoan")
public class RepayLoanServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int accountId = Integer.parseInt(request.getParameter("account_id"));
        double repaymentAmount = Double.parseDouble(request.getParameter("repayment_amount"));
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseUtils.getConnection();
            String selectLoansQuery = "SELECT l.loan_id, lu.updated_loan_amount FROM loans l JOIN loan_updates lu ON l.loan_id = lu.loan_id WHERE l.account_id = ? AND l.loan_status = 'pending' ORDER BY lu.updated_at DESC";
            stmt = conn.prepareStatement(selectLoansQuery);
            stmt.setInt(1, accountId);
            rs = stmt.executeQuery();

            double remainingRepaymentAmount = repaymentAmount;

            while (rs.next() && remainingRepaymentAmount > 0) {
                int loanId = rs.getInt("loan_id");
                double currentLoanAmount = rs.getDouble("updated_loan_amount");

                double newLoanAmount = currentLoanAmount - remainingRepaymentAmount;

                if (newLoanAmount <= 0) {
                    remainingRepaymentAmount = -newLoanAmount;
                    newLoanAmount = 0;
                    String updateLoanStatusQuery = "UPDATE loans SET loan_status = 'repaid' WHERE loan_id = ?";
                    try (PreparedStatement updateStmt = conn.prepareStatement(updateLoanStatusQuery)) {
                        updateStmt.setInt(1, loanId);
                        updateStmt.executeUpdate();
                    }
                } else {
                    remainingRepaymentAmount = 0;
                }

                String updateLoanAmountQuery = "UPDATE loan_updates SET updated_loan_amount = ?, updated_at = CURRENT_TIMESTAMP WHERE loan_id = ?";
                try (PreparedStatement updateStmt = conn.prepareStatement(updateLoanAmountQuery)) {
                    updateStmt.setDouble(1, newLoanAmount);
                    updateStmt.setInt(2, loanId);
                    int rowsUpdated = updateStmt.executeUpdate();

                    if (rowsUpdated == 0) {
                        String insertLoanUpdateQuery = "INSERT INTO loan_updates (loan_id, updated_loan_amount) VALUES (?, ?)";
                        try (PreparedStatement insertStmt = conn.prepareStatement(insertLoanUpdateQuery)) {
                            insertStmt.setInt(1, loanId);
                            insertStmt.setDouble(2, newLoanAmount);
                            insertStmt.executeUpdate();
                        }
                    }
                }
            }
            updateAccountBalance(conn, accountId, -repaymentAmount);
            response.sendRedirect("repayment_success.html");
        } catch (SQLException ex) {
            ex.printStackTrace();
            response.sendRedirect("repayment_form.html?error=databaseError");
        } finally {
            DatabaseUtils.closeResources(rs, stmt, conn);
        }
    }
    private void updateAccountBalance(Connection conn, int accountId, double amount) {
        PreparedStatement stmt = null;
        try {
            String updateBalanceQuery = "UPDATE accounts SET account_balance = account_balance + ? WHERE account_id = ?";
            stmt = conn.prepareStatement(updateBalanceQuery);
            stmt.setDouble(1, amount);
            stmt.setInt(2, accountId);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            DatabaseUtils.closeResources(null, stmt, null);
        }
    }
}
