#!/usr/bin/env bash
# XXX: Fix up permissions on volume in Windows(Hyper-V)
chown -R artifact:artifact /var/opt/jfrog/artifactory

exec /entrypoint-artifactory.sh