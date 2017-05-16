# EJB Utils -> EJBUtils -> JButils -> JButler

This repository is composed of projects that contain utility classes and small frameworks that might be useful for projects that use the standard Java EE stack of frameworks (JSF, CDI, JPA, EJB), plus [PrimeFaces](http://www.primefaces.org).

* _jbutler-wp_: for Java EE, Web Profile projects;
* _jbutler-full_: for Java EE, Full projects.

The projects are managed using [Maven2](http://maven.apache.org) and are published at the [Nemo Maven2 repository](http://dev.nemo.inf.ufes.br/maven2/). To set one of the above projects as a Maven2 dependency, add the following repository to your project's `pom.xml`

```xml
<repository>
	<releases>
		<enabled>true</enabled>
		<updatePolicy>always</updatePolicy>
		<checksumPolicy>fail</checksumPolicy>
	</releases>
	<id>br.ufes.inf.nemo</id>
	<name>Nemo Maven Repository</name>
	<url>http://dev.nemo.inf.ufes.br/maven2</url>
	<layout>default</layout>
</repository>
```

Then you can define one of the projects as a dependency, e.g.:

```xml
<dependency>
	<groupId>br.ufes.inf.nemo</groupId>
	<artifactId>jbutler-wp</artifactId>
	<version>1.4</version>
</dependency>
```


JButler is the new version of [nemo-utils](https://github.com/nemo-ufes/nemo-utils), which will still be available but will not receive any updates.