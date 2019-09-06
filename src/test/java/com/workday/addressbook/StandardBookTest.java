package com.workday.addressbook;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Test;

public class StandardBookTest {

    // email addresses
    final Address jimAddr = new Address("jim.halpert@theoffice.com");
    final Address michaelAddr = new Address("michael.scott@theoffice.com");
    final Address dwightAddr = new Address("dwight.schrute@theoffice.com");

    // primary aliases
    final Alias jimAlias = new Alias("Jim");
    final Alias michaelAlias = new Alias("Michael");
    final Alias dwightAlias = new Alias("Dwight");

    // secondary aliases
    final Alias bigTunaAlias = new Alias("Big Tuna");

    // groups
    final Group mgrGroup = new Group("Manager");
    final Group fireMarshalGroup = new Group("Fire Marshal");

    final Book book = new StandardBook();

    @Test
    public void addingNewMappingShouldModifyBook() {
        assertTrue(book.add(jimAlias, jimAddr));
    }

    @Test
    public void addingDuplicateMappingShouldNotModifyBook() {
        book.add(jimAlias, jimAddr);

        assertFalse(book.add(jimAlias, jimAddr));
    }

    @Test(expected = IllegalArgumentException.class)
    public void addingAliasMappedToMoreThanOneTargetShouldNotBeAllowed()
            throws Exception {
        book.add(jimAlias, jimAddr);

        book.add(jimAlias, michaelAddr);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nameNotAlreadyInBookShouldNotBeAllowedAsTarget() {
        book.add(dwightAlias, michaelAlias);
    }

    @Test
    public void deletingAliasShouldModifyBook() {
        book.add(jimAlias, jimAddr);

        assertTrue(book.delete(jimAlias, jimAddr));
    }

    @Test
    public void deletingGroupMemberShouldModifyBook() {
        book.add(michaelAlias, michaelAddr);
        book.add(dwightAlias, dwightAddr);
        book.add(mgrGroup, michaelAlias);
        book.add(mgrGroup, dwightAlias);

        assertTrue(book.delete(mgrGroup, dwightAlias));
    }

    @Test
    public void deletingMemberFromNonSingletonGroupThatIsTargetShouldBeAllowed() {
        book.add(michaelAlias, michaelAddr);
        book.add(dwightAlias, dwightAddr);
        book.add(mgrGroup, michaelAlias);
        book.add(mgrGroup, dwightAlias);
        book.add(fireMarshalGroup, mgrGroup);

        assertTrue(book.delete(mgrGroup, dwightAlias));
    }

    @Test(expected = IllegalArgumentException.class)
    public void deletingMemberFromSingletonGroupThatIsTargetShouldNotBeAllowed() {
        book.add(michaelAlias, michaelAddr);
        book.add(mgrGroup, michaelAlias);
        book.add(fireMarshalGroup, mgrGroup);

        assertTrue(book.delete(mgrGroup, michaelAlias));
    }

    @Test
    public void lookingUpAddressesInEmptyBook() {
        assertTrue(book.lookup(jimAlias).isEmpty());
    }

    @Test
    public void lookingUpAddressFromAliasOneStepAway() {
        book.add(jimAlias, jimAddr);

        assertTrue(book.lookup(jimAlias).contains(jimAddr));
    }

    @Test
    public void lookingUpAddressFromAliasTwoStepsAway() {
        book.add(jimAlias, jimAddr);
        book.add(bigTunaAlias, jimAlias);

        assertTrue(book.lookup(bigTunaAlias).contains(jimAddr));
    }

    @Test
    public void lookupUpAddressesFromGroupTwoStepsAway() {
        book.add(michaelAlias, michaelAddr);
        book.add(dwightAlias, dwightAddr);
        book.add(mgrGroup, michaelAlias);
        book.add(mgrGroup, dwightAlias);
        book.add(fireMarshalGroup, mgrGroup);

        //System.out.println(book);

        Set<Address> addrs = book.lookup(fireMarshalGroup);

        assertTrue(addrs.contains(michaelAddr));
        assertTrue(addrs.contains(dwightAddr));
    }
}
