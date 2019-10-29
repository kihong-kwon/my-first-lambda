package net.kkhstudy.myfirstlambda.dynamodb.query;

import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Sort {

    private static final Sort UNSORTED = Sort.by(new Order[0]);
    private final List<Order> orders;

    public Sort(Order... orders) {
        this.orders = Arrays.asList(orders);
    }

    public static Sort by(Order... orders) {
        return new Sort(orders);
    }

    public static Sort unsorted() {
        return UNSORTED;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public static enum Direction {

        ASC, DESC;

        /**
         * Returns whether the direction is ascending.
         *
         * @return
         * @since 1.13
         */
        public boolean isAscending() {
            return this.equals(ASC);
        }

        /**
         * Returns whether the direction is descending.
         *
         * @return
         * @since 1.13
         */
        public boolean isDescending() {
            return this.equals(DESC);
        }

    }

    public static class Order {

        private static final boolean DEFAULT_IGNORE_CASE = false;

        private final Direction direction;
        private final String property;

        public Order(Direction direction, String property) {
            this.direction = direction;
            this.property = property;
        }
        public static Order asc(String property) {
            return new Order(Direction.ASC, property);
        }

        public static Order desc(String property) {
            return new Order(Direction.DESC, property);
        }

        public Direction getDirection() {
            return direction;
        }

        public String getProperty() {
            return property;
        }
    }
}