#!/usr/bin/env bash

curl --user "${ORG_GRADLE_PROJECT_artifactoryUser}:${ORG_GRADLE_PROJECT_artifactoryPassword}" \
    --request PATCH \
    --header "Content-Type:application/yaml" \
    --upload-file /artifactory.yml \
    "${ORG_GRADLE_PROJECT_artifactoryUrl}/api/system/configuration"
