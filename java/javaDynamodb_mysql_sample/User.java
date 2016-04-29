package camtogetherclean;

public class User {
	
	private int id;
	private String email;
	private String username;
	
	public User() {
		
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	
	@Override
	public int hashCode() {
		return id;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == null) {
			return false;
		}
		
		if(!o.getClass().equals(getClass())) {
			return false;
		}
		
		return this.id == ((User)o).getId();
		
	}
	
	
	@Override
	public String toString() {
		return this.getClass() + ",id:"+id+",username:"+username+",email:"+email;
	}
	

}
