FROM 723637732594.dkr.ecr.us-west-2.amazonaws.com/openjdk-11-alpine:91cb57eb
ARG artifact_name
WORKDIR /usr/src/sl-es-module/
COPY target/$artifact_name.jar /usr/src/sl-es-module/sl-es-module.jar
ENTRYPOINT  ["java", "-jar", "sl-es-module.jar"]
EXPOSE 8087