package com.sitramobile.modelResponse

data class CustomerModel (
    var custid: String? = null,
    var name: String? = null,
    var address1: String? = null,
    var address2: String? = null,
    var city: String? = null,
    var state: String? = null,
    var pincode: String? = null,
    var email: String? = null,
    var phone: String? = null,
    var mobile: String? = null
)
/*
class CustomerModel constructor() {
    var custid: String? = null
    var name: String? = null
    var address1: String? = null
    var address2: String? = null
    var city: String? = null
    var state: String? = null
    var pincode: String? = null
    var email: String? = null
    var phone: String? = null
    var mobile: String? = null
}*/

/*[
{
    "custid": "1000054",
    "name": "Mill 1",
    "address1": "Address1",
    "address2": "Address2",
    "city": "City1",
    "state": "State1",
    "pincode": "641014",
    "email": "it@sitra.org.in",
    "phone": "4215333",
    "mobile": "9487521330"
}
]*/