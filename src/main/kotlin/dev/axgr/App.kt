package dev.axgr

import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.scheduling.TaskScheduler
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.support.CronTrigger
import java.time.Duration
import java.util.concurrent.TimeUnit

@EnableScheduling
@SpringBootApplication
class App {

  companion object {
    private val log = LoggerFactory.getLogger(App::class.java)
  }

  @Bean
  fun run(scheduler: TaskScheduler) = CommandLineRunner {
    val one = scheduler.scheduleAtFixedRate({ log.info("Job #1 working..") }, Duration.ofSeconds(1))

    Thread.sleep(TimeUnit.SECONDS.toMillis(3))
    one.cancel(true)

    val cron = CronTrigger("* * * * * *")
    val two = scheduler.schedule({ log.info("Job #2 working..") }, cron)

    Thread.sleep(TimeUnit.SECONDS.toMillis(3))
    two?.cancel(true)

    scheduler.run { log.info("Work's done!") }
  }

}

fun main(args: Array<String>) {
  runApplication<App>(*args)
}
