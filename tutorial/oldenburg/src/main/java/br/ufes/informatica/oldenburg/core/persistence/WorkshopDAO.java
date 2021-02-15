package br.ufes.informatica.oldenburg.core.persistence;

import javax.ejb.Local;

import br.ufes.inf.nemo.jbutler.ejb.persistence.BaseDAO;
import br.ufes.informatica.oldenburg.core.domain.Workshop;

@Local
public interface WorkshopDAO extends BaseDAO<Workshop> {

}