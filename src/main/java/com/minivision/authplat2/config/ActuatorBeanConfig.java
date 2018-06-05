package com.minivision.authplat2.config;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.PublicMetrics;
import org.springframework.boot.actuate.health.CompositeHealthIndicator;
import org.springframework.boot.actuate.health.HealthAggregator;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.repository.CrudRepository;

import com.minivision.authplat2.actuator.DBHealthIndicator;
import com.minivision.authplat2.actuator.DBMetrics;
import com.minivision.authplat2.util.ClassUtils;

@Configuration
public class ActuatorBeanConfig {

	@Autowired
	private HealthAggregator healthAggregator;

	@Bean
	public HealthIndicator dbHealthIndicator(Collection<CrudRepository<?, ?>> repositories) {
		CompositeHealthIndicator compositeHealthIndicator = new
				CompositeHealthIndicator(healthAggregator);
		for (CrudRepository<?, ?> repository: repositories) {
			String name = ClassUtils.getRepositoryName(repository.getClass());
			compositeHealthIndicator.addHealthIndicator(name, new DBHealthIndicator(repository));
		}
		return compositeHealthIndicator;
	}

	@Bean
	public PublicMetrics dbMetrics(Collection<CrudRepository<?, ?>> repositories) {
		return new DBMetrics(repositories);
	}

}
