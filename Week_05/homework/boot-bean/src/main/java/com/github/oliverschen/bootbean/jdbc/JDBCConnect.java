package com.github.oliverschen.bootbean.jdbc;

import java.sql.*;

import static com.github.oliverschen.bootbean.jdbc.SqlConst.*;

/**
 * @author ck
 * JDBC 操作数据库
 */
public class JDBCConnect {

    public static void main(String[] args) {

        try {
            // 注册驱动到驱动管理
            Class.forName(MYSQL_DRIVER_CLASS);
            Connection conn = DriverManager.getConnection(MYSQL_CONNECT_URL, MYSQL_USER, MYSQL_PWD);
            Statement st = conn.createStatement();

            findAll(st);
            System.out.println("##操作之后数据##");
            add(st);
            deleteById(st, 1);
            updateNameById(st, "哈哈", 2);
            findAll(st);

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * 查询所有
     */
    private static void findAll(Statement st) throws SQLException {
        ResultSet rs = st.executeQuery(SQL_FIND_USER);
        while (rs.next()) {
            System.out.println("id:" + rs.getInt("id"));
            System.out.println("name:" + rs.getString("name"));
            System.out.println("birth:" + rs.getString("birth"));
            System.out.println("address:" + rs.getString("address"));
        }
    }

    /**
     * 更新
     */
    private static void updateNameById(Statement st, String name, int id) throws SQLException {
        st.executeUpdate(String.format(SQL_UPDATE_USER, name, id));
        System.out.println("更新完成,id:" + id);
    }

    /**
     * 删除
     */
    private static void deleteById(Statement st, int id) throws SQLException {
        st.execute(SQL_DELETE_USER + id);
        System.out.println("删除完成,id:" + id);
    }

    /**
     * 新增
     */
    private static void add(Statement st) throws SQLException {
        st.execute(SQL_INSERT_USER);
        System.out.println("新增完成");
    }
}
