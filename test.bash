#!/bin/bash

SUCCESS="Success"
#ITEM
INSERT=$(curl -H "Content-Type: application/json" -X POST -d '{"name":"maibock","description":"May Bock","price":4.3}' http://localhost:9000/item)
wait
echo $INSERT

RETRIEVE=$(curl http://localhost:9000/item/$INSERT)
wait
echo $RETRIEVE
RET=${RETRIEVE:0:6}
echo $RET
RECORDNOTFOUND="Record"

if [ $RET = $RECORDNOTFOUND ]; then
	echo "Item INSERT FAILED"
	exit 1
else 
	echo "Item INSERT WORKED RECORD $INSERT"
	echo "Item Retrieve Worked $RETRIEVE"
fi

UPDATE=$(curl  -X PUT http://localhost:9000/item/$INSERT/LIGHT/Honey/4.3)
echo $UPDATE
if [ $UPDATE = $SUCCESS ]; then
	echo "Item UPDATE WORKED"
else
	echo "Item UPDATE FAILED"
	exit 1
fi

DELETE=$(curl -X "DELETE" http://localhost:9000/item/$INSERT)
echo $DELETE


if [ $DELETE = $SUCCESS ]; then
	echo "Item Delete Success"
else
	echo "Item Delete Failed"
	echo $DELETE
	exit 1
fi

RETREIVEALL=$(curl http://localhost:9000/item)
echo $RETREIVEALL

#CUSTOMER
INSERT=$(curl -H "Content-Type: application/json" -X POST -d '{"name":"Don Kagle","email":"dkagle@cox.net","pword":"word"}' http://localhost:9000/customer)
wait
echo $INSERT

RETRIEVE=$(curl http://localhost:9000/customer/$INSERT)
wait
echo $RETRIEVE
RET=${RETRIEVE:0:6}
echo $RET
RECORDNOTFOUND="Record"

if [ $RET = $RECORDNOTFOUND ]; then
	echo "Customer INSERT FAILED"
	exit 1
else 
	echo "Customer INSERT WORKED RECORD $INSERT"
	echo "Customer Retrieve Worked $RETRIEVE"
fi

UPDATE=$(curl  -X PUT http://localhost:9000/customer/$INSERT/Allen%20Kagle/allen@cox.net/password)
if [ $UPDATE = $SUCCESS ]; then
	echo "Customer UPDATE WORKED"
else
	echo "Customer UPDATE FAILED"
	exit 1
fi

DELETE=$(curl -X "DELETE" http://localhost:9000/customer/$INSERT)
echo $DELETE
SUCCESS="Success"

if [ $DELETE = $SUCCESS ]; then
	echo "Customer Delete Success"
else
	echo "Customer Delete Failed"
	echo $DELETE
	exit 1
fi

RETREIVEALL=$(curl http://localhost:9000/customer)
echo $RETREIVEALL



#TRANSACTION
INSERT=$(curl -H "Content-Type: application/json" -X POST -d '{"item_id":1,"datetime":"2016-04-27 12:51:01"}' http://localhost:9000/transaction)
wait
echo $INSERT

RETRIEVE=$(curl http://localhost:9000/transaction/$INSERT)
wait
echo $RETRIEVE
RET=${RETRIEVE:0:6}
echo $RET
RECORDNOTFOUND="Record"

if [ $RET = $RECORDNOTFOUND ]; then
	echo "Transaction INSERT FAILED"
	exit 1
else 
	echo "Transaction INSERT WORKED RECORD $INSERT"
	echo "Transaction Retrieve Worked $RETRIEVE"
fi

UPDATE=$(curl  -X PUT http://localhost:9000/transaction/$INSERT/2/2016-04-15%2018:01:04)
if [ $UPDATE = $SUCCESS ]; then
	echo "Transaction UPDATE WORKED"
else
	echo "Transaction UPDATE FAILED"
	exit 1
fi

DELETE=$(curl -X "DELETE" http://localhost:9000/transaction/$INSERT)
echo $DELETE
SUCCESS="Success"

if [ $DELETE = $SUCCESS ]; then
	echo "Transaction Delete Success"
else
	echo "Transaction Delete Failed"
	echo $DELETE
	exit 1
fi

RETREIVEALL=$(curl http://localhost:9000/transaction)
echo $RETREIVEALL


#ADDRESS
INSERT=$(curl -H "Content-Type: application/json" -X POST -d '{"customerid":1,"address1":"6524%20Barnesdale%20Path","address2":"suite%20 201","city":"Centreville","state":"VA","zip":"20120"}' http://localhost:9000/address)
wait
echo $INSERT

RETRIEVE=$(curl http://localhost:9000/address/$INSERT)
wait
echo $RETRIEVE
RET=${RETRIEVE:0:6}
echo $RET
RECORDNOTFOUND="Record"

if [ $RET = $RECORDNOTFOUND ]; then
	echo "Address INSERT FAILED"
	exit 1
else 
	echo "Address INSERT WORKED RECORD $INSERT"
	echo "Address Retrieve Worked $RETRIEVE"
fi

UPDATE=$(curl -X PUT http://localhost:9000/address/$INSERT/1/111%20Herndon%20Pwy/Suite%20105/Herndon/VA/22079)
echo $UPDATE

if [ $UPDATE = $SUCCESS ]; then
	echo "Address UPDATE WORKED"
else
	echo "Address UPDATE FAILED"
	exit 1
fi


DELETE=$(curl -X "DELETE" http://localhost:9000/address/$INSERT)
echo $DELETE
SUCCESS="Success"

if [ $DELETE = $SUCCESS ]; then
	echo "Address Delete Success"
else
	echo "Address Delete Failed"
	echo $DELETE
	exit 1
fi

RETREIVEALL=$(curl http://localhost:9000/address)
echo $RETREIVEALL

