package br.ufes.inf.nemo.marvin.people.domain;

import br.ufes.inf.nemo.jbutler.ejb.persistence.PersistentObjectSupport_;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2016-07-28T09:01:26.009-0300")
@StaticMetamodel(Person.class)
public class Person_ extends PersistentObjectSupport_ {
	public static volatile SingularAttribute<Person, String> name;
	public static volatile SingularAttribute<Person, Date> birthDate;
	public static volatile SingularAttribute<Person, Character> gender;
}
