import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        // Dummy database for storing members' credentials
        Map<String, String> memberDatabase = new HashMap<>();

        while (true) {
            System.out.println("\n--- Library Management System ---");
            System.out.println("1. Admin Login");
            System.out.println("2. Member Login");
            System.out.println("3. Member Registration");
            System.out.println("4. Exit");

            int choice = Integer.parseInt(Utils.getInput("Enter choice: "));
            switch (choice) {
                case 1 -> {
                    // Admin login logic
                    String username = Utils.getInput("Enter admin username: ");
                    String password = Utils.getInput("Enter admin password: ");
                    if (username.equals("admin") && password.equals("admin123")) {
                        new Admin().manageBooks();
                    } else {
                        System.out.println("Invalid credentials!");
                    }
                }
                case 2 -> {
                    // Member login logic
                    String username = Utils.getInput("Enter member username: ");
                    String password = Utils.getInput("Enter member password: ");
                    if (memberDatabase.containsKey(username) && memberDatabase.get(username).equals(password)) {
                        System.out.println("Welcome, " + username + "!");
                        
                        // Call the Member page after successful login
                        new Member().manageMembership();  // Call manageMembership method of Member class
                    } else {
                        System.out.println("Invalid credentials!");
                    }
                }
                
                case 3 -> {
                    // Member registration logic
                    String newUsername = Utils.getInput("Enter new member username: ");
                    String newPassword = Utils.getInput("Enter new member password: ");
                    if (memberDatabase.containsKey(newUsername)) {
                        System.out.println("Username already exists! Please choose a different username.");
                    } else {
                        memberDatabase.put(newUsername, newPassword);
                        System.out.println("Member registration successful! You can now log in.");
                    }
                }
                case 4 -> System.exit(0);
                default -> System.out.println("Invalid choice!");
            }
        }
    }
}
