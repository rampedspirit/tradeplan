FROM postgres:13.3

ARG USER_NAME
ARG PASSWORD
ARG DB_NAME=appdb

# Postgres ENV
ENV POSTGRES_USER=${USER_NAME} 
ENV POSTGRES_PASSWORD=${PASSWORD}
ENV POSTGRES_DB=${DB_NAME}

# Health Monitoring App ENV
ENV DB_HOST=localhost
ENV DB_NAME=${DB_NAME}
ENV DB_USER_NAME=${USER_NAME}
ENV DB_PASSWORD=${PASSWORD}

# Install OpenJDK-8
RUN add-apt-repository ppa:openjdk-r/ppa

RUN apt-get update && \
    apt-get install -y openjdk-8-jdk && \
    apt-get clean;

# Setup JAVA_HOME -- useful for docker commandline
ENV JAVA_HOME /usr/lib/jvm/java-8-openjdk-amd64/
RUN export JAVA_HOME

# Run health monitoring app
COPY target/*.jar app.jar
CMD "java -jar app.jar"