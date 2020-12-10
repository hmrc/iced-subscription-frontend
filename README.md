
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
|```/safety-and-security-subscription```  |        GET        | Landing page showing information about enrolment with button to start the process. |
|```/safety-and-security-subscription/start```   |        GET        | Starts the enrolment process. |
|```/safety-and-security-subscription/sign-out```|        GET        | Clears the session. |
|```/safety-and-security-subscription/keep-alive```   |        GET        | Endpoint used by session logout timer to stay signed in. |
|```/safety-and-security-subscription/successfully-enrolled``` |        GET        | Callback to notify successful enrolment. |

### License

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").

