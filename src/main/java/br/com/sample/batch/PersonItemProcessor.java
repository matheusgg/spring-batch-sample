package br.com.sample.batch;

import br.com.sample.model.Person;
import br.com.sample.model.Registry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

@Slf4j
public class PersonItemProcessor implements ItemProcessor<Person, Registry> {

	@Override
	public Registry process(final Person item) throws Exception {
		log.info("{} - Processing item: {}", Thread.currentThread().getName(), item);
		return new Registry(null, item.toString());
	}
}
