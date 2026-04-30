package uk.gov.dwp.uc.pairtest.domain;

/**
 * Immutable Object
 */

/*
* The final keyword was missing from the class and variable declarations; it has been added to ensure the object is immutable.
*/
public final class TicketTypeRequest {

    private final int noOfTickets;
    private final Type type;

    public TicketTypeRequest(Type type, int noOfTickets) {
        this.type = type;
        this.noOfTickets = noOfTickets;
    }

    public int getNoOfTickets() {
        return noOfTickets;
    }

    public Type getTicketType() {
        return type;
    }

    public enum Type {
        ADULT, CHILD , INFANT
    }

}
