package com.github.erchu.beancp.tutorial;

import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

import com.github.erchu.beancp.Mapper;
import com.github.erchu.beancp.MapperBuilder;

public class _06_Declarative_maps_in_deep_destination_object_construction {

	@Test
	public void test() {
		// GIVEN
		Mapper mapper = new MapperBuilder().addMap(
				Order.class,
				OrderOverviewDto.class,
				(conf, source, destination) -> conf
						.constructDestinationObjectUsing(() -> {
							OrderOverviewDto result = OrderOverviewDtoFactory.getOrderOverviewDto();

							return result;
						})).buildMapper();

		Order order = new Order();

		// WHEN
		OrderOverviewDto destination = mapper
				.map(order, OrderOverviewDto.class);

		// THEN
		assertNotEquals(0, destination.getId());
	}
}
