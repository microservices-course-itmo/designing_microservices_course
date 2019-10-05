FROM docker.bintray.io/jfrog/artifactory-oss

COPY ./entrypoint.sh /entrypoint.sh

ENTRYPOINT [ "/bin/bash", "/entrypoint.sh" ]

# Required for chown
USER "root"
