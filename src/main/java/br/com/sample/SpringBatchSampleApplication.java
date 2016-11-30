package br.com.sample;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class SpringBatchSampleApplication implements CommandLineRunner {

	private final Job registryJob;
	private final JobLauncher jobLauncher;

	public SpringBatchSampleApplication(final Job registryJob, final JobLauncher jobLauncher) {
		this.registryJob = registryJob;
		this.jobLauncher = jobLauncher;
	}

	public static void main(final String[] args) {
		SpringApplication.run(SpringBatchSampleApplication.class, args);
	}

	@Override
	public void run(final String... args) throws Exception {
		final JobExecution jobExecution = this.jobLauncher.run(this.registryJob, new JobParameters());
		log.info("Status: {}", jobExecution.getStatus());
		while (jobExecution.isRunning()) {
			log.info("Status: {}", jobExecution.getStatus());
		}
		log.info("Status: {}", jobExecution.getStatus());
	}
}
