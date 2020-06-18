FROM oracle/graalvm-ce:20.1.0-java11 AS compiler

RUN curl https://bintray.com/sbt/rpm/rpm | tee /etc/yum.repos.d/bintray-sbt-rpm.repo \
 && yum install -y sbt \
 && gu install native-image \
 && curl -L https://github.com/gradinac/musl-bundle-example/releases/download/v1.0/musl.tar.gz | tar -xz

COPY . /app
WORKDIR /app

RUN sbt assembly
RUN native-image --no-server --static -H:+ReportExceptionStackTraces -H:UseMuslC="/bundle" --allow-incomplete-classpath --no-fallback --initialize-at-build-time --enable-http --enable-https --enable-all-security-services --verbose -jar "./target/scala-2.13/voice-server-assembly-0.0.1-SNAPSHOT.jar" voice-server

FROM andrewts129/festival-base-image:latest

COPY --from=compiler /app/voice-server /usr/local/bin/voice-server

EXPOSE 3000
CMD voice-server
