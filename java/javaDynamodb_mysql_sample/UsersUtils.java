package camtogetherclean;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author abraham
 *
 *this class is fetching users from mysql rds
 */
public class UsersUtils {
	
	private static Connection con;
	private static Statement statement;
	private static ResultSet resultSet;
	
	public UsersUtils()  {
		
	}
	
	
	public static List<User> getAllUsers() {
		
	
		try {
			
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost/mysqldatabase?user=root&password=password");
		
			List<User> users = new ArrayList<User>();
			
			statement = con.createStatement();
			
			resultSet = statement.executeQuery("SELECT * FROM users");
			
			while(resultSet.next()) {
				
				User user = new User();
				
				String username = resultSet.getString(1);
				String email = resultSet.getString(2);
				int id = resultSet.getInt(3);
				
				user.setId(id);
				user.setEmail(email);
				user.setUsername(username);
				
				users.add(user);
				
			}
			
			return users;
			
		
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			
			try {
				
			if(resultSet != null) {
					resultSet.close();
			}
			
			if(statement != null) {
				statement.close();
			}
			
			if(con != null) {
				con.close();
			}
			
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return null;
	}
	
	
	/**
	 * 
	 * @return fake/dummy arraylist with users
	 * representing the mysql users...some of them ,maybe non of them
	 * 
	 * for test!
	 */
	public static List<User> getAllUsersDummy() {
		List<User> users = new ArrayList<User>();
		
		User user = new User();
		user.setEmail("piperidis@gmail.com");
		users.add(user);
		
		user = new User();
		user.setEmail("dummyemail@gmail.com");
		users.add(user);
		
		user = new User();
		user.setEmail("mockemail@gmail.com");
		users.add(user);
		
		user = new User();
		user.setEmail("justmockdummy.email@gmail.com");
		users.add(user);
		
		return users;
	}
	
	
	
	
	

}
