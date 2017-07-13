package com.diva_e;

import com.diva_e.data.DominoDataCreator;
import com.diva_e.orientdb.OrientDbFactory;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DominoApplication {

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
