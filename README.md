# graphql-and-hibernate-reactive

This project is a reproducer of a bug / incompatiblity in Quarkus between the GraphQL and hibernate reactive modules.

What the reproducer does is trigger a condition in which the GraphQL execution engine makes parallel calls to `PeopleService.foos`, which returns a Hibernate Reactive Uni.

There's one test that fails with a stack trace.

## Solutions

This repository also comes with two solutions. 

The branch *fix-simple* uses a non-blocking mutex to sequence service methods that run on the same Vertx context
The branch *fix-finegrained* uses a non-blocking mutex in a more fine-grained way; only on session acquisition and execution of the Panache repository methods. 

See the README's in those branches for details.