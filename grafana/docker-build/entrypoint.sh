#!/bin/bash -e

mkdir -p /etc/grafana/provisioning/dashboards/tc-dashboard

envsubst '${metrics_publish_rate}' \
< /etc/grafana/provisioning/dashboards/tc-dashboard-templates/order-metrics.json \
> /etc/grafana/provisioning/dashboards/tc-dashboard/order-metrics.json

envsubst '${metrics_publish_rate}' \
< /etc/grafana/provisioning/dashboards/tc-dashboard-templates/tc-dashboard.json \
> /etc/grafana/provisioning/dashboards/tc-dashboard/tc-dashboard.json

exec /run.sh