# Marvin

A Web-based Information System that aggregates useful tools for managing teaching and research tasks in an university.

Many students from the [Department of Informatics](http://informatica.ufes.br) of the [Federal University of Espírito Santo](http://www.internacional.ufes.br/en) develop tools as part of their undergraduate final project. Marvin is an attempt to integrate these tools in a way they can be actually used by people.


## How to deploy

1. Install [Eclipse Mars.1 (version 4.5.x)](http://www.eclipse.org/home/index.php); 

2. Install [WildFly 9](http://wildfly.org) and create a Server configuration within Eclipse;

3. Install [MySQL](http://www.mysql.com/products/community/) and create a schema called `marvin` and a user called `marvin` with password `marvin` and full access to the homonymous database;

4. Configure [the MySQL JDBC driver](http://dev.mysql.com/downloads/connector/j/) in WildFly;

5. Configure the datasource in WildFly's `standalone.xml` file:

```XML
<datasource jta="true" jndi-name="java:jboss/datasources/Marvin" pool-name="MarvinPool" enabled="true" use-java-context="true">
    <connection-url>jdbc:mysql://localhost:3306/marvin</connection-url>
    <driver>mysql</driver>
    <security>
        <user-name>marvin</user-name>
        <password>marvin</password>
    </security>
</datasource>
``` 

Note: if you need detailed instructions on how to set up Eclipse, WildFly and MySQL, please refer to this [tutorial at nemo-utils wiki](https://github.com/nemo-ufes/nemo-utils/wiki/Tutorial%3A-a-Java-EE-Web-Profile-application-with-nemo-utils%2C-part-1).