package br.ufes.inf.nemo.marvin.core.domain;

import br.ufes.inf.nemo.jbutler.ejb.persistence.PersistentObjectSupport_;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2016-07-28T09:01:25.983-0300")
@StaticMetamodel(MarvinConfiguration.class)
public class MarvinConfiguration_ extends PersistentObjectSupport_ {
	public static volatile SingularAttribute<MarvinConfiguration, Date> creationDate;
	public static volatile SingularAttribute<MarvinConfiguration, String> institutionAcronym;
}
