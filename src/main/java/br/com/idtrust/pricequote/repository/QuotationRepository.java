package br.com.idtrust.pricequote.repository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import br.com.idtrust.pricequote.model.Quotation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuotationRepository extends JpaRepository<Quotation, UUID> {

  Optional<Quotation> findFirstByCultureAndDate(final String culture, final LocalDate date);

}
