FROM gradle:5.4.1

COPY ./api-deployer.sh /api-deployer.sh
COPY ./config.yml /artifactory.yml
COPY ./repo-setup.sh /setup.sh

RUN chmod +x /api-deployer.sh /setup.sh

RUN apt-get update && apt-get install -y vim

VOLUME [ "/project" ]

WORKDIR /project
