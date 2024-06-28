package servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import util.DatabaseUtils;

@WebServlet("/depositMoney")
public class DepositMoneyServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        // Get parameters from the request
        int accountId = Integer.parseInt(request.getParameter("account_id"));
        String accountName = request.getParameter("account_name");
        double amount = Double.parseDouble(request.getParameter("amount"));

        try (Connection conn = DatabaseUtils.getConnection()) {
            // Create SQL update statement
            String sql = "UPDATE accounts SET account_balance = account_balance + ? WHERE account_id = ? AND account_name = ?";

            // Prepare the statement
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                // Set the parameters
                pstmt.setDouble(1, amount);
                pstmt.setInt(2, accountId);
                pstmt.setString(3, accountName);

                // Execute the update
                int rowsUpdated = pstmt.executeUpdate();

                if (rowsUpdated > 0) {
                    // Redirect to the success page
                    response.sendRedirect("success.html");
                } else {
                    // Redirect to the error page
                    response.sendRedirect("error.html");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("error.html");
        }
    }
}
