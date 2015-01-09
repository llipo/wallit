package cz.tmartinik.wallit.config;

import java.security.cert.X509Certificate;
import java.util.Date;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;

import retrofit.RestAdapter;
import retrofit.client.Client;
import retrofit.client.OkClient;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.bind.DateTypeAdapter;
import com.squareup.okhttp.OkHttpClient;

import cz.tmartinik.wallit.Application;
import cz.tmartinik.wallit.wallet.WalletRestResource;

@Configuration
@ComponentScan(basePackageClasses = Application.class, excludeFilters = @Filter({
		Controller.class, Configuration.class }))
class ApplicationConfig {

	@Bean
	public static PropertyPlaceholderConfigurer propertyPlaceholderConfigurer() {
		PropertyPlaceholderConfigurer ppc = new PropertyPlaceholderConfigurer();
		ppc.setLocation(new ClassPathResource("/persistence.properties"));
		return ppc;
	}

	@Bean
	public Gson gson() {
		return new GsonBuilder()
				.setFieldNamingPolicy(
						FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
				.registerTypeAdapter(Date.class, new DateTypeAdapter())
				.create();

	}

	@Bean
	public RestAdapter restAdapter() {
		return new RestAdapter.Builder()
				.setEndpoint("https://www.walletapp.net/api")
				.setClient(client()).build();
	}

	@Bean
	public Client client() {
		return new OkClient(okHttpClient());
	}

	@Bean
	public OkHttpClient okHttpClient() {
		OkHttpClient okHttpClient = new OkHttpClient();
		okHttpClient.setSslSocketFactory(insecureSocketFactory());
		return okHttpClient;
	}

	private SSLSocketFactory insecureSocketFactory() {
		try {
			final TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
				@Override
				public void checkClientTrusted(final X509Certificate[] chain,
						final String authType) {
				}

				@Override
				public void checkServerTrusted(final X509Certificate[] chain,
						final String authType) {
				}

				@Override
				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}
			} };

			// Install the all-trusting trust manager
			final SSLContext sslContext = SSLContext.getInstance("SSL");
			sslContext.init(null, trustAllCerts,
					new java.security.SecureRandom());
			// Create an ssl socket factory with our all-trusting manager
			return sslContext.getSocketFactory();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Bean
	public WalletRestResource walletRestService() {
		return restAdapter().create(WalletRestResource.class);
	}

}