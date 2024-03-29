#!/usr/bin/env bash

zap-api-scan.py -t ${TEST_URL//http/https}/v2/api-docs -f openapi -S -d -u ${SecurityRules} -P 1001 -l FAIL
curl --fail http://0.0.0.0:1001/OTHER/core/other/jsonreport/?formMethod=GET --output report.json
export LC_ALL=C.UTF-8
export LANG=C.UTF-8
zap-cli --zap-url http://0.0.0.0 -p 1001 report -o /zap/api-report.html -f html
zap-cli --zap-url http://0.0.0.0 -p 1001 alerts -l High --exit-code False
cp /zap/api-report.html functional-output/
cat zap.out