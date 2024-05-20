package kr.pah.pcs.pcscoin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class PcsCoinApplication {

    public static void main(String[] args) {
        SpringApplication.run(PcsCoinApplication.class, args);
    }

}
