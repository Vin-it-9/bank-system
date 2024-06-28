<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Account Balance</title>
    <!-- Include Tailwind CSS -->
    <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet">
</head>
<body class="bg-gray-100">
    <div class="max-w-md mx-auto my-10 bg-white p-8 rounded-lg shadow-lg">
        <h2 class="text-2xl font-bold mb-4 text-blue-500">Account Balance</h2>
        <div class="mb-4">
            <p><strong>Account ID:</strong> <%= request.getAttribute("account_id") %></p>
            <p><strong>Account Name:</strong> <%= request.getAttribute("account_name") %></p>
            <p><strong>Balance:</strong> <%= request.getAttribute("account_balance") %> Rs</p>
        </div>
        <a href="dashboard.jsp" class="text-blue-500 hover:underline">Back to Dashboard</a>
    </div>
</body>
</html>
