<%@ page import="java.math.BigDecimal" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Loan Summary Result</title>
    <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet">
</head>
<body class="bg-gray-100 flex items-center justify-center h-screen">
    <div class="bg-white p-8 rounded-lg shadow-lg w-full  sm:w-96" data-aos="fade-up" data-aos-duration="1000">
        <h1 class="text-3xl font-bold mb-6 text-blue-700 mb-6 text-center">Loan Summary Result</h1>
        <div class="space-y-4 mt-5">
            <%-- Check if totalLoanAmount and totalUpdatedLoanAmount are not null --%>
            <% if (request.getAttribute("totalLoanAmount") != null && request.getAttribute("totalUpdatedLoanAmount") != null) { %>
                <div class="flex justify-between">
                    <p class="font-medium">Total Loan Amount:</p>
                    <p class="text-blue-700"><%= request.getAttribute("totalLoanAmount") %></p>
                </div>
                <div class="flex justify-between">
                    <p class="font-medium">Total Updated Loan Amount:</p>
                    <p class="text-blue-700"><%= request.getAttribute("totalUpdatedLoanAmount") %></p>
                </div>
            <% } else { %>
                <p class="text-red-500">No loans found for the specified account.</p>
            <% } %>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/aos@2.3.4/dist/aos.js"></script>
    <script>
        AOS.init();
    </script>
</body>
</html>
