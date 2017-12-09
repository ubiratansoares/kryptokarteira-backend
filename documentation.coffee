###
@api {get} /broker
@apiGroup Broker
@apiName GetBroker
@apiDescription Retrieve lastest prices for cryptocurrencies

@apiExample Example usage:
curl -i http://localhost/broker

@apiSuccessExample Success-Response:
HTTP/1.1 200 OK
  [
      {
          "label": "BTA",
          "name": "Brita",
          "value": 3.1138
      },
      {
          "label": "BTC",
          "name": "Bitcoin",
          "value": 54600
      }
  ]

###

###
@api {verb} /some/resource
@apiGroup Errors
@apiName VerbErrors
@apiDescription Errors supported by resources of this API

@apiError (401) Unauthorized Invalid or missing X-API-KEY
@apiError (500) InternalServerError Usually a contract with an internal system is broken
@apiError (502) BadGateway Some internal system is unavailable
@apiError (503) ServiceUnavailable Integration error with an internal system
@apiError (504) GatewayTimeout Received a timeout from an internal system

@apiErrorExample Response (example):
HTTP/1.1 401 Not Authenticated
 {
      "message": "Not Authenticated"
 }

###
