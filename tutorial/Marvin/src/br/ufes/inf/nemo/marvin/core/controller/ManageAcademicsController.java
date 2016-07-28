package br.ufes.inf.nemo.marvin.core.controller;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import br.ufes.inf.nemo.jbutler.ejb.application.CrudService;
import br.ufes.inf.nemo.jbutler.ejb.application.filters.SimpleFilter;
import br.ufes.inf.nemo.jbutler.ejb.controller.CrudController;
import br.ufes.inf.nemo.marvin.core.application.ManageAcademicsService;
import br.ufes.inf.nemo.marvin.core.domain.Academic;

/**
 * TODO: document this type.
 *
 * @author Vítor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
@Named @SessionScoped
public class ManageAcademicsController extends CrudController<Academic> {
	/** TODO: document this field. */
	private static final long serialVersionUID = 1L;
	
	/** TODO: document this field. */
	@EJB 
	private ManageAcademicsService manageAcademicsService;

	/** @see br.ufes.inf.nemo.jbutler.ejb.controller.CrudController#getCrudService() */
	@Override
	protected CrudService<Academic> getCrudService() {
		return manageAcademicsService;
	}

	/** @see br.ufes.inf.nemo.jbutler.ejb.controller.ListingController#initFilters() */
	@Override
	protected void initFilters() {
		addFilter(new SimpleFilter("manageAcademics.filter.byName", "name", getI18nMessage("msgsCore", "manageAcademics.text.filter.byName")));
	}
}
