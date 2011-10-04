package com.gu.email.http

import org.apache.commons.httpclient.{HttpConnectionManager, HttpClient, SimpleHttpConnectionManager, MultiThreadedHttpConnectionManager}

trait Http {
  //overide this if you need a different htp client
  val httpClient = new HttpClient(Http.connectionManager)
}

private object Http {
  val connectionManager =  new SimpleHttpConnectionManager(true)
}