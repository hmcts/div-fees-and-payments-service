FROM hmcts/cnp-java-base:openjdk-jre-8-alpine-1.4

ENV APP div-fees-and-payment-service.jar
ENV APPLICATION_TOTAL_MEMORY 1024M
ENV APPLICATION_SIZE_ON_DISK_IN_MB 61

COPY build/libs/$APP /opt/app/

WORKDIR /opt/app

HEALTHCHECK --interval=100s --timeout=100s --retries=10 CMD http_proxy="" wget -q http://localhost:4009/health || exit 1

