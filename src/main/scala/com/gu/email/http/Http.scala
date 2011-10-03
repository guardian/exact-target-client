package com.gu.email.http

import org.apache.commons.httpclient.{HttpConnectionManager, HttpClient, SimpleHttpConnectionManager, MultiThreadedHttpConnectionManager}

trait Http {
  //override this in you code with a more funky connection manager
  val connectionManager: HttpConnectionManager =  Http.connectionManager
  val httpClient = new HttpClient(connectionManager)
}

private object Http {
  val connectionManager =  new SimpleHttpConnectionManager(true)
}