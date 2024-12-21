import java.sql.*;

public class Member {

    // Main function to manage member actions
    public void manageMembership() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            while (true) {
                System.out.println("\n--- Member Menu ---");
                System.out.println("1. View Available Books");
                System.out.println("2. Borrow a Book");
                System.out.println("3. Return a Book");
                System.out.println("4. Exit");

                int choice = Integer.parseInt(Utils.getInput("Enter choice: "));
                switch (choice) {
                    case 1 -> viewAvailableBooks(connection);  // View all available books
                    case 2 -> borrowBook(connection);          // Borrow a book
                    case 3 -> returnBook(connection);          // Return a borrowed book
                    case 4 -> {
                        System.out.println("Exiting Member Menu...");
                        return; // Exit to the previous menu
                    }
                    default -> System.out.println("Invalid choice! Please select again.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // View all available books
    private void viewAvailableBooks(Connection connection) throws SQLException {
        String query = "SELECT * FROM books WHERE available = TRUE";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            System.out.println("\nAvailable Books:");
            boolean booksFound = false;
            while (resultSet.next()) {
                booksFound = true;
                System.out.printf("ID: %d | Title: %s | Author: %s%n",
                        resultSet.getInt("id"),
                        resultSet.getString("title"),
                        resultSet.getString("author"));
            }
            if (!booksFound) {
                System.out.println("No available books at the moment.");
            }
        }
    }

    // Borrow a book (mark it as unavailable)
    private void borrowBook(Connection connection) throws SQLException {
        int bookId = Integer.parseInt(Utils.getInput("Enter the book ID to borrow: "));
        String checkQuery = "SELECT available FROM books WHERE id = ?";
        String updateQuery = "UPDATE books SET available = FALSE WHERE id = ?";

        try (PreparedStatement checkStatement = connection.prepareStatement(checkQuery);
             PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
            checkStatement.setInt(1, bookId);
            try (ResultSet resultSet = checkStatement.executeQuery()) {
                if (resultSet.next() && resultSet.getBoolean("available")) {
                    updateStatement.setInt(1, bookId);
                    updateStatement.executeUpdate();
                    System.out.println("Book borrowed successfully!");
                } else {
                    System.out.println("Book is not available or already borrowed.");
                }
            }
        }
    }

    // Return a book (mark it as available again)
    private void returnBook(Connection connection) throws SQLException {
        int bookId = Integer.parseInt(Utils.getInput("Enter the book ID to return: "));
        String checkQuery = "SELECT available FROM books WHERE id = ?";
        String updateQuery = "UPDATE books SET available = TRUE WHERE id = ?";

        try (PreparedStatement checkStatement = connection.prepareStatement(checkQuery);
             PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
            checkStatement.setInt(1, bookId);
            try (ResultSet resultSet = checkStatement.executeQuery()) {
                if (resultSet.next() && !resultSet.getBoolean("available")) {
                    updateStatement.setInt(1, bookId);
                    updateStatement.executeUpdate();
                    System.out.println("Book returned successfully!");
                } else {
                    System.out.println("Book was not borrowed or already returned.");
                }
            }
        }
    }
}
