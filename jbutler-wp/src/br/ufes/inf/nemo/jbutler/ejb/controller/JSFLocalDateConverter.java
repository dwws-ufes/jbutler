package br.ufes.inf.nemo.jbutler.ejb.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 * Source:
 * http://www.guj.com.br/t/resolvido-como-salvar-um-atributo-localdate-com-o-valor-da-data-vindo-de-um-form/333264/4
 *
 * @author Seu-Nome (seu@email)
 * @version 1.0
 */
@FacesConverter(value = "localDateConverter")
public class JSFLocalDateConverter implements Converter {
	/** @see javax.faces.convert.Converter#getAsObject(javax.faces.context.FacesContext, javax.faces.component.UIComponent, java.lang.String) */
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Locale BRAZIL = new Locale("pt", "BR");
		return LocalDate.parse(value, DateTimeFormatter.ofPattern("dd/MM/yyyy").withLocale(BRAZIL));
	}

	/** @see javax.faces.convert.Converter#getAsString(javax.faces.context.FacesContext, javax.faces.component.UIComponent, java.lang.Object) */
	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		LocalDate dateValue = (LocalDate) value;

		return dateValue.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

	}
}
