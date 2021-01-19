## 第十三课

（必做）按自己设计的表结构，插入100万订单模拟数据，测试不同方式的插入效率。

执行时先清空表，然后分别执行得出结果

单条插入时间：340s

开启事物单条插入时间：146s

批量插入时间：275s

进过对比手动开启事物之后插入效率最高。

## 第十四课

1.（选做）配置一遍异步复制，半同步复制、组复制。 

### 部署

Docker 部署 Mysql5.7 

#### 配置

```
[mysqld]
## 设置server_id，唯一
server_id=1
## 复制过滤：去掉不尽兴同步的基础库
binlog-ignore-db=mysql
## 开启二进制日志功能，重要步骤
log-bin=replicas-mysql-bin
## 为每个session分配的内存，在事务过程中用来存储二进制日志的缓存
binlog_cache_size=1M
## 主从复制的格式（mixed,statement,row，默认格式是statement）
binlog_format=mixed
## 二进制日志自动删除/过期的天数。默认值为0，表示不自动删除。
expire_logs_days=7
## 跳过主从复制中遇到的所有错误或指定类型的错误，避免slave端复制中断。
## 如：1062错误是指一些主键重复，1032错误是因为主从数据库数据不一致
slave_skip_errors=1062
```

#### 启动

指定不同的配置和端口启动两个容器

> docker run --name mysql-6 -d -p 3306:3306 -e MYSQL_ROOT_PASSWORD=root -v ~/develop/mysql/mysql-6/data:/var/lib/mysql -v ~/develop/mysql/mysql-6/conf/my.cnf:/etc/mysql/my.cnf mysql:5.7



> docker run --name mysql-7 -d -p 3307:3306 -e MYSQL_ROOT_PASSWORD=root -v ~/develop/mysql/mysql-7/data:/var/lib/mysql -v ~/develop/mysql/mysql-7/conf/my.cnf:/etc/mysql/my.cnf mysql:5.7

#### 主节点

连接主节点授权

```
GRANT REPLICATION SLAVE ON *.* to 'sync'@'%' identified by 'root';
# 查看主节点状态
show master status;
```

#### IP

```
docker inspect --format '{{ .NetworkSettings.IPAddress }}' <container-ID> 
```

#### 从节点

连接主节点，指定主节点的地址信息

```
change master to master_host='172.17.0.2',master_user='sync',master_password='root',master_log_file='mysql-bin.000001',master_log_pos=437,master_port=3306;
```

开启从节点

```
start slave;
```

2.（必做）读写分离-动态切换数据源版本1.0

#### 配置

```properties
spring:
  datasource:
    test:
      jdbc-url: jdbc:mysql://127.0.0.1:3306/test?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=UTF-8&useSSL=false
      password: root
      username: root
      driver-class-name: com.mysql.jdbc.Driver
      hikari:
        maximum-pool-size: 200
        minimum-idle: 50
    geek:
      jdbc-url: jdbc:mysql://127.0.0.1:3307/test?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=UTF-8&useSSL=false
      password: root
      username: root
      driver-class-name: com.mysql.jdbc.Driver
      hikari:
        maximum-pool-size: 200
        minimum-idle: 50
```

#### Java配置

##### test数据源

```java
/**
 * @author ck
 */
@Configuration
@MapperScan(basePackages = "com.github.oliverschen.mybatis.mapper.test",
            sqlSessionFactoryRef = "sqlSessionFactoryTest",
            sqlSessionTemplateRef = "sqlSessionTemplateTest")
public class DataSourceConfigTest {
    private static final String MAPPER_PATH = "classpath:mapper/test/*.xml";

    @Primary
    @Bean(name = "dataSourceTest")
    @ConfigurationProperties(prefix = "spring.datasource.test")
    public DataSource datasourceTest() {
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean(name = "sqlSessionFactoryTest")
    public SqlSessionFactory sqlSessionFactoryTest(
            @Qualifier("dataSourceTest") DataSource datasourceTest) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(datasourceTest);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(MAPPER_PATH));
        return bean.getObject();
    }

    @Primary
    @Bean(name = "transactionManagerTest")
    public DataSourceTransactionManager testTransactionManagerTest(
            @Qualifier("dataSourceTest") DataSource dataSourceTest) {
        return new DataSourceTransactionManager(dataSourceTest);
    }

    @Primary
    @Bean(name = "sqlSessionTemplateTest")
    public SqlSessionTemplate sqlSessionTemplateTest(
            @Qualifier("sqlSessionFactoryTest") SqlSessionFactory sqlSessionFactoryTest) {
        return new SqlSessionTemplate(sqlSessionFactoryTest);
    }

}
```

##### geek数据源

```java
/**
 * @author ck
 */
@Configuration
@MapperScan(basePackages = "com.github.oliverschen.mybatis.mapper.geek",
            sqlSessionFactoryRef = "sqlSessionFactoryGeek",
            sqlSessionTemplateRef = "sqlSessionTemplateGeek")
public class DataSourceConfigGeek {
    private static final String MAPPER_PATH = "classpath:mapper/geek/*.xml";

    @Bean(name = "dataSourceGeek")
    @ConfigurationProperties(prefix = "spring.datasource.geek")
    public DataSource datasourceGeek() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "sqlSessionFactoryGeek")
    public SqlSessionFactory sqlSessionFactoryGeek(@Qualifier("dataSourceGeek") DataSource datasourceGeek)
            throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(datasourceGeek);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(MAPPER_PATH));
        return bean.getObject();
    }

    @Bean(name = "transactionManagerGeek")
    public DataSourceTransactionManager testTransactionManager(
            @Qualifier("dataSourceGeek") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "sqlSessionTemplateGeek")
    public SqlSessionTemplate sqlSessionTemplateGeek(@Qualifier("sqlSessionFactoryGeek")
                                                                SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}
```

##### 动态切换

```java

```



3、（必做）读写分离-数据库框架版本2.0

4、（选做）读写分离-数据库中间件版本3.0

5、（选做）配置 MHA，模拟 master 宕机

6、（选做）配置 MGR，模拟 master 宕机

7、（选做）配置 Orchestrator，模拟 master 宕机，演练 UI 调整拓扑结构

