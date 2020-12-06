package br.com.idtrust.pricequote.business;

import java.time.LocalDate;

import br.com.idtrust.pricequote.model.Quotation;

public interface QuoteBusiness {

  Quotation quotePrice(final String culture, final LocalDate date);

}
