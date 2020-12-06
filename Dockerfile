FROM openjdk:14-alpine

MAINTAINER Thalles Mendes

RUN apk update && apk add bash

RUN mkdir -p /opt/app

ENV PROJECT_HOME /opt/app
ENV JWT_SECRET_KEY stubJwt
ENV DATASOURCE_URL jdbc:postgresql://db:5432/quote
ENV DATABASE_USERNAME quote
ENV DATABASE_PASSWORD quote
ENV HTTP_QUANDL_HOST https://www.quandl.com
ENV HTTP_QUANDL_KEY KrzAVwRiWxzs9qCbVpbC
ENV HTTP_AWESOME_HOST https://economia.awesomeapi.com.br

COPY target/pricequote-*.jar $PROJECT_HOME/app.jar

WORKDIR $PROJECT_HOME

CMD ["java", "-jar", "--enable-preview" ,"./app.jar"]