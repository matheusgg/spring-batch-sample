package br.com.sample.batch;

import br.com.sample.model.Person;
import br.com.sample.model.Registry;
import br.com.sample.model.repository.PersonRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

import javax.persistence.EntityManagerFactory;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

	private final EntityManagerFactory emf;
	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;

	public BatchConfiguration(final EntityManagerFactory emf, final JobBuilderFactory jobBuilderFactory, final StepBuilderFactory stepBuilderFactory) {
		this.emf = emf;
		this.jobBuilderFactory = jobBuilderFactory;
		this.stepBuilderFactory = stepBuilderFactory;
	}

	@Bean
	public ItemReader<Person> personItemReader() {
		final JpaPagingItemReader<Person> reader = new JpaPagingItemReader<>();
		reader.setEntityManagerFactory(this.emf);
		reader.setQueryString("select p from Person p");
		reader.setPageSize(10);
		return reader;
	}

	@Bean
	@StepScope
	public ItemProcessor<Person, Registry> personPersonItemProcessor() {
		return new PersonItemProcessor();
	}

	@Bean
	public ItemWriter<Registry> registryItemWriter() {
		final JpaItemWriter<Registry> writer = new JpaItemWriter<>();
		writer.setEntityManagerFactory(this.emf);
		return writer;
	}

	@Bean
	public Step step1() {
		return this.stepBuilderFactory.get("createRegistryStep")
				.<Person, Registry>chunk(10)
				.reader(this.personItemReader())
				.processor(this.personPersonItemProcessor())
				.writer(this.registryItemWriter())
				.taskExecutor(new SimpleAsyncTaskExecutor())
				.throttleLimit(5)
				.build();
	}

	@Bean
	public Job registryJob(final PersonRepository repository) {
		return this.jobBuilderFactory.get("registryJob")
				.preventRestart()
				.incrementer(new RunIdIncrementer())
				.flow(this.step1())
				.end()
				.build();

	}
}
