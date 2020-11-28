## 数据库启动命令
```
sudo /usr/local/mysql/support-files/mysql.server start
```
## 连接数据库命令
```
/usr/local/mysql/bin/mysql -u root -p
```
## flyway数据库版本管理工具

环境:JDK6.0

支持的数据库：Oracle, SQL Server, SQL Azure, DB2, DB2 z/OS, MySQL (including Amazon RDS), MariaDB, Google Cloud SQL, PostgreSQL (including Amazon RDS and Heroku), Redshift, Vertica, H2, Hsql, Derby, SQLite, SAP HANA, solidDB, Sybase ASE and Phoenix

#### flyway配置
```
 <dependency>
            <groupId>org.flywaydb</groupId>
            <artifactId>flyway-core</artifactId>
 </dependency>
```
```
 <plugin>
                <groupId>org.flywaydb</groupId>
                <artifactId>flyway-maven-plugin</artifactId>
                <version>6.5.5</version>
                <configuration>
                    <url>jdbc:mysql://localhost:3306/text</url>
                    <user>root</user>
                    <password>root</password>
                </configuration>
                <dependencies>
                    <dependency>
                        <groupId>mysql</groupId>
                        <artifactId>mysql-connector-java</artifactId>
                        <version>8.0.21</version>
                    </dependency>
                </dependencies>
 </plugin>
```

#### 文件命名规范
V+版本号(版本号的数字间以"."或"_"分隔开)+双下划线(用来分隔版本号和描述)+文件描述+后缀名，例如：V2020.10.12__Update.sql
#### flyway命令
-  Clean： 删除所有创建的数据库对象，包括用户、表、视图等
-  Migrate： 对数据库依次应用版本更改。
-  Info：获取目前数据库的状态。那些迁移已经完成，那些迁移待完成。所有迁移的执行时间以及结果。
-  Validate：验证数据库结构与迁移脚本的异同。
-  Baseline：根据现有的数据库结构生成一个基准迁移脚本。
####  异常处理
[异常]以下异常说明重复执行同一个版本的命令。例如版本创建失败了。对应的表没创建就会出现以下错误
```
org.flywaydb.core.api.FlywayException: Validate failed
```

## mybatis.generator
#### 添加依赖
主要是jdbc和generator
```
<!-- https://mvnrepository.com/artifact/org.mybatis.generator/mybatis-generator-core -->
<dependency>
    <groupId>org.mybatis.generator</groupId>
    <artifactId>mybatis-generator-core</artifactId>
    <version>1.3.6</version>
</dependency>

<!-- https://mvnrepository.com/artifact/mysql/mysql-connector-java -->
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>5.1.38</version>
</dependency>
```
#### 新建并配置generatorConfig.xml
　这个文件主要是对从哪来，到哪去的描述，还有一些特殊要求的配置

```
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE generatorConfiguration
        PUBLIC "-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN"
        "http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd">
<generatorConfiguration>
    <context id="DB2Tables" targetRuntime="MyBatis3">
        <!-- 生成mysql带有分页的sql的插件  这个可以自己写，-->
        <plugin type="generator.MysqlPaginationPlugin" />
        <plugin type="org.mybatis.generator.plugins.ToStringPlugin" />
        <plugin type="org.mybatis.generator.plugins.SerializablePlugin" />
        <!-- 自定义的注释规则，继承 DefaultCommentGenerator 重写 一些方法 -->
        <commentGenerator type="generator.NewbatisGenerator">
            <!-- 是否去除自动生成日期的注释 true：是 ： false:否 -->
            <property name="suppressDate" value="true"/>
            <!-- 是否去除所有自动生成的注释 true：是 ： false:否 -->
            <property name="suppressAllComments" value="true"/>
        </commentGenerator>
        <jdbcConnection driverClass="com.mysql.cj.jdbc.Driver" connectionURL="jdbc:mysql://数据库地址"
                        userId="username"
                        password="password">
        </jdbcConnection>
        <!--生成entity类存放位置-->
        <javaModelGenerator targetPackage="包名（com.generator.test.entity）" targetProject="项目地址到\java （X:\workspace\src\main\java）">
            <property name="enableSubPackages" value="true"/>
            <property name="trimStrings" value="true"/>
        </javaModelGenerator>
        <!--生成映射文件存放位置-->
        <sqlMapGenerator targetPackage="包名（com.generator.test.mapper）" targetProject="项目地址到\java （X:\workspace\src\main\java）">
            <property name="enableSubPackages" value="true"/>
        </sqlMapGenerator>
        <!--生成Dao类存放位置-->
        <javaClientGenerator type="XMLMAPPER" targetPackage="包名（com.generator.test.dao）"
                             targetProject="项目地址到\java （X:\workspace\src\main\java）">
            <property name="enableSubPackages" value="true"/>
        </javaClientGenerator>
        <table tableName="表名" domainObjectName="生成实体的类名">
        </table>
    </context>
</generatorConfiguration>
```
#### 配置mybatis
在application.propertis添加以下配置
```
mybatis.configuration.map-underscore-to-camel-case=true (驼峰命名自动识别)
mybatis.type-aliases-package=life.catwe.community.mapper
mybatis.mapper-locations=classpath:mapper/*.xml
```
在pom.xml添加以下配置
```
	    <plugin>
                <groupId>org.mybatis.generator</groupId>
                <artifactId>mybatis-generator-maven-plugin</artifactId>
                <version>1.3.7</version>
                <dependencies>
                    <dependency>
                        <groupId>mysql</groupId>
                        <artifactId>mysql-connector-java</artifactId>
                        <version>8.0.21</version>
                    </dependency>
                </dependencies>
            </plugin>
```
[异常]配置错误或者配置遗漏执行generator会出现以下错误
```
No plugin found for prefix 'mybatis-generator' in the current project and in the plugin groups [org.apache.maven.plugins, org.codehaus.mojo] available from the repositories [local (/Users/rassa/.m2/repository), central (https://repo.maven.apache.org/maven2)] -> [Help 1]
```

[异常]高版本mysql数据库schema执行generator会出现以下警告，出现不属于本项目其他数据库的查询语句且缺少了针对主键的查询语句
```
Table Configuration catwehome.user matched more than one table
```
官方说明需在 generatorConfig.xml 中的 <jdbcConnection> 标签下加入如下属性可解决问题
```
<jdbcConnection driverClass="com.mysql.cj.jdbc.Driver"
                        connectionURL="jdbc:mysql://localhost:3306/text"
                        userId="root"
                        password="root">
            <property name="nullCatalogMeansCurrent" value="true"></property>
        </jdbcConnection>
```
启动类添加组件扫描
```
@SpringBootApplication
@MapperScan("cn.catwe.community.mapper")
public class CommunityApplication {

    public static void main(String[] args) {
        SpringApplication.run(CommunityApplication.class, args);
    }
}

```
[异常]mybatis映射异常 极大原因是因为用错了数据库语句。检查数据库代码，特别是方法传参是否规范。
```
nested exception is org.apache.ibatis.reflection.ReflectionException: There is no getter for property named 'distinct' in 'class cn.catwe.community.model.User'
```

<hr>
[异常]调用service层方法时错误的方法,该方法因为自己new的对象没有spring注入导致mapper空指针

```
public void selectData(){
	Long result = new UserService().selectUsername("ie4qprxx");
}
```

正确调用方法如下,先将需要使用的对象通过Spring注入后再使用
```
 @Autowired private UserService userService;
	public void selectData(){
 		Long result = userService.selectUsername("ie4qprxx");
	}
```
#### generator命令
mvn -Dmybatis.generator.overwrite=true mybatis-generator:generate

## idea快捷键
- 快速生成对象：option+command+V
- 格式化代码： option+command+L

## 配置mysql
```
spring.datasource.url=jdbc:mysql://localhost:3306/test

spring.datasource.username=root

spring.datasource.password=root

spring.datasource.driver-class-name=com.mysql.jdbc.Driver
```



## POSTMAN

