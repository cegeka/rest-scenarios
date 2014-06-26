REST Scenario's
===============

This is a library to help you write readable and reusable tests for your REST API.

## Using this library

Right now, you can (re)use this library by adding a git submodule to your git project. Visit [this page](http://git-scm.com/book/en/Git-Tools-Submodules) to learn more about git submodules.

    git submodule add git@github.com:cegeka/rest-scenarios.git

There's [an open issue](https://github.com/cegeka/rest-scenarios/issues/2) to deploy to Cegeka's public nexus repository so you'd be able to simply add a maven dependency that would look like this

    <dependency>
       <groupId>be.cegeka</groupId>
       <artifactId>rest-scenarios</artifactId>
       <version>0.0.1-SNAPSHOT</version>
    </dependency>

## Concepts
### Steps
Write a Step for every resource you have.

Let's say you have a resource that you can use to manage Books (I know, "very original"). Your BookStep would be used somewhat like this:

    // create a book and keep the created id
    Optional<String> bookId = new Bookstep(baseResource).withBookRepresentation(aBookRepresentation).post();
    // get all books
    BookRepresentation[] allBooks = new BookStep(baseResource).get(BookRepresentation[].class);
    // Update a specific book
    new BookStep(baseResource).withBookRepresentation(anUpdatedBookRepresentation).put(bookId);
    // Delete a specific book
    new BookStep(baseResource).delete(bookId);
    

### Scenarios
Scenario's typically contain a sequence of Steps that should provide a completed state of your application.

For example, you want to create a Book, but a Book requires an existing Author.
So you'll first call `authorStep.post()`, and then `bookStep.post()` and you'll have set up Book with an Author this way.

Scenario's also typically contain assertions that verify the completed state of your application.
