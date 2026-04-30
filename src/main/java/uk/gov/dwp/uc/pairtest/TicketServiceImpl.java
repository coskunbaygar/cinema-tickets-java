package uk.gov.dwp.uc.pairtest;

import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest.Type;

/*
 * Note:
 * In a production-grade application, I would typically separate responsibilities
 * into dedicated components such as validators, calculators, and service collaborators
 * to improve maintainability, testability, and adherence to the Single Responsibility Principle.
 *
 * However, this coding exercise explicitly states:
 * "TicketServiceImpl should only have private methods other than the one below."
 *
 * To respect the requested constraints and expected design of the exercise,
 * the solution is intentionally implemented within a single class using private helper methods.
 */

public class TicketServiceImpl implements TicketService {

    private static final int MAX_TICKETS = 25;
    private static final int ADULT_PRICE = 25;
    private static final int CHILD_PRICE = 15;

    private final TicketPaymentService paymentService;
    private final SeatReservationService seatService;

    /*
    Only the class constructor public other than purchaseTickets method
    */

    public TicketServiceImpl(TicketPaymentService paymentService,
                              SeatReservationService seatService) {
        this.paymentService = paymentService;
        this.seatService = seatService;
    }

    /**
     * Should only have private methods other than the one below.
     */

    @Override
    public void purchaseTickets(Long accountId, TicketTypeRequest... ticketTypeRequests) throws InvalidPurchaseException {
        validate(accountId, ticketTypeRequests);

        int adults = count(ticketTypeRequests, Type.ADULT);
        int children = count(ticketTypeRequests, Type.CHILD);

        int totalAmount = (adults * ADULT_PRICE) + (children * CHILD_PRICE);
        int totalSeats = adults + children;

        paymentService.makePayment(accountId, totalAmount);
        seatService.reserveSeat(accountId, totalSeats);
    }

    private void validate(Long accountId, TicketTypeRequest[] requests) {

        if (accountId == null || accountId <= 0) {
            throw new InvalidPurchaseException();
        }

        if (requests == null || requests.length == 0) {
            throw new InvalidPurchaseException();
        }

        int adults = count(requests, Type.ADULT);
        int children = count(requests, Type.CHILD);
        int infants = count(requests, Type.INFANT);

        int total = adults + children + infants;

        if (total > MAX_TICKETS) {
            throw new InvalidPurchaseException();
        }

        if (adults == 0 && (children > 0 || infants > 0)) {
            throw new InvalidPurchaseException();
        }

        //I assumed 1 infants can seat on an 1 Adult's lap. Not more than 1.
        if (infants > adults) { 
            throw new InvalidPurchaseException();
        }
    }

    private int count(TicketTypeRequest[] requests, Type type) {
        int count = 0;

        for (TicketTypeRequest request : requests) {
            if (request.getTicketType() == type) {
                count += request.getNoOfTickets();
            }
        }

        return count;
    }

}
