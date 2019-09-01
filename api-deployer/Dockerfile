FROM gradle:5.4.1

COPY ./api-deployer.sh /api-deployer.sh

RUN apt-get update && apt-get install -y vim

VOLUME [ "/project" ]

WORKDIR /project
