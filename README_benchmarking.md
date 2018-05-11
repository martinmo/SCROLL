# SCROLL benchmarks

All benchmarks use the [Java Microbenchmarking Harness (JMH)][jmh]. The benchmarks can be
invoked in two ways:

* by using the fat jar `benchmarks.jar` directly, or
* by using an interactive `sbt` session.

Using the "fat jar" approach has many advantages: first of all, you don't need to waste time
on waiting for `sbt` to start. Second, you can more easily switch between Java versions. Third,
and most importantly, you can pass JMH options without `sbt` getting in your way.

[jmh]: http://openjdk.java.net/projects/code-tools/jmh/


## Using the fat jar (recommended)

First of all, ensure you've built the JAR by running:

    $ sbt benchmark/jmh:compile benchmark/jmh:assembly

It should create an executable JAR under `benchmark/target/scala-1.x/benchmarks.jar`.
Next, you can either run it directly:

    $ java -jar benchmark/target/scala-1.x/benchmarks.jar ...

Or even better, use the provided starter script, which ensures correct language settings
and module access for Java versions >= 9:

    $ ./run_benchmark.sh ...

(In particular, the script sets an english locale, in order to have a consistent number
format in all produced outputs. For example, `1.234` instead of `1,234` on stdout and
in generated CSV files.)

The ellipsis `...` stands for arbitrary JMH options, such as:

    $ ./run_benchmark.sh -h                 # to get help
    $ ./run_benchmark.sh -l                 # to get a list of benchmarks
    $ ./run_benchmark.sh -prof stack Noop   # run all benchmarks matching the pattern "Noop",
                                            # plus stack profiling


## Using sbt

The benchmarks can also be started from inside an interactive `sbt` session:

    $ sbt

Then, switch to the *benchmark* project:

    > project benchmark

And now you can use the usual JMH commands and/or options, for example:

    > jmh:run -h                 # to get help
    > jmh:run -l                 # to get a list of benchmarks
    > jmh:run -prof stack Noop   # run all benchmarks matching pattern "Noop", plus stack profiling
