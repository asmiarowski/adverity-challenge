package com.example.adveritychallenge.configuration;

import com.example.adveritychallenge.importer.ImporterProperties;
import org.springframework.boot.autoconfigure.web.servlet.HttpEncodingAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(HttpEncodingAutoConfiguration.class)
@EnableConfigurationProperties({
        ImporterProperties.class
})
public class AdverityChallengeConfiguration {
}
