package com.gu.email.http

import org.apache.commons.httpclient.{HttpConnectionManager, HttpClient, SimpleHttpConnectionManager, MultiThreadedHttpConnectionManager}

trait Http {
  val connectionManager: HttpConnectionManager =  Http.connectionManager
  val httpClient = new HttpClient(connectionManager)
}

object Http {
  val connectionManager =  new SimpleHttpConnectionManager(true)
}