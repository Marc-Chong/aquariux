package com.Marc.Test;

import com.Marc.Test.repository.BalanceRepository;
import com.Marc.Test.repository.entity.Balance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@SpringBootApplication
@Service
@EnableScheduling
public class TestApplication {
	@Autowired
	Controller controller;
	public static void main(String[] args) {
		SpringApplication.run(TestApplication.class, args);
	}

	@Autowired
	BalanceRepository balanceRepository;
	@EventListener(ApplicationReadyEvent.class)
	public void initialize()
	{
		balanceRepository.save(new Balance("USDT", 50000.0));
	}

	@Scheduled(fixedRate = 10000)
	public void runEvery10Seconds()
	{
		controller.refreshPrice();
	}
}
