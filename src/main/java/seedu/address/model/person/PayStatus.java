package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

/**
 * Represents a Person's pay status in the address book.
 * Guarantees: immutable; is always valid
 */
public class PayStatus {
    public static final String PAID = "PAID";
    public static final String NOT_PAID = "NOT PAID";

    public final String value;

    /**
    * Constructor for PayStatus.
    * @param payStatus A valid pay status.
    */
    public PayStatus(String payStatus) {
        requireNonNull(payStatus);
        value = payStatus;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof PayStatus // instanceof handles nulls
                && value.equals(((PayStatus) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
