#!/bin/zsh

cd "$(dirname "$0")/.."

echo "Detecting environment variables..."
echo "Java Home: $JAVA_HOME"
echo "Maven Home: $M2_HOME"

echo "Updating path..."
export PATH=$JAVA_HOME/bin:$PATH
export PATH=$M2_HOME/bin:$PATH

echo "Building..."
sam build -t template.yml

echo "Running..."
sam local start-api \
  --template .aws-sam/build/template.yaml \
  --port 3000 \
  --host 0.0.0.0 \
  --docker-network sam-local-net \
  --warm-containers EAGER