###
@api {get} /v1/broker
@apiName GetBroker
@apiGroup Broker
@apiDescription Retrieve lastest prices for cryptocurrencies

@apiSuccessExample Success-Response:
HTTP/1.1 200 OK
Content-Type: application/json
Transfer-Encoding: chunked

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
@api {get} /v1/wallet/new
@apiName GetNewWallet
@apiGroup Wallet
@apiDescription Retrieve a new wallet with an initial giveway from KryptoKarteira

@apiSuccessExample Success-Response:
HTTP/1.1 200 OK
Content-Type: application/json
Transfer-Encoding: chunked

{
    "owner": "5a300f083fa720170001ce56",
    "savings": [
        {
            "name": "Real",
            "amount": 100000
        },
        {
            "name": "Brita",
            "amount": 0
        },
        {
            "name": "Bitcoin",
            "amount": 0
        }
    ],
    "transactions": []
}
###

###
@api {get} /v1/wallet/:owner/details
@apiName WalletDetails
@apiGroup Wallet
@apiDescription Retrieve a wallet for a given owner

@apiSuccessExample Success-Response:
HTTP/1.1 200 OK
Content-Type: application/json
Transfer-Encoding: chunked

{
    "owner": "5a3009883fa720170001ce55",
    "savings": [
        {
            "name": "Real",
            "amount": 43932.65
        },
        {
            "name": "Brita",
            "amount": 86
        },
        {
            "name": "Bitcoin",
            "amount": 1
        }
    ],
    "transactions": [
        {
            "type": "sell",
            "currency": "bta",
            "amount": 6,
            "timestamp": "2017-12-12T15:17:27.75"
        },
        {
            "type": "buy",
            "currency": "bta",
            "amount": 100,
            "timestamp": "2017-12-12T14:55:58.139"
        },
        {
            "type": "buy",
            "currency": "btc",
            "amount": 1,
            "timestamp": "2017-12-12T14:55:21.739"
        }
    ]
}
###

###
@api {get} /v1/home/:owner/info
@apiName GetHome
@apiGroup Home
@apiDescription Retrieve all information needed for the home screen, including broking, wallet and earns scenarios

@apiSuccessExample Success-Response:
HTTP/1.1 200 OK
Content-Type: application/json
Transfer-Encoding: chunked

{
    "broking": [
        {
            "label": "BTA",
            "name": "Brita",
            "buy_price": 3.2845,
            "selling_price": 3.2839,
            "real_money_value": 282.4154
        },
        {
            "label": "BTC",
            "name": "Bitcoin",
            "buy_price": 56400,
            "selling_price": 56150,
            "real_money_value": 56150
        }
    ],
    "wallet": {
        "owner": "5a3009883fa720170001ce55",
        "savings": [
            {
                "name": "Real",
                "amount": 43932.65
            },
            {
                "name": "Brita",
                "amount": 86
            },
            {
                "name": "Bitcoin",
                "amount": 1
            }
        ],
        "transactions": [
            {
                "type": "sell",
                "currency": "bta",
                "amount": 6,
                "timestamp": "2017-12-12T15:17:27.75"
            },
            {
                "type": "sell",
                "currency": "bta",
                "amount": 8,
                "timestamp": "2017-12-12T14:56:26.823"
            },
            {
                "type": "buy",
                "currency": "bta",
                "amount": 100,
                "timestamp": "2017-12-12T14:55:58.139"
            },
            {
                "type": "buy",
                "currency": "btc",
                "amount": 1,
                "timestamp": "2017-12-12T14:55:21.739"
            }
        ]
    }
}
###

###
@api {post} /v1/wallet/:owner/transaction
@apiName PostTransaction
@apiGroup Wallet
@apiDescription Retrieve lastest prices for cryptocurrencies
@apiParam {String} type Type for transaction (buy|sell)
@apiParam {String} currency Label for crypto currency (bta|btc)
@apiParam {Number} amount Quantity involved at this transaction
@apiError (400) BadRequest Provided json body does not follow the desired contract
@apiError (409) Conflict Cannot perform this transaction

@apiSuccessExample Success-Response:
HTTP/1.1 201 OK
Content-Type: application/json
Transfer-Encoding: chunked

{
    "message": "Success!"
}
###

###
@api {verb} /v1/some/resource
@apiGroup #Errors
@apiDescription Errors supported by resources of this API

@apiError (401) Unauthorized Invalid or missing API credential
@apiError (404) NotFound Invalid resource address or content for valid resource not found
@apiError (500) InternalServerError Usually a contract with an internal system is broken
@apiError (502) BadGateway Some internal system is unavailable
@apiError (503) ServiceUnavailable Integration error with an internal system
@apiError (504) GatewayTimeout Received a timeout from an internal system

@apiErrorExample Response (example):
HTTP/1.1 401 Not Authenticated
Content-Type: application/json
Transfer-Encoding: chunked

{
    "message": "Invalid or missing API credentials"
}

###

