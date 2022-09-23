# Login-Service

## Description

Nodejs/Nestjs service that does the user management and authentication for the gropius-backend. It will generate tokens for the user to use to identify against the api

## Installation

```bash
$ npm install
```

### Graphql Model Generation
To generate the needed model types from the graphql schema do the following
1. Make sure, the api-internal service is running on `http://localhost:8081/graphql` (or another url)
2. Check that the value of the `schema` field in codegen.yml matches the API endpoint under which the api-internal is running
3. Run `npm run generate-model` to generate all needed code

## Running the app

```bash
# development
$ npm run start

# watch mode
$ npm run start:dev

# production mode
$ npm run start:prod
```

## Test

```bash
# unit tests
$ npm run test

# e2e tests
$ npm run test:e2e

# test coverage
$ npm run test:cov
```