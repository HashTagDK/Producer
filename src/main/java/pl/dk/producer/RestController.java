package pl.dk.producer;

import ch.qos.logback.core.CoreConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.Clock;

@org.springframework.web.bind.annotation.RestController
public class RestController {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    @Autowired
    private KafkaTemplate<String, Greeting> kafkaTemplateGreeting;

    @GetMapping("/")
    public String helloWorld(){
        return "Hello World PRODUCER";
    }

    @GetMapping("/{message}")
    public String get(@PathVariable("message")String message){
        kafkaTemplate.send("test", message);
        return message;
    }


    @GetMapping("/greeting/{name}/{message}")
    public String getGreeting(@PathVariable(name="name")String name, @PathVariable("message")String message){
        Greeting greeting = new Greeting();
        greeting.setName( name );
        greeting.setMessage( message );
        kafkaTemplateGreeting.send("greeting", greeting);
        return message;
    }

    @KafkaListener(topics = "test")
    public void listen(String message) {
        System.out.println("Received Messasge in group foo: " + message);
    }

    @KafkaListener(topics="test")
    public void listenGreeting( Greeting greeting ){
        System.out.println( greeting.getName() + "-" + greeting.getMessage() );
    }
}
