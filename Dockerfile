ARG APP_INSIGHTS_AGENT_VERSION=3.2.6
ARG PLATFORM=""
FROM hmctspublic.azurecr.io/base/java${PLATFORM}:17-distroless

ENV APP div-fees-and-payment-service.jar

COPY lib/AI-Agent.xml /opt/app/
COPY build/libs/$APP /opt/app/

EXPOSE 4009

CMD ["div-fees-and-payment-service.jar"]