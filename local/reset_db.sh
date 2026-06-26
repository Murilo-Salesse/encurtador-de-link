#!/bin/bash

REGION="sa-east-1"
ENDPOINT="http://localhost:4566"

echo "Listing tables..."

TABLES=$(aws --endpoint-url=$ENDPOINT \
  dynamodb list-tables \
  --region $REGION \
  --query 'TableNames[]' \
  --output text)

if [ -z "$TABLES" ]; then
  echo "No tables found."
  exit 0
fi

for TABLE in $TABLES
do
  echo "Deleting table: $TABLE"

  aws --endpoint-url=$ENDPOINT \
    dynamodb delete-table \
    --table-name $TABLE \
    --region $REGION
    --output text
done

echo "Done."