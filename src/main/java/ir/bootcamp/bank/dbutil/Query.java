package ir.bootcamp.bank.dbutil;

import javax.print.attribute.standard.RequestingUserName;
import java.util.*;

public class Query {

    private String query;
    private List<Object> values;

    private Query(String query, List<Object> values) {
        this.query = query;
        this.values = values;
    }

    public String getQuery() {
        return query.toString();
    }

    public List<Object> getValues() {
        return values;
    }

    public static class Builder {
        private StringBuilder query = new StringBuilder();
        private List<Object> values = new ArrayList<>();

        public Builder insertInto(String table) {
            query.append("insert into ");
            query.append(table);
            return this;
        }

        public Builder setValues(Object... objects) {
            values.addAll(Arrays.asList(objects));
            query.append(" values (DEFAULT");
            query.append(", ?".repeat(objects.length));
            query.append(")");
            return this;
        }

        public Builder returnColumns(String... columns) {
            query.append(" returning ");
            query.append(String.join(", ", columns));
            return this;
        }

        public Builder select(String... columns) {
            query.append("select ");
            query.append(String.join(",", columns));
            return this;
        }

        public Builder deleteFrom(String tableName) {
            query.append("delete from ");
            query.append(tableName);
            return this;
        }

        public Builder update(String tableName) {
            query.append("update ");
            query.append(tableName);
            return this;
        }

        public Builder set(Map<String, Object> map) {
            values.addAll(map.values());
            query.append(" set ");
            List<String> keys = map.keySet()
                    .stream().map(key -> key + " = ?").toList();
            query.append(String.join(", ", keys));
            return this;
        }

        public Builder from(String... tables) {
            query.append(" from ");
            query.append(String.join(",", tables));
            return this;
        }

        public Builder innerJoin(String table) {
            query.append(" inner join ");
            query.append(table);
            return this;
        }

        public Builder leftJoin(String table) {
            query.append(" left join ");
            query.append(table);
            return this;
        }

        public Builder on(String expression) {
            query.append(" on ");
            query.append(expression);
            return this;
        }

        public Builder where(Condition condition) {
            query.append(" where ");
            query.append(condition.getExpression());
            values.add(condition.getValue());
            return this;
        }

        public Builder and(Condition condition) {
            query.append(" and ");
            query.append(condition.getExpression());
            values.add(condition.getValue());
            return this;
        }

        public Builder or(Condition condition) {
            query.append(" or ");
            query.append(condition.getExpression());
            values.add(condition.getValue());
            return this;
        }

        public Query build() {
            return new Query(query.toString(), values);
        }
    }

}
