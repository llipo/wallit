package cz.tmartinik.wallit.account;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import cz.tmartinik.wallit.wallet.WalletRestResource;
import cz.tmartinik.wallit.wallet.model.WalletUser;

public class UserService implements UserDetailsService {
	
	@Autowired
	private WalletRestResource walletRestResource;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		List<WalletUser> account = walletRestResource.getUsers(username);
		if(account == null || account.isEmpty() || account.size() > 1) {
			throw new UsernameNotFoundException("user not found");
		}
		return createUser(account.get(0));
	}
	
	public void signin(WalletUser account) {
		SecurityContextHolder.getContext().setAuthentication(authenticate(account));
	}
	
	private Authentication authenticate(WalletUser account) {
		return new UsernamePasswordAuthenticationToken(createUser(account), null, Collections.singleton(createAuthority(account)));		
	}
	
	private User createUser(WalletUser account) {
		return new User(account.getEmail(), account.getEmail(), Collections.singleton(createAuthority(account)));
	}

	private GrantedAuthority createAuthority(WalletUser account) {
		return new SimpleGrantedAuthority("ROLE_USER");
	}

}
