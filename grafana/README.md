# Grafana
Grafana is a tool for analytics and monitoring. It works with various data storages such as Graphite, Elasticsearch etc.
# Grafana concepts
Grafana uses panels to visualize the data by querying the data source. A panel can be a graph, table, gauge, single stat etc.
Panels can be placed in rows to organize them in a dashboard. A dashboard contains rows, panels, variables and options 
(basically, it's a web page where you can see all your graphs, tables and other visualizations). 

Dashboard variables have several types - the most useful is query type. The value(s) are queried from the data source, so,
for example, it is possible to get all application instance names and then choose current value from the list to show panels for 
selected instance. Variables can be used in queries, graph legends and even in other variables definition (probably in any text field).
The variable can be updated on time range change, on dashboard load or never. Also, it is updated if some other variable that 
is used to define the current one changes. Moreover, panels and rows can be repeated for every selected value of some variable
(for example, for each instance or each endpoint)

Users may have a role - Admin, Editor or Viewer. Admin can add, edit and delete datasources, dashboards, invite new users.
Editor is able only to add, edit and delete dashboards, while viewer is limited to watching dashboards (but it can be hidden
from viewers)

Dashboards and datasources can be externalized by provisioning. Grafana updates them on startup via config files
(dashboard providers and datasource files) and dashboard json models.

# Mounting volume with provisioned dashboards
On systems where docker is not supported natively (where virtualbox is used) only some directories can be mounted (users 
directory and it's subdirectories on windows) by default. To mount other directories you need to configure shared directories
for docker vm. To do so, open vm settings, click shared folders and add one. Specify folder and name it like it's full path, 
but with the disk name as a folder of the root directory (e.g. D:\{path_to_directory} will become /d/{path_to_directory})
and backslash changed to a regular slash.