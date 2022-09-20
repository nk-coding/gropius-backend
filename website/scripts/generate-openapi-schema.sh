#/bin/bash

mkdir -p ./website/schemas
export GROPIUS_INTERNAL_BACKEND_JWT_SECRET="SecretToGetGraphQLSchemaDoNotUseInProduction"
export GROPIUS_LOGIN_SPECIFIC_JWT_SECRET="LoginSpecificSecretdoNotUseInProduction"

./gradlew login-service:npmRunStart &
gradlew_pid=$!
schema_endpoint="http://localhost:3000/login-api-doc-json"
c=0
until curl -s -f -o /dev/null $schema_endpoint
do
    ((c++))
    if ((c > 120)); then
        echo "Failed to get openapi schema: timeout"
        exit 1
    fi
    echo "Waiting for server"
    sleep 2
done
curl -s -o ./website/schemas/login.json $schema_endpoint
echo "Stopping login server"
kill $gradlew_pid