# taxiBooking

A RESTful API that forms the backend system for managing an on demand cab service. Finding available cabs, booking the nearest, billing and managing inventory are some of the features.

It works on in memory maps which can be replaced by adapters that talk to a persistent data store.
All APIs are thread safe!

There are 2 kinds of cabs - plain and pink

Riders can opt for pink cabs specifically. 
Any cab can be assigned if no specifications are given at the time of booking

Start and End points are co-ordinates in 2D space in second quadrant (x - latitude ; y - longitude) 

APIs for Rider's request :-
----------------------------

###Request a booking 

>__URI__ : api/cab/assign/[latitude]/[longitude]?pink=[true|false]
>
>__Method__ : GET
>
>__Params__
>
>*latitude, longitude* - co-ordinates in decimels (eg.23.7)
>
>*pink* - optional Query param (default: false)
>
>__Response__
>
    {"bookingId":"FUB-0009121"}

###Complete a Trip and fetch bill

>__URI__ : api/trip/end/[booking-id]/[latitude]/[longitude]
>
>__Method__ : GET
>
>__Params__
>
>*latitude, longitude* - final co-ordinates in decimels (eg.23.7)
>
>*booking-id* - received previously while booking
>
>__Response__
>
    {
    	"amount": 212.50422227383444,
    	"bookingDetails": {
    		"id": "FUB-000000000001112",
    		"initialCabLocation": {
    			"cab": {
    				"id": "iiqwo11",
    				"licensePlate": "NA88912",
    				"pink": false
    			},
    			"coordinate": {
    				"x": 26.7,
    				"y": 50.1
    			}
    		},
    		"tripComplete": true,
    		"tripStartEpoch": 1460799199535
    	},
    	"distanceInKms": 106.1452778035839,
    	"durationInMinutes": 0.21366666666666667,
    	"invoiceId": "CB-000000000002223"
    }

APIs for Admin :-
------------------

###Add a new Cab to fleet

>__URI__ : api/cab/add/[latitude]/[longitude]
>
>__Method__ : POST
>
>__Params__
>
>*latitude, longitude* - co-ordinates in decimels (eg.23.7)
>
>__Request Json__
>
    {
      "id" : "iiqwo11",
      "licensePlate" : "NA88912",
      "pink" : "false"
    }
>
>__Response__
>
    {
      "status": "Added"
    }

###Get info on all available cabs

>__URI__ : api/cab/available
>
>__Method__ : GET
>
>__Response__
>
    [
      {
        "cab": {
            "id": "iiqwo11",
            "licensePlate": "NA88912",
            "pink": false
        },
        "coordinate": {
            "x": 29.8,
            "y": 156.2
        }
      }
    ]

###Get info on all bookings

>__URI__ : api/bookings
>
>__Method__ : GET
>
>__Response__
>
    [
      {
        "id": "FUB-000000000001111",
        "initialCabLocation": {
            "cab": {
                "id": "iiqwo11",
                "licensePlate": "NA88912",
                "pink": false
            },
            "coordinate": {
                "x": 26.7,
                "y": 50.1
            }
        },
        "tripComplete": false,
        "tripStartEpoch": 1460798747130
      }
    ]

###Get info on all bills generated

>__URI__ : api/bookings/bills
>
>__Method__ : GET
>
>__Response__
>
    {
      "amount": 212.50422227383444,
      "bookingDetails": {
          "id": "FUB-000000000001112",
          "initialCabLocation": {
              "cab": {
                  "id": "iiqwo11",
                  "licensePlate": "NA88912",
                  "pink": false
              },
              "coordinate": {
                  "x": 26.7,
                  "y": 50.1
              }
          },
          "tripComplete": true,
          "tripStartEpoch": 1460799199535
      },
      "distanceInKms": 106.1452778035839,
      "durationInMinutes": 0.21366666666666667,
      "invoiceId": "CB-000000000002223"
    }



