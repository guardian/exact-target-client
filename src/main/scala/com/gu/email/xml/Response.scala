package com.gu.email.xml

case class Response[ResponseObjectType](status: String, data: ResponseObjectType)
