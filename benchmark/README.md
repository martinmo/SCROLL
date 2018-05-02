# SCROLL benchmarks

All benchmarks use the [Java Microbenchmarking Harness (JMH)][jmh]. JMH is completely controlled
with `sbt` and started with an interactive `sbt` session:

    $ sbt

Then, switch to the *benchmark* project:

    > project benchmark

And now you can use the usual JMH commands and/or options, for example:

    > jmh:run -h                 # to get help
    > jmh:run -l                 # to get a list of benchmarks
    > jmh:run -prof stack Noop   # run all benchmarks matching pattern "Noop", plus stack profiling

[jmh]: http://openjdk.java.net/projects/code-tools/jmh/
