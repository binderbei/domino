package com.diva_e;

import com.diva_e.data.DominoDataCreator;
import com.diva_e.orientdb.OrientDbFactory;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
public class DominoApplication {

	@Bean
	WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurerAdapter() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				super.addCorsMappings(registry);
				registry.addMapping("/rest-api/**")
						.allowedMethods("*")
						.allowedOrigins("http://localhost:4200");
			}
		};
	}

	@Bean
	public DominoDataCreator getDominoCreator() {
		return new DominoDataCreator();
	}

	@Bean
	public OrientGraph getOrientDb() {
		return  new OrientDbFactory().createOrientDataBase("memory:domino");
	};

	public static void main(String[] args) {
		SpringApplication.run(DominoApplication.class, args);
	}
}
