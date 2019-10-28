#!/usr/bin/env bash
# XXX: Fix up permissions on volume in Windows(Hyper-V) and Linux
chown -R artifactory:artifactory /var/opt/jfrog/artifactory

# Return from root under expected user
exec su artifactory /entrypoint-artifactory.sh
