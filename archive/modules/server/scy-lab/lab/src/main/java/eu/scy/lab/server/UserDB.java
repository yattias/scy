/**
 * 
 */
package eu.scy.lab.server;

import java.util.Vector;

/**
 * @author Giemza
 *
 */
public class UserDB {
    
    private static Vector<User> users = new Vector<User>();
    
    public static boolean addUser(String birthdate, String email, String firstname, String lastname, String password, String title, String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return false;
            }
        }
        return users.add(new User(birthdate, email, firstname, lastname, password, title, username));
    }
    
    public static boolean authenticateUser(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }
    
    public static class User {
        
        private String username;
        private String password;
        private String firstname;
        private String lastname;
        private String email;
        private String title;
        private String birthdate;
        
        /**
         * @param birthdate
         * @param email
         * @param firstname
         * @param lastname
         * @param password
         * @param title
         * @param username
         */
        public User(String birthdate, String email, String firstname, String lastname, String password, String title, String username) {
            this.birthdate = birthdate;
            this.email = email;
            this.firstname = firstname;
            this.lastname = lastname;
            this.password = password;
            this.title = title;
            this.username = username;
        }
        
        /**
         * @return the username
         */
        public String getUsername() {
            return username;
        }
        
        /**
         * @return the password
         */
        public String getPassword() {
            return password;
        }
        
        /**
         * @return the firstname
         */
        public String getFirstname() {
            return firstname;
        }
        
        
        /**
         * @return the lastname
         */
        public String getLastname() {
            return lastname;
        }
        
        /**
         * @return the email
         */
        public String getEmail() {
            return email;
        }
        
        /**
         * @return the title
         */
        public String getTitle() {
            return title;
        }
        
        /**
         * @return the birthdate
         */
        public String getBirthdate() {
            return birthdate;
        }
    }
}
