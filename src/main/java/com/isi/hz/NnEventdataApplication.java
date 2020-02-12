package com.isi.hz;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.isi.hz.mapper")
public class NnEventdataApplication {

	public static void main(String[] args) {
		SpringApplication.run(NnEventdataApplication.class, args);
	}

}
