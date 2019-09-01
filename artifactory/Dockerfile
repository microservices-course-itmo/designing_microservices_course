FROM docker.bintray.io/jfrog/artifactory-oss

COPY ./entrypoint.sh /entrypoint.sh
# RUN chmod a+x /entrypoint.sh

ENTRYPOINT [ "/bin/bash", "/entrypoint.sh" ]