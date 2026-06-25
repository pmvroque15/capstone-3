package org.yearup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.yearup.models.OrderLineItems;

public interface OrderLineItemsRepository extends JpaRepository<OrderLineItems, Integer> {
}
