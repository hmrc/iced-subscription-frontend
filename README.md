
# Import Control Entry Declaration Subscription Frontend

Frontend providing an entry point to allow traders to enrol with the Safety & Security GB service.

Uses the [EORI Common Component Frontend](https://github.com/hmrc/eori-common-component-frontend) to coordinate 
the enrolment process. 

## Development Setup
Run locally: `sbt run` which runs on port `9837` by default

## Tests
Run Unit Tests: `sbt test`

## Resource paths

| Path | Supported Methods | Description |
| ----------------------------------------------------------| ----------------- | ------------|
|```/safety-and-security-subscription/start```   |        GET        | Starts the enrolment process. |
|```/safety-and-security-subscription/sign-out```|        GET        | Clears the session. |
|```/safety-and-security-subscription/keep-alive```   |        GET        | Endpoint used by session logout timer to stay signed in. |
|```/safety-and-security-subscription/successfully-enrolled``` |        GET        | Callback to notify successful enrolment. |

### All tests and checks

> `sbt runAllChecks`

This is an sbt command alias specific to this project. It will run

- clean
- compile
- unit tests
- and produce a coverage report.

You can view the coverage report in the browser by pasting the generated url.

#### Installing sbt plugin to check for library updates.
To check for dependency updates locally you will need to create this file locally ~/.sbt/1.0/plugins/sbt-updates.sbt
and paste - addSbtPlugin("com.timushev.sbt" % "sbt-updates" % "0.6.3") - into the file.
Then run:

> `sbt dependencyUpdates `

To view library update suggestions - this does not cover sbt plugins.
It is not advised to install the plugin for the project.


### License

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").

