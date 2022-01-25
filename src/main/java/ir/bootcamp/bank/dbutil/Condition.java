package ir.bootcamp.bank.dbutil;

public class Condition {
    private Object value;
    private String column;
    private String operator;

    private Condition(String column, String operator, Object value) {
        this.column = column;
        this.operator = operator;
        this.value = value;
    }

    public static Condition equalsTo(String column, Object value) {
        return new Condition(column, "=", value);
    }

    public static Condition lessThan(String column, Object value) {
        return new Condition(column, "<", value);
    }

    public static Condition greaterThan(String column, Object value) {
        return new Condition(column, ">", value);
    }

    public static Condition lessThanEquals(String column, Object value) {
        return new Condition(column, "<=", value);
    }

    public static Condition greaterThanEquals(String column, Object value) {
        return new Condition(column, ">=", value);
    }

    public Object getValue() {
        return value;
    }

    public String getExpression() {
        return column + " " + operator + " ?";
    }
}
