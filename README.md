# Krypto Karteira Backend
 
> This is a practical project for an application to Android Software 
Engineer position. Check all details below

## Why

Seeking for a company where technology 
**really matters** and people **really cares** about Software Engineering and 
the product ...

The challenge itself **does not demand** a *Back-end for Front-end 
implementation* as a mandatory requirement. 

However, we should do things the right way : this BFF
abstracts **all the bussiness rules** away from the mobile client, since this is 
the right way to go when designing roles between front-ends and back-ends.

You may check the API documentation [here](https://kryptokarteira.herokuapp.com/).

## How

This is a small [Spark](http://sparkjava.com/) application. It abstracts 
away from clients the data sources to Bitcoin an USD prices for broking purposes, 
and enforce rules for virtual wallet resources used by the mobile app. 

As well, it provides a single home screen resource that simplifies 
the mobile development (one single GET instead of three). 

This solution relies on following approaches and technologies

- 100% written in Kotlin
- OO design powered by Clean Architecture
- Robust test coverage at application core
- Dependency Injection with [KodeIn](https://github.com/SalomonBrys/Kodein)
- Application data hosted at [restdb.io](https://restdb.io/)
- Hosted at Heroku for fun and profit

## Setup

For local development, you will need
- IntellijIDEA / Gradle (recommended)
- A [restdb.io](https://restdb.io/) project, with related `X-APIKEY`
- A `resources/private.config.properties` file with both keys used by this app
- **Or**, alternatively, you can set up the proper env vars 


## Building and running tests

You can build this project and run all the unit tests with the straightfoward

```
./gradlew build
```

In order to build the output `.jar` file required by Heroku, we leverage a custom
`stage` task, powered by the [shadowJar](https://github.com/johnrengelman/shadow) 
Gradle plugin to assemble an *uber-jar*:

```
./gradlew stage
```

## TODO

- Add acceptance tests (RestAssured + Betamax)

## License

```
The MIT License (MIT)

Copyright (c) 2017 Ubiratan Soares

Permission is hereby granted, free of charge, to any person obtaining a copy of
this software and associated documentation files (the "Software"), to deal in
the Software without restriction, including without limitation the rights to
use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
the Software, and to permit persons to whom the Software is furnished to do so,
subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
```

