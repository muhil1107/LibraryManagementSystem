import java.sql.*;

public class Admin {
    public void manageBooks() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            while (true) {
                System.out.println("\n--- Admin Menu ---");
                System.out.println("1. Add Book");
                System.out.println("2. Delete Book");
                System.out.println("3. Update Book");
                System.out.println("4. View Books");
                System.out.println("5. Exit");

                int choice = Integer.parseInt(Utils.getInput("Enter choice: "));
                switch (choice) {
                    case 1 -> addBook(connection);
                    case 2 -> deleteBook(connection);
                    case 3 -> updateBook(connection);
                    case 4 -> viewBooks(connection);
                    case 5 -> {
                        return;
                    }
                    default -> System.out.println("Invalid choice!");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void addBook(Connection connection) throws SQLException {
        String title = Utils.getInput("Enter book title: ");
        String author = Utils.getInput("Enter book author: ");
        String query = "INSERT INTO books (title, author) VALUES (?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, title);
            statement.setString(2, author);
            statement.executeUpdate();
            System.out.println("Book added successfully!");
        }
    }

    private void deleteBook(Connection connection) throws SQLException {
        int bookId = Integer.parseInt(Utils.getInput("Enter book ID to delete: "));
        String query = "DELETE FROM books WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, bookId);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Book deleted successfully!");
            } else {
                System.out.println("Book not found!");
            }
        }
    }

    private void updateBook(Connection connection) throws SQLException {
        int bookId = Integer.parseInt(Utils.getInput("Enter book ID to update: "));
        String newTitle = Utils.getInput("Enter new title: ");
        String newAuthor = Utils.getInput("Enter new author: ");
        String query = "UPDATE books SET title = ?, author = ? WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, newTitle);
            statement.setString(2, newAuthor);
            statement.setInt(3, bookId);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Book updated successfully!");
            } else {
                System.out.println("Book not found!");
            }
        }
    }

    private void viewBooks(Connection connection) throws SQLException {
        String query = "SELECT * FROM books";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                System.out.printf("ID: %d | Title: %s | Author: %s | Available: %s%n",
                        resultSet.getInt("id"),
                        resultSet.getString("title"),
                        resultSet.getString("author"),
                        resultSet.getBoolean("available") ? "Yes" : "No");
            }
        }
    }
}
