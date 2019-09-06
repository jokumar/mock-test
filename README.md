Application

AddressBook is a simple application that associates email addresses with names (see Book interface)
* A name is either an alias or a group
* An alias maps to an email address directly or to another name (forming a name chain)
* A group is like an alias but it can refer to one or more addresses or names
* Any name belonging to the book must map (directly or indirectly) to at least one address
* A name cannot be removed from the book if that name is the target of another mapping (e.g. alias in group)

Exercises

1. Correct Failing Tests

Running StandardBookTests results in failures. Add the necessary validation checks to make the 
tests pass (see Target class for an example)

2. Extend Book

BookExt interface contains two new methods
a. Implement both methods in the existing StandardBook class
b. Add tests for these methods in StandardBookTests

3. Fix a Bug

A user of the book application has reported that he gets a java.lang.StackOverflowError
when doing a lookup on a group. He attaches some debug output of a book that generates 
the error (see below). 
a. Write a test to reproduce the problem
b. Modify the book class to fix the problem so all tests run successfully
and class fulfills the contract of the Book interface

{Book 
 {Group 'Manager'} => [{Alias 'Jim'}, {Group 'Fire Marshal'}, {Alias 'Michael'}]
 {Alias 'Jim'} => [{Address 'jim.halpert@theoffice.com'}]
 {Group 'Fire Marshal'} => [{Group 'Manager'}, {Alias 'Dwight'}]
 {Alias 'Dwight'} => [{Address 'dwight.schrute@theoffice.com'}]
 {Alias 'Michael'} => [{Address 'michael.scott@theoffice.com'}]
}EndBook

4. Optimize Book

Rewrite the lookup methods so that neither method uses recursion.

