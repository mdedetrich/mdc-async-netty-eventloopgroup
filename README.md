# MDC Async Netty EventLoopGroup

This library provides an implementation of netty's `io.netty.channel.EventLoopGroup` that
allows you to delegate an existing `io.netty.channel.EventLoopGroup` to provide a slf4j
MDC context that can properly propagate in Netty's asynchronous environments.

The primary use case of this was to make [Async Http Client](https://github.com/AsyncHttpClient/async-http-client)
working with slf4j MDC

## Installation

The library is dual versioned against Netty release cycle, so version `0.1_4.0` signifies
version `0.1` of MDC Async Netty EventLoopGroup built against Netty version `4.0.x`.

The dependency for the current version is

```xml
<dependency>
  <groupId>org.mdedetrich</groupId>
  <artifactId>mdc-async-netty-eventloopgroup</artifactId>
  <version>0.1_4.0</version>
</dependency>
```

## Usage

There is a single method that is provided which creates the `io.netty.channel.EventLoopGroup`
from an existing one

```java
import io.netty.channel.*;
import org.asynchttpclient.*;

public class AsyncHttpExample {
    EventLoopGroup eventLoopGroup = new DefaultAsyncHttpClientConfig();
    EventLoopGroup eventLoopGroupWithMDC = MDCEventLoopGroup.fromThread(eventLoopGroup);
    
    // Now we can provide the new EventLoopGroup
    Builder asyncHttpClientBuilder = new DefaultAsyncHttpClientConfig.Builder();
    asyncHttpClientBuilder.setEventLoopGroup(eventLoopGroupWithMDC);
}

```