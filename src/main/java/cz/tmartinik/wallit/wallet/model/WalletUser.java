package cz.tmartinik.wallit.wallet.model;

public class WalletUser {
	private String id;
	private String email;
	private String token;

	public WalletUser(String id, String email, String token) {
		this.id = id;
		this.email = email;
		this.setToken(token);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
