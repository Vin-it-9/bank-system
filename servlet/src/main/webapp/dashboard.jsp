<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>User Dashboard</title>
    <!-- Include Tailwind CSS -->
    <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet">
</head>
<body class="bg-gray-100 ">


<div class="sticky top-0 flex justify-between items-center bg-white p-4 shadow-md">
    <div class="flex-grow">
        <h3 class="text-lg font-semibold text-gray-800">
            Welcome, <%= session.getAttribute("username") %>
        </h3>
    </div>
    <div class="flex space-x-4">
        <button id="toggleButton" class="bg-blue-500 text-white py-2 px-4 rounded-md hover:bg-blue-600 transition duration-300">Deposit</button>
        <button id="toggleCreateAccount" class="bg-blue-500 hover:bg-blue-600 text-white py-2 px-4 rounded-md transition duration-300">Create Account</button>
        <button onclick="showChangePasswordForm()" class="bg-blue-500 hover:bg-blue-600 text-white py-2 px-4 rounded-md transition duration-300">Change Password</button>
        <form id="logoutForm" action="logout" method="POST">
            <button type="submit" class="bg-blue-500 hover:bg-blue-600 text-white py-2 px-4 rounded-md transition duration-300">Logout</button>
        </form>
    </div>
</div>

<div class="container mx-auto px-5 py-6 ">
    <h1 class="text-2xl font-semibold text-gray-800 mb-4">User Dashboard</h1>

    <% if (request.getParameter("message") != null) { %>
        <% if (request.getParameter("message").equals("accountCreated")) { %>
            <div class="bg-green-100 border border-green-400 text-green-700 px-4 py-3 rounded relative alert" role="alert">
                <span class="block sm:inline">Account created successfully.</span>
            </div>
        <% } %>
    <% } %>

    <% if (request.getParameter("error") != null) { %>
        <% if (request.getParameter("error").equals("userNotFound")) { %>
            <div class="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded relative alert" role="alert">
                <span class="block sm:inline">User not found.</span>
            </div>
        <% } else if (request.getParameter("error").equals("nomatch")) { %>
            <div class="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded relative alert" role="alert">
                <span class="block sm:inline">Passwords do not match.</span>
            </div>
        <% } else if (request.getParameter("error").equals("incorrect")) { %>
            <div class="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded relative alert" role="alert">
                <span class="block sm:inline">Current password is incorrect.</span>
            </div>
        <% } else if (request.getParameter("error").equals("failed")) { %>
            <div class="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded relative alert" role="alert">
                <span class="block sm:inline">Failed to create.</span>
            </div>
        <% } %>
    <% } %>

<div class="bg-white p-4 rounded-lg shadow-md mb-4">
            <h3 class="text-lg font-semibold text-gray-800">
                Welcome, <%= session.getAttribute("username") %>
            </h3>
            <p>Email: <%= session.getAttribute("email") %></p>
            <p>Member Since: <%= session.getAttribute("memberSince") %></p>
        </div>

        <div class="bg-white p-4 rounded-lg shadow-md mb-4">
            <a href="withdrow.html" class="inline-block bg-blue-600 hover:bg-blue-500 text-white py-2 px-4 rounded-md transition duration-300">Withdraw Money</a>
             <a href="transfer.html" class="inline-block bg-blue-600 hover:bg-blue-500 text-white py-2 px-4 rounded-md transition duration-300">Transfer Money</a>
             <a href="check_balance.html" class="inline-block bg-blue-600 hover:bg-blue-500 text-white py-2 px-4 rounded-md transition duration-300">Check Balance</a>
             <a href="loan_form.html" class="inline-block bg-blue-600 hover:bg-blue-500 text-white py-2 px-4 rounded-md transition duration-300">Apply For Loan</a>
             <a href="repayment_form.html" class="inline-block bg-blue-600 hover:bg-blue-500 text-white py-2 px-4 rounded-md transition duration-300">Fill Your Loan</a>
              <a href="loan_form.jsp" class="inline-block bg-blue-600 hover:bg-blue-500 text-white py-2 px-4 rounded-md transition duration-300">Check Your Loan</a>

        </div>

    <div class="flex-grow p-6 overflow-hidden">
        <div class="overflow-hidden">
            <!-- Create Account Form -->
            <div id="createAccountSection" class="hidden bg-white p-8 rounded-lg shadow-lg  w-full sm:w-96 mx-auto max-w-md mb-7" data-aos="fade-up" data-aos-duration="1000">
                <h2 class="text-3xl font-bold mb-4 text-blue-700 text-center">Create New Account</h2>
                <form action="createAccount" method="POST" class="space-y-4">
                    <div class="form-group">
                        <label for="accountName" class="block font-medium">Account Name</label>
                        <input type="text" id="accountName" name="accountName" class="w-full px-3 py-2 border rounded-md focus:outline-none focus:border-blue-500" placeholder="Enter your account name" required>
                    </div>
                    <div class="form-group">
                        <label for="password" class="block font-medium">Password</label>
                        <input type="password" id="password" name="password" class="w-full px-3 py-2 border rounded-md focus:outline-none focus:border-blue-500" placeholder="Enter your password" required>
                    </div>
                    <div class="form-group">
                        <button type="submit" class="w-full bg-blue-500 text-white py-2 px-4 rounded-md hover:bg-blue-600 transition duration-300">Create Account</button>
                    </div>
                </form>
            </div>

            <!-- Change Password Form -->
            <div id="changePasswordForm" class="hidden bg-white p-8  rounded-lg shadow-lg w-full sm:w-96 mx-auto max-w-md mb-7">
                <h2 class="text-3xl font-bold mb-4 text-blue-700 text-center">Change User Password</h2>
                <form action="changePassword" method="POST" class="space-y-4">
                    <div class="form-group">
                        <label for="currentPassword" class="block font-medium">Current Password</label>
                        <input type="password" id="currentPassword" name="currentPassword" class="w-full px-3 py-2 border rounded-md focus:outline-none focus:border-blue-500" placeholder="Enter your current password" required>
                    </div>
                    <div class="form-group">
                        <label for="newPassword" class="block font-medium">New Password</label>
                        <input type="password" id="newPassword" name="newPassword" class="w-full px-3 py-2 border rounded-md focus:outline-none focus:border-blue-500" placeholder="Enter your new password" required>
                    </div>
                    <div class="form-group">
                        <label for="confirmPassword" class="block font-medium">Confirm New Password</label>
                        <input type="password" id="confirmPassword" name="confirmPassword" class="w-full px-3 py-2 border rounded-md focus:outline-none focus:border-blue-500" placeholder="Confirm your new password" required>
                    </div>
                    <div class="form-group">
                        <button type="submit" class="w-full bg-blue-500 text-white py-2 px-4 rounded-md hover:bg-blue-600 transition duration-300">Change Password</button>
                        <button type="button" onclick="hideChangePasswordForm()" class="w-full bg-gray-500 text-white py-2 px-4 rounded-md hover:bg-gray-600 transition duration-300 mt-2">Cancel</button>
                    </div>
                </form>
            </div>

             <!-- Deposit Money Form -->
                                   <div id="formContainer" class="hidden bg-white p-8 rounded-lg shadow-lg w-full sm:w-96 mx-auto max-w-md mb-7" data-aos="fade-up" data-aos-duration="1000">
                                       <h2 class="text-3xl font-bold mb-4 text-blue-700 text-center">Deposit Money</h2>
                                       <form action="depositMoney" method="POST" class="space-y-4">
                                           <div class="form-group">
                                               <label for="account_id" class="block font-medium">Account ID</label>
                                               <input type="text" id="account_id" name="account_id" class="w-full px-3 py-2 border rounded-md focus:outline-none focus:border-blue-500" placeholder="Enter your account ID" required>
                                           </div>
                                           <div class="form-group">
                                               <label for="account_name" class="block font-medium">Account Name</label>
                                               <input type="text" id="account_name" name="account_name" class="w-full px-3 py-2 border rounded-md focus:outline-none focus:border-blue-500" placeholder="Enter your account name" required>
                                           </div>
                                           <div class="form-group">
                                               <label for="amount" class="block font-medium">Amount</label>
                                               <input type="text" id="amount" name="amount" class="w-full px-3 py-2 border rounded-md focus:outline-none focus:border-blue-500" placeholder="Enter the amount" required>
                                           </div>
                                           <div class="form-group">
                                               <button type="submit" class="w-full bg-blue-500 text-white py-2 px-4 rounded-md hover:bg-blue-600 transition duration-300">Deposit</button>
                                           </div>
                                       </form>
                                   </div>
        </div>
    </div>










</div>

<script>
    document.addEventListener('DOMContentLoaded', function() {
        document.getElementById('toggleButton').addEventListener('click', function() {
            var formContainer = document.getElementById('formContainer');
            if (formContainer.classList.contains('hidden')) {
                formContainer.classList.remove('hidden');
            } else {
                formContainer.classList.add('hidden');
            }
        });

        document.getElementById('toggleCreateAccount').addEventListener('click', function() {
            var createAccountSection = document.getElementById('createAccountSection');
            if (createAccountSection.classList.contains('hidden')) {
                createAccountSection.classList.remove('hidden');
            } else {
                createAccountSection.classList.add('hidden');
            }
        });
    });

    function showChangePasswordForm() {
        document.getElementById('changePasswordForm').classList.remove('hidden');
    }

    function hideChangePasswordForm() {
        document.getElementById('changePasswordForm').classList.add('hidden');
    }

    function logout() {
        fetch('/logout', { method: 'POST' })
            .then(response => {
                if (response.redirected) {
                    window.location.href = response.url; // Redirect to login page
                }
            })
            .catch(error => console.error('Error during logout:', error));
    }
</script>

</body>
</html>
