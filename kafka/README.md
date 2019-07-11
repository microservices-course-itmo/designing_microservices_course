# Setting up kafka
Install docker-compose and use docker-compose up command in the root directory
## Notes for Windows users
Docker for Windows works only on Windows 10 Pro, Enterprise or Education editions. Docker toolbox can be used on other systems. 
It uses virtualbox to run a linux virtual machine, which has it's own ip. In order to be able to connect to the containers via localhost, 
you'll have to forward ports using virtualbox. To do this, open virtual machine settings (it is named "default" by default), navigate to 
network, NAT adapter (it will be first), advanced, port forwarding. Then click on add button (it's located near the top right corner) and 
name your port forwarding rule as you want. Host address should be 127.0.0.1, host port - 9092 (guest port is also 9092). Guest address
should be left empty.
