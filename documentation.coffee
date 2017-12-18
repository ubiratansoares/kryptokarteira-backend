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
        "label": "bta",
        "buy": 3.3182,
        "sell": 3.3176
    },
    {
        "label": "btc",
        "buy": 68999,
        "sell": 68900
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
    "owner": "5a380e30a5ee897c00004c53"
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
    "owner": "5a3084213fa720170001d05f",
    "savings": [
        {
            "label": "blr",
            "amount": 44075
        },
        {
            "label": "bta",
            "amount": 0
        },
        {
            "label": "btc",
            "amount": 1
        }
    ],
    "transactions": [
        {
            "type": "buy",
            "currency": "btc",
            "amount": 1,
            "timestamp": "2017-12-13T03:12:31.821"
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
    "currencies": [
        {
            "label": "btc",
            "name": "Bitcoin"
        },
        {
            "label": "bta",
            "name": "Brita"
        },
        {
            "label": "blr",
            "name": "Real"
        }
    ],
    "broking": [
        {
            "label": "bta",
            "buy_price": 3.3182,
            "selling_price": 3.3176
        },
        {
            "label": "btc",
            "buy_price": 68502,
            "selling_price": 68502
        }
    ],
    "wallet": {
        "owner": "5a3084213fa720170001d05f",
        "savings": [
            {
                "label": "blr",
                "amount": 44075
            },
            {
                "label": "bta",
                "amount": 0
            },
            {
                "label": "btc",
                "amount": 1
            }
        ],
        "transactions": [
            {
                "type": "buy",
                "currency": "btc",
                "amount": 1,
                "timestamp": "2017-12-13T03:12:31.821"
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

