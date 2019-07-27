# Order service

## Overview
Order service is responsible for storing orders and performing base opertaions on this. 

## REST API description

Name | Path | Request parameters | Path parameters | Body parameter | Return value | Description
-----|------|--------------------|-----------------|----------------|--------------|------------
Get order | /order/{orderId} | | *order id* | | OrderDto | Getting order current state by it's id |
Add detail | /order/detail/ | | | AddDetailDto | OrderDto | Bind given detail to existing order or create a new one if doesn't exists |
Assign tariff | /order/tariff | | | AssignTariffDto | OrderDto | Assign tariff information to order |

## Outgoing events