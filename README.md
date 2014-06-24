REST Scenario's
===============

This is a library to help you write readable and reusable tests for your REST API.

## Using this library

Right now, you can (re)use this library by adding a git submodule to your git project. Visit [this page](http://git-scm.com/book/en/Git-Tools-Submodules) to learn more about git submodules.

    git submodule add git@github.com:cegeka/rest-scenarios.git

## Concepts
### Steps
Write a Step for every resource you have.

### Scenarios
Scenario's typically contain a sequence of Steps that should provide a completed state of your application.

For example, you want to create a Book, but a Book requires an existing Author.
So you'll first call AuthorStep.post(), and then BookStep.post() and you'll have set up Book with an Author this way.

Scenario's also typically contain assertions that verify the completed state of your application.
