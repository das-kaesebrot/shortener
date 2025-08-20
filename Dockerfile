FROM docker.io/gradle:9-jdk21 AS build

ARG OUT_DIR=/srv/final

COPY . .
RUN gradle clean bootJar && \
    mkdir -pv ${OUT_DIR} && \
    mv -v build/libs/*.jar ${OUT_DIR}/app.jar

FROM docker.io/eclipse-temurin:21.0.7_6-jre AS app

ARG APP_UNIX_USER=shortener

# Create user and group to run as
RUN useradd --system ${APP_UNIX_USER}

ARG SPRING_FOLDER=/var/opt/shortener

# Create settings and logs folder
RUN mkdir -pv ${SPRING_FOLDER}/logs && \
    mkdir -pv ${SPRING_FOLDER}/config

ENV SPRING_PROFILES_ACTIVE=prod

# Set proper perms
RUN chown -R ${APP_UNIX_USER}:${APP_UNIX_USER} ${SPRING_FOLDER}

# Set run user and group
USER ${APP_UNIX_USER}:${APP_UNIX_USER}

# Copy over compiled jar
ARG JAR_PATH=/srv/final/app.jar

WORKDIR ${SPRING_FOLDER}
COPY --from=build ${JAR_PATH} app.jar

RUN if [ ! -f "app.jar" ]; then exit 1; fi

COPY docker/entrypoint.sh .

CMD ["./entrypoint.sh"]
