bucket_name="dev-bucket-encurtador-links-test"

env = "dev"
region = "sa-east-1"

lambda_memory = 1024

app_prefix = "encurtador-links-saas"

lambda_handler = "com.salessew.StreamLambdaHandler::handleRequest"

# acm_domain_name_arn = "arn:aws:acm:us-east-1:800960611580:certificate/99fbcd92-6e3a-40a5-94bd-904e2ec0169f"
# domain_name = "fbr.buildrun.link"