package seedu.address.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_SUBJECT_MATH;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.AMY;
import static seedu.address.testutil.TypicalPersons.BOB;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.person.Person;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.DuplicatePhoneException;
import seedu.address.testutil.PersonBuilder;

public class AddressBookTest {

    private final AddressBook addressBook = new AddressBook();

    @Test
    public void constructor() {
        assertEquals(Collections.emptyList(), addressBook.getPersonList());
    }

    @Test
    public void resetData_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> addressBook.resetData(null));
    }

    @Test
    public void resetData_withValidReadOnlyAddressBook_replacesData() {
        AddressBook newData = getTypicalAddressBook();
        addressBook.resetData(newData);
        assertEquals(newData, addressBook);
    }

    @Test
    public void resetData_withDuplicatePersons_throwsDuplicatePersonException() {
        // Two persons with the same identity fields
        Person editedAlice = new PersonBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).withSubjects(VALID_SUBJECT_MATH)
                .build();
        List<Person> newPersons = Arrays.asList(ALICE, editedAlice);
        AddressBookStub newData = new AddressBookStub(newPersons);

        assertThrows(DuplicatePersonException.class, () -> addressBook.resetData(newData));
    }

    @Test
    public void resetData_withDuplicatePhone_throwsDuplicatePhoneException() {
        // Two persons with the same phone number
        Person editedAmy = new PersonBuilder(AMY).withPhone(VALID_PHONE_BOB).withSubjects(VALID_SUBJECT_MATH)
                .build();
        List<Person> newPersons = Arrays.asList(BOB, editedAmy);
        AddressBookStub newData = new AddressBookStub(newPersons);

        assertThrows(DuplicatePhoneException.class, () -> addressBook.resetData(newData));
    }

    @Test
    public void hasPerson_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> addressBook.hasPerson(null));
    }

    @Test
    public void hasPhone_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> addressBook.hasPhone(null));
    }

    @Test
    public void hasPerson_personNotInAddressBook_returnsFalse() {
        assertFalse(addressBook.hasPerson(ALICE));
    }

    @Test
    public void hasPhone_personNotInAddressBook_returnsFalse() {
        assertFalse(addressBook.hasPerson(ALICE));
    }

    @Test
    public void hasPerson_personInAddressBook_returnsTrue() {
        addressBook.addPerson(ALICE);
        assertTrue(addressBook.hasPerson(ALICE));
    }

    @Test
    public void hasPhone_personInAddressBook_returnsTrue() {
        addressBook.addPerson(ALICE);
        assertTrue(addressBook.hasPhone(ALICE));
    }

    @Test
    public void hasPerson_personWithSameIdentityFieldsInAddressBook_returnsTrue() {
        addressBook.addPerson(ALICE);
        Person editedAlice = new PersonBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).withSubjects(VALID_SUBJECT_MATH)
                .build();
        assertTrue(addressBook.hasPerson(editedAlice));
    }

    @Test
    public void hasPhone_personWithSamePhoneNumberInAddressBook_returnsTrue() {
        addressBook.addPerson(BOB);
        Person editedBob = new PersonBuilder(ALICE).withPhone(VALID_PHONE_BOB).withSubjects(VALID_SUBJECT_MATH)
                .build();
        assertTrue(addressBook.hasPhone(editedBob));
    }

    @Test
    public void getPersonList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> addressBook.getPersonList().remove(0));
    }

    @Test
    public void sortPersonListByNextLesson_sortsCorrectly() {
        // Create an address book to be sorted
        AddressBook addressBookToSort = new AddressBook();

        Person editedAlice = new PersonBuilder().withName("Alice").withPhone("12345678")
                .withNextLesson(LocalDate.of(2025, 6, 10), LocalTime.of(10, 0), LocalTime.of(12, 0))
                .build();

        Person editedBob = new PersonBuilder().withName("Bob").withPhone("23456789")
                .withNextLesson(LocalDate.of(2025, 6, 11), LocalTime.of(10, 0), LocalTime.of(12, 0))
                .build();

        Person editedCharlie = new PersonBuilder().withName("Charlie").withPhone("34567891")
                .withNextLesson(LocalDate.of(2025, 6, 11), LocalTime.of(13, 0), LocalTime.of(15, 0))
                .build();

        Person editedDavid = new PersonBuilder().withName("David").withPhone("45678912")
                .build();

        addressBook.addPerson(editedAlice);
        addressBook.addPerson(editedBob);
        addressBook.addPerson(editedCharlie);
        addressBook.addPerson(editedDavid);

        addressBookToSort.addPerson(editedAlice);
        addressBookToSort.addPerson(editedDavid);
        addressBookToSort.addPerson(editedBob);
        addressBookToSort.addPerson(editedCharlie);
        addressBookToSort.sortPersonListByNextLesson();

        assertEquals(addressBook, addressBookToSort);

        // Add Ethan to have the earliest lesson to test sorting
        Person editedEthan = new PersonBuilder().withName("Ethan").withPhone("56789123")
                .withNextLesson(LocalDate.of(2025, 6, 10), LocalTime.of(8, 0), LocalTime.of(10, 0))
                .build();

        AddressBook expectedAddressBook = new AddressBook();
        expectedAddressBook.addPerson(editedEthan);
        expectedAddressBook.addPerson(editedAlice);
        expectedAddressBook.addPerson(editedBob);
        expectedAddressBook.addPerson(editedCharlie);
        expectedAddressBook.addPerson(editedDavid);

        addressBookToSort.addPerson(editedEthan);
        addressBookToSort.sortPersonListByNextLesson();

        assertEquals(expectedAddressBook, addressBookToSort);
    }

    @Test
    public void toStringMethod() {
        String expected = AddressBook.class.getCanonicalName() + "{persons=" + addressBook.getPersonList() + "}";
        assertEquals(expected, addressBook.toString());
    }

    @Test
    public void equals() {
        // same object -> returns true
        assertEquals(addressBook, addressBook);

        // null -> returns false
        assertNotEquals(addressBook, null);

        // different types -> returns false
        assertNotEquals(addressBook, 1);

        // different persons -> returns false
        AddressBook addressBookCopy = new AddressBook();
        addressBook.addPerson(ALICE);
        assertNotEquals(addressBook, addressBookCopy);

        // same persons -> returns true
        addressBookCopy.addPerson(ALICE);
        assertEquals(addressBook, addressBookCopy);

        // same hashcode -> returns true
        assertEquals(addressBook.hashCode(), addressBookCopy.hashCode());
    }

    /**
     * A stub ReadOnlyAddressBook whose persons list can violate interface constraints.
     */
    private static class AddressBookStub implements ReadOnlyAddressBook {
        private final ObservableList<Person> persons = FXCollections.observableArrayList();

        AddressBookStub(Collection<Person> persons) {
            this.persons.setAll(persons);
        }

        @Override
        public ObservableList<Person> getPersonList() {
            return persons;
        }
    }

}
