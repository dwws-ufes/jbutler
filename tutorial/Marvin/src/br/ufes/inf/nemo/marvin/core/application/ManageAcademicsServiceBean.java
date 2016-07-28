package br.ufes.inf.nemo.marvin.core.application;

import java.util.Date;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import br.ufes.inf.nemo.jbutler.ejb.application.CrudServiceBean;
import br.ufes.inf.nemo.jbutler.ejb.persistence.BaseDAO;
import br.ufes.inf.nemo.marvin.core.domain.Academic;
import br.ufes.inf.nemo.marvin.core.persistence.AcademicDAO;

/**
 * TODO: document this type.
 *
 * @author Vítor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
@Stateless
public class ManageAcademicsServiceBean extends CrudServiceBean<Academic> implements ManageAcademicsService {
	/** TODO: document this field. */
	private static final long serialVersionUID = 1L;
	
	/** TODO: document this field. */
	@EJB 
	private AcademicDAO academicDAO;
	
	/** @see br.ufes.inf.nemo.jbutler.ejb.application.ListingService#getDAO() */
	@Override
	public BaseDAO<Academic> getDAO() {
		return academicDAO;
	}
	
	@Override
	protected Academic validate(Academic newEntity, Academic oldEntity) {
		// New academics must have their creation date set.
		Date now = new Date(System.currentTimeMillis());
		if (oldEntity == null) newEntity.setCreationDate(now);
		
		// All academics have their last update date set when persisted.
		newEntity.setLastUpdateDate(now);
		
		return newEntity;
	}
}
