package cz.tmartinik.wallit.wallet;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Query;
import cz.tmartinik.wallit.wallet.model.WalletUser;

public interface WalletRestResource {

	@GET("/v1/users")
	List<WalletUser> getUsers(@Query("auth") String auth);

}
