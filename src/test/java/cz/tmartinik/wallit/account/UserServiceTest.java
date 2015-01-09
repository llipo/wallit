package cz.tmartinik.wallit.account;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import javax.sql.rowset.WebRowSet;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import cz.tmartinik.wallit.wallet.WalletRestResource;
import cz.tmartinik.wallit.wallet.model.WalletUser;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

	@InjectMocks
	private UserService userService = new UserService();

	@Mock
	private WalletRestResource accountRepositoryMock;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void shouldThrowExceptionWhenUserNotFound() {
		// arrange
		thrown.expect(UsernameNotFoundException.class);
		thrown.expectMessage("user not found");

		when(accountRepositoryMock.getUsers("123456789")).thenReturn(null);
		// act
		userService.loadUserByUsername("123456789");
	}

	@Test
	public void shouldReturnUserDetails() {
		// arrange
		WalletUser demoUser = new WalletUser("user@example.com", "demo", "ROLE_USER");
		when(accountRepositoryMock.getUsers("123456789")).thenReturn(Arrays.asList(demoUser));

		// act
		UserDetails userDetails = userService.loadUserByUsername("123456789");

		// assert
		assertThat(demoUser.getEmail()).isEqualTo(userDetails.getUsername());
        assertThat(hasAuthority(userDetails, "ROLE_USER"));
	}

	private boolean hasAuthority(UserDetails userDetails, String role) {
		Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
		for(GrantedAuthority authority : authorities) {
			if(authority.getAuthority().equals(role)) {
				return true;
			}
		}
		return false;
	}
}
