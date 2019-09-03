package br.ufes.informatica.oldenburg.core.controller;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import br.ufes.inf.nemo.jbutler.ejb.application.CrudService;
import br.ufes.inf.nemo.jbutler.ejb.controller.CrudController;
import br.ufes.informatica.oldenburg.core.application.ManageWorkshopsService;
import br.ufes.informatica.oldenburg.core.domain.Workshop;

@Named
@SessionScoped
public class ManageWorkshopsController extends CrudController<Workshop> {
	private static final long serialVersionUID = 1L;

	@EJB
	private ManageWorkshopsService manageWorkshopsService;

	@Override
	protected CrudService<Workshop> getCrudService() {
		return manageWorkshopsService;
	}

	@Override
	protected void initFilters() {
	}
}
