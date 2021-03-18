package com.dhagz.githubusers._remote

class ApiCallException(httpStatusCode: Int, message: String) :
  Exception("HTTP Status Code: $httpStatusCode | Message: $message")