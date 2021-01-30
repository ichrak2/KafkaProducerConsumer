import kafka.tools.ConsoleConsumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ConsumerApp {
    public static void main(String[] args) {

        Properties properties=new Properties();
        
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
        properties.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG,"1000");
        properties.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG,30000);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,"true");
        properties.put(ConsumerConfig.GROUP_ID_CONFIG,"test-group-1");

        KafkaConsumer<String,String> kafkaConsumer=new KafkaConsumer <String,String>(properties);
        kafkaConsumer.subscribe(Collections.singletonList("TestTopicTp"));
        /*creation d'un thread qui va lancer un pull a chaque seconde  */
        Executors.newScheduledThreadPool(1).scheduleAtFixedRate(()->{
            System.out.println("-------------");
            /*je veux faire un pull pour les entités qui sont produites pour les 100 derniers millisecondes */
            ConsumerRecords<String,String> consumerRecords=kafkaConsumer.poll(Duration.ofMillis(100));
            consumerRecords.forEach(cr->{
                System.out.println("key=>"+cr.key()+" value=>"+cr.value()+" offset=>"+cr.offset());
            });
        },1000,1000, TimeUnit.MILLISECONDS);

    }
}
