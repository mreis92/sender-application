package no.mnemonic.senderapplication.configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;

@Configuration
@EnableRetry
public class AppConfig {

    @Bean
    public Gson gson() {
        return new GsonBuilder().create();
    }
}
