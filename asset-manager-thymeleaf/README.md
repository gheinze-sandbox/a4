# asset-manager
Spring MVC application with Spring Security, ThymeLeaf

The default db configuration (`src/main/resources/persistence.properties`) assumes
 - postgresql has been installed
 - a database named "a4" has been created
 - connecting with the user "postgres"
 - "postgres" user has "a4" on the search path (`alter user postgres set search_path = a4,public;`)

Build the schema by running `src/main/sql/create_schema.sh`

Build the project by running `mvn install`.  Note that you may need to run `mvn install` on the `money` project first since that will be a dependency.
