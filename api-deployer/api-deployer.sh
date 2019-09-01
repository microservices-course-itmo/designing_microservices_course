#!/usr/bin/env bash

set -e
set -x

API_PROJECTS="
    laundry-management-service/laundrymanagement-api
    order-management-service/ordermanagement-api
    taskcoordinator/taskcoordinator-api
"

WORKDIR="${PWD}"
TMPDIR="$(mktemp -d)"

echo "Copy project into ${TMPDIR}..." 1>&2
cp -r "${WORKDIR}/." "${TMPDIR}"

cd "${TMPDIR}"

for project in ${API_PROJECTS}; do
    cd "${project}"
    gradle build
    gradle artifactoryPublish
    cd "${TMPDIR}"   
done

cd "${WORKDIR}"

rm -rf "${TMPDIR}"