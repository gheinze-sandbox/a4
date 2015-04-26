# Sandbox for experimental code

*	_a4ac  An AngularJs client calling REST-like services. Full-stack client (Node, npm, bower, grunt, jasmine)
*	_service Spring REST-like server, can curl a request to generate a pdf amortization schedule


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
