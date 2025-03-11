package bg.softuni.barberstudio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class BarberStudioApplication {

	public static void main(String[] args) {
		SpringApplication.run(BarberStudioApplication.class, args);
	}

}
