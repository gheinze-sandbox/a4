# Accounted4 Asset Management

*	_a4ac [abandoned]_  An AngularJs client calling REST-like services. Full-stack client (Node, npm, bower, grunt, jasmine)
*	**asset-manager** [latest] Application framework using: SpringMVC, SpringSecurity, ThymeLeaf
*	**money** [libarary] Representing money, containing some amortization functions
*	_service [abandoned]_ Spring REST-like server, can curl a request to generate a pdf amortization schedule

##asset-manager

* install PostgreSQL
* create the schema objects via src/main/sql/create_schema.sh (read the script to check about connect info)
* build the "money" library first (mvn install) since "asset-manager" will be using the library
* update src/main/resources.properties with appropriate db connect information
* build the "asset-manager" (mvn install) to generate the war file for deployment on a web server

## service
* mvn install

Look at AmortizationController for an example of the requests to send:

**Generate an amortization schedule returned as a json object.**

Example invocation:

```
curl -i -H "Accept: application/json" -H "Content-Type: application/json" --data '{"loanAmount":"20000.00", "regularPayment":"200","startDate":"2013-01-05","adjustmentDate":"2013-01-15","termInMonths":"12","interestOnly":"false","amortizationPeriodMonths":"120","compoundingPeriodsPerYear":"2","interestRate":"10"}' http://localhost:8080/loan/schedule.json
```

Note:
* through cli: all fields required, no tabs/line breaks

**Stream back an amortization schedule in pdf format.**

Example invocation:

```
curl -i -H "Accept: application/pdf" -H "Content-Type: application/json" --data '{"loanAmount":"20000.00", "regularPayment":"0","startDate":"2013-01-05","adjustmentDate":"2013-01-15","termInMonths":"12","interestOnly":"false","amortizationPeriodMonths":"120","compoundingPeriodsPerYear":"2","interestRate":"10"}' http://localhost:8080/loan/schedule.pdf -o junk.pdf
```
