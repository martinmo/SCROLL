package scroll.benchmarks

import java.util.concurrent.TimeUnit

import org.openjdk.jmh.annotations._

@BenchmarkMode(Array(Mode.AverageTime))
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 20)
@Measurement(iterations = 10)
@Fork(2)
@State(Scope.Benchmark)
abstract class AbstractBenchmark