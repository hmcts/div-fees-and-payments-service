ARG APP_INSIGHTS_AGENT_VERSION=2.6.1
FROM hmctspublic.azurecr.io/base/java:11-distroless

ENV APP div-fees-and-payment-service.jar

COPY lib/AI-Agent.xml /opt/app/
COPY build/libs/$APP /opt/app/

EXPOSE 4009

CMD ["div-fees-and-payment-service.jar"]
