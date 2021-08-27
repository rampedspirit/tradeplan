FROM postgres:13.4

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

# Install OpenJDK-11
RUN apt-get update && \
    apt-get install -y openjdk-11-jre && \
    apt-get clean;

# Setup JAVA_HOME -- useful for docker commandline
ENV JAVA_HOME /usr/lib/jvm/java-11-openjdk-amd64/
RUN export JAVA_HOME

# Copy health monitoring app
COPY target/*.jar app.jar
COPY run_health_monitor_app.sh /docker-entrypoint-initdb.d/run_health_monitor_app.sh
