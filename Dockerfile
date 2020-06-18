FROM hseeberger/scala-sbt:graalvm-ce-20.0.0-java11_1.3.12_2.13.2 AS compiler

RUN gu install native-image \
 && curl -L https://github.com/gradinac/musl-bundle-example/releases/download/v1.0/musl.tar.gz | tar -xz

COPY . /app
WORKDIR /app

RUN sbt assembly
RUN native-image --no-server --static -H:+ReportExceptionStackTraces -H:UseMuslC="/bundle" --allow-incomplete-classpath --no-fallback --initialize-at-build-time --enable-http --enable-https --enable-all-security-services --verbose -jar "./target/scala-2.13/voice-server-assembly-0.0.1-SNAPSHOT.jar" voice-server

FROM andrewts129/festival-base-image:latest

RUN apt update && apt install -y lame

COPY --from=compiler /app/voice-server /usr/local/bin/voice-server

EXPOSE 3000
CMD voice-server
