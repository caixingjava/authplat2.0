package com.minivision.authplat2.actuator;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.springframework.boot.actuate.endpoint.PublicMetrics;
import org.springframework.boot.actuate.metrics.Metric;
import org.springframework.data.repository.CrudRepository;

import com.minivision.authplat2.util.ClassUtils;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * DB表数据统计器
 * @author hughzhao
 * @2018年3月2日
 */
@Setter
@Getter
@NoArgsConstructor
@RequiredArgsConstructor
public class DBMetrics implements PublicMetrics {

	@NonNull
	private Collection<CrudRepository<?, ?>> repositories;

	@Override
	public Collection<Metric<?>> metrics() {
		List<Metric<?>> metrics = new LinkedList<>();
		for (CrudRepository<?, ?> repository: repositories) {
			String name = ClassUtils.getRepositoryName(repository.getClass());
			String metricName = "counter.datasource." + name;
			metrics.add(new Metric<Long>(metricName, repository.count()));
		}
		return metrics;
	}

}
