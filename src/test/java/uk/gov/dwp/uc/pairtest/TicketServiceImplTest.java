package uk.gov.dwp.uc.pairtest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest.Type;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class TicketServiceImplTest {

    private TicketPaymentService paymentService;
    private SeatReservationService seatService;
    private TicketServiceImpl service;

    @BeforeEach
    void setUp() {
        paymentService = mock(TicketPaymentService.class);
        seatService = mock(SeatReservationService.class);

        service = new TicketServiceImpl(paymentService, seatService);
    }

    /*
     * Test case for 
     * - There are 3 types of tickets i.e. Infant, Child, and Adult.
     * - The ticket prices are based on the type of ticket (see table below).
     * - The ticket purchaser declares how many and what type of tickets they want to buy.
     * - Multiple tickets can be purchased at any given time.  
    */
    @Test
    void shouldProcessValidPurchaseCorrectly() {

        service.purchaseTickets(
                1L,
                new TicketTypeRequest(Type.ADULT, 2),
                new TicketTypeRequest(Type.CHILD, 1),
                new TicketTypeRequest(Type.INFANT, 1));

        verify(paymentService).makePayment(1L, 65);
        verify(seatService).reserveSeat(1L, 3);
    }

    /*
     * Test case for invalid account id
    */
    @Test
    void shouldRejectWhenAccountIDNotGreaterThanZero() {

        assertThrows(InvalidPurchaseException.class, () -> service.purchaseTickets(
                -1L,
                new TicketTypeRequest(Type.ADULT, 2)));

        verifyNoInteractions(paymentService, seatService);
    }


    /*
     * Test case for "Child and Infant tickets cannot be purchased without purchasing an Adult ticket."
    */
    @Test
    void shouldRejectWhenNoAdult() {

        assertThrows(InvalidPurchaseException.class, () -> service.purchaseTickets(
                1L,
                new TicketTypeRequest(Type.CHILD, 2)));

        verifyNoInteractions(paymentService, seatService);
    }

    /*
     * Test case for "Only a maximum of 25 tickets that can be purchased at a time."
    */
    @Test
    void shouldRejectMoreThan25Tickets() {

        assertThrows(InvalidPurchaseException.class, () -> service.purchaseTickets(
                1L,
                new TicketTypeRequest(Type.ADULT, 26)));
    }

    /*
     * Test case for "Infants do not pay for a ticket and are not allocated a seat. They will be sitting on an Adult's lap."
    */
    @Test
    void shouldRejectInfantMoreThanAdult() {

        assertThrows(InvalidPurchaseException.class, () -> service.purchaseTickets(
                1L,
                new TicketTypeRequest(Type.ADULT, 1),
                new TicketTypeRequest(Type.INFANT, 2)));
    }
}