<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Loan Summary</title>
    <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/aos@2.3.4/dist/aos.css" rel="stylesheet">
</head>
<body class="bg-gray-100 flex items-center justify-center h-screen">
    <div class="bg-white p-8 rounded-lg shadow-lg w-full sm:w-96" data-aos="fade-up" data-aos-duration="1000">
        <h2 class="text-3xl font-bold mb-6 text-blue-700 text-center">Loan Summary</h2>
        <form action="LoanSummaryServlet" method="post" class="space-y-4">
            <div class="form-group">
                <label for="account_id" class="block font-medium">Account ID:</label>
                <input type="text" id="account_id" name="account_id" placeholder="Enter your account ID" class="w-full px-3 py-2 border rounded-md focus:outline-none focus:border-blue-500" required>
            </div>
            <div class="form-group">
                <label for="password" class="block font-medium">Password:</label>
                <input type="password" id="password" name="password" placeholder="Enter your password" class="w-full px-3 py-2 border rounded-md focus:outline-none focus:border-blue-500" required>
            </div>
            <button type="submit" class="w-full bg-blue-500 text-white py-2 px-4 rounded-md hover:bg-blue-600 transition duration-300">Submit</button>
        </form>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/aos@2.3.4/dist/aos.js"></script>
    <script>
        AOS.init();
    </script>
</body>
</html>
