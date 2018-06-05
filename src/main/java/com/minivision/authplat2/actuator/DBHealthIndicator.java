package com.minivision.authplat2.actuator;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.data.repository.CrudRepository;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * DB健康检查器
 * @author hughzhao
 * @2018年3月2日
 */
@Setter
@Getter
@NoArgsConstructor
@RequiredArgsConstructor
public class DBHealthIndicator implements HealthIndicator {

	@NonNull
	private CrudRepository<?, ?> crudRepository;

	@Override
	public Health health() {
		try {
			long count = crudRepository.count();
			if (count >= 0) {
				return Health.up().withDetail("count", count).build();
			} else {
				return Health.unknown().withDetail("count", count).build();
			}
		} catch (Exception e) {
			return Health.down(e).build();
		}
	}

}
