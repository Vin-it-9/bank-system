package servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import util.DatabaseUtils;

@WebServlet("/applyLoan")
public class ApplyLoanServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    public void init() throws ServletException {
        super.init();
        double interestRatePerMinute = 5.00 / 100 / 60;
        List<Integer> activeLoans = getActiveLoans();

        for (int loanId : activeLoans) {
            Timer timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    updateLoanAmount(loanId, interestRatePerMinute);
                }
            }, 0, 60000);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int accountId = Integer.parseInt(request.getParameter("account_id"));
        String contact = request.getParameter("contact");
        String password = request.getParameter("password");
        double loanAmount = Double.parseDouble(request.getParameter("loan_amount"));
        double interestRatePerMinute = 5.00 / 100 / 60;

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseUtils.getConnection();
            String checkAccountQuery = "SELECT password FROM accounts WHERE account_id = ?";
            stmt = conn.prepareStatement(checkAccountQuery);
            stmt.setInt(1, accountId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                String storedPassword = rs.getString("password");

                if (password.equals(storedPassword)) {
                    String insertLoanQuery = "INSERT INTO loans (account_id, contact, loan_amount, loan_time, interest_rate, loan_status) VALUES (?, ?, ?, ?, ?, ?)";
                    stmt = conn.prepareStatement(insertLoanQuery, PreparedStatement.RETURN_GENERATED_KEYS);
                    stmt.setInt(1, accountId);
                    stmt.setString(2, contact);
                    stmt.setDouble(3, loanAmount);
                    stmt.setInt(4, 0);
                    stmt.setDouble(5, interestRatePerMinute);
                    stmt.setString(6, "pending");
                    stmt.executeUpdate();

                    rs = stmt.getGeneratedKeys();
                    int loanId = 0;
                    if (rs.next()) {
                        loanId = rs.getInt(1);
                    }
                    insertUpdatedLoanAmount(loanId, loanAmount);
                    updateAccountBalance(conn, accountId, loanAmount);
                    Timer timer = new Timer();
                    final int finalLoanId = loanId;
                    timer.scheduleAtFixedRate(new TimerTask() {
                        @Override
                        public void run() {
                            updateLoanAmount(finalLoanId, interestRatePerMinute);
                        }
                    }, 0, 60000);

                    response.sendRedirect("success.html");
                } else {
                    response.sendRedirect("loan_form.html?error=incorrectPassword");
                }
            } else {
                response.sendRedirect("loan_form.html?error=accountNotFound");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            response.sendRedirect("loan_form.html?error=databaseError");
        } finally {
            DatabaseUtils.closeResources(rs, stmt, conn);
        }
    }

    private void insertUpdatedLoanAmount(int loanId, double loanAmount) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DatabaseUtils.getConnection();
            String insertUpdateQuery = "INSERT INTO loan_updates (loan_id, updated_loan_amount) VALUES (?, ?)";
            stmt = conn.prepareStatement(insertUpdateQuery);
            stmt.setInt(1, loanId);
            stmt.setDouble(2, loanAmount);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            DatabaseUtils.closeResources(null, stmt, conn);
        }
    }

    private void updateLoanAmount(int loanId, double interestRatePerMinute) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseUtils.getConnection();
            String selectLoanQuery = "SELECT updated_loan_amount FROM loan_updates WHERE loan_id = ? ORDER BY updated_at DESC LIMIT 1 FOR UPDATE";
            stmt = conn.prepareStatement(selectLoanQuery);
            stmt.setInt(1, loanId);
            rs = stmt.executeQuery();

            double currentAmount = 0;
            if (rs.next()) {
                currentAmount = rs.getDouble("updated_loan_amount");
            }
            double updatedAmount = currentAmount * (1 + interestRatePerMinute);
            String updateUpdateQuery = "UPDATE loan_updates SET updated_loan_amount = ?, updated_at = CURRENT_TIMESTAMP WHERE loan_id = ?";
            stmt = conn.prepareStatement(updateUpdateQuery);
            stmt.setDouble(1, updatedAmount);
            stmt.setInt(2, loanId);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            DatabaseUtils.closeResources(rs, stmt, conn);
        }
    }

    private void updateAccountBalance(Connection conn, int accountId, double loanAmount) {
        PreparedStatement stmt = null;
        try {
            String updateBalanceQuery = "UPDATE accounts SET account_balance = account_balance + ? WHERE account_id = ?";
            stmt = conn.prepareStatement(updateBalanceQuery);
            stmt.setDouble(1, loanAmount);
            stmt.setInt(2, accountId);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            DatabaseUtils.closeResources(null, stmt, null);
        }
    }

    private List<Integer> getActiveLoans() {
        List<Integer> activeLoanIds = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DatabaseUtils.getConnection();
            String selectActiveLoansQuery = "SELECT loan_id FROM loans WHERE loan_status = 'pending'";
            stmt = conn.prepareStatement(selectActiveLoansQuery);
            rs = stmt.executeQuery();
            while (rs.next()) {
                activeLoanIds.add(rs.getInt("loan_id"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            DatabaseUtils.closeResources(rs, stmt, conn);
        }
        return activeLoanIds;
    }
}
