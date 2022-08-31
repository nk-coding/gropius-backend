#/bin/bash
set -e

cd website
npx docusaurus graphql-to-doc:api-public -f
rm docs/graphql/api-public/generated.md