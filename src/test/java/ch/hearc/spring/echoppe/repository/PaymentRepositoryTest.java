package ch.hearc.spring.echoppe.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import ch.hearc.spring.echoppe.model.Payment;

@RunWith(SpringRunner.class)
@DataJpaTest
public class PaymentRepositoryTest {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private PaymentRepository payementRepository;

	@Test
	public void givenPayment_whenPersistPayment_thePaymentIsPersisted() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");

		Payment payment = new Payment();
		try {
			payment.setDate(sdf.parse("04-04-2019 10:30:00"));
		} catch (ParseException e) {

		}

		payment.setMethod(0);
		payment.setStatus(0);

		entityManager.persist(payment);
		entityManager.flush();

		Optional<Payment> paymentRecherche = payementRepository.findById(payment.getId());

		
		assertTrue(paymentRecherche.isPresent());
		Payment payment2 = paymentRecherche.get();
		assertTrue(payment2.getId().equals(payment.getId()));
		assertTrue(payment2.getDate().equals(payment.getDate()));
		assertTrue(payment2.getMethod() == payment.getMethod());
		assertTrue(payment2.getStatus() == payment.getStatus());
		assertTrue(payment2.hashCode() == payment.hashCode());
		assertTrue(payment2.equals(payment));
		assertThat(payment2).isNotNull();
	}
}
