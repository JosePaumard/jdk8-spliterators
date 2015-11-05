Examples of `Spliterator` from my talk at JavaOne 2015 and Devoxx 2015. You can find the slides on my Slideshare account: [http://www.slideshare.net/jpaumard](http://www.slideshare.net/jpaumard).

There are four spliterators shown here:
- the `GroupingSpliterator`, that reads a stream and regroup its elements with a grouping factor.
- the `RollingSpliterator`, that does basically the same, but starts at each element of the stream.
- the `ZippingSpliteraror`, that merges two streams, possibly of different types, using a bifunction.
- the `WeavingSpliterator`, that merges several streams, taking one element of each at a time.

Examples of using these spliterators are also provided.