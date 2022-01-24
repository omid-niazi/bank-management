package ir.bootcamp.bank.repositories;

public class Const {
    public static final String CUSTOMER_TABLE_NAME = "customer";
    public static final String CUSTOMER_COLUMN_ID = "customer_id";
    public static final String CUSTOMER_COLUMN_NAME = "customer_name";
    public static final String CUSTOMER_COLUMN_NATIONAL_CODE = "customer_national_code";
    public static final String CUSTOMER_COLUMN_PHONE = "customer_phone";

    public static final String ACCOUNT_TABLE_NAME = "account";
    public static final String ACCOUNT_COLUMN_ID = "account_id";
    public static final String ACCOUNT_COLUMN_NUMBER = "account_number";
    public static final String ACCOUNT_COLUMN_AMOUNT = "account_amount";
    public static final String ACCOUNT_COLUMN_CUSTOMER_ID = "account_customer_id";

    public static final String BRANCH_TABLE_NAME = "branch";
    public static final String BRANCH_COLUMN_ID = "branch_id";
    public static final String BRANCH_COLUMN_NAME = "branch_name";
    public static final String BRANCH_COLUMN_ADDRESS = "branch_address";

    public static final String EMPLOYEE_TABLE_NAME = "employee";
    public static final String EMPLOYEE_COLUMN_ID = "employee_id";
    public static final String EMPLOYEE_COLUMN_NAME = "employee_name";
    public static final String EMPLOYEE_COLUMN_PASSWORD = "employee_password";
    public static final String EMPLOYEE_COLUMN_MANAGER_ID = "employee_manager_id";
    public static final String EMPLOYEE_COLUMN_BRANCH_ID = "employee_branch_id";

    public static final String CARD_TABLE_NAME = "card";
    public static final String CARD_COLUMN_ID = "card_id";
    public static final String CARD_COLUMN_NUMBER = "card_number";
    public static final String CARD_COLUMN_CVV2 = "card_cvv2";
    public static final String CARD_COLUMN_EXPIRE_DATE = "card_expire_date";
    public static final String CARD_COLUMN_PASSWORD = "card_password";
    public static final String CARD_COLUMN_ACCOUNT_ID = "card_account_id";
    public static final String CARD_COLUMN_FAILED_ATTEMPT = "card_failed_attempt";
    public static final String CARD_COLUMN_STATUS = "card_status";

    public static final String BRANCH_MANAGER_TABLE_NAME = "branch_manager";
    public static final String BRANCH_MANAGER_COLUMN_ID = "branch_manager_id";
    public static final String BRANCH_MANAGER_COLUMN_BRANCH_ID = "branch_manager_branch_id";
    public static final String BRANCH_MANAGER_COLUMN_MANAGER_ID = "branch_manager_employee_id";

    public static final String TRANSACTION_TABLE_NAME = "transaction_table_name";
    public static final String TRANSACTION_COLUMN_ID = "transaction_column_id";
    public static final String TRANSACTION_COLUMN_FROM_CARD_ID= "transaction_column_from_card";
    public static final String TRANSACTION_COLUMN_TO_CARD_ID = "transaction_column_to_card";
    public static final String TRANSACTION_COLUMN_AMOUNT= "transaction_column_amount";
    public static final String TRANSACTION_COLUMN_IS_OUT_GOING = "transaction_column_is_out_going";
}
