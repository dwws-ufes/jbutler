package br.ufes.informatica.oldenburg.core.application;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import br.ufes.inf.nemo.jbutler.ejb.application.CrudServiceBean;
import br.ufes.inf.nemo.jbutler.ejb.persistence.BaseDAO;
import br.ufes.informatica.oldenburg.core.domain.Workshop;
import br.ufes.informatica.oldenburg.core.persistence.WorkshopDAO;

@Stateless
@PermitAll
public class ManageWorkshopsServiceBean extends CrudServiceBean<Workshop> implements ManageWorkshopsService {
	private static final long serialVersionUID = 1L;

	@EJB
	private WorkshopDAO workshopDAO;

	@Override
	public BaseDAO<Workshop> getDAO() {
		return workshopDAO;
	}
}
