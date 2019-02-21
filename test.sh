#!/usr/bin/env bash

URL="127.0.0.1:8080"

curl "$URL/users" -X POST -H "Content-Type: application/json" -d '{
  "publicData": {
    "userId": "1",
    "userImage": "string",
    "userName": "alice"
  },
  "privateData": {
    "email": "string"
  }
}'
echo

curl "$URL/users" -X POST -H "Content-Type: application/json" -d '{
  "publicData": {
    "userId": "2",
    "userImage": "string",
    "userName": "bob"
  },
  "privateData": {
    "email": "string"
  }
}'

curl "$URL/users" -X POST -H "Content-Type: application/json" -d '{
  "publicData": {
    "userId": "3",
    "userImage": "string",
    "userName": "charles"
  },
  "privateData": {
    "email": "string"
  }
}'
echo

curl -X PUT "$URL/follows/1/2"
curl -X PUT "$URL/follows/1/3"

curl -X POST "$URL/messages" -H "Content-Type: application/json" -d '
{
  "messageBody": "1st",
  "messageId": "string",
  "postDate": 0,
  "postingUserId": "1"
}'

curl -X POST "$URL/messages" -H "Content-Type: application/json" -d '
{
  "messageBody": "2nd",
  "messageId": "string",
  "postDate": 0,
  "postingUserId": "3"
}'

curl -X POST "$URL/messages" -H "Content-Type: application/json" -d '
{
  "messageBody": "3rd",
  "messageId": "string",
  "postDate": 0,
  "postingUserId": "2"
}'

curl -X POST "$URL/messages" -H "Content-Type: application/json" -d '
{
  "messageBody": "4th",
  "messageId": "string",
  "postDate": 0,
  "postingUserId": "1"
}'

curl -X POST "$URL/messages" -H "Content-Type: application/json" -d '
{
  "messageBody": "5th",
  "messageId": "string",
  "postDate": 0,
  "postingUserId": "3"
}'

curl "$URL/timeline?userId=1"
