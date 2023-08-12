package clinic.sql;


public enum SQLQueries {

    DENTIST(
            "DROP TABLE IF EXISTS dentist; CREATE TABLE dentist(id BIGINT PRIMARY KEY, name VARCHAR(255), surname VARCHAR(255), license_num INT);",
            "INSERT INTO dentist VALUES(?,?,?,?);",
            "SELECT * FROM dentist"
    ),
    PATIENT(
            "DROP TABLE IF EXISTS patient; CREATE TABLE patient(id BIGINT PRIMARY KEY, name VARCHAR(255), surname VARCHAR(255), address VARCHAR(255), ni_num INT, reg_date VARCHAR(255));",
            "INSERT INTO patient VALUES(?,?,?,?,?,?);",
            "SELECT * FROM patient"
    );

    // ----    Statement Interface   ----
    // DDL - Data Definition Language
    // CREATE - DROP - ALTER
    private final String createTable;

    // ----    PreparedStatement Interface    ----
    // DML - Data Manipulation Language
    // SELECT - INSERT - UPDATE - DELETE
    private final String insertCustom;
    private final String selectAll;

    SQLQueries(String createTable, String insertCustom, String selectAll) {
        this.createTable = createTable;
        this.insertCustom = insertCustom;
        this.selectAll = selectAll;
    }

    public String getCreateTable() {
        return createTable;
    }

    public String getSelectAll() {
        return selectAll;
    }

    public String getInsertCustom() {
        return insertCustom;
    }

}
