package pl.lodz.p.liceum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;
import pl.lodz.p.liceum.matura.WorkerApplication;

@TestConfiguration(proxyBeanMethods = false)
public class TestWorkerApplication {

	public static void main(String[] args) {
		SpringApplication.from(WorkerApplication::main).with(TestWorkerApplication.class).run(args);
	}

}
